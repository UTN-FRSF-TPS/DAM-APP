package com.fvt.dondeestudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.fvt.dondeestudio.databinding.ActivityMainBinding;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.listeners.AlumnoReservasListener;
import com.fvt.dondeestudio.listeners.ProfesorReservasListener;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        Context context = navHostFragment.getContext();
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        //Si recibo un intent con con un extra fragment es pq viene del broadcast receiver
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().get("fragment") != null) {
            Integer fr = (Integer) intent.getExtras().get("fragment");
            switch (fr) {
                case 1:
                case 2:
                    navController.navigate(R.id.action_global_reservasPendientesFragment);
                    break;
                case 3:
                    navController.navigate(R.id.action_global_clasesReservadasFragment);
                    break;
            }
        } else {
            //sino es porque abre normalmente la ubicacion
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (Util.getRol(getApplicationContext())) {
                        case "Profesor":
                            navController.navigate(R.id.action_global_agregarClaseFragment);
                            ProfesorReservasListener.seguirReserva(Util.getUserId(getApplicationContext()), context);
                            break;
                        case "Alumno":
                            navController.navigate(R.id.action_global_buscarClasesFragment);
                            AlumnoReservasListener.seguirReserva(Util.getUserId(getApplicationContext()), context);
                            break;
                        case "nulo":
                            navController.navigate(R.id.action_global_loginFragment2);
                            break;
                    }
                }
            }, 2000); //mostrar splash por 2 segundos
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //segun el rol se infla cada menu
           switch(Util.getRol(getApplicationContext())){
               case "Profesor":
                  getMenuInflater().inflate(R.menu.menu_toolbar_profesor, menu);
                   break;

               case "Alumno":
                   getMenuInflater().inflate(R.menu.menu_toolbar_alumno, menu);
                   break;

               case "nulo":
                   break;
           }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_search) {
            navController.navigate(R.id.action_global_buscarClasesFragment);
            return true;
        }

        if (id == R.id.menu_item_reservas) {
            navController.navigate(R.id.action_global_clasesReservadasFragment);
            return true;
        }

        if (id == R.id.menu_item_agregar) {
            navController.navigate(R.id.action_global_agregarClaseFragment);
            return true;
        }

        if (id == R.id.menu_item_programadas) {
            navController.navigate(R.id.action_global_clasesProgramadasFragment);
            return true;
        }

        if (id == R.id.menu_item_pendientes) {
            navController.navigate(R.id.action_global_reservasPendientesFragment);
            return true;
        }

        if (id == R.id.menu_item_mensajes) {
            navController.navigate(R.id.action_global_fragment_mensajeria);
        }

        if (id == R.id.menu_item_perfil) {
            navController.navigate(R.id.action_global_perfilFragment);
        }

        return super.onOptionsItemSelected(item);
    }


}

