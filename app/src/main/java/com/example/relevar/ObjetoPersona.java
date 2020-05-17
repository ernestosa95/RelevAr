package com.example.relevar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ObjetoPersona {
    public String Nombre;
    public String Apellido;
    public String DNI;
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

    public void setInfoPersonal(String nombre, String apellido, String dni){
        Nombre=nombre;
        Apellido=apellido;
        DNI=dni;
    }

    public ObjetoPersona() {
        Nombre="";
        Apellido="";
        DNI="";
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
    }

    public String FormatoGuardar(){
        // DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;LOTE DE VACUNA
        String guardar=DNI+";"+Apellido+";"+Nombre+";"+Edad+";"+UnidadEdad+";"+Nacimiento+";"+Efector+
                ";"+FactoresDeRiesgo+";"+CodfigoFactorRiesgo+";"+Vacunas+";"+LoteVacuna+";"+Celular+
                ";"+Fijo+";"+Mail+";"+Observaciones+";"+Limpieza;
        return guardar;
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
}

