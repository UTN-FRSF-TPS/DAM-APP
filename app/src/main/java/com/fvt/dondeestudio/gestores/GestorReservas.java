package com.fvt.dondeestudio.gestores;

import android.content.Context;
import android.icu.text.BidiClassifier;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.DTO.ReservaDTO;
import com.fvt.dondeestudio.helpers.Callback;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GestorReservas {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
/* TODO VER NOTIFICACIONES DE RESERVA*/
public void getReservasNuevas(String idProfesor, final Callback<ArrayList<ReservaDTO>> callback){
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
                        GestorClases gC = new GestorClases();
                        final int[] numCalls = {documents.size()};
                        if(task.getResult().size() > 0) {
                            for (DocumentSnapshot documento : documents) {
                                ReservaDTO rDTO = new ReservaDTO();
                                gC.getClase(documento.get("idClase").toString(), new Callback<Clase>() {
                                    @Override
                                    public void onComplete(Clase data) {
                                        String fechaHora = data.getHorario();
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                        Date date = null;
                                        try {
                                           date = formatter.parse(fechaHora);
                                        } catch(Exception e){
                                            System.out.println("Error de parseo");
                                        }
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(date);
                                        if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                                            rDTO.setIdAlumno(documento.get("idAlumno").toString());
                                            rDTO.setIdProfesor(documento.get("idProfesor").toString());
                                            rDTO.setEstado(documento.get("estado").toString());
                                            rDTO.setIdClase(documento.get("idClase").toString());
                                            rDTO.setId(documento.getId());
                                            lista.add(rDTO);
                                        }
                                        numCalls[0]--;
                                        if (numCalls[0] == 0) {
                                            callback.onComplete(lista);
                                        }
                                    }
                                });
                            }
                        } else {
                            callback.onComplete(lista);
                        }
                    }
                }
            });
}


    /**
     * @param idAlumno
     * @param clase    Crea una reserva a una clase si hay cupos disponibles con estado="pendiente"
     */
    public void guardarReserva(String idAlumno, Clase clase, Callback<Integer> resultado) {
        Map<String, Object> map = new HashMap<>();
        map.put("idAlumno", idAlumno);
        map.put("idClase", clase.getId());
        map.put("estado", "pendiente");
        map.put("idProfesor", clase.getProfesor().getId());
        if (clase.getCupo() > 0) {
            usuarioReservoClase(idAlumno, clase.getId(), new Callback<Boolean>() {
                @Override
                public void onComplete(Boolean data) {
                    if (data == true)
                        System.out.println("Ya reservaste esta clase");
                    else {
                        db.collection("reserva").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            public void onSuccess(DocumentReference documentReference) {
                                GestorClases g = new GestorClases();
                                resultado.onComplete(1); //Reserva OK

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            public void onFailure(@NonNull Exception e) {
                                resultado.onComplete(2); //Error de Firebase
                            }
                        });
                    }
                }
            });
        } else {
           resultado.onComplete(3); //La clase no tiene mas cupos
        }
    }

    /**
     * CAMBIAR ESTADO DE UNA RESERVA
     *
     * @param idReserva
     * @param estado
     * @param cupo
     *
     *
     * PARA CONFIRMAR RESERVA estado="confirmada", cupo =-1
     * PARA RECHAZAR RESERVA estado="rechazada", cupo =0
     * PARA CANCELAR RESERVA estado="cancelada", cupo=0
     */


    public void cambiarEstadoReserva(String idReserva, String estado, Integer cupo, Callback<Integer> resultado){
        Map<String, Object> confirmacion= new HashMap<>();
        confirmacion.put("estado", estado);
        //se debe notificar al alumno.
        db.collection("reserva").document(idReserva).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().getString("estado").equals("pendiente")){
                    db.collection("reserva").document(idReserva).update(confirmacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            db.collection("reserva").document(idReserva).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    GestorClases g = new GestorClases();
                                    String idClase = (String) documentSnapshot.get("idClase");
                                    g.cambiarCupo(idClase, cupo);
                                    resultado.onComplete(1);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            resultado.onComplete(2);
                        }
                    });
                } else {
                    resultado.onComplete(3);
                }

            }
        });

    }

    public void usuarioReservoClase(String idAlumno, String idClase, Callback<Boolean> callback) {
        db.collection("reserva").whereEqualTo("idAlumno", idAlumno).whereEqualTo("idClase", idClase).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() > 0) {
                    callback.onComplete(true);
                } else {
                    callback.onComplete(false);
                }
            }
        });
    }
}
