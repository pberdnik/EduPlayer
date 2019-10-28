package io.github.pberdnik.eduplayer.features.videoplayer

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import io.github.pberdnik.eduplayer.R
import io.github.pberdnik.eduplayer.domain.DeviceVideo

private const val PLAYBACK_CHANNEL_ID = "playback_channel"
private const val PLAYBACK_NOTIFICATION_ID = 1

fun MediaPlaybackService.createNotification(
    context: MediaPlaybackService,
    deviceVideo: DeviceVideo
): PlayerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
    this,
    PLAYBACK_CHANNEL_ID,
    R.string.playback_channel_name,
    R.string.playback_channel_description,
    PLAYBACK_NOTIFICATION_ID,
    createMediaDescriptionAdapter(deviceVideo, context),
    createNotificationListener()
).apply {
    setUseNavigationActions(false)
    setUseStopAction(true)
    setColorized(false)
    setUseChronometer(true)
    setSmallIcon(R.drawable.ic_edu_logo_notification)
}


private fun MediaPlaybackService.createNotificationListener() =
    object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationStarted(notificationId: Int, notification: Notification?) =
            startForeground(notificationId, notification)

        override fun onNotificationCancelled(notificationId: Int) = stopSelf()
    }


private fun createMediaDescriptionAdapter(deviceVideo: DeviceVideo, context: MediaPlaybackService) =
    object : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): String {
            return deviceVideo.title
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return PendingIntent.getActivity(
                context,
                0,
                Intent(context, VideoPlayerActivity::class.java),
                0
            )
        }

        override fun getCurrentContentText(player: Player): String? {
            return deviceVideo.duration
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Glide.with(context).asBitmap().load(deviceVideo.uri).into(
                object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) = callback.onBitmap(resource)

                    override fun onLoadCleared(placeholder: Drawable?) {}
                }
            )
            return null
        }
    }