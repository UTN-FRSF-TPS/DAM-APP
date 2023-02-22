package com.fvt.dondeestudio;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.databinding.FragmentRegistroBinding;
import com.fvt.dondeestudio.gestores.GestorAlumnos;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.listeners.AlumnoReservasListener;
import com.fvt.dondeestudio.listeners.ProfesorReservasListener;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Profesor;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistroFragment extends Fragment {
    private FragmentRegistroBinding binding;


    private void registrar() {

        FirebaseUser user = getArguments().getParcelable("user");

        String email = binding.textoEmail.getText().toString();
        String nombre = binding.textoNombre.getText().toString();
        String apellido = binding.textoApellido.getText().toString();

        user.updateEmail(email);
        UserProfileChangeRequest cambioNombre = new UserProfileChangeRequest.Builder()
                .setDisplayName(nombre + " " + apellido)
                .build();

        user.updateProfile(cambioNombre);

        if (binding.spinnerRol.getSelectedItem().equals("Alumno")) {
            Alumno alumno = new Alumno(user.getUid(), email, nombre, apellido, user.getPhoneNumber());
            GestorAlumnos gestor = new GestorAlumnos();
            gestor.agregarAlumno(alumno);
            AlumnoReservasListener.seguirReserva(user.getUid(), getContext());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_registroFragment_to_buscarClasesFragment, null);
        }
        else {
            Profesor profesor = new Profesor(user.getUid(), email, nombre, apellido, user.getPhoneNumber());
            GestorProfesores gestor = new GestorProfesores();
            gestor.agregarProfesor(profesor);
            ProfesorReservasListener.seguirReserva(user.getUid(), getContext());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_registroFragment_to_agregarClaseFragment, null);

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        binding.botonRegistrar.setOnClickListener(lambda -> registrar());
        return binding.getRoot();
    }
}