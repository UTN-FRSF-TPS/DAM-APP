package com.fvt.dondeestudio;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.fvt.dondeestudio.databinding.FragmentAgregarClaseBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Nivel;
import com.google.firebase.auth.FirebaseAuth;

public class AgregarClaseFragment extends Fragment {
    private FragmentAgregarClaseBinding binding;

    private static final String CHANNEL_ID = "my_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    public AgregarClaseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AgregarClaseFragment newInstance(String param1, String param2) {
        AgregarClaseFragment fragment = new AgregarClaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contexto = this.getContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Agregar Clase");

        // Inflate the layout for this fragment
        binding = FragmentAgregarClaseBinding.inflate(inflater, container, false);
        binding.btnAgregarClase.setOnClickListener(lambda -> agregar());
        return binding.getRoot();
    }

    private void agregar() {

        String asignatura = binding.etMateria.getText().toString();
        String fecha = binding.etFecha.getText().toString();
        Integer cupo = binding.slicerCantAlum.getProgress(); //Deberiamos hacer algo para mostrar cuanto va dinamicamente

        // Horario
        String hInicio = binding.etHoraInicio.getText().toString();
        String hFin = binding.etHoraFin.getText().toString();
        String horario = hInicio + "-" + hFin;

        // Nivel
        String nivelS = binding.spinnerNivel.getSelectedItem().toString();
        Nivel nivel = Nivel.valueOf(nivelS); //No se si eso va a funcionar

        // Tarifa
        String tarifa = binding.etTarifa.getText().toString();
        Double tarifaPorHora = Double.parseDouble(tarifa);

        // Profesor
        GestorProfesores gestorProfesores = new GestorProfesores();
        String idLog = FirebaseAuth.getInstance().getUid();
        //No se bien como obtener el objeto profesor con la funcion obtener profesor
        gestorProfesores.obtenerProfesor(idLog,null);

        if (binding.checkBoxPresencial.equals("True")){
            //Obtener la ubicacion


            //profesor, tarifahora, cupo, nivel, ubicacion, asignatura, valoracion, horario
            Clase clase = new Clase();
            GestorClases gestor = new GestorClases();
            gestor.agregarClase(clase);
        }else{
            //profesor, tarifahora, cupo, nivel, ubicacion, asignatura, valoracion, horario
            Clase clase = new Clase(null, tarifaPorHora, cupo, nivel, null, asignatura, null, horario);
            GestorClases gestor = new GestorClases();
            gestor.agregarClase(clase);
        }
        // Corroborar que la clase se haya agregado correctamente
        // Mostrar un toast diciendo que la clase se agrego correctamente y limpiar los campos
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_agregarClaseFragment_self,null);

    }


}