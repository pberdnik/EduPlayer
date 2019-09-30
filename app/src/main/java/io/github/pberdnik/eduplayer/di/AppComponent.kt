package io.github.pberdnik.eduplayer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        AssistedInjectModule::class,
        AppModule::class,
        SharedPreferencesModule::class,
        DatabaseModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
}