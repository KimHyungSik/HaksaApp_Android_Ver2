package com.example.haksaapp.Firebase

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.haksaapp.MainActivity
import com.example.haksaapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
import com.example.haksaapp.Util.Utility.TAG
import com.example.haksaapp.Util.Utility.CHANNEL_ID
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    //새 토큰 생성 시 호출
    override fun onNewToken(token: String) {
    }

    fun getToken(callback: (String)->Unit){
        var token = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result.toString()
            Log.d(TAG, "getToken: $token")
            callback(token)
        })
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived")
        val title = remoteMessage.notification?.title
        val msg = remoteMessage.notification?.body
        Log.d(TAG, "$title and $msg")

        val intent = Intent(this, MainActivity::class.java)
        val notification_ID = Random.nextInt()

        //플래그 추가
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //다른 프로세스에서 권한 할당
        val contentIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(notification_ID, builder)
    }
}