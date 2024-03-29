package com.fvt.dondeestudio;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fvt.dondeestudio.adapters.AlumnosAdapter;
import com.fvt.dondeestudio.databinding.DialogAlumnosBinding;
import com.fvt.dondeestudio.databinding.FragmentDetalleClaseProfesorBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Clase;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleClaseProfesorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleClaseProfesorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentDetalleClaseProfesorBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetalleClaseProfesorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleClaseProfesorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleClaseProfesorFragment newInstance(String param1, String param2) {
        DetalleClaseProfesorFragment fragment = new DetalleClaseProfesorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = FragmentDetalleClaseProfesorBinding.inflate(inflater, container, false);
       Clase clase = (Clase) getArguments().getSerializable("clase");
       LatLng coordenada = getArguments().getParcelable("coordenada");

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(clase.getAsignatura());

       binding.horario.setText(clase.getHorario());
       binding.cupoActual.setText("Cupos disponible: " + clase.getCupo().toString());
       binding.tarifa.setText(clase.getTarifaHora().toString());

        GestorClases gC = new GestorClases();

    binding.botonVerInscriptos.setOnClickListener(e->{
        if(Util.conectado(getContext())) {
            gC.getAlumnosClase(clase.getId(), new Callback<ArrayList<Alumno>>() {
                @Override
                public void onComplete(ArrayList<Alumno> data) {
                    mostrarDialogoAlumnos(getContext(), data);
                }
            });
        } else {
            Toast noConexion = Toast.makeText(getContext(), "En este momento no tenés internet. Por favor, cuando tengas conexión continua.", Toast.LENGTH_LONG);
            noConexion.getView().setBackgroundColor(Color.RED);
            noConexion.show();
        }
    });

    binding.editButton.setOnClickListener(e->{
        this.DialogEditarTarifa(clase);
    });

    binding.botonEliminarClase.setOnClickListener(e->{
        if(Util.conectado(getContext())) {
            gC.eliminarClase(clase.getId(), new Callback<Integer>() {
                @Override
                public void onComplete(Integer resultado) {
                    switch (resultado) {
                        case 1: {
                            Toast.makeText(getContext(), "Clase borrada correctamente.", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_clasesProgramadasFragment);
                            break;
                        }
                        case 2: {
                            Toast.makeText(getContext(), "Ocurrio un error al intentar borrar la clase.", Toast.LENGTH_LONG).show();
                            break;
                        }
                        case 3: {
                            Toast.makeText(getContext(), "No se pudo encontrar la clase a borrar.", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_clasesProgramadasFragment);
                            break;
                        }
                    }
                }
            });
        } else {
            Toast noConexion = Toast.makeText(getContext(), "En este momento no tenés internet. Por favor, cuando tengas conexión continua.", Toast.LENGTH_LONG);
            noConexion.getView().setBackgroundColor(Color.RED);
            noConexion.show();
        }
    });


        if(clase.getTipo().equals("Presencial")) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng ubicacion = coordenada;
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(ubicacion);
                    googleMap.addMarker(markerOptions);
                    binding.direccion.setText(clase.getDireccion());
                    CameraUpdate ubicacionCamara = CameraUpdateFactory.newLatLngZoom(ubicacion, 17);
                    googleMap.moveCamera(ubicacionCamara);
                }
            });
        } else {
            getChildFragmentManager().findFragmentById(R.id.map_fragment).getView().setVisibility(View.GONE);
            binding.direccion.setText("Clase virtual");
            binding.locationIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_computer_24));
        }







       return binding.getRoot();
    }

    public void mostrarDialogoAlumnos(Context context, ArrayList<Alumno> alumnosList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogAlumnosBinding binding = DialogAlumnosBinding.inflate(getLayoutInflater());
        builder.setTitle("Alumnos inscriptos");
        View view = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewDialog;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(alumnosList.size() > 0) {
            binding.mensaje.setVisibility(View.GONE);
            AlumnosAdapter adapter = new AlumnosAdapter(alumnosList);
            recyclerView.setAdapter(adapter);
        } else {
            binding.mensaje.setText("No tenes alumnos inscriptos :(");
        }
        builder.setView(view);
        builder.setPositiveButton("Volver", null);
        builder.show();
    }

        public void DialogEditarTarifa(Clase clase){
           EditText mTarifaEditText = new EditText(getContext());

            // Crear el cuadro de diálogo de la tarifa
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Actualizar tarifa");
            builder.setMessage("Introduce la nueva tarifa:");

            // Agregar el campo de entrada de texto al cuadro de diálogo
            builder.setView(mTarifaEditText);

            // Agregar el botón Aceptar al cuadro de diálogo
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Obtener el valor introducido en el campo de texto
                    String nuevaTarifa = mTarifaEditText.getText().toString();
                    if (Util.conectado(getContext())) {
                        GestorClases gC = new GestorClases();
                        gC.actualizarClase(clase.getId(), Double.valueOf(nuevaTarifa), new Callback<Boolean>() {
                            @Override
                            public void onComplete(Boolean resultado) {
                                if (resultado) {
                                    binding.tarifa.setText(nuevaTarifa);
                                    Toast.makeText(getContext(), "Tarifa cambiada correctamente", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Ha ocurrido un error al cambiar la tarifa.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast noConexion = Toast.makeText(getContext(), "En este momento no tenés internet. Por favor, cuando tengas conexión continua.", Toast.LENGTH_LONG);
                        noConexion.getView().setBackgroundColor(Color.RED);
                        noConexion.show();
                    }
                }
            });

            // Agregar el botón Cancelar al cuadro de diálogo
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Cerrar el cuadro de diálogo
                    dialog.cancel();
                }
            });

            // Mostrar el cuadro de diálogo
            AlertDialog dialog = builder.create();
            dialog.show();
        }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (this instanceof DetalleClaseProfesorFragment) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}