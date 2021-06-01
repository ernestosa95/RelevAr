package com.example.relevar.ModuloGeneral.Ubicacion;

import android.content.ContentResolver;
import android.provider.Settings;

import java.security.Provider;

public class Ubicacion {

    public boolean checkUbicacionGPS(ContentResolver contentResolver){
        String provider = Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        //System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps")){//|| provider.contains("network")){
            return true;
        }
        return false;
    }


}
