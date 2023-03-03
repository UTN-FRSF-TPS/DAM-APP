package com.fvt.dondeestudio;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.fvt.dondeestudio.databinding.FragmentAgregarClaseBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.helpers.UbicacionUtil;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Nivel;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contexto = this.getContext();
        final String[] fechaHora = {null};
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Agregar Clase");

        // Inflate the layout for this fragment
        binding = FragmentAgregarClaseBinding.inflate(inflater, container, false);

        Button dateTimePickerButton = binding.datePickerButton;
        final Calendar calendar = Calendar.getInstance();

        dateTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                               fechaHora[0] = dateFormat.format(calendar.getTime());
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.btnAgregarClase.setOnClickListener(lambda -> {
           if(Util.conectado(getContext())) {
               agregar(fechaHora[0]);
           } else {
               Toast noConexion = Toast.makeText(getContext(), "En este momento no tenés internet. Por favor, cuando tengas conexión continua.", Toast.LENGTH_LONG);
               noConexion.getView().setBackgroundColor(Color.RED);
               noConexion.show();
           }

        });
        return binding.getRoot();


    }

    private void agregar(String fechaHora) {
        String asignatura = binding.asignatura.getText().toString();
        String tipo = binding.tipo.getSelectedItem().toString();
        if(asignatura.length() > 0 && binding.cupoMax.getText().toString().length() > 0 && binding.tarifaHora.getText().toString().length() > 0 && fechaHora != null) {


                    Integer cupo = Integer.valueOf(binding.cupoMax.getText().toString());
                    Nivel nivel = Nivel.valueOf(binding.spinnerNivel.getSelectedItem().toString());
                    Double tarifaPorHora = Double.parseDouble(binding.tarifaHora.getText().toString());

                    GestorProfesores gestorProfesores = new GestorProfesores();
                    String idLog = FirebaseAuth.getInstance().getUid();
                    gestorProfesores.obtenerProfesor(idLog, new Callback<Profesor>() {
                        @Override
                        public void onComplete(Profesor prof) {

                            Clase clase = new Clase(tipo, prof, tarifaPorHora, cupo.longValue(), nivel, asignatura, 0.00, fechaHora);

                            if (binding.tipo.getSelectedItem().toString().equals("Presencial")) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("clase", clase);
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_agregarClaseFragment_to_seleccionarUbicacionFragment2, bundle);
                            } else {
                                //profesor, tarifahora, cupo, nivel, ubicacion, asignatura, valoracion, horario
                                GestorClases gestor = new GestorClases();
                                gestor.agregarClase(clase, new Callback<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean retorno) {
                                        if (retorno) {
                                            Toast.makeText(getContext(), "La clase se ha agregado correctamente!", Toast.LENGTH_LONG);
                                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_clasesProgramadasFragment, null);
                                        } else {
                                            Toast.makeText(getContext(), "La clase no se pudo agregar. Intentalo mas tarde", Toast.LENGTH_LONG);
                                        }
                                    }
                                });
                            }
                        }
                    });

        } else {
            Toast.makeText(getContext(), "No completaste todos los campos necesarios.", Toast.LENGTH_LONG).show();
        }
    }

}