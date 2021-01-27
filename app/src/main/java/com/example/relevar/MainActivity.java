package com.example.relevar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.relevar.Recursos.Encuestador;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;
import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
    // DEFINICION DE VARIABLES GLOBALES
    // Definicion de contantes que hacen al funcionamiento
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private int STORAGE_PERMISSION_CODE =1;

    // Definicion de String para contener informacion
    private String Latitud, Longitud;
    private Double Latitudenviar, Longitudenviar;

    // Encuestador
    Encuestador encuestador = new Encuestador();

    // Defino Arrays para almacenar datos
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> categoriasPersona = new ArrayList<>();
    private ArrayList<ObjetoPersona> MiembrosFamiliares = new ArrayList<ObjetoPersona>();

    // Defino la lisa de personas
    private ListView lv1;

    // Defino un Adaptador para el ListView
    ArrayAdapter<String> adapter;

    int position, NumerosPersonas;

    LocationManager locationManager;
    LocationListener locationListener;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Evita la rotacion
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_main);

        // Seteo el titulo de la action bar del activity
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Familia");
        encuestador.setID((String) getIntent().getStringExtra("IDENCUESTADOR"));
        lv1 = (ListView) findViewById(R.id.list1);

        LatLong();

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
                Modif.putExtra("TEMPERATURA" , MiembrosFamiliares.get(position).Temperatura);
                Modif.putExtra("TOS" , MiembrosFamiliares.get(position).Tos);
                Modif.putExtra("RESPIRATORIO" , MiembrosFamiliares.get(position).Respiratorio);
                Modif.putExtra("GARGANTA" , MiembrosFamiliares.get(position).Garganta);
                Modif.putExtra("ANOSMIA" , MiembrosFamiliares.get(position).Anosmia);
                Modif.putExtra("DISGEUSIA" , MiembrosFamiliares.get(position).Disgeusia);
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
                ObjetoPersona Persona=new ObjetoPersona(categoriasPersona);
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
                Persona.Temperatura = data.getStringExtra("TEMPERATURA");
                Persona.Tos = data.getStringExtra("TOS");
                Persona.Respiratorio = data.getStringExtra("RESPIRATORIO");
                Persona.Garganta = data.getStringExtra("GARGANTA");
                Persona.Anosmia = data.getStringExtra("ANOSMIA");
                Persona.Disgeusia = data.getStringExtra("DISGEUSIA");

                //Agrego la persona como miembro de la familia
                MiembrosFamiliares.add(Persona);

                //makeText(this, Persona.Nombre, LENGTH_SHORT).show();
                names.add("DNI: "+Persona.DNI+", "+Persona.Apellido+" "+Persona.Nombre);}

                ListeVer();
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
                /*final EditText cantidadintegrantes = view_alert.findViewById(R.id.CANTIDADMIEMBROSFAMILIARES);
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
                });*/

                Button guardar = view_alert.findViewById(R.id.GUARDARFAMILIA);
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                        e.printStackTrace();}
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

