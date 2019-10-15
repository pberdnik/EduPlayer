package io.github.pberdnik.eduplayer.util

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class OperationStatus { LOADING, ERROR, DONE }

data class Operation(val status: OperationStatus, @StringRes val messageId: Int?)

data class SnackbarMessages(
    @StringRes val loadingMessageId: Int? = null,
    @StringRes val doneMessageId: Int? = null,
    @StringRes val errorMessageId: Int? = null
)


fun ViewModel.performIOOperation(
    status: MutableLiveData<Event<Operation>>? = null,
    snackbarMessages: SnackbarMessages? = null,
    block: suspend () -> Unit
) =
    performLongOperation(Dispatchers.IO, status, snackbarMessages, block)

fun ViewModel.performMainThreadOperation(
    status: MutableLiveData<Event<Operation>>? = null,
    snackbarMessages: SnackbarMessages? = null,
    block: suspend () -> Unit
) =
    performLongOperation(Dispatchers.Main, status, snackbarMessages, block)


private fun ViewModel.performLongOperation(
    coroutineDispatcher: CoroutineDispatcher,
    status: MutableLiveData<Event<Operation>>? = null,
    snackbarMessages: SnackbarMessages? = null,
    block: suspend () -> Unit
) =
    viewModelScope.launch {
        try {
            status?.value = Event(Operation(OperationStatus.LOADING, snackbarMessages?.loadingMessageId))
            withContext(coroutineDispatcher) { block() }
            status?.value = Event(Operation(OperationStatus.DONE, snackbarMessages?.doneMessageId))
        } catch (e: Exception) {
            status?.value = Event(Operation(OperationStatus.ERROR, snackbarMessages?.errorMessageId))
        }
    }
