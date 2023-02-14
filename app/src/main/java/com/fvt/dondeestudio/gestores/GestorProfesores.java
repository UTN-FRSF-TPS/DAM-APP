package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class GestorProfesores {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public Boolean agregarProfesor(Profesor profesor) {
        mFirestore.collection("profesor").document(profesor.getId()).set(profesor);
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
        mFirestore.collection("clase").whereEqualTo("profesor.idProfesor", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Long suma = 0L;
                Long tam = 0L;
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    suma = suma + doc.getLong("valoracion");
                    tam++;
                }
                Double prom = Double.valueOf(suma) / Double.valueOf(tam);
                callback.onComplete(prom);
            }
        });
    }

}
