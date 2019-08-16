package io.github.pberdnik.eduplayer.network.dto


data class Thumbnails(
    val default: Thumbnail? = null,
    val medium: Thumbnail? = null,
    val high: Thumbnail? = null,
    val standard: Thumbnail? = null,
    val maxres: Thumbnail? = null
) {
    val best = (maxres ?: standard ?: high ?: medium ?: default)!!.url
}

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)
