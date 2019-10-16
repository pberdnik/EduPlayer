package io.github.pberdnik.eduplayer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.pberdnik.eduplayer.features.account.AccountViewModel
import io.github.pberdnik.eduplayer.features.explore.ExploreViewModel
import io.github.pberdnik.eduplayer.features.learn.LearnViewModel
import io.github.pberdnik.eduplayer.features.library.LibraryViewModel
import io.github.pberdnik.eduplayer.features.library.devicevideos.DeviceVideosViewModel
import io.github.pberdnik.eduplayer.features.library.eduplayerplaylists.EduPlayerPlaylistsViewModel
import io.github.pberdnik.eduplayer.features.library.youtubeplaylists.YoutubePlaylistsViewModel
import io.github.pberdnik.eduplayer.features.playlistdetails.PlaylistDetailsViewModel
import io.github.pberdnik.eduplayer.features.videoplayer.VideoPlayerViewModel
import io.github.pberdnik.eduplayer.network.networkstate.NetworkStateBroadcastReceiver
import javax.inject.Singleton

@Component(
    modules = [
        AssistedInjectModule::class,
        AuthModule::class,
        SharedPreferencesModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        NetworkStateModule::class
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
    val eduPlayerPlaylistsViewModel: EduPlayerPlaylistsViewModel
    val deviceVideosViewModel: DeviceVideosViewModel
    val youtubePlaylistsViewModel: YoutubePlaylistsViewModel

    val learnViewModel: LearnViewModel

    val accountViewModel: AccountViewModel

    val playlistDetailsViewModelFactory: PlaylistDetailsViewModel.Factory

    val networkStateBroadcastReceiver: NetworkStateBroadcastReceiver

    val videoPlayerViewModel: VideoPlayerViewModel
}