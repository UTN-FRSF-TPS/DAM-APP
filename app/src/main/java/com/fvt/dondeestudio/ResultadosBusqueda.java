package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.DTO.ClaseDTO;
import com.fvt.dondeestudio.adapters.ClasesResultadoAdapter;
import com.fvt.dondeestudio.adapters.ClasesUsuarioAdapter;
import com.fvt.dondeestudio.databinding.FragmentResultadosBusquedaBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Clase;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultadosBusqueda#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultadosBusqueda extends Fragment {

    FragmentResultadosBusquedaBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResultadosBusqueda() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultadosBusqueda.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultadosBusqueda newInstance(String param1, String param2) {
        ResultadosBusqueda fragment = new ResultadosBusqueda();
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
        binding = FragmentResultadosBusquedaBinding.inflate(inflater, container, false);
       Bundle bundle = getArguments();
       ClaseDTO filtro = (ClaseDTO) bundle.getSerializable("filtro");
        GestorClases gC = new GestorClases();
        RecyclerView recycler = binding.recyclerView;
        gC.filtrarClases(filtro, new Callback<ArrayList<Clase>>() {
            @Override
            public void onComplete(ArrayList<Clase> clases) {
                System.out.println("TAM: " + clases.size());
                if(clases.size() > 0) {
                    recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    recycler.setAdapter(new ClasesResultadoAdapter(getContext(), clases));
                }else
                    binding.resultado.setText("No se encontraron clases con esos filtros :( ");
                    binding.resultado.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    binding.resultado.setGravity(Gravity.CENTER);
            }
        }, null);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}