package com.fvt.dondeestudio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fvt.dondeestudio.MessageActivity;

public class NuevoMensajeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String userId = intent.getStringExtra("userId");

        Intent activityIntent = new Intent(context, MessageActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.putExtra("userId", userId);
        context.startActivity(activityIntent);
        context.startActivity(intent);
    }
}
