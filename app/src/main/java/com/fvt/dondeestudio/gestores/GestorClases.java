package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.model.Clase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void getClase(String id) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("clase").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Clase clase = documentSnapshot.toObject(Clase.class);
            }
        });
    }

    public void actualizarClase(String id, Clase actualizada){
        Map<String, Object> nuevo= new HashMap<>();
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

    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }



}


