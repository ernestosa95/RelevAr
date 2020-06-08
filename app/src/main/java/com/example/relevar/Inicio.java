package com.example.relevar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.relevar.Recursos.ServicioGPS;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
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

        empezar = (Button) findViewById(R.id.EMPEZAR);

        // Solicito multiples permisos
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA},
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);

        // Creo un archivo y lo inicializo con la cabecera predefinida
        AgregarCabecera();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // PASAR AL MENU PPAL
    public void NextMenuPrincipal(View view) {

        Intent Modif = new Intent(this, MenuPrincipal.class);
        startActivityForResult(Modif, 1);
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
                    "TELEFONO CONTACTO;PARENTEZCO CONTACTO;OCUPACION;EDUCACION;ENCUESTADOR\n";
            try {

                FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
                // que se agreguen los datos al final sin perder los datos anteriores
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                myOutWriter.append(cabecera);
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
    // PERMISOS PARA ACCEDER A LA UBICACION DEL GPS

}