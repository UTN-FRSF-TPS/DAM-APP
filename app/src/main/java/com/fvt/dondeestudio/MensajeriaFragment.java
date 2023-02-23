package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.adapters.UsuarioAdapter;
import com.fvt.dondeestudio.databinding.FragmentMensajeriaBinding;
import com.fvt.dondeestudio.model.Chat;
import com.fvt.dondeestudio.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MensajeriaFragment extends Fragment {

    private FragmentMensajeriaBinding binding;

    private RecyclerView recyclerView;

    private UsuarioAdapter userAdapter;
    private List<Usuario> mUsers;

    private FirebaseUser fuser;


    private Set<String> userList;
    private CollectionReference reference;

    public void pasarAAgregar(View view) {
        Navigation.findNavController(view).navigate(R.id.action_fragment_mensajeria_to_buscarUsuario);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMensajeriaBinding.inflate(inflater, container, false);
        binding.botonAgregarChat.setOnClickListener(view -> pasarAAgregar(view));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ActionBar barra =  ((AppCompatActivity) getActivity()).getSupportActionBar();
        barra.show();
        barra.setTitle("Mensajes");

        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new HashSet<>();
        reference = FirebaseFirestore.getInstance().collection("chats");
        reference.addSnapshotListener((value, e) -> {
            userList.clear();
            Integer cont = 0;
            if (e == null) {
//cuando hay un mensaje nuevo
                for (QueryDocumentSnapshot doc : value) {
                    Chat chat = doc.toObject(Chat.class);
                    if (chat.getSender().equals(fuser.getUid())) {
                        userList.add(chat.getReceiver());
                        cont++;
                    }
                    if (chat.getReceiver().equals(fuser.getUid())) {
                        userList.add(chat.getSender());
                        cont++;
                    }
                }
                if(cont == 0){
                    binding.resultado.setText("No tenes ningún chat :(");
                } else {
                    binding.resultado.setVisibility(View.GONE);
                }
                readChats();
            }
        });
        return binding.getRoot();
    }

    private void readChats() {
        mUsers = new ArrayList<>();
        mUsers.clear();

        for (String id : userList) {
            DocumentReference referenceAlumno = FirebaseFirestore.getInstance().collection("alumno").document(id);
            DocumentReference referenceProfesor = FirebaseFirestore.getInstance().collection("profesor").document(id);

            referenceAlumno.addSnapshotListener((value, error) -> {
                if (error == null) {
                    Usuario usuario = value.toObject(Usuario.class);
                    if (usuario != null) mUsers.add(usuario);
                    userAdapter = new UsuarioAdapter(getContext(), mUsers);
                    recyclerView.setAdapter(userAdapter);
                }
            });
            referenceProfesor.addSnapshotListener((value, error) -> {
                if (error == null) {
                    Usuario usuario = value.toObject(Usuario.class);
                    if (usuario != null) mUsers.add(usuario);
                    userAdapter = new UsuarioAdapter(getContext(), mUsers);
                    recyclerView.setAdapter(userAdapter);
                }
            });
        }

    }
}