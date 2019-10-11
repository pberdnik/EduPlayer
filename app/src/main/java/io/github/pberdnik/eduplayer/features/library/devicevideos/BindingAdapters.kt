package io.github.pberdnik.eduplayer.features.library.devicevideos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.pberdnik.eduplayer.domain.DeviceVideo

@BindingAdapter("listData")
fun RecyclerView.bindListData(data: List<DeviceVideo>?) {
    (adapter as DeviceVideoAdapter).submitList(data)
}

@BindingAdapter("videoUri")
fun ImageView.bindImage(videoUri: String?) {
    videoUri?.let {
        Glide.with(context)
            .asBitmap()
            .load(Uri.parse(videoUri))
            .placeholder(ColorDrawable(Color.LTGRAY))
            .into(this)
    }
}