package com.care.locationpoc.pushnotification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class TokenPushService : FirebaseMessagingService() {



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //the logic here
        Log.i("[PUSH]", "Message received! ${remoteMessage.data}")
    }

    override fun onNewToken(token: String) {
        //all the logic of the old FirebaseInstanceIdService.onTokenRefresh() here
        //usually, to send to the app server the instance ID token
        //sendTokenToTheAppServer(token)
        Log.i("[PUSH]", "Token! $token")
    }


}