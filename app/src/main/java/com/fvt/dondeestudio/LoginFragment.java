package com.fvt.dondeestudio;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fvt.dondeestudio.databinding.FragmentLoginBinding;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.listeners.AlumnoReservasListener;
import com.fvt.dondeestudio.listeners.ProfesorReservasListener;
import com.fvt.dondeestudio.model.Profesor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }
//TODO AGREGAR OTRO FRAGMENTO ANTES DEL LOGIN
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    private void loginOrRegister(View view) {
        TextView numeroText = binding.numeroText;
        CountryCodePicker codigoPais = binding.ccp;

        String numeroCompleto = "+" + codigoPais.getSelectedCountryCode() + numeroText.getText().toString();

        Bundle bundle = new Bundle();

        bundle.putString("numeroCompleto", numeroCompleto);

        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_verificacionFragment, bundle);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Configurar la Toolbar de la actividad
        binding = FragmentLoginBinding.inflate(inflater, container, false);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.getEmail() != null) {
            //esta logueado
            GestorProfesores g = new GestorProfesores();
            String idLog = FirebaseAuth.getInstance().getUid();
                g.obtenerProfesor(idLog, new Callback<Profesor>() {
                    @Override
                    public void onComplete(Profesor data) {
                        if (data == null) { //es alumno
                            AlumnoReservasListener.seguirReserva(idLog, getContext());
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("login", true);
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_buscarClasesFragment, bundle);
                        } else { //es profesor
                            ProfesorReservasListener.seguirReserva(idLog, getContext());
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_agregarClaseFragment, null);
                        }
                    }
                });

        } else {
            binding.botonLogin.setVisibility(View.VISIBLE);
            binding.ccp.setVisibility(View.VISIBLE);
            binding.numeroText.setVisibility(View.VISIBLE);
            binding.textoVerificacion.setVisibility(View.VISIBLE);
            binding.imageView2.setVisibility(View.VISIBLE);
            binding.textView.setVisibility(View.VISIBLE);
            binding.botonLogin.setOnClickListener(view -> loginOrRegister(view));
        }
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}

