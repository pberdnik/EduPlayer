package io.github.pberdnik.eduplayer.util

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

fun Fragment.observeSnackbar(rootView: View, status: LiveData<Event<Operation>>) {
    status.observe(viewLifecycleOwner, Observer { event ->
        event.getIfNotHandled()?.let { operation ->
            operation.messageId?.let { messageId ->
                Snackbar.make(
                    rootView,
                    activity?.getString(messageId).toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    })
}

fun Fragment.observeSnackbars(rootView: View, vararg statuses: LiveData<Event<Operation>>) {
    statuses.forEach {status ->
        observeSnackbar(rootView, status)
    }
}