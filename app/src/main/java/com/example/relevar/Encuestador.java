package com.example.relevar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Environment;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.relevar.ModuloGeneral.Archivos;
import com.example.relevar.ModuloGeneral.Ubicacion.Ubicacion;
import com.example.relevar.MySQL.SQLitePpal;
import com.google.android.gms.maps.model.LatLng;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;
import static android.widget.Toast.makeText;

public class Encuestador extends Activity {
    // DEFINICIONES DE VARIABLES A UTILIZAR
    private final List<String> Provincias = Arrays.asList("","ENTRE RÍOS","SANTA FE", "BUENOS AIRES","CABA","CATAMARCA",
                                    "CÓRDOBA","TIERRA DEL FUEGO","TUCUMÁN","SANTA CRUZ","RÍO NEGRO","CHUBUT",
                                    "MENDOZA","SAN JUAN","LA PAMPA","CHACO","CORRIENTES","MISIONES","FORMOSA",
                                    "SANTIAGO DEL ESTERO","SAN LUIS","LA RIOJA","SALTA","JUJUY","NEUQUÉN");
    private final List<String> Botones = Arrays.asList("INSPECCION EXTERIOR", "SERVICIOS BASICOS","VIVIENDA","DENGUE",
                                    "EDUCACION","INGRESO Y OCUPACION","CONTACTO","EFECTOR","OBSERVACIONES","FACTORES DE RIESGO",
                                    "DISCAPACIDAD","EMBARAZO","VITAMINA D","ENFERMEDADES CRONICAS","ACOMPAÑAMIENTO",
                                    "TRASTORNOS EN NIÑOS","TRASTORNOS MENTALES","ADICCIONES","VIOLENCIA","OCIO");

    public String Nombre, Apellido, DNI, Provincia;
    private Context context;
    public Ubicacion ubicacion;
    public Archivos archivos;

    // CONSTRUCTOR: Obtengo los datos del encuestador que esta activado
    public Encuestador(Context baseContext){
        context = baseContext;
        archivos = new Archivos(context);
        ubicacion = new Ubicacion(context);
        SQLitePpal admin = new SQLitePpal(context, "DATA_PRINCIPAL", null, 1);
        HashMap<String, String> encuestador_activado = admin.encuestadorActivado();
        if (encuestador_activado.size()!=0){
            Nombre = encuestador_activado.get("NOMBRE");
            Apellido = encuestador_activado.get("APELLIDO");
            DNI = encuestador_activado.get("DNI");
            Provincia = encuestador_activado.get("PROVINCIA");
        }
    }

    public boolean checkUbicacionGPS(ContentResolver contentResolver){
        return ubicacion.checkUbicacionGPS(contentResolver);
    }

    public boolean existe(String nombre, String apellido){
        SQLitePpal admin = new SQLitePpal(context, "DATA_PRINCIPAL", null, 1);
        return admin.existeEncuestador(nombre,apellido);
    }

    public void activarUsuario(String nombre, String apellido){
        SQLitePpal admin = new SQLitePpal(context, "DATA_PRINCIPAL", null, 1);
        admin.desactivarUsuarios();
        admin.activarUsuario(nombre, apellido);
    }

    public ArrayList<String> Provincias(){
        return new ArrayList<String>(Provincias);
    }

    public ArrayList<String> Botones(){
        return new ArrayList<String>(Botones);
    }

    public void crearUsuario(String nombre, String apellido, String dni, String provincia){
        SQLitePpal admin = new SQLitePpal(context, "DATA_PRINCIPAL", null, 1);
        admin.desactivarUsuarios();
        admin.CrearUsuario(nombre, apellido, dni, provincia);
    }

    public void cerrarSesion(){
        SQLitePpal admin = new SQLitePpal(context, "DATA_PRINCIPAL", null, 1);
        admin.desactivarUsuarios();
    }

    // BOTONES
    public Switch comportamientoSwitch(Switch switch_editar){
        final SQLitePpal admin = new SQLitePpal(context, "DATA_PRINCIPAL", null, 1);
        if(admin.EstadoBoton("INSPECCION EXTERIOR")){
            switch_editar.setChecked(true);
        }

        switch_editar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(switch_editar.getText().toString());
                } else {
                    admin.DesactivarBoton(switch_editar.getText().toString());
                }
            }
        });
        return switch_editar;
    }




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

            String cabecera = Integer.toString(hora)+":"+Integer.toString(minutos)+":"+Integer.toString(segundos)+";"+Latitud+" "+Longitud+";"
                    +";"+CantidadRegistros()+";"+ID+"\n";
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

    private String CantidadRegistros(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha = dateFormat.format(date1);
        String NombreArchivo = "RelevAr-"+fecha+".csv";

        int cant = 0;
        String linea = "";
        try {
            File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
            nuevaCarpeta.mkdirs();
            File dir = new File(nuevaCarpeta, NombreArchivo);
            String strLine = "";
            // leer datos
            String myData = "";

            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            linea = br.readLine();

            while((linea =br.readLine())!= null){
                cant++;
            }

            br.close();
            in.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.toString(cant);
    }

    public ArrayList<LatLng> Marcadores (String fechaVisualizacion){
        ArrayList<LatLng> marcadores = new ArrayList<>();
        String linea;
        try {
            File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
            nuevaCarpeta.mkdirs();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = new Date();
            String fecha = dateFormat.format(date1);
            String NombreArchivo = "RelevAr-" +fechaVisualizacion;//+ fecha + ".csv";
            File dir = new File(nuevaCarpeta, NombreArchivo);
            String strLine = "";
            // leer datos
            String myData = "";

            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            linea = br.readLine();
            while((linea =br.readLine())!= null){
                String[] aux = linea.split(";");
                String[] coordenadas = aux[2].split(" ");
                marcadores.add(new LatLng(Double.parseDouble(coordenadas[0]), Double.parseDouble(coordenadas[1])));
            }

            br.close();
            in.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return marcadores;
    }

    public ArrayList<String> CodigoColores (String fechaInteres){
        ArrayList<String> marcadores = new ArrayList<>();
        String linea;
        try {
            File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
            nuevaCarpeta.mkdirs();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = new Date();
            String fecha = dateFormat.format(date1);
            String NombreArchivo = "RelevAr-" +fechaInteres;//+ fecha + ".csv";
            File dir = new File(nuevaCarpeta, NombreArchivo);
            String strLine = "";
            // leer datos
            String myData = "";

            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            linea = br.readLine();
            while((linea =br.readLine())!= null){
                String[] aux = linea.split(";");

                marcadores.add(aux[3]);
            }

            br.close();
            in.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return marcadores;
    }

    public ArrayList<String> CodigoCartografia (String fechaInteres){
        ArrayList<String> marcadores = new ArrayList<>();
        String linea;
        try {
            File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
            nuevaCarpeta.mkdirs();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = new Date();
            String fecha = dateFormat.format(date1);
            String NombreArchivo = "RelevAr-" +fechaInteres;//+ fecha + ".csv";
            File dir = new File(nuevaCarpeta, NombreArchivo);
            String strLine = "";
            // leer datos
            String myData = "";

            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            linea = br.readLine();
            while((linea =br.readLine())!= null){
                String[] aux = linea.split(";");
                if(aux.length<=10){
                    marcadores.add("");
                }
                else {
                    marcadores.add(aux[10]);}
            }

            br.close();
            in.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return marcadores;
    }
}
