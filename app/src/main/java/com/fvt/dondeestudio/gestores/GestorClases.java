package com.fvt.dondeestudio.gestores;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.DTO.ClaseDTO;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GestorClases {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void agregarClase(Clase clase) {

        db.collection("clase").add(clase);
    }


    public boolean eliminarClase(String id) {

        db.collection("clase").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Borrado correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("No se pudo borrar");
            }
        });
        return true;
    }

    public void getClase(String id, final Callback<Clase> callback) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("clase").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Clase clase = documentSnapshot.toObject(Clase.class);
                clase.setID(documentSnapshot.getId());
                callback.onComplete(clase);
            }
        });


    }

    public interface Callback<T> {
        void onComplete(T data);
    }

    public void actualizarClase(String id, Clase actualizada) {
        Map<String, Object> nuevo = new HashMap<>();
        nuevo.put("tarifaHoras", actualizada.getTarifaHora());
        nuevo.put("asignatura", actualizada.getAsignatura());

        db.collection("clase").document(id).update(nuevo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Actualizada correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }

    @SuppressLint("SuspiciousIndentation")
    public void filtrarClases(ClaseDTO filtro, final Callback<ArrayList<Clase>> callback) {
        Query q1 = FirebaseFirestore.getInstance().collection("clase");
        if (filtro.getAsignatura() != null)
            q1 = q1.whereEqualTo("asignatura", filtro.getAsignatura());
        if (filtro.getTarifaHoraMax() != null)
            q1 = q1.whereLessThanOrEqualTo("tarifaHora", filtro.getTarifaHoraMax());
        if (filtro.getNivel() != null)
            q1 = q1.whereEqualTo("nivel", filtro.getNivel());
        if (filtro.getValoracionProfesor() != null)
            q1 = q1.whereGreaterThanOrEqualTo("profesor.valoracion", filtro.getValoracionProfesor());
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Clase> clases = new ArrayList<Clase>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Clase clase = document.toObject(Clase.class);
                        clase.setID(document.getId());
                        clases.add(clase);
                    }
                }
                callback.onComplete(clases);
            }
        });




    }


}




