package io.github.pberdnik.eduplayer.feat.playlistdetails

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.domain.PlaylistItemWithInfo

@BindingAdapter("listData")
fun RecyclerView.bindListData(data: List<PlaylistItemWithInfo>?) {
    (adapter as PlaylistItemAdapter).submitList(data)
}
