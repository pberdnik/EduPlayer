package io.github.pberdnik.eduplayer

import android.app.Application
import io.github.pberdnik.eduplayer.di.DaggerAppComponent
import io.github.pberdnik.eduplayer.di.InjectorProvider
import timber.log.Timber

class EduPlayerApplication : Application(), InjectorProvider {

    override val component by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        setupLogger()
    }

    private fun setupLogger() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }


}