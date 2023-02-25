package com.fvt.dondeestudio.adapters;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;

import com.fvt.dondeestudio.R;
import com.fvt.dondeestudio.databinding.ItemClaseBusquedaBinding;
import com.fvt.dondeestudio.gestores.GestorReservas;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Clase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ClasesResultadoAdapter extends RecyclerView.Adapter<ClasesResultadoAdapter.ClaseViewHolder> {
    private ArrayList<Clase> mClases;
    private LayoutInflater mInflater;

    public static class ClaseViewHolder extends RecyclerView.ViewHolder {
        private ItemClaseBusquedaBinding binding;

        public ClaseViewHolder(ItemClaseBusquedaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Clase clase) {
            binding.asignatura2.setText(clase.getAsignatura());
            binding.profesor.setText(clase.getProfesor().getNombre() + " " + clase.getProfesor().getApellido());
            binding.horario.setText(clase.getHorario());
            GestorReservas gR = new GestorReservas();
            gR.usuarioReservoClase(FirebaseAuth.getInstance().getCurrentUser().getUid(), clase.getId(), new Callback<Boolean>() {
                @Override
                public void onComplete(Boolean data) {
                    if(data) {
                        binding.botonReservar.setEnabled(false);
                        binding.botonReservar.setVisibility(VISIBLE);
                        binding.botonReservar.setText("YA RESERVADA");
                    }  else {
                        binding.botonReservar.setEnabled(true);
                        binding.botonReservar.setVisibility(VISIBLE);
                        binding.botonReservar.setText("RESERVAR");
                    }
                }
            });
           //TODO: convertir de coordenadas a direccion para mostrar en el card view
            binding.botonReservar.setOnClickListener(new View.OnClickListener() {
                  @Override
                 public void onClick(View v) {
                     gR.guardarReserva(FirebaseAuth.getInstance().getCurrentUser().getUid(), clase);
                     Toast.makeText(v.getContext(), "Reserva realizada correctamente.", Toast.LENGTH_LONG).show();
                     binding.botonReservar.setEnabled(false);
                     binding.botonReservar.setText("Reservada");
                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_clasesReservadasFragment);
                          }
                      }, 1000);
                 }
             });
        }
    }

    public ClasesResultadoAdapter(Context context, ArrayList<Clase> personNames) {
        mInflater = LayoutInflater.from(context);
        mClases = personNames;
    }


    @NonNull
    @Override
    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClaseBusquedaBinding binding = ItemClaseBusquedaBinding.inflate(mInflater, parent, false);
        return new ClaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClasesResultadoAdapter.ClaseViewHolder holder, int position) {
        Clase clase = mClases.get(position);
        holder.bind(clase);
    }


    @Override
    public int getItemCount() {
        return mClases.size();
    }
}
