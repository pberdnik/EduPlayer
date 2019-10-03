package io.github.pberdnik.eduplayer.di

import android.content.Context
import android.content.SharedPreferences
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes
import dagger.Module
import dagger.Provides
import io.github.pberdnik.eduplayer.features.account.PREF_ACCOUNT_NAME
import javax.inject.Singleton


private val SCOPES = listOf(YouTubeScopes.YOUTUBE_READONLY)


@Module
object AuthModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideGoogleAccountCredential(
        applicationContext: Context,
        sharedPreferences: SharedPreferences
    ): GoogleAccountCredential {
        val credential = GoogleAccountCredential.usingOAuth2(applicationContext, SCOPES)
            .setBackOff(ExponentialBackOff())
        val accountName = sharedPreferences.getString(PREF_ACCOUNT_NAME, null)
        if (accountName != null) credential.selectedAccountName = accountName
        return credential
    }
}