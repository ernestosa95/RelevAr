package com.example.relevar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.relevar.MySQL.ConexionSQLiteHelper;
import com.example.relevar.MySQL.SQLitePpal;
import com.example.relevar.Recursos.Encuestador;
import com.example.relevar.Recursos.ServicioGPS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

// Descripcion de la Activity:
/*      Esta activity es la pantalla inicial de la App, su principal función es la de preparar los
 *       datos necesarios para poder iniciar el uso de la app.*/

// Metodos de la Activity:

//--------------------------------------------------------------------------------------------------

//  - Oncreate()
//  -

public class MenuMapa extends AppCompatActivity implements OnMapReadyCallback {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    // Widgets
    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private ArrayAdapter adaptador;
    ListView lv1;
    // Inicio de toma de ubicacion
    EditText Nencuestador;
    Button PararServicio, BtnCompartir;
    // Creo al encuestador
    Encuestador encuestador = new Encuestador();
    // Mapa
    private MapView mapView;
    private GoogleMap map;
    LatLng latLng;
    ArrayList<LatLng> recorrido = new ArrayList<>();
    Polyline ruta;
    TextView txt;
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    String directorioraiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Eliminar el action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        // Evitar la rotacion
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }

        // Obtengo el encuestador que esta activado desde la base de datos
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        encuestador.setID(admin.ObtenerActivado());

        // Creo el mapa y lo centro en las coodenadas -60 -30
        mapView = (MapView) findViewById(R.id.MAPA);
        Bundle mapBundle = null;
        txt = (TextView) findViewById(R.id.COMPLETADOFACTORES);
        if (savedInstanceState != null) {
            mapBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapBundle);
        mapView.getMapAsync(this);
        latLng = new LatLng(-60, -30);

        /* Creo y asigno tareas al boton para iniciar o terminar la toma de datos del recorrido por
        medio del GPS*/
        PararServicio = (Button) findViewById(R.id.TERMINAR);
        boolean estado = isMyServiceRunning(ServicioGPS.class);
        if (estado == true) {
            PararServicio.setText(getString(R.string.terminar_recorrido));
        } else {
            PararServicio.setText(getString(R.string.iniciar_recorrido));
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        /* Creo y asigno funciones para compartir los arcivos .csv*/
        directorioraiz = "/storage/emulated/0/RelevAr";
        BtnCompartir = (Button) findViewById(R.id.COMPARTIR);
        BtnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(directorioraiz);
            }
        });
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

    // ESCUCHA AL SERVICIO DE GPS Y ACTUALIZA EL RECORRIDO
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            recorrido = intent.getParcelableArrayListExtra("RECORRIDO");
        }
    };

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

    // BOTON PARA COMPARTIR LOS ARCHIVOS .CSV DESDE LA APP
    @SuppressLint("WrongConstant")
    private void buscar(String RutaDirectorio){

    /* Se crea la lista de los archivos .csv que estan disponibles en la memoria interna en la carpeta
    * RelevAr*/
    listaNombresArchivos = new ArrayList<String>();
    listaRutasArchivos = new ArrayList<String>();
    File directorioactual = new File(RutaDirectorio);
    File[] listaArchivos = directorioactual.listFiles();

    int x=0;
    if(!RutaDirectorio.equals(directorioraiz)){
        listaNombresArchivos.add("../");
        listaRutasArchivos.add(directorioactual.getParent());
        x=1;
    }

    for(File archivo : listaArchivos){
        listaRutasArchivos.add(archivo.getPath());
    }

    Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);

    for(int i=x; i<listaRutasArchivos.size(); i++){
        File archivo = new File(listaRutasArchivos.get(i));
        if(archivo.isFile()){
            listaNombresArchivos.add(archivo.getName());
        } else{
            listaNombresArchivos.add("/"+archivo.getName());
        }
    }

    if(listaArchivos.length<1){
        listaNombresArchivos.add("NO HAY ARCHIVOS");
        listaRutasArchivos.add(RutaDirectorio);
    }

    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    LayoutInflater Inflater = getLayoutInflater();
    View view1 = Inflater.inflate(R.layout.alert_explorador, null);
    builder.setView(view1);
    builder.setCancelable(false);
    final AlertDialog dialog = builder.create();
    dialog.show();

    lv1 = view1.findViewById(R.id.LIDTVIEW1);
    adaptador = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, listaNombresArchivos);
    lv1.setAdapter(adaptador);

    //realizar accion con el listview
    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int p1, long id) {
            File archivo = new File(listaRutasArchivos.get(p1));
            if(archivo.isFile()){
                String ubicacion = archivo.getAbsolutePath();
                //Toast.makeText(getBaseContext(), "es un archivo", 6000).show();
                compartir(archivo.getName());
            } else {
                buscar(listaRutasArchivos.get(p1));
            }
        }
    });
    final Button cancelar = view1.findViewById(R.id.CANCELAR);
    cancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });
}

    // FUNCION QUE INICIA EL ACTION PARA COMPARTIR (MAIL, DRIVE, WSSP U OTRAS OPCIONES)
    private void compartir(String nombreArchivo){
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();

        File dir = new File(nuevaCarpeta, nombreArchivo);

        Uri path = FileProvider.getUriForFile(this, "com.example.relevar", dir);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, nombreArchivo);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Valores.");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        this.startActivity(Intent.createChooser(emailIntent, "SUBIR ARCHIVO"));
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

    // NUEVA FAMILIA
    public void NuevaFamilia(View view) {

        /* Corroboro el estado del servicio de GPS ya que para tomar datos de la persona debe estar
        * tomando datos de ubicación*/
        boolean estado = isMyServiceRunning(ServicioGPS.class);
        if (estado == false) {

            /* Si no esta iniciado el servicio creo una alert para solicitar el inicio del gps*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater Inflater = getLayoutInflater();
            View view1 = Inflater.inflate(R.layout.alert_iniciar_terminar_recorrido, null);
            builder.setView(view1);
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            dialog.show();

            TextView txt1 = view1.findViewById(R.id.CONSULTA);
            txt1.setText("¿Iniciar recorrido?");
            Botones();

            Button si = view1.findViewById(R.id.BTNSI);
            si.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /* Iniciar el recorrido y pasar el foco de la app a un nuevo activity para poder
                    * cargar los datos de una familia*/
                    Intent intent = new Intent(getBaseContext(), ServicioGPS.class);
                    startService(intent);
                    PararServicio.setText(getString(R.string.terminar_recorrido));
                    dialog.dismiss();

                    Intent Modif = new Intent(getBaseContext(), Familia.class);
                    Modif.putExtra("IDENCUESTADOR", encuestador.getID());
                    startActivityForResult(Modif, 1);
                }
            });

            // Cerrar el alert de iniciar el recorrido
            Button no = view1.findViewById(R.id.BTNNO);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {

            /* Si esta iniciado el recorrido, directamente paso el foco de la app al activity para
            * poder cargar la familia*/
            Intent Modif = new Intent(getBaseContext(), Familia.class);
            Modif.putExtra("IDENCUESTADOR", encuestador.getID());
            startActivityForResult(Modif, 1);
        }
    }

    // TERMINAR RECORRIDO
    public void TerminarRecorrido(View view) {

        /* Consulto la situacion del gps*/
        boolean estado = isMyServiceRunning(ServicioGPS.class);

        if (estado == true) {

            /* Si esta iniciado la opcion que debe estar disponible es terminar el recorrido, o
            * reiniciarlo*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater Inflater = getLayoutInflater();
            View view1 = Inflater.inflate(R.layout.alert_iniciar_terminar_recorrido, null);
            builder.setView(view1);
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            dialog.show();

            TextView txt1 = view1.findViewById(R.id.CONSULTA);
            txt1.setText("¿Terminar recorrido?");

            Button si = view1.findViewById(R.id.BTNSI);
            si.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), ServicioGPS.class);
                    stopService(intent);
                    PararServicio.setText(getString(R.string.reiniciar_recorrido));
                    dialog.dismiss();
                }
            });

            Button no = view1.findViewById(R.id.BTNNO);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {

            /* Si el recorrido no esta iniciado, la opcion disponible es iniciar el recorrido*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater Inflater = getLayoutInflater();
            View view1 = Inflater.inflate(R.layout.alert_iniciar_terminar_recorrido, null);
            builder.setView(view1);
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            dialog.show();

            TextView txt1 = view1.findViewById(R.id.CONSULTA);
            txt1.setText("¿Iniciar recorrido?");
            Botones();

            Button si = view1.findViewById(R.id.BTNSI);
            si.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), ServicioGPS.class);
                    startService(intent);
                    PararServicio.setText(getString(R.string.terminar_recorrido));
                    dialog.dismiss();
                }
            });

            Button no = view1.findViewById(R.id.BTNNO);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    // CORROBORAR LA SITUACION DEL GPS
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    // FUNCION QUE ESPERA EL RESULTADO DE LOS SERVICIOS DE GPS
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getParcelableExtra("bundle");
                LatLng position = bundle.getParcelable("from_position");
                latlngs.add(position);
            }
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

    // PARA EL MAPA
    @Override
    public void onMapReady(final GoogleMap map) {

        //map.addMarker(new MarkerOptions().position(new LatLng(Latitud,Longitud)).title("aca toy"));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                //LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(mMessageReceiver, new IntentFilter("recorridos"));
                map.clear();
                ruta = map.addPolyline(new PolylineOptions()
                        .clickable(true).color(Color.parseColor("#69A4D1")));
                int ultimo = recorrido.size();
                if(ultimo!=0){
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(recorrido.get(ultimo-1),17));
                ruta.setPoints(recorrido);

                if (encuestador.Marcadores().size()!=0){
                    ArrayList<LatLng> marcadores = encuestador.Marcadores();
                    for(int i=0; i<encuestador.Marcadores().size(); i++){
                        MarkerOptions mo1 = new MarkerOptions();
                        mo1.position(marcadores.get(i));
                        mo1.title("REGISTRO "+Integer.toString(i+1));
                        //mo1.icon(BitmapDescriptorFactory.fromResource(R.drawable.casa));
                        map.addMarker(mo1);
                    }
                }}

                handler.postDelayed(this, 15000);
            }

        }, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // FUNCIONES DE INICIO, CREAR Y SELECCIONAR EL ENCUESTADOR

    // FUNCION DE ENCUESTADORES, muestra los encuestadores cargados y la posibilidad de crear un
    // nuevo encuestador
    private void Encuestador(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view = Inflater.inflate(R.layout.alert_encuestador, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Defino el spinner y le agrego los encuestadores guardados
        final Spinner encuestadores = view.findViewById(R.id.ENCUESTADOR);
        ArrayList<String> enc = new ArrayList<>();

        // Recupero los encuestadores cargados
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getBaseContext(), "datos", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String consulta ="SELECT * FROM ENCUESTADOR";
        Cursor a = db.rawQuery(consulta, null);
        while (a.moveToNext()){
            enc.add(a.getString(0));
        }
        db.close();

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, enc);
        encuestadores.setAdapter(comboAdapter);

        // Crear nuevo encuestador
        Button nuevo = view.findViewById(R.id.NUEVOENC);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            NuevoEncuestador();
            dialog.dismiss();
            }
        });

        // Ingresar con encuestador existente
        Button ingresar = view.findViewById(R.id.INGRESAR);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (encuestadores.getAdapter().getCount()!=0){
            encuestador.setID(encuestadores.getSelectedItem().toString());
            //makeText(getBaseContext(), encuestador.getID(), LENGTH_SHORT).show();
            dialog.dismiss();}
                else {makeText(getBaseContext(), "NO HAY ENCUESTADORES", LENGTH_SHORT).show();}
            }
        });
    }

    // FUNCION DE CREAR NUEVO ENCUESTADOR
    private void NuevoEncuestador(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view = Inflater.inflate(R.layout.alert_nuevo_encuestador, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Nencuestador = view.findViewById(R.id.EditNuevoEncuestador);
        Button nuevo = view.findViewById(R.id.GUARDAR);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ingreso datos del encuestador
                if (Nencuestador.getText().toString().isEmpty()){makeText(getBaseContext(), "INGRESE NOMBRE Y APELLIDO", LENGTH_SHORT).show();}
                else {
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getBaseContext(), "datos", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();
                String insert ="INSERT INTO ENCUESTADOR (ID) VALUES ('"+Nencuestador.getText().toString()+"')";
                db.execSQL(insert);
                db.close();

                // Inicializo el encuestador
                encuestador.setID(Nencuestador.getText().toString());
                dialog.dismiss();}
            }
        });

        Button cancelar = view.findViewById(R.id.CANCELAR);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Encuestador();
                dialog.dismiss();
            }
        });
    }

    // ABRIR EL EXPLORADOR DE ARCHIVOS
    public void MostrarArchivos(View view){
        Uri selectedUri = Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/RelevAr");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "application/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // Muestro la check list de los botones

    private void Botones(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_listado_botones, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        admin.DesactivarBotones();

        final Switch inspeccionExterior = view1.findViewById(R.id.SWITCHINSPECCIONEXTERIOR);
        inspeccionExterior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(inspeccionExterior.getText().toString());
                } else {
                    admin.DesactivarBoton(inspeccionExterior.getText().toString());
                }
            }
        });

        final Switch serviciosBasicos = view1.findViewById(R.id.SWITCHSERVICIOSBASICOS);
        serviciosBasicos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(serviciosBasicos.getText().toString());
                } else {
                    admin.DesactivarBoton(serviciosBasicos.getText().toString());
                }
            }
        });

        final Switch vivienda = view1.findViewById(R.id.SWITCHVIVIENDA);
        vivienda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(vivienda.getText().toString());
                } else {
                    admin.DesactivarBoton(vivienda.getText().toString());
                }
            }
        });

        admin.close();
        final Button listo = view1.findViewById(R.id.LISTOBOTON);
        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // Desactivo el boton de volver atras
    @Override
    public void onBackPressed()
    {
        //thats it
    }// Desactivo el boton de volver atras

}
