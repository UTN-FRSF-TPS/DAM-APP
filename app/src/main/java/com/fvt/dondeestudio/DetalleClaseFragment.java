package com.fvt.dondeestudio;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fvt.dondeestudio.DTO.ClaseDTO;
import com.fvt.dondeestudio.databinding.FragmentDetalleClaseBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.helpers.NotificacionHelper;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleClaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleClaseFragment extends Fragment {
    FragmentDetalleClaseBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetalleClaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleClaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleClaseFragment newInstance(String param1, String param2) {
        DetalleClaseFragment fragment = new DetalleClaseFragment();
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

/*
  TODO: OBTENER DIRECCION SEGUN COORDENADAS SOLO SI DIRECCION PUESTA POR PROFESOR ES NULA
 */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalleClaseBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        Clase clase = (Clase) bundle.getSerializable("clase");
        LatLng coordenada = (LatLng) bundle.getParcelable("coordenada");
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Clase de " + clase.getAsignatura());
        this.actualizarEstado(clase);
        binding.horario.setText(clase.getHorario());


        //Cargar foto de perfil del profesor
        if(Util.conectado(getContext())) {
            GestorProfesores gP = new GestorProfesores();
            gP.obtenerProfesor(clase.getProfesor().getId(), new Callback<Profesor>() {
                @Override
                public void onComplete(Profesor retorno) {
                    if (retorno.getPhotoUrl() == null) {
                        binding.fotoPerfil.setImageResource(R.drawable.ic_baseline_person_24);
                    } else {
                        Glide.with(getActivity()).load(retorno.getPhotoUrl()).into(binding.fotoPerfil);
                    }
                }
            });
        } else {
            binding.fotoPerfil.setImageResource(R.drawable.ic_baseline_person_24);
        }

        //Nombre del profesor
        binding.profesor.setText("Profesor: " + clase.getProfesor().getNombre() + " " + clase.getProfesor().getApellido());

        //Tarifa de la clase
        binding.tarifa.setText(clase.getTarifaHora().toString() + " por hora");
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime fechaClase = LocalDateTime.parse(clase.getHorario(), formatter);
            if(clase.getEstadoUsuario().equals("pendiente") && fechaClase.isAfter(LocalDateTime.now())){
            binding.botonCancelarReserva.setVisibility(View.VISIBLE);
        }
                //Si la clase termino, si la clase fue confirmada

            GestorClases gC = new GestorClases();
            gC.ClaseTieneRetroalimentacionDeUsuario(clase.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid(), new Callback<Boolean>() {
                @Override
                public void onComplete(Boolean agregoR) {
                    if(fechaClase.isBefore(LocalDateTime.now()) && clase.getEstadoUsuario().equals("confirmada") && !agregoR) {
                        binding.botonRetroalimentacion.setVisibility(View.VISIBLE);
                    }
                }
            });


        }

        if(clase.getTipo().equals("Presencial")) {
            binding.direccion.setText(clase.getDireccion());
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng ubicacion = coordenada;
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(ubicacion);
                    googleMap.addMarker(markerOptions);
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    CameraUpdate ubicacionCamara = CameraUpdateFactory.newLatLngZoom(ubicacion, 17);
                    googleMap.moveCamera(ubicacionCamara);
                }
            });
        } else {
            getChildFragmentManager().findFragmentById(R.id.map_fragment).getView().setVisibility(View.GONE);
            binding.direccion.setText("Clase virtual");
            binding.locationIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_computer_24));

        }

        /* LISTENERS DE BOTONES*/
        binding.botonRetroalimentacion.setOnClickListener(e ->{
            this.mostrarDialog(clase.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        });

        binding.botonEnviarMensajes.setOnClickListener(e -> {
            String idDestino = clase.getProfesor().getId();
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            intent.putExtra("userId", idDestino);
            getContext().startActivity(intent);
        });


        return binding.getRoot();
    }

    private void mostrarDialog(String idClase, String idUsuario){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Califica la clase");
        builder.setMessage("Selecciona una calificación:");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rating_dialog, null);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        builder.setView(view);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Util.conectado(getContext())) {
                    GestorClases gC = new GestorClases();
                    System.out.println("Clase id: " + idClase);
                    gC.agregarRetroalimentacion(idClase, idUsuario, Integer.valueOf(Double.valueOf(ratingBar.getRating()).intValue()), new Callback<Integer>() {
                        @Override
                        public void onComplete(Integer resultado) {
                            switch (resultado) {
                                case 1: {
                                    binding.botonRetroalimentacion.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), "Retroalimentacion agregada correctamente", Toast.LENGTH_LONG).show();
                                    break;
                                }
                                case 2: {
                                    Toast.makeText(getContext(), "No se pudo agregar la retroalimentacion. Intente mas tarde.", Toast.LENGTH_LONG).show();
                                    break;
                                }
                                case 3: {
                                    Toast.makeText(getContext(), "Ya ha agregado una retroalimentación a esta clase.", Toast.LENGTH_LONG).show();
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
            }
        });
        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }



    private void actualizarEstado(Clase clase){
        switch(clase.getEstadoUsuario()) {
            case "pendiente":
                binding.estado.setText("Pendiente");
                binding.estado.setTextColor(Color.BLUE);
                binding.statusIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.reserva_pendiente));
                binding.statusIcon.setColorFilter(Color.BLUE);
                break;
            case "confirmada":
                binding.estado.setText("Confirmada");
                binding.estado.setTextColor(Color.GREEN);
                binding.statusIcon.setColorFilter(Color.GREEN);
                binding.statusIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.reserva_confirmada));

                break;
            case "rechazada":
                binding.estado.setText("Rechazada");
                binding.estado.setTextColor(Color.RED);
                binding.statusIcon.setColorFilter(Color.RED);
                binding.statusIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.reserva_rechazada));
                break;
            case "cancelada":
                binding.estado.setText("Cancelada");
                binding.estado.setTextColor(Color.RED);
                binding.statusIcon.setColorFilter(Color.RED);
                binding.statusIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.reserva_rechazada));
                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (this instanceof DetalleClaseFragment) {
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
