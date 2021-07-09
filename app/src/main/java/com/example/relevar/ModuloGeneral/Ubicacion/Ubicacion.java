package com.example.relevar.ModuloGeneral.Ubicacion;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import java.security.Provider;

public class Ubicacion {

    Context context;
    public Ubicacion(Context newContext){
        context = newContext;
    }

    public boolean checkUbicacionGPS(ContentResolver contentResolver){
        String provider = Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        //System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps")){//|| provider.contains("network")){
            return true;
        }
        return false;
    }

    public boolean funcionaServicioGPS(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
