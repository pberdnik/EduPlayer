package io.github.pberdnik.eduplayer.network.networkstate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateBroadcastReceiver @Inject constructor(
    private val networkStateLiveData: NetworkStateLiveData,
    private val credential: GoogleAccountCredential
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        networkStateLiveData.postValue(
            if (isDeviceOnline(context)) {
                if (isAuthorized()) NetworkState.CONNECTED_AUTH
                else NetworkState.CONNECTED_NO_AUTH
            } else NetworkState.NO_INTERNET
        )
    }

    private fun isAuthorized() = credential.selectedAccountName != null

    private fun isDeviceOnline(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}