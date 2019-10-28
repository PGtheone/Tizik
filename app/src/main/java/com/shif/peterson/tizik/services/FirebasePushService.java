package com.shif.peterson.tizik.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shif.peterson.tizik.R;

public class FirebasePushService extends FirebaseMessagingService {

    private static final String CHANNEL_ID_DEFAULT = "default";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        createDefaultChannel();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


    private void createDefaultChannel() {
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_DEFAULT,
                    "Default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
        }
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
