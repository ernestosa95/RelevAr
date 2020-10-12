package com.example.relevar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.relevar.MySQL.SQLitePpal;
import com.example.relevar.Recursos.Encuestador;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

// Descripcion de la Activity:
/*      Esta activity es la pantalla inicial de la App, su principal función es la de preparar los
*       datos necesarios para poder iniciar el uso de la app.*/

// Metodos de la Activity:

//--------------------------------------------------------------------------------------------------

//  - Oncreate()
//  - NextMenuPrincipal()
//  - AgregarCabecera()
//  - checkIfLocationOpened()
//  - Encuestador()
//  - NuevoEncuestador()
//  - Extensión de AsyncTask: BdEfectores

public class Inicio extends AppCompatActivity {

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;

    // Widgets
    Button empezar;
    Spinner SPProvincias;

    private ArrayList<String> botones = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_inicio);

        // Boton para comenzar a cargar datos
        empezar = (Button) findViewById(R.id.EMPEZAR);

        // Eliminar el action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        // Evitar la rotacion
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }

        // Solicito multiples permisos para las diferentes servicios que se utilizan
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.GET_ACCOUNTS},
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);

        // Cargo los botones
        // Botones de la familia
        botones.add("INSPECCION EXTERIOR");
        botones.add("SERVICIOS BASICOS");
        botones.add("VIVIENDA");
        botones.add("DENGUE");

        // Botones de la persona: general
        botones.add("EDUCACION");
        botones.add("OCUPACION");
        botones.add("CONTACTO");
        botones.add("EFECTOR");
        botones.add("OBSERVACIONES");

        // Botones de la persona: fisico
        botones.add("FACTORES DE RIESGO");
        botones.add("DISCAPACIDAD");
        botones.add("EMBARAZO");
        botones.add("VITAMINA D");

        // Botones de la persona: psico-social
        botones.add("ACOMPAÑAMIENTO");
        botones.add("TRASTORNOS EN NIÑOS");
        botones.add("TRASTORNOS MENTALES");
        botones.add("ADICCIONES");
        botones.add("VIOLENCIA");
        botones.add("OCIO");
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // PASAR AL MENU PPAL, PRIMERO ELIGIENDO UN USUARIO

    public void NextMenuPrincipal(View view) {
        if(checkIfLocationOpened()) {
            AgregarCabecera();
            Encuestador();
        }
        else{makeText(getBaseContext(), "UBICACION DEL TELEFONO DESACTIVADA, POR FAVOR ACTIVARLA", LENGTH_SHORT).show();}
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // AGREGAR CABECERA POR UNICA VEZ A LOS ARCHIVOS DE DATOS

    private void AgregarCabecera() {
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

        /* Con los datos de la primer fila es necesario ahora corroborar que esta se corresponda con
        * la cabecera, la primer palabra que debe aparecer es la palbra "CALLE"*/
        if (strLine.equals("CALLE") != true) {
            //String cab = getString(R.string.encabezado);
            String cab = "CALLE;NUMERO;COORDENADAS;ESTADO;GRUPO FAMILIAR;DNI;APELLIDO;NOMBRE;FECHA DE NACIMIENTO;SEXO;NUMERO CASA CARTOGRAFIA\n";
            try {
                //el true es para que se agreguen los datos al final sin perder los datos anteriores
                FileOutputStream fOut = new FileOutputStream(dir, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(cab);
                myOutWriter.close();
                fOut.close();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.ocurrio_error+" 2", Toast.LENGTH_SHORT).show();
            }
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // CONOCER SI LA UBICACION ESTA ACTIVADA

    private boolean checkIfLocationOpened() {
    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    System.out.println("Provider contains=> " + provider);
    if (provider.contains("gps") || provider.contains("network")){
        return true;
    }
    return false;
}

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // FUNCIONES DE INICIO, CREAR Y SELECCIONAR EL ENCUESTADOR

    // ENCUENTADOR, INGRESAR CON UN ENCUESTADOR PRECARGADO O CREAR UNO NUEVO

    private void Encuestador(){
        /* Creo un alert dialog y utilizo el recurso layout creado alert_encuestador para poder
        * mostrar las diferentes opciones y el listado de encuestadores precargados*/
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

        // Recupero los encuestadores cargados en la base de datos
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        enc = admin.Encuestadores();

        // Cargo el spinner con los datos de los encuestadores
        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, enc);
        encuestadores.setAdapter(comboAdapter);

        // Creo y le otorgo las funciones al boton crear nuevo encuestador
        Button nuevo = view.findViewById(R.id.NUEVOENC);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevoEncuestador();
                dialog.dismiss();
            }
        });

        /* Creo y le otorgo las funciones al boton ingresar, desactivando todos los usarios,
        * seleccionando el nuevo usuario y activando solo a este, una vez realizada estas acciones
        * se dirije la app a la activity MenuMapa*/
        Button ingresar = view.findViewById(R.id.INGRESAR);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (encuestadores.getAdapter().getCount()!=0){
                    SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
                    admin.DesactivarUsuario();
                    String nombre = encuestadores.getSelectedItem().toString().split(",")[0];
                    String apellido = encuestadores.getSelectedItem().toString().split(",")[1];
                    admin.ActivarUsuario(nombre,apellido);
                    Intent Modif = new Intent(getBaseContext(), MenuMapa.class);
                    startActivityForResult(Modif, 1);
                    dialog.dismiss();}
                else {
                    makeText(getBaseContext(), "NO HAY ENCUESTADORES", LENGTH_SHORT).show();
                }
            }
        });
    }

    // FUNCION DE CREAR NUEVO ENCUESTADOR

    private void NuevoEncuestador(){
        // Creo el Alert dialog con los recursos layout creados para este alert_crear_encuestador
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view = Inflater.inflate(R.layout.alert_crear_encuestador, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Creo un spinner para mostrar la seleccion de provincias
        SPProvincias = view.findViewById(R.id.PROVINCIA);
        ArrayList<String> Provincias = new ArrayList<String>();
        Provincias.add("");
        Provincias.add("ENTRE RÍOS");
        Provincias.add("CABA");
        Provincias.add("BUENOS AIRES");
        Provincias.add("CATAMARCA");
        Provincias.add("CÓRDOBA");
        Provincias.add("TIERRA DEL FUEGO");
        Provincias.add("TUCUMÁN");
        Provincias.add("SANTA CRUZ");
        Provincias.add("RÍO NEGRO");
        Provincias.add("CHUBUT");
        Provincias.add("MENDOZA");
        Provincias.add("SAN JUAN");
        Provincias.add("LA PAMPA");
        Provincias.add("SANTA FE");
        Provincias.add("CHACO");
        Provincias.add("CORRIENTES");
        Provincias.add("MISIONES");
        Provincias.add("FORMOSA");
        Provincias.add("SANTIAGO DEL ESTERO");
        Provincias.add("SAN LUIS");
        Provincias.add("LA RIOJA");
        Provincias.add("SALTA");
        Provincias.add("JUJUY");
        Provincias.add("NEUQUÉN");

        // Cargo el spinner con los datos
        ArrayAdapter<String> comboAdapterAgua = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, Provincias);
        SPProvincias.setAdapter(comboAdapterAgua);

        // Creo los edittext para ingresar datos de nombre, apellido, dni
        final EditText NonmbreEncuestador = view.findViewById(R.id.EditCrearEncuestador);
        final EditText ApellidoEncuestador = view.findViewById(R.id.editApellidoEncuestador);
        final EditText DNI = view.findViewById(R.id.editDNI);

        /* Creo y asigno las funciones al boton de crear encuentador, corroboro que ninguno de los
        campos este vacio, desactivo todos los encuestadores y luego inserto en la base de datos el
        nuevo, activandolo, paso siguiente paso la app a la activity MenuMapa*/
        Button nuevo = view.findViewById(R.id.GUARDARCREARENCUESTADOR);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(DNI.getText().toString().length()!=0) {
                        if (SPProvincias.getSelectedItem().toString().length() != 0) {
                            if(NonmbreEncuestador.getText().toString().length()!=0){
                                if(ApellidoEncuestador.getText().toString().length()!=0){
                            SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
                            admin.DesactivarUsuario();
                            admin.CrearUsuario(NonmbreEncuestador.getText().toString(),
                                    ApellidoEncuestador.getText().toString(),
                                    DNI.getText().toString(),
                                    SPProvincias.getSelectedItem().toString());

                            // Creo en segundo plano las bases de datos correspondientes a este
                            // encuestador
                            BdEfectores bdEfectores = new BdEfectores();
                            bdEfectores.execute();

                            Intent Modif = new Intent(getBaseContext(), MenuMapa.class);
                            startActivityForResult(Modif, 1);
                            dialog.dismiss();
                                } else {
                                    makeText(getBaseContext(), "INGRESE UN APELLIDO", LENGTH_SHORT).show();
                                }
                            } else {
                                makeText(getBaseContext(), "INGRESE UN NOMBRE", LENGTH_SHORT).show();
                            }
                            } else {
                            makeText(getBaseContext(), "INGRESE UNA PROVINCIA", LENGTH_SHORT).show();
                        }
                    }else{makeText(getBaseContext(), "INGRESE DNI", LENGTH_SHORT).show();}

            }
        });

        /* Creo y asigno la posibilidad de cerrar las opciones de cancelar la carga de un nuevo
        * encuestador, iniciando nuevamente el alert dialog de encuestador*/
        Button cancelar = view.findViewById(R.id.CANCELARCREARENCUESTADOR);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Encuestador();
                dialog.dismiss();
            }
        });
    }

    // ESTA ES UNA EXTENSION PARA PODER CREAR LAS BASES DE DATOS EN SEGUNDO PLANO

    private class BdEfectores extends AsyncTask<Void, Void, Void> {

        // Creo un progress dialog para mostrar mientras se ejecuta este codigo
        ProgressDialog pd;

        /*Antes de comenzar la ejecucion se inicia el progress dialog con los siguientes atributos*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Inicio.this);
            pd.setMessage("Cargando datos, aguarde");
            pd.setCancelable(false);
            pd.show();
        }

        /* Este es el codigo que se ejecuta en segundo plano mientras el usuario ve un cartel de
        * cargando datos*/
        @Override
        protected Void doInBackground(Void... voids) {
            /* Se crea una conexion con la base de datos, se corrobora que los datos de la provincia
            * seleccionada por el encuestador no esten cargados y se comienza a leer desde efectores.csv
            * solo eligiendo los efectores correspondientes a esta provincia, posteriomente se los
            * inserta en la base de datos.
            * Esta accion se realiza una vez por provincia*/
            SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
            String myData = "";
            try {
                InputStream fis = getResources().openRawResource(R.raw.efectores);//new FileInputStream(R.raw.efectores);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                SQLiteDatabase Bd1 = admin.getWritableDatabase();
                if (!admin.ExisteEfectores(SPProvincias.getSelectedItem().toString())) {
                    while ((myData = br.readLine()) != null) {

                        if (myData.split(",")[2].equals(SPProvincias.getSelectedItem().toString())) {
                            ContentValues registro = new ContentValues();
                            registro.put("NOMBRE", myData.split(",")[0]);
                            registro.put("PROVINCIA", myData.split(",")[2]);
                            Bd1.insert("EFECTORES", null, registro);
                        }
                    }
                    Bd1.close();
                    br.close();
                    in.close();
                    fis.close();

                }
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), R.string.ocurrio_error, Toast.LENGTH_SHORT).show();
            }

            /* Como la base de datos de los trabajos solo ejecuta una vez desde la instalacion de la
            * app aprovecho y tambien cargo la base de datos de los botones*/
            /* Tambien debo crear la base de datos de los trabajos, esto se hace una vez, jsto despues
            * de instalar la app, para eso corroboro que la misma no este creada, leo los datos desde
            * trabajo.csv y los inserto en la base de datos correspondiente*/
            try{
                InputStream fis = getResources().openRawResource(R.raw.trabajos);//new FileInputStream(R.raw.efectores);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                SQLiteDatabase Bd1 = admin.getWritableDatabase();
                //String myData="";
                if(!admin.ExisteTrabajos()){
                    while ((myData=br.readLine())!=null){

                        ContentValues registro = new ContentValues();
                        registro.put("TRABAJO", myData);
                        Bd1.insert("TRABAJOS", null, registro);


                    }
                    for(int i=0; i<botones.size(); i++){
                        ContentValues botonesValores = new ContentValues();
                        botonesValores.put("BOTON", botones.get(i));
                        botonesValores.put("ACTIVO", false);
                        Bd1.insert("BOTONES", null, botonesValores);
                    }
                    Bd1.close();
                    br.close();
                    in.close();
                    fis.close();
                }
            }
            catch (IOException e){
                Toast.makeText(getBaseContext(), R.string.ocurrio_error, Toast.LENGTH_SHORT).show();}
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

}