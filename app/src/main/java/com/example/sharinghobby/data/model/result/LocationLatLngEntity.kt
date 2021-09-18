package com.example.sharinghobby.data.model.result

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationLatLngEntity (
    val latitude: Float,
    val lontitude: Float
): Parcelable