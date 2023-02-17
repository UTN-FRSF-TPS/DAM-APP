package com.fvt.dondeestudio.gestores;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.DTO.ClaseDTO;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Valoracion;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorClases {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * Agrega la clase a la coleccion de clases. La clase debe tener el objeto profesor incluido
     * @param clase
     */
    public void agregarClase(Clase clase) {

        db.collection("clase").add(clase);
    }

    /**
     * Se elimina la clase id, eliminando todas las reservas que existan para la clase.
     * Al eliminar las reservas, se notifica a los alumnos que habian reservados que la clase se elimino.
     * @param id
     */

    public void eliminarClase(String id) {

        db.collection("clase").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                db.collection("reserva").whereEqualTo("idClase", id).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    documentSnapshot.getReference().delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("a", "Eliminado? ok");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("a", "Error borrando documento", e);
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("a", "Error getting documents.", e);
                            }
                        });
            }
        });
    }

    /**
     *
     * @param idClase
     * @param idUsuario
     * @param calif
     *
     * Agrega la retroalimentacion del usuario idUsuario con valor calif en la clase idClase
     * La agrega si el usuario todavia no hizo una retroalimentacion en la clase
     * Cuando se agrega la retroalimentacion se actualiza el valor de la clase en base al valor ingresado
     */

    public void agregarRetroalimentacion(String idClase, String idUsuario, Integer calif){
        Valoracion valoracion = new Valoracion(calif.longValue(), idUsuario);
        DocumentReference docRef = db.collection("clase").document(idClase);
        this.ClaseTieneRetroalimentacionDeUsuario(idClase, idUsuario, new Callback<Boolean>() {
                    @Override
                    public void onComplete(Boolean ret) {
                        if(!ret) {
                            docRef.update("valoraciones", FieldValue.arrayUnion(valoracion));
                            calculaReatroalimentaciones(idClase, new Callback<Map<String, Object>>() {
                                @Override
                                public void onComplete(Map<String, Object> data) {
                                    Integer tam = (Integer) data.get("tam");
                                    Long suma = (Long) data.get("suma");
                                    Double prom = Double.valueOf(suma)/Double.valueOf(tam);
                                    docRef.update("valoracion", prom);
                                }

                            });
                        }else
                            System.out.println("Ya hizo una valoracion");
                    }
                });

    }


    /**
     *
     * @param idClase
     * @param idUsuario
     * @param callback
     *
     * Devuelve en el callback true si el usuario idUsuario ya hizo una retroalimentacion en la clase idClase
     * False en caso contrario
     */
    public void ClaseTieneRetroalimentacionDeUsuario(String idClase,String idUsuario, final Callback<Boolean> callback){
        db.collection("clase").document(idClase).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<HashMap<String, Object>> valoracionesFB = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("valoraciones");
                ArrayList<Valoracion> valoraciones = new ArrayList<>();
                Boolean retorno= false;
                for (HashMap<String, Object> valoracionFB : valoracionesFB) {
                    Long puntaje = (Long) valoracionFB.get("puntaje");
                    String usuarioId = (String) valoracionFB.get("usuarioId");
                    Valoracion valoracion = new Valoracion(puntaje, usuarioId);
                    valoraciones.add(valoracion);
                }
                for(Valoracion valoracion2 : valoraciones){
                    if(valoracion2.getUsuarioId().equals(idUsuario)) {
                        retorno = true;
                        break;
                    }
                }
                System.out.println("RETORNO:" + retorno);
                callback.onComplete(retorno);

            }});



    }

    /**
     * Calcula la retroalimentacion de la clase idClase en base a todas las retroalimentaciones que tiene la clase
     * @param idClase
     * @param callback
     */
    private void calculaReatroalimentaciones(String idClase, final Callback<Map<String, Object>> callback) {
        db.collection("clase").document(idClase).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<HashMap<String, Object>> valoracionesFB = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("valoraciones");
                ArrayList<Valoracion> valoraciones = new ArrayList<>();
                for (HashMap<String, Object> valoracionFB : valoracionesFB) {
                    Long puntaje = (Long) valoracionFB.get("puntaje");
                    String usuarioId = (String) valoracionFB.get("usuarioId");
                    Valoracion valoracion = new Valoracion(puntaje, usuarioId);
                    valoraciones.add(valoracion);
                }
                Integer totalValoraciones = valoraciones.size();
                Long sumaValoraciones = Long.valueOf(0);

                for (Valoracion valoracion : valoraciones) {
                    sumaValoraciones += valoracion.getPuntaje();
                }
                Map<String, Object> retorno = new HashMap<>();
                retorno.put("tam", totalValoraciones);
                retorno.put("suma", sumaValoraciones);
                callback.onComplete(retorno);
            }
        });
    }


    /**
     *
     * @param id
     * @param callback
     *
     * Devuelve en el callback el objeto clase correspondiente al id
     */
    public void getClase(String id, final Callback<Clase> callback) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("clase").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Clase clase = documentSnapshot.toObject(Clase.class); //Falla pq no tiene el profesor
               System.out.println("Asignatura" + clase.getAsignatura());
                if(clase != null)
                    clase.setId(documentSnapshot.getId());
                callback.onComplete(clase);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("FALLA" + e.getMessage());
            }
        });


    }

    /**
     *
     * @param id
     * @param tarifa
     *
     * Permite cambiiar la tarifa de la clase.
     */

    public void actualizarClase(String id, double tarifa) {
        Map<String, Object> nuevo = new HashMap<>();
        nuevo.put("tarifaHoras", tarifa);

        db.collection("clase").document(id).update(nuevo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Actualizada correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }

    /**
     *
     * @param filtro
     * @param callback
     * @param ubicacion
     *
     * Devuelve en el callback las clases que cumplen con el filtro de busqueda filtro
     * TODO: REFACTORIZAR EL METODO
     */

    //si FiltroDTO tiene radioMax null, ubicacion se puede pasar como nulo.
    public void filtrarClases(ClaseDTO filtro, final Callback<ArrayList<Clase>> callback, LatLng ubicacion) {

        Query q1 = FirebaseFirestore.getInstance().collection("clase");
        if (filtro.getAsignatura() != null)
            q1 = q1.whereEqualTo("asignatura", filtro.getAsignatura());
        if (filtro.getNivel() != null)
            q1 = q1.whereEqualTo("nivel", filtro.getNivel());
        if (filtro.getTipo() != null)
            q1 = q1.whereEqualTo("tipo", filtro.getTipo());
        if (filtro.getTarifaHoraMax() != null)
            q1 = q1.whereLessThanOrEqualTo("tarifaHora", filtro.getTarifaHoraMax());
        if (filtro.getValoracionProfesor() != null)
        Tasks.whenAllSuccess(q1.get()).addOnCompleteListener(new OnCompleteListener<List<Object>>() {
            @Override
            public void onComplete(@NonNull Task<List<Object>> task) {
                ArrayList<Clase> clases = new ArrayList<Clase>();
                if (task.isSuccessful()) {
                    QuerySnapshot q1Result = (QuerySnapshot) task.getResult().get(0);
                    for (DocumentSnapshot document : q1Result) {
                            String horario = (String) document.get("horario");
                            DateTimeFormatter formatter = null;
                            LocalDateTime dateTime = null;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                dateTime = LocalDateTime.parse(horario, formatter);
                                if (dateTime.isAfter(LocalDateTime.now())) {

                                    //Controlar aca que la valoracion del filtro sea mayor a la valoracion del profesor
                                    GestorProfesores gP = new GestorProfesores();
                                    gP.calcularReputacion(document.get("profesor.id").toString(), new Callback<Double>() {
                                        @Override
                                        public void onComplete(Double reputacion) {
                                            if(reputacion > filtro.getValoracionProfesor()){

                                                if (filtro.getRadioMaxMetros() != null && filtro.getTipo().equals("Presencial")) {

                                                    GeoPoint ubi = document.getGeoPoint("ubicacion");
                                                    if (Util.calcularDistancia(filtro.getUbicacion(), new LatLng(ubi.getLatitude(), ubi.getLongitude())) < filtro.getRadioMaxMetros()) {

                                                        Clase clase = document.toObject(Clase.class); //si es menor la distancia lo agrego.
                                                        clase.setId(document.getId());
                                                        clases.add(clase);
                                                        System.out.println("ID " + clases.get(0).getId());
                                                    }
                                                } else { //si no es presencial lo agrego directamente
                                                    Clase clase = document.toObject(Clase.class);
                                                    clase.setId(document.getId());
                                                    clases.add(clase);
                                                }
                                            }
                                            callback.onComplete(clases);
                                        }
                                    });
                                } //fecha anterior a la actual
                            } //version
                        } //recorre documentos
                } //si fue exitoso
            } //callback
        });
    }

    /**
     *
     * @param idAlumno
     * @param callback
     * Devuelve en el callback los objetos clases que reservó el alumno
     *
     */

    public void claseReservadasAlumno(String idAlumno, final Callback<ArrayList<Clase>> callback){
        Query q1 = FirebaseFirestore.getInstance().collection("reserva").whereEqualTo("idAlumno", idAlumno);
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final ArrayList<Clase> clases = new ArrayList<Clase>();
                if (task.isSuccessful()) {
                    final int[] count = {0};
                    for (DocumentSnapshot document : task.getResult()) {
                        getClase(document.get("idClase").toString(), new Callback<Clase>() {
                            @Override
                            public void onComplete(Clase clase) {
                                clase.setEstadoUsuario(document.get("estado").toString()); //se setea el estado de la reserva para mostrarlo en el cardview
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                    LocalDateTime dateTime = LocalDateTime.parse(clase.getHorario(), formatter);
                                    if(dateTime.isAfter(LocalDateTime.now()))
                                        clases.add(clase);
                                }
                                count[0]++;
                                if (count[0] == task.getResult().size()) { //si ya estan todos los registros llamo a on complete
                                    callback.onComplete(clases);
                                }
                            }
                        });
                    }
                } else {
                    callback.onComplete(null);
                }
            }
        });
    }

    /**
     *
     * @param idProfesor
     * @param callback
     *
     * Devuelve en el callback los objetos clases que fueron creadas por el profesor
     * Solo devuelve las clases creadas con fecha posterior a la actual
     */

    public void claseReservadasProfesor(String idProfesor, final Callback<ArrayList<Clase>> callback) {

        Query q1 = FirebaseFirestore.getInstance().collection("clase").whereEqualTo("idProfesor", idProfesor);
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Clase> clases = new ArrayList<Clase>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Clase clase = document.toObject(Clase.class);
                            clase.setId(document.getId());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                            LocalDateTime dateTime = LocalDateTime.parse(clase.getHorario(), formatter);
                            if(dateTime.isAfter(LocalDateTime.now()))
                                clases.add(clase);
                        }
                    }
                }
                callback.onComplete(clases);
            }
        });
    }

    /**
     * Aumenta o disminuye el cupo de la clase claseId en cant unidades
     * @param claseId
     * @param cant
     */
        public void cambiarCupo(String claseId, Integer cant){
            db.collection("clase").document(claseId).update("cupo", FieldValue.increment(cant)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    System.out.println("Disminuido cupo");
                }
            });
        }
    }




