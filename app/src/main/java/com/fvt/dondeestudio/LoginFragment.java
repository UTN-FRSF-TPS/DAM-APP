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
import android.widget.TextView;
import android.widget.Toast;

import com.fvt.dondeestudio.databinding.FragmentLoginBinding;
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

    public LoginFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.botonLogin.setOnClickListener(view -> loginOrRegister(view));
        return binding.getRoot();
    }
}