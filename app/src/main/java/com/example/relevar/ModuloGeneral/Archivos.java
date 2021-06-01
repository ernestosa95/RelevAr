package com.example.relevar.ModuloGeneral;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.relevar.R;

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
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;

public class Archivos {

    // Defino los valores
    Context context;
    ArrayList<String> listaNombresArchivos = new ArrayList<String>();
    ArrayList<String> listaRutasArchivos = new ArrayList<String>();

    public Archivos(Context newContext){
        context = newContext;
    }

    public void agregarCabecera(){
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
            String cab = "CALLE;NUMERO;COORDENADAS;ESTADO;GRUPO FAMILIAR;MENORES;MAYORES;DNI;APELLIDO;NOMBRE;FECHA DE NACIMIENTO;SEXO;QR;NUMERO CASA CARTOGRAFIA\n";
            try {
                //el true es para que se agreguen los datos al final sin perder los datos anteriores
                FileOutputStream fOut = new FileOutputStream(dir, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(cab);
                myOutWriter.close();
                fOut.close();

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(this, R.string.ocurrio_error+" 2", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // COMPARTIR ARCHIVOS
    public ArrayList<String> getListaNombresArchivos(String rutaDirectorio){
        this.obtenerListados(rutaDirectorio);
        return listaNombresArchivos;
    }

    public ArrayList<String> getListaRutasArchivos(String rutaDirectorio){
        this.obtenerListados(rutaDirectorio);
        return listaRutasArchivos;
    }

    private void obtenerListados(String rutaDirectorio){

        listaNombresArchivos.clear();
        listaRutasArchivos.clear();

        File directorioactual = new File(rutaDirectorio);
        File[] listaArchivos = directorioactual.listFiles();

        int x=0;
        if(!rutaDirectorio.equals("/storage/emulated/0/RelevAr")){
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
            listaRutasArchivos.add(rutaDirectorio);
        }
    }

    public Intent Compartir(String nombreArchivo){
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();

        File dir = new File(nombreArchivo);
        Uri path = FileProvider.getUriForFile(context, "com.example.relevar", dir);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, nombreArchivo);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Valores.");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        return emailIntent;
        //context.startActivity(Intent.createChooser(emailIntent, "SUBIR ARCHIVO"));
    }
}
