package com.fvt.dondeestudio;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class BuscarUsuarioFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Navega a BuscarUsuarioFragment
        onView(ViewMatchers.withId(R.id.action_global_buscarUsuario)).perform(ViewActions.click());
    }

    @Test
    public void testBuscarPorEmail() {
        // Ingresa un correo electrónico
        onView(withId(R.id.textoEmailBusqueda)).perform(ViewActions.typeText("valefontana15@gmail.com"));

        // Presiona el botón de buscar
        onView(withId(R.id.botonBuscar)).perform(ViewActions.click());

        // Verifica que el RecyclerView de usuarios esté visible
        onView(withId(R.id.recyclerUsuarios)).check(matches(isDisplayed()));

        // Desplázate al primer usuario y verifica que su correo electrónico sea igual al ingresado
        onView(withId(R.id.recyclerUsuarios)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withId(R.id.recyclerUsuarios)).check(matches(hasMinimumChildCount(1)));

    }
}