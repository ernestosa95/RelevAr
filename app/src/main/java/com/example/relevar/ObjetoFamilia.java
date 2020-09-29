package com.example.relevar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.relevar.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ObjetoFamilia{
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
    private ArrayList<String> cabeceraFamilia = new ArrayList<String>();
    private HashMap<String, String> datosIngresados = new HashMap<>();


    public ObjetoFamilia(ArrayList<String> aux) {
        cabeceraFamilia = aux;
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


    public ArrayList<String> DatosCargadosCsv(){
        ArrayList<String> cabecera = new ArrayList<>();
            if(TipoVivienda.length()!=0){
                cabecera.add(cabeceraFamilia.get(0));
                datosIngresados.put(cabeceraFamilia.get(0), TipoVivienda);
            }
            if(DueñoVivienda.length()!=0){
                cabecera.add(cabeceraFamilia.get(1));
                datosIngresados.put(cabeceraFamilia.get(1), DueñoVivienda);
            }
            if(CantidadPiezas.length()!=0){
                cabecera.add(cabeceraFamilia.get(2));
                datosIngresados.put(cabeceraFamilia.get(2), CantidadPiezas);
            }
            if(LugarCocinar.length()!=0){
                cabecera.add(cabeceraFamilia.get(3));
                datosIngresados.put(cabeceraFamilia.get(3), LugarCocinar);
            }
            if(UsaParaCocinar.length()!=0){
                cabecera.add(cabeceraFamilia.get(4));
                datosIngresados.put(cabeceraFamilia.get(4), UsaParaCocinar);
            }
            if(Paredes.length()!=0){
                cabecera.add(cabeceraFamilia.get(5));
                datosIngresados.put(cabeceraFamilia.get(5), Paredes);
            }
            if(Revoque.length()!=0){
                cabecera.add(cabeceraFamilia.get(6));
                datosIngresados.put(cabeceraFamilia.get(6), Revoque);
            }
            if(Pisos.length()!=0){
                cabecera.add(cabeceraFamilia.get(7));
                datosIngresados.put(cabeceraFamilia.get(7), Pisos);
            }
            if(Cielorraso.length()!=0){
                cabecera.add(cabeceraFamilia.get(8));
                datosIngresados.put(cabeceraFamilia.get(8), Cielorraso);
            }
            if(Techo.length()!=0){
                cabecera.add(cabeceraFamilia.get(9));
                datosIngresados.put(cabeceraFamilia.get(9), Techo);
            }
            if(Agua.length()!=0){
                cabecera.add(cabeceraFamilia.get(10));
                datosIngresados.put(cabeceraFamilia.get(10), Agua);
            }
            if(AguaOrigen.length()!=0){
                cabecera.add(cabeceraFamilia.get(11));
                datosIngresados.put(cabeceraFamilia.get(11), AguaOrigen);
            }
            if(Excretas.length()!=0){
                cabecera.add(cabeceraFamilia.get(12));
                datosIngresados.put(cabeceraFamilia.get(12), Excretas);
            }
            if(Electricidad.length()!=0){
                cabecera.add(cabeceraFamilia.get(13));
                datosIngresados.put(cabeceraFamilia.get(13), Electricidad);
            }
            if(Gas.length()!=0){
                cabecera.add(cabeceraFamilia.get(14));
                datosIngresados.put(cabeceraFamilia.get(14), Gas);
            }
            if(AguaLluvia.length()!=0){
                cabecera.add(cabeceraFamilia.get(15));
                datosIngresados.put(cabeceraFamilia.get(15), AguaLluvia);
            }
            if(Arboles.length()!=0){
                cabecera.add(cabeceraFamilia.get(16));
                datosIngresados.put(cabeceraFamilia.get(16), Arboles);
            }
            if(Baño.length()!=0){
                cabecera.add(cabeceraFamilia.get(17));
                datosIngresados.put(cabeceraFamilia.get(17), Baño);
            }
            if(BañoTiene.length()!=0){
                cabecera.add(cabeceraFamilia.get(18));
                datosIngresados.put(cabeceraFamilia.get(18), BañoTiene);
            }
        return cabecera;
    }

    public HashMap<String,String> DatosIngresados(){
        /* Esta funcion necesita de la ejecucion de la anterior para llenar el Hashmap con los
        * datos*/
        return datosIngresados;
    }
}

