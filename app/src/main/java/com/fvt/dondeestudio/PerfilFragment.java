package com.fvt.dondeestudio;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fvt.dondeestudio.databinding.FragmentPerfilBinding;
import com.fvt.dondeestudio.gestores.GestorAlumnos;
import com.fvt.dondeestudio.gestores.GestorProfesores;
import com.fvt.dondeestudio.helpers.Util;
import com.fvt.dondeestudio.model.Alumno;
import com.fvt.dondeestudio.model.Profesor;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.fvt.dondeestudio.helpers.Callback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


public class PerfilFragment extends Fragment {
    FragmentPerfilBinding binding;

    private Uri imageUri;
    private StorageTask uploadTask;

    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ActionBar barra =  ((AppCompatActivity) getActivity()).getSupportActionBar();
        barra.show();
        barra.setTitle("Perfil");

        actualizaImagen();


        binding.cambiarImagen.setOnClickListener(view -> {
            if(Util.conectado(getContext())) {
                openImage();
            } else {
                Toast noConexion = Toast.makeText(getContext(), "En este momento no tenés internet. Por favor, cuando tengas conexión continua.", Toast.LENGTH_LONG);
                noConexion.getView().setBackgroundColor(Color.RED);
                noConexion.show();
            }
        });

        binding.cerrarSesion.setOnClickListener(e->{
            FirebaseAuth.getInstance().signOut();
            Util.logout(getContext());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_loginFragment2);
        });

        return binding.getRoot();
    }

    private void actualizaImagen() {
        GestorAlumnos gestorAlumnos = new GestorAlumnos();
        GestorProfesores gestorProfesores = new GestorProfesores();
        String uid = FirebaseAuth.getInstance().getUid();
        Callback<Alumno> alumno = data -> {
            if (data != null) {
                if (data.getPhotoUrl() != null) Glide.with(getActivity()).load(data.getPhotoUrl()).into(binding.imagenPerfil);
                else Glide.with(getActivity()).load(R.drawable.ic_baseline_person_24).into(binding.imagenPerfil);
            }
        };
        gestorAlumnos.obtenerAlumno(uid, alumno);

        Callback<Profesor> profesor = data -> {
            if (data != null) {
                if (data.getPhotoUrl() != null) Glide.with(getActivity()).load(data.getPhotoUrl()).into(binding.imagenPerfil);
                else Glide.with(getActivity()).load(R.drawable.ic_baseline_person_24).into(binding.imagenPerfil);
            }
        };
        gestorProfesores.obtenerProfesor(uid, profesor);
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 16);
        intent.putExtra("outputY", 16);
        intent.putExtra("return-data", false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Subiendo");
        pd.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileReference.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String mUri = downloadUri.toString();

                    String uid = FirebaseAuth.getInstance().getUid();
                    Callback<Alumno> alumno = data -> {
                        if (data != null) {
                            FirebaseFirestore.getInstance().collection("alumno").document(data.getId()).update("photoUrl", mUri);
                            actualizaImagen();
                        }
                    };
                    (new GestorAlumnos()).obtenerAlumno(uid, alumno);

                    Callback<Profesor> profesor = data -> {
                        if (data != null) {
                            FirebaseFirestore.getInstance().collection("profesor").document(data.getId()).update("photoUrl", mUri);
                            actualizaImagen();
                        }
                    };
                    (new GestorProfesores()).obtenerProfesor(uid, profesor);
                    pd.dismiss();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
            }
            else {
                uploadImage();
            }
        }
    }
}