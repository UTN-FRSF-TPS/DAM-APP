package com.fvt.dondeestudio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fvt.dondeestudio.gestores.GestorReservas;

public class ConfirmarReservaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GestorReservas g = new GestorReservas();
        g.cambiarEstadoReserva(intent.getStringExtra("idReserva"), "confirmada", -1);
    }

}
