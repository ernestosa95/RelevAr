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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class persona extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private EditText dni, Apellido, Nombre, Edad;
    private TextView fecha;
    private Button Vacuna, Riesgo, Contacto, Observacion;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private int STORAGE_PERMISSION_CODE =1;
    private String telefonocelular, telefonofijo, direccionmail, nombre, edad, unidadedad, vacunas, riesgos, codigoriesgo, observaciones;
    private DatePickerDialog.OnDateSetListener Date;
    private Spinner Sp1;
    private ArrayList<String> categorias = new ArrayList<>();
    CheckBox hepaA, hepaB, bcg, rotavirus, quintuple, tripleviral, varicela, dtp, vph, tripeacelular, dt, antigripal, vcn23, vcn13, tetravalente, ipv, sabin;
    String date;
    ArrayList<String> lista;
    CheckBox calendario, embarazo, puerperio, personalsalud, personalesencial, viajeros, inmunocomprometidos, cadiologicos, respiratorios, diabeticos,
    prematuros, asplenicos, obesidad, inmunodeficiencia, conviviente, otros;
    EditText celular, fijo, mail, obs, lotevacuna;
    TextView tipovacuna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

        // Defino los widgets
        dni = (EditText) findViewById(R.id.DNI);
        Apellido = (EditText) findViewById(R.id.APELLIDO);
        Nombre = (EditText) findViewById(R.id.NOMBRE);


        Vacuna = (Button) findViewById(R.id.VACUNA);
        Riesgo = (Button) findViewById(R.id.RIESGO);
        Contacto = (Button) findViewById(R.id.CONTACTO);
        Observacion = (Button) findViewById(R.id.OBSERVACION);

        fecha=(TextView) findViewById(R.id.fecha);

        Sp1 = (Spinner) findViewById(R.id.efector);
        categorias.add("CAPs D'ANGELO");
        categorias.add("CAPs ANTARTIDA (CIC)");
        categorias.add("CAPs SAN MARTIN");
        categorias.add("CRR CARRILLO");
        categorias.add("HOSP. SAN ROQUE");
        categorias.add("HOSP. SAN MARTIN");
        categorias.add("HOSP. BAXADA");
        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, categorias);//Cargo el spinner con los datos
        Sp1.setAdapter(comboAdapter);

        // Construyo el widget para la fecha
        Date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int  year, int month, int day) {
                Log.d(TAG, "onDateSet: date:"+year+"/"+month+"/"+day);
                int mes = month + 1;
                date=day+" - "+mes+" - "+year;
                fecha.setText(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date1 = new Date();
                String fecha1 = dateFormat.format(date1);
                String[] partes = fecha1.split("-");
                int anioactual = Integer.parseInt(partes[0]);
                int mesactual = Integer.parseInt(partes[1]);
                int diaactual = Integer.parseInt(partes[2]);
                java.util.Date Actual = new Date(anioactual, mesactual, diaactual);
                if(fecha.getText().toString()!="DD - MM - YYYY"){
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
                        edad=Long.toString(MesesTranscurridos);
                        unidadedad="MESES";
                    }
                    else {edad=Long.toString(AñosTranscurridos);
                        unidadedad="AÑOS";}
                }else edad="S/D";}
        };

        //Cargo los datos para que se pueda editar un registro que ya esta hecho
        lista = (ArrayList<String>) getIntent().getSerializableExtra("DATOS");
        if(lista!=null){
        makeText(this, lista.get(8), LENGTH_SHORT).show();
        dni.setText(lista.get(0));
        Apellido.setText(lista.get(1));
        Nombre.setText(lista.get(2));
        Sp1.setSelection(ObtenerPosicion(Sp1, lista.get(5)));

        // Para la fecha es necesario corroborar si ya hay un dato cargado
        if(lista.get(11)!=""){
        fecha.setText(lista.get(11));
        edad=lista.get(3);
        unidadedad=lista.get(4);
        date=lista.get(12);
        }
        else{fecha.setText("DD - MM - AAAA");}

        /*celular.setText(lista.get(0).get(9));
        fijo.setText(lista.get(0).get(10));
        mail.setText(lista.get(0).get(11));
        obs.setText(lista.get(0).get(12));*/
        riesgos = lista.get(6);
        codigoriesgo = lista.get(7);
        vacunas = lista.get(8);

        }
    }

    //Funcion para obtener la posicion del efector seleccionado
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

    public void Fecha (View view){
        Calendar cal=Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, Date, 1955,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void TiposVacuna(View view){

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
        //lotevacuna.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
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


        // En el caso de editar un registro
        // Necesito que me ponga seleccionado todos aquellos que ya se habian seleeccionado antes
        // para que los pueda editar
        if(lista!=null){
            //Necesito activar los check que ya habia seleccionado antes
            CheckSeleccionadosVacunas(lista.get(8));
        }

        // Defino un ScrollView para visualizar todos
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sv.setVerticalScrollBarEnabled(true);
        sv.addView(mainLayout);

        mainLayout0.addView(mainLayout1);
        mainLayout0.addView(sv);
        builder.setView(mainLayout0);



        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                vacunas=null;
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
                Toast.makeText(getApplicationContext(), vacunas, Toast.LENGTH_SHORT).show();
                if (vacunas.length()!=0){
                    Vacuna.setBackgroundColor(Color.parseColor("#8BC34A"));
                }

            }
        });
        builder.setNegativeButton("CANCELAR", null);

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        /*Button bn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        bn.setBackgroundColor(Color.parseColor(ColorImpares));
        //bn.setLayoutParams (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Button bp = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        bp.setBackgroundColor(Color.parseColor(ColorImpares));
        //bp.setLayoutParams (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));*/
    }

    private void CheckSeleccionadosVacunas(String Vacunas){
        String[] vac =Vacunas.split(",");
        for (int x = 0; x < vac.length; x++) {
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
    }

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
        textView.setText("VACUNAS");
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
        otros = PersonalCheck(mainLayout,"OTROS", TamañoLetra, ColorImpares, AltoContenedor);

        // En el caso de editar un registro
        // Necesito que me ponga seleccionado todos aquellos que ya se habian seleeccionado antes
        // para que los pueda editar
        if(lista!=null){
            //Necesito activar los check que ya habia seleccionado antes
            CheckSeleccionadosFactores(lista.get(6));
        }

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                riesgos=null;
                codigoriesgo=null;
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
                if (otros.isChecked()){
                    if (riesgos==null){
                        riesgos="OTROS";
                        codigoriesgo="16";
                    } else {
                        riesgos+=",OTROS";
                        codigoriesgo+=",16";}}
                Toast.makeText(getApplicationContext(), riesgos, Toast.LENGTH_SHORT).show();
                if (riesgos!=null){
                    Riesgo.setBackgroundColor(Color.parseColor("#8BC34A"));
                }

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

    private void CheckSeleccionadosFactores(String Vacunas){
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
    }

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
        //sabin.setText(Texto);
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
        //sabin.setText(Texto);
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
        //sabin.setText(Texto);
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

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                telefonofijo=null;
                telefonocelular=null;
                direccionmail=null;
                telefonocelular = celular.getText().toString();
                telefonofijo = fijo.getText().toString();
                direccionmail = mail.getText().toString();
                Toast.makeText(getApplicationContext(), telefonocelular+telefonofijo+direccionmail, Toast.LENGTH_SHORT).show();
                direccionmail=telefonocelular+";"+telefonofijo+";"+direccionmail;
                if (telefonocelular.length()!=0){
                    Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
                }

            }
        });
        builder.setNegativeButton("CANCELAR", null);
        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

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
                observaciones=null;
                observaciones = obs.getText().toString();
                Toast.makeText(getApplicationContext(), observaciones, Toast.LENGTH_SHORT).show();
                if (observaciones.length()!=0){
                    Observacion.setBackgroundColor(Color.parseColor("#8BC34A"));
                }

            }
        });
        builder.setNegativeButton("CANCELAR", null);
        builder.setView(mainLayout);
        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void GuardarPersona(View view){
        Intent Modif1= new Intent (this, MainActivity.class);
        String nombre, apellido, Edad="S/D", Unidadedad="S/D", codigofactores="S/D", contacto="S/D", obs1="S/D";
        if(Nombre.getText().toString()==null){nombre="S/D";}else{nombre=Nombre.getText().toString();}
        if(Apellido.getText().toString()==null){apellido="S/D";}else{apellido=Apellido.getText().toString();}
        if(edad==null){Edad="S/D";}else{Edad=edad;}
        if(unidadedad==null){Unidadedad="S/D";}else{Unidadedad=edad;}
        if(codigoriesgo==null){codigofactores="S/D";}else{codigofactores=codigoriesgo;}
        if(direccionmail==null){contacto="S/D";}else{contacto=direccionmail;}
        if(observaciones==null){obs1="S/D";}else{obs1=observaciones;}

        if(dni.getText().toString()!=null){ //&& riesgos!=null && vacunas!=null){
        Modif1.putExtra("NOMBRE" , nombre);
        Modif1.putExtra("APELLIDO" , apellido);
        Modif1.putExtra("DNI" , dni.getText().toString());
        Modif1.putExtra("EDAD" , Edad);
        Modif1.putExtra("UNIDADEDAD" , unidadedad);
        Modif1.putExtra("EFECTOR" , Sp1.getSelectedItem().toString());
        Modif1.putExtra("FACTORES" , riesgos);
        Modif1.putExtra("CODIGOFACTORES" , codigoriesgo);
        Modif1.putExtra("VACUNAS" , vacunas);
        Modif1.putExtra("CONTACTO" , direccionmail);
        Modif1.putExtra("OBSERVACIONES" , observaciones);
        Modif1.putExtra("NACIMIENTO" , date);}
        else{Toast.makeText(this, "FALTAN DATOS", Toast.LENGTH_SHORT).show();}

        setResult(RESULT_OK, Modif1);
        finish();
    }
}
