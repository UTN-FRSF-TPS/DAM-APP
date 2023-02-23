package com.fvt.dondeestudio.listeners;

import android.content.Context;

import androidx.annotation.Nullable;

import com.fvt.dondeestudio.helpers.NotificacionHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatListener {

    public static void seguirChat(String idUsuario, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("chats");
        collRef.whereEqualTo("receiver", idUsuario)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    String idSender = (String) dc.getDocument().get("sender");
                                    Task<DocumentSnapshot> alumnoTask = db.collection("alumno").document(idSender).get();
                                    alumnoTask.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String nombre = documentSnapshot.getString("nombre") + " " + documentSnapshot.getString("apellido");
                                                NotificacionHelper.showNotification(context, "Nuevo mensaje", "Tenes un nuevo mensaje de " + nombre, null, null);
                                            } else {
                                                Task<DocumentSnapshot> profesorTask = db.collection("profesor").document(idSender).get();
                                                profesorTask.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            String nombre = documentSnapshot.getString("nombre") + " " + documentSnapshot.getString("apellido");
                                                            NotificacionHelper.showNotification(context, "Nuevo mensaje", "Tenes un nuevo mensaje de " + nombre, null, null);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    break;
                            }
                        }


                    }
                });
    }
}