package com.fvt.dondeestudio.gestores;

import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GestorProfesores {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public Boolean agregarProfesor(Profesor profesor) {
        mFirestore.collection("profesor").document(profesor.getId()).set(profesor);
        return null;
    }

    public void obtenerProfesor(String id, final GestorClases.Callback<Profesor> callback){
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("profesor").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Profesor profesor= documentSnapshot.toObject(Profesor.class);
                profesor.setId(documentSnapshot.getId());
                callback.onComplete(profesor);
            }
        });
    }

}
