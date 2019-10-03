package io.github.pberdnik.eduplayer.features.account

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.pberdnik.eduplayer.R

@BindingAdapter("avatarUrl")
fun ImageView.bindAvatar(imgUrl: String?) {
    if (imgUrl == null) {
        this.setImageDrawable(context.getDrawable(R.drawable.ic_account_24dp))
    } else {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUri)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
    }
}