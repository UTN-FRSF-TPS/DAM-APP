package com.fvt.dondeestudio;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.databinding.FragmentRegistroBinding;
import com.fvt.dondeestudio.gestores.GestorAlumnos;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Profesor;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment {
    private FragmentRegistroBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void registrar() {

        FirebaseUser user = getArguments().getParcelable("user");

        String email = binding.textoEmail.getText().toString();
        String nombre = binding.textoNombre.getText().toString();
        String apellido = binding.textoApellido.getText().toString();

        user.updateEmail(email);


        if (binding.spinnerRol.getSelectedItem().equals("Alumno")) {
            Alumno alumno = new Alumno(user.getUid(), email, nombre, apellido);
            GestorAlumnos gestor = new GestorAlumnos();
            gestor.agregarAlumno(alumno);
        }
        else {
            Profesor profesor = new Profesor(user.getUid(), email, nombre, apellido);
            GestorProfesores gestor = new GestorProfesores();
            gestor.agregarProfesor(profesor);
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