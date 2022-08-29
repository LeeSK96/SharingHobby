package com.example.sharinghobby.utillity.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sharinghobby.HomeActivity
import com.example.sharinghobby.R
import com.example.sharinghobby.utillity.fcm.FCMUtil.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService(){

    private var CHANNEL_ID : String = ""

    override fun onMessageReceived(rm: RemoteMessage) {
        super.onMessageReceived(rm)

        val title = rm.notification?.title
        val content = rm.notification?.body

        Log.e(TAG, rm.notification.toString())
        Log.e(TAG, title.toString())
        Log.e(TAG, content.toString())

        if (rm.data.isNotEmpty()){
            Log.e("바디", rm.data["body"].toString())
            Log.e("타이틀",rm.data["title"].toString())
            sendNotification(rm)
        }else{
            Log.e("수신에러 : ", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
            Log.e("data값 :",rm.data.toString())
        }

        // TODO Notification
        // https://developer.android.com/training/notify-user/build-notification

        sendNotification(rm)

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // 토큰 값 따로 저장
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()


    }

    // 알림 생성
    private fun sendNotification(rm : RemoteMessage){
        //RemoteCode, ID를 고유값으로 지정하여 알림이 개별 표시 되도록 함
        val uid: Int = (System.currentTimeMillis() / 7).toInt()

        //일회용 PendingIntent
        // PendingIntent : Intent의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack을 경로만 남김
        val pendingIntent = PendingIntent.getActivity(this, uid, intent, PendingIntent.FLAG_ONE_SHOT)

        // 알림 채널 이름
        val channelId = getString(R.string.channel_name)

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)     // 아이콘 설정
            .setContentTitle(rm.data["body"].toString())     // 제목
            .setContentText(rm.data["title"].toString())     // 메시지 내용
            .setAutoCancel(true)
            .setSound(soundUri)     // 알림 소리
            .setContentIntent(pendingIntent)       // 알림 실행 시 Intent

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uid, notificationBuilder.build())
    }

    // 알림채널만들기(FCM)
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}