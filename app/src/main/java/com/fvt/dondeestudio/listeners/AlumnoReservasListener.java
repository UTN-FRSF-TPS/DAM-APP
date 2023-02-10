package com.fvt.dondeestudio.listeners;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
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
    public static void seguirReserva(String idAlumno) {

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
                                                @Override
                                                public void onComplete(Profesor profesor) {
                                                    String nombre = profesor.getNombre();
                                                    String apellido = profesor.getApellido();
                                                    if (estado.equals("confirmado"))
                                                        System.out.println("El profesor" + nombre + " " + apellido + " acepto tu reserva a la clase de " + asignatura);
                                                    if (estado.equals("rechazada"))
                                                        System.out.println("El profesor" + nombre + " " + apellido + " rechazo tu reserva a la clase de " + asignatura);
                                                    //crear notificacion con esos datos;
                                                }
                                            });
                                        }
                                    });
                                    break;

                                case REMOVED:
                                    //se elimina la reserva pq se elimno la clase

                            }
                        }
                    }
                });

//Solo permitir eliminar clase si fecha < actual, y solo mostrar clases (tanto para alumnos y profesores)
        //cuya fecha es mayor a la actual.
    }
}