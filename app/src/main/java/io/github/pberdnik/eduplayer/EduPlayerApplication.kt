package io.github.pberdnik.eduplayer

import android.app.Application
import io.github.pberdnik.eduplayer.di.DaggerAppComponent
import io.github.pberdnik.eduplayer.di.InjectorProvider

class EduPlayerApplication : Application(), InjectorProvider {

    override val component by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}