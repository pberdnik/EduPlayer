package io.github.pberdnik.eduplayer.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// compiles successfully; will be fixed in kotlin-kapt 1.3.60
@Parcelize
data class DeviceVideo(
    val uri: String,
    val title: String,
    val duration: String
): Parcelable