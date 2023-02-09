package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.DTO.ReservaDTO;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Reserva;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorReservas {

private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void getReservasNuevas(String idProfesor, final GestorClases.Callback<ArrayList<ReservaDTO>> callback){
        CollectionReference reservationsRef = db.collection("reserva");
         reservationsRef.whereEqualTo("idProfesor", idProfesor).whereEqualTo("estado", "pendiente")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            List<DocumentSnapshot> documents = result.getDocuments();
                            ArrayList<ReservaDTO> lista =  new ArrayList<ReservaDTO>();
                           for(DocumentSnapshot documento : documents){
                               ReservaDTO rDTO = new ReservaDTO();
                                rDTO.setIdAlumno(documento.get("idAlumno").toString());
                                rDTO.setIdProfesor(documento.get("idProfesor").toString());
                                rDTO.setEstado(documento.get("estado").toString());
                                rDTO.setIdClase(documento.get("idClase").toString());
                                lista.add(rDTO);
                           }
                           callback.onComplete(lista);

                        }

                    }
                });
    }

    public void guardarReserva(String idAlumno, Clase clase) {
            Map<String, Object> map = new HashMap<>();
            map.put("idAlumno", idAlumno);
            map.put("idClase", clase.getId());
            map.put("estado", "pendiente");
            map.put("idProfesor", clase.getProfesor().getId());
            //estado: pendiente, aprobada, cancelada
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

    public void confirmarReserva(String idReserva){

        Map<String, Object> confirmacion= new HashMap<>();
        confirmacion.put("estado", "confirmada");

        db.collection("reserva").document(idReserva).update(confirmacion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Reserva confirmada ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void cancelarReserva(String idReserva){

        Map<String, Object> confirmacion= new HashMap<>();
        confirmacion.put("estado", "cancelada");

        db.collection("reserva").document(idReserva).update(confirmacion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Reserva confirmada ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }

    }
