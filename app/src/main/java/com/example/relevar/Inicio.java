package com.example.relevar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.relevar.MySQL.ConexionSQLiteHelper;
import com.example.relevar.MySQL.SQLitePpal;
import com.example.relevar.Recursos.Encuestador;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_POSITION = 1;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;
    // Widgets
    Button empezar;
    Encuestador encuestador = new Encuestador();
    EditText Nencuestador;

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
        /*SQLiteDatabase db = conn.getReadableDatabase();
        String consulta ="SELECT * FROM ENCUESTADOR";
        Cursor a = db.rawQuery(consulta, null);
        while (a.moveToNext()){
            enc.add(a.getString(0));
        }
        db.close();*/

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
                    Intent Modif = new Intent(getBaseContext(), MenuMapa.class);
                    Modif.putExtra("IDENCUESTADOR", encuestador.getID());
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
                    SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);
                    admin.CrearUsuario(Nencuestador.getText().toString(), "APELLIDO");
                    /*SQLiteDatabase db = conn.getWritableDatabase();
                    String insert ="INSERT INTO ENCUESTADOR (ID) VALUES ('"+Nencuestador.getText().toString()+"')";
                    db.execSQL(insert);
                    db.close();*/

                    // Inicializo el encuestador
                    encuestador.setID(Nencuestador.getText().toString());
                    Intent Modif = new Intent(getBaseContext(), MenuMapa.class);
                    Modif.putExtra("IDENCUESTADOR", encuestador.getID());
                    startActivityForResult(Modif, 1);
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

}