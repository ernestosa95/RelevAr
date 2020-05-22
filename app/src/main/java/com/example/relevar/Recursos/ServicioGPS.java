package com.example.relevar.Recursos;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.relevar.Inicio;
import com.example.relevar.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.relevar.Recursos.App.CHANNEL_ID;

public class ServicioGPS extends Service {
    int contador = 0;
    TimerTask timerTask;
    LatLng latLng;
    String Latitud, Longitud;
    public ServicioGPS() {
    }

    @Override
    public void onCreate() {

        //System.out.println("El servicio a creado");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //System.out.println("El servicio a Comenzado "+Integer.toString(contador));
        // Tomo la posicion cada 1 minuto o cada vez que me muevo 25 metros y muevo la camara
        // Acquire a reference to the system Location Manager
        final LocationManager locationManagerEncuestador = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListenerEncuestador = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //Latitud=Double.toString(location.getLatitude());
                //Longitud=Double.toString(location.getLongitude());
                Latitud=Double.toString(location.getLatitude());
                Longitud=Double.toString(location.getLongitude());
                System.out.println("UBICACION AAAAAAAAAAAAAAAAAAA"+Latitud+Longitud);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),17));
                //recorrido.add(new LatLng(location.getLatitude(), location.getLongitude()));
                //ruta.setPoints(recorrido);

                //if (encuestador.getID().length()!=0){
                //    encuestador.GuardarRecorrido(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));}
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManagerEncuestador.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListenerEncuestador);

        Intent notificationIntent = new Intent(this, Inicio.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Recorrido")
                .setContentText("Estamos tomando datos GPS")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);




        /*Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("El servicio a Comenzado "+Integer.toString(contador));
                contador+=1;
            }
            };

        timer.scheduleAtFixedRate(timerTask, 0, 1000);*/

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //timerTask.cancel();
        System.out.println("El servicio a terminado"+Integer.toString(contador));
    }
}
