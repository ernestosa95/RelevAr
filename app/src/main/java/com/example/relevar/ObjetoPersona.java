package com.example.relevar;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.relevar.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;

public class ObjetoPersona implements Serializable {

    public String Nombre;
    public String Apellido;
    public String DNI;
    public String Sexo;
    public String QR;
    public String Celular;
    public String Fijo;
    public String Mail;
    public String Nacimiento;
    public String Edad;
    public String FactoresDeRiesgo;
    public String Vacunas;
    public String LoteVacuna;
    public String CodfigoFactorRiesgo;
    public String UnidadEdad;
    public String Efector;
    public String Observaciones;
    public String Limpieza;
    public String NombreContacto;
    public String TelefonoContacto;
    public String ParentezcoContacto;
    public String Ocupacion;
    public String Educacion;
    public String Temperatura;
    public String Tos;
    public String Garganta;
    public String Respiratorio;
    public String Disgeusia;
    public String Anosmia;
    public String Vitamina;
    public String FechaParto;
    public String UltimoControlEmbarazo;
    public String EnfermedadEmbarazo;
    public String CertificadoDiscapacidad;
    public String TipoDiscapacidad;
    public String Acompañamiento;
    public String TrastornoNiños;
    public String Adicciones;
    public String ActividadesOcio;
    public String LugarOcio;
    public String TipoViolencia;
    public String ModalidadViolencia;
    public String TrastornosMentales;
    public String EnfermedadCronica;
    public String PlanSocial;

    public HashMap<String, String> Valores = new HashMap<>();
    public HashMap<String, String> datosEditar = new HashMap<>();
    public ArrayList<String> DatosEnviar = new ArrayList<>();

    private ArrayList<String> cabeceraPersona = new ArrayList<>();
    private HashMap<String,String> datosIngresados = new HashMap<>();




    public void setInfoPersonal(String nombre, String apellido, String dni){
        Nombre=nombre;
        Apellido=apellido;
        DNI=dni;
    }

