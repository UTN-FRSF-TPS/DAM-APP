package com.fvt.dondeestudio.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fvt.dondeestudio.DetalleClaseFragment;
import com.fvt.dondeestudio.R;
import com.fvt.dondeestudio.databinding.ItemClaseReservaBinding;
import com.fvt.dondeestudio.model.Clase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ClasesUsuarioAdapter extends RecyclerView.Adapter<ClasesUsuarioAdapter.ClaseViewHolder> {
    private ArrayList<Clase> mClases;
    private LayoutInflater mInflater;
    private static Context mContext;

    public static class ClaseViewHolder extends RecyclerView.ViewHolder {
        private ItemClaseReservaBinding mBinding;

        public ClaseViewHolder(ItemClaseReservaBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Clase clase) {
            mBinding.nombreClase.setText(clase.getAsignatura());
            mBinding.profesor.setText(clase.getProfesor().getNombre() + " " + clase.getProfesor().getApellido());


            DateTimeFormatter formatter = null;
            LocalDateTime dateTime = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                dateTime = LocalDateTime.parse(clase.getHorario(), formatter);
                if (dateTime.isAfter(LocalDateTime.now())) {
                    switch(clase.getEstadoUsuario()){
                        case "pendiente":
                            mBinding.estado.setText("Pendiente");
                            mBinding.estado.setTextColor(Color.BLUE);
                            mBinding.iconoEstado.setImageDrawable(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.reserva_pendiente));
                            mBinding.iconoEstado.setColorFilter(Color.BLUE);
                            break;

                        case  "confirmada":
                            mBinding.estado.setText("Confirmada");
                            mBinding.estado.setTextColor(Color.GREEN);
                            mBinding.iconoEstado.setColorFilter(Color.GREEN);
                            mBinding.iconoEstado.setImageDrawable(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.reserva_confirmada));
                            break;

                        case "rechazada":
                            mBinding.estado.setText("Rechazada");
                            mBinding.estado.setTextColor(Color.RED);
                            mBinding.iconoEstado.setColorFilter(Color.RED);
                            mBinding.iconoEstado.setImageDrawable(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.reserva_rechazada));
                            break;
                    }

                } else {
                    mBinding.estado.setText("Finalizada");
                    mBinding.estado.setTextColor(Color.DKGRAY);
                    mBinding.iconoEstado.setColorFilter(Color.DKGRAY);
                    mBinding.iconoEstado.setImageDrawable(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.finalizada));
                }


            mBinding.horario.setText(clase.getHorario());
            mBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("clase", clase);
                    Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_global_detalleClaseFragment, bundle);
                }
            });

        }
                }
    }
    public ClasesUsuarioAdapter(Context context, ArrayList<Clase> personNames) {
        mInflater = LayoutInflater.from(context);
        mClases = personNames;
        mContext = context;
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
