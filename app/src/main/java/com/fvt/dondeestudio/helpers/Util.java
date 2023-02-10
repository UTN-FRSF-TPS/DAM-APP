package com.fvt.dondeestudio.helpers;

import static android.content.Context.LOCATION_SERVICE;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

public class Util {

    public static double calcularDistancia(LatLng latLng1, LatLng latLng2) {
        float[] dist = new float[1];
        android.location.Location.distanceBetween(
                latLng1.latitude, latLng1.longitude,
                latLng2.latitude, latLng2.longitude,
                dist);
        return dist[0];
    }
}


