package com.fvt.dondeestudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.fvt.dondeestudio.databinding.ActivityMainBinding;
import com.fvt.dondeestudio.gestores.GestorClases;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment finalHost = NavHostFragment.create(R.navigation.nav_graph);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            //El usuario está registrado.
            //todo: cambiar a donde corresponda

            NavController navController = ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView5)).getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
                navGraph.setStartDestination(R.id.buscarUsuario);
            navController.setGraph(navGraph);
        }

    }

}