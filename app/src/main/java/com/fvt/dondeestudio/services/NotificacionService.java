package com.fvt.dondeestudio.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.fvt.dondeestudio.DTO.ReservaDTO;
import com.fvt.dondeestudio.gestores.GestorAlumnos;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.gestores.GestorReservas;
import com.fvt.dondeestudio.helpers.NotificacionHelper;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Profesor;

import java.util.ArrayList;

public class NotificacionService extends Service {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       final Context contexto = this;
        GestorReservas gestorReservas = new GestorReservas();
        GestorAlumnos gestorAlumnos = new GestorAlumnos();
        GestorProfesores gestorProfesores = new GestorProfesores();
        GestorClases gestorClases = new GestorClases();
        gestorReservas.getReservasNuevas("lpWjYgGw8RYkwxnfgI24j7VGhof2", new GestorClases.Callback<ArrayList<ReservaDTO>>() {
            @Override
            public void onComplete(ArrayList<ReservaDTO> reservas) {
                System.out.println("RESERVAS.SIZE = " + reservas.size());
                if(reservas.size() > 0){
                    for(ReservaDTO rDTO : reservas){
                        gestorAlumnos.obtenerAlumno(rDTO.getIdAlumno(), new GestorClases.Callback<Alumno>() {
                            @Override
                            public void onComplete(Alumno alumno) {
                                gestorProfesores.obtenerProfesor(rDTO.getIdProfesor(), new GestorClases.Callback<Profesor>() {
                                    @Override
                                    public void onComplete(Profesor prof) {
                                        gestorClases.getClase(rDTO.getIdClase(), new GestorClases.Callback<Clase>() {
                                            @Override
                                            public void onComplete(Clase clase) {
                                                NotificacionHelper notificationHelper = new NotificacionHelper();
                                                notificationHelper.showNotification(contexto, "Solicitud de " + alumno.getNombre() + " " + alumno.getApellido(), "Para unirse a tu " +
                                                        "clase de " + clase.getAsignatura(), null);
                                            }
                                        });

                                    }
                                });
                            }
                        });

                    }
                }
            }
        });


        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, NotificacionService.class);
        alarmIntent = PendingIntent.getService(this, 0, i, 0);

        // Establecer una alarma para ejecutarse cada 1 min.
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 60 * 1000,
                60 * 1000, alarmIntent);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}