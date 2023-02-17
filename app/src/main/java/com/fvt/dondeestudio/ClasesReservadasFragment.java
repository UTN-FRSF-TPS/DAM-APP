package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fvt.dondeestudio.model.Clase;
import com.fvt.dondeestudio.model.Profesor;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ClasesReservadasFragment extends Fragment {

    public ClasesReservadasFragment() {
    }
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Arreglar los errores aca, capaz tiene que ver algo con la vista
        /*
        //setContentView(R.layout.fragment_clases_reservadas);
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerClasesReservadas);
        recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //mAdapter = new ClasesReservadasRecyclerAdapter(lista de clases, this);
        recyclerView.setAdapter(mAdapter);
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clases_reservadas, container, false);
    }



    public static class ClasesReservadasRecyclerAdapter extends RecyclerView.Adapter<ClasesReservadasRecyclerAdapter.ClasesReservadasViewHolder> {
    private List<Clase> mDataSet;

    public static class ClasesReservadasViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        TextView materia;
        TextView direccion;
        TextView estado;
        TextView fechaHora;
        ImageView imgProf;
        TextView nombreProfesor;

        public ClasesReservadasViewHolder(@NonNull View v){
            super(v);
            card = v.findViewById(R.id.cardClaseReservada);
            materia = v.findViewById(R.id.tvMateria);
            direccion = v.findViewById(R.id.tvDireccion);
            estado = v.findViewById(R.id.tvEstado);
            fechaHora = v.findViewById(R.id.tvFechaHora);
            imgProf = v.findViewById(R.id.imgProfesor);
            nombreProfesor = v.findViewById(R.id.tvNombreProfesor);
        }

    }

    public ClasesReservadasRecyclerAdapter(List<Clase> myDataSet) { mDataSet = myDataSet; }

    @Override
    public ClasesReservadasViewHolder
    onCreateViewHolder(ViewGroup prn, int tipo){
        View v = LayoutInflater.from(prn.getContext())
                .inflate(R.layout.item_clase_reservada, prn, false);

        ClasesReservadasViewHolder vh= new ClasesReservadasViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ClasesReservadasViewHolder claseHolder, final int position){
        //Obtiene y setea los datos
        Clase clase = mDataSet.get(position);
        Profesor profesor = clase.getProfesor();
        claseHolder.materia.setText(clase.getAsignatura());
        claseHolder.fechaHora.setText(clase.getHorario().toString());
        //TODO: nombreProfesor
        /**claseHolder.nombreProfesor.setText(profesor.getNombre()+" "+profesor.getApellido());*/

        //TODO: ubicacion, ver como hacer lo de la latitud y longitud
        //TODO: ESTADO ES COMPLICADO PQ DICE SI EL PROFESOR ACEPTO AL ALUMNO, NO SE DONDE SE GUARDARIA
        //TODO: imagen del profesor, buscar del profesor(si tiene)
    }

    @Override
    public int getItemCount(){ return mDataSet.size(); }

}
}