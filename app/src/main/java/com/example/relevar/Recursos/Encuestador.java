package com.example.relevar.Recursos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;

public class Encuestador {
    public String ID="";

    public void setID(String ID) {this.ID = ID;}

    public String getID() {
        return ID;
    }

    public void GuardarRecorrido(String Latitud, String Longitud){
        //permisosEscribir();
        // Agrego la cabecera en .csv
        File ReleVar = new File(Environment.getExternalStorageDirectory() +
                "/RelevAr");
        File nuevaCarpeta = new File(ReleVar, "RECORRIDOS");
        nuevaCarpeta.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "Recorridos-"+fecha+".csv";

        File dir = new File(nuevaCarpeta, NombreArchivo);

        Calendar calendario = new GregorianCalendar();
        int hora, minutos, segundos;
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);

        if(Latitud!=null && Longitud!=null){
            String cabecera = Integer.toString(hora)+":"+Integer.toString(minutos)+":"+Integer.toString(segundos)+";"+Latitud+" "+Longitud+";"+ID+"\n";
            try {
                FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
                // que se agreguen los datos al final sin perder los datos anteriores
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(cabecera);
                myOutWriter.close();
                fOut.close();

            } catch (IOException e){
                e.printStackTrace();
            }}
    }
}
