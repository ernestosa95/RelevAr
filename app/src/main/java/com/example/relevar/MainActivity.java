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
import android.icu.text.IDNA;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import java.io.BufferedWriter;
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
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
    // DEFINICION DE VARIABLES GLOBALES
    // Definicion de contantes que hacen al funcionamiento
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private int STORAGE_PERMISSION_CODE =1;

    // Definicion de String para contener informacion
    private String Latitud, Longitud, IDencuestador="";

    // Definicion de EditText para ingresar info
    private EditText calle, numero, grupofamiliar;

    // Definicion de TextView para mostrar info
    private TextView lat, lon;

    // Definicion de los Button para realizar acciones
    private Button btnagregarpersona, btnubicacion;

    // Defino Arrays para almacenar datos
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<ArrayList<String>> InfoPersonas = new ArrayList<ArrayList<String>>();
    private ArrayList<ObjetoPersona> MiembrosFamiliares = new ArrayList<ObjetoPersona>();
    private double[] latitude = new double[3];
    private double[] longitude = new double[3];

    // Defino la lisa de personas
    private ListView lv1;

    // Defino un Adaptador para el ListView
    ArrayAdapter<String> adapter;

    // Objeto Persona
    //ObjetoPersona Persona=new ObjetoPersona();

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); //evita la rotacion
        setContentView(R.layout.activity_main);

        // Definición de los widget
        calle = (EditText) findViewById(R.id.CALLE);
        numero = (EditText) findViewById(R.id.NUMERO);
        grupofamiliar = (EditText) findViewById(R.id.GRUPOFAMILIAR);
        btnagregarpersona = (Button) findViewById(R.id.AGREGARPERSONA);
        lat = (TextView) findViewById(R.id.LATITUD);
        lon = (TextView) findViewById(R.id.LONGITUD);
        lv1 = (ListView) findViewById(R.id.list1);

        //Solicito los permisos de ubicación y escritura
        permisosPosicion();

        // Agrego cabecera con lo nombres de las columnas al archivo
        AgregarCabecera();

        // Solicito los datos del encuestador
        Presentacion();

        // Inicio el guardado del recorrido
        ejecutar();
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

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SOLICIUD DE LOS DATOS DEL ENCUESTADOR

    // Defino la primer pantalla que aparece al iniciar la App
    private void Presentacion(){
        // Defino los contenedores
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("Encuestador");
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

        // EditText del nombre del encuestador
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final EditText nombreencuestador = new EditText(getApplicationContext());
        nombreencuestador.setHint("NOMBRE Y APELLIDO");
        //nombreencuestador.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        nombreencuestador.setHintTextColor(Color.WHITE);
        nombreencuestador.setTextSize(TamañoLetra);
        nombreencuestador.setTextColor(Color.WHITE);
        nombreencuestador.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.addView(nombreencuestador);

        /*// EditText del apellido del encuestador
        LinearLayout layout1       = new LinearLayout(this);
        layout1.setOrientation(LinearLayout.HORIZONTAL);
        layout1.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final EditText apellidoencuestador = new EditText(getApplicationContext());
        apellidoencuestador.setHint("APELLIDO ENCUESTADOR");
        //apellidoencuestador.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        apellidoencuestador.setHintTextColor(Color.WHITE);
        apellidoencuestador.setTextSize(TamañoLetra);
        apellidoencuestador.setTextColor(Color.WHITE);
        apellidoencuestador.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout1.addView(apellidoencuestador);*/

        // Incluyo los views
        mainLayout.addView(layout0);
        //mainLayout.addView(layout1);

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                IDencuestador = nombreencuestador.getText().toString();
                // Obtener la posicion cada 10 segundos
                ejecutar();
            }
        });
        //builder.setNegativeButton("CANCELAR", null);
        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // OBTENCION DE LOS DATOS DE LONGTUD Y LATITUD

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

                /*double LatPromedio=0;
                latitude [0] = latitude [1];
                latitude [1] = latitude [2];
                latitude [2] = location.getLatitude();

                if(latitude[0]!=0 && latitude[1]!=0){
                LatPromedio = (latitude[0]+latitude[1])/2;}
                if(LatPromedio!=0 && latitude[2]!=0){
                    LatPromedio = (LatPromedio+latitude[2])/2;
                    Toast.makeText(getBaseContext(), Double.toString(LatPromedio), Toast.LENGTH_SHORT).show();
                }*/
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    // Guardar el recorrido de las persona
    private void ejecutar(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LatLong();
                GuardarRecorrido();
                handler.postDelayed(this,20000);//se ejecutara cada 20 segundos
            }
        },0);//empezara a ejecutarse después de 5 milisegundos
    }

    private void GuardarRecorrido(){
        //permisosEscribir();
        // Agrego la cabecera en .csv
        File ReleVar = new File(Environment.getExternalStorageDirectory() +
                "/RelevAr");
        File nuevaCarpeta = new File(ReleVar, "RECORRIDOS");
        nuevaCarpeta.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "Recorridos-"+fecha+".csv";

        File dir = new File(nuevaCarpeta, NombreArchivo);

        Calendar calendario = new GregorianCalendar();
        int hora, minutos, segundos;
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);

        if(Latitud!=null && Longitud!=null){
        String cabecera = Integer.toString(hora)+":"+Integer.toString(minutos)+":"+Integer.toString(segundos)+";"+Latitud+" "+Longitud+";"+IDencuestador+"\n";
        try {
                FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
                // que se agreguen los datos al final sin perder los datos anteriores
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(cabecera);
                myOutWriter.close();
                fOut.close();

        } catch (IOException e){
                e.printStackTrace();
        }}
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SOLICITUD DE PERMISOS

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

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // AGREGAR CABECERA POR UNICA VEZ A LOS ARCHIVOS DE DATOS

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
        if(strLine.equals("CALLE")!=true){
        String cabecera = "CALLE;NUMERO;COORDENADAS;GRUPO FAMILIAR;DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;" +
                "FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;" +
                "LOTE DE VACUNA;TELEFONO CELULAR;TELEFONO FIJO;MAIL;OBSERVACIONES;PRODUCTO DE LIMPIEZA;ENCUESTADOR\n";
        try {

            FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
            // que se agreguen los datos al final sin perder los datos anteriores
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            //BufferedWriter writer = null;
            //writer = new BufferedWriter( new OutputStreamWriter(
            //        new FileOutputStream( dir ),"UTF-8"));
            myOutWriter.append(cabecera);
            myOutWriter.close();
            fOut.close();
           //Toast.makeText(this, strLine+"2", Toast.LENGTH_SHORT).show();

        } catch (IOException e){
            e.printStackTrace();
            //Toast.makeText(this, "Datos NO guardados", Toast.LENGTH_SHORT).show();
        }}
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // AGREGAR, EDITAR O ELIMINAR UNA PERSONA Y VISUALIZARLAS
    public void NuevaPersona(View view){
        Intent Modif= new Intent (this, persona.class);
        startActivityForResult(Modif, 1);}

    private void ListeVer(){

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, names);
        lv1.setAdapter(adapter);

        // Elegir entre eliminar y editar

        //realizar accion con el listview
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p1, long id) {
                //Elegir entre Eliminar y Editar
                position=p1;
                EliminarEditar();
                //makeText(getBaseContext(), Integer.toString(position), LENGTH_SHORT).show();
                //makeText(getBaseContext(), MiembrosFamiliares.get(0).DNI, LENGTH_SHORT).show();
            }
        });
    }

    // Eliminar o editar un registro de una persona
    private void EliminarEditar(){
        // Defino los contenedores
        //makeText(getBaseContext(), MiembrosFamiliares.get(position).DNI, LENGTH_SHORT).show();
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

        // Informacion de la persona
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final TextView descripcion = new TextView(getApplicationContext());
        String datospersonas = "DNI: "+ MiembrosFamiliares.get(position).DNI+"\nNombre: "+ MiembrosFamiliares.get(position).Nombre+
        "\nApellido: "+MiembrosFamiliares.get(position).Apellido+"\nEdad: "+MiembrosFamiliares.get(position).Edad+" "+MiembrosFamiliares.get(position).UnidadEdad+
                "\nFecha de nacimiento: " + MiembrosFamiliares.get(position).Nacimiento + "\nEfector: "+MiembrosFamiliares.get(position).Efector+"\nFactores de Riesgo: "+MiembrosFamiliares.get(position).FactoresDeRiesgo+"\nVacunas aplicadas: "+
                MiembrosFamiliares.get(position).Vacunas+"\nLote de vacunas: "+MiembrosFamiliares.get(position).LoteVacuna+"\nCelular: "+
                MiembrosFamiliares.get(position).Celular+"\nObservaciones: "+MiembrosFamiliares.get(position).Observaciones;
        descripcion.setText(datospersonas);
        descripcion.setTextSize(TamañoLetra);
        descripcion.setTextColor(Color.WHITE);
        descripcion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.setMinimumHeight(500);
        layout0.addView(descripcion);

        // Añadir los view al Layout
        mainLayout.addView(layout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                Intent Modif= new Intent (getBaseContext(), persona.class);
                Modif.putExtra("NOMBRE" , MiembrosFamiliares.get(position).Nombre);
                Modif.putExtra("APELLIDO" , MiembrosFamiliares.get(position).Apellido);
                Modif.putExtra("DNI" , MiembrosFamiliares.get(position).DNI);
                Modif.putExtra("EDAD" ,  MiembrosFamiliares.get(position).Edad);
                Modif.putExtra("UNIDADEDAD" ,  MiembrosFamiliares.get(position).UnidadEdad);
                Modif.putExtra("EFECTOR" , MiembrosFamiliares.get(position).Efector);
                Modif.putExtra("FACTORES" , MiembrosFamiliares.get(position).FactoresDeRiesgo);
                Modif.putExtra("CODIGOFACTORES" ,  MiembrosFamiliares.get(position).CodfigoFactorRiesgo);
                Modif.putExtra("VACUNAS" ,  MiembrosFamiliares.get(position).Vacunas);
                Modif.putExtra("CELULAR" ,  MiembrosFamiliares.get(position).Celular);
                Modif.putExtra("FIJO" ,  MiembrosFamiliares.get(position).Fijo);
                Modif.putExtra("MAIL" ,  MiembrosFamiliares.get(position).Mail);
                Modif.putExtra("OBSERVACIONES" , MiembrosFamiliares.get(position).Observaciones);
                Modif.putExtra("NACIMIENTO" , MiembrosFamiliares.get(position).Nacimiento);
                Modif.putExtra("LIMPIEZA" , MiembrosFamiliares.get(position).Limpieza);
                Modif.putExtra("LOTE" , MiembrosFamiliares.get(position).LoteVacuna);
                setResult(RESULT_OK, Modif);
                MiembrosFamiliares.remove(position);
                names.remove(position);

                ListeVer();
                startActivityForResult(Modif, 1);

            }
        });
        builder.setNegativeButton("ELIMINAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MiembrosFamiliares.remove(position);
                names.remove(position);
                ListeVer();
            }
        });
        builder.setNeutralButton("VOLVER", null);
        builder.setView(mainLayout);

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Recibir los datos de la carga de personas
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode== RESULT_OK){
                ObjetoPersona Persona=new ObjetoPersona();
                ArrayList<String> CamposPersona = new ArrayList<String>();

                Persona.DNI = data.getStringExtra("DNI");
                Persona.Nombre = data.getStringExtra("NOMBRE");
                Persona.Apellido = data.getStringExtra("APELLIDO");
                Persona.Edad = data.getStringExtra("EDAD");
                Persona.UnidadEdad = data.getStringExtra("UNIDADEDAD");
                Persona.Efector = data.getStringExtra("EFECTOR");
                Persona.FactoresDeRiesgo = data.getStringExtra("FACTORES");
                Persona.CodfigoFactorRiesgo = data.getStringExtra("CODIGOFACTORES");
                Persona.Vacunas = data.getStringExtra("VACUNAS");
                Persona.Celular = data.getStringExtra("CELULAR");
                Persona.Fijo = data.getStringExtra("FIJO");
                Persona.Mail = data.getStringExtra("MAIL");
                Persona.Observaciones = data.getStringExtra("OBSERVACIONES");
                Persona.Nacimiento = data.getStringExtra("NACIMIENTO");
                Persona.Limpieza = data.getStringExtra("LIMPIEZA");
                Persona.LoteVacuna = data.getStringExtra("LOTE");

                //Agrego la persona como miembro de la familia
                MiembrosFamiliares.add(Persona);

                //makeText(this, Persona.Nombre, LENGTH_SHORT).show();
                names.add("DNI: "+Persona.DNI+" "+Persona.Apellido+" "+Persona.Nombre);}

                ListeVer();
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // GUARDAR GRUPO FAMILIAR

    public void Guardar(View view){
        final String CantidadGrupoFamiliar=grupofamiliar.getText().toString();
        // Inicio la obtencion de datos de ubicacion del GPS
        LatLong();
        if(MiembrosFamiliares.size()!=0){
            if(IDencuestador.length()!=0){
                //makeText(this, Integer.toString(IDencuestador.length()), LENGTH_SHORT).show();
                if (CantidadGrupoFamiliar.length()!= 0) {
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
                        /*BufferedWriter writer = null;
                        writer = new BufferedWriter( new OutputStreamWriter(
                                new FileOutputStream( dir ),"UTF-8"));*/
                        String guardar = null;
                        for (int x = 0; x < MiembrosFamiliares.size(); x++) {
                            guardar = calle.getText().toString() + ";" + numero.getText().toString() + ";" + Latitud + Longitud + ";" + CantidadGrupoFamiliar;
                            guardar+=";"+MiembrosFamiliares.get(x).FormatoGuardar();
                            guardar += ";"+IDencuestador+"\n";
                            myOutWriter.append(guardar);
                        }

                        MiembrosFamiliares.clear();
                        lv1.setAdapter(null);
                        adapter.clear();
                        calle.setText("");
                        numero.setText("");
                        grupofamiliar.setText("");
                        myOutWriter.close();
                        fOut.close();

                    } catch (IOException e) {
                        e.printStackTrace();

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
        }}
            else{ Presentacion();}}
            else{ makeText(this, "NO HAY PERSONAS CARGADAS", LENGTH_SHORT).show();}
    }

    // Desactivo el boton de volver atras
    @Override
    public void onBackPressed()
    {
        //thats it
    }
}

