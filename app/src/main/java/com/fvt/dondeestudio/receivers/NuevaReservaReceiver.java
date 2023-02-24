package com.fvt.dondeestudio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fvt.dondeestudio.R;

public class NuevaReservaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NavController navController = Navigation.findNavController(
                ((FragmentActivity) context).findViewById(R.id.nav_host_fragment)
        );
        navController.navigate(R.id.action_global_reservasPendientesFragment);

    }

}
