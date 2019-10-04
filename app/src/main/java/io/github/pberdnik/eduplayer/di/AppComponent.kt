package io.github.pberdnik.eduplayer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.pberdnik.eduplayer.features.account.AccountViewModel
import io.github.pberdnik.eduplayer.features.explore.ExploreViewModel
import io.github.pberdnik.eduplayer.features.library.LibraryViewModel
import io.github.pberdnik.eduplayer.features.playlistdetails.PlaylistDetailsViewModel
import javax.inject.Singleton

@Component(
    modules = [
        AssistedInjectModule::class,
        AuthModule::class,
        SharedPreferencesModule::class,
        DatabaseModule::class,
        NetworkModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    val exploreViewModel: ExploreViewModel
    val libraryViewModel: LibraryViewModel
    val accountViewModel: AccountViewModel
    val playlistDetailsViewModelFactory: PlaylistDetailsViewModel.Factory
}