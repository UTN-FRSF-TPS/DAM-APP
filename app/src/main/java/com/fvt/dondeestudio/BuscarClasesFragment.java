package com.fvt.dondeestudio;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fvt.dondeestudio.model.Clase;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class BuscarClasesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public BuscarClasesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    /*
    public static BuscarClasesFragment newInstance() {
        BuscarClasesFragment fragment = new BuscarClasesFragment();

        return fragment;
    }
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buscar_clases, container, false);
    }



    public static class ClasesBuscadasRecyclerAdapter extends RecyclerView.Adapter<ClasesBuscadasRecyclerAdapter.ClasesBuscadasViewHolder> {
        private List<Clase> mDataSet;

        public static class ClasesBuscadasViewHolder extends RecyclerView.ViewHolder{
            CardView card;
            //Todo: agregar todos los demas
            //materia
            //direccion
            //fechahora
            //nombreprofesor

            public ClasesBuscadasViewHolder(@NonNull View v){
                super (v);
                card = v.findViewById(R.id.cardClaseBuscada);
            }
        }
        public ClasesBuscadasRecyclerAdapter(List<Clase> myDataSet){ mDataSet = myDataSet;}

        public ClasesBuscadasRecyclerAdapter.ClasesBuscadasViewHolder
        onCreateViewHolder(ViewGroup prn, int tipo){
            View v = LayoutInflater.from(prn.getContext())
                    .inflate(R.layout.item_clase_buscada, prn, false);
            ClasesBuscadasViewHolder vh = new ClasesBuscadasViewHolder(v);
            return vh;
        }
        public void onBindViewHolder(ClasesBuscadasViewHolder clasesBuscadasViewHolder, final int position){
            //Todo: Obtener y setear los datos
        }
        public int getItemCount(){
            return mDataSet.size();
        }
    }
}