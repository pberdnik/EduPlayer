package io.github.pberdnik.eduplayer.feat.explore

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.domain.Playlist

@BindingAdapter("listData")
fun RecyclerView.bindListData(data: List<Playlist>?) {
    (adapter as PlaylistAdapter).submitList(data)
}