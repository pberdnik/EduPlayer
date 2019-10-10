package io.github.pberdnik.eduplayer.features.library.devicevideos

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.domain.DeviceVideo

@BindingAdapter("listData")
fun RecyclerView.bindListData(data: List<DeviceVideo>?) {
    (adapter as DeviceVideoAdapter).submitList(data)
}
