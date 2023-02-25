package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.adapters.ClasesUsuarioAdapter;
import com.fvt.dondeestudio.databinding.FragmentClasesReservadasBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Clase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClasesReservadasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClasesReservadasFragment extends Fragment {
    private FragmentClasesReservadasBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClasesReservadasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClasesReservadasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClasesReservadasFragment newInstance(String param1, String param2) {
        ClasesReservadasFragment fragment = new ClasesReservadasFragment();
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

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClasesReservadasBinding.inflate(inflater, container, false);


        ActionBar barra =  ((AppCompatActivity) getActivity()).getSupportActionBar();
        barra.show();
        barra.setTitle("Clases reservadas");

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GestorClases gestor = new GestorClases();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String idAlumno = firebaseAuth.getCurrentUser().getUid();
        gestor.claseReservadasAlumno(idAlumno, new Callback<ArrayList<Clase>>() {
            @Override
            public void onComplete(ArrayList<Clase> clases) {
                if(clases.size() > 0) {
                    recyclerView.setAdapter(new ClasesUsuarioAdapter(getContext(), clases));
                } else {
                    binding.mensaje.setText("Actualmente no tenés clases reservadas :(");
                    binding.mensaje.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    binding.mensaje.setGravity(Gravity.CENTER);
                }
            }
        });
        return binding.getRoot();
    }


}