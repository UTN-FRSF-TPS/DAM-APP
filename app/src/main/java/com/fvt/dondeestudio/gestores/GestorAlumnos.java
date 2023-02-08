package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Clase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GestorAlumnos {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public Boolean agregarAlumno(Alumno alumno) {
        mFirestore.collection("alumno").document(alumno.getId()).set(alumno);
        return null;
    }

}
