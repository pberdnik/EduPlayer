package io.github.pberdnik.eduplayer.features.account

import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.domain.UserInfo
import io.github.pberdnik.eduplayer.network.networkstate.NetworkState
import io.github.pberdnik.eduplayer.network.networkstate.NetworkStateLiveData
import io.github.pberdnik.eduplayer.repository.YoutubeRepository
import io.github.pberdnik.eduplayer.util.Event
import io.github.pberdnik.eduplayer.util.Operation
import io.github.pberdnik.eduplayer.util.SnackbarMessages
import io.github.pberdnik.eduplayer.util.performMainThreadOperation
import javax.inject.Inject

const val PREF_ACCOUNT_NAME = "youtubeAccountName"

class AccountViewModel @Inject constructor(
    private val credential: GoogleAccountCredential,
    private val youtubeRepository: YoutubeRepository,
    private val sharedPreferences: SharedPreferences,
    private val networkStateLiveData: NetworkStateLiveData
) : ViewModel() {

    private val _isSignedIn = MutableLiveData<Boolean>(false)
    val isSignedIn: LiveData<Boolean> = _isSignedIn

    val userInfo: LiveData<UserInfo> = Transformations.switchMap(isSignedIn) {isSignedIn ->
        if (!isSignedIn) {
            MutableLiveData(UserInfo())
        } else {
            youtubeRepository.getUserInfo(credential.selectedAccountName)
        }
    }

    private val _signIn = MutableLiveData<Boolean>(false)
    val signIn: LiveData<Boolean> = _signIn

    private val _refreshStatus = MutableLiveData<Event<Operation>>()
    val refreshStatus: LiveData<Event<Operation>> = _refreshStatus

    val networkState: LiveData<NetworkState> = networkStateLiveData

    val isConnected: LiveData<Boolean> = Transformations.map(networkState) {ns ->
        ns != NetworkState.NO_INTERNET
    }

    init {
        if (hasSelectedAccount()) loadAccountInfo()
    }

    fun displaySignInDialog() {
        _signIn.value = true
    }

    fun displaySignInDialogComplete() {
        _signIn.value = false
    }

    fun signOut() {
        credential.selectedAccount = null
        _isSignedIn.value = false
        sharedPreferences.edit().putString(PREF_ACCOUNT_NAME, null).apply()
    }

    fun newChooseAccountIntent(): Intent = credential.newChooseAccountIntent()

    fun hasSelectedAccount() = credential.selectedAccountName != null

    fun selectAccount(accountName: String) {
        credential.selectedAccountName = accountName
        sharedPreferences.edit().putString(PREF_ACCOUNT_NAME, accountName).apply()
        loadAccountInfo()
    }

    private fun loadAccountInfo() {
        _isSignedIn.value = true
        performMainThreadOperation(
            _refreshStatus,
            SnackbarMessages(errorMessageId = R.string.couldnt_load_account_info)
        ) {
            youtubeRepository.updateUserInfo(credential.selectedAccountName)
        }
    }

}