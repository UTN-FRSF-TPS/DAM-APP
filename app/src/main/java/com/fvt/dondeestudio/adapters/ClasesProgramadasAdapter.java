package com.fvt.dondeestudio.adapters;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fvt.dondeestudio.DetalleClaseFragment;
import com.fvt.dondeestudio.R;
import com.fvt.dondeestudio.databinding.ItemClaseProfesorBinding;
import com.fvt.dondeestudio.databinding.ItemClaseReservaBinding;
import com.fvt.dondeestudio.model.Clase;
import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ClasesProgramadasAdapter extends RecyclerView.Adapter<ClasesProgramadasAdapter.ClaseViewHolder> {
    private ArrayList<Clase> mClases;
    private LayoutInflater mInflater;
    private static Context mContext;

    public static class ClaseViewHolder extends RecyclerView.ViewHolder {
        private ItemClaseProfesorBinding mBinding;

        public ClaseViewHolder(ItemClaseProfesorBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Clase clase) {

         TextView asignatura = mBinding.nombreClase;
         TextView horario = mBinding.horario;
         TextView tarifa = mBinding.tarifa;
         Button botonVerMas = mBinding.botonVerMas;

         asignatura.setText(clase.getAsignatura());
         horario.setText(clase.getHorario());
         tarifa.setText(clase.getTarifaHora().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime fechaClase = LocalDateTime.parse(clase.getHorario(), formatter);
                if (fechaClase.isBefore(LocalDateTime.now())){
                    botonVerMas.setEnabled(false);
                    botonVerMas.setText("CLASE FINALIZADA");
                }
            }


         botonVerMas.setOnClickListener(e -> {
             Bundle bundle = new Bundle();
             bundle.putSerializable("clase", clase);
             if(clase.getUbicacion() != null)
                 bundle.putParcelable("coordenada", new LatLng(clase.getUbicacion().getLatitude(), clase.getUbicacion().getLongitude()));
             Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_global_detalleClaseProfesorFragment, bundle);
         });

        }
    }
    public ClasesProgramadasAdapter(Context context, ArrayList<Clase> clases) {
        mInflater = LayoutInflater.from(context);
        mClases = clases;
        mContext = context;
    }


    @NonNull
    @Override
    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClaseProfesorBinding binding = ItemClaseProfesorBinding.inflate(mInflater, parent, false);
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

