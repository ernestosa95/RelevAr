package com.example.relevar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.relevar.R;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ObjetoFamilia implements Serializable {
    public String TipoVivienda;
    public String TelefonoFamiliar;
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

    public HashMap<String,String> Valores = new HashMap<>();
    public ArrayList<String> DatosEnviar = new ArrayList<>();

    private ArrayList<String> cabeceraFamilia = new ArrayList<String>();
    private HashMap<String, String> datosIngresados = new HashMap<>();
    public HashMap<String, String> datosEditar = new HashMap<>();

    // Datos de dengue
    public String Tanques;
    public String Piletas;
    public String Cubiertas;
    public String Canaleta;
    public String Hueco;
    public String Macetas;
    public String RecipientesPlasticos;
    public String Botellas;
    public String ElementosDesuso;
    public String SituacionVivienda;
    public String TipoTrabajo;
    public String TotalTratados;
    public String TotalFocoAedico;
    public String TotalIspeccionado;
    public String Larvicida;
    public String Destruidos;
    public String HieloCalle;
    public String PerrosCalle;

    public ObjetoFamilia(ArrayList<String> aux) {
        cabeceraFamilia = aux;

        Valores.put("COORDENADAS","");
        Valores.put("FECHA_REGISTRO","");
        Valores.put("MENORES","");
        Valores.put("MAYORES","");

        DatosEnviar.add("TIPO_DE_VIVIENDA");
        DatosEnviar.add("DUEÑO_DE_LA_VIVIENDA");
        DatosEnviar.add("CANTIDAD_DE_PIEZAS");
        DatosEnviar.add("LUGAR_PARA_COCINAR");
        DatosEnviar.add("USA_PARA_COCINAR");
        DatosEnviar.add("MATERIAL_PREDOMINANTE_EN_LAS_PAREDES_EXTERIORES");
        DatosEnviar.add("REVESTIMIENTO_EXTERNO_O_REVOQUE");
        DatosEnviar.add("MATERIAL_DE_LOS_PISOS");
        DatosEnviar.add("CIELORRASO");
        DatosEnviar.add("MATERIAL_PREDOMINANTE_EN_LA_CUBIERTA_EXTERIOR");
        DatosEnviar.add("AGUA");
        DatosEnviar.add("ORIGEN_AGUA");
        DatosEnviar.add("EXCRETAS");
        DatosEnviar.add("ELECTRICIDAD");
        DatosEnviar.add("GAS");
        DatosEnviar.add("ALMACENA_AGUA_DE_LLUVIA");
        DatosEnviar.add("ÁRBOLES");
        DatosEnviar.add("BAÑO");
        DatosEnviar.add("EL_BAÑO_TIENE");
        DatosEnviar.add("NIEVE_Y_O_HIELO_EN_LA_CALLE");
        DatosEnviar.add("PERROS_SUELTOS");
        DatosEnviar.add("MENORES");
        DatosEnviar.add("MAYORES");

        for (int i=0; i<DatosEnviar.size(); i++){
            Valores.put(DatosEnviar.get(i),"");
        }

        datosEditar.put("CALLE","");
        datosEditar.put("NUMERO","");
        datosEditar.put("COORDENADAS","");
        datosEditar.put("ESTADO","");
        datosEditar.put("GRUPO FAMILIAR","");
        datosEditar.put("MENORES","");
        datosEditar.put("MAYORES","");
        datosEditar.put("NUMERO CASA CARTOGRAFIA","");

        for (int i=0; i<cabeceraFamilia.size(); i++){
            datosEditar.put(cabeceraFamilia.get(i),"");
        }

        TipoVivienda=datosEditar.get("TIPO DE VIVIENDA");
        DueñoVivienda=datosEditar.get("DUEÑO DE LA VIVIENDA");
        CantidadPiezas=datosEditar.get("CANTIDAD DE PIEZAS");
        LugarCocinar=datosEditar.get("LUGAR PARA COCINAR");
        UsaParaCocinar=datosEditar.get("USA PARA COCINAR...");
        Paredes=datosEditar.get("MATERIAL PREDOMINANTE EN LAS PAREDES EXTERIORES");
        Revoque=datosEditar.get("REVESTIMIENTO EXTERNO O REVOQUE");
        Pisos=datosEditar.get("MATERIAL DE LOS PISOS");
        Cielorraso=datosEditar.get("CIELORRASO");
        Techo=datosEditar.get("MATERIAL PREDOMINANTE EN LA CUBIERTA EXTERIOR DEL TECHO");
        Agua=datosEditar.get("AGUA");
        AguaOrigen=datosEditar.get("ORIGEN AGUA");
        Excretas=datosEditar.get("EXCRETAS");
        Electricidad=datosEditar.get("ELECTRICIDAD");
        Gas=datosEditar.get("GAS");
        AguaLluvia=datosEditar.get("ALMACENA AGUA DE LLUVIA");
        Arboles=datosEditar.get("ÁRBOLES");
        Baño=datosEditar.get("BAÑO");
        BañoTiene=datosEditar.get("EL BAÑO TIENE");
        HieloCalle=datosEditar.get("NIEVE Y/O HIELO EN LA CALLE");
        PerrosCalle=datosEditar.get("PERROS SUELTOS");
        TelefonoFamiliar=datosEditar.get("TELEFONO FAMILIAR");


        // Dengue
        Tanques = "0;0;0";
        Piletas = "0;0;0";
        Cubiertas = "0;0;0";
        Canaleta = "0;0;0";
        Hueco = "0;0;0";
        Macetas = "0;0;0";
        RecipientesPlasticos = "0;0;0";
        Botellas = "0;0;0";
        ElementosDesuso = "0;0;0";
        SituacionVivienda = "";
        TipoTrabajo="";
        TotalFocoAedico="";
        TotalIspeccionado="";
        TotalTratados="";
        Larvicida="";
        Destruidos="";
    }

    public void cargar_datos(){
        TipoVivienda=datosEditar.get("TIPO DE VIVIENDA");
        DueñoVivienda=datosEditar.get("DUEÑO DE LA VIVIENDA");
        CantidadPiezas=datosEditar.get("CANTIDAD DE PIEZAS");
        LugarCocinar=datosEditar.get("LUGAR PARA COCINAR");
        UsaParaCocinar=datosEditar.get("USA PARA COCINAR...");
        Paredes=datosEditar.get("MATERIAL PREDOMINANTE EN LAS PAREDES EXTERIORES");
        Revoque=datosEditar.get("REVESTIMIENTO EXTERNO O REVOQUE");
        Pisos=datosEditar.get("MATERIAL DE LOS PISOS");
        Cielorraso=datosEditar.get("CIELORRASO");
        Techo=datosEditar.get("MATERIAL PREDOMINANTE EN LA CUBIERTA EXTERIOR DEL TECHO");
        Agua=datosEditar.get("AGUA");
        AguaOrigen=datosEditar.get("ORIGEN AGUA");
        Excretas=datosEditar.get("EXCRETAS");
        Electricidad=datosEditar.get("ELECTRICIDAD");
        Gas=datosEditar.get("GAS");
        AguaLluvia=datosEditar.get("ALMACENA AGUA DE LLUVIA");
        Arboles=datosEditar.get("ÁRBOLES");
        Baño=datosEditar.get("BAÑO");
        BañoTiene=datosEditar.get("EL BAÑO TIENE");
        HieloCalle=datosEditar.get("NIEVE Y/O HIELO EN LA CALLE");
        PerrosCalle=datosEditar.get("PERROS SUELTOS");
        TelefonoFamiliar=datosEditar.get("TELEFONO FAMILIAR");
    }

    public String FormatoGuardar(){
        // DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;LOTE DE VACUNA
        String guardar=TipoVivienda+";"+Paredes+";"+Revoque+";"+Techo+";"+Arboles;
        guardar+=";"+Agua+";"+AguaOrigen+";"+AguaLluvia+";"+Electricidad+";"+Gas+";"+Excretas;
        guardar+=";"+CantidadPiezas+";"+DueñoVivienda+";"+Baño+";"+BañoTiene+";"+LugarCocinar+";"+UsaParaCocinar+";"+Pisos+";"+Cielorraso;
        return guardar;
    }

    public String FormatoGuardarDengue(){
        // DNI;APELLIDO;NOMBRE;EDAD;UNIDAD EDAD;FECHA DE NACIMIENTO;EFECTOR;FACTORES DE RIESGO;CODIGO SISA F. DE RIESGO;VACUNAS;LOTE DE VACUNA
        String guardar = SituacionVivienda+";"+Tanques+";"+Piletas+";"+Cubiertas+";"+Canaleta+";"+Hueco+";"+
                Macetas+";"+RecipientesPlasticos+";"+Botellas+";"+ElementosDesuso+";"+TotalIspeccionado+";"+
                TotalTratados+";"+Destruidos+";"+TotalFocoAedico+";"+Larvicida+";"+TipoTrabajo;
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
            if(HieloCalle.length()!=0){
                cabecera.add(cabeceraFamilia.get(19));
                datosIngresados.put(cabeceraFamilia.get(19), HieloCalle);
            }
            if(PerrosCalle.length()!=0){
              cabecera.add(cabeceraFamilia.get(20));
               datosIngresados.put(cabeceraFamilia.get(20), HieloCalle);
            }
            if(TelefonoFamiliar.length()!=0){
               cabecera.add(cabeceraFamilia.get(21));
               datosIngresados.put(cabeceraFamilia.get(21), TelefonoFamiliar);
            }
        return cabecera;
    }

    public HashMap<String,String> DatosIngresados(){
        /* Esta funcion necesita de la ejecucion de la anterior para llenar el Hashmap con los
        * datos*/
        return datosIngresados;
    }
}

