package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.ScriptGroup;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.DTO.ReservaDTO;
import com.fvt.dondeestudio.adapters.ReservaAdapter;
import com.fvt.dondeestudio.databinding.FragmentReservasPendientesBinding;
import com.fvt.dondeestudio.gestores.GestorReservas;
import com.fvt.dondeestudio.helpers.Callback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservasPendientesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservasPendientesFragment extends Fragment {

    private FragmentReservasPendientesBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReservasPendientesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservasPendientesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservasPendientesFragment newInstance(String param1, String param2) {
        ReservasPendientesFragment fragment = new ReservasPendientesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        binding = FragmentReservasPendientesBinding.inflate(inflater, container, false);
        RecyclerView recycler = binding.recycler;
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reservas");
        GestorReservas gR = new GestorReservas();
        System.out.println("MI ID: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        gR.getReservasNuevas(FirebaseAuth.getInstance().getCurrentUser().getUid(), new Callback<ArrayList<ReservaDTO>>() {
                    @Override
                    public void onComplete(ArrayList<ReservaDTO> reservas) {
                        if(reservas.size() > 0){
                            binding.mensaje.setVisibility(View.GONE);
                            recycler.setAdapter(new ReservaAdapter(getContext(), reservas));
                        } else {
                            binding.mensaje.setText("No se encontraron reservas pendientes.");
                            binding.mensaje.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                            binding.mensaje.setGravity(Gravity.CENTER);
                        }
                    }
                });
        return binding.getRoot();
    }
}