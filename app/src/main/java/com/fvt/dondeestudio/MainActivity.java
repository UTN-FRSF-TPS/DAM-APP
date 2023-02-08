package com.fvt.dondeestudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.widget.Button;

import com.fvt.dondeestudio.gestores.GestorClases;
import com.fvt.dondeestudio.model.Clase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.ZipEntry;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
    }*/
    }
}