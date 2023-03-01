package com.fvt.dondeestudio;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import static org.hamcrest.core.Is.is;

import android.os.SystemClock;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.view.View;


@RunWith(AndroidJUnit4.class)
public class BuscarUsuarioFragmentTest {
    CountingIdlingResource componentIdlingResource;
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new BuscarUsuarioFragment()).commit();
       componentIdlingResource = activityActivityTestRule.getActivity().getIdlingResourceInTest();
        IdlingRegistry.getInstance().register(componentIdlingResource);
    }

    @Test
    public void testBuscarPorEmail() throws InterruptedException {

        onView(withId(R.id.textoEmailBusqueda)).perform(ViewActions.typeText("valefontana15@gmail.com"));
        onView(withId(R.id.botonBuscar)).perform(ViewActions.click());
        SystemClock.sleep(5000);
        onView(withId(R.id.recyclerUsuarios)).check(new RecyclerViewItemCountAssertion(1));
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(componentIdlingResource);
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

        private class RecylerIdlingResource implements IdlingResource {

        private ResourceCallback resourceCallback;
        private boolean isIdle;

        @Override
        public String getName() {
            return RecylerIdlingResource.class.getName();
        }

        @Override
        public boolean isIdleNow() {
            return isIdle;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback callback) {
            this.resourceCallback = callback;
        }

        public void setIdle(boolean isIdle) {
            this.isIdle = isIdle;
            if (isIdle && resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
        }
    }


}