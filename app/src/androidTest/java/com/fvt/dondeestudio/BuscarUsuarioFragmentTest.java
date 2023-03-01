package com.fvt.dondeestudio;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingPolicies;
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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import static org.hamcrest.core.Is.is;

import android.os.SystemClock;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;

import android.text.format.DateUtils;
import android.view.View;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@RunWith(AndroidJUnit4.class)
public class BuscarUsuarioFragmentTest {
    CountingIdlingResource componentIdlingResource;
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init() {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new BuscarUsuarioFragment()).commit();
        componentIdlingResource = activityActivityTestRule.getActivity().getIdlingResourceInTest();
        IdlingRegistry.getInstance().register(componentIdlingResource);
    }


    public class ElapsedTimeIdlingResource implements IdlingResource {
        private final long startTime;
        private final long waitingTime;
        private ResourceCallback resourceCallback;

        public ElapsedTimeIdlingResource(long waitingTime) {
            this.startTime = System.currentTimeMillis();
            this.waitingTime = waitingTime;
        }

        @Override
        public String getName() {
            return ElapsedTimeIdlingResource.class.getName() + ":" + waitingTime;
        }

        @Override
        public boolean isIdleNow() {
            long elapsed = System.currentTimeMillis() - startTime;
            boolean idle = (elapsed >= waitingTime);
            if (idle) {
                resourceCallback.onTransitionToIdle();
            }
            return idle;
        }

        @Override
        public void registerIdleTransitionCallback(
                ResourceCallback resourceCallback) {
            this.resourceCallback = resourceCallback;
        }
    }

    @Test
    public void waitForOneHour() {
        long waitingTime = 10000;

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(
                waitingTime * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(
                waitingTime * 2, TimeUnit.MILLISECONDS);

        onView(withId(R.id.textoEmailBusqueda)).perform(ViewActions.typeText("valefontana15@gmail.com"));
        onView(withId(R.id.botonBuscar)).perform(ViewActions.click());

        // Now we wait
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(waitingTime);
        Espresso.registerIdlingResources(idlingResource);

        onView(withId(R.id.recyclerUsuarios)).check(new RecyclerViewItemCountAssertion(1));
        // Clean up
        Espresso.unregisterIdlingResources(idlingResource);
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
}