    public ObjetoPersona(ArrayList<String> cabeceraPersonaIngresada) {

        cabeceraPersona = cabeceraPersonaIngresada;
        Nombre="";
        Sexo="";
        Apellido="";
        QR="";
        Celular="";
        Fijo="";
        Mail="";
        Edad="";
        FactoresDeRiesgo="";
        Vacunas="";
        LoteVacuna="";
        CodfigoFactorRiesgo="";
        UnidadEdad="";
        Efector="";
        Observaciones="";
        Limpieza="";
        NombreContacto="";
        TelefonoContacto="";
        ParentezcoContacto="";
        Ocupacion="";
        Educacion="";
        Temperatura="";
        Tos="";
        Garganta="";
        Respiratorio="";
        Anosmia="";
        Disgeusia="";
        Vitamina="";
        FechaParto="";
        UltimoControlEmbarazo="";
        EnfermedadEmbarazo="";
        CertificadoDiscapacidad="";
        TipoDiscapacidad="";
        Acompañamiento="";
        TrastornoNiños="";
        Adicciones="";
        ActividadesOcio="";
        LugarOcio="";
        TipoViolencia="";
        ModalidadViolencia="";
        TrastornosMentales="";
        EnfermedadCronica="";
        PlanSocial="";

        // Para centralizacion
        Valores.put("COORDENADAS","");
        Valores.put("EDAD","");
        Valores.put("SEXO","");

        DatosEnviar.add("FACTORES_DE_RIESGO");
        DatosEnviar.add("EFECTOR");
        DatosEnviar.add("OBSERVACIONES");
        DatosEnviar.add("INGRESO_Y_OCUPACION");
        DatosEnviar.add("EDUCACION");
        DatosEnviar.add("VITAMINA_D");
        DatosEnviar.add("ULTIMO_CONTROL");
        DatosEnviar.add("ENFERMEDAD_ASOCIADA_AL_EMBARAZO");
        DatosEnviar.add("CERTIFICADO_UNICO_DE_DISCAPACIDAD");
        DatosEnviar.add("TIPO_DE_DISCAPACIDAD");
        DatosEnviar.add("ACOMPAÑAMIENTO");
        DatosEnviar.add("TRASTORNOS_EN_NIÑOS");
        DatosEnviar.add("ADICCIONES");
        DatosEnviar.add("ACTIVIDADES_DE_OCIO");
        DatosEnviar.add("DONDE_REALIZA_LAS_ACTIVIDADES");
        DatosEnviar.add("TIPO_DE_VIOLENCIA");
        DatosEnviar.add("MODALIDAD_DE_LA_VIOLENCIA");
        DatosEnviar.add("TRASTORNOS_MENTALES");
        DatosEnviar.add("ENFERMEDADES_CRONICAS");
        DatosEnviar.add("PLAN_SOCIAL");

        for (int i=0; i<DatosEnviar.size(); i++){
            Valores.put(DatosEnviar.get(i),"");
        }

        datosEditar.put("DNI","");
        datosEditar.put("APELLIDO","");
        datosEditar.put("NOMBRE","");
        datosEditar.put("FECHA DE NACIMIENTO","");
        datosEditar.put("SEXO","");
        for (int i=0; i<cabeceraPersona.size(); i++){
            datosEditar.put(cabeceraPersona.get(i),"");
        }

        DNI=datosEditar.get("DNI");
        Nombre=datosEditar.get("NOMBRE");;
        Sexo=datosEditar.get("SEXO");;
        Apellido=datosEditar.get("APELLIDO");;
        Celular=datosEditar.get("CELULAR");;
        Fijo=datosEditar.get("FIJO");;
        Mail=datosEditar.get("MAIL");
        Edad=datosEditar.get("FECHA DE NACIMIENTO");
        FactoresDeRiesgo=datosEditar.get("FACTORES DE RIESGO");
        Vacunas="";
        LoteVacuna="";
        CodfigoFactorRiesgo="";
        UnidadEdad="";
        Efector=datosEditar.get("EFECTOR");
        Observaciones=datosEditar.get("OBSERVACIONES");
        Limpieza="";
        NombreContacto=datosEditar.get("NOMBRE Y APELLIDO");;
        TelefonoContacto=datosEditar.get("TELEFONO CONTACTO");
        ParentezcoContacto=datosEditar.get("PARENTEZCO");;
        Ocupacion=datosEditar.get("INGRESO Y OCUPACION");;
        Educacion=datosEditar.get("EDUCACION");;
        Temperatura="";
        Tos="";
        Garganta="";
        Respiratorio="";
        Anosmia="";
        Disgeusia="";
        Vitamina="";
        FechaParto=datosEditar.get("DIA/MES/AÑO");
        UltimoControlEmbarazo=datosEditar.get("ULTIMO CONTROL");
        EnfermedadEmbarazo=datosEditar.get("ENFERMEDAD ASOCIADA AL EMBARAZO");
        CertificadoDiscapacidad=datosEditar.get("CERTIFICADO UNICO DE DISCAPACIDAD");
        TipoDiscapacidad=datosEditar.get("TIPO DE DISCAPACIDAD");;
        Acompañamiento=datosEditar.get("ACOMPAÑAMIENTO");;
        TrastornoNiños=datosEditar.get("TRASTORNOS EN NIÑOS");
        Adicciones=datosEditar.get("ADICCIONES");
        ActividadesOcio=datosEditar.get("ACTIVIDADES DE OCIO");;
        LugarOcio=datosEditar.get("¿DONDE REALIZA LAS ACTIVIDADES?");
        TipoViolencia=datosEditar.get("TIPO DE VIOLENCIA");;
        ModalidadViolencia=datosEditar.get("MODALIDAD DE LA VIOLENCIA");;
        TrastornosMentales=datosEditar.get("TRASTORNOS MENTALES");;
        EnfermedadCronica=datosEditar.get("ENFERMEDADES CRONICAS");;
        PlanSocial=datosEditar.get("PLAN SOCIAL");;
    }

    public void cargar_datos(){
        DNI=datosEditar.get("DNI");
        Nombre=datosEditar.get("NOMBRE");;
        Sexo=datosEditar.get("SEXO");;
        Apellido=datosEditar.get("APELLIDO");;
        Celular=datosEditar.get("CELULAR");;
        Fijo=datosEditar.get("FIJO");;
        Mail=datosEditar.get("MAIL");
        Edad=datosEditar.get("FECHA DE NACIMIENTO");
        FactoresDeRiesgo=datosEditar.get("FACTORES DE RIESGO");
        Vacunas="";
        LoteVacuna="";
        CodfigoFactorRiesgo="";
        UnidadEdad="";
        Efector=datosEditar.get("EFECTOR");
        Observaciones=datosEditar.get("OBSERVACIONES");
        Limpieza="";
        NombreContacto=datosEditar.get("NOMBRE Y APELLIDO");;
        TelefonoContacto=datosEditar.get("TELEFONO CONTACTO");
        ParentezcoContacto=datosEditar.get("PARENTEZCO");;
        Ocupacion=datosEditar.get("INGRESO Y OCUPACION");;
        Educacion=datosEditar.get("EDUCACION");;
        Temperatura="";
        Tos="";
        Garganta="";
        Respiratorio="";
        Anosmia="";
        Disgeusia="";
        Vitamina="";
        FechaParto=datosEditar.get("DIA/MES/AÑO");
        UltimoControlEmbarazo=datosEditar.get("ULTIMO CONTROL");
        EnfermedadEmbarazo=datosEditar.get("ENFERMEDAD ASOCIADA AL EMBARAZO");
        CertificadoDiscapacidad=datosEditar.get("CERTIFICADO UNICO DE DISCAPACIDAD");
        TipoDiscapacidad=datosEditar.get("TIPO DE DISCAPACIDAD");;
        Acompañamiento=datosEditar.get("ACOMPAÑAMIENTO");;
        TrastornoNiños=datosEditar.get("TRASTORNOS EN NIÑOS");
        Adicciones=datosEditar.get("ADICCIONES");
        ActividadesOcio=datosEditar.get("ACTIVIDADES DE OCIO");;
        LugarOcio=datosEditar.get("¿DONDE REALIZA LAS ACTIVIDADES?");
        TipoViolencia=datosEditar.get("TIPO DE VIOLENCIA");;
        ModalidadViolencia=datosEditar.get("MODALIDAD DE LA VIOLENCIA");;
        TrastornosMentales=datosEditar.get("TRASTORNOS MENTALES");;
        EnfermedadCronica=datosEditar.get("ENFERMEDADES CRONICAS");;
        PlanSocial=datosEditar.get("PLAN SOCIAL");;
    }

