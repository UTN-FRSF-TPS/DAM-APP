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

import com.fvt.dondeestudio.databinding.FragmentLoginBinding;
import com.fvt.dondeestudio.databinding.FragmentVerificacionBinding;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.listeners.AlumnoReservasListener;
import com.fvt.dondeestudio.listeners.ChatListener;
import com.fvt.dondeestudio.listeners.ProfesorReservasListener;
import com.fvt.dondeestudio.model.Profesor;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class VerificacionFragment extends Fragment {
    private String mVerificationId;
    private String smsCode;
    private FragmentVerificacionBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificacionFragment newInstance(String param1, String param2) {
        VerificacionFragment fragment = new VerificacionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void verifyNumber() {
       smsCode = binding.codigoVerificacion.getText().toString();

       if(smsCode.length() > 0) {
           PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, smsCode);
           signInWithPhoneAuthCredential(credential);
       } else {
           Toast.makeText(getContext(), "El código que ingresaste no es correcto. Ingresalo correctamente", Toast.LENGTH_LONG).show();
       }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful() && smsCode != null) {
                      //Se logueo correctamente
                        Log.d(TAG, "signInWithCredential:success");
                        System.out.println("Se logueó");
                        FirebaseUser user = task.getResult().getUser();

                        if (user.getEmail() == null) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("user", user);
                            //si no existe usuario voy al registro.
                            Navigation.findNavController(this.getView()).navigate(R.id.action_verificacionFragment_to_registroFragment, bundle);
                        }
                        else {
                            //si existe, logueo con el rol correspondiente
                            GestorProfesores g = new GestorProfesores();
                            String idLog = FirebaseAuth.getInstance().getUid();
                            ChatListener.seguirChat(idLog, getContext());
                            g.obtenerProfesor(idLog, new Callback<Profesor>() {
                                @Override
                                public void onComplete(Profesor data) {
                                    if (data == null) { //es alumno
                                        AlumnoReservasListener.seguirReserva(idLog, getContext());
                                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_verificacionFragment_to_buscarClasesFragment, null);
                                    } else { //es profesor
                                        ProfesorReservasListener.seguirReserva(idLog, getContext());
                                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_verificacionFragment_to_agregarClaseFragment, null);
                                    }
                                }

                            });
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getContext(), "El código que ingresaste no es correcto. Ingresalo correctamente", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        String numeroCompleto = getArguments().getString("numeroCompleto");
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            PhoneAuthProvider.ForceResendingToken mResendToken;

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    Toast.makeText(getContext(), "El formato de número de teléfono es inválido", Toast.LENGTH_LONG).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                System.out.println(mVerificationId);

            }
        };


        System.out.println("aca llegaste");
        Log.d("test", "passed");
        //Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registroFragment);
        System.out.println(numeroCompleto);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(numeroCompleto)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this.getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public VerificacionFragment() {
        // Required empty public constructor
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
        binding = FragmentVerificacionBinding.inflate(inflater, container, false);
        binding.verificar.setOnClickListener(lambda -> verifyNumber());
        return binding.getRoot();
    }
}