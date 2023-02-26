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
import android.widget.Toast;

import com.fvt.dondeestudio.databinding.FragmentRegistroBinding;
import com.fvt.dondeestudio.gestores.GestorAlumnos;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.listeners.AlumnoReservasListener;
import com.fvt.dondeestudio.listeners.ChatListener;
import com.fvt.dondeestudio.listeners.ProfesorReservasListener;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Chat;
import com.fvt.dondeestudio.model.Profesor;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.checkerframework.checker.units.qual.C;

public class RegistroFragment extends Fragment {
    private FragmentRegistroBinding binding;


    private void registrar() {

        FirebaseUser user = getArguments().getParcelable("user");

        String email = binding.email.getText().toString();
        String nombre = binding.nombre.getText().toString();
        String apellido = binding.apellido.getText().toString();
    if(email.length() != 0 && nombre.length() != 0 && apellido.length() != 0) {
        user.updateEmail(email);
        UserProfileChangeRequest cambioNombre = new UserProfileChangeRequest.Builder()
                .setDisplayName(nombre + " " + apellido)
                .build();

        user.updateProfile(cambioNombre);

        if (binding.rol.getSelectedItem().equals("Alumno")) {
            Alumno alumno = new Alumno(user.getUid(), email, nombre, apellido, user.getPhoneNumber());
            GestorAlumnos gestor = new GestorAlumnos();
            gestor.agregarAlumno(alumno, new Callback<Boolean>() {
                @Override
                public void onComplete(Boolean resultado) {
                    if (resultado) {
                        AlumnoReservasListener.seguirReserva(user.getUid(), getContext());
                        ChatListener.seguirChat(user.getUid(), getContext());
                        Toast.makeText(getContext(), "Te registraste correctamente!", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_registroFragment_to_buscarClasesFragment, null);
                    } else {
                        Toast.makeText(getContext(), "Ocurrio un error al regristrarte. Intenta mas tarde.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Profesor profesor = new Profesor(user.getUid(), email, nombre, apellido, user.getPhoneNumber());
            GestorProfesores gestor = new GestorProfesores();
            gestor.agregarProfesor(profesor, new Callback<Boolean>() {
                @Override
                public void onComplete(Boolean retorno) {
                    if(retorno) {
                        ProfesorReservasListener.seguirReserva(user.getUid(), getContext());
                        Toast.makeText(getContext(), "Te registraste correctamente!", Toast.LENGTH_LONG).show();
                        ChatListener.seguirChat(user.getUid(), getContext());
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_registroFragment_to_agregarClaseFragment, null);
                    } else {
                        Toast.makeText(getContext(), "Ocurrio un error al registrarte. Intenta mas tarde.", Toast.LENGTH_LONG).show();
                    }
                    }
            });
        }
    } else {
        Toast.makeText(getContext(), "No completaste todos los datos. Completalos", Toast.LENGTH_LONG).show();
    }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        binding.btnRegistro.setOnClickListener(lambda -> registrar());
        return binding.getRoot();
    }
}