    public String FormatoGuardar(){
        // DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;LOTE DE VACUNA
        String guardar=DNI+";"+Apellido+";"+Nombre+";"+Edad+";"+UnidadEdad+";"+Nacimiento+";"+Efector+";"+FactoresDeRiesgo+
                ";"+CodfigoFactorRiesgo+";"+Vacunas+";"+LoteVacuna+";"+Celular+";"+Fijo+
                ";"+Mail+";"+Observaciones+";"+Limpieza+";"+NombreContacto+";"+TelefonoContacto+
                ";"+ParentezcoContacto+";"+Ocupacion+";"+Educacion;
        return guardar;
    }


    public String FormatoGuardarDengue(){
        // DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;LOTE DE VACUNA
        String guardar=DNI+";"+Apellido+";"+Nombre;
        return guardar;
    }

    public String[] CamposCsv(){
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
    return cabecera_recortada;
    }

    public ArrayList<String> DatosCargadosCsv(){
        ArrayList<String> cabecera = new ArrayList<>();
        if(Celular.length()!=0){
            cabecera.add(cabeceraPersona.get(0));
            datosIngresados.put(cabeceraPersona.get(0), Celular);
        }
        if(Fijo.length()!=0){
            cabecera.add(cabeceraPersona.get(1));
            datosIngresados.put(cabeceraPersona.get(1), Fijo);
        }
        if(Mail.length()!=0){
            cabecera.add(cabeceraPersona.get(2));
            datosIngresados.put(cabeceraPersona.get(2), Mail);
        }
        if(FactoresDeRiesgo.length()!=0){
            cabecera.add(cabeceraPersona.get(3));
            datosIngresados.put(cabeceraPersona.get(3), FactoresDeRiesgo);
        }
        if(Efector.length()!=0){
            cabecera.add(cabeceraPersona.get(4));
            datosIngresados.put(cabeceraPersona.get(4), Efector);
        }
        if(Observaciones.length()!=0){
            cabecera.add(cabeceraPersona.get(5));
            datosIngresados.put(cabeceraPersona.get(5), Observaciones);
        }
        if(NombreContacto.length()!=0){
            cabecera.add(cabeceraPersona.get(6));
            datosIngresados.put(cabeceraPersona.get(6), NombreContacto);
        }
        if(TelefonoContacto.length()!=0){
            cabecera.add(cabeceraPersona.get(7));
            datosIngresados.put(cabeceraPersona.get(7), TelefonoContacto);
        }
        if(ParentezcoContacto.length()!=0){
            cabecera.add(cabeceraPersona.get(8));
            datosIngresados.put(cabeceraPersona.get(8), ParentezcoContacto);
        }
        if(Ocupacion.length()!=0){
            cabecera.add(cabeceraPersona.get(9));
            datosIngresados.put(cabeceraPersona.get(9), Ocupacion);
        }
        if(Educacion.length()!=0){
            cabecera.add(cabeceraPersona.get(10));
            datosIngresados.put(cabeceraPersona.get(10), Educacion);
        }
        if(Vitamina.length()!=0){
            cabecera.add(cabeceraPersona.get(11));
            datosIngresados.put(cabeceraPersona.get(11), Vitamina);
        }
        if(FechaParto.length()!=0){
            cabecera.add(cabeceraPersona.get(12));
            datosIngresados.put(cabeceraPersona.get(12), FechaParto);
        }
        if(UltimoControlEmbarazo.length()!=0){
            cabecera.add(cabeceraPersona.get(13));
            datosIngresados.put(cabeceraPersona.get(13), UltimoControlEmbarazo);
        }
        if(EnfermedadEmbarazo.length()!=0){
            cabecera.add(cabeceraPersona.get(14));
            datosIngresados.put(cabeceraPersona.get(14), EnfermedadEmbarazo);
        }
        if(CertificadoDiscapacidad.length()!=0){
            cabecera.add(cabeceraPersona.get(15));
            datosIngresados.put(cabeceraPersona.get(15), CertificadoDiscapacidad);
        }
        if(TipoDiscapacidad.length()!=0){
            cabecera.add(cabeceraPersona.get(16));
            datosIngresados.put(cabeceraPersona.get(16), TipoDiscapacidad);
        }
        if(Acompañamiento.length()!=0){
            cabecera.add(cabeceraPersona.get(17));
            datosIngresados.put(cabeceraPersona.get(17), Acompañamiento);
        }
        if(TrastornoNiños.length()!=0){
            cabecera.add(cabeceraPersona.get(18));
            datosIngresados.put(cabeceraPersona.get(18), TrastornoNiños);
        }
        if(Adicciones.length()!=0){
            cabecera.add(cabeceraPersona.get(19));
            datosIngresados.put(cabeceraPersona.get(19), Adicciones);
        }
        if(ActividadesOcio.length()!=0){
            cabecera.add(cabeceraPersona.get(20));
            datosIngresados.put(cabeceraPersona.get(20), ActividadesOcio);
        }
        if(LugarOcio.length()!=0){
            cabecera.add(cabeceraPersona.get(21));
            datosIngresados.put(cabeceraPersona.get(21), LugarOcio);
        }
        if(TipoViolencia.length()!=0){
            cabecera.add(cabeceraPersona.get(22));
            datosIngresados.put(cabeceraPersona.get(22), TipoViolencia);
        }
        if(ModalidadViolencia.length()!=0){
            cabecera.add(cabeceraPersona.get(23));
            datosIngresados.put(cabeceraPersona.get(23), ModalidadViolencia);
        }
        if(TrastornosMentales.length()!=0){
            cabecera.add(cabeceraPersona.get(24));
            datosIngresados.put(cabeceraPersona.get(24), TrastornosMentales);
        }
        if(EnfermedadCronica.length()!=0){
            cabecera.add(cabeceraPersona.get(25));
            datosIngresados.put(cabeceraPersona.get(25), EnfermedadCronica);
        }
        if(PlanSocial.length()!=0){
            cabecera.add(cabeceraPersona.get(26));
            datosIngresados.put(cabeceraPersona.get(26), PlanSocial);
        }
        return cabecera;
    }

