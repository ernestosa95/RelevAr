package com.example.relevar.Recursos;

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
    public String Excretas;
    public String Electricidad;
    public String Gas;
    public String AguaLluvia;
    public String Arboles;
    public String Baño;
    public String BañoTiene;

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
        Excretas="";
        Electricidad="";
        Gas="";
        AguaLluvia="";
        Arboles="";
        Baño="";
        BañoTiene="";
    }

    public String FormatoGuardar(){
        // DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;LOTE DE VACUNA
        String guardar=TipoVivienda+";"+Paredes+";"+Revoque+";"+Techo+";"+Arboles;
        guardar+=";"+Agua+";"+AguaOrigen+";"+AguaLluvia+";"+Electricidad+";"+Gas+";"+Excretas;
        guardar+=";"+CantidadPiezas+";"+DueñoVivienda+";"+Baño+";"+BañoTiene+";"+LugarCocinar+";"+UsaParaCocinar+";"+Pisos+";"+Cielorraso;
        return guardar;
    }

}

