package com.fvt.dondeestudio.listeners;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fvt.dondeestudio.receivers.CancelarReservaReceiver;
import com.fvt.dondeestudio.receivers.ConfirmarReservaReceiver;
import com.fvt.dondeestudio.gestores.GestorAlumnos;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.helpers.NotificacionHelper;
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

    public static void seguirReserva(String idProfesor, Context context) {
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
                                    String idReserva = dc.getDocument().getId();
                                    String idAlumno = (String) dc.getDocument().get("idAlumno");
                                    String estado = (String) dc.getDocument().get("estado");
                                    if (estado.equals("pendiente")) {
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
                                                        System.out.println("El alumno" + nombre + " " + apellido + " solicito unirse a tu clase de " + asignatura);
                                                        Intent confirm = new Intent();
                                                        confirm.setAction("confirmarReserva");
                                                        confirm.putExtra("idReserva", idReserva);
                                                        Intent cancel = new Intent();
                                                        cancel.setAction("cancelarReserva");
                                                        cancel.putExtra("idReserva", idReserva);
                                                        NotificacionHelper.showNotification(context, "Nueva reserva", "El alumno " + nombre + " " + apellido + " solicito unirse a tu clase de " + asignatura, confirm, cancel);
                                                        ConfirmarReservaReceiver receiver = new ConfirmarReservaReceiver();
                                                        IntentFilter intentFilter = new IntentFilter("confirmarReserva");
                                                        context.registerReceiver(receiver, intentFilter);
                                                        CancelarReservaReceiver receiver2 = new CancelarReservaReceiver();
                                                        IntentFilter intentFilter2 = new IntentFilter("cancelarReserva");
                                                        context.registerReceiver(receiver2, intentFilter2);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    break;
                                case MODIFIED:
                                    String estado2 = (String) dc.getDocument().get("estado");
                                    String idClase2 = (String) dc.getDocument().get("idClase");
                                    String idAlumno2 = (String) dc.getDocument().get("idAlumno");
                                    if (estado2.equals(("cancelada"))) {
                                        GestorClases gC = new GestorClases();
                                        gC.getClase(idClase2, new Callback<Clase>() {
                                            @Override
                                            public void onComplete(Clase clase) {
                                                String asignatura2 = clase.getAsignatura();
                                                GestorAlumnos gA = new GestorAlumnos();
                                                gA.obtenerAlumno(idAlumno2, new Callback<Alumno>() {
                                                    @Override
                                                    public void onComplete(Alumno alumno) {
                                                        String nombre2 = alumno.getNombre();
                                                        String apellido2 = alumno.getApellido();
                                                        NotificacionHelper.showNotification(context, "Reserva cancelada", "El alumno " + nombre2 + " " + apellido2 + " cancelo la reserva a tu clase de " + asignatura2, null, null);
                                                    }
                                                });
                                            }
                                        });
                                    }
                            }
                        }
                    }
                });
    }


}