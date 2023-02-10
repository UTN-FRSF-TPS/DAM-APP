package com.fvt.dondeestudio.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.fvt.dondeestudio.AgregarClaseFragment;
import com.fvt.dondeestudio.BuscarClasesFragment;
import com.fvt.dondeestudio.DetalleClaseFragment;
import com.fvt.dondeestudio.MainActivity;
import com.fvt.dondeestudio.R;

public class NotificacionHelper {

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent opcion1 = new Intent(context, MainActivity.class);
        opcion1.putExtra("datox", 9999);
        PendingIntent opcion1Pi = PendingIntent.getBroadcast(context, 0, opcion1, 0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(opcion1Pi)
                .addAction(android.R.drawable.ic_menu_agenda, "Confirmar",
                        opcion1Pi)
                        .setAutoCancel(true);
/*
        Intent actionIntent1 = new Intent(context, DetalleClaseFragment.class);
        actionIntent1.setAction("confirmar");

        PendingIntent actionPendingIntent1 = PendingIntent.getBroadcast(context, 0,
                actionIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.addAction(R.mipmap.ic_launcher, "Confirmar", actionPendingIntent1);
        mBuilder.setContentIntent(actionPendingIntent1);

        Intent actionIntent2 = new Intent(context, AgregarClaseFragment.class);
        actionIntent2.setAction("cancelar");

        PendingIntent actionPendingIntent2 = PendingIntent.getBroadcast(context, 0,
                actionIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.addAction(R.mipmap.ic_launcher, "Cancelar", actionPendingIntent2);
        mBuilder.setContentIntent(actionPendingIntent2);

        /*
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
*/
        notificationManager.notify(notificationId, mBuilder.build());
    }

}