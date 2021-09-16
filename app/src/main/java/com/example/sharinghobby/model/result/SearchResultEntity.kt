package com.example.sharinghobby.model.result

import android.os.Parcelable
import com.example.sharinghobby.model.result.LocationLatLngEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResultEntity (
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
): Parcelable