package com.fvt.dondeestudio.gestores;

import androidx.annotation.NonNull;

import com.fvt.dondeestudio.model.Clase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GestorClases {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public Boolean agregarClase(Clase clase) {
        Map<String, Object> map = new HashMap<>();
        map.put("tarifaHoras", clase.getTarifaHora());
        map.put("asignatura", clase.getAsignatura());
        //...seguir agregando, ver como es el tema de agregar un alumno o profesor

        mFirestore.collection("clase").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Cargado");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error");
            }
        });
        return null;
    }


    public boolean eliminarClase(String id) {

        mFirestore.collection("clase").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Borrado correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("No se pudo borrar");
            }
        });
        return true;
    }

    public void getClase(String id){
        mFirestore.collection("clase").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                System.out.println("Tarifa: " + documentSnapshot.get("tarifaHoras"));
                System.out.println("Asignatura: " + documentSnapshot.get("asignatura"));
                System.out.println("IDFirebase: " + documentSnapshot.getId());
                //la idea es despues devolver un objeto clase con los campos, esto es para probar nomas
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error");
            }
        });
    }

    public void actualizarClase(String id, Clase actualizada){
        Map<String, Object> nuevo= new HashMap<>();
        nuevo.put("tarifaHoras", actualizada.getTarifaHora());
        nuevo.put("asignatura", actualizada.getAsignatura());

        mFirestore.collection("clase").document(id).update(nuevo).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}


