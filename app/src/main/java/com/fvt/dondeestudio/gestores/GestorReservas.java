package com.fvt.dondeestudio.gestores;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Reserva;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorReservas {

private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public ArrayList<Reserva> getReservasNuevas(String idProfesor){
        CollectionReference reservationsRef = db.collection("clase");
        ArrayList<Reserva> retorno = new ArrayList<Reserva>();
        reservationsRef
                .whereEqualTo("idProfesor", idProfesor)
                .whereEqualTo("estado", "nueva")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> reservationData = document.getData();
                                String estudiante = (String) reservationData.get("idAlumno");
                                //retorno.add(new Reserva());
                            }
                        } else {
                            Log.d("Firestore", "Error al obtener los datos: " + task.getException());
                        }
                    }
                });

        return retorno;
    }

    public void guardarReserva(String idAlumno, String idClase, String estado) {
            Map<String, Object> map = new HashMap<>();
            map.put("idAlumno", idAlumno);
            map.put("idClase", idClase);
            map.put("estado", "nueva");
            map.put("idProfesor", "iriaiddeusuarioprofesor");
            //estado: nueva, leida, cancelada
            db.collection("reserva").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                public void onSuccess(DocumentReference documentReference) {
                    //insertado
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {

                    //error;
                }
            });
        }
    }
