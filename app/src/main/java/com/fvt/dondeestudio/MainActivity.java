package com.fvt.dondeestudio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fvt.dondeestudio.databinding.ActivityMainBinding;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Callback;
import com.fvt.dondeestudio.model.Profesor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


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
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            GestorProfesores g = new GestorProfesores();
            String idLog = FirebaseAuth.getInstance().getUid();
            g.obtenerProfesor(idLog, new Callback<Profesor>() {
                @Override
                public void onComplete(Profesor data) {
                    if (data == null) { //es alumno
                        getMenuInflater().inflate(R.menu.menu_toolbar_alumno, menu);
                    } else {
                        getMenuInflater().inflate(R.menu.menu_toolbar_profesor, menu);
                    }
                }
            });
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

        return super.onOptionsItemSelected(item);
    }





}