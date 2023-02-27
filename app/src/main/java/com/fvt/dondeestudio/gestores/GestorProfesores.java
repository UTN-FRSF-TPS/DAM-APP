package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class GestorProfesores {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public Boolean agregarProfesor(Profesor profesor, Callback<Boolean> resultado) {
        mFirestore.collection("profesor").document(profesor.getId()).set(profesor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    resultado.onComplete(true);
                } else {
                    resultado.onComplete(false);
                }
            }
        });
        return null;
    }

    public void obtenerProfesor(String id, final Callback<Profesor> callback){
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("profesor").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Profesor profesor= documentSnapshot.toObject(Profesor.class);
                if(profesor != null)
                    profesor.setId(documentSnapshot.getId());
                callback.onComplete(profesor);
            }
        });
    }

    public void calcularReputacion(String id, final Callback<Double> callback){
        mFirestore.collection("clase").whereEqualTo("profesor.id", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Long suma = 0L;
                Long tam = 0L;
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    suma = suma + doc.getLong("valoracion");
                    tam++;
                }
                Double prom = Double.valueOf(suma) / Double.valueOf(tam);
                if(prom == 0.0){
                    callback.onComplete(3.0);
                } else {
                    callback.onComplete(prom);
                }
            }
        });
    }

}
