package io.github.pberdnik.eduplayer.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class OperationStatus { LOADING, ERROR, DONE }

fun ViewModel.performLongOperation(status: MutableLiveData<OperationStatus>, block: suspend () -> Unit) {
    viewModelScope.launch {
        try {
            status.value = OperationStatus.LOADING
            withContext(Dispatchers.IO) { block() }
            status.value = OperationStatus.DONE
        } catch (e: Exception) {
            status.value = OperationStatus.ERROR
        }
    }
}