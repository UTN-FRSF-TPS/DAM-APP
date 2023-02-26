package com.fvt.dondeestudio.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fvt.dondeestudio.DTO.ReservaDTO;
import com.fvt.dondeestudio.R;
import com.fvt.dondeestudio.databinding.ItemReservaProfesorBinding;
import com.fvt.dondeestudio.gestores.GestorAlumnos;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.gestores.GestorReservas;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Clase;

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    private ArrayList<ReservaDTO> mReservas;
    private LayoutInflater mInflater;

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        private ItemReservaProfesorBinding binding;

        public ReservaViewHolder(ItemReservaProfesorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ReservaDTO reserva) {
            TextView asignatura = binding.asignaturaTextview;
            TextView fecha = binding.fechaTextview;
            Button aceptar = binding.aceptarButton;
            Button cancelar = binding.cancelarButton;
            TextView alumno = binding.nombreApellidoTextview;

            GestorClases gC = new GestorClases();
            GestorAlumnos gA = new GestorAlumnos();
            GestorReservas gR = new GestorReservas();

            gC.getClase(reserva.getIdClase(), new Callback<Clase>() {
                @Override
                public void onComplete(Clase clase) {
                    gA.obtenerAlumno(reserva.getIdAlumno(), new Callback<Alumno>() {
                        @Override
                        public void onComplete(Alumno alumnoBD) {
                            asignatura.setText(clase.getAsignatura());
                            fecha.setText(clase.getHorario());
                            alumno.setText(alumnoBD.getNombre() + " " + alumnoBD.getApellido());
                        }
                    });
                }
            });

            aceptar.setOnClickListener(e-> {
                gR.cambiarEstadoReserva(reserva.getId(), "confirmada", -1, new Callback<Integer>() {
                    @Override
                    public void onComplete(Integer resultado) {
                        switch (resultado) {
                            case 1: {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_reservasPendientesFragment);
                                Toast.makeText(itemView.getContext(), "Reserva aceptada correctamente.", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 2: {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_reservasPendientesFragment);
                                Toast.makeText(itemView.getContext(), "Ocurrio un error. Intentalo mas tarde.", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 3: {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_reservasPendientesFragment);
                                Toast.makeText(itemView.getContext(), "Ya aceptaste o rechazaste esta reserva.", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                        }


                });
            });
            cancelar.setOnClickListener(e -> {
                gR.cambiarEstadoReserva(reserva.getId(), "rechazada", 0, new Callback<Integer>() {
                    @Override
                    public void onComplete(Integer resultado) {
                        switch (resultado) {
                            case 1: {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_reservasPendientesFragment);
                                Toast.makeText(itemView.getContext(), "Reserva rechazada correctamente.", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 2: {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_reservasPendientesFragment);
                                Toast.makeText(itemView.getContext(), "Ocurrio un error. Intentalo mas tarde.", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 3: {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_reservasPendientesFragment);
                                Toast.makeText(itemView.getContext(), "Ya aceptaste o rechazaste esta reserva.", Toast.LENGTH_LONG).show();
                                break;
                            }

                        }
                    }
                });
            });

        }
    }
        public ReservaAdapter(Context context, ArrayList<ReservaDTO> reservas) {
            mInflater = LayoutInflater.from(context);
            mReservas = reservas;
        }


        @NonNull
        @Override
        public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @NonNull ItemReservaProfesorBinding binding = ItemReservaProfesorBinding.inflate(mInflater, parent, false);
            return new ReservaViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ReservaAdapter.ReservaViewHolder holder, int position) {
            ReservaDTO reserva = mReservas.get(position);
            holder.bind(reserva);
        }


        @Override
        public int getItemCount() {
            return mReservas.size();
        }

}
