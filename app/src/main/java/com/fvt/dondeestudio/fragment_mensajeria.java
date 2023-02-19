package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.adapters.UsuarioAdapter;
import com.fvt.dondeestudio.databinding.FragmentLoginBinding;
import com.fvt.dondeestudio.databinding.FragmentMensajeriaBinding;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Chat;
import com.fvt.dondeestudio.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class fragment_mensajeria extends Fragment {

    private FragmentMensajeriaBinding binding;

    private RecyclerView recyclerView;

    private UsuarioAdapter userAdapter;
    private List<Usuario> mUsers;

    private FirebaseUser fuser;


    private List<String> userList;
    private CollectionReference reference;

    public void pasarAAgregar(View view) {
        Navigation.findNavController(view).navigate(R.id.action_fragment_mensajeria_to_buscarUsuario);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMensajeriaBinding.inflate(inflater, container, false);
        binding.botonAgregarChat.setOnClickListener(view -> pasarAAgregar(view));

        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        reference = FirebaseFirestore.getInstance().collection("chats");

        reference.addSnapshotListener((value, e) -> {
            userList.clear();
            if (e == null) {

                for (QueryDocumentSnapshot doc : value) {
                    Chat chat = doc.toObject(Chat.class);
                    if (chat.getSender().equals(fuser.getUid())) {
                        userList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(fuser.getUid())) {
                        userList.add(chat.getSender());
                    }
                }

                readChats();
            }
        });
        return binding.getRoot();
    }

    private void readChats() {
        mUsers = new ArrayList<>();

        CollectionReference referenceAlumno = FirebaseFirestore.getInstance().collection("alumno");
        CollectionReference referenceProfesor = FirebaseFirestore.getInstance().collection("profesor");


        mUsers.clear();
        referenceAlumno.addSnapshotListener((value, e) -> {

            for (QueryDocumentSnapshot doc : value) {
                Usuario alumno = doc.toObject(Usuario.class);

                for (String id : userList) {
                    if (alumno.getId().equals(id)) {
                        if (mUsers.size() != 0) {
                            if (!mUsers.contains(alumno))
                                    mUsers.add(alumno);
                            }
                            else {
                                mUsers.add(alumno);
                            }
                        }
                    }
                }

                userAdapter = new UsuarioAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);

            });

        referenceProfesor.addSnapshotListener((value, e) -> {
            System.out.println(userList);
            for (QueryDocumentSnapshot doc : value) {
                Usuario profesor = doc.toObject(Usuario.class);

                for (String id : userList) {
                    if (profesor.getId().equals(id)) {
                        if (mUsers.size() != 0) {
                            if (!mUsers.contains(profesor))
                                mUsers.add(profesor);
                        }
                    else {
                        mUsers.add(profesor);
                    }
                    }
                }
            }

            userAdapter = new UsuarioAdapter(getContext(), mUsers);
            recyclerView.setAdapter(userAdapter);

        });
    }
}