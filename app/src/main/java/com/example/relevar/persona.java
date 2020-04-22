package com.example.relevar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

public class persona extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private EditText dni, apellido, Nombre, Edad;
    private TextView fecha;
    private Button salud;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private int STORAGE_PERMISSION_CODE =1;
    private String nombre, edad, unidadedad, vacunas;
    private DatePickerDialog.OnDateSetListener Date;
    private Spinner Sp1;
    private ArrayList<String> categorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

        // Defino los widgets
        dni = (EditText) findViewById(R.id.DNI);
        apellido = (EditText) findViewById(R.id.APELLIDO);
        Nombre = (EditText) findViewById(R.id.NOMBRE);

        salud = (Button) findViewById(R.id.SALUD);
        fecha=(TextView) findViewById(R.id.fecha);

        Sp1 = (Spinner) findViewById(R.id.efector);
        categorias.add("CAPs D'ANGELO");
        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, categorias);//Cargo el spinner con los datos
        Sp1.setAdapter(comboAdapter);

        // Construyo el widget para la fecha
        Date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int  year, int month, int day) {
                Log.d(TAG, "onDateSet: date:"+year+"/"+month+"/"+day);
                int mes = month + 1;
                String date=day+" - "+mes+" - "+year;
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

    /*@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Salud (View view){
        Intent Modif= new Intent (this, salud.class);
        startActivity(Modif);
    }*/

    public void Vacunas (View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.vacunas, null);
        mBuilder.setView(mView);
        mBuilder.setTitle("VACUNAS");
        //mBuilder.setMultiChoiceItems()
        Button listoVacunas = (Button) mView.findViewById(R.id.btnVacunas);
        // Defino los checklist de las vacunas
        /*final CheckBox hepaA = (CheckBox) findViewById(R.id.HEPA);
        final CheckBox hepaB = (CheckBox) findViewById(R.id.HEPB);
        final CheckBox bcg = (CheckBox) findViewById(R.id.BCG);
        final CheckBox rotavirus = (CheckBox) findViewById(R.id.ROTAVIRUS);
        final CheckBox quintuple = (CheckBox) findViewById(R.id.QUINTIPLE);
        final CheckBox tripleviral = (CheckBox) findViewById(R.id.TRIPLEVIRAL);
        final CheckBox varicela = (CheckBox) findViewById(R.id.VARICELA);
        final CheckBox dtp = (CheckBox) findViewById(R.id.DTP);
        final CheckBox vph = (CheckBox) findViewById(R.id.VPH);
        final CheckBox tripleacelular = (CheckBox) findViewById(R.id.TRIPLE);
        final CheckBox dt = (CheckBox) findViewById(R.id.dT);
        final CheckBox antigripal = (CheckBox) findViewById(R.id.ANTIGRIPAL);
        final CheckBox vcn23 = (CheckBox) findViewById(R.id.VCN23);
        final CheckBox vcn13 = (CheckBox) findViewById(R.id.VCN13);
        final CheckBox meningococica = (CheckBox) findViewById(R.id.MENINGOCOCICA);
        final CheckBox ipv = (CheckBox) findViewById(R.id.IPV);
        final CheckBox sabin = (CheckBox) findViewById(R.id.SABIN);*/
        String[] items = {"java","android","Data Structures","HTML","CSS"};
        boolean[] checkedItems = {false, false, false, false, false,false};
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        listoVacunas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recupero el check seleccionado
                /*if(hepaA.isChecked()) if (vacunas == null) {
                    vacunas = "HEPATITIS A";
                } else {
                    vacunas += ",HEPATITIS A";
                }*/
                /*if(hepaB.isChecked()){
                    if (vacunas==null){
                        vacunas="HEPATITIS B";}
                    else{vacunas+=",HEPATITIS B";}}
                if(bcg.isChecked()){
                    if (vacunas==null){
                        vacunas="BCG";}
                    else{vacunas+=",BCG";}}
                if(rotavirus.isChecked()){
                    if (vacunas==null){
                        vacunas="ROTAVIRUS";}
                    else{vacunas+=",ROTAVIRUS";}}
                if(quintuple.isChecked()){
                    if (vacunas==null){
                        vacunas="QUINTUPLE";}
                    else{vacunas+=",QUINTUPLE";}}
                if(tripleviral.isChecked()){
                    if (vacunas==null){
                        vacunas="TRIPLE VIRAL";}
                    else{vacunas+=",TRIPLE VIRAL";}}
                if(varicela.isChecked()){
                    if (vacunas==null){
                        vacunas="VARICELA";}
                    else{vacunas+=",VARICELA";}}
                if(dtp.isChecked()){
                    if (vacunas==null){
                        vacunas="DTP";}
                    else{vacunas+=",DTP";}}
                if(vph.isChecked()){
                    if (vacunas==null){
                        vacunas="VPH";}
                    else{vacunas+=",VPH";}}
                if(tripleacelular.isChecked()){
                    if (vacunas==null){
                        vacunas="TRIPLE ACELULAR";}
                    else{vacunas+=",TRIPLE ACELULAR";}}
                if(dt.isChecked()){
                    if (vacunas==null){
                        vacunas="dT";}
                    else{vacunas+=",dT";}}
                if(antigripal.isChecked()){
                    if (vacunas==null){
                        vacunas="ANTIGRIPAL";}
                    else{vacunas+=",ANTIGRIPAL";}}
                if(vcn23.isChecked()){
                    if (vacunas==null){
                        vacunas="NEUMOCOCO POLISACARIDA 23V";}
                    else{vacunas+=",NEUMOCOCO POLISACARIDA 23V";}}
                if(vcn13.isChecked()){
                    if (vacunas==null){
                        vacunas="NEUMOCOCO CONJUGADA 13V";}
                    else{vacunas+=",NEUMOCOCO CONJUGADA 13V";}}
                if(meningococica.isChecked()){
                    if (vacunas==null){
                        vacunas="MENINGOCOCOCICA TETRAVALENTE";}
                    else{vacunas+=",MENINGOCOCICA TETRAVALENTE";}}
                if(ipv.isChecked()){
                    if (vacunas==null){
                        vacunas="POLIO IPV";}
                    else{vacunas+=",POLIO IPV";}}
                if(sabin.isChecked()){
                    if (vacunas==null){
                        vacunas="POLIO SABIN";}
                    else{vacunas+=",POLIO SABIN";}}*/
                vacunas="aca1";
                Toast.makeText(getApplicationContext(),vacunas,Toast.LENGTH_SHORT).show();

                dialog.cancel();
            }
        });
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

        // Defino el Layaout que va a contener a los Check
        LinearLayout mainLayout       = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Defino los Checks
        String ColorPares = "#4A4A4A";
        String ColorImpares = "#4588BC";
        int TamañoLetra =20;
        int AltoContenedor = 80;
        // 0 HEPATITIS A
        LinearLayout layout0       = new LinearLayout(this);
        layout0.setVerticalGravity(Gravity.CENTER_VERTICAL);
        layout0.setOrientation(LinearLayout.HORIZONTAL);
        final CheckBox hepaA = new CheckBox(getApplicationContext());
        hepaA.setText("HEPATITIS A");
        hepaA.setTextSize(TamañoLetra);
        hepaA.setTextColor(Color.WHITE);
        hepaA.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout0.addView(hepaA);
        layout0.setBackgroundColor(Color.parseColor(ColorPares));
        layout0.setMinimumHeight(AltoContenedor);
        // 1 HEPATITIS B
        LinearLayout layout1       = new LinearLayout(this);
        layout1.setOrientation(LinearLayout.HORIZONTAL);
        layout1.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox hepaB = new CheckBox(getApplicationContext());
        hepaB.setText("HEPATITIS B");
        hepaB.setTextSize(TamañoLetra);
        hepaB.setTextColor(Color.WHITE);
        hepaB.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout1.addView(hepaB);
        layout1.setBackgroundColor(Color.parseColor(ColorImpares));
        layout1.setMinimumHeight(AltoContenedor);
        // 2 HEPATITIS BCG
        LinearLayout layout2       = new LinearLayout(this);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox bcg = new CheckBox(getApplicationContext());
        bcg.setText("BCG");
        bcg.setTextSize(TamañoLetra);
        bcg.setTextColor(Color.WHITE);
        bcg.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout2.addView(bcg);
        layout2.setBackgroundColor(Color.parseColor(ColorPares));
        layout2.setMinimumHeight(AltoContenedor);
        // 3 ROTAVIRUS
        LinearLayout layout3       = new LinearLayout(this);
        layout3.setOrientation(LinearLayout.HORIZONTAL);
        layout3.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox rotavirus = new CheckBox(getApplicationContext());
        rotavirus.setText("ROTAVIRUS");
        rotavirus.setTextSize(TamañoLetra);
        rotavirus.setTextColor(Color.WHITE);
        rotavirus.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout3.addView(rotavirus);
        layout3.setBackgroundColor(Color.parseColor(ColorImpares));
        layout3.setMinimumHeight(AltoContenedor);
        // 4 QUINTUPLE
        LinearLayout layout4       = new LinearLayout(this);
        layout4.setOrientation(LinearLayout.HORIZONTAL);
        layout4.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox quintuple = new CheckBox(getApplicationContext());
        quintuple.setText("QUINTUPLE");
        quintuple.setTextSize(TamañoLetra);
        quintuple.setTextColor(Color.WHITE);
        quintuple.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout4.addView(quintuple);
        layout4.setBackgroundColor(Color.parseColor(ColorPares));
        layout4.setMinimumHeight(AltoContenedor);
        // 5 TRIPLE VIRAL
        LinearLayout layout5       = new LinearLayout(this);
        layout5.setOrientation(LinearLayout.HORIZONTAL);
        layout5.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox tripleviral = new CheckBox(getApplicationContext());
        tripleviral.setText("TRIPLE VIRAL");
        tripleviral.setTextSize(TamañoLetra);
        tripleviral.setTextColor(Color.WHITE);
        tripleviral.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout5.addView(tripleviral);
        layout5.setBackgroundColor(Color.parseColor(ColorImpares));
        layout5.setMinimumHeight(AltoContenedor);
        // 6 VARICELA
        LinearLayout layout6       = new LinearLayout(this);
        layout6.setOrientation(LinearLayout.HORIZONTAL);
        layout6.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox varicela = new CheckBox(getApplicationContext());
        varicela.setText("VARICELA");
        varicela.setTextSize(TamañoLetra);
        varicela.setTextColor(Color.WHITE);
        varicela.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout6.addView(varicela);
        layout6.setBackgroundColor(Color.parseColor(ColorPares));
        layout6.setMinimumHeight(AltoContenedor);
        // 7 DTP
        LinearLayout layout7       = new LinearLayout(this);
        layout7.setOrientation(LinearLayout.HORIZONTAL);
        layout7.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox dtp = new CheckBox(getApplicationContext());
        dtp.setText("DTP");
        dtp.setTextSize(TamañoLetra);
        dtp.setTextColor(Color.WHITE);
        dtp.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout7.addView(dtp);
        layout7.setBackgroundColor(Color.parseColor(ColorImpares));
        layout7.setMinimumHeight(AltoContenedor);
        // 8 VPH
        LinearLayout layout8       = new LinearLayout(this);
        layout8.setOrientation(LinearLayout.HORIZONTAL);
        layout8.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox vph = new CheckBox(getApplicationContext());
        vph.setText("VPH");
        vph.setTextSize(TamañoLetra);
        vph.setTextColor(Color.WHITE);
        vph.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout8.addView(vph);
        layout8.setBackgroundColor(Color.parseColor(ColorPares));
        layout8.setMinimumHeight(AltoContenedor);
        // 9 TRIPE ACELULAR
        LinearLayout layout9       = new LinearLayout(this);
        layout9.setOrientation(LinearLayout.HORIZONTAL);
        layout9.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox tripeacelular = new CheckBox(getApplicationContext());
        tripeacelular.setText("TRIPE ACELULAR dTpa");
        tripeacelular.setTextSize(TamañoLetra);
        tripeacelular.setTextColor(Color.WHITE);
        tripeacelular.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout9.addView(tripeacelular);
        layout9.setBackgroundColor(Color.parseColor(ColorImpares));
        layout9.setMinimumHeight(AltoContenedor);
        // 10 dT
        LinearLayout layout10       = new LinearLayout(this);
        layout10.setOrientation(LinearLayout.HORIZONTAL);
        layout10.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox dt = new CheckBox(getApplicationContext());
        dt.setText("dT");
        dt.setTextSize(TamañoLetra);
        dt.setTextColor(Color.WHITE);
        dt.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout10.addView(dt);
        layout10.setBackgroundColor(Color.parseColor(ColorPares));
        layout10.setMinimumHeight(AltoContenedor);
        // 11 ANTIGRIPAL
        LinearLayout layout11       = new LinearLayout(this);
        layout11.setOrientation(LinearLayout.HORIZONTAL);
        layout11.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox antigripal = new CheckBox(getApplicationContext());
        antigripal.setText("ANTIGRIPAL");
        antigripal.setTextSize(TamañoLetra);
        antigripal.setTextColor(Color.WHITE);
        antigripal.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout11.addView(antigripal);
        layout11.setBackgroundColor(Color.parseColor(ColorImpares));
        layout11.setMinimumHeight(AltoContenedor);
        // 12 NEUMOCOCO POLISACARIDA 23V
        LinearLayout layout12       = new LinearLayout(this);
        layout12.setOrientation(LinearLayout.HORIZONTAL);
        layout12.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox vcn23 = new CheckBox(getApplicationContext());
        vcn23.setText("NEUMOCOCO POLISACARIDA 23V");
        vcn23.setTextSize(TamañoLetra);
        vcn23.setTextColor(Color.WHITE);
        vcn23.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout12.addView(vcn23);
        layout12.setBackgroundColor(Color.parseColor(ColorPares));
        layout12.setMinimumHeight(AltoContenedor);
        // 13 NEUMOCOCO CONJUGADA 13V
        LinearLayout layout13       = new LinearLayout(this);
        layout13.setOrientation(LinearLayout.HORIZONTAL);
        layout13.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox vcn13 = new CheckBox(getApplicationContext());
        vcn13.setText("NEUMOCOCO CONJUGADA 13V");
        vcn13.setTextSize(TamañoLetra);
        vcn13.setTextColor(Color.WHITE);
        vcn13.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout13.addView(vcn13);
        layout13.setBackgroundColor(Color.parseColor(ColorImpares));
        layout13.setMinimumHeight(AltoContenedor);
        // 14 MENINGOCOCICA TETRAVALENTE
        LinearLayout layout14       = new LinearLayout(this);
        layout14.setOrientation(LinearLayout.HORIZONTAL);
        layout14.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox tetravalente = new CheckBox(getApplicationContext());
        tetravalente.setText("MENINGOCOCICA TETRAVALENTE");
        tetravalente.setTextSize(TamañoLetra);
        tetravalente.setTextColor(Color.WHITE);
        tetravalente.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout14.addView(tetravalente);
        layout14.setBackgroundColor(Color.parseColor(ColorPares));
        layout14.setMinimumHeight(AltoContenedor);
        // 15 POLIO - IPV
        LinearLayout layout15       = new LinearLayout(this);
        layout15.setOrientation(LinearLayout.HORIZONTAL);
        layout15.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox ipv = new CheckBox(getApplicationContext());
        ipv.setText("POLIO - IPV");
        ipv.setTextSize(TamañoLetra);
        ipv.setTextColor(Color.WHITE);
        ipv.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout15.addView(ipv);
        layout15.setBackgroundColor(Color.parseColor(ColorImpares));
        layout15.setMinimumHeight(AltoContenedor);
        // 16 POLIO - SABIN ORAL
        LinearLayout layout16       = new LinearLayout(this);
        layout16.setOrientation(LinearLayout.HORIZONTAL);
        layout16.setVerticalGravity(Gravity.CENTER_VERTICAL);
        final CheckBox sabin = new CheckBox(getApplicationContext());
        sabin.setText("POLIO - SABIN ORAL");
        sabin.setTextSize(TamañoLetra);
        sabin.setTextColor(Color.WHITE);
        sabin.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        layout16.addView(sabin);
        layout16.setBackgroundColor(Color.parseColor(ColorPares));
        layout16.setMinimumHeight(AltoContenedor);

        // Añadir todos los Check
        mainLayout.addView(layout0);
        mainLayout.addView(layout1);
        mainLayout.addView(layout2);
        mainLayout.addView(layout3);
        mainLayout.addView(layout4);
        mainLayout.addView(layout5);
        mainLayout.addView(layout6);
        mainLayout.addView(layout7);
        mainLayout.addView(layout8);
        mainLayout.addView(layout9);
        mainLayout.addView(layout10);
        mainLayout.addView(layout11);
        mainLayout.addView(layout12);
        mainLayout.addView(layout13);
        mainLayout.addView(layout14);
        mainLayout.addView(layout15);
        mainLayout.addView(layout16);

        // Defino un ScrollView para visualizar todos
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sv.setVerticalScrollBarEnabled(true);
        sv.addView(mainLayout);

        builder.setView(sv);

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
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
                vacunas="";
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

    public void mostrar(View view){
        Bundle datos = this.getIntent().getExtras();
        String recuperada = datos.getString("VacunasAplicadas");
        Toast.makeText(getApplicationContext(),recuperada,Toast.LENGTH_SHORT).show();
    }
}
