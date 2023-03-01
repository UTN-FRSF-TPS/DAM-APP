package com.fvt.dondeestudio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fvt.dondeestudio.MainActivity;
import com.fvt.dondeestudio.MessageActivity;
import com.fvt.dondeestudio.R;
import com.fvt.dondeestudio.gestores.GestorReservas;
import com.fvt.dondeestudio.helpers.Util;

public class ReservaModificadaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Util.isAppInForeground(context)){
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.putExtra("fragment", 3);
            context.startActivity(activityIntent);
        } else {
            NavController navController = Navigation.findNavController(
                    ((FragmentActivity) context).findViewById(R.id.nav_host_fragment)
            );
            navController.navigate(R.id.action_global_clasesReservadasFragment);
        }
    }
}
