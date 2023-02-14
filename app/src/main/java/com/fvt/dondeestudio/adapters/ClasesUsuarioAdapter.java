package com.fvt.dondeestudio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fvt.dondeestudio.databinding.ItemClaseBinding;
import com.fvt.dondeestudio.model.Clase;

import java.util.ArrayList;

public class ClasesUsuarioAdapter extends RecyclerView.Adapter<ClasesUsuarioAdapter.PersonViewHolder> {
    private ArrayList<Clase> mPersonNames;
    private LayoutInflater mInflater;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        private ItemClaseBinding mBinding;

        public PersonViewHolder(ItemClaseBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Clase clase) {
            mBinding.asignatura.setText(clase.getAsignatura());
            mBinding.profesor.setText(clase.getProfesor().getNombre() + " " + clase.getProfesor().getApellido());
            mBinding.estado.setText(clase.getEstadoUsuario());
        }
    }

    public ClasesUsuarioAdapter(Context context, ArrayList<Clase> personNames) {
        mInflater = LayoutInflater.from(context);
        mPersonNames = personNames;
    }


    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClaseBinding binding = ItemClaseBinding.inflate(mInflater, parent, false);
        return new PersonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Clase clase = mPersonNames.get(position);
        holder.bind(clase);
    }

    @Override
    public int getItemCount() {
        return mPersonNames.size();
    }
}
