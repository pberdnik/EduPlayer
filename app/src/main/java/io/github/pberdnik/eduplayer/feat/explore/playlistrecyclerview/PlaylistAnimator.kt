package io.github.pberdnik.eduplayer.feat.explore.playlistrecyclerview

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager

class PlaylistAnimator : DefaultItemAnimator() {

    override fun recordPreLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder,
        changeFlags: Int,
        payloads: MutableList<Any>
    ) = PlaylistHolderInfo().setFrom(viewHolder)

    override fun recordPostLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder
    ) = PlaylistHolderInfo().setFrom(viewHolder)

//    Log.d(
//    "PlaylistAnimator",
//    """2 oldHolder ${if (oldHolder == newHolder) "==" else "!="} newHolder
//            | oldHolder: ${oldHolder.binding.pi?.expanded}
//            | oldHolder: ${oldHolder.binding.motionLayout.progress}
//            | newHolder: ${newHolder.binding.pi?.expanded}
//            | newHolder: ${newHolder.binding.motionLayout.progress}
//        """.trimMargin()
//    )

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        // Use default animation if this is not a fold/expansion
        if (!(oldHolder is PlaylistExpandedViewHolder && newHolder is PlaylistFoldedViewHolder ||
                    oldHolder is PlaylistFoldedViewHolder && newHolder is PlaylistExpandedViewHolder)
        ) {
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }
        if (preInfo !is PlaylistHolderInfo || postInfo !is PlaylistHolderInfo ||
                preInfo.id != postInfo.id) {
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }
        // Run motion layout transition otherwise, i.e. in case of fold/expansion
        if (oldHolder is PlaylistExpandedViewHolder && newHolder is PlaylistFoldedViewHolder) {
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        } else if (oldHolder is PlaylistFoldedViewHolder && newHolder is PlaylistExpandedViewHolder) {
            TransitionManager.beginDelayedTransition(newHolder.binding.clContainer)
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        } else {
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }

    }

    private class PlaylistHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo() {
        lateinit var id: String
        var expanded: Boolean = false

        override fun setFrom(holder: RecyclerView.ViewHolder): ItemHolderInfo {
            if (holder is PlaylistExpandedViewHolder) {
                holder.binding.pi?.let {
                    id = it.playlist.id
                    expanded = it.expanded
                }
            }
            if (holder is PlaylistFoldedViewHolder) {
                holder.binding.pi?.let {
                    id = it.playlist.id
                    expanded = it.expanded
                }
            }
            return super.setFrom(holder)
        }
    }
}
