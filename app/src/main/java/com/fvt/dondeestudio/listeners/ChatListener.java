package com.fvt.dondeestudio.listeners;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;

import com.fvt.dondeestudio.MessageActivity;
import com.fvt.dondeestudio.helpers.NotificacionHelper;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.receivers.NuevaReservaReceiver;
import com.fvt.dondeestudio.receivers.NuevoMensajeReceiver;
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
/**
 * PERMITE NOTIFICAR AL USUARIO CUANDO RECIBE UN NUEVO MENSAJE.
 *
 */
    public static void seguirChat(String idUsuario, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("chats");
        collRef.whereEqualTo("receiver", idUsuario).whereEqualTo("leido", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        NuevoMensajeReceiver receiver = new NuevoMensajeReceiver();
                        IntentFilter intentFilter = new IntentFilter("nuevoMensaje");
                        context.registerReceiver(receiver, intentFilter);
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
                                                Intent nuevoMensaje = new Intent();
                                                nuevoMensaje.setAction("nuevoMensaje");
                                                nuevoMensaje.putExtra("chatId", documentSnapshot.getId());
                                                System.out.println("ID A DONDE VA EL MSJ: " + documentSnapshot.getId());
                                                NotificacionHelper.showNotification(context, "Nuevo mensaje", "Tenes un nuevo mensaje de " + nombre, nuevoMensaje);
                                              } else {
                                                Task<DocumentSnapshot> profesorTask = db.collection("profesor").document(idSender).get();
                                                profesorTask.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            String nombre = documentSnapshot.getString("nombre") + " " + documentSnapshot.getString("apellido");
                                                            Intent nuevoMensaje = new Intent();
                                                            nuevoMensaje.setAction("nuevoMensaje");
                                                            System.out.println("EL MENSAJE VA A IR A" + documentSnapshot.getId());
                                                            nuevoMensaje.putExtra("chatId", documentSnapshot.getId());
                                                                NotificacionHelper.showNotification(context, "Nuevo mensaje", "Tenes un nuevo mensaje de " + nombre, nuevoMensaje);
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