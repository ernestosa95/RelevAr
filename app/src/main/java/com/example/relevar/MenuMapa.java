package com.example.relevar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MenuMapa extends AppCompatActivity implements OnMapReadyCallback {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int REQUEST_CODE_POSITION = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private ArrayAdapter adaptador;
    ListView lv1;
    // Inicio de toma de ubicacion
    boolean devolver = false;
    //
    EditText Nencuestador;
    Button PararServicio, BtnCompartir;

    // Creo al encuestador
    Encuestador encuestador = new Encuestador();

    // Mapa
    private MapView mapView;
    private GoogleMap map;
    LatLng latLng;
    ArrayList<LatLng> recorrido = new ArrayList<>();
    ArrayList<LatLng> marcadores = new ArrayList<>();
    // String
    private Double Latitud, Longitud;
    Polyline ruta;
    LocationManager locationManagerEncuestador;
    LocationListener locationListenerEncuestador;
    private BroadcastReceiver broadcastReceiver;
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

        //
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        encuestador.setID(admin.ObtenerActivado());
        // Mapa
        mapView = (MapView) findViewById(R.id.MAPA);
        Bundle mapBundle = null;
        txt = (TextView) findViewById(R.id.COMPLETADOFACTORES);
        if (savedInstanceState != null) {
            mapBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapBundle);
        mapView.getMapAsync(this);

        latLng = new LatLng(-60, -30);

        // Inicio de la App
        //Encuestador();
        PararServicio = (Button) findViewById(R.id.TERMINAR);
        boolean estado = isMyServiceRunning(ServicioGPS.class);
        if (estado == true) {
            PararServicio.setText(getString(R.string.terminar_recorrido));
        } else {
            PararServicio.setText(getString(R.string.iniciar_recorrido));
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        //directorioraiz = Environment.getExternalStorageDirectory().getPath();
        directorioraiz = "/storage/emulated/0/RelevAr";
        BtnCompartir = (Button) findViewById(R.id.COMPARTIR);
        BtnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(directorioraiz);
            }
        });
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            recorrido = intent.getParcelableArrayListExtra("RECORRIDO");
            Log.d("receiver", Integer.toString(recorrido.size()));
        }
    };
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // COMPARTIR
    @SuppressLint("WrongConstant")
    private void buscar(String RutaDirectorio){

    Toast.makeText(this, RutaDirectorio, 6000).show();
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
                compartir(ubicacion);
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

    private void compartir(String dir){
        final String fileUriString = dir;
        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse( fileUriString ) ) ;
        sharingIntent.setType("text/csv");
        startActivity(Intent.createChooser(sharingIntent, "share file with"));
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // NUEVA FAMILIA
    public void NuevaFamilia(View view) {
        boolean estado = isMyServiceRunning(ServicioGPS.class);
        if (estado == false) {
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
                    PararServicio.setText(getString(R.string.terminar_recorrido));
                    dialog.dismiss();

                    Intent Modif = new Intent(getBaseContext(), Familia.class);
                    Modif.putExtra("IDENCUESTADOR", encuestador.getID());
                    startActivityForResult(Modif, 1);
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
            Intent Modif = new Intent(getBaseContext(), Familia.class);
            Modif.putExtra("IDENCUESTADOR", encuestador.getID());
            startActivityForResult(Modif, 1);
        }
    }

    // TERMINAR RECORRIDO
    public void TerminarRecorrido(View view) {
        boolean estado = isMyServiceRunning(ServicioGPS.class);
        //Intent intent = new Intent(getBaseContext(), ServicioGPS.class);
        //Toast.makeText(this, Boolean.toString(EstadoServicio()), Toast.LENGTH_SHORT).show();
        if (estado == true) {
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getParcelableExtra("bundle");
                LatLng position = bundle.getParcelable("from_position");
                latlngs.add(position);
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // Desactivo el boton de volver atras
    @Override
    public void onBackPressed()
    {
        //thats it
    }// Desactivo el boton de volver atras

}
