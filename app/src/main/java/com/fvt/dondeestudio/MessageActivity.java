package com.fvt.dondeestudio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fvt.dondeestudio.adapters.MessageAdapter;
import com.fvt.dondeestudio.databinding.ActivityMessageBinding;
import com.fvt.dondeestudio.model.Chat;
import com.fvt.dondeestudio.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    ActivityMessageBinding binding;

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DocumentReference reference;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageButton btn_send;
        EditText text_send;

        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recycler;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        profile_image = binding.profileImage;
        username = binding.username;
        text_send = binding.textSend;
        btn_send = binding.btnSend;

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(view -> {
            String message = text_send.getText().toString();
            if (!message.equals("")) {
                sendMessage(fuser.getUid(), userId, message);
            }
            text_send.setText("");
        });


        System.out.println("recibiendo " + userId);

        System.out.println("id " + userId);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance().collection("profesor").document(userId);;

        //todo: pasar a archivos separados
        reference.get().addOnCompleteListener(task -> {
            System.out.println(task.getResult().get("email"));
            Usuario user = task.getResult().toObject(Usuario.class);
            if (user != null) {
                username.setText(user.getNombre() + " " + user.getApellido());
                if (user.getPhotoUrl() == null) {
                    profile_image.setImageResource(R.drawable.ic_baseline_person_24);

                }
                else {
                    Glide.with(MessageActivity.this).load(user.getPhotoUrl()).into(profile_image);
                }
            }
            else {
                DocumentReference alumnoReference= FirebaseFirestore.getInstance().collection("alumno").document(userId);;
                alumnoReference.get().addOnCompleteListener(task2 -> {
                    Usuario userAlumno = task2.getResult().toObject(Usuario.class);
                    if (userAlumno.getId() != null) {
                        username.setText(userAlumno.getNombre() + " " + userAlumno.getApellido());
                        if (userAlumno.getPhotoUrl() == null) {
                            profile_image.setImageResource(R.drawable.ic_baseline_person_24);

                        }
                        else {
                            Glide.with(MessageActivity.this).load(user.getPhotoUrl()).into(profile_image);
                        }
                    }

                    if (userAlumno != null) readMessages(fuser.getUid(), userId, userAlumno.getPhotoUrl());
                });
            }
            if (user != null) readMessages(fuser.getUid(), userId, user.getPhotoUrl());
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference chats = db.collection("chats");

        Chat chat = new Chat(sender, receiver, message);

        chats.add(chat);

    }

    private void readMessages(String myid, String userid, String imageurl) {

        CollectionReference chatReference = FirebaseFirestore.getInstance().collection("chats");
        chatReference.addSnapshotListener((value, e) -> {
            mchat = new ArrayList<>();
            if (e == null) {

                for (QueryDocumentSnapshot doc : value) {
                    Chat chat = doc.toObject(Chat.class);
                    System.out.println(chat);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                        chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                    System.out.println(messageAdapter.getItemCount());
                }
            }
        });
    }

}