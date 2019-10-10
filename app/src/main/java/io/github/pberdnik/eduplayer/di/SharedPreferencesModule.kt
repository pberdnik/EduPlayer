package io.github.pberdnik.eduplayer.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.github.pberdnik.eduplayer.R

@Module
object SharedPreferencesModule {

    @JvmStatic
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            context.getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        )

    @JvmStatic
    @Provides
    fun provideContentResolver(context: Context): ContentResolver = context.contentResolver
}
