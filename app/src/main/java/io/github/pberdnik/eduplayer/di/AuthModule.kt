package io.github.pberdnik.eduplayer.di

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


private val SCOPES = listOf(YouTubeScopes.YOUTUBE_READONLY)


@Module
object AuthModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideGoogleAccountCredential(applicationContext: Context): GoogleAccountCredential =
        GoogleAccountCredential.usingOAuth2(applicationContext, SCOPES)
            .setBackOff(ExponentialBackOff())
}