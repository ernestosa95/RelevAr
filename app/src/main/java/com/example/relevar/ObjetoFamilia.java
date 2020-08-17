package com.example.relevar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ObjetoFamilia {
    public String TipoVivienda;
    public String DueñoVivienda;
    public String CantidadPiezas;
    public String LugarCocinar;
    public String UsaParaCocinar;
    public String Paredes;
    public String Revoque;
    public String Pisos;
    public String Techo;
    public String Cielorraso;
    public String Agua;
    public String AguaOrigen;

    public ObjetoFamilia() {
        TipoVivienda="";
        DueñoVivienda="";
        CantidadPiezas="";
        LugarCocinar="";
        UsaParaCocinar="";
        Paredes="";
        Revoque="";
        Pisos="";
        Cielorraso="";
        Techo="";
        Agua="";
        AguaOrigen="";

    }

    public String FormatoGuardar(){
        // DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;LOTE DE VACUNA
        String guardar=TipoVivienda+";"+DueñoVivienda+";"+CantidadPiezas+";"+LugarCocinar+";"+UsaParaCocinar+";"+
                Paredes+";"+Revoque+";"+Pisos+";"+Techo+";"+Cielorraso;
        return guardar;
    }

}

