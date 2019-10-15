package io.github.pberdnik.eduplayer.network.networkstate

import androidx.lifecycle.MutableLiveData

enum class NetworkState { CONNECTED_AUTH, CONNECTED_NO_AUTH, NO_INTERNET, }

class NetworkStateLiveData : MutableLiveData<NetworkState>()