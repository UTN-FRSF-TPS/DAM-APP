package com.fvt.dondeestudio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fvt.dondeestudio.databinding.ItemClaseBusquedaBinding;
import com.fvt.dondeestudio.gestores.GestorReservas;
import com.fvt.dondeestudio.model.Clase;
import com.google.firebase.auth.FirebaseAuth;

import java.time.format.DateTimeFormatter;
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
            binding.botonReservar.setOnClickListener(l->{
                GestorReservas gR = new GestorReservas();
                gR.guardarReserva(FirebaseAuth.getInstance().getCurrentUser().getUid(), clase);
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
