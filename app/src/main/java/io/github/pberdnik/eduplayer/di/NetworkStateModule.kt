package io.github.pberdnik.eduplayer.di

import dagger.Module
import dagger.Provides
import io.github.pberdnik.eduplayer.network.networkstate.NetworkStateLiveData
import javax.inject.Singleton

@Module
object NetworkStateModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideNetworkStateLiveData() = NetworkStateLiveData()
}