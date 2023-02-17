package com.fvt.dondeestudio.listeners;


import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fvt.dondeestudio.AgregarClaseFragment;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.helpers.NotificacionHelper;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Profesor;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class AlumnoReservasListener {


    /**
     * Permite conocer si el profesor de alguna de las clases que reservo el alumno idAlumno lo acepto o rechazo en la clase
     * @param idAlumno
     * TODO Generar la notificacion para el alumno
     */
    public static void seguirReserva(String idAlumno, Context context) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("reserva");
        collRef.whereEqualTo("idAlumno", idAlumno)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("a", "Fallo del listener1", e);
                            return;
                        }

                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case MODIFIED:
                                    String idClase = (String) dc.getDocument().get("idClase");
                                    String idProfesor = (String) dc.getDocument().get("idProfesor");
                                    String estado = (String) dc.getDocument().get("estado");
                                    GestorClases gC = new GestorClases();
                                    gC.getClase(idClase, new Callback<Clase>() {
                                        @Override
                                        public void onComplete(Clase clase) {
                                            String asignatura = clase.getAsignatura();
                                            GestorProfesores gP = new GestorProfesores();
                                            gP.obtenerProfesor(idProfesor, new Callback<Profesor>() {
                                                @SuppressLint("SuspiciousIndentation")
                                                @Override
                                                public void onComplete(Profesor profesor) {
                                                    String nombre = profesor.getNombre();
                                                    String apellido = profesor.getApellido();
                                                    if (estado.equals("confirmada"))
                                                    NotificacionHelper.showNotification(context, "Reserva de Clase", "El profesor " + nombre + " " + apellido + " acepto tu reserva a la clase de " + asignatura, null, null);
                                                    if (estado.equals("rechazada"))
                                                        NotificacionHelper.showNotification(context, "Reserva de Clase", "El profesor " + nombre + " " + apellido + " rechazo tu reserva a la clase de " + asignatura, null, null);
                                                }
                                            });
                                        }
                                    });
                                    break;

                                case REMOVED:
                                    String idClase2 = (String) dc.getDocument().get("idClase");
                                    String idProfesor2 = (String) dc.getDocument().get("idProfesor");
                                    String estado2 = (String) dc.getDocument().get("estado");
                                    GestorClases gC2 = new GestorClases();
                                    gC2.getClase(idClase2, new Callback<Clase>() {
                                        @Override
                                        public void onComplete(Clase clase) {
                                            String asignatura = clase.getAsignatura();
                                            GestorProfesores gP = new GestorProfesores();
                                            gP.obtenerProfesor(idProfesor2, new Callback<Profesor>() {
                                                @Override
                                                public void onComplete(Profesor profesor) {
                                                    String nombre = profesor.getNombre();
                                                    String apellido = profesor.getApellido();
                                                        NotificacionHelper.showNotification(context, "Reserva de Clase", "El profesor " + nombre + " " + apellido + " elimino la clase de " + asignatura +  " que habías reservado.", null, null);
                                                }
                                            });
                                        }
                                    });
                                    break;

                            }
                        }
                    }
                });

    }
}