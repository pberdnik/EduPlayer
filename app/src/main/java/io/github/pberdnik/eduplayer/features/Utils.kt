package io.github.pberdnik.eduplayer.features

fun Long.timeToHumanFormat(): String {
    val durationSeconds = this / 1000
    val h = durationSeconds / 60 / 60
    val m = durationSeconds % (60 * 60) / 60
    val s = durationSeconds % 60
    return if (h == 0L) {
        String.format("%d:%02d", m, s)
    } else {
        String.format("%d:%02d:%02d", h, m, s)
    }
}