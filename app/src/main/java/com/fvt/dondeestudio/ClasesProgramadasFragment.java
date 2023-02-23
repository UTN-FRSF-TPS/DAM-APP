package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.adapters.ClasesProgramadasAdapter;
import com.fvt.dondeestudio.databinding.FragmentClasesProgramadasBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Clase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClasesProgramadasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClasesProgramadasFragment extends Fragment {
    //TODO: Mostrar clases, con los usuarios inscriptos
    //TODO: Otra interfaz que diga "Reservas" donde pueda aceptar o rechazar.
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentClasesProgramadasBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClasesProgramadasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClasesProgramadasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClasesProgramadasFragment newInstance(String param1, String param2) {
        ClasesProgramadasFragment fragment = new ClasesProgramadasFragment();
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

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Clases programadas");

        binding = FragmentClasesProgramadasBinding.inflate(inflater, container, false);
        RecyclerView recycler = binding.recyclerView;
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        GestorClases gC = new GestorClases();
        gC.claseReservadasProfesor(FirebaseAuth.getInstance().getCurrentUser().getUid(), new Callback<ArrayList<Clase>>() {
            @Override
            public void onComplete(ArrayList<Clase> clases) {
                if(clases.size() > 0){
                    recycler.setAdapter(new ClasesProgramadasAdapter(getContext(), clases));
                } else {
                    binding.mensaje.setText("No tenes ninguna clase programada :(");
                }
            }
        });

        return binding.getRoot();
    }
}