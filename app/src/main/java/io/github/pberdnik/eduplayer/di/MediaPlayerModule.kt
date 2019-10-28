package io.github.pberdnik.eduplayer.di

import android.content.Context
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.pberdnik.eduplayer.R

@Module
object MediaPlayerModule {

    @JvmStatic
    @Provides
    @Reusable
    fun provideMediaSourceFactory(context: Context): ProgressiveMediaSource.Factory =
        ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, context.getString(R.string.app_name))
            )
        )
}