package com.example.sharinghobby.utillity.fcm

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

object FCMUtil {
    val TAG = "FCM"

    /** Device Token 생성 */
    fun getToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM Token 생성 실패 :: ${task.exception}")
                return@OnCompleteListener
            }

            val token = task.result

            Log.e(TAG, token.toString())
        })
    }
}