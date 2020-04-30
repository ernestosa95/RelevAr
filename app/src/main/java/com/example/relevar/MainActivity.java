package com.example.relevar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static android.os.Environment.getExternalStorageDirectory;
import static android.widget.Toast.*;

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
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<ArrayList<String>> InfoPersonas = new ArrayList<ArrayList<String>>();
    // Defino la lisa de personas
    private ListView lv1;
    private ArrayList<String> personas = new ArrayList<>();
    private String DNIreturn, Nombrereturn, Apellidoreturn, Edadreturn ,Unidadedadreturn ,Efectorreturn ,Factoresreturn ,Codigofactoresreturn ,
    Vacunasreturn ,Contactoreturn ,Observacionesreturn, Nacimiento;
    ArrayAdapter<String> adapter;
    //@RequiresApi(api = Build.VERSION_CODES.O)
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
        //btnubicacion = (Button) findViewById(R.id.BTNUBICACION);

        lat = (TextView) findViewById(R.id.LATITUD);
        lon = (TextView) findViewById(R.id.LONGITUD);

        //Solicito los permisos de ubicación y escritura
        permisosPosicion();


        // Agrego cabecera con lo nombres de las columnas al archivo
        AgregarCabecera();

        //
        lv1 = (ListView) findViewById(R.id.list1);
        // Muestro las personas cargadas
        ListeVer();
        Presentacion();

    }

    //@Override
    protected void onStart() {
        //todo esto pa actualizr la listview
        super.onStart();
        ListeVer();
        AgregarCabecera();
        // Inicio la obtencion de datos de ubicacion del GPS
        LatLong();
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void Presentacion(){
        // Defino los contenedores
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("RelevAr");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check
        LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        // Defino los parametros
        int TamañoLetra =18;

        // Telefono Celular
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final TextView descripcion = new TextView(getApplicationContext());
        //sabin.setText(Texto);
        descripcion.setText("Esta App permite cargar datos de salud personal, partiendo desde la ubicación del grupo familiar.\n\n- Los datos son almacenados en \nMEMORIA DEL TELEFONO->RelevAr." +
                "\n\n- La App no utiliza Datos moviles para establecer la Longitud y Latitud.\n\nAnte cualquier duda comunicarse con los desarrolladores.");
        //descripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        //descripcion.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        descripcion.setTextSize(TamañoLetra);
        descripcion.setTextColor(Color.WHITE);
        descripcion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.setMinimumHeight(500);
        layout0.addView(descripcion);

        mainLayout.addView(layout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("EMPEZAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
            }
        });
        //builder.setNegativeButton("CANCELAR", null);
        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void LatLong(){
    //public void LatLong(View view){
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

    private void AgregarCabecera(){
        permisosEscribir();
        // Agrego la cabecera en .csv
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "RelevAr-"+fecha+".csv";
        //File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(nuevaCarpeta, NombreArchivo);
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
        if(strLine.equals("CALLE")!=true){
        String cabecera = "CALLE;NUMERO;COORDENADAS;GRUPO FAMILIAR;DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;EFECTOR DE SALUD;FACTORES DE RIESGO;CODIGO SISA FACTOR DE RIESGO;VACUNA;TELEFONO CELULAR; TELEFONO FIJO; MAIL;OBSERVACION\n";
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
        startActivityForResult(Modif, 1);}

    private void ListeVer(){
        //names.add("DNI: 30777333 Cosme Fulano");
        //names.add("DNI: 77456345 LA LA");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, names);
        lv1.setAdapter(adapter);

        // Elegir entre eliminar y editar

        //realizar accion con el listview
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Elegir entre Eliminar y Editar
                EliminarEditar(position);
            }
        });
    }

    private void EliminarEditar(final int position){
        // Defino los contenedores
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("PERSONA");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check
        LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        // Defino los parametros
        int TamañoLetra =18;

        // Telefono Celular
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final TextView descripcion = new TextView(getApplicationContext());
        //sabin.setText(Texto);
        descripcion.setText("DNI: "+ InfoPersonas.get(position).get(0)+"\n"+"Nombre: "+ InfoPersonas.get(position).get(1));
        //descripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        //descripcion.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        descripcion.setTextSize(TamañoLetra);
        descripcion.setTextColor(Color.WHITE);
        descripcion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.setMinimumHeight(500);
        layout0.addView(descripcion);

        mainLayout.addView(layout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                Intent Modif= new Intent (getBaseContext(), persona.class);
                Modif.putExtra("DATOS" , InfoPersonas.get(position));
                InfoPersonas.remove(position);
                lv1.setAdapter(null);
                adapter.clear();
                ListeVer();
                startActivityForResult(Modif, 1);
            }
        });
        builder.setNegativeButton("ELIMINAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InfoPersonas.remove(position);
                lv1.setAdapter(null);
                adapter.clear();
                ListeVer();
            }
        });
        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode== RESULT_OK){
                ArrayList<String> CamposPersona = new ArrayList<String>();
                //DNIreturn=getIntent().getStringExtra("DNI");
                DNIreturn = data.getStringExtra("DNI");
                Nombrereturn = data.getStringExtra("NOMBRE");
                Apellidoreturn = data.getStringExtra("APELLIDO");
                Edadreturn = data.getStringExtra("EDAD");
                Unidadedadreturn = data.getStringExtra("UNIDADEDAD");
                Efectorreturn = data.getStringExtra("EFECTOR");
                Factoresreturn = data.getStringExtra("FACTORES");
                Codigofactoresreturn = data.getStringExtra("CODIGOFACTORES");
                Vacunasreturn = data.getStringExtra("VACUNAS");
                Contactoreturn = data.getStringExtra("CONTACTO");
                Observacionesreturn = data.getStringExtra("OBSERVACIONES");
                Nacimiento = data.getStringExtra("NACIMIENTO");
                // Lleno el Array de personas con datos
                CamposPersona.add(DNIreturn);           //0
                CamposPersona.add(Apellidoreturn);      //1
                CamposPersona.add(Nombrereturn);        //2
                CamposPersona.add(Edadreturn);          //3
                CamposPersona.add(Unidadedadreturn);    //4
                CamposPersona.add(Efectorreturn);       //5
                CamposPersona.add(Factoresreturn);      //6
                CamposPersona.add(Codigofactoresreturn);//7
                CamposPersona.add(Vacunasreturn);       //8
                CamposPersona.add(Contactoreturn);      //9,10,11
                CamposPersona.add(Observacionesreturn); //12
                CamposPersona.add(Nacimiento);          //12

                InfoPersonas.add(CamposPersona);

                makeText(this, DNIreturn, LENGTH_SHORT).show();
                names.add("DNI: "+DNIreturn+" "+Apellidoreturn+" "+Nombrereturn);

                ListeVer();
            }
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void Guardar(View view){
        // Inicio la obtencion de datos de ubicacion del GPS
        LatLong();
        if(InfoPersonas.size()!=0){
        if (Latitud != null && Longitud != null && grupofamiliar.getText().toString().length() != 0) {
        // Defino los contenedores
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("RelevAr");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check
        LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        // Defino los parametros
        int TamañoLetra =18;

        // Telefono Celular
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final TextView descripcion = new TextView(getApplicationContext());
        //sabin.setText(Texto);
        descripcion.setText("¿Desea guardar los datos de este grupo familiar?");
        //descripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        //descripcion.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        descripcion.setTextSize(TamañoLetra);
        descripcion.setTextColor(Color.WHITE);
        descripcion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.setMinimumHeight(200);
        layout0.addView(descripcion);

        mainLayout.addView(layout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                // Agrego la cabecera en .csv
                File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
                nuevaCarpeta.mkdirs();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date1 = new Date();
                String fecha = dateFormat.format(date1);
                String NombreArchivo = "RelevAr-" + fecha + ".csv";
                //File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File dir = new File(nuevaCarpeta, NombreArchivo);
                    try {
                        FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
                        // que se agreguen los datos al final sin perder los datos anteriores
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        String guardar = null;
                        for (int x = 0; x < InfoPersonas.size(); x++) {
                            guardar = calle.getText().toString() + ";" + numero.getText().toString() + ";" + Latitud + Longitud + ";" + grupofamiliar.getText().toString();
                            for (int y = 0; y < InfoPersonas.get(x).size()-1; y++) {
                                //guardar = "ACA1";
                                guardar += ";" + InfoPersonas.get(x).get(y);
                            }
                            guardar += "\n";
                            myOutWriter.append(guardar);
                        }

                        InfoPersonas.clear();
                        //Toast.makeText(this, InfoPersonas.get(0).get(0).toString(), Toast.LENGTH_SHORT).show();
                        lv1.setAdapter(null);
                        adapter.clear();
                        calle.setText("");
                        numero.setText("");
                        grupofamiliar.setText("");
                        myOutWriter.close();
                        fOut.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                        //Toast.makeText(this, "Datos NO guardados", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        builder.setNegativeButton("CANCELAR", null);
        builder.setView(mainLayout);
        //builder.setNegativeButton("CANCELAR", null);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        } else {
            makeText(this, "FALTA LLENAR MIEMBROS G. FAMILIAR", LENGTH_SHORT).show();
        }}else{ makeText(this, "NO HAY PERSONAS CARGADAS", LENGTH_SHORT).show();} }

}

