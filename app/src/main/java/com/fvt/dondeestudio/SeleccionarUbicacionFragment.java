package com.fvt.dondeestudio;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.databinding.FragmentDetalleClaseBinding;
import com.fvt.dondeestudio.databinding.FragmentRegistroBinding;
import com.fvt.dondeestudio.databinding.FragmentSeleccionarUbicacionBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeleccionarUbicacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeleccionarUbicacionFragment extends Fragment {
    FragmentSeleccionarUbicacionBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeleccionarUbicacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeleccionarUbicacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeleccionarUbicacionFragment newInstance(String param1, String param2) {
        SeleccionarUbicacionFragment fragment = new SeleccionarUbicacionFragment();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Seleccionar Ubicación");

        // Inflate the layout for this fragment
        binding = FragmentSeleccionarUbicacionBinding.inflate(inflater, container, false);
        binding.btnAgregarUbicacion.setOnClickListener(lamda -> pasarUbicacion());
        return binding.getRoot();
    }

    private void pasarUbicacion() {
        LatLng ubicacion = null;
        //TODO: Obtener la ubicacion

        Bundle bundle = new Bundle();
        //bundle.putSerializable("ubicacion", ubicacion); //no es serializable, capaz se puede pasar la long y lat
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_seleccionarUbicacionFragment_to_agregarClaseFragment, bundle);

    }
}