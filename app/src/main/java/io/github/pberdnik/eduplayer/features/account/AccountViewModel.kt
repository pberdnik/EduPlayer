package io.github.pberdnik.eduplayer.features.account

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.pberdnik.eduplayer.network.YoutubeDataApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class AccountViewModel @Inject constructor(
    private val credential: GoogleAccountCredential,
    private val youtubeDataApiService: YoutubeDataApiService
) : ViewModel() {

    private val _signIn = MutableLiveData<Boolean>(false)
    val signIn: LiveData<Boolean> = _signIn

    val token = MutableLiveData<String>(null)

    fun displaySignInDialog() {
        _signIn.value = true
    }

    fun displaySignInDialogComplete() {
        _signIn.value = false
    }

    fun signOut() {
        credential.selectedAccount = null
        token.value = null
    }

    fun newChooseAccountIntent(): Intent = credential.newChooseAccountIntent()

    fun hasSelectedAccount() = credential.selectedAccountName == null

    fun selectAccount(accountName: String) {
        credential.selectedAccountName = accountName
        viewModelScope.launch(Dispatchers.IO) {
            token.postValue(credential.token)
        }
    }

    fun callApi() {
        viewModelScope.launch {
            val myPlaylists = try {
                youtubeDataApiService.getMyPlaylists().toString()
            } catch (e: Exception) {
                e.message
            }
            token.postValue(myPlaylists)
        }
    }

}