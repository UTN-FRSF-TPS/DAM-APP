package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.fvt.dondeestudio.DTO.ClaseDTO;
import com.fvt.dondeestudio.databinding.FragmentBuscarClasesBinding;
import com.fvt.dondeestudio.helpers.UbicacionUtil;
import com.google.android.gms.maps.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuscarClasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscarClasesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentBuscarClasesBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuscarClasesFragment() {
        // Required empty public constructor
    }
    public static BuscarClasesFragment newInstance(String param1, String param2) {
        BuscarClasesFragment fragment = new BuscarClasesFragment();
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


       binding = FragmentBuscarClasesBinding.inflate(inflater, container, false);
       Button boton = binding.btnSearch;
       this.actualizarSeekBar(binding);
       boton.setOnClickListener(e ->{
           Bundle bundle = new Bundle();
           ClaseDTO claseDTO = new ClaseDTO();
           if(!binding.asignatura.getText().toString().equals(""))
            claseDTO.setAsignatura(binding.asignatura.getText().toString());
           claseDTO.setNivel(binding.nivel.getSelectedItem().toString());
           claseDTO.setTarifaHoraMax(Double.valueOf(binding.tarifa.getProgress()));
           claseDTO.setRadioMaxMetros(Double.valueOf(binding.distancia.getProgress()));
           claseDTO.setValoracionProfesor(binding.valoracion.getProgress());
           claseDTO.setTipo(binding.tipo.getSelectedItem().toString());
           if(claseDTO.getTipo().equals("Presencial")){
               UbicacionUtil.obtenerUbicacionActual(this.getActivity(), new UbicacionUtil.LocationResultCallback() {
                   @Override
                   public void onLocationResult(LatLng location) {
                       claseDTO.setUbicacion(location);
                   }
               });
           }
           bundle.putSerializable("filtro", claseDTO);
           Navigation.findNavController(binding.getRoot()).navigate(R.id.action_buscarClasesFragment_to_resultadosBusqueda, bundle);
        });
        return binding.getRoot();

    }

    private void actualizarSeekBar(FragmentBuscarClasesBinding binding) {
        binding.tarifa.setProgress(1000);
        binding.distancia.setProgress(5);
        binding.valoracion.setProgress(3);

        binding.tarifa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Actualizar el texto del TextView con el valor actual de la SeekBar
                binding.tarifaTextView.setText(String.valueOf(progress) + " $");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.distancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Actualizar el texto del TextView con el valor actual de la SeekBar
                binding.distanciaTextView.setText(String.valueOf(progress) + " km.");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.valoracion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Actualizar el texto del TextView con el valor actual de la SeekBar
                binding.valoracionTextView.setText(String.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

}