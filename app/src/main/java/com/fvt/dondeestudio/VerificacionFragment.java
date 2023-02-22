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
        String smsCode = binding.codigoVerificacion.getText().toString();

        System.out.println(mVerificationId + " " + smsCode);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, smsCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        System.out.println("Se logueó");
                        FirebaseUser user = task.getResult().getUser();

                        System.out.println(user.getEmail());

                        if (user.getEmail() == null) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("user", user);
                            Navigation.findNavController(this.getView()).navigate(R.id.action_verificacionFragment_to_registroFragment, bundle);
                        }
                        else {
                            GestorProfesores g = new GestorProfesores();
                            String idLog = FirebaseAuth.getInstance().getUid();
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
                        // Pasa a la siguiente actividad
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        String numeroCompleto = getArguments().getString("numeroCompleto");

        System.out.println("el numero recibido es: " + numeroCompleto);

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            PhoneAuthProvider.ForceResendingToken mResendToken;

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);


                CharSequence text = "funco";

                Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);


                toast.show();
                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
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
        // Inflate the layout for this fragment
        binding = FragmentVerificacionBinding.inflate(inflater, container, false);
        binding.verificar.setOnClickListener(lambda -> verifyNumber());
        return binding.getRoot();
    }
}