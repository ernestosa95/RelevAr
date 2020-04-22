package com.example.relevar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ubicacion extends AppCompatActivity {
    private static final int REQUEST_CODE_POSITION = 1;
    private TextView txtLatitud, txtLongitud;
    private EditText txtTelefono, txtCalle, txtNumero;
    private String telefono, calle, numero;
    private Button btn2, siguiente;
    private String Latitud="S/D", Longitud="S/D";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); //evita la rotacion
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        txtLatitud=(TextView) findViewById(R.id.txtLatitud);
        txtLongitud=(TextView) findViewById(R.id.txtLongitud);
        txtTelefono=(EditText) findViewById(R.id.txtTelefono);
        txtCalle=(EditText) findViewById(R.id.txtCalle);
        txtNumero=(EditText) findViewById(R.id.txtNumero);
        btn2=(Button) findViewById(R.id.btn2);
        siguiente=(Button) findViewById(R.id.siguiente);
        permisosPosicion();

    }
    private void permisosPosicion(){

        // Check whether this app has write external storage permission or not.
        int PositionPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // If do not grant write external storage permission.
        if(PositionPermission!= PackageManager.PERMISSION_GRANTED)
        {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_POSITION);
        }
    }
    public void LatLong(View view){
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Latitud=Double.toString(location.getLatitude());
                Longitud=Double.toString(location.getLongitude());
                txtLatitud.setText(Latitud);
                txtLongitud.setText(Longitud);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    /*public void DatosSalud(View view){
        Bundle datos = this.getIntent().getExtras();
        String recuperada = datos.getString("identificacion");

        if(Longitud!="S/D"){
            if(txtCalle.getText().toString().length()!=0){
                calle=txtCalle.getText().toString();}
            else {calle="S/D";}
            if(txtNumero.getText().toString().length()!=0){
                numero=txtNumero.getText().toString();}
            else {numero="S/D";}
            if(txtTelefono.getText().toString().length()!=0){
                telefono=txtTelefono.getText().toString();}
            else {telefono="S/D";}
            String pasar =recuperada+";"+calle+" "+numero+";"+Latitud+" "+Longitud+";"+telefono;
            Intent Modif= new Intent (this, salud.class);
            Modif.putExtra("datos", pasar);
            startActivity(Modif);
            finish();
        }
        else {Toast.makeText(getApplicationContext(),"Falta Ubicación",Toast.LENGTH_SHORT).show();} }*/
}
