package com.example.sharinghobby.utillity.fcm

import com.google.gson.annotations.SerializedName

data class FCMRequest(@field:SerializedName("to") var token: String, var notification: FCMContent)