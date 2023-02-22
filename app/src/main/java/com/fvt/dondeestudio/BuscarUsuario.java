package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fvt.dondeestudio.adapters.UsuarioAdapter;
import com.fvt.dondeestudio.databinding.FragmentBuscarUsuarioBinding;
import com.fvt.dondeestudio.databinding.FragmentRegistroBinding;
import com.fvt.dondeestudio.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuscarUsuario extends Fragment {

    FragmentBuscarUsuarioBinding binding;

    private RecyclerView recyclerView;

    private TextView textoEmail;

    private UsuarioAdapter usuarioAdapter;
    List<Usuario> listaUsuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBuscarUsuarioBinding.inflate(inflater, container, false);

        recyclerView = binding.recyclerUsuarios;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        textoEmail = binding.textoEmailBusqueda;

        binding.botonBuscar.setOnClickListener(lambda -> getUsers());

        return binding.getRoot();
    }

    public void getUsers() {
        listaUsuarios = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference alumnos = db.collection("alumno");
        CollectionReference profesores = db.collection("profesor");

        Query queryAlumnos = alumnos.whereEqualTo("email", textoEmail.getText().toString());
        Query queryProfesores = profesores.whereEqualTo("email", textoEmail.getText().toString());
        Task<QuerySnapshot> taskAlumnos = queryAlumnos.get();
        Task<QuerySnapshot> taskProfesores = queryProfesores.get();

        Tasks.whenAll(taskAlumnos, taskProfesores)
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot esteDocumento : taskAlumnos.getResult().getDocuments()) {
                        String id = (String) esteDocumento.get("id");
                        String nombre = (String) esteDocumento.get("nombre");
                        String apellido = (String) esteDocumento.get("apellido");
                        String email = (String) esteDocumento.get("email");
                        //String photoUrl = (String) esteDocumento.get("photoUrl");

                        Usuario esteUsuario = new Usuario(id, nombre, apellido, email);
                        listaUsuarios.add(esteUsuario);
                    }

                    for (DocumentSnapshot esteDocumento : taskProfesores.getResult().getDocuments()) {
                        String id = (String) esteDocumento.get("id");
                        String nombre = (String) esteDocumento.get("nombre");
                        String apellido = (String) esteDocumento.get("apellido");
                        String email = (String) esteDocumento.get("email");
                        //String photoUrl = (String) esteDocumento.get("photoUrl");

                        Usuario esteUsuario = new Usuario(id, nombre, apellido, email);
                        listaUsuarios.add(esteUsuario);
                    }

                    usuarioAdapter = new UsuarioAdapter(getContext(), listaUsuarios);
                    recyclerView.setAdapter(usuarioAdapter);
                });




        System.out.println(listaUsuarios.size());

    }
}