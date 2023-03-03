package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Profesor;
import com.fvt.dondeestudio.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GestorUsuarios {

    public void encuentraUsuario(String id, final Callback<Usuario> callback){
        if (id != null) {
            GestorAlumnos gestorAlumnos = new GestorAlumnos();
            Callback<Alumno> alumno;
            gestorAlumnos.obtenerAlumno(id, data -> {
                if (data == null) {
                    GestorProfesores gestorProfesores = new GestorProfesores();
                    gestorProfesores.obtenerProfesor(id, data1 -> {
                        if (data1 != null) callback.onComplete(data1);
                        else callback.onComplete(null);
                    });
                }
                else {
                    callback.onComplete(data);
                }
            });
        }
    }

}
