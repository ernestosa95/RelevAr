package com.example.relevar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //probando las modificaciones de github
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private EditText calle, numero, grupofamiliar;
    private TextView lat, lon, fecha;
    private Button btnagregarpersona, btnubicacion;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private int STORAGE_PERMISSION_CODE =1;
    private String Latitud, Longitud, dni, apellido, nombre, edad, unidadedad;
    private DatePickerDialog.OnDateSetListener Date;

    // Defino la lisa de personas
    private ListView lv1;
    private ArrayList<String> personas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); //evita la rotacion
        setContentView(R.layout.activity_main);

        //Definición de los widget
        calle = (EditText) findViewById(R.id.CALLE);
        numero = (EditText) findViewById(R.id.NUMERO);
        grupofamiliar = (EditText) findViewById(R.id.GRUPOFAMILIAR);

        btnagregarpersona = (Button) findViewById(R.id.AGREGARPERSONA);
        btnubicacion = (Button) findViewById(R.id.BTNUBICACION);

        lat = (TextView) findViewById(R.id.LATITUD);
        lon = (TextView) findViewById(R.id.LONGITUD);

        //Solicito los permisos de ubicación y escritura
        permisosPosicion();
        permisosEscribir();

        // Agrego cabecera con lo nombres de las columnas al archivo
        AgregarCabecera();

        //
        lv1 = (ListView) findViewById(R.id.list1);
        // Muestro las personas cargadas
        ListeVer();

        /*Date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker,int  year, int month, int day) {
                Log.d(TAG, "onDateSet: date:"+year+"/"+month+"/"+day);
                int mes = month + 1;
                String date=day+" - "+mes+" - "+year;
                fecha.setText(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date1 = new Date();
                String fecha1 = dateFormat.format(date1);
                String[] partes = fecha1.split("-");
                int anioactual = Integer.parseInt(partes[0]);
                int mesactual = Integer.parseInt(partes[1]);
                int diaactual = Integer.parseInt(partes[2]);
                java.util.Date Actual = new Date(anioactual, mesactual, diaactual);
                if(fecha.getText().toString()!="DD - MM - YYYY"){
                java.util.Date Nacimiento = new Date(year, month, day);
                long diferencia = Actual.getTime() - Nacimiento.getTime();
                long segsMilli = 1000;
                long minsMilli = segsMilli * 60;
                long horasMilli = minsMilli * 60;
                long diasMilli = horasMilli * 24;
                long mesesMillo = diasMilli * 30;
                long añosMilli = diasMilli * 365;
                long AñosTranscurridos = diferencia / añosMilli;
                if(AñosTranscurridos<2){
                    long MesesTranscurridos = diferencia / mesesMillo;
                    edad=Long.toString(MesesTranscurridos);
                    unidadedad="MESES";
                }
                else {edad=Long.toString(AñosTranscurridos);
                    unidadedad="AÑOS";}
            }else edad="S/D";}
        };*/
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
                lat.setText(Latitud);
                lon.setText(Longitud);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void permisosEscribir(){
        // Check whether this app has write external storage permission or not.
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // If do not grant write external storage permission.
        if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
        {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
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

    /*public void DatosUbicacion(View view){

        if(DNI.getText().toString().length()!=0){
            if(edad!=null){
            dni=DNI.getText().toString();
            DNI.setText("");
            if(Apellido.getText().toString().length()!=0){
                apellido=Apellido.getText().toString();
                Apellido.setText("");}
            else {apellido="S/D";}
            if(Nombre.getText().toString().length()!=0){
                nombre=Nombre.getText().toString();
                Nombre.setText("");}
            else {nombre="S/D";}
            fecha.setText("DD - MM - YYYY");
            String Id =apellido+";"+nombre+";"+dni+";"+edad+";"+unidadedad;
            edad=null;
            Intent Modif= new Intent (this, ubicacion.class);
            Modif.putExtra("identificacion", Id);
            startActivity(Modif);}
            else {Toast.makeText(getApplicationContext(),"Falta Fecha de Nacimiento",Toast.LENGTH_SHORT).show();}}
        else Toast.makeText(getApplicationContext(),"Falta DNI",Toast.LENGTH_SHORT).show();
    }*/

    /*public void Fecha(View view){
        Calendar cal=Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, Date, 1955,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }*/
    private void AgregarCabecera(){
        // Agrego la cabecera en .csv
        File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(ruta, "DATOS.csv");
        String strLine="";
        // leer datos
        String myData = "";
            //File myExternalFile = new File("assets/","log.txt");
        try {
                FileInputStream fis = new FileInputStream(dir);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                //while ((strLine = br.readLine()) != null) {
                //    myData = myData + strLine + "\n";
                //}
                strLine = br.readLine().split(";")[0];
                //Toast.makeText(this, strLine, Toast.LENGTH_SHORT).show();
                br.close();
                in.close();
                fis.close();
        } catch (IOException e) {
            }
        // guardo los datos
        //Toast.makeText(this, strLine+"1", Toast.LENGTH_SHORT).show();
        if(strLine.equals("APELLIDO")!=true){
        String cabecera = "APELLIDO;NOMBRE;DNI;EDAD;UNIDAD EDAD;UBICACION\n";
        try {
            FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
            // que se agreguen los datos al final sin perder los datos anteriores
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(cabecera);
            myOutWriter.close();
            fOut.close();
           // Toast.makeText(this, strLine+"2", Toast.LENGTH_SHORT).show();

        } catch (IOException e){
            e.printStackTrace();
            //Toast.makeText(this, "Datos NO guardados", Toast.LENGTH_SHORT).show();
        }}
    }

    public void NuevaPersona(View view){
        Intent Modif= new Intent (this, persona.class);
        startActivity(Modif);}

    private void ListeVer(){
        //nombres = Leer();
        //nombres1.clear();
        //nombres1 = quitarguion(nombres);
        ArrayList<String> names = new ArrayList<String>();
        names.add("DNI: 30777333 Cosme Fulano");
        names.add("DNI: 77456345 LA LA");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, names);
        lv1.setAdapter(adapter);

        //realizar accion con el listview
        /*lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent Modif1= new Intent (this, MostrarDatos.class);
                Modif1.putExtra("pasar_nombre" , nombres.get(position));
                startActivity(Modif1);
            }
        });*/
    }
    }
