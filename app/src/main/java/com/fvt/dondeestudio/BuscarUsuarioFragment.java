package com.fvt.dondeestudio;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.test.espresso.idling.CountingIdlingResource;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fvt.dondeestudio.adapters.UsuarioAdapter;
import com.fvt.dondeestudio.databinding.FragmentBuscarUsuarioBinding;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.model.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuscarUsuarioFragment extends Fragment {
    FragmentBuscarUsuarioBinding binding;

    private RecyclerView recyclerView;

    private TextView textoEmail;
    private TextView textoTelefono;

    private UsuarioAdapter usuarioAdapter;
    List<Usuario> listaUsuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBuscarUsuarioBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerUsuarios;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textoEmail = binding.textoEmailBusqueda;
        textoTelefono = binding.buscaTelefono;
        binding.botonBuscar.setOnClickListener(lambda ->{
              if(Util.conectado(getContext())) {
                  getUsers(new Callback<List<Usuario>>() {
                      @Override
                      public void onComplete(List<Usuario> data) {
                          usuarioAdapter = new UsuarioAdapter(getContext(), listaUsuarios);
                          recyclerView.setAdapter(usuarioAdapter);
                      }
                  });
              } else{
                  Toast noConexion = Toast.makeText(getContext(), "En este momento no tenés internet. Por favor, cuando tengas conexión continua.", Toast.LENGTH_LONG);
                  noConexion.getView().setBackgroundColor(Color.RED);
                  noConexion.show();
              }
        });

        binding.botonTelefono.setOnClickListener(lambda -> {
            binding.textoEmailBusqueda.setVisibility(View.GONE);
            binding.buscaTelefono.setVisibility(View.VISIBLE);
        });
        binding.botonMail.setOnClickListener(lambda -> {
            binding.textoEmailBusqueda.setVisibility(View.VISIBLE);
            binding.buscaTelefono.setVisibility(View.GONE);
        });

        return binding.getRoot();
    }

    public void getUsers(Callback<List<Usuario>> resultados) {
        listaUsuarios = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference alumnos = db.collection("alumno");
        CollectionReference profesores = db.collection("profesor");

        if (textoEmail.getVisibility() == View.VISIBLE) {
            Task<QuerySnapshot> taskAlumnos = alumnos.whereEqualTo("email", textoEmail.getText().toString()).get();
            Task<QuerySnapshot> taskProfesores = profesores.whereEqualTo("email", textoEmail.getText().toString()).get();
            Tasks.whenAll(taskAlumnos, taskProfesores)
                    .addOnCompleteListener(task -> {
                        for (DocumentSnapshot esteDocumento : taskAlumnos.getResult().getDocuments()) {
                            Usuario esteUsuario = esteDocumento.toObject(Usuario.class);
                            listaUsuarios.add(esteUsuario);
                        }

                        for (DocumentSnapshot esteDocumento : taskProfesores.getResult().getDocuments()) {
                            Usuario esteUsuario = esteDocumento.toObject(Usuario.class);
                            listaUsuarios.add(esteUsuario);
                        }
                        resultados.onComplete(listaUsuarios);

                    });

        }
        else {
            Task<QuerySnapshot> taskAlumnos = alumnos.whereEqualTo("telefono", "+54" + textoTelefono.getText().toString()).get();
            Task<QuerySnapshot> taskProfesores = profesores.whereEqualTo("telefono", "+54" + textoTelefono.getText().toString()).get();

            Tasks.whenAll(taskAlumnos, taskProfesores)
                    .addOnCompleteListener(task -> {
                        for (DocumentSnapshot esteDocumento : taskAlumnos.getResult().getDocuments()) {
                            Usuario esteUsuario = esteDocumento.toObject(Usuario.class);
                            listaUsuarios.add(esteUsuario);
                        }

                        for (DocumentSnapshot esteDocumento : taskProfesores.getResult().getDocuments()) {
                            Usuario esteUsuario = esteDocumento.toObject(Usuario.class);
                            listaUsuarios.add(esteUsuario);
                        }
                        resultados.onComplete(listaUsuarios);
                    });
        }

    }


}