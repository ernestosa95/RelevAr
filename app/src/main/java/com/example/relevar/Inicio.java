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

public class Inicio extends AppCompatActivity {

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;

    // Widgets
    Button empezar;
    Encuestador encuestador = new Encuestador();
    EditText Nencuestador;
    Spinner SPProvincias;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_inicio);

        // Eliminar el action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        // Evitar la rotacion
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }

        // Boton para comenzar a cargar datos
        empezar = (Button) findViewById(R.id.EMPEZAR);

        // Solicito multiples permisos
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA},
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);


        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        // Cargo la base de datos de los trabajos si no esta vacia
        //SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        try{
            InputStream fis = getResources().openRawResource(R.raw.trabajos);//new FileInputStream(R.raw.efectores);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            SQLiteDatabase Bd1 = admin.getWritableDatabase();
            String myData="";
            if(!admin.ExisteTrabajos()){
                while ((myData=br.readLine())!=null){

                    ContentValues registro = new ContentValues();
                    registro.put("TRABAJO", myData);
                    Bd1.insert("TRABAJOS", null, registro);

                }
                // Cierro todo las bases de datos y lectura de archivos
                Bd1.close();
                br.close();
                in.close();
                fis.close();
            }else{
                //Toast.makeText(this, "YA ESTA CARGADO TRABAJOS", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e){
            Toast.makeText(this, "NO SE CREO LA BASE DE DATOS DE TRABAJOS", Toast.LENGTH_SHORT).show();}

    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // PASAR AL MENU PPAL, PRIMERO ELIGIENDO UN USUARIO
    public void NextMenuPrincipal(View view) {
        if(checkIfLocationOpened()) {
            /*Intent Modif = new Intent(this, MenuMapa.class);
            AgregarCabecera();
            startActivityForResult(Modif, 1);
             */
            AgregarCabecera();
            Encuestador();
        }
        else{makeText(getBaseContext(), "UBICACION DEL TELEFONO DESACTIVADA, POR FAVOR ACTIVARLA", LENGTH_SHORT).show();}
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // AGREGAR CABECERA POR UNICA VEZ A LOS ARCHIVOS DE DATOS

    private void AgregarCabecera() {
        // Agrego la cabecera en .csv
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "RelevAr-" + fecha + ".csv";
        File dir = new File(nuevaCarpeta, NombreArchivo);
        String strLine = "";
        // leer datos
        String myData = "";
        try {
            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            strLine = br.readLine().split(";")[0];
            //Toast.makeText(this, strLine, Toast.LENGTH_SHORT).show();
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
        }
        // guardo los datos
        if (strLine.equals("CALLE") != true) {
            String cabecera = "CALLE;NUMERO;COORDENADAS;GRUPO FAMILIAR;DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;" +
                    "FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;" +
                    "LOTE DE VACUNA;TELEFONO CELULAR;TELEFONO FIJO;MAIL;OBSERVACIONES;PRODUCTO DE LIMPIEZA;NOMBRE CONTACTO;" +
                    "TELEFONO CONTACTO;PARENTEZCO CONTACTO;OCUPACION;EDUCACION;TIPO DE VIVIENDA;LA VIVIENDA ES;CANTIDAD HABITACIONES;LUGAR PARA COCINAR;USA PARA COCINAR;PAREDES EXTERIORES;REVOQUE EXTERIOR;PISOS;EXTERIOR DEL TECHO; CIELORRASO;ENCUESTADOR\n";
            String cab = getString(R.string.encabezado);
            try {

                FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
                // que se agreguen los datos al final sin perder los datos anteriores
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                myOutWriter.append(cab);
                myOutWriter.close();
                fOut.close();
                //Toast.makeText(this, strLine+"2", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(this, "Datos NO guardados", Toast.LENGTH_SHORT).show();
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

    // FUNCION DE ENCUESTADORES, muestra los encuestadores cargados y la posibilidad de crear un
    // NUEVO ENCUENTADOR
    private void Encuestador(){
        // Creo el alert dialog con los widgets para encuestador
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
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
        enc = admin.Encuestadores();


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
                    SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
                    admin.DesactivarUsuario();
                    String nombre = encuestadores.getSelectedItem().toString().split(",")[0];
                    String apellido = encuestadores.getSelectedItem().toString().split(",")[1];
                    admin.ActivarUsuario(nombre,apellido);
                    //encuestador.setID(encuestadores.getSelectedItem().toString());
                    //makeText(getBaseContext(), encuestador.getID(), LENGTH_SHORT).show();
                    Intent Modif = new Intent(getBaseContext(), MenuMapa.class);
                    //Modif.putExtra("IDENCUESTADOR", encuestador.getID());
                    startActivityForResult(Modif, 1);

                    dialog.dismiss();}
                else {makeText(getBaseContext(), "NO HAY ENCUESTADORES", LENGTH_SHORT).show();}
            }
        });
    }

    // FUNCION DE CREAR NUEVO ENCUESTADOR
    private void NuevoEncuestador(){
        // Creo el Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view = Inflater.inflate(R.layout.alert_crear_encuestador, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

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

        final EditText NonmbreEncuestador = view.findViewById(R.id.EditCrearEncuestador);
        final EditText ApellidoEncuestador = view.findViewById(R.id.editApellidoEncuestador);
        final EditText DNI = view.findViewById(R.id.editDNI);

        Button nuevo = view.findViewById(R.id.GUARDARCREARENCUESTADOR);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ingreso datos del encuestador
                    if(DNI.getText().toString().length()!=0) {
                        if (SPProvincias.getSelectedItem().toString().length() != 0) {
                            if(NonmbreEncuestador.getText().toString().length()!=0){
                            SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
                            admin.DesactivarUsuario();
                            admin.CrearUsuario(NonmbreEncuestador.getText().toString(),
                                    ApellidoEncuestador.getText().toString(),
                                    DNI.getText().toString(),
                                    SPProvincias.getSelectedItem().toString());

                            BdEfectores bdEfectores = new BdEfectores();
                            bdEfectores.execute();

                            // Inicializo el encuestador
                            //encuestador.setID(Nencuestador.getText().toString());
                            Intent Modif = new Intent(getBaseContext(), MenuMapa.class);
                            //Modif.putExtra("IDENCUESTADOR", encuestador.getID());
                            startActivityForResult(Modif, 1);
                            dialog.dismiss();

                            } else {
                                makeText(getBaseContext(), "INGRESE UNA NOMBRE", LENGTH_SHORT).show();
                            }
                            } else {
                            makeText(getBaseContext(), "INGRESE UNA PROVINCIA", LENGTH_SHORT).show();
                        }
                    }else{makeText(getBaseContext(), "INGRESE DNI", LENGTH_SHORT).show();}

            }
        });

        Button cancelar = view.findViewById(R.id.CANCELARCREARENCUESTADOR);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Encuestador();
                dialog.dismiss();
            }
        });
    }

    private class BdEfectores extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Inicio.this);
            pd.setMessage("Cargando datos, aguarde");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
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

                    // Cierro todo las bases de datos y lectura de archivos
                    Bd1.close();
                    br.close();
                    in.close();
                    fis.close();
                    //Toast.makeText(getBaseContext(), "CREADA LA BASE DE DATOS", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), "YA ESTA CREADA LA BASE DE DATOS", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                //Toast.makeText(getBaseContext(), "NO SE CREO LA BASE DE DATOS", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

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