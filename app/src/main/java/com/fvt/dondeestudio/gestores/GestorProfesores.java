package com.fvt.dondeestudio.gestores;

import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Profesor;
import com.google.firebase.firestore.FirebaseFirestore;

public class GestorProfesores {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public Boolean agregarProfesor(Profesor profesor) {
        mFirestore.collection("profesor").document(profesor.getId()).set(profesor);
        return null;
    }


}
