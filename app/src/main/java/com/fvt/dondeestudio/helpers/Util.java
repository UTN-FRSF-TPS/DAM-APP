package com.fvt.dondeestudio.helpers;

import static android.content.Context.LOCATION_SERVICE;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Util {

    public static double calcularDistancia(LatLng latLng1, LatLng latLng2) {
        float[] dist = new float[1];
        android.location.Location.distanceBetween(
                latLng1.latitude, latLng1.longitude,
                latLng2.latitude, latLng2.longitude,
                dist);
        return dist[0] / 1000.0; //devuelve distancia en km
    }

    public static String getActividadActual(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                return topActivity.getClassName();
            }
        } else {
            ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(myProcess);
            if (myProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && myProcess.processName.equals(context.getPackageName())) {
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
                if (!appTasks.isEmpty()) {
                    ActivityManager.RecentTaskInfo taskInfo = appTasks.get(0).getTaskInfo();
                    ComponentName topActivity = taskInfo.topActivity;
                    return topActivity.getClassName();
                }
            }
        }
        return "default";
    }
    /*
    * rol = 1 -> alumno
    * rol = 2 -> profesor
    * */
    public static void guardarRol(Integer rol, Context context, String userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(rol == 1)
            editor.putString("rol", "Alumno");
        else
            editor.putString("rol", "Profesor");
        editor.putString("userId", userId);
        editor.apply();
    }

    public static String getUserId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "nulo");
    }

    public static boolean conectado(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static String getRol(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("usuario", Context.MODE_PRIVATE);
       return sharedPreferences.getString("rol", "nulo");
    }
}


