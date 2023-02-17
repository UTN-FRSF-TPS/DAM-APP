package com.fvt.dondeestudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.Button;

import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.model.Clase;

import java.util.zip.ZipEntry;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        Button botonTest = findViewById(R.id.button);
        botonTest.setOnClickListener(e->{
            GestorClases gestor = new GestorClases();
            Clase claseTest = new Clase("", 700.0, "Matematica");
            Clase claseTest2 = new Clase("", 900.0, "Algebra");
            //el id del constructor es el de firebase
            //gestor.agregarClase(claseTest);
            //gestor.eliminarClase("irIkCtluG6G5UJYT29eY");
            //gestor.getClase("CPwFcORPxLg3jNFRr2FN");
            gestor.actualizarClase("CPwFcORPxLg3jNFRr2FN", claseTest2);

        });
         */

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}