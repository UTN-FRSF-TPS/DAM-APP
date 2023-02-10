package com.fvt.dondeestudio;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fvt.dondeestudio.databinding.FragmentAgregarClaseBinding;
import com.fvt.dondeestudio.DTO.ClaseDTO;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Nivel;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessaging;

import org.checkerframework.checker.units.qual.C;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgregarClaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarClaseFragment extends Fragment {
    private FragmentAgregarClaseBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CHANNEL_ID = "my_notification_channel";
    private static final int NOTIFICATION_ID = 1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String asignatura;
    private Double tarifaPorHora;
    private String fecha;
    private String hInicio;
    private String hFin;
    private String nivel;
    private Button agregarClase;

    public AgregarClaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarClaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarClaseFragment newInstance(String param1, String param2) {
        AgregarClaseFragment fragment = new AgregarClaseFragment();
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
        final Context contexto = this.getContext();
        // Inflate the layout for this fragment
        binding = FragmentAgregarClaseBinding.inflate(inflater, container, false);
        agregarClase = binding.button5;

        return binding.getRoot();

    }
}