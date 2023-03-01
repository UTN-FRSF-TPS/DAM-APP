package com.fvt.dondeestudio;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.fvt.dondeestudio.helpers.Util;

public class GuardarRolTest {

    @Mock
    private SharedPreferences sharedPreferences;

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);
    }

    @Test
    public void guardarRol_alumno() {
        Integer rol = 1; //probar que si se ingresa 1 se guarda alumno
        String userId = "123";
        Util.guardarRol(rol, context, userId);
        verify(editor).putString("rol", "Alumno");
        verify(editor).putString("userId", userId);
        verify(editor).apply();
    }

    @Test
    public void guardarRol_profesor() {
        Integer rol = 2; //probar que si se ingresa 2 se guarda profesor.
        String userId = "456";
        Util.guardarRol(rol, context, userId);
        verify(editor).putString("rol", "Profesor");
        verify(editor).putString("userId", userId);
        verify(editor).apply();
    }
}

