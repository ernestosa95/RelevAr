package com.example.relevar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.relevar.Recursos.Encuestador;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MenuPrincipal extends AppCompatActivity implements OnMapReadyCallback{
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    // Inicio de toma de ubicacion
    boolean devolver = false;
    //
    EditText Nencuestador;

    // Creo al encuestador
    Encuestador encuestador= new Encuestador();

    // Mapa
    private MapView mapView;
    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        // Eliminar el action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        // Mapa
        mapView = (MapView) findViewById(R.id.MAPA);
        Bundle mapBundle = null;

        if(savedInstanceState!=null){
            mapBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);

        }
        mapView.onCreate(mapBundle);
        mapView.getMapAsync(this);
        // Inicio de la App
        Empezar();
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // PARA EL MAPA

    @Override
    public void onMapReady(GoogleMap map) {
    map.addMarker(new MarkerOptions().position(new LatLng(-60, -30)).title("aca toy"));
    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    map.setMyLocationEnabled(true);
    map.getUiSettings().setMapToolbarEnabled(false);
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

    // FUNCION DE INICIO, muestra el logo y info
    private void Empezar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view = Inflater.inflate(R.layout.alert_inicio, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Enlazo el boton de empezar con una función
        Button empezar = view.findViewById(R.id.EMPEZAR);

        // Comenzar con la carga de datos
        empezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Encuestador();
            }
        });
    }

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
        Button nuevo = view.findViewById(R.id.NUEVOENCUESTADOR);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            NuevoEncuestador();
            dialog.dismiss();
            }
        });

        // Ingresar con encuestador existente
        Button ingresar = view.findViewById(R.id.BTNCANCELANE);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            encuestador.setID(encuestadores.getSelectedItem().toString());
            dialog.dismiss();
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
        Button nuevo = view.findViewById(R.id.BTNCANCELANE);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ingreso datos del encuestador
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getBaseContext(), "datos", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();
                String insert ="INSERT INTO ENCUESTADOR (ID) VALUES ('"+Nencuestador.getText().toString()+"')";
                db.execSQL(insert);
                db.close();

                // Inicializo el encuestador
                encuestador.setID(Nencuestador.getText().toString());
                dialog.dismiss();
            }
        });

        Button cancelar = view.findViewById(R.id.BTNCANCELANE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Encuestador();
                dialog.dismiss();
            }
        });
    }

//--------------------------------------------------------------------------------------------------
    // Desactivo el boton de volver atras
    @Override
    public void onBackPressed()
    {
        //thats it
    }// Desactivo el boton de volver atras

}
