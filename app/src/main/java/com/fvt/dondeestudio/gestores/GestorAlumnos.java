package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Clase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.fvt.dondeestudio.helpers.Callback;

import java.util.HashMap;
import java.util.Map;

public class GestorAlumnos {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public Boolean agregarAlumno(Alumno alumno) {
        mFirestore.collection("alumno").document(alumno.getId()).set(alumno);
        return null;
    }

    public void obtenerAlumno(String id, final Callback<Alumno> callback){
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("alumno").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               Alumno alumno= documentSnapshot.toObject(Alumno.class);
               alumno.setId(documentSnapshot.getId());
                callback.onComplete(alumno);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onComplete(null);
            }
        });
    }

}
