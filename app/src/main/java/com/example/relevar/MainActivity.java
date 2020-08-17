package com.example.relevar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.relevar.Recursos.Encuestador;
import com.google.android.gms.maps.model.LatLng;

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
import static com.example.relevar.R.drawable.fondo1;

public class MainActivity extends AppCompatActivity {
    // DEFINICION DE VARIABLES GLOBALES
    // Definicion de contantes que hacen al funcionamiento
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private int STORAGE_PERMISSION_CODE =1;

    // Definicion de String para contener informacion
    private String Latitud, Longitud, IDencuestador="";
    private Double Latitudenviar=0.0, Longitudenviar=0.0;

    // Encuestador
    Encuestador encuestador = new Encuestador();

    // Defino Arrays para almacenar datos
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<ArrayList<String>> InfoPersonas = new ArrayList<ArrayList<String>>();
    private ArrayList<ObjetoPersona> MiembrosFamiliares = new ArrayList<ObjetoPersona>();

    // Defino la lisa de personas
    private ListView lv1;

    // Defino un Adaptador para el ListView
    ArrayAdapter<String> adapter;

    int position, NumerosPersonas;

    LocationManager locationManager;
    LocationListener locationListener;
    ObjetoFamilia familia = new ObjetoFamilia();
    ConstraintLayout CLVivienda;
    TextView avanceVivienda;
    String auxLat;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); //evita la rotacion
        setContentView(R.layout.activity_main);

        // Seteo el titulo de la action bar del activity
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Familia");
        encuestador.setID((String) getIntent().getStringExtra("IDENCUESTADOR"));
        lv1 = (ListView) findViewById(R.id.list1);

        LatLong();

        // PARA EL AVANCE DE LOS FACTORES
        CLVivienda = (ConstraintLayout) findViewById(R.id.AVANCEVIVIENDA);
        avanceVivienda = (TextView) findViewById(R.id.COMPLETADOVIVIENDA);
    }

    //@Override
    protected void onStart() {
        //todo esto pa actualizr la listview
        super.onStart();
        ListeVer();
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // OBTENCION DE LOS DATOS DE LONGTUD Y LATITUD

    private void LatLong(){
    //public void LatLong(View view){
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Latitud=Double.toString(location.getLatitude());
                Longitud=Double.toString(location.getLongitude());
                Latitudenviar = location.getLatitude();
                Longitudenviar = location.getLongitude();
                //System.out.println(Latitud+Longitud+"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // AGREGAR, EDITAR O ELIMINAR UNA PERSONA Y VISUALIZARLAS
    public void NuevaPersona(View view){
        Intent Modif= new Intent (this, persona.class);
        startActivityForResult(Modif, 1);}

    private void ListeVer(){

        adapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, names);
        lv1.setAdapter(adapter);

        // Elegir entre eliminar y editar

        //realizar accion con el listview
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p1, long id) {
                //Elegir entre Eliminar y Editar
                position=p1;
                EliminarEditar();
            }
        });
    }

    private void EliminarEditar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_eliminar_editar, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView txtnombre = view1.findViewById(R.id.MOSTRARNOMBREAPELLIDO);
        txtnombre.setText(MiembrosFamiliares.get(position).Nombre+" "+MiembrosFamiliares.get(position).Apellido);

        TextView txtdni = view1.findViewById(R.id.MOSTRARDNI);
        txtdni.setText(MiembrosFamiliares.get(position).DNI);

        TextView txtfecha = view1.findViewById(R.id.MOSTRARFECHANACIMIENTO);
        txtfecha.setText( MiembrosFamiliares.get(position).Nacimiento);

        TextView riesgo = view1.findViewById(R.id.MOSTRARFACTORRIESGO);
        riesgo.setText(MiembrosFamiliares.get(position).FactoresDeRiesgo);

        TextView txtcelular = view1.findViewById(R.id.MOSTRARCELULAR);
        txtcelular.setText("CEL: "+  MiembrosFamiliares.get(position).Celular);

        TextView txtfijo = view1.findViewById(R.id.MOSTRARFIJO);
        txtfijo.setText("FIJO: "+  MiembrosFamiliares.get(position).Fijo);

        TextView txtnombrecontacto = view1.findViewById(R.id.MOSTRARPERSONACONACTO);
        txtnombrecontacto.setText(MiembrosFamiliares.get(position).NombreContacto);

        TextView telcontacto = view1.findViewById(R.id.MOSTRARTELEFONOCONTCTO);
        telcontacto.setText(MiembrosFamiliares.get(position).TelefonoContacto);

        Button editar = view1.findViewById(R.id.EDITAR);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Modif.putExtra("NOMBRECONTACTO" , MiembrosFamiliares.get(position).NombreContacto);
                Modif.putExtra("TELEFONOCONTACTO" , MiembrosFamiliares.get(position).TelefonoContacto);
                Modif.putExtra("PARENTEZCOCONTACTO" , MiembrosFamiliares.get(position).ParentezcoContacto);
                Modif.putExtra("OCUPACION" , MiembrosFamiliares.get(position).Ocupacion);
                Modif.putExtra("EDUCACION" , MiembrosFamiliares.get(position).Educacion);
                setResult(RESULT_OK, Modif);
                MiembrosFamiliares.remove(position);
                names.remove(position);

                ListeVer();
                startActivityForResult(Modif, 1);
                dialog.dismiss();
            }
        });

        Button cancelar = view1.findViewById(R.id.CANCELAREDICION);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button eliminar = view1.findViewById(R.id.ELIMINAR);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MiembrosFamiliares.remove(position);
                names.remove(position);
                ListeVer();
                dialog.dismiss();
            }
        });
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
                Persona.NombreContacto = data.getStringExtra("NOMBRECONTACTO");
                Persona.TelefonoContacto = data.getStringExtra("TELEFONOCONTACTO");
                Persona.ParentezcoContacto = data.getStringExtra("PARENTEZCOCONTACTO");
                Persona.Ocupacion = data.getStringExtra("OCUPACION");
                Persona.Educacion = data.getStringExtra("EDUCACION");

                //Agrego la persona como miembro de la familia
                MiembrosFamiliares.add(Persona);

                //makeText(this, Persona.Nombre, LENGTH_SHORT).show();
                names.add("DNI: "+Persona.DNI+", "+Persona.Apellido+" "+Persona.Nombre);}

                ListeVer();
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SERVICIOS BASICOS
    public void ServiciosBasicos(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_servicios_basicos, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // AGUA
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner Agua = view1.findViewById(R.id.SPAGUA);
        ArrayList<String> OpcAgua = new ArrayList<>();

        OpcAgua.add("");
        OpcAgua.add("POR CAÑERIA DENTRO DE LA VIVIENDA");
        OpcAgua.add("FUERA DE LA VIVIENDA PERO DENTRO DEL TERRENO");
        OpcAgua.add("FUERA DEL TERRENO");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterAgua = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcAgua);
        Agua.setAdapter(comboAdapterAgua);

        // Recordar las opciones seleccionadas
        if(familia.Agua.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcAgua.size(); i++){
                if(OpcAgua.get(i).equals(familia.Agua)){
                    indice=i;
                }
            }
            Agua.setSelection(indice);
        }

        // PROVEDURIA DE AGUA
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner AguaOrigen = view1.findViewById(R.id.SPAGUAORIGEN);
        ArrayList<String> OpcAguaOrigen = new ArrayList<>();

        OpcAguaOrigen.add("");
        OpcAguaOrigen.add("RED PUBLICA");
        OpcAguaOrigen.add("CONEXION SUPERFICIAL ENTRE HOGARES");
        OpcAguaOrigen.add("PERFORACION CON BOMBA A MOTOR");
        OpcAguaOrigen.add("PERFORACION CON BOMBA MANUAL");
        OpcAguaOrigen.add("POZO SIN BOMBA");
        OpcAguaOrigen.add("OTRO");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterAguaOrigen = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcAguaOrigen);
        AguaOrigen.setAdapter(comboAdapterAguaOrigen);

        // Recordar las opciones seleccionadas
        if(familia.AguaOrigen.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcAguaOrigen.size(); i++){
                if(OpcAguaOrigen.get(i).equals(familia.AguaOrigen)){
                    indice=i;
                }
            }
            AguaOrigen.setSelection(indice);
        }

    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // VIVIENDA
    public void Vivienda(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_vivienda, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // TIPO VIVIENDA
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner TipoVivienda = view1.findViewById(R.id.SPTIPODEVIVIENDA);
        ArrayList<String> OpcTipoVivienda = new ArrayList<>();

        OpcTipoVivienda.add("");
        OpcTipoVivienda.add("CASA");
        OpcTipoVivienda.add("CASILLA");
        OpcTipoVivienda.add("RANCHO");
        OpcTipoVivienda.add("DEPARTAMENTO");
        OpcTipoVivienda.add("PENSION");
        OpcTipoVivienda.add("LOCAL NO CONSTRUIDO PARA VIVIENDA");
        OpcTipoVivienda.add("VIVIENDA MOVIL");
        OpcTipoVivienda.add("PERSONA VIVIENDO EN LA CALLE");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcTipoVivienda);
        TipoVivienda.setAdapter(comboAdapter);

        // Recordar las opciones seleccionadas
        if(familia.TipoVivienda.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcTipoVivienda.size(); i++){
                if(OpcTipoVivienda.get(i).equals(familia.TipoVivienda)){
                    indice=i;
                }
            }
            TipoVivienda.setSelection(indice);
        }

        // LA VIVIENDA ES
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner ViviendaEs = view1.findViewById(R.id.SPDUEÑOVIVIENDA);
        ArrayList<String> OpcDueñoVivienda = new ArrayList<>();

        OpcDueñoVivienda.add("");
        OpcDueñoVivienda.add("PROPIA");
        OpcDueñoVivienda.add("ALQUILADA");
        OpcDueñoVivienda.add("CEDIDA POR TRABAJO");
        OpcDueñoVivienda.add("PRESTADA");
        OpcDueñoVivienda.add("OTRA");


        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterDueño = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcDueñoVivienda);
        ViviendaEs.setAdapter(comboAdapterDueño);

        // Recordar las opciones seleccionadas
        if(familia.DueñoVivienda.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcDueñoVivienda.size(); i++){
                if(OpcDueñoVivienda.get(i).equals(familia.DueñoVivienda)){
                    indice=i;
                }
            }
            ViviendaEs.setSelection(indice);
        }

        // COCINA
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner Cocinar = view1.findViewById(R.id.SPLUGARCOCINAR);
        ArrayList<String> OpcCocinar = new ArrayList<>();

        OpcCocinar.add("");
        OpcCocinar.add("CON INSTALACION DE AGUA Y DESAGUE");
        OpcCocinar.add("CON INSTALCION DE AGUA SIN DESAGUE");
        OpcCocinar.add("SIN INSTALACION DE AGUA");
        OpcCocinar.add("NO TIENE LUGAR O CUARTO PARA COCINAR");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterCocinar = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcCocinar);
        Cocinar.setAdapter(comboAdapterCocinar);

        // Recordar las opciones seleccionadas
        if(familia.LugarCocinar.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcCocinar.size(); i++){
                if(OpcCocinar.get(i).equals(familia.LugarCocinar)){
                    indice=i;
                }
            }
            Cocinar.setSelection(indice);
        }

        // USA PARA COCINAR
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner UsaCocinar = view1.findViewById(R.id.SPUSAPARACOCINAR);
        ArrayList<String> OpcUsaCocinar = new ArrayList<>();

        OpcUsaCocinar.add("");
        OpcUsaCocinar.add("GAS DE RED");
        OpcUsaCocinar.add("GAS EN GARRAFA, EN TUBO O A GRANEL");
        OpcUsaCocinar.add("LEÑA O CARBON");
        OpcUsaCocinar.add("ELECTRICIDAD");
        OpcUsaCocinar.add("OTRO");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterUsaCocinar = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcUsaCocinar);
        UsaCocinar.setAdapter(comboAdapterUsaCocinar);

        // Recordar las opciones seleccionadas
        if(familia.UsaParaCocinar.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcUsaCocinar.size(); i++){
                if(OpcUsaCocinar.get(i).equals(familia.UsaParaCocinar)){
                    indice=i;
                }
            }
            UsaCocinar.setSelection(indice);
        }

        // PARADES
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner Paredes = view1.findViewById(R.id.SPPAREDES);
        ArrayList<String> OpcParedes = new ArrayList<>();

        OpcParedes.add("");
        OpcParedes.add("LADRILLO, PIEDRA, BLOQUE, HORMIGON");
        OpcParedes.add("CHAPA DE METAL O FIBROCEMENTO");
        OpcParedes.add("ADOBE");
        OpcParedes.add("MADERA");
        OpcParedes.add("CHORIZO, CARTON, PALMA, PAJA SOLA O MATERIL DE DESECHO");
        OpcParedes.add("OTRO");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterParedes = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcParedes);
        Paredes.setAdapter(comboAdapterParedes);

        // Recordar las opciones seleccionadas
        if(familia.Paredes.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcParedes.size(); i++){
                if(OpcParedes.get(i).equals(familia.Paredes)){
                    indice=i;
                }
            }
            Paredes.setSelection(indice);
        }

        // TECHO
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner Techo = view1.findViewById(R.id.SPTECHO);
        ArrayList<String> OpcTecho = new ArrayList<>();

        OpcTecho.add("");
        OpcTecho.add("BALDOZA, MEMBRANA, PINTURA ASFALTICA, PIZARRA O TEJA");
        OpcTecho.add("LOZA O CARPETA A LA VISTA (SIN CUBIERTA)");
        OpcTecho.add("CHAPA DE METAL");
        OpcTecho.add("BOLSAS");
        OpcTecho.add("CHAPA DE CARTON, CAÑA, PALMA, TABLA CON BARRO, PAJA CON BARRO O PAJA SOLA");
        OpcTecho.add("OTRO");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterTecho = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcTecho);
        Techo.setAdapter(comboAdapterTecho);

        // Recordar las opciones seleccionadas
        if(familia.Techo.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcTecho.size(); i++){
                if(OpcTecho.get(i).equals(familia.Techo)){
                    indice=i;
                }
            }
            Techo.setSelection(indice);
        }

        // PIDO
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner Piso = view1.findViewById(R.id.SPPISOS);
        ArrayList<String> OpcPiso = new ArrayList<>();

        OpcPiso.add("");
        OpcPiso.add("CERAMICA, BALDOZA, MOSAICO, MADERA O CEMENTO ALISADO");
        OpcPiso.add("CARPETA, COMTRAPISO O LADRILLO FIJO");
        OpcPiso.add("TIERRA O LADRILLO SUELTO");
        OpcPiso.add("OTRO");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterPiso = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcPiso);
        Piso.setAdapter(comboAdapterPiso);

        // Recordar las opciones seleccionadas
        if(familia.Pisos.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcPiso.size(); i++){
                if(OpcPiso.get(i).equals(familia.Pisos)){
                    indice=i;
                }
            }
            Piso.setSelection(indice);
        }

        final EditText CantidadHabitaciones = view1.findViewById(R.id.CANTPIEZAS);
        CantidadHabitaciones.setText(familia.CantidadPiezas);

        final RadioButton SiRevoque = view1.findViewById(R.id.SIREVOQUE);
        final RadioButton NoRevoque = view1.findViewById(R.id.NOREVOQUE);
        if(familia.Revoque=="SI"){SiRevoque.setChecked(true);}
        if(familia.Revoque=="NO"){NoRevoque.setChecked(true);}

        final RadioButton SiCielorraso = view1.findViewById(R.id.SICIELORRAZO);
        final RadioButton NoCielorraso = view1.findViewById(R.id.NOCIELORRAZO);
        if(familia.Cielorraso=="SI"){SiCielorraso.setChecked(true);}
        if(familia.Cielorraso=="NO"){NoCielorraso.setChecked(true);}

        // GUARDAR
        final Button guardar = view1.findViewById(R.id.GUARADRVIVIENDA);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familia.TipoVivienda = TipoVivienda.getSelectedItem().toString();
                familia.DueñoVivienda = ViviendaEs.getSelectedItem().toString();
                familia.CantidadPiezas = CantidadHabitaciones.getText().toString();
                familia.LugarCocinar = Cocinar.getSelectedItem().toString();
                familia.UsaParaCocinar = UsaCocinar.getSelectedItem().toString();
                familia.Paredes = Paredes.getSelectedItem().toString();
                if(SiRevoque.isChecked()){familia.Revoque="SI";}
                if(NoRevoque.isChecked()){familia.Revoque="NO";}
                familia.Pisos = Piso.getSelectedItem().toString();
                familia.Techo = Techo.getSelectedItem().toString();
                if(SiCielorraso.isChecked()){familia.Cielorraso="SI";}
                if(NoCielorraso.isChecked()){familia.Cielorraso="NO";}
                dialog.dismiss();
                ColorAvanceVivienda();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARVIVIENDA);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceVivienda(){
        // Cambio los colores de avance
        float avance = 0;
        if (familia.TipoVivienda.length()!=0){
            avance+=1;
        }
        if (familia.DueñoVivienda.length()!=0){
            avance+=1;
        }
        if (familia.CantidadPiezas.length()!=0){
            avance+=1;
        }
        if (familia.LugarCocinar.length()!=0){
            avance+=1;
        }
        if (familia.UsaParaCocinar.length()!=0){
            avance+=1;
        }
        if (familia.Paredes.length()!=0){
            avance+=1;
        }
        if (familia.Revoque.length()!=0){
            avance+=1;
        }
        if (familia.Pisos.length()!=0){
            avance+=1;
        }
        if (familia.Techo.length()!=0){
            avance+=1;
        }
        if (familia.Cielorraso.length()!=0){
            avance+=1;
        }
        if(avance>0 && avance<10){
            CLVivienda.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/10)*100);
            //Toast.makeText(getApplicationContext(), Double.toString(porcentaje), Toast.LENGTH_SHORT).show();
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceVivienda.setText(aux);
            //AvContacto.setText(aux);
            //AvContacto.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==10){
            CLVivienda.setBackgroundResource(R.drawable.verde);
            avanceVivienda.setText(getString(R.string.completado)+" 100%");
            //Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvContacto.setText("3/3");
            //AvContacto.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
        if(avance==0){
            CLVivienda.setBackgroundResource(R.drawable.rojo);
            avanceVivienda.setText(getString(R.string.completado)+" 00%");
            //Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvContacto.setText("3/3");
            //AvContacto.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // GUARDAR GRUPO FAMILIAR
    public void Guardar(View view){

        //final String CantidadGrupoFamiliar=grupofamiliar.getText().toString();
        // Inicio la obtencion de datos de ubicacion del GPS
        //LatLong();
        if(MiembrosFamiliares.size()!=0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater Inflater = getLayoutInflater();
                View view_alert = Inflater.inflate(R.layout.alert_guardar_familia, null);
                builder.setView(view_alert);
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button cancelar = view_alert.findViewById(R.id.CANCELAR1);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final EditText calle = view_alert.findViewById(R.id.CALLE);
                final EditText numero = view_alert.findViewById(R.id.NUMERO);
                final EditText cantidadintegrantes = view_alert.findViewById(R.id.CANTIDADMIEMBROSFAMILIARES);
                NumerosPersonas = MiembrosFamiliares.size();
                cantidadintegrantes.setText(Integer.toString(NumerosPersonas));

                ImageView mas = view_alert.findViewById(R.id.MAS);
                mas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NumerosPersonas+=1;
                        cantidadintegrantes.setText(Integer.toString(NumerosPersonas));
                    }
                });

                ImageView menos = view_alert.findViewById(R.id.MENOS);
                menos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (NumerosPersonas!=MiembrosFamiliares.size()){
                        NumerosPersonas-=1;
                        cantidadintegrantes.setText(Integer.toString(NumerosPersonas));}
                    }
                });

                Button guardar = view_alert.findViewById(R.id.GUARDARFAMILIA);
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Latitudenviar!=0.0){
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
                                guardar = calle.getText().toString() + ";" + numero.getText().toString() + ";" + Latitud +" "+ Longitud + ";" + Integer.toString(NumerosPersonas);
                                guardar+=";"+MiembrosFamiliares.get(x).FormatoGuardar();
                                guardar+=";"+familia.FormatoGuardar();
                                guardar += ";"+encuestador.getID()+"\n";
                                myOutWriter.append(guardar);
                            }

                            // ENVIO LA UBICACION PARA AGREGAR UN MARCADOR
                            Intent intent= new Intent (getBaseContext(), MenuPrincipal.class);
                            LatLng position = new LatLng(Latitudenviar,Longitudenviar); // Boa Vista
                            Bundle args = new Bundle();
                            args.putParcelable("from_position", position);
                            //intent.putExtra("MARCADOR", position);
                            intent.putExtra("bundle", args);
                            setResult(RESULT_OK, intent);

                            MiembrosFamiliares.clear();
                            lv1.setAdapter(null);
                            adapter.clear();
                            myOutWriter.close();
                            fOut.close();
                            locationManager.removeUpdates(locationListener);
                            dialog.dismiss();
                            finish();
                    } catch (IOException e) {
                        e.printStackTrace();}}
                        else{makeText(getBaseContext(), "ESPERE UNOS SEGUNDOS E INTENTE DE NUEVO, EL GPS SE ESTA UBICANDO", LENGTH_SHORT).show();}
                }});
    } else {makeText(getBaseContext(), "NO HAY PERSONAS CARGADAS", LENGTH_SHORT).show();}
    }

    // Boton atras
    @Override
    public void onBackPressed(){
        if(MiembrosFamiliares.size()==0){
            locationManager.removeUpdates(locationListener);
            finish();
        }
        //thats it
    }
}

