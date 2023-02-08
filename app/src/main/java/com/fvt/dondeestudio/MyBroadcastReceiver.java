package com.fvt.dondeestudio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("com.example.MY_ACTION")) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setSmallIcon(com.google.firebase.appcheck.interop.R.drawable.common_google_signin_btn_icon_dark)
                    .setContentTitle("Notificación")
                    .setContentText("Se ha recibido la acción com.example.MY_ACTION")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, builder.build());
        }
    }
}