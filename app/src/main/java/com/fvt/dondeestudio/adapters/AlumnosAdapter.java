package com.fvt.dondeestudio.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fvt.dondeestudio.MessageActivity;
import com.fvt.dondeestudio.R;
import com.fvt.dondeestudio.model.Alumno;
import com.google.android.material.navigation.NavigationBarItemView;

import java.util.ArrayList;
import java.util.List;

public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.ViewHolder> {
    private ArrayList<Alumno> alumnosList;

    public AlumnosAdapter(ArrayList<Alumno> alumnosList) {
        this.alumnosList = alumnosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumno, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alumno alumno = alumnosList.get(position);
        holder.txtNombreAlumno.setText(alumno.getNombre() + " " + alumno.getApellido());
        holder.btnEnviarMensaje.setOnClickListener(e -> {
            String idDestino = alumno.getId();
            Intent intent = new Intent(holder.itemView.getContext(), MessageActivity.class);
            intent.putExtra("userId", idDestino);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return alumnosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNombreAlumno;
        public Button btnEnviarMensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreAlumno = itemView.findViewById(R.id.txtNombreAlumno);
            btnEnviarMensaje = itemView.findViewById(R.id.btnEnviarMensaje);

        }
    }
}

