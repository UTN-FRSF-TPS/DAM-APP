package com.fvt.dondeestudio.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fvt.dondeestudio.R;
import com.fvt.dondeestudio.databinding.ItemClaseReservaBinding;
import com.fvt.dondeestudio.model.Clase;

import java.util.ArrayList;

public class ClasesUsuarioAdapter extends RecyclerView.Adapter<ClasesUsuarioAdapter.ClaseViewHolder> {
    private ArrayList<Clase> mClases;
    private LayoutInflater mInflater;

    public static class ClaseViewHolder extends RecyclerView.ViewHolder {
        private ItemClaseReservaBinding mBinding;

        public ClaseViewHolder(ItemClaseReservaBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Clase clase) {
            mBinding.nombreClase.setText(clase.getAsignatura());
            mBinding.profesor.setText(clase.getProfesor().getNombre() + " " + clase.getProfesor().getApellido());
            if (clase.getEstadoUsuario().equals("pendiente")) {
                mBinding.estado.setText("Pendiente");
                mBinding.estado.setTextColor(Color.BLUE);
                mBinding.iconoEstado.setImageDrawable(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.reserva_pendiente));
                mBinding.iconoEstado.setColorFilter(Color.BLUE);
            }
            if (clase.getEstadoUsuario().equals("confirmada")) {
                mBinding.estado.setText("Confirmada");
                mBinding.estado.setTextColor(Color.GREEN);
                mBinding.iconoEstado.setColorFilter(Color.GREEN);
                mBinding.iconoEstado.setImageDrawable(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.reserva_confirmada));
            }
            if (clase.getEstadoUsuario().equals("rechazada")) {
                mBinding.estado.setText("Rechazada");
                mBinding.estado.setTextColor(Color.RED);
                mBinding.iconoEstado.setColorFilter(Color.RED);
                mBinding.iconoEstado.setImageDrawable(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.reserva_rechazada));
            }
            mBinding.horario.setText(clase.getHorario());

        }
    }
    public ClasesUsuarioAdapter(Context context, ArrayList<Clase> personNames) {
        mInflater = LayoutInflater.from(context);
        mClases = personNames;
    }


    @NonNull
    @Override
    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClaseReservaBinding binding = ItemClaseReservaBinding.inflate(mInflater, parent, false);
        return new ClaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ClaseViewHolder holder, int position) {
        Clase clase = mClases.get(position);
        holder.bind(clase);
    }

    @Override
    public int getItemCount() {
        return mClases.size();
    }
}
