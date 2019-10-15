package io.github.pberdnik.eduplayer.di

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import io.github.pberdnik.eduplayer.BuildConfig
import io.github.pberdnik.eduplayer.network.YoutubeDataApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.Date
import javax.inject.Singleton

@Module
object NetworkModule {

    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    @Singleton
    @JvmStatic
    @Provides
    fun provideOkHttpClient(credential: GoogleAccountCredential): OkHttpClient {
        /** Adds apiKey and auth query parameter to each request */
        val apiKeyAndAuthInterceptor = { chain: Interceptor.Chain ->
            val urlBuilder = chain.request().url.newBuilder()
                .addQueryParameter("key", BuildConfig.YouTubeApiKey)

            if (credential.selectedAccountName != null) try {
                urlBuilder.addQueryParameter("access_token", credential.token)
            } catch (e: Exception) {
                Timber.e("Selected account name is ${credential.selectedAccountName} but no accessToken")
            }

            val url = urlBuilder.build()
            val request = chain.request().newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }

        /** Shows full request data (headers and body) in logs */
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyAndAuthInterceptor)
            .build()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideMoshi() = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .build()

    @Singleton
    @JvmStatic
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @JvmStatic
    @Provides
    fun provideYoutubeDataApiService(retrofit: Retrofit): YoutubeDataApiService =
        retrofit.create(YoutubeDataApiService::class.java)
}