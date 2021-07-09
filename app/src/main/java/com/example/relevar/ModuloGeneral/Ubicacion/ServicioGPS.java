package com.example.relevar.ModuloGeneral.Ubicacion;

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
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.relevar.Encuestador;
import com.example.relevar.MenuMapa;
import com.example.relevar.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static com.example.relevar.Recursos.App.CHANNEL_ID;

public class ServicioGPS extends Service {
    int contador = 0;
    LatLng latLng;
    ArrayList<LatLng> recorrido = new ArrayList<>();
    String Latitud, Longitud;
    LocationListener locationListenerEncuestador;
    LocationManager locationManagerEncuestador;
    Encuestador encuestador;
    public ServicioGPS() {
    }

    @Override
    public void onCreate() {
        encuestador= new Encuestador(getBaseContext());
        encuestador.setID("");
        System.out.println("El servicio a creado");

        //System.out.println("El servicio a Comenzado "+Integer.toString(contador));
        // Tomo la posicion cada 1 minuto o cada vez que me muevo 25 metros y muevo la camara
        // Acquire a reference to the system Location Manager
        locationManagerEncuestador = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListenerEncuestador = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //Latitud=Double.toString(location.getLatitude());
                //Longitud=Double.toString(location.getLongitude());
                Latitud=Double.toString(location.getLatitude());
                Longitud=Double.toString(location.getLongitude());
                //System.out.println("UBICACION AAAAAAAAAAAAAAAAAAA"+Latitud+Longitud);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                recorrido.add(latLng);

                Log.d("sender", "Broadcasting message");
                Intent intent = new Intent("custom-event-name");
                // You can also include some extra data.
                intent.putParcelableArrayListExtra("RECORRIDO", recorrido);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                encuestador.GuardarRecorrido(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManagerEncuestador.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 5, locationListenerEncuestador);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Intent notificationIntent = new Intent(this, MenuMapa.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Recorrido")
                .setContentText("Estamos tomando datos GPS")
                .setSmallIcon(R.drawable.icono_notificacion)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //timerTask.cancel();
        //locationManagerEncuestador.removeUpdates(locationListenerEncuestador);
        System.out.println("El servicio a terminado"+Integer.toString(contador));
    }
}
