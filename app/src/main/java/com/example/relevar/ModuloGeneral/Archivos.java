package com.example.relevar.ModuloGeneral;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.relevar.MySQL.SQLitePpal;
import com.example.relevar.ObjetoFamilia;
import com.example.relevar.ObjetoPersona;
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
import java.util.HashMap;
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

    public ArrayList<String> listadoFechasArchivo(){
        /* Se crea la lista de los archivos .csv que estan disponibles en la memoria interna en la carpeta
         * RelevAr*/
        ArrayList<String> listaFechasArchivos = new ArrayList<String>();
        ArrayList<String> listaRutasFechasArchivos = new ArrayList<String>();
        String RutaDirectorio = "/storage/emulated/0/RelevAr";
        File directorioactual = new File(RutaDirectorio);
        File[] listaArchivos = directorioactual.listFiles();

        int x=0;

        for(File archivo : listaArchivos){
            listaRutasFechasArchivos.add(archivo.getPath());
        }

        Collections.sort(listaRutasFechasArchivos, String.CASE_INSENSITIVE_ORDER);

        for(int i=x; i<listaRutasFechasArchivos.size(); i++){
            File archivo = new File(listaRutasFechasArchivos.get(i));
            if(archivo.isFile()){
                String[] auxCorte = archivo.getName().split("-");
                String auxNombre = auxCorte[1]+"-"+auxCorte[2]+"-"+auxCorte[3];
                //auxNombre.replaceAll(".csv", "");
                listaFechasArchivos.add(auxNombre);
            } else{
                //listaFechasArchivos.add("/"+archivo.getName());
            }
        }
        Collections.reverse(listaFechasArchivos);
        return listaFechasArchivos;
    }

    //COMPARTIR ARCHIVOS PARA GNUHEALTH
    public void compartirUnificado(ArrayList<String> categoriasPersona, ArrayList<String> familiaCabecera, Context view_compartir){
        ArrayList<String> fechas = this.listadoFechasArchivo();

        ArrayList<ObjetoFamilia> datosFamilias = new ArrayList<>();
        ArrayList<ObjetoPersona> datosPersonas = new ArrayList<>();

        SQLitePpal admin = new SQLitePpal(context, "DATA_PRINCIPAL", null, 1);
        admin.CrearTablaUnificados();
        SQLiteDatabase Bd1 = admin.getWritableDatabase();
        String DNIencuestador = admin.ObtenerDniActivado();

        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr");
        nuevaCarpeta.mkdirs();

        File nuevaCarpetaUnificado = new File(getExternalStorageDirectory(), "RelevAr/RECORRIDOS");
        nuevaCarpetaUnificado.mkdirs();
        String NombreArchivoUnificado = "UNIFICADO.csv";
        File dirUnificado = new File(nuevaCarpetaUnificado, NombreArchivoUnificado);
        if(dirUnificado.exists()){
            dirUnificado.delete();
        }
        String cabUnificado = "CALLE;NUMERO;COORDENADAS;ESTADO;GRUPO FAMILIAR;MENORES;MAYORES;DNI;APELLIDO;NOMBRE;FECHA DE NACIMIENTO;SEXO;NUMERO DE CASA CARTOGRAFIA;QR;" +
                "TIPO DE VIVIENDA;DUEÑO DE LA VIVIENDA;CANTIDAD DE PIEZAS;LUGAR PARA COCINAR;USA PARA COCINAR;" +
                "MATERIAL PREDOMINANTE EN LAS PAREDES EXTERIORES;REVESTIMIENTO EXTERNO O REVOQUE;MATERIAL DE LOS PISOS;" +
                "CIELORRASO;MATERIAL PREDOMINANTE EN LA CUBIERTA EXTERIOR DEL TECHO;AGUA;ORIGEN AGUA;EXCRETAS;ELECTRICIDAD;" +
                "GAS;ALMACENA AGUA DE LLUVIA;ÁRBOLES;BAÑO;EL BAÑO TIENE;NIEVE Y/O HIELO EN LA CALLE;PERROS SUELTOS;TELEFONO FAMILIAR;" +
                "CELULAR;FIJO;MAIL;FACTORES DE RIESGO;EFECTOR;OBSERVACIONES;NOMBRE Y APELLIDO;TELEFONO CONTACTO;PARENTEZCO;INGRESO Y OCUPACION;" +
                "EDUCACION;VITAMINA D;FECHA PROBABLE DE PARTO;ULTIMO CONTROL DE EMBARAZO;ENFERMEDAD ASOCIADA AL EMBARAZO;" +
                "CERTIFICADO UNICO DE DISCAPACIDAD;TIPO DE DISCAPACIDAD;ACOMPAÑAMIENTO;TRASTORNOS EN NIÑOS;ADICCIONES;" +
                "ACTIVIDADES DE OCIO;¿DONDE REALIZA LAS ACTIVIDADES?;TIPO DE VIOLENCIA;MODALIDAD DE LA VIOLENCIA;" +
                "TRASTORNOS MENTALES;ENFERMEDADES CRONICAS;PLAN SOCIAL\n";
        String [] vectorCabeceraUnificado = cabUnificado.split(";");

        //el true es para que se agreguen los datos al final sin perder los datos anteriores
        String escribir = "";
        try {
            FileOutputStream fOutUnificado = new FileOutputStream(dirUnificado, true);
            OutputStreamWriter myOutWriterUnificado = new OutputStreamWriter(fOutUnificado);
            myOutWriterUnificado.append(cabUnificado);

            for (int j=0; j<fechas.size();j++) {

                if (!admin.ExisteFechaUnificados(fechas.get(j))){
                    String NombreArchivo = "RelevAr-" + fechas.get(j);
                    //Toast.makeText(context, NombreArchivo, Toast.LENGTH_LONG).show();
                    File dir = new File(nuevaCarpeta, NombreArchivo);
                    String[] cabecera;
                    //String datosFamilia="";
                    try {
                        FileInputStream fis = new FileInputStream(dir);
                        DataInputStream in = new DataInputStream(fis);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));

                        //
                        cabecera = br.readLine().split(";");

                        String myData;
                        while ((myData = br.readLine()) != null) {

                            String[] Datos = myData.split(";");
                            datosPersonas.add(new ObjetoPersona(categoriasPersona));
                            datosFamilias.add(new ObjetoFamilia(familiaCabecera));

                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("CALLE", Datos[0]);
                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("NUMERO", Datos[1]);
                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("COORDENADAS", Datos[2]);
                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("ESTADO", Datos[3]);
                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("GRUPO FAMILIAR", Datos[4]);
                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("MENORES", Datos[5]);
                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("MAYORES", Datos[6]);

                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("FECHA_REGISTRO", fechas.get(j));
                            datosFamilias.get(datosFamilias.size() - 1).Valores.put("DNI", Datos[7]);

                            datosPersonas.get(datosPersonas.size() - 1).Valores.put("APELLIDO", Datos[8]);
                            datosPersonas.get(datosPersonas.size() - 1).Valores.put("NOMBRE", Datos[9]);
                            datosPersonas.get(datosPersonas.size() - 1).Valores.put("FECHA DE NACIMIENTO", Datos[10]);
                            datosPersonas.get(datosPersonas.size() - 1).Valores.put("SEXO", Datos[11]);
                            datosPersonas.get(datosPersonas.size() - 1).Valores.put("QR", Datos[12]);
                            if(Datos.length>13){
                                datosFamilias.get(datosFamilias.size() - 1).Valores.put("NUMERO DE CASA CARTOGRAFIA", Datos[13]);
                            }else{
                                datosFamilias.get(datosFamilias.size() - 1).Valores.put("NUMERO DE CASA CARTOGRAFIA", "");
                            }

                            if (Datos.length > 13) {

                                for (int i = 14; i < Datos.length; i++) {
                                    if (cabecera[i].equals("MATERIAL PREDOMINANTE EN LA CUBIERTA EXTERIOR DEL TECHO")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("TECHO", Datos[i]);
                                    }
                                    if (cabecera[i].equals("USA PARA COCINAR...")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("USA PARA COCINAR", Datos[i]);
                                    }
                                    if (cabecera[i].equals("NIEVE Y/O HIELO EN LA CALLE")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("NIEVE Y/O HIELO EN LA CALLE", Datos[i]);
                                    }
                                    if (cabecera[i].equals("CELULAR")) {
                                        //Toast.makeText(context, Datos[i] , Toast.LENGTH_SHORT).show();
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("CELULAR", Datos[i]);
                                    }
                                    if (cabecera[i].equals("FIJO")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("FIJO", Datos[i]);
                                    }
                                    if (cabecera[i].equals("MAIL")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("MAIL", Datos[i]);
                                    }
                                    if (cabecera[i].equals("NOMBRE Y APELLIDO")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("NOMBRE Y APELLIDO", Datos[i]);
                                    }
                                    if (cabecera[i].equals("TELEFONO CONTACTO")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("TELEFONO CONTACTO", Datos[i]);
                                    }
                                    if (cabecera[i].equals("PARENTEZCO")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("PARENTEZCO", Datos[i]);
                                    }
                                    if (cabecera[i].equals("DIA/MES/AÑO")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("FECHA PROBABLE DE PARTO", Datos[i]);
                                    }
                                    if (cabecera[i].equals("ULTIMO CONTROL")) {
                                        datosFamilias.get(datosFamilias.size() - 1).Valores.put("ULTIMO CONTROL DE EMBARAZO", Datos[i]);
                                    }
                                    if (EsDeFamilia(cabecera[i],familiaCabecera)) {
                                        if (!Datos[i].equals("")) {
                                            datosFamilias.get(datosFamilias.size() - 1).Valores.put(cabecera[i], Datos[i]);
                                        }
                                    }
                                    if (EsDePersona(cabecera[i],categoriasPersona)) {
                                        if (!Datos[i].equals("")) {
                                            datosPersonas.get(datosPersonas.size() - 1).Valores.put(cabecera[i], Datos[i]);
                                        }
                                    }
                                }
                            }
                        }

                        br.close();
                        in.close();
                        fis.close();
                    } catch (IOException e) {
                        //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    //HASTA ACA LEI TODOS LOS DATOS DE UN ARCHIVO
                    HashMap<String, String> unificado = new HashMap<>();
                    for (int k = 0; k < datosFamilias.size(); k++) {
                        unificado.clear();
                        unificado.putAll(datosPersonas.get(k).Valores);
                        unificado.putAll(datosFamilias.get(k).Valores);
                        //Toast.makeText(context, unificado.get("NIEVE Y/O HIELO EN LA CALLE"), Toast.LENGTH_SHORT).show();
                        for (int l = 0; l < vectorCabeceraUnificado.length - 1; l++) {
                            if(unificado.get(vectorCabeceraUnificado[l]) != null || l==23){
                                if(vectorCabeceraUnificado[l].equals("MATERIAL PREDOMINANTE EN LA CUBIERTA EXTERIOR DEL TECHO")){
                                        escribir += unificado.get("TECHO") + ";";}
                                else{
                                        escribir += unificado.get(vectorCabeceraUnificado[l]) + ";";}
                                    }
                            else{escribir += ";";}
                        }
                        if(unificado.containsKey(vectorCabeceraUnificado[vectorCabeceraUnificado.length - 1].replace("\n", ""))){
                        escribir += unificado.get(vectorCabeceraUnificado[vectorCabeceraUnificado.length - 1].replace("\n", ""));}
                        //Toast.makeText(context, vectorCabeceraUnificado[vectorCabeceraUnificado.length - 1], Toast.LENGTH_SHORT).show();
                        escribir+="\n";

                    }
                    datosFamilias.clear();
                    datosPersonas.clear();

                    ContentValues registro = new ContentValues();
                    registro.put("FECHA", fechas.get(j));
                    //Bd1.insert("UNIFICADOS", null, registro);
                }
            }
            myOutWriterUnificado.append(escribir);
            myOutWriterUnificado.close();
            fOutUnificado.close();
        } catch (IOException e) {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        view_compartir.startActivity(Intent.createChooser(compartirUnificado(), "SUBIR ARCHIVO"));
    }

    // Corroborar si el dato corresponde a un dato de la familia
    private boolean EsDeFamilia(String valor, ArrayList<String> familiaCabecera){
        valor=valor.replace(" ", "_");
        valor=valor.replace("/","");
        valor=valor.replace("...","");
        boolean devolver = false;
        ObjetoFamilia aux = new ObjetoFamilia(familiaCabecera);
        for(int i=0; i<aux.DatosEnviar.size();i++){
            if(valor.equals(aux.DatosEnviar.get(i))){
                devolver = true;
            }
        }
        return devolver;
    }

    // Corroborar si el dato corresponde a un dato de la persona
    private boolean EsDePersona(String valor, ArrayList<String> categoriasPersona){
        valor=valor.replace(" ", "_");
        valor=valor.replace("¿","");
        valor=valor.replace("?","");
        boolean devolver = false;
        ObjetoPersona aux = new ObjetoPersona(categoriasPersona);
        for(int i=0; i<aux.DatosEnviar.size();i++){
            if(valor.equals(aux.DatosEnviar.get(i))){
                devolver = true;
            }
        }
        return devolver;
    }

    private Intent compartirUnificado(){
        File nuevaCarpeta = new File(getExternalStorageDirectory(), "RelevAr/RECORRIDOS");
        nuevaCarpeta.mkdirs();

        File dir = new File(nuevaCarpeta,"UNIFICADO.csv");
        Uri path = FileProvider.getUriForFile(context, "com.example.relevar", dir);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "UNIFICADO.csv");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Valores.");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        return emailIntent;
        //this.startActivity(Intent.createChooser(emailIntent, "SUBIR ARCHIVO"));
    }
}
