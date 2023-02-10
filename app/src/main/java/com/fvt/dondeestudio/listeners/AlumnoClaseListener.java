package com.fvt.dondeestudio.listeners;

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

public class AlumnoClaseListener {

    /**
     * Permite notificar al alumno cuando se libera un cupo en una clase en la que no habia lugar
     * Se escucha si el estado de alguna reserva de la clase idClase cambia a rechazada o cancelada
     * @param idClase
     * TODO Generar la notificacion para el alumno
     */
    public static void seguirClase(String idClase) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("reserva");
        collRef.whereEqualTo("idClase", idClase)
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

                                    String estado = (String) dc.getDocument().get("estado");
                                   if(estado.equals("cancelada")  || estado.equals("rechazada"))
                                        System.out.println("Se libera un cupo");

                            }
                        }
                    }
                });
    }
}
