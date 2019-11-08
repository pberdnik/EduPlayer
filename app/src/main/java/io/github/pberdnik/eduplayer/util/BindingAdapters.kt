package io.github.pberdnik.eduplayer.util

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter

@BindingAdapter("android:layout_marginTop")
fun setTopMargin(view: View, topMargin: Float) {
    (view.layoutParams as ViewGroup.MarginLayoutParams).topMargin = topMargin.toInt()
}

@BindingAdapter("android:layout_marginBottom")
fun setBottomMargin(view: View, bottomMargin: Float) {
    (view.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = bottomMargin.toInt()
}