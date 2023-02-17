package com.fvt.dondeestudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fvt.dondeestudio.databinding.ActivityMainBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.listeners.AlumnoClaseListener;
import com.fvt.dondeestudio.listeners.AlumnoReservasListener;
import com.fvt.dondeestudio.listeners.ProfesorReservasListener;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GestorProfesores g = new GestorProfesores();
        String idLog = FirebaseAuth.getInstance().getUid();
        if(idLog != null) {
            g.obtenerProfesor(idLog, new Callback<Profesor>() {
                @Override
                public void onComplete(Profesor data) {
                    if (data == null) {
                        //entonces es alumno, veo si cambia alguna reserva que hizo el alumno
                        AlumnoReservasListener.seguirReserva(idLog, getApplicationContext());
                    } else {
                        ProfesorReservasListener.seguirReserva(idLog, getApplicationContext());
                    }
                }
            });
        } else {
            //Tiene que loguearse
        }


    }
}