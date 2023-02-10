package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.DTO.ReservaDTO;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.listeners.AlumnoClaseListener;
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

/* En teoria ya no se deberia usar, lo dejo por las dudas
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
*/

    /**
     *
     * @param idAlumno
     * @param clase
     *
     * Crea una reserva a una clase si hay cupos disponibles con estado="pendiente"
     */
    public void guardarReserva(String idAlumno, Clase clase) {
        Map<String, Object> map = new HashMap<>();
        map.put("idAlumno", idAlumno);
        map.put("idClase", clase.getId());
        map.put("estado", "pendiente");
        map.put("idProfesor", clase.getProfesor().getId());
        if (clase.getCupo() > 0) {
            db.collection("reserva").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                public void onSuccess(DocumentReference documentReference) {
                    GestorClases g = new GestorClases();
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Ocurrio un error al registrar la clase");
                }
            });
        } else {
            /*
                Si el usuario quiere escuchar la clase debe iniciar
                AlumnoClaseListener.seguirClase(clase.getId());
            */

            System.out.println("No existen cupos para esta clase. Puede suscribirse para saber si se libera un cupo");
;        }
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
     * PARA RECHAZAR RESERVA estado="rechazada", cupo =1
     * PARA CANCELAR RESERVA estado="cancelada", cupo=1
     */


    public void cambiarEstadoReserva(String idReserva, String estado, Integer cupo){
        Map<String, Object> confirmacion= new HashMap<>();
        confirmacion.put("estado", estado);
        //se debe notificar al alumno.
        db.collection("reserva").document(idReserva).update(confirmacion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                db.collection("reserva").document(idReserva).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        GestorClases g = new GestorClases();
                        String idClase = (String) documentSnapshot.get("idClase");
                        g.cambiarCupo(idClase, cupo);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }
}
