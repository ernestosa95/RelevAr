package com.example.relevar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.relevar.MySQL.SQLitePpal;
import com.example.relevar.Recursos.Encuestador;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import static android.os.Environment.getExternalStorageDirectory;
import static android.widget.Toast.*;

public class Familia extends AppCompatActivity {
    // CABECERA EN LA FAMILIA
    ArrayList<String> familiaCabecera = new ArrayList<>();
    ArrayList<String> categoriasPersona = new ArrayList<>();

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
    ObjetoFamilia familia;
    ConstraintLayout BtnInspeccionExterior, BtnDengue;
    ConstraintLayout BtnServiciosBasicos, BtnVivienda;
    ConstraintLayout CLVivienda, CLServiciosBasicos, CLExteriorVivienda, CLDengue;
    TextView avanceVivienda, avanceServiciosBasicos, avanceExteriorVivienda, avanceDengue;
    LinearLayout BotonesFamilia;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); //evita la rotacion
        setContentView(R.layout.activity_main);

        // Eliminar el action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        // Evitar la rotacion
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }

        // Seteo el titulo de la action bar del activity
        //ActionBar actionbar = getSupportActionBar();
        //actionbar.setTitle("Familia");
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        encuestador.setID(admin.ObtenerActivado());
        lv1 = (ListView) findViewById(R.id.list1);

        LatLong();

        // Defino el linearlayout que contiene los botones
        BotonesFamilia = findViewById(R.id.BtnsFamilia);

        CrearBotonera();

        // Lleno y paso el array de la cabecera
        familiaCabecera.add(getString(R.string.tipo_de_vivienda));
        familiaCabecera.add(getString(R.string.dueño_vivienda));
        familiaCabecera.add(getString(R.string.cantidad_piezas));
        familiaCabecera.add(getString(R.string.lugar_cocinar));
        familiaCabecera.add(getString(R.string.usa_para_cocinar));
        familiaCabecera.add(getString(R.string.paredes));
        familiaCabecera.add(getString(R.string.revoque));
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
        familia=new ObjetoFamilia(familiaCabecera);

        //
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
    }

    private void CrearBotonera(){
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);

        //
        BtnInspeccionExterior = (ConstraintLayout) findViewById(R.id.BTNEXTERIOR);
        CLExteriorVivienda = (ConstraintLayout) findViewById(R.id.AVANCEXTERIOR);
        avanceExteriorVivienda = (TextView) findViewById(R.id.COMPLETADOEXTERIOR);
        if(!admin.EstadoBoton("INSPECCION EXTERIOR")){
            BtnInspeccionExterior.setVisibility(View.GONE);
        }
        else{
            BtnInspeccionExterior.setVisibility(View.VISIBLE);
        }

        //
        BtnServiciosBasicos = (ConstraintLayout) findViewById(R.id.BTNSERVICIOSBASICOS);
        CLServiciosBasicos = (ConstraintLayout) findViewById(R.id.AVANCESERVICIOSBASICOS);
        avanceServiciosBasicos = (TextView) findViewById(R.id.COMPLETADOSERVICIOS);
        if(!admin.EstadoBoton("SERVICIOS BASICOS")){
            BtnServiciosBasicos.setVisibility(View.GONE);
        }
        else{
            BtnServiciosBasicos.setVisibility(View.VISIBLE);
        }

        //
        BtnVivienda = (ConstraintLayout) findViewById(R.id.BTNVIVIENDA);
        CLVivienda = (ConstraintLayout) findViewById(R.id.AVANCEVIVIENDA);
        avanceVivienda = (TextView) findViewById(R.id.COMPLETADOVIVIENDA);
        if(!admin.EstadoBoton("VIVIENDA")){
            BtnVivienda.setVisibility(View.GONE);
        }
        else {
            BtnVivienda.setVisibility(View.VISIBLE);
        }

        //
        BtnDengue = (ConstraintLayout) findViewById(R.id.BTNDENGUE);
        CLDengue = (ConstraintLayout) findViewById(R.id.AVANCEDENGUE);
        avanceDengue = (TextView) findViewById(R.id.COMPLETADODENGUE);
        if(!admin.EstadoBoton("DENGUE")){
            BtnDengue.setVisibility(View.GONE);
        }
        else {
            BtnDengue.setVisibility(View.VISIBLE);
        }
        admin.close();
    }

    public void Botones(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_listado_botones, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);

        final Switch inspeccionExterior = view1.findViewById(R.id.SWITCHINSPECCIONEXTERIOR);
        if(admin.EstadoBoton("INSPECCION EXTERIOR")){
            inspeccionExterior.setChecked(true);
        }

        inspeccionExterior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint({"WrongConstant", "ResourceType"})
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(inspeccionExterior.getText().toString());
                    BtnInspeccionExterior.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(inspeccionExterior.getText().toString());
                    BtnInspeccionExterior.setVisibility(View.GONE);
                }
            }

        });

        final Switch serviciosBasicos = view1.findViewById(R.id.SWITCHSERVICIOSBASICOS);
        if (admin.EstadoBoton("SERVICIOS BASICOS")){
            serviciosBasicos.setChecked(true);
        }

        serviciosBasicos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(serviciosBasicos.getText().toString());
                    BtnServiciosBasicos.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(serviciosBasicos.getText().toString());
                    BtnServiciosBasicos.setVisibility(View.GONE);
                }
            }
        });

        final Switch vivienda = view1.findViewById(R.id.SWITCHVIVIENDA);
        if(admin.EstadoBoton("VIVIENDA")){
            vivienda.setChecked(true);
        }
        vivienda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(vivienda.getText().toString());
                    BtnVivienda.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(vivienda.getText().toString());
                    BtnVivienda.setVisibility(View.GONE);
                }
            }
        });

        final Switch dengue = view1.findViewById(R.id.SWITCHDENGUE);
        if(admin.EstadoBoton("DENGUE")){
            dengue.setChecked(true);
        }
        dengue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(dengue.getText().toString());
                    BtnDengue.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(dengue.getText().toString());
                    BtnDengue.setVisibility(View.GONE);
                }
            }
        });

        admin.close();
        final Button listo = view1.findViewById(R.id.LISTOBOTON);
        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //recreate();
            }
        });

        final Switch educacion = view1.findViewById(R.id.SWITCHEDUCACION);
        educacion.setVisibility(View.GONE);
        final Switch ocupacion = view1.findViewById(R.id.SWITCHOCUPACION);
        ocupacion.setVisibility(View.GONE);
        final Switch contacto = view1.findViewById(R.id.SWITCHCONTACTO);
        contacto.setVisibility(View.GONE);
        final Switch efector = view1.findViewById(R.id.SWITCHEFECTOR);
        efector.setVisibility(View.GONE);
        final Switch observaciones = view1.findViewById(R.id.SWITCHOBSERVACIONES);
        observaciones.setVisibility(View.GONE);

        final Switch factores_riesgo = view1.findViewById(R.id.SWITCHFACTORESRIESGO);
        factores_riesgo.setVisibility(View.GONE);
        final Switch discapacidad = view1.findViewById(R.id.SWITCHDISCAPACIDAD);
        discapacidad.setVisibility(View.GONE);
        final Switch embarazo = view1.findViewById(R.id.SWITCHEMBARAZO);
        embarazo.setVisibility(View.GONE);
        final Switch vitamina_d = view1.findViewById(R.id.SWITCHVITAMINAD);
        vitamina_d.setVisibility(View.GONE);
        final Switch encabezadadescronicas = view1.findViewById(R.id.SWITCHENFERMEDADCRONICA);
        encabezadadescronicas.setVisibility(View.GONE);

        final Switch acompañamiento = view1.findViewById(R.id.SWITCHACOMPAÑAMIENTO);
        acompañamiento.setVisibility(View.GONE);
        final Switch transtornos_niños = view1.findViewById(R.id.SWITCHTRANSTORNOSENNIÑOS);
        transtornos_niños.setVisibility(View.GONE);
        final Switch transtornos_mentales = view1.findViewById(R.id.SWITCHTRANSTORNOSMENTALES);
        transtornos_mentales.setVisibility(View.GONE);
        final Switch adicciones = view1.findViewById(R.id.SWITCHADICCIONES);
        adicciones.setVisibility(View.GONE);
        final Switch violencia = view1.findViewById(R.id.SWITCHVIOLENCIA);
        violencia.setVisibility(View.GONE);
        final Switch ocio = view1.findViewById(R.id.SWITCHOCIO);
        ocio.setVisibility(View.GONE);

    }

    //@Override
    protected void onStart() {
        //todo esto pa actualizr la listview
        super.onStart();
        ListeVer();
        CrearBotonera();
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
        Intent Modif= new Intent (this, Persona.class);
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
                Intent Modif= new Intent (getBaseContext(), Persona.class);
                /*Modif.putExtra("NOMBRE" , MiembrosFamiliares.get(position).Nombre);
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
                Modif.putExtra("EDUCACION" , MiembrosFamiliares.get(position).Educacion);*/

                Bundle bundle = new Bundle();
                bundle.putSerializable("PERSONAEDITAR", MiembrosFamiliares.get(position));
                Modif.putExtras(bundle);

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
            if(resultCode == RESULT_OK){
                ObjetoPersona Persona=new ObjetoPersona(categoriasPersona);
                //ArrayList<String> CamposPersona = new ArrayList<String>();

                /*Persona.DNI = data.getStringExtra("DNI");
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
                Persona.Educacion = data.getStringExtra("EDUCACION");*/

                Bundle bundle = data.getExtras();
                if(bundle!=null){
                    Persona = (ObjetoPersona) bundle.getSerializable("PERSONA");
                }


                //Agrego la persona como miembro de la familia
                MiembrosFamiliares.add(Persona);
                //makeText(this, Persona.Nombre, LENGTH_SHORT).show();
                names.add("DNI: "+Persona.DNI+", "+Persona.Apellido+" "+Persona.Nombre);}

            if(resultCode == 3){
                ObjetoFamilia aux = new ObjetoFamilia(null);
                Bundle bundle = data.getExtras();
                if(bundle!=null){
                    aux = (ObjetoFamilia) bundle.getSerializable("DENGUE");
                }
                //Toast.makeText(this, aux.RecipientesPlasticos, Toast.LENGTH_SHORT).show();
                familia.SituacionVivienda = aux.SituacionVivienda;
                familia.TipoTrabajo = aux.TipoTrabajo;
                familia.ElementosDesuso = aux.ElementosDesuso;
                familia.Botellas = aux.Botellas;
                familia.RecipientesPlasticos = aux.RecipientesPlasticos;
                familia.Macetas = aux.Macetas;
                familia.Hueco = aux.Hueco;
                familia.Canaleta = aux.Canaleta;
                familia.Cubiertas = aux.Cubiertas;
                familia.Piletas = aux.Piletas;
                familia.Tanques = aux.Tanques;
                familia.TotalFocoAedico = aux.TotalFocoAedico;
                familia.TotalIspeccionado = aux.TotalIspeccionado;
                familia.TotalTratados = aux.TotalTratados;
                familia.Larvicida = aux.Larvicida;
                familia.Destruidos = aux.Destruidos;
            }

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

        // PROVEDURIA DE AGUA
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner Excretas = view1.findViewById(R.id.SPEXCRETAS);
        ArrayList<String> OpcExcretas = new ArrayList<>();

        OpcExcretas.add("");
        OpcExcretas.add("A RED PUBLICA O CLOACA");
        OpcExcretas.add("A CAMARA SEPTICA Y POZO CIEGO");
        OpcExcretas.add("SOLO A POZO CIEGO");
        OpcExcretas.add("A HOYO, EXCAVACION EN LA TIERRA");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterExcretas = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcExcretas);
        Excretas.setAdapter(comboAdapterExcretas);

        // Recordar las opciones seleccionadas
        if(familia.Excretas.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcExcretas.size(); i++){
                if(OpcExcretas.get(i).equals(familia.Excretas)){
                    indice=i;
                }
            }
            Excretas.setSelection(indice);
        }

        final RadioButton SiElectricidad = view1.findViewById(R.id.SIELECTRICIDAD);
        final RadioButton NoElectricidad = view1.findViewById(R.id.NOELECTRICIDAD);
        if(familia.Electricidad=="SI"){SiElectricidad.setChecked(true);}
        if(familia.Electricidad=="NO"){NoElectricidad.setChecked(true);}

        final RadioButton SiGas = view1.findViewById(R.id.SIGAS);
        final RadioButton NoGas = view1.findViewById(R.id.NOGAS);
        if(familia.Gas=="SI"){SiGas.setChecked(true);}
        if(familia.Gas=="NO"){NoGas.setChecked(true);}

        final RadioButton SiAguaLluvia = view1.findViewById(R.id.SIAGUALLUVIA);
        final RadioButton NoAguaLluvia = view1.findViewById(R.id.NOAGUALLUVIA);
        if(familia.AguaLluvia=="SI"){SiAguaLluvia.setChecked(true);}
        if(familia.AguaLluvia=="NO"){NoAguaLluvia.setChecked(true);}

        final Button guardar = view1.findViewById(R.id.GUARDARSERVICIOBASICOS);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SiElectricidad.isChecked()){familia.Electricidad="SI";}
                if(NoElectricidad.isChecked()){familia.Electricidad="NO";}

                if(SiGas.isChecked()){familia.Gas="SI";}
                if(NoGas.isChecked()){familia.Gas="NO";}

                familia.Excretas = Excretas.getSelectedItem().toString();
                familia.AguaOrigen = AguaOrigen.getSelectedItem().toString();
                familia.Agua = Agua.getSelectedItem().toString();

                if(SiAguaLluvia.isChecked()){familia.AguaLluvia="SI";}
                if(NoAguaLluvia.isChecked()){familia.AguaLluvia="NO";}

                ColorAvanceServiciosBasicos();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARSERVICIOSBASICOS);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceServiciosBasicos(){
        // Cambio los colores de avance
        float avance = 0;
        if (familia.Agua.length()!=0){
            avance+=1;
        }
        if (familia.AguaOrigen.length()!=0){
            avance+=1;
        }
        if (familia.AguaLluvia.length()!=0){
            avance+=1;
        }
        if (familia.Electricidad.length()!=0){
            avance+=1;
        }
        if (familia.Excretas.length()!=0){
            avance+=1;
        }
        if (familia.Gas.length()!=0){
            avance+=1;
        }

        if(avance>0 && avance<6){
            CLServiciosBasicos.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/6)*100);
            //Toast.makeText(getApplicationContext(), Double.toString(porcentaje), Toast.LENGTH_SHORT).show();
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceServiciosBasicos.setText(aux);
            //AvContacto.setText(aux);
            //AvContacto.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==6){
            CLServiciosBasicos.setBackgroundResource(R.drawable.verde);
            avanceServiciosBasicos.setText(getString(R.string.completado)+" 100%");
            //Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvContacto.setText("3/3");
            //AvContacto.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
        if(avance==0){
            CLServiciosBasicos.setBackgroundResource(R.drawable.rojo);
            avanceServiciosBasicos.setText(getString(R.string.completado)+" 00%");
            //Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvContacto.setText("3/3");
            //AvContacto.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SERVICIOS BASICOS
    public void Dengue(View view){
        Intent Modif= new Intent (this, Dengue.class);
        startActivityForResult(Modif, 1);
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // EXTERIOR VIVIENDA
    public void ServiciosExteriorVivienda(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        final View view1 = Inflater.inflate(R.layout.alert_inspeccion_exterior, null);
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

        final ImageView helpcasa = view1.findViewById(R.id.INFOBOTONCASA);
        helpcasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builderhelpcasa = new androidx.appcompat.app.AlertDialog.Builder(view1.getContext());
                LayoutInflater Inflaterhelpcasa = getLayoutInflater();
                View viewcasa = Inflaterhelpcasa.inflate(R.layout.informacion, null); //nombre del alert que quiero mostrar
                builderhelpcasa.setView(viewcasa);
                //builder.setCancelable(false); //NO cerrar el alert haciendo click en cualquier lado de la pantalla
                final androidx.appcompat.app.AlertDialog dialogcasa = builderhelpcasa.create();
                dialogcasa.show(); //mostrar
                final TextView casa = viewcasa.findViewById(R.id.INFOTEXT);
                casa.setText(getString(R.string.casilla) + "\n"+ "\n" + getString(R.string.rancho) + "\n" + "\n"+ getString(R.string.local) + "\n"+ "\n" + getString(R.string.movil));
            }
        });

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

        final RadioButton SiRevoque = view1.findViewById(R.id.SIREVOQUE);
        final RadioButton NoRevoque = view1.findViewById(R.id.NOREVOQUE);
        if(familia.Revoque=="SI"){SiRevoque.setChecked(true);}
        if(familia.Revoque=="NO"){NoRevoque.setChecked(true);}

        final ImageView helprevoque = view1.findViewById(R.id.INFOBOTONREVOQUE);
        helprevoque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builderhelprevoque = new androidx.appcompat.app.AlertDialog.Builder(view1.getContext());
                LayoutInflater Inflaterhelprevoque = getLayoutInflater();
                View viewrevoque = Inflaterhelprevoque.inflate(R.layout.informacion, null); //nombre del alert que quiero mostrar
                builderhelprevoque.setView(viewrevoque);
                //builder.setCancelable(false); //NO cerrar el alert haciendo click en cualquier lado de la pantalla
                final androidx.appcompat.app.AlertDialog dialogrevoque = builderhelprevoque.create();
                dialogrevoque.show(); //mostrar
                final TextView revoque = viewrevoque.findViewById(R.id.INFOTEXT);
                revoque.setText(getString(R.string.revoque));
            }
        });

        final EditText CantArboles = view1.findViewById(R.id.EDTARBOLES);
        if(familia.Arboles.length()!=0){CantArboles.setText(familia.Arboles);}

        final RadioButton siHielo = view1.findViewById(R.id.SINIEVEHIELO);
        final RadioButton noHielo = view1.findViewById(R.id.NONIEVEHIELO);

        final RadioButton siPerros = view1.findViewById(R.id.SIPERROSSUELTOS);
        final RadioButton noPerros = view1.findViewById(R.id.NOPERROSSUELTOS);

        final Button guardar = view1.findViewById(R.id.GUARDAREXTERIOR);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familia.TipoVivienda = TipoVivienda.getSelectedItem().toString();
                familia.Paredes = Paredes.getSelectedItem().toString();
                if(SiRevoque.isChecked()){familia.Revoque="SI";}
                if(NoRevoque.isChecked()){familia.Revoque="NO";}
                familia.Techo = Techo.getSelectedItem().toString();
                familia.Arboles = CantArboles.getText().toString();
                if(siHielo.isChecked()){familia.HieloCalle="SI";}
                if(noHielo.isChecked()){familia.HieloCalle="NO";}
                if(siPerros.isChecked()){familia.PerrosCalle="SI";}
                if(noPerros.isChecked()){familia.PerrosCalle="NO";}
                dialog.dismiss();
                ColorAvanceExteriorVivienda();
            }
        });

        final  Button cancelar =view1.findViewById(R.id.CANCELAREXTERIOR);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceExteriorVivienda(){
        // Cambio los colores de avance
        float avance = 0;
        if (familia.TipoVivienda.length()!=0){
            avance+=1;
        }
        if (familia.Paredes.length()!=0){
            avance+=1;
        }
        if (familia.Revoque.length()!=0){
            avance+=1;
        }
        if (familia.Techo.length()!=0){
            avance+=1;
        }
        if (familia.Arboles.length()!=0){
            avance+=1;
        }
        if (familia.HieloCalle.length()!=0){
            avance+=1;
        }
        if (familia.PerrosCalle.length()!=0){
            avance+=1;
        }

        if(avance>0 && avance<7){
            CLExteriorVivienda.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/7)*100);
            //Toast.makeText(getApplicationContext(), Double.toString(porcentaje), Toast.LENGTH_SHORT).show();
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceExteriorVivienda.setText(aux);
            //AvContacto.setText(aux);
            //AvContacto.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==7){
            CLExteriorVivienda.setBackgroundResource(R.drawable.verde);
            avanceExteriorVivienda.setText(getString(R.string.completado)+" 100%");
            //Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvContacto.setText("3/3");
            //AvContacto.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
        if(avance==0){
            CLExteriorVivienda.setBackgroundResource(R.drawable.rojo);
            avanceExteriorVivienda.setText(getString(R.string.completado)+" 00%");
            //Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvContacto.setText("3/3");
            //AvContacto.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // VIVIENDA
    public void Vivienda(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        final View view1 = Inflater.inflate(R.layout.alert_vivienda, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();



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
        // BAÑO
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner Baño = view1.findViewById(R.id.SPBAÑO);
        ArrayList<String> OpcBaño = new ArrayList<>();

        OpcBaño.add("");
        OpcBaño.add("DENTRO DE LA VIVIENDA");
        OpcBaño.add("FUERA DE LA VIVIENDA");
        OpcBaño.add("NO TIENE");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterBaño = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcBaño);
        Baño.setAdapter(comboAdapterBaño);

        // Recordar las opciones seleccionadas
        if(familia.Baño.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcBaño.size(); i++){
                if(OpcBaño.get(i).equals(familia.Baño)){
                    indice=i;
                }
            }
            Baño.setSelection(indice);
        }

        // BAÑO
        // Defino el spinner del tipo de vivienda y le agrego las opciones
        final Spinner BañoTiene = view1.findViewById(R.id.SPBAÑOTIENE);
        ArrayList<String> OpcBañoTiene = new ArrayList<>();

        OpcBañoTiene.add("");
        OpcBañoTiene.add("INODORO CON BOTON, MOCHILA O CADENA");
        OpcBañoTiene.add("INODORO SIN BOTON O SIN CADENA");
        OpcBañoTiene.add("POZO");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterBañoTiene = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, OpcBañoTiene);
        BañoTiene.setAdapter(comboAdapterBañoTiene);

        // Recordar las opciones seleccionadas
        if(familia.BañoTiene.length()!=0) {
            int indice=0;
            for(int i=0; i<OpcBañoTiene.size(); i++){
                if(OpcBañoTiene.get(i).equals(familia.BañoTiene)){
                    indice=i;
                }
            }
            BañoTiene.setSelection(indice);
        }

        final ImageView helpbaño = view1.findViewById(R.id.INFOBOTON);
        helpbaño.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builderhelpbaño = new androidx.appcompat.app.AlertDialog.Builder(view1.getContext());
                LayoutInflater Inflaterhelpbaño = getLayoutInflater();
                View view2 = Inflaterhelpbaño.inflate(R.layout.informacion, null); //nombre del alert que quiero mostrar
                builderhelpbaño.setView(view2);
                //builder.setCancelable(false); //NO cerrar el alert haciendo click en cualquier lado de la pantalla
                final androidx.appcompat.app.AlertDialog dialog1 = builderhelpbaño.create();
                dialog1.show(); //mostrar
                final TextView inodoro = view2.findViewById(R.id.INFOTEXT);
                inodoro.setText(getString(R.string.inodoro)+ "\n" + "\n"+ getString(R.string.inodoro2));
            }
        });
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

        final RadioButton SiCielorraso = view1.findViewById(R.id.SICIELORRAZO);
        final RadioButton NoCielorraso = view1.findViewById(R.id.NOCIELORRAZO);
        if(familia.Cielorraso=="SI"){SiCielorraso.setChecked(true);}
        if(familia.Cielorraso=="NO"){NoCielorraso.setChecked(true);}

        // GUARDAR
        final Button guardar = view1.findViewById(R.id.GUARADRVIVIENDA);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                familia.DueñoVivienda = ViviendaEs.getSelectedItem().toString();
                familia.CantidadPiezas = CantidadHabitaciones.getText().toString();
                familia.LugarCocinar = Cocinar.getSelectedItem().toString();
                familia.UsaParaCocinar = UsaCocinar.getSelectedItem().toString();
                familia.Pisos = Piso.getSelectedItem().toString();
                if(SiCielorraso.isChecked()){familia.Cielorraso="SI";}
                if(NoCielorraso.isChecked()){familia.Cielorraso="NO";}
                familia.Baño = Baño.getSelectedItem().toString();
                familia.BañoTiene = BañoTiene.getSelectedItem().toString();
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
        if (familia.Baño.length()!=0){
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
        if (familia.BañoTiene.length()!=0){
            avance+=1;
        }
        if (familia.Pisos.length()!=0){
            avance+=1;
        }
        if (familia.Cielorraso.length()!=0){
            avance+=1;
        }
        if(avance>0 && avance<8){
            CLVivienda.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/8)*100);
            //Toast.makeText(getApplicationContext(), Double.toString(porcentaje), Toast.LENGTH_SHORT).show();
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceVivienda.setText(aux);
            //AvContacto.setText(aux);
            //AvContacto.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==8){
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
                            Intent intent= new Intent (getBaseContext(), MenuMapa.class);
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

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // GUARDAR GRUPO FAMILIAR
    public void Guardar2(View view){
        CheckBox deshabitada = findViewById(R.id.CHECKVIVIENDADESHABITADA);

        if(deshabitada.isChecked()){
            MiembrosFamiliares.add(new ObjetoPersona(null));
        }
        if(MiembrosFamiliares.size()!=0 || familia.SituacionVivienda.length()!=0) {

            //final String CantidadGrupoFamiliar=grupofamiliar.getText().toString();
            // Inicio la obtencion de datos de ubicacion del GPS
            //LatLong();
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

            final String[] codigoColor = {"V"};
            final ImageView rojo = view_alert.findViewById(R.id.ICONOROJO);
            final ImageView amarillo = view_alert.findViewById(R.id.ICONOAMARILLO);
            final ImageView verde = view_alert.findViewById(R.id.ICONOVERDE);
            rojo.setBackground(getResources().getDrawable(R.drawable.button_redondo_gris));
            amarillo.setBackground(getResources().getDrawable(R.drawable.button_redondo_gris));
            verde.setBackground(getResources().getDrawable(R.drawable.button_redondo_blanco));

            rojo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    codigoColor[0] = "R";
                    rojo.setBackground(getResources().getDrawable(R.drawable.button_redondo_blanco));
                    amarillo.setBackground(getResources().getDrawable(R.drawable.button_redondo_gris));
                    verde.setBackground(getResources().getDrawable(R.drawable.button_redondo_gris));
                }
            });

            amarillo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    codigoColor[0] = "A";
                    amarillo.setBackground(getResources().getDrawable(R.drawable.button_redondo_blanco));
                    rojo.setBackground(getResources().getDrawable(R.drawable.button_redondo_gris));
                    verde.setBackground(getResources().getDrawable(R.drawable.button_redondo_gris));
                }
            });

            verde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    codigoColor[0] = "V";
                    verde.setBackground(getResources().getDrawable(R.drawable.button_redondo_blanco));
                    amarillo.setBackground(getResources().getDrawable(R.drawable.button_redondo_gris));
                    rojo.setBackground(getResources().getDrawable(R.drawable.button_redondo_gris));
                }
            });

            ObjetoPersona auxPersona = new ObjetoPersona(null);
            if(familia.SituacionVivienda.equals("X")){
                MiembrosFamiliares.add(auxPersona);
                Toast.makeText(this, "ENTRO", Toast.LENGTH_SHORT).show();
            }

            final EditText edtCalle = view_alert.findViewById(R.id.CALLE);
            final EditText edtNumero = view_alert.findViewById(R.id.NUMERO);
            final EditText cantidadintegrantes = view_alert.findViewById(R.id.CANTIDADMIEMBROSFAMILIARES);
            final EditText edtnumerocartografia = view_alert.findViewById(R.id.NUMEROSEGUNCARTOGRAFIA);

            if(deshabitada.isChecked()){
                NumerosPersonas = 0;
            }else{
            NumerosPersonas = MiembrosFamiliares.size();}

            cantidadintegrantes.setText(Integer.toString(NumerosPersonas));

            ImageView mas = view_alert.findViewById(R.id.MAS);
            mas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NumerosPersonas += 1;
                    cantidadintegrantes.setText(Integer.toString(NumerosPersonas));
                }
            });

            ImageView menos = view_alert.findViewById(R.id.MENOS);
            menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NumerosPersonas != MiembrosFamiliares.size()) {
                        NumerosPersonas -= 1;
                        cantidadintegrantes.setText(Integer.toString(NumerosPersonas));
                    }
                }
            });

            Button guardar = view_alert.findViewById(R.id.GUARDARFAMILIA);
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Latitudenviar != 0.0) {
                        /* Cuales son los datos que cargo el usuario, primero agrego los datos de la
                         familia y luego unifico los datos de las personas cargadas */
                        ArrayList<String> CategoriasDatos = familia.DatosCargadosCsv();

                        /* De cada persona cuales son los datos cargados*/
                        ArrayList<String> CategoriaPersonas = new ArrayList<>();

                        CategoriaPersonas = MiembrosFamiliares.get(0).DatosCargadosCsv();

                        /* Creo una lista de datos unificada para todas la personas de la misma
                         * familia */
                        for (int i = 1; i < MiembrosFamiliares.size(); i++) {
                            ArrayList<String> auxDatosCargados = MiembrosFamiliares.get(i).DatosCargadosCsv();
                            for (int j = 0; j < auxDatosCargados.size(); j++) {
                                if (!CategoriaPersonas.contains(auxDatosCargados.get(j))) {
                                    CategoriaPersonas.add(auxDatosCargados.get(j));
                                }
                            }
                        }

                        // Unifico las dos listas
                        CategoriasDatos.addAll(CategoriaPersonas);

                        // Leo la cabecera para comparar los datos
                        ArrayList<String> datosCabeceraCsv = DatosCabeceraCsv();

                        /* Comparo el conjunto de datos cargados con los de la cabecera csv */
                        for (int i = 0; i < CategoriasDatos.size(); i++) {
                            if (!datosCabeceraCsv.contains(CategoriasDatos.get(i))) {
                                datosCabeceraCsv.add(CategoriasDatos.get(i));
                            }
                        }

                        // Crear un string que es la nueva cabecera
                        String cabecera = "";
                        for (int i = 0; i < datosCabeceraCsv.size() - 1; i++) {
                            cabecera += datosCabeceraCsv.get(i) + ";";
                        }
                        cabecera += datosCabeceraCsv.get(datosCabeceraCsv.size() - 1);
                        //cabecera +="\n";

                        // Creo el string de la vieja cabecera
                        String viejaCabecera = "";
                        CategoriaPersonas = DatosCabeceraCsv();
                        for (int i = 0; i < CategoriaPersonas.size() - 1; i++) {
                            viejaCabecera += CategoriaPersonas.get(i) + ";";
                        }
                        viejaCabecera += CategoriaPersonas.get(CategoriaPersonas.size() - 1);
                        //viejaCabecera +="\n";

                        // Reemplazo la cabecera por una nueva
                        try {
                            ReemplazarCabecera(viejaCabecera, cabecera);
                        } catch (IOException e) {
                            makeText(getBaseContext(), "ERROR NO SE PUDO CARGAR LA CABECERA", LENGTH_SHORT).show();
                        }

                        /* Solicito los datos cargados tanto en la persona como en la familia*/

                        String coordenadas = Latitud + " " + Longitud;
                        String calle = edtCalle.getText().toString();
                        String numero = edtNumero.getText().toString();
                        String numerocartografia = edtnumerocartografia.getText().toString();
                        HashMap<String, String> datosFamilia = familia.DatosIngresados();
                        for (int i = 0; i < MiembrosFamiliares.size(); i++) {
                            String datosGuardar = "";
                            HashMap<String, String> auxHash = new HashMap<>();
                            auxHash.putAll(datosFamilia);
                            auxHash.put("COORDENADAS", coordenadas);
                            auxHash.put("ESTADO", codigoColor[0]);
                            auxHash.put("GRUPO FAMILIAR", Integer.toString(NumerosPersonas));
                            auxHash.put("NUMERO", numero);
                            auxHash.put("CALLE", calle.toUpperCase());
                            auxHash.put("NUMERO CASA CARTOGRAFIA", numerocartografia);
                            auxHash.putAll(MiembrosFamiliares.get(i).DatosIgresados());
                            for (int j = 0; j < datosCabeceraCsv.size() - 1; j++) {
                                if (auxHash.get(datosCabeceraCsv.get(j)) != null) {
                                    datosGuardar += auxHash.get(datosCabeceraCsv.get(j)) + ";";
                                } else {
                                    datosGuardar += ";";
                                }
                            }
                            if (auxHash.get(datosCabeceraCsv.get(datosCabeceraCsv.size() - 1)) != null) {
                                datosGuardar += auxHash.get(datosCabeceraCsv.get(datosCabeceraCsv.size() - 1)) + "\n";
                            } else {
                                datosGuardar += "\n";
                            }

                            /* guardo el string de la persona agregando una linea */
                            GuardarPersona(datosGuardar);
                        }

                        /* Guardo Dengue si es que se cargaron datos en este modulo*/
                        if(familia.TipoTrabajo.length()!=0){
                        GuardarDengue(calle, numero, Integer.toString(NumerosPersonas), MiembrosFamiliares.get(0));}

                        // ENVIO LA UBICACION PARA AGREGAR UN MARCADOR
                        Intent intent = new Intent(getBaseContext(), MenuMapa.class);
                        LatLng position = new LatLng(Latitudenviar, Longitudenviar); // Boa Vista
                        Bundle args = new Bundle();
                        args.putParcelable("from_position", position);
                        //intent.putExtra("MARCADOR", position);
                        intent.putExtra("bundle", args);
                        setResult(RESULT_OK, intent);

                    } else {
                        makeText(getBaseContext(), "ESPERE UNOS SEGUNDOS E INTENTE DE NUEVO, EL GPS SE ESTA UBICANDO", LENGTH_SHORT).show();
                    }
                }
            });
        }else{
                makeText(getBaseContext(), "NO HAY PERSONAS CARGADAS", LENGTH_SHORT).show();
            }

    }

    private ArrayList<String> DatosCabeceraCsv(){
        String[] cabecera_recortada = new String[0];
        /* Me dirijo a el directorio en la memoria interna del dispositivo para poder acceder a la
         * carpeta llamada RelevAr donde se almacena de manera publlica la informacion, de no
         * encontrarla se debe crear la misma*/
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();

        /* Necesito abrir el archivo .csv correspondiente al dia actual, por esta razon se solicita
         * la fecha actual al dispositivo, si este no existe se debe crear*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "RelevAr-" + fecha + ".csv";
        File dir = new File(nuevaCarpeta, NombreArchivo);

        /* Una vez tenemos el archivo dir con la ruta correcta, necesitamos leer los datos conenidos
         * en este, se va a leer la primera linea que es la que debe contener la cabecera con las categorias*/
        try {
            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            cabecera_recortada = br.readLine().split(";");
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            //Toast.makeText(this, getText(R.string.ocurrio_error) + " 1", Toast.LENGTH_SHORT).show();
        }

        ArrayList<String> devolver = new ArrayList<>();
        for(int i=0; i<cabecera_recortada.length; i++){
            devolver.add(cabecera_recortada[i]);
        }
        return devolver;
    }

    private void ReemplazarCabecera(String viejaCabecera, String nuevaCabecera) throws IOException {
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "RelevAr-" + fecha + ".csv";
        //File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(nuevaCarpeta, NombreArchivo);

        //Instantiating the Scanner class to read the file
        Scanner sc = new Scanner(dir);
        //instantiating the StringBuffer class
        StringBuffer buffer = new StringBuffer();
        //Reading lines of the file and appending them to StringBuffer
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()+System.lineSeparator());
        }

        String fileContents = buffer.toString();
        System.out.println("Contents of the file: "+viejaCabecera);
        System.out.println("Contents of the file: "+nuevaCabecera);
        //closing the Scanner object
        sc.close();

        //Replacing the old line with new line
        fileContents = fileContents.replaceAll(viejaCabecera, nuevaCabecera);
        //instantiating the FileWriter class
        FileWriter writer = new FileWriter(dir);
        System.out.println("");
        System.out.println("new data: "+fileContents);
        writer.append(fileContents);
        writer.flush();
    }

    private void GuardarPersona(String datos){
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "RelevAr-" + fecha + ".csv";

        File dir = new File(nuevaCarpeta, NombreArchivo);
        try {
            FileOutputStream fOut = new FileOutputStream(dir, true);

            //el true es para
            // que se agreguen los datos al final sin perder los datos anteriores
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                myOutWriter.append(datos);

            myOutWriter.close();
            fOut.close();
            locationManager.removeUpdates(locationListener);
            finish();

        } catch (IOException e) {
            e.printStackTrace();}
    }

    private void GuardarDengue(String calle, String numeroCalle, String cantidadMiembros, ObjetoPersona responsable){

        // Agrego la cabecera en .csv
        File ReleVar = new File(Environment.getExternalStorageDirectory() +
                "/RelevAr");
        File nuevaCarpeta = new File(ReleVar, "PLANILLAS DENGUE");
        nuevaCarpeta.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "Dengue-"+fecha+".csv";
        File dir = new File(nuevaCarpeta, NombreArchivo);

        String strLine = "";
        try {
            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            strLine = br.readLine().split(";")[0];
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            //Toast.makeText(this, getText(R.string.ocurrio_error) + " 1", Toast.LENGTH_SHORT).show();
        }

        if(!strLine.equals("CALLE")){
        String cabecera_dengue = getString(R.string.encabezado_dengue);
        try {
            FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
            // que se agreguen los datos al final sin perder los datos anteriores
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(cabecera_dengue);
            myOutWriter.append(getString(R.string.encabezado_dengue_2));
            myOutWriter.close();
            fOut.close();

        } catch (IOException e){
            e.printStackTrace();
        }}

        try {
            String coordenadas = Latitud +" "+ Longitud;
            String guardar = calle+";"+numeroCalle+";"+coordenadas+";"+responsable.FormatoGuardarDengue()+";"+
                    cantidadMiembros+";"+familia.FormatoGuardarDengue()+"\n";
        FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
        // que se agreguen los datos al final sin perder los datos anteriores
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(guardar);
        myOutWriter.close();
        fOut.close();

        } catch (IOException e){
        e.printStackTrace();
        }

    }

}

