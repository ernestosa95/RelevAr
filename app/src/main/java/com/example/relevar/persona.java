package com.example.relevar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class persona extends AppCompatActivity {
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// DEFINICION DE VARIABLES GLOBALES

    // Defino diferentes constantes para el funcionamiento de la activity
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private int STORAGE_PERMISSION_CODE =1;

    // Defino de forma global los String para recibir y devolver la informacion
    private String telefonocelular="", telefonofijo="", direccionmail="", date="DD-MM-AAAA";

    // Defino de manera global los EditText para ingresar informacion
    private EditText dni, Apellido, Nombre, Efector, celular, fijo, mail, obs, lotevacuna;

    // Defino de manera global los TextView para mostrar informacion
    private TextView fecha, tipovacuna, limp, Riesgo, AvRiesgo, Vacuna, Contacto, Observacion, AvVacuna, AvContacto, AvObs;

    // Defino de manera global los Button para realizar acciones
    //private Button ;

    // Defino de manera global los Chekclist para crear los menus de opciones
    CheckBox hepaA, hepaB, bcg, rotavirus, quintuple, tripleviral, varicela, dtp, vph, tripeacelular,
            dt, antigripal, vcn23, vcn13, tetravalente, ipv, sabin;
    CheckBox calendario, embarazo, puerperio, personalsalud, personalesencial, viajeros,
            inmunocomprometidos, cadiologicos, respiratorios, diabeticos, prematuros, asplenicos,
            obesidad, inmunodeficiencia, conviviente,HTA, otros;

    // Defino el Interfaz para ingresar la fecha
    private DatePickerDialog.OnDateSetListener Date;

    // Defino el Spinner que va a contener los efectores
    private Spinner Sp1;

    // Defino los Array que contienen conjunto de informacion
    private ArrayList<String> categorias = new ArrayList<>();

    // Defino los RadioButton para la limpieza
    RadioButton rb1, rb2;

    ObjetoPersona Persona = new ObjetoPersona();

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// CLASES DE CREACION Y ACTUALIZACION DE LAS VARIABLES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

        // Defino los widgets
        dni = (EditText) findViewById(R.id.DNI);
        Apellido = (EditText) findViewById(R.id.APELLIDO);
        Nombre = (EditText) findViewById(R.id.NOMBRE);
        Efector = (EditText) findViewById(R.id.EFECTOR);
        Vacuna = (TextView) findViewById(R.id.VACUNA);
        Riesgo = (TextView) findViewById(R.id.RIESGO);
        Contacto = (TextView) findViewById(R.id.CONTACTO);
        Observacion = (TextView) findViewById(R.id.OBSERVACION);
        fecha=(TextView) findViewById(R.id.fecha);
        AvRiesgo=(TextView) findViewById(R.id.AvanceRiesgo);
        AvVacuna=(TextView) findViewById(R.id.AvanceVacunas);
        AvContacto=(TextView) findViewById(R.id.AvanceContacto);
        AvObs=(TextView) findViewById(R.id.AvanceObservaciones);

        // Defino el Spinner de los efectores
        /*Sp1 = (Spinner) findViewById(R.id.efector);
        categorias.add("CAPs D'ANGELO");
        categorias.add("CAPs ANTARTIDA (CIC)");
        categorias.add("CAPs SAN MARTIN");
        categorias.add("CRR CARRILLO");
        categorias.add("HOSP. SAN ROQUE");
        categorias.add("HOSP. SAN MARTIN");
        categorias.add("HOSP. BAXADA");
        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, categorias);//Cargo el spinner con los datos
        Sp1.setAdapter(comboAdapter);*/

        // Construyo el widget para la fecha
        Date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int  year, int month, int day) {
                //Log.d(TAG, "onDateSet: date:"+year+"/"+month+"/"+day);
                int mes = month + 1;
                date=day+"-"+mes+"-"+year;
                fecha.setText(Integer.toString(mes)+" "+Integer.toString(day)+", "+Integer.toString(year));
                Persona.Nacimiento = date;
                Persona.CalcularEdad(year, month, day);
                }
        };

        // Cargo los datos para que se pueda editar un registro que ya esta hecho
        if((String) getIntent().getStringExtra("DNI")!=null){
        Persona.DNI = (String) getIntent().getStringExtra("DNI");
        Persona.Nombre = (String) getIntent().getStringExtra("NOMBRE");
        Persona.Apellido = (String) getIntent().getStringExtra("APELLIDO");
        Persona.Efector = (String) getIntent().getStringExtra("EFECTOR");
        Persona.Edad = (String) getIntent().getStringExtra("EDAD");
        Persona.UnidadEdad = (String) getIntent().getStringExtra("UNIDADEDAD");
        Persona.FactoresDeRiesgo = (String) getIntent().getStringExtra("FACTORES");
        Persona.Vacunas = (String) getIntent().getStringExtra("VACUNAS");
        Persona.CodfigoFactorRiesgo = (String) getIntent().getStringExtra("CODIGOFACTORES");
        Persona.Celular = (String) getIntent().getStringExtra("CELULAR");
        Persona.Fijo = (String) getIntent().getStringExtra("FIJO");
        Persona.Mail = (String) getIntent().getStringExtra("MAIL");
        Persona.Observaciones = (String) getIntent().getStringExtra("OBSERVACIONES");
        Persona.Nacimiento = (String) getIntent().getStringExtra("NACIMIENTO");
        Persona.Limpieza = (String) getIntent().getStringExtra("LIMPIEZA");
        Persona.LoteVacuna = (String) getIntent().getStringExtra("LOTE");

        // Inicializo los campos de edicion
        dni.setText(Persona.DNI);
        Apellido.setText(Persona.Apellido);
        Nombre.setText(Persona.Nombre);

        //Toast.makeText(this, Persona.Nacimiento, Toast.LENGTH_SHORT).show();
        if(Persona.Nacimiento!=""){
        fecha.setText(Persona.Nacimiento);}
        else {fecha.setText("DD-MM-AAAA");}

        Sp1.setSelection(ObtenerPosicion(Sp1, Persona.Efector));
        }
    }

    /*//@Override
    protected void onStart() {
        //todo esto pa actualizr la listview
        super.onStart();

        // Actualizo los colores de avance
        //ColorAvanceContacto();
        //ColorAvanceFactores();
        //ColorAvanceVacunas();
        //ColorAvanceObservaciones();
    }*/

    // Funcion para obtener la posicion del efector seleccionado dentro del spinner
    public static int ObtenerPosicion(Spinner spinner, String efector) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(efector)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }

    // Se inicializa el scrollbar para la fecha
    public void Fecha (View view){
        Calendar cal=Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, Date, 1955,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// SECCION DE LAS VISTA DE LAS VACUNAS

    // Defino las opciones referidas al AlertDialog de las vacunas
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void TiposVacuna(View view){

        // Inializo el alert dialog, defino el titulo
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("VACUNAS");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check principal
        LinearLayout mainLayout0       = new LinearLayout(this);
        mainLayout0.setOrientation(LinearLayout.VERTICAL);

        // Defino el Layaout que va a contener a los Check principal cabecera
        LinearLayout mainLayout1       = new LinearLayout(this);
        mainLayout1.setOrientation(LinearLayout.VERTICAL);

        // Defino el Layaout que va a contener a los Check
        LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Defino los Checks
        String ColorPares = "#4A4A4A";
        String ColorImpares = "#4588BC";
        int TamañoLetra =20;
        int AltoContenedor = 80;

        // Lote de la vacuna
        LinearLayout layoutvacuna       = new LinearLayout(this);
        layoutvacuna.setOrientation(LinearLayout.HORIZONTAL);
        layoutvacuna.setVerticalGravity(Gravity.CENTER_VERTICAL);
        lotevacuna = new EditText(getApplicationContext());
        lotevacuna.setHint("LOTE VACUNA");
        lotevacuna.setHintTextColor(Color.WHITE);
        lotevacuna.setTextSize(TamañoLetra);
        lotevacuna.setTextColor(Color.WHITE);
        lotevacuna.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layoutvacuna.addView(lotevacuna);
        mainLayout1.addView(layoutvacuna);

        // Tipos de vacuna aplicada
        LinearLayout layoutvacunaaplicada       = new LinearLayout(this);
        layoutvacunaaplicada.setOrientation(LinearLayout.HORIZONTAL);
        layoutvacunaaplicada.setVerticalGravity(Gravity.CENTER_VERTICAL);
        layoutvacunaaplicada.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        tipovacuna = new TextView(getApplicationContext());
        tipovacuna.setText("VACUNA A APLICAR");
        tipovacuna.setGravity(Gravity.CENTER_HORIZONTAL);
        tipovacuna.setBackgroundColor(Color.parseColor("#396F98"));
        tipovacuna.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        tipovacuna.setHintTextColor(Color.WHITE);
        tipovacuna.setTextSize(16);
        tipovacuna.setTextColor(Color.WHITE);
        tipovacuna.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layoutvacunaaplicada.addView(tipovacuna);
        mainLayout1.addView(layoutvacunaaplicada);

        // 0 HEPATITIS A
        hepaA = PersonalCheck(mainLayout,"HEPATITIS A", TamañoLetra, ColorPares, AltoContenedor);

        // 1 HEPATITIS B
        hepaB = PersonalCheck(mainLayout,"HEPATITIS B", TamañoLetra, ColorImpares, AltoContenedor);

        // 2 HEPATITIS BCG
        bcg = PersonalCheck(mainLayout,"BCG", TamañoLetra, ColorPares, AltoContenedor);

        // 3 ROTAVIRUS
        rotavirus = PersonalCheck(mainLayout,"ROTAVIRUS", TamañoLetra, ColorImpares, AltoContenedor);

        // 4 QUINTUPLE
        quintuple = PersonalCheck(mainLayout,"QUINTUPLE", TamañoLetra, ColorPares, AltoContenedor);

        // 5 TRIPLE VIRAL
        tripleviral = PersonalCheck(mainLayout,"TRIPLE VIRAL", TamañoLetra, ColorImpares, AltoContenedor);

        // 6 VARICELA
        varicela = PersonalCheck(mainLayout,"VARICELA", TamañoLetra, ColorPares, AltoContenedor);

        // 7 DTP
        dtp = PersonalCheck(mainLayout,"DTP", TamañoLetra, ColorImpares, AltoContenedor);

        // 8 VPH
        vph = PersonalCheck(mainLayout,"VPH", TamañoLetra, ColorPares, AltoContenedor);

        // 9 TRIPE ACELULAR
        tripeacelular = PersonalCheck(mainLayout,"TRIPE ACELULAR dTpa", TamañoLetra, ColorImpares, AltoContenedor);

        // 10 dT
        dt = PersonalCheck(mainLayout,"dT", TamañoLetra, ColorPares, AltoContenedor);

        // 11 ANTIGRIPAL
        antigripal = PersonalCheck(mainLayout,"ANTIGRIPAL", TamañoLetra, ColorImpares, AltoContenedor);

        // 12 NEUMOCOCO POLISACARIDA 23V
        vcn23 = PersonalCheck(mainLayout,"NEUMOCOCO POLISACARIDA 23V", TamañoLetra, ColorPares, AltoContenedor);

        // 13 NEUMOCOCO CONJUGADA 13V
        vcn13 = PersonalCheck(mainLayout,"NEUMOCOCO CONJUGADA 13V", TamañoLetra, ColorImpares, AltoContenedor);

        // 14 MENINGOCOCICA TETRAVALENTE
        tetravalente = PersonalCheck(mainLayout,"MENINGOCOCICA TETRAVALENTE", TamañoLetra, ColorPares, AltoContenedor);

        // 15 POLIO - IPV
        ipv = PersonalCheck(mainLayout,"POLIO - IPV", TamañoLetra, ColorImpares, AltoContenedor);

        // 16 POLIO - SABIN ORAL
        sabin = PersonalCheck(mainLayout,"POLIO - SABIN ORAL", TamañoLetra, ColorPares, AltoContenedor);


        // En el caso de editar un registro necesito que me ponga seleccionado todos aquellos que
        // ya se habian seleeccionado antes para que los pueda editar
        //Necesito activar los check que ya habia seleccionado antes
        CheckSeleccionadosVacunas(Persona.Vacunas);

        // Seteo si hay cargado un lote de vacuna
        if(Persona.LoteVacuna!=""){lotevacuna.setText(Persona.LoteVacuna);}

        // Defino un ScrollView para visualizar todos
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sv.setVerticalScrollBarEnabled(true);
        sv.addView(mainLayout);

        // Agrego las vistas al Layout principal
        mainLayout0.addView(mainLayout1);
        mainLayout0.addView(sv);
        builder.setView(mainLayout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                String vacunas=null;
                if (hepaA.isChecked()){
                    if (vacunas==null){
                        vacunas="HEPATITIS A";
                    } else vacunas+=",HEPATITIS A";
                }
                if (hepaB.isChecked()){
                    if (vacunas==null){
                        vacunas="HEPATITIS B";
                    } else vacunas+=",HEPATITIS B";
                }
                if (bcg.isChecked()){
                    if (vacunas==null){
                        vacunas="BCG";
                    } else vacunas+=",BCG";
                }
                if (rotavirus.isChecked()){
                    if (vacunas==null){
                        vacunas="ROTAVIRUS";
                    } else vacunas+=",ROTAVIRUS";
                }
                if (quintuple.isChecked()){
                    if (vacunas==null){
                        vacunas="QUINTUPLE";
                    } else vacunas+=",QUINTUPLE";
                }
                if (tripleviral.isChecked()){
                    if (vacunas==null){
                        vacunas="TRIPLE VIRAL";
                    } else vacunas+=",TRIPLE VIRAL";
                }
                if (varicela.isChecked()){
                    if (vacunas==null){
                        vacunas="VARICELA";
                    } else vacunas+=",VARICELA";
                }
                if (dtp.isChecked()){
                    if (vacunas==null){
                        vacunas="DTP";
                    } else vacunas+=",DTP";
                }
                if (vph.isChecked()){
                    if (vacunas==null){
                        vacunas="VPH";
                    } else vacunas+=",VPH";
                }
                if (tripeacelular.isChecked()){
                    if (vacunas==null){
                        vacunas="TRIPE ACELULAR dTpa";
                    } else vacunas+=",TRIPE ACELULAR dTpa";
                }
                if (dt.isChecked()){
                    if (vacunas==null){
                        vacunas="dT";
                    } else vacunas+=",dT";
                }
                if (antigripal.isChecked()){
                    if (vacunas==null){
                        vacunas="ANTIGRIPAL";
                    } else vacunas+=",ANTIGRIPAL";
                }
                if (vcn23.isChecked()){
                    if (vacunas==null){
                        vacunas="NEUMOCOCO POLISACARIDA 23V";
                    } else vacunas+=",NEUMOCOCO POLISACARIDA 23V";
                }
                if (vcn13.isChecked()){
                    if (vacunas==null){
                        vacunas="NEUMOCOCO CONJUGADA 13V";
                    } else vacunas+=",NEUMOCOCO CONJUGADA 13V";
                }
                if (tetravalente.isChecked()){
                    if (vacunas==null){
                        vacunas="MENINGOCOCICA TETRAVALENTE";
                    } else vacunas+=",MENINGOCOCICA TETRAVALENTE";
                }
                if (ipv.isChecked()){
                    if (vacunas==null){
                        vacunas="POLIO - IPV";
                    } else vacunas+=",POLIO - IPV";
                }
                if (sabin.isChecked()){
                    if (vacunas==null){
                        vacunas="POLIO - SABIN ORAL";
                    } else vacunas+=",POLIO - SABIN ORAL";
                }
                if(vacunas!=null){Persona.Vacunas=vacunas;}
                Persona.LoteVacuna=lotevacuna.getText().toString();

                // Cambio los colores de avance
                ColorAvanceVacunas();
            }
        });
        builder.setNegativeButton("CANCELAR", null);

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Cambia lo colores de avance de factores
    private void ColorAvanceVacunas(){
        int avance = 0;
        if (Persona.Vacunas.length()!=0){
            avance+=1;
        }
        if (Persona.LoteVacuna.length()!=0){
            avance+=1;
        }

        if(avance==1){
            //Vacuna.setBackgroundColor(Color.parseColor("#FFA07A"));
            String aux = Integer.toString(avance)+"/2";
            AvVacuna.setText(aux);
            AvVacuna.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==2){
            //Vacuna.setBackgroundColor(Color.parseColor("#8BC34A"));
            AvVacuna.setText("2/2");
            AvVacuna.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }

    // Detecto los Check seleccionados anteriormente
    private void CheckSeleccionadosVacunas(String Vacunas){
        //Toast.makeText(getApplicationContext(), Vacunas, Toast.LENGTH_SHORT).show();
        if(Vacunas!=""){
        String[] vac =Vacunas.split(",");
        for (int x = 0; x < vac.length; x++) {
            //Toast.makeText(getApplicationContext(), Vacunas, Toast.LENGTH_SHORT).show();
            if (vac[x].equals("HEPATITIS A")){
                hepaA.setChecked(true);
            }
            if (vac[x].equals("HEPATITIS B")){
                hepaB.setChecked(true);
            }
            if (vac[x].equals("BCG")){
                bcg.setChecked(true);
            }
            if (vac[x].equals("ROTAVIRUS")){
                rotavirus.setChecked(true);
            }
            if (vac[x].equals("QUINTUPLE")){
                quintuple.setChecked(true);
            }
            if (vac[x].equals("TRIPLE VIRAL")){
                tripleviral.setChecked(true);
            }
            if (vac[x].equals("VARICELA")){
                varicela.setChecked(true);
            }
            if (vac[x].equals("DTP")){
                dtp.setChecked(true);
            }
            if (vac[x].equals("VPH")){
                vph.setChecked(true);
            }
            if (vac[x].equals("TRIPLE ACELULAR dTpa")){
                tripeacelular.setChecked(true);
            }
            if (vac[x].equals("dT")){
                dt.setChecked(true);
            }
            if (vac[x].equals("ANTIGRIPAL")){
                antigripal.setChecked(true);
            }
            if (vac[x].equals("NEUMOCOCO POLISACARIDA 23V")){
                vcn23.setChecked(true);
            }
            if (vac[x].equals("NEUMOCOCO CONJUGADA 13V")){
                vcn13.setChecked(true);
            }
            if (vac[x].equals("MENINGOCOCICA TETRAVALENTE")){
                tetravalente.setChecked(true);
            }
            if (vac[x].equals("POLIO - IPV")){
                ipv.setChecked(true);
            }
            if (vac[x].equals("POLIO - SABIN ORAL")){
                sabin.setChecked(true);
            }
        }
    }}

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// SECCION DE LAS VISTAS DE LOS FACTORES DE RIESGO

    // Defino las opciones referidas al AlertDialog de factores
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void TipoFactores(View view){
    // Defino las caracteristicas
    int TamañoLetra = 20;
    String ColorPares = "#4A4A4A";
    String ColorImpares = "#4588BC";
    int AltoContenedor = 80;

    // Defino los contenedores
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("FACTORES DE RIESGO");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check
        LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        calendario = PersonalCheck(mainLayout,"POR CALENDARIO", TamañoLetra, ColorPares, AltoContenedor);
        embarazo = PersonalCheck(mainLayout,"EMBARAZO", TamañoLetra, ColorImpares, AltoContenedor);
        puerperio = PersonalCheck(mainLayout,"PUERPERIO", TamañoLetra, ColorPares, AltoContenedor);
        personalsalud = PersonalCheck(mainLayout,"PERSONAL DE SALUD", TamañoLetra, ColorImpares, AltoContenedor);
        personalesencial = PersonalCheck(mainLayout,"PERSONAL ESENCIAL", TamañoLetra, ColorPares, AltoContenedor);
        viajeros = PersonalCheck(mainLayout,"VIAJEROS", TamañoLetra, ColorImpares, AltoContenedor);
        inmunocomprometidos = PersonalCheck(mainLayout,"INMUNOCOMPROMETIDOS", TamañoLetra, ColorPares, AltoContenedor);
        cadiologicos = PersonalCheck(mainLayout,"CARDIOLOGICOS", TamañoLetra, ColorImpares, AltoContenedor);
        respiratorios = PersonalCheck(mainLayout,"RESPIRATORIOS", TamañoLetra, ColorPares, AltoContenedor);
        diabeticos = PersonalCheck(mainLayout,"DIABÉTICOS", TamañoLetra, ColorImpares, AltoContenedor);
        prematuros = PersonalCheck(mainLayout,"PREMATUROS", TamañoLetra, ColorPares, AltoContenedor);
        asplenicos = PersonalCheck(mainLayout,"ASPLÉNICOS", TamañoLetra, ColorImpares, AltoContenedor);
        obesidad = PersonalCheck(mainLayout,"OBESIDAD MORBIDA", TamañoLetra, ColorPares, AltoContenedor);
        inmunodeficiencia = PersonalCheck(mainLayout,"INMUNODEFICIENCIA", TamañoLetra, ColorImpares, AltoContenedor);
        conviviente = PersonalCheck(mainLayout,"CONVIVIENTE INMUNOCOMPROMETIDOS", TamañoLetra, ColorPares, AltoContenedor);
        HTA = PersonalCheck(mainLayout,"HIPERTENSO", TamañoLetra, ColorImpares, AltoContenedor);
        otros = PersonalCheck(mainLayout,"OTROS", TamañoLetra, ColorPares, AltoContenedor);

        // En el caso de editar un registro
        // Necesito que me ponga seleccionado todos aquellos que ya se habian seleeccionado antes
        // para que los pueda editar

        //Necesito activar los check que ya habia seleccionado antes
        CheckSeleccionadosFactores(Persona.FactoresDeRiesgo);

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                String riesgos=null;
                String codigoriesgo=null;
                if (calendario.isChecked()){
                    if (riesgos==null){
                        riesgos="POR CALENDARIO";
                        codigoriesgo="1";
                    } else {
                        riesgos+=",POR CALENDARIO";
                        codigoriesgo+=",1";}}
                if (embarazo.isChecked()){
                        if (riesgos==null){
                            riesgos="EMBARAZO";
                            codigoriesgo="2";
                        } else {
                            riesgos+=",EMBARAZO";
                            codigoriesgo+=",2";}}
                if (puerperio.isChecked()){
                    if (riesgos==null){
                        riesgos="PUERPERIO";
                        codigoriesgo="3";
                    } else {
                        riesgos+=",PUERPERIO";
                        codigoriesgo+=",3";}}
                if (personalsalud.isChecked()){
                    if (riesgos==null){
                        riesgos="PERSONAL DE SALUD";
                        codigoriesgo="4";
                    } else {
                        riesgos+=",PERSONAL DE SALUD";
                        codigoriesgo+=",4";}}
                if (personalesencial.isChecked()){
                    if (riesgos==null){
                        riesgos="PERSONAL ESENCIAL";
                        codigoriesgo="5";
                    } else {
                        riesgos+=",PERSONAL ESENCIAL";
                        codigoriesgo+=",5";}}
                if (viajeros.isChecked()){
                    if (riesgos==null){
                        riesgos="VIAJEROS";
                        codigoriesgo="6";
                    } else {
                        riesgos+=",VIAJEROS";
                        codigoriesgo+=",6";}}
                if (inmunocomprometidos.isChecked()){
                    if (riesgos==null){
                        riesgos="INMUNOCOMPROMETIDOS";
                        codigoriesgo="7";
                    } else {
                        riesgos+=",INMUNOCOMPROMETIDOS";
                        codigoriesgo+=",7";}}
                if (cadiologicos.isChecked()){
                    if (riesgos==null){
                        riesgos="CARDIOLOGICOS";
                        codigoriesgo="8";
                    } else {
                        riesgos+=",CARDIOLOGICOS";
                        codigoriesgo+=",8";}}
                if (respiratorios.isChecked()){
                    if (riesgos==null){
                        riesgos="RESPIRATORIOS";
                        codigoriesgo="9";
                    } else {
                        riesgos+=",RESPIRATORIOS";
                        codigoriesgo+=",9";}}
                if (diabeticos.isChecked()){
                    if (riesgos==null){
                        riesgos="DIABETICOS";
                        codigoriesgo="10";
                    } else {
                        riesgos+=",DIABETICOS";
                        codigoriesgo+=",10";}}
                if (prematuros.isChecked()){
                    if (riesgos==null){
                        riesgos="PREMATURO";
                        codigoriesgo="11";
                    } else {
                        riesgos+=",PREMATURO";
                        codigoriesgo+=",11";}}
                if (asplenicos.isChecked()){
                    if (riesgos==null){
                        riesgos="ASPLENICOS";
                        codigoriesgo="12";
                    } else {
                        riesgos+=",ASPLENICOS";
                        codigoriesgo+=",12";}}
                if (obesidad.isChecked()){
                    if (riesgos==null){
                        riesgos="OBESIDAD MORBIDA";
                        codigoriesgo="13";
                    } else {
                        riesgos+=",OBESIDAD MORBIDA";
                        codigoriesgo+=",13";}}
                if (inmunodeficiencia.isChecked()){
                    if (riesgos==null){
                        riesgos="INMUNODEFICIENCIA";
                        codigoriesgo="14";
                    } else {
                        riesgos+=",INMUNODEFICIENCIA";
                        codigoriesgo+=",14";}}
                if (conviviente.isChecked()){
                    if (riesgos==null){
                        riesgos="CONVIVIENTE INMUNOCOMPROMETIDOS";
                        codigoriesgo="15";
                    } else {
                        riesgos+=",CONVIVIENTE INMUNOCOMPROMETIDOS";
                        codigoriesgo+=",15";}}
                if (HTA.isChecked()){
                    if (riesgos==null){
                        riesgos="HIPERTENSO";
                        //codigoriesgo="";
                    } else {
                        riesgos+=",HIPERTENSO";
                        //codigoriesgo+="";
                        }}
                if (otros.isChecked()){
                    if (riesgos==null){
                        riesgos="OTROS";
                        codigoriesgo="16";
                    } else {
                        riesgos+=",OTROS";
                        codigoriesgo+=",16";}}
                Persona.FactoresDeRiesgo=riesgos;
                Persona.CodfigoFactorRiesgo=codigoriesgo;
                //Toast.makeText(getApplicationContext(), riesgos, Toast.LENGTH_SHORT).show();
                ColorAvanceFactores();
            }
        });
        builder.setNegativeButton("CANCELAR", null);

        // Defino un ScrollView para visualizar todos
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sv.setVerticalScrollBarEnabled(true);
        sv.addView(mainLayout);

        builder.setView(sv);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Cambia lo colores de avance de factores
    private void ColorAvanceFactores(){
        if (Persona.FactoresDeRiesgo.length()!=0){
            //Riesgo.setBackgroundColor(Color.parseColor("#8BC34A"));
            AvRiesgo.setText("1/1");
            AvRiesgo.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }

    // Detecto los Check seleccionados anteriormente
    private void CheckSeleccionadosFactores(String Vacunas){
    if(Vacunas!=null){
        String[] vac =Vacunas.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals("POR CALENDARIO")){
                calendario.setChecked(true);
            }
            if (vac[x].equals("EMBARAZO")){
                embarazo.setChecked(true);
            }
            if (vac[x].equals("PUERPERIO")){
                puerperio.setChecked(true);
            }
            if (vac[x].equals("PERSONAL DE SALUD")){
                personalsalud.setChecked(true);
            }
            if (vac[x].equals("PERSONAL ESENCIAL")){
                personalesencial.setChecked(true);
            }
            if (vac[x].equals("VIAJEROS")){
                viajeros.setChecked(true);
            }
            if (vac[x].equals("INMUNOCOMPROMETIDOS")){
                inmunocomprometidos.setChecked(true);
            }
            if (vac[x].equals("CARDIOLOGICOS")){
                cadiologicos.setChecked(true);
            }
            if (vac[x].equals("RESPIRATORIOS")){
                respiratorios.setChecked(true);
            }
            if (vac[x].equals("DIABÉTICOS")){
                diabeticos.setChecked(true);
            }
            if (vac[x].equals("PREMATUROS")){
                prematuros.setChecked(true);
            }
            if (vac[x].equals("ASPLÉNICOS")){
                asplenicos.setChecked(true);
            }
            if (vac[x].equals("OBESIDAD MORBIDA")){
                obesidad.setChecked(true);
            }
            if (vac[x].equals("INMUNODEFICIENCIA")){
                inmunodeficiencia.setChecked(true);
            }
            if (vac[x].equals("CONVIVIENTE INMUNOCOMPROMETIDOS")){
                conviviente.setChecked(true);
            }
            if (vac[x].equals("OTROS")){
                otros.setChecked(true);
            }
            if (vac[x].equals("PREMATUROS")){
                prematuros.setChecked(true);
            }
        }
    }}

    //----------------------------------------------------------------------------------------------
    // Defino los Layout para los Check que se añaden a los AlertDialog
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private final CheckBox PersonalCheck (LinearLayout Contenedor, String Texto, int TamañoLetra, String ColorPares, int AltoContenedor){
        // 16 POLIO - SABIN ORAL
        LinearLayout layout16       = new LinearLayout(this);
        layout16.setOrientation(LinearLayout.HORIZONTAL);
        layout16.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox sabin = new CheckBox(getApplicationContext());
        sabin.setText(Texto);
        sabin.setTextSize(TamañoLetra);
        sabin.setTextColor(Color.WHITE);
        sabin.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout16.addView(sabin);
        layout16.setBackgroundColor(Color.parseColor(ColorPares));
        layout16.setMinimumHeight(AltoContenedor);
        Contenedor.addView(layout16);
        return sabin;}

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// SECCION DE DEFINICION DE LAS VISTAS DE LOS CONTACTOS

    // Defino el AlertDialog de los contactos
    public void Contactos(View view){
        // Defino los contenedores
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("CONTACTO");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check
        LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        // Defino los parametros
        int TamañoLetra =20;

        // Defino los EditText
        // Telefono Celular
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        celular = new EditText(getApplicationContext());
        celular.setHint("TELEFONO CELULAR");
        celular.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        celular.setHintTextColor(Color.WHITE);
        celular.setTextSize(TamañoLetra);
        celular.setTextColor(Color.WHITE);
        celular.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.addView(celular);
        // Telefono Fijo
        LinearLayout layout1       = new LinearLayout(this);
        layout1.setOrientation(LinearLayout.HORIZONTAL);
        layout1.setVerticalGravity(Gravity.CENTER_VERTICAL);
        fijo = new EditText(getApplicationContext());
        fijo.setHint("TELEFONO FIJO");
        fijo.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        fijo.setHintTextColor(Color.WHITE);
        fijo.setTextSize(TamañoLetra);
        fijo.setTextColor(Color.WHITE);
        fijo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout1.addView(fijo);
        // Mail
        LinearLayout layout2       = new LinearLayout(this);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setVerticalGravity(Gravity.CENTER_VERTICAL);
        mail = new EditText(getApplicationContext());
        mail.setHint("MAIL");
        mail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mail.setHintTextColor(Color.WHITE);
        mail.setTextSize(TamañoLetra);
        mail.setTextColor(Color.WHITE);
        mail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout2.addView(mail);

        mainLayout.addView(layout0);
        mainLayout.addView(layout1);
        mainLayout.addView(layout2);

        // Si ya tengo valores de contactos debo inicializar
        if(Persona.Celular!=""){celular.setText(Persona.Celular);}
        if(Persona.Fijo!=""){fijo.setText(Persona.Fijo);}
        if(Persona.Mail!=""){mail.setText(Persona.Mail);}

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                Persona.Celular = celular.getText().toString();
                Persona.Fijo = fijo.getText().toString();
                Persona.Mail = mail.getText().toString();
                //Toast.makeText(getApplicationContext(), telefonocelular+telefonofijo+direccionmail, Toast.LENGTH_SHORT).show();

                // Cambio los colores de avance
                ColorAvanceContacto();

            }
        });
        builder.setNegativeButton("CANCELAR", null);
        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        if(Persona!=null){
        celular.setText(Persona.Celular);
        fijo.setText(Persona.Fijo);
        mail.setText(Persona.Mail);
        }
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceContacto(){
        // Cambio los colores de avance
        int avance = 0;
        if (Persona.Celular.length()!=0){
            avance+=1;
        }
        if (Persona.Fijo.length()!=0){
            avance+=1;
        }
        if (Persona.Mail.length()!=0){
            avance+=1;
        }
        if(avance==1 || avance==2){
            //Contacto.setBackgroundColor(Color.parseColor("#FFA07A"));
            String aux = Integer.toString(avance)+"/3";
            AvContacto.setText(aux);
            AvContacto.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==3){
            //Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
            AvContacto.setText("3/3");
            AvContacto.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// SECCION DE DEFINICION DE LAS VISTAS DE LAS OBSERVACIONES Y LOS PRODUCTOS DE LIMPIEZA

    // Defino el AletDialog de las observaiones, incluye la informacion de entrega de productos de
    // limpieza
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void observaciones(View view){
        // Defino los contenedores
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("OBSERVACIONES");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check
        LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Entrega de productos de limpieza
        LinearLayout layoutlimp       = new LinearLayout(this);
        layoutlimp.setOrientation(LinearLayout.HORIZONTAL);
        layoutlimp.setVerticalGravity(Gravity.CENTER_VERTICAL);
        layoutlimp.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        limp = new TextView(getApplicationContext());
        limp.setText("ENTREGA DE PROD. DE LIMPIEZA");
        limp.setGravity(Gravity.CENTER_HORIZONTAL);
        limp.setBackgroundColor(Color.parseColor("#396F98"));
        limp.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        limp.setHintTextColor(Color.WHITE);
        limp.setTextSize(20);
        limp.setTextColor(Color.WHITE);
        limp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layoutlimp.addView(limp);
        mainLayout.addView(layoutlimp);

        // Defino el Layaout que va a contener los radiobutton
        //LinearLayout mainLayout1       = new LinearLayout(this);
        RadioGroup mainLayout1 = new RadioGroup(this);
        mainLayout1.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout1.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        // Button si
        rb1 = new RadioButton(getApplicationContext());
        rb1.setText("SI");
        rb1.setLayoutParams(new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.MATCH_PARENT));
        rb1.setTextColor(Color.WHITE);
        rb1.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        rb1.setTextSize(25);

        // Button no
        rb2 = new RadioButton(getApplicationContext());
        rb2.setText("NO");
        rb2.setTextColor(Color.WHITE);
        rb2.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        rb2.setTextSize(25);

        mainLayout1.addView(rb1);
        mainLayout1.addView(rb2);
        mainLayout.addView(mainLayout1);

        // Defino los parametros
        int TamañoLetra =20;

        // OBSERVACIONES
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        obs = new EditText(getApplicationContext());
        //sabin.setText(Texto);
        obs.setHint("OBSERVACIONES");
        obs.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        obs.setHintTextColor(Color.WHITE);
        obs.setTextSize(TamañoLetra);
        obs.setTextColor(Color.WHITE);
        obs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.setMinimumHeight(200);
        layout0.addView(obs);


        mainLayout.addView(layout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                Persona.Observaciones = obs.getText().toString();
                if(rb1.isChecked()==true){Persona.Limpieza="SI";}
                if(rb2.isChecked()==true){Persona.Limpieza="NO";}

                // Cambio los colores de avance
                ColorAvanceObservaciones();

            }
        });
        builder.setNegativeButton("CANCELAR", null);
        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        if(Persona.Observaciones!=null || Persona.Limpieza!=null){
            obs.setText(Persona.Observaciones);
            if(Persona.Limpieza.equals("SI")){rb1.setChecked(true);}
            if(Persona.Limpieza.equals("NO")){rb2.setChecked(true);}
        }
    }

    // Cambia los colores de los botones de llenado de Observaciones
    private void ColorAvanceObservaciones(){
        // Cambio los colores de avance
        int avance = 0;
        if (Persona.Observaciones.length()!=0){
            avance+=1;
        }
        if (Persona.Limpieza.length()!=0){
            avance+=1;
        }

        if(avance==1){
            //Observacion.setBackgroundColor(Color.parseColor("#FFA07A"));
            String aux = Integer.toString(avance)+"/2";
            AvObs.setText(aux);
            AvObs.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==2){
            //Observacion.setBackgroundColor(Color.parseColor("#8BC34A"));
            AvObs.setText("2/2");
            AvObs.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// SECCION DE DEFINICION DE DEFINICION DE LAS OPCIONES DE GUARDADO DE LAS PERSONAS

    // Defino las funciones de guardado de persona
    // Para que sea una funcion disparada por un Button
    public void GuardarPersona(View view){
        // Defino el intent para permitir pasar los datos
        Intent Modif1= new Intent (this, MainActivity.class);

        // Reviso que no se carguen datos nuevos, si se cargaron los obtengo
        if(Nombre.getText().toString()!=null){Persona.Nombre=Nombre.getText().toString();}
        if(Apellido.getText().toString()!=null){Persona.Apellido=Apellido.getText().toString();}
        if(dni.getText().toString()!=null){Persona.DNI=dni.getText().toString();}
        if(Efector.getText().toString()!=null){Persona.Efector=Efector.getText().toString();}

        // Si uno de los campos listados abajo es nulo lo reemplazo por S/D
        if(Persona.Edad==null){Persona.Edad="";}
        if(Persona.UnidadEdad==null){Persona.UnidadEdad="";}
        if(Persona.CodfigoFactorRiesgo==null){Persona.CodfigoFactorRiesgo="";}
        if(Persona.Observaciones==null){Persona.Observaciones="";}

        // Si dni es distinto a null le permito guardar la persona
        if(Persona.DNI!=""){
        Modif1.putExtra("NOMBRE" , Persona.Nombre);
        Modif1.putExtra("APELLIDO" , Persona.Apellido);
        Modif1.putExtra("DNI" , Persona.DNI);
        Modif1.putExtra("EDAD" , Persona.Edad);
        Modif1.putExtra("UNIDADEDAD" , Persona.UnidadEdad);
        Modif1.putExtra("EFECTOR" , Persona.Efector);
        Modif1.putExtra("FACTORES" , Persona.FactoresDeRiesgo);
        Modif1.putExtra("CODIGOFACTORES" , Persona.CodfigoFactorRiesgo);
        Modif1.putExtra("VACUNAS" , Persona.Vacunas);
        Modif1.putExtra("CELULAR" , Persona.Celular);
        Modif1.putExtra("FIJO" , Persona.Fijo);
        Modif1.putExtra("MAIL" , Persona.Mail);
        Modif1.putExtra("OBSERVACIONES" , Persona.Observaciones);
        Modif1.putExtra("NACIMIENTO" , Persona.Nacimiento);
        Modif1.putExtra("LIMPIEZA" , Persona.Limpieza);
        Modif1.putExtra("LOTE" , Persona.LoteVacuna);

        Toast.makeText(this, "PERSONA CARGADA", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, Modif1);
        finish();
        }
        else{Toast.makeText(this, "FALTA DNI", Toast.LENGTH_SHORT).show();}
    }
    // Para que se una funcion disparada por el Button de regreso de forma private
    private void GuardarPersona(){
        // Defino el intent para permitir pasar los datos
        Intent Modif1= new Intent (this, MainActivity.class);

        // Reviso que no se carguen datos nuevos, si se cargaron los obtengo
        if(Nombre.getText().toString()!=null){Persona.Nombre=Nombre.getText().toString();}
        if(Apellido.getText().toString()!=null){Persona.Apellido=Apellido.getText().toString();}
        if(dni.getText().toString()!=null){Persona.DNI=dni.getText().toString();}
        if(Sp1.getSelectedItem().toString()!=null){Persona.Efector=Sp1.getSelectedItem().toString();}

        // Si uno de los campos listados abajo es nulo lo reemplazo por S/D
        if(Persona.Edad==null){Persona.Edad="";}
        if(Persona.UnidadEdad==null){Persona.UnidadEdad="";}
        if(Persona.CodfigoFactorRiesgo==null){Persona.CodfigoFactorRiesgo="";}
        if(Persona.Observaciones==null){Persona.Observaciones="";}

        // Si dni es distinto a null le permito guardar la persona
        if(Persona.DNI!=""){
            Modif1.putExtra("NOMBRE" , Persona.Nombre);
            Modif1.putExtra("APELLIDO" , Persona.Apellido);
            Modif1.putExtra("DNI" , Persona.DNI);
            Modif1.putExtra("EDAD" , Persona.Edad);
            Modif1.putExtra("UNIDADEDAD" , Persona.UnidadEdad);
            Modif1.putExtra("EFECTOR" , Persona.Efector);
            Modif1.putExtra("FACTORES" , Persona.FactoresDeRiesgo);
            Modif1.putExtra("CODIGOFACTORES" , Persona.CodfigoFactorRiesgo);
            Modif1.putExtra("VACUNAS" , Persona.Vacunas);
            Modif1.putExtra("CELULAR" , Persona.Celular);
            Modif1.putExtra("FIJO" , Persona.Fijo);
            Modif1.putExtra("MAIL" , Persona.Mail);
            Modif1.putExtra("OBSERVACIONES" , Persona.Observaciones);
            Modif1.putExtra("NACIMIENTO" , Persona.Nacimiento);
            Modif1.putExtra("LIMPIEZA" , Persona.Limpieza);
            Modif1.putExtra("LOTE" , Persona.LoteVacuna);

            Toast.makeText(this, "PERSONA CARGADA", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, Modif1);
            finish();
        }
        else{Toast.makeText(this, "FALTA DNI", Toast.LENGTH_SHORT).show();}
    }
    // Función que evita que al presionar volver atras se pierdan todos los datos
    @Override
    public void onBackPressed()
    {
        // Defino los contenedores
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MiEstiloAlert);
        TextView textView = new TextView(this);
        textView.setText("RelevAr");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(22F);
        textView.setBackgroundColor(Color.parseColor("#4588BC"));
        textView.setTextColor(Color.WHITE);
        builder.setCustomTitle(textView);

        // Defino el Layaout que va a contener a los Check
        final LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        // Defino los parametros
        int TamañoLetra =18;

        // Telefono Celular
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final TextView descripcion = new TextView(getApplicationContext());
        descripcion.setText("Salir y eliminar registro");
        descripcion.setGravity(Gravity.CENTER_HORIZONTAL);
        descripcion.setTextSize(TamañoLetra);
        descripcion.setTextColor(Color.WHITE);
        descripcion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout0.setMinimumHeight(100);
        layout0.addView(descripcion);

        mainLayout.addView(layout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                //if(Persona.DNI!=""){
                //GuardarPersona();}
                finish();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //finish();
            }
        });

        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
