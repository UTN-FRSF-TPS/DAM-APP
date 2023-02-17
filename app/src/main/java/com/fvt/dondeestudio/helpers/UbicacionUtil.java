package com.fvt.dondeestudio.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.model.LatLng;


public class UbicacionUtil {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    public interface LocationResultCallback {
        void onLocationResult(LatLng location);
    }

    public static void obtenerUbicacionActual(Activity activity, LocationResultCallback callback) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // No se han otorgado permisos de ubicación, solicitarlos
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            } else {
                // Se han otorgado los permisos, obtener la ubicación actual
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        callback.onLocationResult(new LatLng(location.getLatitude(), location.getLongitude()));
                        locationManager.removeUpdates(this);
                    }

                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onProviderDisabled(String provider) {}

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                };
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation != null) {
                    callback.onLocationResult(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                }
            }
        }
    }
}