    public HashMap<String, String> DatosIgresados(){
        /* Esta funcion necesita para llenar el hassmap de la ejecucion de la funcion DatosCargadosCsv*/
        datosIngresados.put("DNI", DNI);
        datosIngresados.put("NOMBRE", Nombre);
        datosIngresados.put("APELLIDO", Apellido);
        datosIngresados.put("FECHA DE NACIMIENTO", Nacimiento);
        datosIngresados.put("SEXO", Sexo);
        datosIngresados.put("QR", QR);

        return datosIngresados;
    }

    public void CalcularEdad(int year, int month, int day){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = new Date();
        String fecha1 = dateFormat.format(date1);
        String[] partes = fecha1.split("-");
        int anioactual = Integer.parseInt(partes[0]);
        int mesactual = Integer.parseInt(partes[1]);
        int diaactual = Integer.parseInt(partes[2]);
        java.util.Date Actual = new Date(anioactual, mesactual, diaactual);
        if(Nacimiento!="mm dd, aaaa"){
            java.util.Date Nacimiento = new Date(year, month, day);
            long diferencia = Actual.getTime() - Nacimiento.getTime();
            long segsMilli = 1000;
            long minsMilli = segsMilli * 60;
            long horasMilli = minsMilli * 60;
            long diasMilli = horasMilli * 24;
            long mesesMillo = diasMilli * 30;
            long añosMilli = diasMilli * 365;
            long AñosTranscurridos = diferencia / añosMilli;
            if(AñosTranscurridos<2){
                long MesesTranscurridos = diferencia / mesesMillo;
                Edad=Long.toString(MesesTranscurridos);
                UnidadEdad="MESES";
            }
            else {Edad=Long.toString(AñosTranscurridos);
                UnidadEdad="AÑOS";}
        }else Edad="";
    }

    public void CargarInformacion(HashMap<String, String> datos){

    }
}

