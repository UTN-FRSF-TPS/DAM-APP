package com.fvt.dondeestudio.listeners;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fvt.dondeestudio.gestores.GestorAlumnos;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Clase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfesorReservasListener {


    /**
     * Permite conocer al profesor idProfesor si un alumno reservo alguna de sus clases (se agrega una reserva) o
     * si cancela una reserva (estado = cancelada)
     * @param idProfesor
     * TODO Generar la notificacion para el alumno
     */

    public static void seguirReserva(String idProfesor) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("reserva");
        collRef.whereEqualTo("idProfesor", idProfesor)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("a", "Listen failed.", e);
                            return;
                        }

                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    String idClase = (String) dc.getDocument().get("idClase");
                                    String idAlumno = (String) dc.getDocument().get("idAlumno");
                                    GestorClases gC = new GestorClases();
                                    gC.getClase(idClase, new Callback<Clase>() {
                                        @Override
                                        public void onComplete(Clase clase) {
                                            String asignatura = clase.getAsignatura();
                                            GestorAlumnos gA = new GestorAlumnos();
                                            gA.obtenerAlumno(idAlumno, new Callback<Alumno>() {
                                                @Override
                                                public void onComplete(Alumno alumno) {
                                                    String nombre = alumno.getNombre();
                                                    String apellido = alumno.getApellido();
                                                    System.out.println("El alumno" + nombre + " " +  apellido +  " solicito unirse a tu clase de " + asignatura);
                                                    //crear notificacion con esos datos;
                                                }
                                            });
                                        }
                                    });
                                    break;
                                case MODIFIED:
                                    String estado = (String) dc.getDocument().get("estado");
                                    if(estado.equals(("cancelada"))){
                                        //Un alumno cancelo una reserva, generar notificacion
                                    }
                            }
                        }
                    }
                });

    }


}