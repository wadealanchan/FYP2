package com.example.alan.fyp.util;

import android.content.Intent;
import android.util.Log;

import com.example.alan.fyp.Henson;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Alan on 26/7/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static String TAG="FCMMessage";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG,"recived Message");
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.e(TAG,"Date Received : "+remoteMessage.getData().toString());

            /*String conversationId = remoteMessage.getData().get("conversationId");
            String targetUserName = remoteMessage.getData().get("targetUserName");
            Intent intent = Henson.with(getApplicationContext()).
                    gotoChat2()
                    .conversationId(conversationId)
                    .targetUserName(targetUserName)
                    .build();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);*/

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            } else {
                // Handle message within 10 seconds


            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.e(TAG,"Notification Received : "+remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}
