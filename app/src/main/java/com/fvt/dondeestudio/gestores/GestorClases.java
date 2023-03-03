package com.fvt.dondeestudio.gestores;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.DTO.ClaseDTO;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.model.Alumno;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GestorClases {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * Agrega la clase a la coleccion de clases. La clase debe tener el objeto profesor incluido
     * @param clase
     */
    public void agregarClase(Clase clase, Callback<Boolean> insertado) {

        db.collection("clase").add(clase).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                    insertado.onComplete(true);
                else
                    insertado.onComplete(false);
            }
        });
    }

    /**
     * Se elimina la clase id, eliminando todas las reservas que existan para la clase.
     * Al eliminar las reservas, se notifica a los alumnos que habian reservados que la clase se elimino.
     * @param id
     */

    public void eliminarClase(String id, Callback<Integer> resultado) {

        db.collection("clase").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                db.collection("reserva").whereEqualTo("idClase", id).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size() > 0) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        documentSnapshot.getReference().delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        resultado.onComplete(1);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        resultado.onComplete(2);
                                                        ;
                                                    }
                                                });
                                    }
                                } else {
                                    resultado.onComplete(1);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                resultado.onComplete(3);
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

    public void agregarRetroalimentacion(String idClase, String idUsuario, Integer calif, Callback<Integer> resultado){
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
                                    docRef.update("valoracion", prom).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            resultado.onComplete(1);
                                        }    else {
                                            resultado.onComplete(2);
                                        }
                                        }
                                    });
                                }

                            });
                        }else
                           resultado.onComplete(3);
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
     * AL AGREGAR LA CLASE SE DEBE AGREGAR UN ARRAY VALORACIONES, QUE SEA NULO.
     */
    public void ClaseTieneRetroalimentacionDeUsuario(String idClase,String idUsuario, final Callback<Boolean> callback){

        db.collection("clase").document(idClase).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<HashMap<String, Object>> valoracionesFB = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("valoraciones");
                ArrayList<Valoracion> valoraciones = new ArrayList<>();
                Boolean retorno= false;
                if(valoracionesFB != null) {
                    for (HashMap<String, Object> valoracionFB : valoracionesFB) {
                        Long puntaje = (Long) valoracionFB.get("puntaje");
                        String usuarioId = (String) valoracionFB.get("usuarioId");
                        Valoracion valoracion = new Valoracion(puntaje, usuarioId);
                        valoraciones.add(valoracion);
                    }
                    for (Valoracion valoracion2 : valoraciones) {
                        if (valoracion2.getUsuarioId().equals(idUsuario)) {
                            retorno = true;
                            break;
                        }
                    }
                    callback.onComplete(retorno);
                } else {
                    callback.onComplete(false);
                }
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

    public void actualizarClase(String id, double tarifa, Callback<Boolean> resultado) {
        Map<String, Object> nuevo = new HashMap<>();
        nuevo.put("tarifaHora", tarifa);

        db.collection("clase").document(id).update(nuevo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                resultado.onComplete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              resultado.onComplete(false);
            }
        });

    }

    /**
     *
     * @param filtro
     * @param callback
     *
     * Devuelve en el callback las clases que cumplen con el filtro de busqueda filtro
     * TODO: REFACTORIZAR EL METODO
     */

        public void filtrarClases(ClaseDTO filtro, final Callback<ArrayList<Clase>> callback) {
            System.out.println("Asignatura" + filtro.getAsignatura())  ;
            System.out.println("Nivel" + filtro.getNivel())  ;
            System.out.println("Tipo" + filtro.getTipo())  ;
            System.out.println("Tarifa" + filtro.getTarifaHoraMax());
            Query q1 = FirebaseFirestore.getInstance().collection("clase");
            if (filtro.getNivel() != null)
                q1 = q1.whereEqualTo("nivel", filtro.getNivel());
            if (filtro.getTipo() != null)
                q1 = q1.whereEqualTo("tipo", filtro.getTipo());
            if (filtro.getTarifaHoraMax() != null)
                q1 = q1.whereLessThanOrEqualTo("tarifaHora", filtro.getTarifaHoraMax());
            final boolean[] arr = {false};
            Tasks.whenAllSuccess(q1.get()).addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                @Override
                public void onComplete(@NonNull Task<List<Object>> task) {
                    ArrayList<Clase> clases = new ArrayList<Clase>();
                    if (task.isSuccessful()) {
                        QuerySnapshot q1Result = (QuerySnapshot) task.getResult().get(0);
                        System.out.println("TAM: " + q1Result.size());
                        for (DocumentSnapshot document : q1Result) {
                            String horario = (String) document.get("horario");
                            DateTimeFormatter formatter = null;
                            LocalDateTime dateTime = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                dateTime = LocalDateTime.parse(horario, formatter);
                                if (dateTime.isAfter(LocalDateTime.now())) {
                                    System.out.println("Pasa fecha");
                                    //Controlar aca que la valoracion del filtro sea mayor a la valoracion del profesor
                                    GestorProfesores gP = new GestorProfesores();
                                    gP.calcularReputacion(document.get("profesor.id").toString(), new Callback<Double>() {
                                        @Override
                                        public void onComplete(Double reputacion) {
                                            System.out.println("Reputacion minima:" + filtro.getValoracionProfesor());
                                            System.out.println("Reputacion profesor: " + reputacion);
                                            if (reputacion >= filtro.getValoracionProfesor()) {
                                                System.out.println("Pasa reputacion");
                                                if (filtro.getRadioMaxMetros() != null && filtro.getTipo().equals("Presencial")) {
                                                    GeoPoint ubi = document.getGeoPoint("ubicacion");
                                                   // System.out.println("UBI YO" + filtro.getUbicacion());
                                                    // System.out.println("UBI CLASE" + document.getGeoPoint(("ubicacion")));
                                                    // System.out.println("DISTANCIA ES DE: " + Util.calcularDistancia(filtro.getUbicacion(), new LatLng(ubi.getLatitude(), ubi.getLongitude())));
                                                    if (Util.calcularDistancia(filtro.getUbicacion(), new LatLng(ubi.getLatitude(), ubi.getLongitude())) < filtro.getRadioMaxMetros()) {
                                                        System.out.println("UBI" + filtro.getUbicacion());
                                                        System.out.println("Llega aca?");
                                                        Clase clase = document.toObject(Clase.class); //si es menor la distancia lo agrego.
                                                        clase.setId(document.getId());
                                                        if(clase != null && (filtro.getAsignatura() == null || like(clase.getAsignatura().toLowerCase(Locale.ROOT), filtro.getAsignatura().toLowerCase(Locale.ROOT))))
                                                            clases.add(clase);
                                                        }
                                                } else { //si no es presencial lo agrego directamente


                                                    Clase clase = document.toObject(Clase.class);
                                                    clase.setId(document.getId());
                                                    if(filtro.getAsignatura() == null || like(clase.getAsignatura().toLowerCase(Locale.ROOT), filtro.getAsignatura().toLowerCase(Locale.ROOT)))
                                                         clases.add(clase);

                                                }

                                            }
                                            arr[0] = true;
                                            Collections.sort(clases);
                                            callback.onComplete(clases);
                                        }
                                    });
                                } //fecha anterior a la actual
                            } //version
                        } //recorre documentos
                    } //si fue exitoso
                    if (!arr[0]) {
                        Collections.sort(clases);
                        callback.onComplete(clases);
                    }
                } //callback
            });

        }


        public boolean like(String asignatura, String asignaturaFiltro){

        return asignatura.startsWith(asignaturaFiltro)
                || asignatura.endsWith(asignaturaFiltro)
                || asignatura.contains(asignaturaFiltro)
                || asignaturaFiltro.startsWith(asignatura)
                || asignaturaFiltro.endsWith(asignatura)
                || asignaturaFiltro.contains(asignatura);
       }





    /**
     *
     * @param idAlumno
     * @param callback
     * Devuelve en el callback los objetos clases que reservó el alumno
     *TODO: DEVOLVER TAMBIEN ANTERIORES A LA FECHA, Y SI LA FECHA ES ANTERIOR, PERMITIR AGREGAR RETROALIMENTACION
     *
     */

    public void claseReservadasAlumno(String idAlumno, final Callback<ArrayList<Clase>> callback){
        Query q1 = FirebaseFirestore.getInstance().collection("reserva").whereEqualTo("idAlumno", idAlumno);
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final ArrayList<Clase> clases = new ArrayList<Clase>();
                if (task.isSuccessful() && task.getResult().size() > 0) {
                    final int[] count = {0};
                    for (DocumentSnapshot document : task.getResult()) {
                        if(task.getResult().size() != 0) {
                            getClase(document.get("idClase").toString(), new Callback<Clase>() {
                                @Override
                                public void onComplete(Clase clase) {
                                    clase.setEstadoUsuario(document.get("estado").toString()); //se setea el estado de la reserva para mostrarlo en el cardview
                                    clases.add(clase);
                                    count[0]++;
                                    if (count[0] == task.getResult().size()) { //si ya estan todos los registros llamo a on complete
                                        Collections.sort(clases);
                                        callback.onComplete(clases);
                                    }
                                }
                            });
                        } else {
                            callback.onComplete(clases);
                        }
                    }
                } else {
                    Collections.sort(clases);
                    callback.onComplete(clases);
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

        Query q1 = FirebaseFirestore.getInstance().collection("clase").whereEqualTo("profesor.id", idProfesor);
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Clase> clases = new ArrayList<Clase>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Clase clase = document.toObject(Clase.class);
                            clase.setId(document.getId());
                                clases.add(clase);
                    }
                }
                if(clases.size() > 1)
                    Collections.sort(clases);
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

    public void getAlumnosClase(String idClase, final Callback<ArrayList<Alumno>> callback) {

        Query q1 = FirebaseFirestore.getInstance().collection("reserva").whereEqualTo("idClase", idClase).whereEqualTo("estado", "confirmada");
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Alumno> alumnos= new ArrayList<Alumno>();
                if (task.isSuccessful()) {
                    GestorAlumnos gA = new GestorAlumnos();
                    int numAlumnos = task.getResult().size(); // Obtener el número total de alumnos
                    final int[] numRespuestas = {0}; // Llevar un contador de respuestas recibidas
                    for (DocumentSnapshot document : task.getResult()) {
                        gA.obtenerAlumno(document.getString("idAlumno"), new Callback<com.fvt.dondeestudio.model.Alumno>() {
                            @Override
                            public void onComplete(com.fvt.dondeestudio.model.Alumno data) {
                                alumnos.add(data);
                                numRespuestas[0]++;
                                if (numRespuestas[0] == numAlumnos) {
                                    callback.onComplete(alumnos); // Llamar a callback.onComplete() una vez que se hayan recibido todas las respuestas
                                }
                            }
                        });
                    }
                } else {
                    callback.onComplete(alumnos); // Llamar a callback.onComplete() en caso de error
                }
            }
        });
    }


}




