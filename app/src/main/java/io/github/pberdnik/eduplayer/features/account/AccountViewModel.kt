package io.github.pberdnik.eduplayer.features.account

import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.network.YoutubeDataApiService
import io.github.pberdnik.eduplayer.util.Event
import io.github.pberdnik.eduplayer.util.Operation
import io.github.pberdnik.eduplayer.util.SnackbarMessages
import io.github.pberdnik.eduplayer.util.performMainThreadOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val PREF_ACCOUNT_NAME = "youtubeAccountName"

class AccountViewModel @Inject constructor(
    private val credential: GoogleAccountCredential,
    private val youtubeDataApiService: YoutubeDataApiService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _isSignedIn = MutableLiveData<Boolean>(false)
    val isSignedIn: LiveData<Boolean> = _isSignedIn

    private val _avatarUrl = MutableLiveData<String>(null)
    val avatarUrl: LiveData<String> = _avatarUrl

    private val _accountDescription = MutableLiveData<String>(null)
    val accountDescription: LiveData<String> = _accountDescription

    private val _signIn = MutableLiveData<Boolean>(false)
    val signIn: LiveData<Boolean> = _signIn

    private val _refreshStatus = MutableLiveData<Event<Operation>>()
    val refreshStatus: LiveData<Event<Operation>> = _refreshStatus

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
        _avatarUrl.value = null
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
            val userInfo = withContext(Dispatchers.IO) { youtubeDataApiService.getUserInfo() }
            if (userInfo.items.isNotEmpty()) {
                _avatarUrl.value = userInfo.items[0].snippet.thumbnails.best
                _accountDescription.value = userInfo.items[0].snippet.title
            } else {
                _accountDescription.value = credential.selectedAccountName
            }
        }
    }

}