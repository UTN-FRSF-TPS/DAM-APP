package com.fvt.dondeestudio.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.fvt.dondeestudio.AgregarClaseFragment;
import com.fvt.dondeestudio.MainActivity;
import com.fvt.dondeestudio.R;

public class NotificacionHelper {

    public static void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Donde Estudio";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        /* Los intent que se pasan por parametro deben definirse así
        Intent opcion1 = new Intent(context, MainActivity.class);
        opcion1.putExtra("fragment", "AgregarClaseFragment");
        opcion1.putExtra("datox", 9999);
         */


       //



        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(body));

        if(intent != null) {
            PendingIntent Pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            mBuilder.setContentIntent(Pi);
        }


        notificationManager.notify(notificationId, mBuilder.build());
    }

}