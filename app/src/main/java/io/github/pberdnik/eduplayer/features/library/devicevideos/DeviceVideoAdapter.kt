package io.github.pberdnik.eduplayer.features.library.devicevideos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.pberdnik.eduplayer.databinding.ListItemDeviceVideoBinding
import io.github.pberdnik.eduplayer.domain.DeviceVideo


class DeviceVideoAdapter(private val onClickListener: DeviceVideoClickListener) :
    ListAdapter<DeviceVideo, DeviceVideoViewHolder>(DeviceVideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DeviceVideoViewHolder.from(parent)

    override fun onBindViewHolder(holder: DeviceVideoViewHolder, position: Int) {
        val deviceVideo = getItem(position)
        holder.itemView.setOnClickListener { onClickListener.onClick(deviceVideo) }
        holder.bind(deviceVideo)
    }
}


class DeviceVideoViewHolder(private val binding: ListItemDeviceVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(dv: DeviceVideo) {
        binding.deviceVideo = dv
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): DeviceVideoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemDeviceVideoBinding.inflate(layoutInflater, parent, false)

            return DeviceVideoViewHolder(binding)
        }
    }
}


class DeviceVideoDiffCallback : DiffUtil.ItemCallback<DeviceVideo>() {

    override fun areItemsTheSame(oldItem: DeviceVideo, newItem: DeviceVideo) =
        oldItem.uri == newItem.uri

    override fun areContentsTheSame(oldItem: DeviceVideo, newItem: DeviceVideo) =
        oldItem == newItem
}


class DeviceVideoClickListener(val clickListener: (deviceVideo: DeviceVideo) -> Unit) {
    fun onClick(deviceVideo: DeviceVideo) = clickListener(deviceVideo)
}
