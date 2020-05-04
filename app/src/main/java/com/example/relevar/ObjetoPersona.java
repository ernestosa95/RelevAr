package com.example.relevar;

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
        Nacimiento="";
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
}

