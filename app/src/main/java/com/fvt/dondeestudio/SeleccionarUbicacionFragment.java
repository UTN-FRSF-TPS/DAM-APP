package com.fvt.dondeestudio;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.fvt.dondeestudio.databinding.FragmentSeleccionarUbicacionBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Clase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SeleccionarUbicacionFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mMap;
    private EditText direccion;
    private Button botonBusqueda;
    private Button botonConfirmar;

    private LatLng ubicacionClase;
    private String direccionClase;
    private FragmentSeleccionarUbicacionBinding binding;

    public static SeleccionarUbicacionFragment newInstance() {
        return new SeleccionarUbicacionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSeleccionarUbicacionBinding.inflate(inflater, container, false);

        mMapView = binding.mapView;
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        direccion = binding.direccion;
        botonBusqueda = binding.botonBuscar;
        botonConfirmar = binding.botonConfirmar;

        botonBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dir = direccion.getText().toString();
                new GeocodeAsyncTask().execute(dir);
            }
        });

        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Clase clase = (Clase) getArguments().getSerializable("clase");
              clase.setUbicacion(new GeoPoint(ubicacionClase.latitude, ubicacionClase.longitude));
              clase.setDireccion(direccionClase);
                GestorClases gC = new GestorClases();
                gC.agregarClase(clase, new Callback<Boolean>() {
                    @Override
                    public void onComplete(Boolean retorno) {
                        if(retorno) {
                            Toast.makeText(getContext(), "La clase se ha agregado correctamente!", Toast.LENGTH_LONG);
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_clasesProgramadasFragment, null);
                        } else {
                            Toast.makeText(getContext(), "La clase no se pudo agregar. Intentalo mas tarde", Toast.LENGTH_LONG);
                        }
                        }
                });
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ubicacionClase = latLng;
                direccionClase = null;
                botonConfirmar.setEnabled(true);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private class GeocodeAsyncTask extends AsyncTask<String, Void, LatLng> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
            protected LatLng doInBackground(String... strings) {
                String address = strings[0];
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    if (addresses != null && addresses.size() > 0) {
                        Address firstAddress = addresses.get(0);
                        double latitude = firstAddress.getLatitude();
                        double longitude = firstAddress.getLongitude();
                        return new LatLng(latitude, longitude);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(LatLng latLng) {
                super.onPostExecute(latLng);
                if (latLng != null) {
                    ubicacionClase = latLng;
                    direccionClase = direccion.getText().toString();
                    botonConfirmar.setEnabled(true);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                } else {
                    Toast.makeText(getActivity(), "No se encontraron resultados. Intenta agregando la ciudad y el país.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }