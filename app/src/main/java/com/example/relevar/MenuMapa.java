package com.example.relevar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.relevar.MySQL.ConexionSQLiteHelper;
import com.example.relevar.MySQL.SQLitePpal;
import com.example.relevar.Recursos.Encuestador;
import com.example.relevar.Recursos.ServicioGPS;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.Environment.getExternalStorageDirectory;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Descripcion de la Activity:
/*      Esta activity es la pantalla inicial de la App, su principal función es la de preparar los
 *       datos necesarios para poder iniciar el uso de la app.*/

// Metodos de la Activity:

//--------------------------------------------------------------------------------------------------

//  - Oncreate()
//  - BroadcastReceiver()
//  - Buscar(), Compartir()
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

    // Creo al encuestador
    Encuestador encuestador;
    // Mapa
    private MapView mapView;
    LatLng latLng;
    ArrayList<LatLng> recorrido = new ArrayList<>();
    Polyline ruta;
    //private ArrayList<LatLng> latlngs = new ArrayList<>();
    String directorioraiz;
    Spinner fechas;
    ArrayList<String> MiembrosFamiliares = new ArrayList<>();
    ArrayList<String> DatosMiembrosFamiliares = new ArrayList<>();
    ArrayList<String> categoriasPersona = new ArrayList<>();
    ArrayList<String> familiaCabecera = new ArrayList<>();

    private  Switch serviciosBasicos, vivienda, dengue, educacion, ocupacion, contacto, efector, observaciones,
            factores_riesgo, discapacidad, embarazo, vitamina_D, acompañamiento, transtornos_niños,
            trastornos_mentales, adicciones, violencia, ocio, enfermedadescronicas, inspeccionExterior;
    TextView encabezado;
    ImageView volver;
    int [] pantallas = {1};

    ArrayList<ObjetoFamilia> datosFamilias = new ArrayList<>();
    ArrayList<ObjetoPersona> datosPersonas = new ArrayList<>();
    ArrayList<String> fechas_disponibles = new ArrayList<>();

    FloatingActionButton ITrecorrido , Compartir;

    String direccion = "http://relevar.ddns.net:1492";
    //String direccion = "http://192.168.0.102:1492";

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
        encuestador = new Encuestador(getApplicationContext());
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        encuestador.setID(admin.ObtenerActivado());

        // Creo el mapa y lo centro en las coodenadas -60 -30
        mapView = (MapView) findViewById(R.id.MAPA);
        Bundle mapBundle = null;

        if (savedInstanceState != null) {
            mapBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapBundle);
        mapView.getMapAsync(this);
        latLng = new LatLng(-60, -30);

        /* Creo y asigno tareas al boton para iniciar o terminar la toma de datos del recorrido por
        medio del GPS*/
       // PararServicio = (Button) findViewById(R.id.TERMINAR);

        ITrecorrido = (FloatingActionButton) findViewById(R.id.recorrido);
        ITrecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TerminarRecorrido();
            }
        });

        boolean estado = isMyServiceRunning(ServicioGPS.class);
        if (estado == true) {
         //   PararServicio.setText(getString(R.string.terminar_recorrido));
            ITrecorrido.setTitle(getString(R.string.terminar_recorrido));
        } else {
            //PararServicio.setText(getString(R.string.iniciar_recorrido));
            ITrecorrido.setTitle(getString(R.string.iniciar_recorrido));
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        /* Creo y asigno funciones para compartir los arcivos .csv*/
        directorioraiz = "/storage/emulated/0/RelevAr";

        Compartir = (FloatingActionButton) findViewById(R.id.compartir);
        Compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(directorioraiz);
            }
        });

        fechas = findViewById(R.id.FECHAS);
        // Cargo el spinner con los datos de los encuestadores
        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spiner_personalizado, FechasArchivos());
        fechas.setAdapter(comboAdapter);

        categoriasPersona.add(getString(R.string.celular));
        categoriasPersona.add(getString(R.string.fijo));
        categoriasPersona.add(getString(R.string.mail));
        categoriasPersona.add(getString(R.string.factores_riesgo));
        categoriasPersona.add(getString(R.string.efector));
        categoriasPersona.add(getString(R.string.observaciones));
        categoriasPersona.add(getString(R.string.nombre_apellido_contacto));
        categoriasPersona.add(getString(R.string.telefono_contacto));
        categoriasPersona.add(getString(R.string.parentezco_contacto));
        categoriasPersona.add(getString(R.string.ocupacion));
        categoriasPersona.add(getString(R.string.educacion));
        categoriasPersona.add(getString(R.string.vitamina));
        categoriasPersona.add(getString(R.string.fecha_nacimiento));
        categoriasPersona.add(getString(R.string.ultimo_control));
        categoriasPersona.add(getString(R.string.enfermedad_relacionada_embarazo));
        categoriasPersona.add(getString(R.string.certificado_unico_discapacidad));
        categoriasPersona.add(getString(R.string.tipo_discapacidad));
        categoriasPersona.add(getString(R.string.acompañamiento));
        categoriasPersona.add(getString(R.string.transtornos_en_niños));
        categoriasPersona.add(getString(R.string.adicciones));
        categoriasPersona.add(getString(R.string.actividades_ocio));
        categoriasPersona.add(getString(R.string.donde_ocio));
        categoriasPersona.add(getString(R.string.tipo_violencia));
        categoriasPersona.add(getString(R.string.modalidad_violencia));
        categoriasPersona.add(getString(R.string.trastornos_mentales));
        categoriasPersona.add(getString(R.string.enfermedad_cronica));
        categoriasPersona.add(getString(R.string.plan_social));

        familiaCabecera.add(getString(R.string.tipo_de_vivienda));
        familiaCabecera.add(getString(R.string.dueño_vivienda));
        familiaCabecera.add(getString(R.string.cantidad_piezas));
        familiaCabecera.add(getString(R.string.lugar_cocinar));
        familiaCabecera.add(getString(R.string.usa_para_cocinar));
        familiaCabecera.add(getString(R.string.paredes));
        familiaCabecera.add(getString(R.string.revestimiento));
        familiaCabecera.add(getString(R.string.pisos));
        familiaCabecera.add(getString(R.string.cielorraso));
        familiaCabecera.add(getString(R.string.techo));
        familiaCabecera.add(getString(R.string.agua));
        familiaCabecera.add(getString(R.string.origenagua));
        familiaCabecera.add(getString(R.string.excretas));
        familiaCabecera.add(getString(R.string.electricidad));
        familiaCabecera.add(getString(R.string.gas));
        familiaCabecera.add(getString(R.string.agua_lluvia));
        familiaCabecera.add(getString(R.string.arboles));
        familiaCabecera.add(getString(R.string.baño));
        familiaCabecera.add(getString(R.string.baño_tiene));
        familiaCabecera.add(getString(R.string.hielo));
        familiaCabecera.add(getString(R.string.perros_sueltos));
        familiaCabecera.add(getString(R.string.telefono_familiar));

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

    // Realizar accion con el listview
    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int p1, long id) {
            File archivo = new File(listaRutasArchivos.get(p1));
            if(archivo.isFile()){
                compartir(listaRutasArchivos.get(p1));
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
    final Button EnviarGNU = view1.findViewById(R.id.enviarGNU);
    EnviarGNU.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CrearUnificado();
        }
    });
}

    // FUNCION QUE INICIA EL ACTION PARA COMPARTIR (MAIL, DRIVE, WSSP U OTRAS OPCIONES)
    private void compartir(String nombreArchivo){
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();

        File dir = new File(nombreArchivo);
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

            Button si = view1.findViewById(R.id.BTNSI);
            si.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /* Iniciar el recorrido y pasar el foco de la app a un nuevo activity para poder
                    * cargar los datos de una familia*/
                    Intent intent = new Intent(getBaseContext(), ServicioGPS.class);
                    startService(intent);
                    //PararServicio.setText(getString(R.string.terminar_recorrido));
                    ITrecorrido.setTitle(getString(R.string.terminar_recorrido));
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

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // RECORRIDO
    // TERMINAR RECORRIDO
    public void TerminarRecorrido() {

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
                    ITrecorrido.setTitle(getString(R.string.reiniciar_recorrido));
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

            Button si = view1.findViewById(R.id.BTNSI);
            si.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), ServicioGPS.class);
                    startService(intent);
                    //PararServicio.setText(getString(R.string.terminar_recorrido));
                    ITrecorrido.setTitle(getString(R.string.terminar_recorrido));
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

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // MAPA
    @Override
    public void onMapReady(final GoogleMap map) {

        //map.addMarker(new MarkerOptions().position(new LatLng(Latitud,Longitud)).title("aca toy"));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        LatLng posinicial = new LatLng(-34.891920, -63.719044);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(posinicial,3));

        // Funcion que le da al boton centrar la funcionalidad
        FloatingActionButton centrar = (FloatingActionButton) findViewById(R.id.centrar);
        centrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager service = (LocationManager)
                        getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = service.getBestProvider(criteria, false);
                Location location = service.getLastKnownLocation(provider);
                try{
                    LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,17));}
                catch (Exception e) {
                    Toast.makeText(getBaseContext(), "AGUARDE, EL GPS SE ESTA LOCALIZANDO" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        // funcionalidad de los ioconos de marcado para que cuando se los toca se centre el mapa sobre
        // ellos y se despliegue la ventana secundaria de informacion
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng position = marker.getPosition();
                String pos = Double.toString(position.latitude)+" "+Double.toString(position.longitude);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));
                //Toast.makeText(getBaseContext(),pos, Toast.LENGTH_SHORT).show();
                InfoFamilia(pos);
                return false;
            }
        });

        // Este handler (lo que hace es ejecutar cada cierto tiempo el codigo que tiene adentro),
        // borra y vuelve a dibujar los marcadores para mantener actualizado el mapa, cada vez que
        // se carga una nueva familia
        final Handler handlerMarker = new Handler();
        handlerMarker.postDelayed(new Runnable() {
            @Override
            public void run() {
                map.clear();

                ruta = map.addPolyline(new PolylineOptions()
                        .clickable(true).color(Color.parseColor("#69A4D1")));
                int ultimo = recorrido.size();
                if(ultimo!=0){
                    //map.moveCamera(CameraUpdateFactory.newLatLngZoom(recorrido.get(ultimo-1),17));
                    ruta.setPoints(recorrido);}

                if (encuestador.Marcadores(fechas.getSelectedItem().toString()).size()!=0){
                    ArrayList<LatLng> marcadores = encuestador.Marcadores(fechas.getSelectedItem().toString());
                    ArrayList<String> codigoColores = encuestador.CodigoColores(fechas.getSelectedItem().toString());
                    //ArrayList<String> codigoCartografia = encuestador.CodigoCartografia(fechas.getSelectedItem().toString());
                    for(int i=0; i<encuestador.Marcadores(fechas.getSelectedItem().toString()).size(); i++){

                        MarkerOptions mo1 = new MarkerOptions();
                        mo1.position(marcadores.get(i));
                        /*if(codigoCartografia.get(i).length()!=0){
                            mo1.title("CARTOGRAFIA "+codigoCartografia.get(i));
                        }
                        else {mo1.title("SIN CODIGO");}*/
                        //mo1.icon(BitmapDescriptorFactory.fromResource(getBitmap(R.id)));
                        Bitmap drawableBitmap = null;
                        if(codigoColores.get(i).equals("R")){
                            drawableBitmap = getBitmap(R.drawable.icono_mapa_rojo);}
                        if(codigoColores.get(i).equals("A")){
                            drawableBitmap = getBitmap(R.drawable.icono_mapa_amarilla);}
                        if(codigoColores.get(i).equals("V")){
                            drawableBitmap = getBitmap(R.drawable.icono_mapa_verde);}
                        mo1.icon(BitmapDescriptorFactory.fromBitmap(drawableBitmap));
                        map.addMarker(mo1);
                    }}
                handlerMarker.postDelayed(this, 2000);
            }
        }, 0);

    }

    /* Transformo los xml de los iconos en un bitmap para que puedan ser graficados */
    private Bitmap getBitmap(int drawableRes){
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap( (int)getResources().getDimension(R.dimen.ancho_map), (int)getResources().getDimension(R.dimen.alto_map), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.alto_icono),
                (int)getResources().getDimension(R.dimen.ancho_icono));
        drawable.draw(canvas);
        return bitmap;
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
    /*
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
        Uri selectedUri = Uri.parse("file:/" + Environment.getExternalStorageDirectory() + "/RelevAr");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "application/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }*/

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // MENU DE ACTIVACION DE BOTONES

    public void Botones(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_listado_botones, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        pantallas[0]=1;

        encabezado = view1.findViewById(R.id.ENCABEZADOMODULOS);
        encabezado.setText(getString(R.string.modulos)+": "+getString(R.string.grupo_familiar));

        final SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        //admin.DesactivarBotones();

        inspeccionExterior = view1.findViewById(R.id.SWITCHINSPECCIONEXTERIOR);
        if(admin.EstadoBoton("INSPECCION EXTERIOR")){
            inspeccionExterior.setChecked(true);
        }

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
        //inspeccionExterior.setVisibility(View.GONE);

        serviciosBasicos = view1.findViewById(R.id.SWITCHSERVICIOSBASICOS);
        if(admin.EstadoBoton("SERVICIOS BASICOS")){
            serviciosBasicos.setChecked(true);
        }

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
        //serviciosBasicos.setVisibility(View.GONE);

        vivienda = view1.findViewById(R.id.SWITCHVIVIENDA);
        if(admin.EstadoBoton("VIVIENDA")){
            vivienda.setChecked(true);
        }

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
        //vivienda.setVisibility(View.GONE);

        dengue = view1.findViewById(R.id.SWITCHDENGUE);
        if(admin.EstadoBoton("DENGUE")){
            dengue.setChecked(true);
        }

        dengue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(dengue.getText().toString());
                } else {
                    admin.DesactivarBoton(dengue.getText().toString());
                }
            }
        });
        //dengue.setVisibility(View.GONE);

        educacion = view1.findViewById(R.id.SWITCHEDUCACION);
        if(admin.EstadoBoton("EDUCACION")){
            educacion.setChecked(true);
        }

        educacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(educacion.getText().toString());
                } else {
                    admin.DesactivarBoton(educacion.getText().toString());
                }
            }
        });
        educacion.setVisibility(View.GONE);

        ocupacion = view1.findViewById(R.id.SWITCHOCUPACION);
        if(admin.EstadoBoton("INGRESO Y OCUPACION")){
            ocupacion.setChecked(true);
        }

        ocupacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(ocupacion.getText().toString());
                } else {
                    admin.DesactivarBoton(ocupacion.getText().toString());
                }
            }
        });
        ocupacion.setVisibility(View.GONE);

        contacto = view1.findViewById(R.id.SWITCHCONTACTO);
        if(admin.EstadoBoton("CONTACTO")){
            contacto.setChecked(true);
        }
        contacto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(contacto.getText().toString());
                } else {
                    admin.DesactivarBoton(contacto.getText().toString());
                }
            }
        });
        contacto.setVisibility(View.GONE);

        efector = view1.findViewById(R.id.SWITCHEFECTOR);
        if(admin.EstadoBoton("EFECTOR")){
            efector.setChecked(true);
        }
        efector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(efector.getText().toString());
                } else {
                    admin.DesactivarBoton(efector.getText().toString());
                }
            }
        });
        efector.setVisibility(View.GONE);

        observaciones = view1.findViewById(R.id.SWITCHOBSERVACIONES);
        if(admin.EstadoBoton("OBSERVACIONES")){
            observaciones.setChecked(true);
        }

        observaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(observaciones.getText().toString());
                } else {
                    admin.DesactivarBoton(observaciones.getText().toString());
                }
            }
        });
        observaciones.setVisibility(View.GONE);

        // ESTADO RIESGO
        factores_riesgo = view1.findViewById(R.id.SWITCHFACTORESRIESGO);
        if(admin.EstadoBoton("FACTORES DE RIESGO")){
            factores_riesgo.setChecked(true);
        }

        factores_riesgo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(factores_riesgo.getText().toString());
                } else {
                    admin.DesactivarBoton(factores_riesgo.getText().toString());
                }
            }
        });
        factores_riesgo.setVisibility(View.GONE);

        discapacidad = view1.findViewById(R.id.SWITCHDISCAPACIDAD);
        if(admin.EstadoBoton("DISCAPACIDAD")){
            discapacidad.setChecked(true);
        }

        discapacidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(discapacidad.getText().toString());
                } else {
                    admin.DesactivarBoton(discapacidad.getText().toString());
                }
            }
        });
        discapacidad.setVisibility(View.GONE);

        embarazo = view1.findViewById(R.id.SWITCHEMBARAZO);
        if(admin.EstadoBoton("EMBARAZO")){
            embarazo.setChecked(true);
        }
        embarazo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(embarazo.getText().toString());
                } else {
                    admin.DesactivarBoton(embarazo.getText().toString());
                }
            }
        });
        embarazo.setVisibility(View.GONE);

        vitamina_D = view1.findViewById(R.id.SWITCHVITAMINAD);
        if(admin.EstadoBoton("VITAMINA D")){
            vitamina_D.setChecked(true);
        }

        vitamina_D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(vitamina_D.getText().toString());
                } else {
                    admin.DesactivarBoton(vitamina_D.getText().toString());
                }
            }
        });
        vitamina_D.setVisibility(View.GONE);

        // ESTADO PSICO-SOCIAL
        acompañamiento = view1.findViewById(R.id.SWITCHACOMPAÑAMIENTO);
        if(admin.EstadoBoton("ACOMPAÑAMIENTO")){
            acompañamiento.setChecked(true);
        }

        acompañamiento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(acompañamiento.getText().toString());
                } else {
                    admin.DesactivarBoton(acompañamiento.getText().toString());
                }
            }
        });
        acompañamiento.setVisibility(View.GONE);

        transtornos_niños = view1.findViewById(R.id.SWITCHTRANSTORNOSENNIÑOS);
        if(admin.EstadoBoton("TRASTORNOS EN NIÑOS")){
            transtornos_niños.setChecked(true);
        }

        transtornos_niños.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(transtornos_niños.getText().toString());
                } else {
                    admin.DesactivarBoton(transtornos_niños.getText().toString());
                }
            }
        });
        transtornos_niños.setVisibility(View.GONE);

        trastornos_mentales = view1.findViewById(R.id.SWITCHTRANSTORNOSMENTALES);
        if(admin.EstadoBoton("TRASTORNOS MENTALES")){
            trastornos_mentales.setChecked(true);
        }

        trastornos_mentales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(trastornos_mentales.getText().toString());
                } else {
                    admin.DesactivarBoton(trastornos_mentales.getText().toString());
                }
            }
        });
        trastornos_mentales.setVisibility(View.GONE);

        adicciones = view1.findViewById(R.id.SWITCHADICCIONES);
        if(admin.EstadoBoton("ADICCIONES")){
            adicciones.setChecked(true);
        }

        adicciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(adicciones.getText().toString());
                } else {
                    admin.DesactivarBoton(adicciones.getText().toString());
                }
            }
        });
        adicciones.setVisibility(View.GONE);

        violencia = view1.findViewById(R.id.SWITCHVIOLENCIA);
        if(admin.EstadoBoton("VIOLENCIA")){
            violencia.setChecked(true);
        }

        violencia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(violencia.getText().toString());
                } else {
                    admin.DesactivarBoton(violencia.getText().toString());
                }
            }
        });
        violencia.setVisibility(View.GONE);

        ocio = view1.findViewById(R.id.SWITCHOCIO);
        if(admin.EstadoBoton("OCIO")){
            ocio.setChecked(true);
        }

        ocio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(ocio.getText().toString());
                } else {
                    admin.DesactivarBoton(ocio.getText().toString());
                }
            }
        });
        ocio.setVisibility(View.GONE);

        enfermedadescronicas = view1.findViewById(R.id.SWITCHENFERMEDADCRONICA);
        if(admin.EstadoBoton("ENFERMEDADES CRONICAS")){
            enfermedadescronicas.setChecked(true);
        }

        enfermedadescronicas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(enfermedadescronicas.getText().toString());
                } else {
                    admin.DesactivarBoton(enfermedadescronicas.getText().toString());
                }
            }
        });
        enfermedadescronicas.setVisibility(View.GONE);

        admin.close();
        final Button listo = view1.findViewById(R.id.LISTOBOTON);
        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pantallas[0]<0){
                    pantallas[0]=0;
                }
              OpcionesPantallaBoton(dialog);
            }
        });

        volver = view1.findViewById(R.id.VOLVER);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pantallas[0]>0){
                    int aux = pantallas[0]-1;
                    pantallas[0]-=2;
                    //Toast.makeText(getBaseContext(), Integer.toString(aux), Toast.LENGTH_SHORT).show();
                    OpcionesPantallaBoton(dialog);
                }
            }
        });
        volver.setVisibility(View.GONE);


    }

    private void OpcionesPantallaBoton(AlertDialog dialog){
        switch (pantallas[0]){
            case 0:
                volver.setVisibility(View.INVISIBLE);
                educacion.setVisibility(View.GONE);
                ocupacion.setVisibility(View.GONE);
                contacto.setVisibility(View.GONE);
                efector.setVisibility(View.GONE);
                observaciones.setVisibility(View.GONE);

                inspeccionExterior.setVisibility(View.VISIBLE);
                serviciosBasicos.setVisibility(View.VISIBLE);
                vivienda.setVisibility(View.VISIBLE);
                dengue.setVisibility(View.VISIBLE);
                //Toast.makeText(getBaseContext(), Integer.toString(pantallas[0]), Toast.LENGTH_SHORT).show();

                encabezado.setText(getString(R.string.modulos)+": "+getString(R.string.grupo_familiar));

                pantallas[0] +=1;
                break;
            case 1:
                volver.setVisibility(View.VISIBLE);
                inspeccionExterior.setVisibility(View.GONE);
                serviciosBasicos.setVisibility(View.GONE);
                vivienda.setVisibility(View.GONE);
                dengue.setVisibility(View.GONE);
                factores_riesgo.setVisibility(View.GONE);
                discapacidad.setVisibility(View.GONE);
                embarazo.setVisibility(View.GONE);
                vitamina_D.setVisibility(View.GONE);
                enfermedadescronicas.setVisibility(View.GONE);

                //Toast.makeText(getBaseContext(), Integer.toString(pantallas[0]), Toast.LENGTH_SHORT).show();

                encabezado.setText(getString(R.string.modulos)+": "+getString(R.string.persona_general));

                educacion.setVisibility(View.VISIBLE);
                ocupacion.setVisibility(View.VISIBLE);
                contacto.setVisibility(View.VISIBLE);
                efector.setVisibility(View.VISIBLE);
                observaciones.setVisibility(View.VISIBLE);

                pantallas[0] +=1;
                break;
            case 2:
                volver.setVisibility(View.VISIBLE);
                educacion.setVisibility(View.GONE);
                ocupacion.setVisibility(View.GONE);
                contacto.setVisibility(View.GONE);
                efector.setVisibility(View.GONE);
                observaciones.setVisibility(View.GONE);
                acompañamiento.setVisibility(View.GONE);
                transtornos_niños.setVisibility(View.GONE);
                trastornos_mentales.setVisibility(View.GONE);
                adicciones.setVisibility(View.GONE);
                violencia.setVisibility(View.GONE);
                ocio.setVisibility(View.GONE);

                encabezado.setText(getString(R.string.modulos)+": "+getString(R.string.persona_estado_fisico));

                factores_riesgo.setVisibility(View.VISIBLE);
                discapacidad.setVisibility(View.VISIBLE);
                embarazo.setVisibility(View.VISIBLE);
                vitamina_D.setVisibility(View.VISIBLE);
                enfermedadescronicas.setVisibility(View.VISIBLE);

                pantallas[0] +=1;
                break;
            case 3:
                volver.setVisibility(View.VISIBLE);
                factores_riesgo.setVisibility(View.GONE);
                discapacidad.setVisibility(View.GONE);
                embarazo.setVisibility(View.GONE);
                vitamina_D.setVisibility(View.GONE);
                enfermedadescronicas.setVisibility(View.GONE);

                encabezado.setText(getString(R.string.modulos)+": "+getString(R.string.persona_psico_social));

                acompañamiento.setVisibility(View.VISIBLE);
                transtornos_niños.setVisibility(View.VISIBLE);
                trastornos_mentales.setVisibility(View.VISIBLE);
                adicciones.setVisibility(View.VISIBLE);
                violencia.setVisibility(View.VISIBLE);
                ocio.setVisibility(View.VISIBLE);
                pantallas[0] +=1;

                break;
            case 4:
                dialog.dismiss();
                break;
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // NAVEGACION DE ARCHIVOS
    // Obtener listado de archivos disponibles
    private ArrayList<String> FechasArchivos (){
        /* Se crea la lista de los archivos .csv que estan disponibles en la memoria interna en la carpeta
         * RelevAr*/
        ArrayList<String> listaFechasArchivos = new ArrayList<String>();
        ArrayList<String> listaRutasFechasArchivos = new ArrayList<String>();
        String RutaDirectorio = "/storage/emulated/0/RelevAr";
        File directorioactual = new File(RutaDirectorio);
        File[] listaArchivos = directorioactual.listFiles();

        int x=0;

        for(File archivo : listaArchivos){
            listaRutasFechasArchivos.add(archivo.getPath());
        }

        Collections.sort(listaRutasFechasArchivos, String.CASE_INSENSITIVE_ORDER);

        for(int i=x; i<listaRutasFechasArchivos.size(); i++){
            File archivo = new File(listaRutasFechasArchivos.get(i));
            if(archivo.isFile()){
                String[] auxCorte = archivo.getName().split("-");
                String auxNombre = auxCorte[1]+"-"+auxCorte[2]+"-"+auxCorte[3];
                //auxNombre.replaceAll(".csv", "");
                listaFechasArchivos.add(auxNombre);
            } else{
                //listaFechasArchivos.add("/"+archivo.getName());
            }
        }
        Collections.reverse(listaFechasArchivos);
     return listaFechasArchivos;
    }

    // Asigno la informacion a los diferentes widgets
    private void InfoFamilia(String coordenadas){
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuMapa.this);
        LayoutInflater Inflater = getLayoutInflater();
        View view = Inflater.inflate(R.layout.alert_info_familia, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final String infoFamilia = LeerInfo(coordenadas);
        final TextView txtInfoFamilia = view.findViewById(R.id.DATOSFAMILIARES);
        txtInfoFamilia.setText(infoFamilia);

        final ImageButton volverInfoFamilia = view.findViewById(R.id.VOLVERANTERIOR);
        volverInfoFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInfoFamilia.setText(infoFamilia);
            }
        });

        final ListView Personas = view.findViewById(R.id.LISTMIEMBROS);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, MiembrosFamiliares);
        Personas.setAdapter(adapter);

        Personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtInfoFamilia.setText(DatosMiembrosFamiliares.get(position));
            }
        });

        final ImageButton cancelar = view.findViewById(R.id.CANCELARINFOFMAILIA);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // NECESITO CREAR UNA LISTA DE PERSONAS EN DETERMINADA COORDENDA Y UN OBJETO FAMILIA PARA
        // PASARLE A LA PARTE DE CARGAR DATOS
        final Button editar = view.findViewById(R.id.EDITARREGISTRO);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String msg = datosPersonas.get(0).datosEditar.get("DNI");
                //Toast.makeText(getBaseContext(), msg , Toast.LENGTH_SHORT).show();

                Intent Modif= new Intent (getBaseContext(), Familia.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("FAMILIA", datosFamilias.get(0));
                Modif.putExtra("PERSONAS", datosPersonas);
                Modif.putExtra("FECHA", fechas.getSelectedItem().toString());
                Modif.putExtras(bundle);
                startActivity(Modif);
                dialog.dismiss();
            }
        });
    }

    // Busco la info de la familia con las coordenadas
    private String LeerInfo(String coordenadas){
        MiembrosFamiliares.clear();
        DatosMiembrosFamiliares.clear();

        datosFamilias.clear();
        datosFamilias.add(new ObjetoFamilia(familiaCabecera));
        datosPersonas.clear();

        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        //fecha = fechas.getSelectedItem().toString();
        String NombreArchivo = "RelevAr-" + fechas.getSelectedItem().toString();//+ fecha + ".csv";
        File dir = new File(nuevaCarpeta, NombreArchivo);

        String[] cabecera;
        String datosFamilia_string="";
        try {
            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            cabecera = br.readLine().split(";");
            String myData;
            while ((myData=br.readLine())!=null){
                datosFamilia_string="";
                String[] Datos = myData.split(";");
                String datosMostrar = "";
                if(Datos[2].equals(coordenadas)){
                    String identificacion="";
                    if(Datos.length>=8){identificacion+=Datos[7]+",";}
                    if(Datos.length>=9){identificacion+=Datos[8]+",";}
                    if(Datos.length>=10){identificacion+=Datos[9]+",";}

                    MiembrosFamiliares.add(identificacion);
                    // Tengo datos fijos de los cuales conozco la ubicacion
                    for (int i=0; i<Datos.length; i++) {
                        for (int j = 0; j < categoriasPersona.size(); j++) {
                            if (cabecera[i].equals(categoriasPersona.get(j))) {
                                datosMostrar += categoriasPersona.get(j) + ": " + Datos[i] + "\n";
                            }
                        }
                        for (int k=0; k < familiaCabecera.size(); k++){
                            if (cabecera[i].equals(familiaCabecera.get(k))){
                                datosFamilia_string += familiaCabecera.get(k)+": "+ Datos[i]+ "\n";
                                //Toast.makeText(this, Datos[i], Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    // CREO LOS OBJETOS PARA EDITAR LOS VALORES
                    for (int i=0; i<Datos.length; i++){
                        datosFamilias.get(0).datosEditar.put(cabecera[i],Datos[i]);
                    }

                    datosPersonas.add(new ObjetoPersona(categoriasPersona));
                    for (int i=0; i<Datos.length; i++){
                        datosPersonas.get(datosPersonas.size()-1).datosEditar.put(cabecera[i],Datos[i]);
                    }

                DatosMiembrosFamiliares.add(datosMostrar);
                //Toast.makeText(this, Integer.toString(MiembrosFamiliares.size()), Toast.LENGTH_SHORT).show();

                }
            }

            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            //Toast.makeText(this, getText(R.string.ocurrio_error) + " 1", Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(this, datosFamilia, Toast.LENGTH_SHORT).show();
        return datosFamilia_string;
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // CENTRALIZACION
    // Subir archivos a servidor
    public void probar(View view){

        //EnviarDatosServidor enviarDatosServidor = new EnviarDatosServidor();
        //enviarDatosServidor.execute();
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        String DNIencuestador = admin.ObtenerDniActivado();
        GET_USER_ACCESS(direccion+"/prueba/consultar_usuario.php?documento=",DNIencuestador);
    }

    // Listao de fechas de las cuales se tienen archivos
    private ArrayList<String> ListadoFechas(){
        ArrayList<String> fechas = new ArrayList<>();

        listaNombresArchivos = new ArrayList<String>();
        listaRutasArchivos = new ArrayList<String>();

        File directorioactual = new File("/storage/emulated/0/RelevAr");
        File[] listaArchivos = directorioactual.listFiles();
        for(File archivo : listaArchivos){
            listaRutasArchivos.add(archivo.getPath());
        }

        Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);

        for(int i=0; i<listaRutasArchivos.size(); i++){
            File archivo = new File(listaRutasArchivos.get(i));
            if(archivo.isFile()){
                String[] aux=archivo.getName().split("-");
                String aux1=aux[1]+"-"+aux[2]+"-"+aux[3];
                String aux2 =aux1.replace(".csv", "");
                fechas.add(aux2);}
        }
        return fechas;
    }

    //Recupero los datos de los .csv
    private void DatosEnviar(ArrayList<String> fechas){

        datosPersonas.clear();
        datosFamilias.clear();

        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        String DNIencuestador = admin.ObtenerDniActivado();

        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();

        for (int j=0; j<fechas.size();j++){
        String NombreArchivo = "RelevAr-" + fechas.get(j) + ".csv";
        File dir = new File(nuevaCarpeta, NombreArchivo);

        String[] cabecera;
        String datosFamilia="";
        try {
            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            //
            cabecera = br.readLine().split(";");

            String myData;
            while ((myData=br.readLine())!=null){
                String[] Datos = myData.split(";");

                datosPersonas.add(new ObjetoPersona(categoriasPersona));
                datosFamilias.add(new ObjetoFamilia(familiaCabecera));

                datosFamilias.get(datosFamilias.size()-1).Valores.put("COORDENADAS", Datos[2]);
                datosPersonas.get(datosPersonas.size()-1).Valores.put("COORDENADAS", Datos[2]);

                datosFamilias.get(datosFamilias.size()-1).Valores.put("FECHA_REGISTRO", fechas.get(j));
                datosFamilias.get(datosFamilias.size()-1).Valores.put("DNI", DNIencuestador);

                datosFamilias.get(datosFamilias.size()-1).Valores.put("MENORES", Datos[5]);
                datosFamilias.get(datosFamilias.size()-1).Valores.put("MAYORES", Datos[6]);

                datosPersonas.get(datosPersonas.size()-1).Valores.put("EDAD", Datos[10]);
                datosPersonas.get(datosPersonas.size()-1).Valores.put("SEXO", Datos[11]);

                if(Datos.length>12){
                for (int i=13; i<Datos.length;i++){
                    if (EsDeFamilia(cabecera[i])){
                        if (!Datos[i].equals("")){
                            String aux=cabecera[i].replace(" ", "_");
                            aux=aux.replace("/","_");
                            aux=aux.replace("...","");
                            datosFamilias.get(datosFamilias.size()-1).Valores.put(aux, Datos[i]);
                        }
                    }
                    if (EsDePersona(cabecera[i])){
                        if (!Datos[i].equals("")){
                            String aux=cabecera[i].replace(" ", "_");
                            aux=aux.replace("¿","");
                            aux=aux.replace("?","");
                            datosPersonas.get(datosPersonas.size()-1).Valores.put(aux, Datos[i]);
                        }
                    }
                }
                }
            }

            //Toast.makeText(this, datosPersonas.get(0).Valores.get("ENFERMEDAD ASOCIADA AL EMBARAZO"), Toast.LENGTH_LONG).show();

            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }}

        ArrayList<HashMap<String,String>> DatosEnviarPersona = new ArrayList<>();
        for (int i=0; i<datosPersonas.size();i++){
            DatosEnviarPersona.add(datosPersonas.get(i).Valores);
        }

        ArrayList<HashMap<String,String>> DatosEnviarFamilia = new ArrayList<>();
        for (int i=0; i<datosFamilias.size();i++) {
            DatosEnviarFamilia.add(datosFamilias.get(i).Valores);
        }

        //POST_DATA("http://192.168.0.102:8080/prueba/cargar_datos.php", DatosEnviarPersona, DatosEnviarFamilia);
        POST_DATA(direccion+"/prueba/cargar_datos.php", DatosEnviarPersona, DatosEnviarFamilia);

        datosPersonas.clear();
        datosFamilias.clear();
    }

    // Corroborar si el dato corresponde a un dato de la familia
    private boolean EsDeFamilia(String valor){
        valor=valor.replace(" ", "_");
        valor=valor.replace("/","");
        valor=valor.replace("...","");
        boolean devolver = false;
        ObjetoFamilia aux = new ObjetoFamilia(familiaCabecera);
        for(int i=0; i<aux.DatosEnviar.size();i++){
            if(valor.equals(aux.DatosEnviar.get(i))){
                devolver = true;
            }
        }
        return devolver;
    }

    // Corroborar si el dato corresponde a un dato de la persona
    private boolean EsDePersona(String valor){
        valor=valor.replace(" ", "_");
        valor=valor.replace("¿","");
        valor=valor.replace("?","");
        boolean devolver = false;
        ObjetoPersona aux = new ObjetoPersona(categoriasPersona);
        for(int i=0; i<aux.DatosEnviar.size();i++){
            if(valor.equals(aux.DatosEnviar.get(i))){
                devolver = true;
            }
        }
        return devolver;
    }

    private void POST_DATA(String URL, final ArrayList<HashMap<String,String>> enviarPersonas, final ArrayList<HashMap<String,String>> enviarFamiliares){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getBaseContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String personas = new Gson().toJson(enviarPersonas);
                params.put("persona", personas);
                String familias = new Gson().toJson(enviarFamiliares);
                //Toast.makeText(getBaseContext(), familias, Toast.LENGTH_SHORT).show();
                params.put("familia", familias);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void GET_USER_ACCESS(String URL, String dni){
        fechas_disponibles = ListadoFechas();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL+dni, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject usuario = response.getJSONObject("usuario");
                            //Toast.makeText(getBaseContext(), usuario.toString(), Toast.LENGTH_LONG).show();
                            if(usuario.getString("NOMBRE").equals("NO REGISTRA")){
                                Toast.makeText(getBaseContext(), "SU USUARIO NO ESTA HABILITADO PARA CARGAR DATOS\nCONTACTESE CON LOS DESARROLLADORES", Toast.LENGTH_LONG).show();
                            }else{
                                try {
                                    ArrayList<String> fechas_cargadas = new ArrayList<>();
                                    JSONArray fechas = response.getJSONArray("fechas");
                                    for(int i=0; i<fechas.length(); i++){
                                        fechas_cargadas.add(fechas.getJSONArray(i).getString(0));
                                    }

                                    // necesito comparar con las fechas que tengo en el telefono

                                    for (int i=0; i<fechas_disponibles.size(); i++){
                                        for (int j=0; j<fechas_cargadas.size();j++){
                                            if(fechas_disponibles.get(i).equals(fechas_cargadas.get(j))){
                                                fechas_disponibles.remove(i);
                                            }
                                        }
                                    }
                                }catch (JSONException e){
                                    Toast.makeText(getBaseContext(), "no hay fechas", Toast.LENGTH_LONG).show();
                                }

                                // llamo para cargar los datos
                                if(fechas_disponibles.size()!=0){
                                    //Toast.makeText(getBaseContext(), Integer.toString(fec), Toast.LENGTH_LONG).show();
                                    DatosEnviar(fechas_disponibles);}
                                else{
                                    Toast.makeText(getBaseContext(), "YA ESTA TODO ACTUALIZADO", Toast.LENGTH_LONG).show();
                                }

                            }


                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Toast.makeText(getBaseContext(), "ERROR DE LECTURA", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private class EnviarDatosServidor extends AsyncTask<Void, Void, Void> {

        // Creo un progress dialog para mostrar mientras se ejecuta este codigo
        ProgressDialog pd;

        /*Antes de comenzar la ejecucion se inicia el progress dialog con los siguientes atributos*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MenuMapa.this);
            pd.setMessage("Enviando datos, aguarde");
            pd.setCancelable(false);
            pd.show();
        }

        /* Este es el codigo que se ejecuta en segundo plano mientras el usuario ve un cartel de
         * cargando datos*/
        @Override
        protected Void doInBackground(Void... voids) {
            SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
            String DNIencuestador = admin.ObtenerDniActivado();
            //GET_USER_ACCESS("http://192.168.0.102:8080/prueba/consultar_usuario.php?documento=",DNIencuestador);
            GET_USER_ACCESS(direccion+"/prueba/consultar_usuario.php?documento=",DNIencuestador);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Toast.makeText(getBaseContext(), "aca", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        /* Despues de la ejecucion del codigo en segundo plano debo detener el alert que me indica
         * que se estan cargando los datos*/
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null)
            {
                pd.dismiss();
            }
        }

    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // INFORMACION
    //Función para desplegar información de la app al clickear el logo de RelevAr
    public void mostrarInfoGeneral (View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_iniciar_terminar_recorrido, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView txt1 = view1.findViewById(R.id.CONSULTA);
        txt1.setText("RelevAr - Versión 2.1 \n \n" +
                "Acerca de RelevAr:\n" +
                "La aplicación está destinada al relevamiento georreferenciado de información sociosanitaria. La misma le permitirá recolectar datos tanto de personas, como de las viviendas y su entorno. Usando RelevAr usted podrá personalizar los registros activando los módulos que desee, además de guardar, editar y compartir los datos relevados con su equipo de trabajo y al mismo tiempo mantenerlos almacenados y accesibles desde la misma aplicación para consultas.\n" +
                "\n" +
                "Por cualquier problema o consulta sobre la aplicación póngase en contacto con el equipo de RelevAr.\n \n" +
                "Teléfonos de contacto\n" +
                "+54 9 3455 53-4522 \n" +
                "+54 9 3404 52-7870\n \n" +
                "Argentina - 2021");
        Button No= view1.findViewById(R.id.BTNNO);
        No.setVisibility(View.INVISIBLE);
        Button Si= view1.findViewById(R.id.BTNSI);
        Si.setText("Volver");
        Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    private void CrearUnificado() {

        ArrayList<String> fechas = ListadoFechas();

        datosPersonas.clear();
        datosFamilias.clear();

        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        admin.CrearTablaUnificados();
        SQLiteDatabase Bd1 = admin.getWritableDatabase();
        String DNIencuestador = admin.ObtenerDniActivado();

        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();

        File nuevaCarpetaUnificado = new File(getExternalStorageDirectory(), "RelevAr/RECORRIDOS");
        nuevaCarpetaUnificado.mkdirs();
        String NombreArchivoUnificado = "UNIFICADO.csv";
        File dirUnificado = new File(nuevaCarpetaUnificado, NombreArchivoUnificado);
        if(dirUnificado.exists()){
            dirUnificado.delete();
        }
        String cabUnificado = "CALLE;NUMERO;COORDENADAS;DNI;APELLIDO;NOMBRE;FECHA DE NACIMIENTO;SEXO;QR\n";
        String [] vectorCabeceraUnificado = cabUnificado.split(";");
        //el true es para que se agreguen los datos al final sin perder los datos anteriores
        String escribir = "";
        try {
            FileOutputStream fOutUnificado = new FileOutputStream(dirUnificado, true);
            OutputStreamWriter myOutWriterUnificado = new OutputStreamWriter(fOutUnificado);
            myOutWriterUnificado.append(cabUnificado);

        for (int j=0; j<fechas.size();j++) {
            if (!admin.ExisteFechaUnificados(fechas.get(j))){
                String NombreArchivo = "RelevAr-" + fechas.get(j) + ".csv";
            File dir = new File(nuevaCarpeta, NombreArchivo);
            String[] cabecera;
            //String datosFamilia="";
            try {
                FileInputStream fis = new FileInputStream(dir);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                //
                cabecera = br.readLine().split(";");

                String myData;
                while ((myData = br.readLine()) != null) {

                    String[] Datos = myData.split(";");

                    datosPersonas.add(new ObjetoPersona(categoriasPersona));
                    datosFamilias.add(new ObjetoFamilia(familiaCabecera));

                    datosFamilias.get(datosFamilias.size() - 1).Valores.put("CALLE", Datos[0]);
                    datosFamilias.get(datosFamilias.size() - 1).Valores.put("NUMERO", Datos[1]);
                    datosFamilias.get(datosFamilias.size() - 1).Valores.put("COORDENADAS", Datos[2]);

                    datosFamilias.get(datosFamilias.size() - 1).Valores.put("FECHA_REGISTRO", fechas.get(j));
                    datosFamilias.get(datosFamilias.size() - 1).Valores.put("DNI", DNIencuestador);

                    //datosFamilias.get(datosFamilias.size()-1).Valores.put("MENORES", Datos[5]);
                    //datosFamilias.get(datosFamilias.size()-1).Valores.put("MAYORES", Datos[6]);

                    datosPersonas.get(datosPersonas.size() - 1).Valores.put("APELLIDO", Datos[8]);
                    datosPersonas.get(datosPersonas.size() - 1).Valores.put("NOMBRE", Datos[9]);
                    datosPersonas.get(datosPersonas.size() - 1).Valores.put("FECHA DE NACIMIENTO", Datos[10]);
                    datosPersonas.get(datosPersonas.size() - 1).Valores.put("SEXO", Datos[11]);
                    datosPersonas.get(datosPersonas.size() - 1).Valores.put("QR", Datos[12]);
                    //Toast.makeText(this,datosPersonas.get(0).Valores.get("EDAD"), Toast.LENGTH_LONG).show();

                    if (Datos.length > 12) {
                        for (int i = 13; i < Datos.length; i++) {
                            if (EsDeFamilia(cabecera[i])) {
                                if (!Datos[i].equals("")) {
                                    String aux = cabecera[i].replace(" ", "_");
                                    aux = aux.replace("/", "_");
                                    aux = aux.replace("...", "");
                                    datosFamilias.get(datosFamilias.size() - 1).Valores.put(aux, Datos[i]);
                                }
                            }
                            if (EsDePersona(cabecera[i])) {
                                if (!Datos[i].equals("")) {
                                    String aux = cabecera[i].replace(" ", "_");
                                    aux = aux.replace("¿", "");
                                    aux = aux.replace("?", "");
                                    datosPersonas.get(datosPersonas.size() - 1).Valores.put(aux, Datos[i]);
                                }
                            }
                        }
                        // Datos completa de una persona
                        // Agregar al archivo unificado los datos necesarios, es necesario ya tenerlo abierto
                        // y con la cabecera que va a ser fija

                    }
                }

                br.close();
                in.close();
                fis.close();
            } catch (IOException e) {
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            //HASTA ACA LEI TODOS LOS DATOS DE UN ARCHIVO
            HashMap<String, String> unificado = new HashMap<>();
            for (int k = 0; k < datosFamilias.size(); k++) {
                unificado.putAll(datosPersonas.get(k).Valores);
                unificado.putAll(datosFamilias.get(k).Valores);
                for (int l = 0; l < vectorCabeceraUnificado.length - 1; l++) {
                    escribir += unificado.get(vectorCabeceraUnificado[l]) + ";";
                }
                escribir += unificado.get(vectorCabeceraUnificado[vectorCabeceraUnificado.length - 1].replace("\n", ""));
                Toast.makeText(this, vectorCabeceraUnificado[vectorCabeceraUnificado.length - 1], Toast.LENGTH_SHORT).show();
                escribir+="\n";

            }
                datosFamilias.clear();
                datosPersonas.clear();

                ContentValues registro = new ContentValues();
                registro.put("FECHA", fechas.get(j));
                Bd1.insert("UNIFICADOS", null, registro);
        }
        }
            myOutWriterUnificado.append(escribir);
            myOutWriterUnificado.close();
            fOutUnificado.close();
        } catch (IOException e) {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        compartirUnificado();
    }

    // FUNCION QUE INICIA EL ACTION PARA COMPARTIR (MAIL, DRIVE, WSSP U OTRAS OPCIONES)
    private void compartirUnificado(){
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr/RECORRIDOS");
        nuevaCarpeta.mkdirs();

        File dir = new File(nuevaCarpeta,"UNIFICADO.csv");
        Uri path = FileProvider.getUriForFile(this, "com.example.relevar", dir);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "UNIFICADO.csv");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Valores.");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        this.startActivity(Intent.createChooser(emailIntent, "SUBIR ARCHIVO"));
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // Desactivo el boton de volver atras
    @Override
    public void onBackPressed()
    {
        // Defino los contenedores
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("RelevAr");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check
        final LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        // Defino los parametros
        int TamañoLetra =20;

        // Telefono Celular
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final TextView descripcion = new TextView(getApplicationContext());
        descripcion.setText("Salir y cerrar sesion");
        descripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        descripcion.setTextSize(TamañoLetra);
        descripcion.setTextColor(Color.WHITE);
        descripcion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.setMinimumHeight(100);
        layout0.addView(descripcion);

        mainLayout.addView(layout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                encuestador.cerrarSesion();
                finish();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
