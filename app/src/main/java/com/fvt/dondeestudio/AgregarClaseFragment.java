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
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Clase;
import com.google.firebase.messaging.FirebaseMessaging;

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

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    String token = task.getResult();
                    System.out.println("TOKEN" + token);

                    //  AQUI PODEMOS MANDAR LLAMAR A UNA FUNCION PARA QUE LO GUARDEMOS EN NUESTRO BACKEND
                });



                agregarClase.setOnClickListener(e->{
            //todos los campos
            //convertir strings a objetos (LocalDate, Date, Double, etc)
            //llamar al gestor y crear clase
            ClaseDTO filtro = new ClaseDTO();
            filtro.setValoracionProfesor(2);
            GestorClases gestor = new GestorClases();
            //gestor.agregarRetroalimentacion("O9PkVVZyPLA3h3ZiNLtC", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //gestor.agregarRetroalimentacion("O9PkVVZyPLA3h3ZiNLtC", "abcde");
          gestor.getClase("O9PkVVZyPLA3h3ZiNLtC", new Callback<Clase>() {
                @Override
                public void onComplete(Clase data) {
                    System.out.println("CLASE A: " + data.getAsignatura());
                }
            });
            /*
            gestor.filtrarClases(filtro,new Callback<ArrayList<Clase>>() {
                @Override
                public void onComplete(ArrayList<Clase> data) {
                    System.out.println("TAMAÑO: " + data.size());
                    Clase c0 = data.get(0);
                    GestorReservas gestorReservas = new GestorReservas();
                    //el id del profesor deberia ser el de la sesion
                   /* gestorReservas.getReservasNuevas("lpWjYgGw8RYkwxnfgI24j7VGhof2", new Callback<ArrayList<ReservaDTO>>() {
                        @Override
                        public void onComplete(ArrayList<ReservaDTO> data) {

                        }});
                        */

                    //gestorReservas.confirmarReserva("MKmArL3vn2jbt6UGW7qd");
                    //NotificationHelper notificationHelper = new NotificationHelper();
                    //notificationHelper.showNotification(contexto, "Hola", "Que onda", null);
                    /*Alumno alumno = new Alumno("", "valefontana15@gmail.com", "Valentin", "Fontana");
                    GestorAlumnos gestorAlumnos = new GestorAlumnos();
                    gestorAlumnos.agregarAlumno(alumno);*/
                   // Intent i = new Intent(getActivity(), NotificacionProfesorService.class);
                    //i.putExtra("idProfesor", "lpWjYgGw8RYkwxnfgI24j7VGhof2");
                    //getActivity().startService(i);
                   // gestor.claseReservadasAlumno("wlZW8w9VdWaonYhkyuxh", new Callback<ArrayList<Clase>>());
                    /*gestor.claseReservadasAlumno("wlZW8w9VdWaonYhkyuxh", new Callback<ArrayList<Clase>>() {

                                @Override
                                public void onComplete(ArrayList<Clase> data) {
                                    System.out.println("CANTIDAD " + data.size());
                                    System.out.println("ASIGNATURA" + data.get(0).getAsignatura());
                                }
                            });
                //mejorar notificacion agregando boton para confirmar y rechazar
                //se deberia hacer el adapter para mostrar las clases que cumplen con una busqueda -> OK
                //guardar la fecha, hora de la clase.
                //implementar metodos para ver el radio maximo -> Hay que hacer un mapa para q elija el lugar

            LatLng latLng1 = new LatLng(37.7749, -122.4194);
            LatLng latLng2 = new LatLng(40.7128, -74.0060);
            System.out.println("Distancia" + Util.calcularDistancia(latLng1, latLng2));

*/
            });
        return binding.getRoot();

    }
}