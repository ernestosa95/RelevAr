package com.example.relevar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.relevar.MySQL.SQLitePpal;
import com.example.relevar.Recursos.EfectoresSearchAdapter;
import com.example.relevar.Recursos.ScannerQR;
import com.example.relevar.Recursos.TrabajosSearchAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class Persona extends AppCompatActivity {
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// DEFINICION DE VARIABLES GLOBALES

    // Defino diferentes constantes para el funcionamiento de la activity
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123 ;
    private int STORAGE_PERMISSION_CODE =1;

    // Defino de forma global los String para recibir y devolver la informacion
    private String date="DD-MM-AAAA";

    // Defino de manera global los EditText para ingresar informacion
    private EditText dni, Apellido, Nombre, Efector, celular, fijo, mail, obs, lotevacuna;

    // Defino de manera global los TextView para mostrar informacion
    private TextView fecha, tipovacuna, limp;

    // Defino de manera global los Button para realizar acciones
    //private Button ;
    private AutoCompleteTextView autocomplete;

    // Defino de manera global los Chekclist para crear los menus de opciones
    CheckBox hepaA, hepaB, bcg, rotavirus, quintuple, tripleviral, varicela, dtp, vph, tripeacelular,
            dt, antigripal, vcn23, vcn13, tetravalente, ipv, sabin;
    CheckBox calendario, embarazo, puerperio, personalsalud, personalesencial, viajeros,
            inmunocomprometidos, cadiologicos, respiratorios, diabeticos, prematuros, asplenicos,
            obesidad, inmunodeficiencia, conviviente,HTA, oncologicos, mayor60, otros;
    CheckBox SD, Jardin, EscuelaEspecial, PrimariaCursando, PrimarioIncompleto, PrimariaCompleta, SecundariaCursando,
    SecundariaImcompleto, SecundariaCompleto, TerciarioCursando, TerciarioImcompleto, TerciarioCompleto,
    UniersidadCursando, UniversidadImcompleto, UniversidadCompleto;

    // String constantes para educacion
    String sindato="Sin dato", jardin="Jardin", escuelaespecial="Escuela Especial", primariacurasando="Cursando Primaria",
            primarioincompleto="Primario Incompleto", primariacompleta="Primaria Completa", secundariacursando="Cursando Secundaria",
            secundariaincompleta="Secundaria Incompleta", secundariocompleto="Secundario Completo", terciariocursando="Cursando Terciario",
            terciarioincompleto="Terciario Incompleto", terciariocompleto="Terciario Completo", universidadcursando="Cursando Univeridad",
            universidadincompleto="Universidad Incompleta", universidadcompleto="Universidad Completa";
    // Defino el Interfaz para ingresar la fecha
    private DatePickerDialog.OnDateSetListener Date;
    private DatePickerDialog.OnDateSetListener Date2, Date1;

    // Defino los Array que contienen conjunto de informacion
    private ArrayList<String> categoriasPersona = new ArrayList<>();

    // Defino los RadioButton para la limpieza
    RadioButton rb1, rb2;

    ObjetoPersona Persona;
    ConstraintLayout factores, contacto, observaciones, efector, layout_ocupacion, layout_educacion, layout_vitamina,
                layout_embarazo, layout_discapacidad, layout_acompañamiento, layout_trastornosniños, layout_adicciones,
                layout_ocio, layout_violencia, layout_trastornosmentales, layout_enfermedades_cronicas;
    TextView avancefactores, avancecontacto, avanceobservaciones, avanceefector, avanceocupacion, avanceeducacion,
                avancevitamina, avanceembarazo, avancediscapacidad, avanceacompañamiento, avancetrastornosniños,
                avanceadicciones, avanceocio, avanceviolencia, avancetrastornosmentales, avanceenfermedadescronicas;

    AutoCompleteTextView autoEfector;

    TextView txtNombre, txtApellido, txtDni, txtSexo, txtNacimiento, txtNacimientoEditar;

    private TabHost tabs;
    ConstraintLayout BtnVitamina, CLVitamina;
    TextView avanceVitamina;
    //private String date="DD-MM-AAAA";

    ConstraintLayout BtnEducacion, BtnOcupacion, BtnContacto, BtnEfector, BtnObservaciones;
    ConstraintLayout BtnFactoresRiesgo, BtnDiscapacidad, BtnEmbarazo, BtnVitaminaD, BtnEnfermedadesCronicas;
    ConstraintLayout BtnAcompañamiento, BtnTranstornosNiños, BtnTranstornosMentales, BtnAdicciones, BtnViolencia, BtnOcio;

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// CLASES DE CREACION Y ACTUALIZACION DE LAS VARIABLES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

        // Evitar la rotacion
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }

        //
        categoriasPersona.add(getString(R.string.celular));
        categoriasPersona.add(getString(R.string.fijo));
        categoriasPersona.add(getString(R.string.mail));
        categoriasPersona.add(getString(R.string.factores_riesgo));
        categoriasPersona.add(getString(R.string.efector));
        categoriasPersona.add(getString(R.string.observaciones));
        categoriasPersona.add(getString(R.string.nombre_apellido_contacto));
        categoriasPersona.add(getString(R.string.telefono_contacto));
        categoriasPersona.add(getString(R.string.parentezco_contacto));
        categoriasPersona.add(getString(R.string.ocupacion));
        categoriasPersona.add(getString(R.string.educacion));
        categoriasPersona.add(getString(R.string.vitamina));
        categoriasPersona.add(getString(R.string.fecha_nacimiento));
        categoriasPersona.add(getString(R.string.ultimo_control));
        categoriasPersona.add(getString(R.string.enfermedad_relacionada_embarazo));
        categoriasPersona.add(getString(R.string.certificado_unico_discapacidad));
        categoriasPersona.add(getString(R.string.tipo_discapacidad));
        categoriasPersona.add(getString(R.string.acompañamiento));
        categoriasPersona.add(getString(R.string.transtornos_en_niños));
        categoriasPersona.add(getString(R.string.adicciones));
        categoriasPersona.add(getString(R.string.actividades_ocio));
        categoriasPersona.add(getString(R.string.donde_ocio));
        categoriasPersona.add(getString(R.string.tipo_violencia));
        categoriasPersona.add(getString(R.string.modalidad_violencia));
        categoriasPersona.add(getString(R.string.trastornos_mentales));
        categoriasPersona.add(getString(R.string.enfermedad_cronica));
        categoriasPersona.add(getString(R.string.plan_social));

        Persona = new ObjetoPersona(categoriasPersona);
        // Eliminar el action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        txtNombre = findViewById(R.id.TXTNOMBRE);
        txtApellido = findViewById(R.id.TXTAPELLIDO);
        txtDni = findViewById(R.id.TXTDNI);
        txtSexo = findViewById(R.id.TXTSEXO);
        txtNacimiento = findViewById(R.id.TXTFECHANACIMIENTO);

        // Construyo el widget para la fecha
        Date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int  year, int month, int day) {
                //Log.d(TAG, "onDateSet: date:"+year+"/"+month+"/"+day);
                int mes = month + 1;
                date=day+"-"+mes+"-"+year;
                txtNacimientoEditar.setText(Integer.toString(day)+"/"+Integer.toString(mes)+"/"+Integer.toString(year));
                Persona.Nacimiento = date;
                //Persona.CalcularEdad(year, month, day);
                }
        };

        if(getIntent().getExtras()!=null){
        Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                Persona = (ObjetoPersona) bundle.getSerializable("PERSONAEDITAR");
        }

        // Inicializo los campos de edicion
        txtDni.setText(Persona.DNI);
        txtApellido.setText(Persona.Apellido);
        txtNombre.setText(Persona.Nombre);
        txtSexo.setText(Persona.Sexo);

        //Toast.makeText(this, Persona.Nacimiento, Toast.LENGTH_SHORT).show();
        if(Persona.Nacimiento!=""){
        txtNacimiento.setText(Persona.Nacimiento);}
        else {txtNacimiento.setText("DD-MM-AAAA");}

        //Sp1.setSelection(ObtenerPosicion(Sp1, Persona.Efector));
        }

        CrearBotonera();

        // inicar estado de carga
        ColorAvanceFactores();
        ColorAvanceContacto();
        ColorAvanceObservaciones();
        ColorAvanceEfector();
        ColorAvanceOcupacion();
        ColorAvanceEducacion();
        ColorAvanceEmbarazo();
        ColorAvanceVitamina();
        ColorAvanceDiscapacidad();
        ColorAvanceAcompañamiento();
        ColorAvanceTrastornosNiños();
        ColorAvanceAdicciones();
        ColorAvanceOcio();
        ColorAvanceViolencia();
        ColorAvanceTrastornosMentales();

        // Codigo de funcionamiento de los tabs
        tabs = findViewById(R.id.TABS);
        tabs.setup();

        TextView tv = (TextView)LayoutInflater.from(this).inflate(R.layout.titulo_tabs,null);
        tv.setText("GENERAL");
        tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        TabHost.TabSpec spec = tabs.newTabSpec("mytab1");
        spec.setContent(R.id.GENERAL);
        //spec.setIndicator("GENERAL");
        spec.setIndicator(tv);
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mytab2");
        spec.setContent(R.id.FISICO);
        TextView tv1 = (TextView)LayoutInflater.from(this).inflate(R.layout.titulo_tabs,null);
        tv1.setText("ESTADO FISICO");
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        spec.setIndicator(tv1);
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mytab3");
        spec.setContent(R.id.PSICO);
        TextView tv3 = (TextView)LayoutInflater.from(this).inflate(R.layout.titulo_tabs,null);
        tv3.setText("PSICO SOCIAL");
        tv3.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        spec.setIndicator(tv3);
        tabs.addTab(spec);

        tabs.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#4A4A4A"));
        tabs.getTabWidget().getChildAt(1).setBackgroundColor(Color.BLACK);
        tabs.getTabWidget().getChildAt(2).setBackgroundColor(Color.BLACK);

        BtnVitamina = (ConstraintLayout) findViewById(R.id.BTNVITAMINA);
        CLVitamina = (ConstraintLayout) findViewById(R.id.AVANCEVITAMINA);
        avanceVitamina = (TextView) findViewById(R.id.COMPLETADOVITAMINA);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int tab = tabs.getCurrentTab();
                tabs.getTabWidget().getChildAt(0).setBackgroundColor(Color.BLACK);
                tabs.getTabWidget().getChildAt(1).setBackgroundColor(Color.BLACK);
                tabs.getTabWidget().getChildAt(2).setBackgroundColor(Color.BLACK);
                tabs.getTabWidget().getChildAt(tab).setBackgroundColor(Color.parseColor("#4A4A4A"));
            }
        });


    }

    //@Override
    protected void onStart() {
        //todo esto pa actualizr la listview
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){

            if(resultCode== RESULT_OK){

                    //Toast.makeText(this, data.getStringExtra("DNI_ESCANEADO"), Toast.LENGTH_SHORT).show();
                    txtDni.setText(data.getStringExtra("DNI_ESCANEADO"));
                    Persona.DNI = data.getStringExtra("DNI_ESCANEADO");
                    txtApellido.setText(data.getStringExtra("APELLIDO_ESCANEADO"));
                    Persona.Apellido = data.getStringExtra("APELLIDO_ESCANEADO");
                    txtNombre.setText(data.getStringExtra("NOMBRE_ESCANEADO"));
                    Persona.Nombre = data.getStringExtra("NOMBRE_ESCANEADO");
                    txtNacimiento.setText(data.getStringExtra("FECHA_NACIMIENTO_ESCANEADO"));
                    Persona.Nacimiento = data.getStringExtra("FECHA_NACIMIENTO_ESCANEADO");
                    Persona.Sexo = data.getStringExtra("SEXO_ESCANEADO");
                    txtSexo.setText(data.getStringExtra("SEXO_ESCANEADO"));
                    Persona.QR = Boolean.toString(true);
                    int año, mes, dia;
                    String[] convertir = Persona.Nacimiento.split("/");
                    dia = Integer.parseInt(convertir[0]);
                    mes = Integer.parseInt(convertir[1]);
                    año = Integer.parseInt(convertir[2]);
                    Persona.CalcularEdad(año,mes,dia);

            }}}

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

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, Date, year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // Leer QR
    public void escanear(View view){
        Intent Modif= new Intent (this, ScannerQR.class);
        startActivityForResult(Modif, 1);
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    private void CrearBotonera(){
        SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);

        //------------------------------------------------------------------------------------------
        // GENERAL
        // PARA EL AVANCE DE LA EDUCACION
        layout_educacion = (ConstraintLayout) findViewById(R.id.AVANCEEDUCACION);
        avanceeducacion = (TextView) findViewById(R.id.COMPLETADOEDUCACION);
        BtnEducacion = (ConstraintLayout) findViewById(R.id.BTNEDUCACION);
        if(!admin.EstadoBoton("EDUCACION")){
            BtnEducacion.setVisibility(View.GONE);
        }
        else{
            BtnEducacion.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DE LOS EFECTOR
        layout_ocupacion = (ConstraintLayout) findViewById(R.id.AVANCETRABAJO);
        avanceocupacion = (TextView) findViewById(R.id.COMPLETADOTRABAJO);
        BtnOcupacion = (ConstraintLayout) findViewById(R.id.BTNTRABAJO);
        if(!admin.EstadoBoton("INGRESO Y OCUPACION")){
            BtnOcupacion.setVisibility(View.GONE);
        }
        else{
            BtnOcupacion.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DE LOS CONTACTOS
        contacto = (ConstraintLayout) findViewById(R.id.AVANCECONTACTO);
        avancecontacto = (TextView) findViewById(R.id.COMPLETADOCONTACTO);
        BtnContacto = (ConstraintLayout) findViewById(R.id.BTNCONTACTO);
        if(!admin.EstadoBoton("CONTACTO")){
            BtnContacto.setVisibility(View.GONE);
        }
        else{
            BtnContacto.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DE LOS EFECTOR
        efector = (ConstraintLayout) findViewById(R.id.AVANCEEFECTOR);
        avanceefector = (TextView) findViewById(R.id.COMPLETADOEFECTOR);
        BtnEfector = (ConstraintLayout) findViewById(R.id.BTNEFECTOR);
        if(!admin.EstadoBoton("EFECTOR")){
            BtnEfector.setVisibility(View.GONE);
        }
        else{
            BtnEfector.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DE LOS OBSERVACIONES
        observaciones = (ConstraintLayout) findViewById(R.id.AVANCEOBSERVACIONES);
        avanceobservaciones = (TextView) findViewById(R.id.COMPLETADOOBSERVACIONES);
        BtnObservaciones = (ConstraintLayout) findViewById(R.id.BTNOBSERVACIONES);
        if(!admin.EstadoBoton("OBSERVACIONES")){
            BtnObservaciones.setVisibility(View.GONE);
        }
        else{
            BtnObservaciones.setVisibility(View.VISIBLE);
        }

        // Fisico
        // PARA EL AVANCE DE LOS FACTORES
        factores = (ConstraintLayout) findViewById(R.id.AVANCEFACTORES);
        avancefactores = (TextView) findViewById(R.id.COMPLETADOFACTORES);
        BtnFactoresRiesgo = (ConstraintLayout) findViewById(R.id.BTNFACTORES);
        if(!admin.EstadoBoton("FACTORES DE RIESGO")){
            BtnFactoresRiesgo.setVisibility(View.GONE);
        }
        else{
            BtnFactoresRiesgo.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DE LA EMBARAZO
        layout_discapacidad = (ConstraintLayout) findViewById(R.id.AVANCEDISCAPACIDAD);
        avancediscapacidad = (TextView) findViewById(R.id.COMPLETADODISCAPACIDAD);
        BtnDiscapacidad = (ConstraintLayout) findViewById(R.id.BTNDISCAPACIDAD);
        if(!admin.EstadoBoton("DISCAPACIDAD")){
            BtnDiscapacidad.setVisibility(View.GONE);
        }
        else{
            BtnDiscapacidad.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DE LA EMBARAZO
        layout_embarazo = (ConstraintLayout) findViewById(R.id.AVANCEEMBARAZO);
        avanceembarazo = (TextView) findViewById(R.id.COMPLETADOEMBARAZO);
        BtnEmbarazo = (ConstraintLayout) findViewById(R.id.BTNEMBARAZO);
        if(!admin.EstadoBoton("EMBARAZO")){
            BtnEmbarazo.setVisibility(View.GONE);
        }
        else{
            BtnEmbarazo.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DE LA VITAMINA
        layout_vitamina = (ConstraintLayout) findViewById(R.id.AVANCEVITAMINA);
        avancevitamina = (TextView) findViewById(R.id.COMPLETADOVITAMINA);
        BtnVitamina = (ConstraintLayout) findViewById(R.id.BTNVITAMINA);
        if(!admin.EstadoBoton("VITAMINA D")){
            BtnVitamina.setVisibility(View.GONE);
        }
        else{
            BtnVitamina.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DEL ACOMPAÑAMIENTO
        layout_acompañamiento = (ConstraintLayout) findViewById(R.id.AVANCEACOMPAÑAMIENTO);
        avanceacompañamiento = (TextView) findViewById(R.id.COMPLETADOACOMPAÑAMIENTO);
        BtnAcompañamiento = (ConstraintLayout) findViewById(R.id.BTNACOMPAÑAMIENTO);
        if(!admin.EstadoBoton("ACOMPAÑAMIENTO")){
            BtnAcompañamiento.setVisibility(View.GONE);
        }
        else{
            BtnAcompañamiento.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DEL ACOMPAÑAMIENTO
        layout_trastornosniños = (ConstraintLayout) findViewById(R.id.AVANCETRANSTORNOSENNIÑOS);
        avancetrastornosniños = (TextView) findViewById(R.id.COMPLETADOTRANSTORNOSENNIÑOS);
        BtnTranstornosNiños = (ConstraintLayout) findViewById(R.id.BTNTRANSTORNOSENNIÑOS);
        if(!admin.EstadoBoton("TRASTORNOS EN NIÑOS")){
            BtnTranstornosNiños.setVisibility(View.GONE);
        }
        else{
            BtnTranstornosNiños.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DEL ACOMPAÑAMIENTO
        layout_trastornosmentales = (ConstraintLayout) findViewById(R.id.AVANCETRANSTORNOSMENTALES);
        avancetrastornosmentales = (TextView) findViewById(R.id.COMPLETADOTRANSTORNOSMENTALES);
        BtnTranstornosMentales = (ConstraintLayout) findViewById(R.id.BTNTRANSTORNOSMENTALES);
        if(!admin.EstadoBoton("TRASTORNOS MENTALES")){
            BtnTranstornosMentales.setVisibility(View.GONE);
        }
        else{
            BtnTranstornosMentales.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DEL ACOMPAÑAMIENTO
        layout_adicciones = (ConstraintLayout) findViewById(R.id.AVANCEADICCIONES);
        avanceadicciones = (TextView) findViewById(R.id.COMPLETADOADICCIONES);
        BtnAdicciones = (ConstraintLayout) findViewById(R.id.BTNADICCIONES);
        if(!admin.EstadoBoton("ADICCIONES")){
            BtnAdicciones.setVisibility(View.GONE);
        }
        else{
            BtnAdicciones.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DEL ACOMPAÑAMIENTO
        layout_violencia = (ConstraintLayout) findViewById(R.id.AVANCEVIOLENCIA);
        avanceviolencia = (TextView) findViewById(R.id.COMPLETADOVIOLENCIA);
        BtnViolencia = (ConstraintLayout) findViewById(R.id.BTNVIOLENCIA);
        if(!admin.EstadoBoton("VIOLENCIA")){
            BtnViolencia.setVisibility(View.GONE);
        }
        else{
            BtnViolencia.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DEL ACOMPAÑAMIENTO
        layout_ocio = (ConstraintLayout) findViewById(R.id.AVANCEOCIO);
        avanceocio = (TextView) findViewById(R.id.COMPLETADOOCIO);
        BtnOcio = (ConstraintLayout) findViewById(R.id.BTNOCIO);
        if(!admin.EstadoBoton("OCIO")){
            BtnOcio.setVisibility(View.GONE);
        }
        else{
            BtnOcio.setVisibility(View.VISIBLE);
        }

        // PARA EL AVANCE DEL ACOMPAÑAMIENTO
        layout_enfermedades_cronicas = (ConstraintLayout) findViewById(R.id.AVANCEENFERMEDADCRONICA);
        avanceenfermedadescronicas = (TextView) findViewById(R.id.COMPLETADOENFERMEDADCRONICA);
        BtnEnfermedadesCronicas = (ConstraintLayout) findViewById(R.id.BTNENFERMEDADCRONICA);
        if(!admin.EstadoBoton("ENFERMEDADES CRONICAS")){
            BtnEnfermedadesCronicas.setVisibility(View.GONE);
        }
        else{
            BtnEnfermedadesCronicas.setVisibility(View.VISIBLE);
        }
        admin.close();
    }

    public void BotonesPersona(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_listado_botones, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final SQLitePpal admin = new SQLitePpal(getBaseContext(), "DATA_PRINCIPAL", null, 1);

        final Switch educacion = view1.findViewById(R.id.SWITCHEDUCACION);
        if(admin.EstadoBoton("EDUCACION")){
            educacion.setChecked(true);
        }
        educacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint({"WrongConstant", "ResourceType"})
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(educacion.getText().toString());
                    BtnEducacion.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(educacion.getText().toString());
                    BtnEducacion.setVisibility(View.GONE);
                }
            }

        });

        final Switch ocupacion = view1.findViewById(R.id.SWITCHOCUPACION);
        if(admin.EstadoBoton("INGRESO Y OCUPACION")){
            ocupacion.setChecked(true);
        }
        ocupacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint({"WrongConstant", "ResourceType"})
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(ocupacion.getText().toString());
                    BtnOcupacion.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(ocupacion.getText().toString());
                    BtnOcupacion.setVisibility(View.GONE);
                }
            }

        });

        final Switch contacto = view1.findViewById(R.id.SWITCHCONTACTO);
        if(admin.EstadoBoton("CONTACTO")){
            contacto.setChecked(true);
        }
        contacto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint({"WrongConstant", "ResourceType"})
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(contacto.getText().toString());
                    BtnContacto.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(contacto.getText().toString());
                    BtnContacto.setVisibility(View.GONE);
                }
            }

        });

        final Switch efector = view1.findViewById(R.id.SWITCHEFECTOR);
        if(admin.EstadoBoton("EFECTOR")){
            efector.setChecked(true);
        }
        efector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint({"WrongConstant", "ResourceType"})
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(efector.getText().toString());
                    BtnEfector.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(efector.getText().toString());
                    BtnEfector.setVisibility(View.GONE);
                }
            }

        });

        final Switch observaciones = view1.findViewById(R.id.SWITCHOBSERVACIONES);
        if(admin.EstadoBoton("OBSERVACIONES")){
            observaciones.setChecked(true);
        }
        observaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint({"WrongConstant", "ResourceType"})
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(observaciones.getText().toString());
                    BtnObservaciones.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(observaciones.getText().toString());
                    BtnObservaciones.setVisibility(View.GONE);
                }
            }

        });


        final Switch factores_riesgo = view1.findViewById(R.id.SWITCHFACTORESRIESGO);
        if(admin.EstadoBoton("FACTORES DE RIESGO")){
            factores_riesgo.setChecked(true);
        }
        factores_riesgo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint({"WrongConstant", "ResourceType"})
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(factores_riesgo.getText().toString());
                    BtnFactoresRiesgo.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(factores_riesgo.getText().toString());
                    BtnFactoresRiesgo.setVisibility(View.GONE);
                }
            }

        });

        final Switch discapacidad = view1.findViewById(R.id.SWITCHDISCAPACIDAD);
        if (admin.EstadoBoton("DISCAPACIDAD")){
            discapacidad.setChecked(true);
        }
        discapacidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(discapacidad.getText().toString());
                    BtnDiscapacidad.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(discapacidad.getText().toString());
                    BtnDiscapacidad.setVisibility(View.GONE);
                }
            }
        });

        final Switch embarazo = view1.findViewById(R.id.SWITCHEMBARAZO);
        if(admin.EstadoBoton("EMBARAZO")){
            embarazo.setChecked(true);
        }
        embarazo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(embarazo.getText().toString());
                    BtnEmbarazo.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(embarazo.getText().toString());
                    BtnEmbarazo.setVisibility(View.GONE);
                }
            }
        });

        final Switch vitamina_d = view1.findViewById(R.id.SWITCHVITAMINAD);
        if(admin.EstadoBoton("VITAMINA D")){
            vitamina_d.setChecked(true);
        }
        vitamina_d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(vitamina_d.getText().toString());
                    BtnVitamina.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(vitamina_d.getText().toString());
                    BtnVitamina.setVisibility(View.GONE);
                }
            }
        });


        final Switch acompañamiento = view1.findViewById(R.id.SWITCHACOMPAÑAMIENTO);
        if(admin.EstadoBoton("ACOMPAÑAMIENTO")){
            acompañamiento.setChecked(true);
        }
        acompañamiento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(acompañamiento.getText().toString());
                    BtnAcompañamiento.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(acompañamiento.getText().toString());
                    BtnAcompañamiento.setVisibility(View.GONE);
                }
            }
        });

        final Switch transtornos_niños = view1.findViewById(R.id.SWITCHTRANSTORNOSENNIÑOS);
        if(admin.EstadoBoton("TRASTORNOS EN NIÑOS")){
            transtornos_niños.setChecked(true);
        }
        transtornos_niños.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(transtornos_niños.getText().toString());
                    BtnTranstornosNiños.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(transtornos_niños.getText().toString());
                    BtnTranstornosNiños.setVisibility(View.GONE);
                }
            }
        });

        final Switch transtornos_mentales = view1.findViewById(R.id.SWITCHTRANSTORNOSMENTALES);
        if(admin.EstadoBoton("TRASTORNOS MENTALES")){
            transtornos_mentales.setChecked(true);
        }
        transtornos_mentales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(transtornos_mentales.getText().toString());
                    BtnTranstornosMentales.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(transtornos_mentales.getText().toString());
                    BtnTranstornosMentales.setVisibility(View.GONE);
                }
            }
        });

        final Switch adicciones = view1.findViewById(R.id.SWITCHADICCIONES);
        if(admin.EstadoBoton("ADICCIONES")){
            adicciones.setChecked(true);
        }
        adicciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(adicciones.getText().toString());
                    BtnAdicciones.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(adicciones.getText().toString());
                    BtnAdicciones.setVisibility(View.GONE);
                }
            }
        });

        final Switch violencia = view1.findViewById(R.id.SWITCHVIOLENCIA);
        if(admin.EstadoBoton("VIOLENCIA")){
            violencia.setChecked(true);
        }
        violencia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(violencia.getText().toString());
                    BtnViolencia.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(violencia.getText().toString());
                    BtnViolencia.setVisibility(View.GONE);
                }
            }
        });

        final Switch ocio = view1.findViewById(R.id.SWITCHOCIO);
        if(admin.EstadoBoton("OCIO")){
            ocio.setChecked(true);
        }
        ocio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(ocio.getText().toString());
                    BtnOcio.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(ocio.getText().toString());
                    BtnOcio.setVisibility(View.GONE);
                }
            }
        });

        final Switch enfermedadcronica = view1.findViewById(R.id.SWITCHENFERMEDADCRONICA);
        if(admin.EstadoBoton("ENFERMEDADES CRONICAS")){
            enfermedadcronica.setChecked(true);
        }
        enfermedadcronica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin.ActivarBoton(enfermedadcronica.getText().toString());
                    BtnEnfermedadesCronicas.setVisibility(View.VISIBLE);
                } else {
                    admin.DesactivarBoton(enfermedadcronica.getText().toString());
                    BtnEnfermedadesCronicas.setVisibility(View.GONE);
                }
            }
        });

        admin.close();
        final Button listo = view1.findViewById(R.id.LISTOBOTON);
        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //recreate();
            }
        });

        final Switch dengue = view1.findViewById(R.id.SWITCHDENGUE);
        dengue.setVisibility(View.GONE);
        final Switch inspeccionEcterior = view1.findViewById(R.id.SWITCHINSPECCIONEXTERIOR);
        inspeccionEcterior.setVisibility(View.GONE);
        final Switch serviciosBasicos = view1.findViewById(R.id.SWITCHSERVICIOSBASICOS);
        serviciosBasicos.setVisibility(View.GONE);
        final Switch vivienda = view1.findViewById(R.id.SWITCHVIVIENDA);
        vivienda.setVisibility(View.GONE);

    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// Tomar los datos de identificacion de la persona
    public void DatosIdentificar(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_informacion_personal, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText edtNombre = view1.findViewById(R.id.EDTNOMBRE);
        if(Persona.Nombre.length()!=0){edtNombre.setText(Persona.Nombre);}
        final EditText edtApellido = view1.findViewById(R.id.EDTAPELLIDO);
        if(Persona.Apellido.length()!=0){edtApellido.setText(Persona.Apellido);}
        final EditText edtDni = view1.findViewById(R.id.EDTDNI);
        if(Persona.DNI.length()!=0){edtDni.setText(Persona.DNI);}
        txtNacimientoEditar = view1.findViewById(R.id.EDTFECHANACIMIENTO);
        txtNacimientoEditar.setText(Persona.Nacimiento);
        final RadioButton masculino = view1.findViewById(R.id.MASCULINO);
        final RadioButton femenino = view1.findViewById(R.id.FEMENINO);
        if (Persona.Sexo.equals("M")){masculino.setChecked(true);}
        if (Persona.Sexo.equals("F")){femenino.setChecked(true);}

        final Button guardar = view1.findViewById(R.id.GUARDARINFORMACIONPERSONAL);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Persona.Nombre = edtNombre.getText().toString();
                txtNombre.setText(Persona.Nombre);
                Persona.Apellido = edtApellido.getText().toString();
                txtApellido.setText(Persona.Apellido);
                if(masculino.isChecked()){Persona.Sexo = "M";}
                if(femenino.isChecked()){Persona.Sexo = "F";}
                txtSexo.setText(Persona.Sexo);
                Persona.DNI = edtDni.getText().toString();
                txtDni.setText(Persona.DNI);
                Persona.Nacimiento = txtNacimientoEditar.getText().toString();
                txtNacimiento.setText(Persona.Nacimiento);
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARINFORMACIONPERSONAL);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
            //AvVacuna.setText(aux);
            //AvVacuna.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==2){
            //Vacuna.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvVacuna.setText("2/2");
            //AvVacuna.setBackgroundColor(Color.parseColor("#8BC34A"));
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
// SECCION DE LAS VISTA DE LAS VACUNAS

    public void Educacion(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_educacion, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final RadioGroup Educacion = view1.findViewById(R.id.GroupEducacion);
        if(Persona.Educacion.length()!=0){
        for(int i=0; i<Educacion.getChildCount(); i++){
          RadioButton rb = (RadioButton) Educacion.findViewById(Educacion.getChildAt(i).getId());
          if(rb.getText().toString().split(" ")[0].equals(Persona.Educacion.split(" ")[0])){
              rb.setChecked(true);
          }
        }}

        final RadioButton primaria = view1.findViewById(R.id.PRIMARIO);
        primaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CompletoIncompletoCursando(primaria.getText().toString());
               dialog.dismiss();
            }
        });
        final RadioButton secundaria = view1.findViewById(R.id.SECUNDARIO);
        secundaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompletoIncompletoCursando(secundaria.getText().toString());
                dialog.dismiss();
            }
        });
        final RadioButton terciaria = view1.findViewById(R.id.TERCIARIO);
        terciaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompletoIncompletoCursando(terciaria.getText().toString());
                dialog.dismiss();
            }
        });
        final RadioButton universidad = view1.findViewById(R.id.UNIVERSIDAD);
        universidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompletoIncompletoCursando(universidad.getText().toString());
                dialog.dismiss();
            }
        });

        final Button guardar = view1.findViewById(R.id.GUARDAREDUCACION);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) Educacion.findViewById(Educacion.getCheckedRadioButtonId());
                if(Educacion.findViewById(Educacion.getCheckedRadioButtonId())!=null){
                Persona.Educacion = rb.getText().toString();}
                dialog.dismiss();
                ColorAvanceEducacion();
            }
        });
    }

    // Cambia lo colores de avance de factores
    private void ColorAvanceEducacion(){
        if (Persona.Educacion.length()!=0){
            layout_educacion.setBackgroundResource(R.drawable.verde);
            avanceeducacion.setText(getString(R.string.completado)+" 100%");
        }
    }

    // Detecto los Check seleccionados anteriormente
    private void CheckSeleccionadosEducacion(String Vacunas){
        //Toast.makeText(getApplicationContext(), Vacunas, Toast.LENGTH_SHORT).show();
        if(Vacunas!=""){
            String[] vac =Vacunas.split(",");
            for (int x = 0; x < vac.length; x++) {
                //Toast.makeText(getApplicationContext(), Vacunas, Toast.LENGTH_SHORT).show();
                if (vac[x].equals(jardin)){
                    Jardin.setChecked(true);
                }
                if (vac[x].equals(escuelaespecial)){
                    EscuelaEspecial.setChecked(true);
                }
                if (vac[x].equals(primariacurasando)){
                    PrimariaCursando.setChecked(true);
                }
                if (vac[x].equals(primariacompleta)){
                    PrimarioIncompleto.setChecked(true);
                }
                if (vac[x].equals(primarioincompleto)){
                    PrimarioIncompleto.setChecked(true);
                }
                if (vac[x].equals(secundariacursando)){
                    SecundariaCursando.setChecked(true);
                }
                if (vac[x].equals(secundariocompleto)){
                    SecundariaCompleto.setChecked(true);
                }
                if (vac[x].equals(secundariaincompleta)){
                    SecundariaImcompleto.setChecked(true);
                }
                if (vac[x].equals(terciariocursando)){
                    TerciarioCursando.setChecked(true);
                }
                if (vac[x].equals(terciariocompleto)){
                    TerciarioCompleto.setChecked(true);
                }
                if (vac[x].equals(terciarioincompleto)){
                    TerciarioImcompleto.setChecked(true);
                }
                if (vac[x].equals(universidadcursando)){
                    UniersidadCursando.setChecked(true);
                }
                if (vac[x].equals(universidadincompleto)){
                    UniversidadImcompleto.setChecked(true);
                }
                if (vac[x].equals(universidadcompleto)){
                    UniversidadCompleto.setChecked(true);
                }
                if (vac[x].equals(sindato)){
                    SD.setChecked(true);
                }
            }
        }}

    private void CompletoIncompletoCursando(final String nivel){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_educacion_completitud, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final RadioGroup completitud = view1.findViewById(R.id.COMPLETITUD);
        /*if(Persona.Educacion.length()!=0){
            for(int i=0; i<completitud.getChildCount(); i++){
                RadioButton rb = (RadioButton) completitud.findViewById(completitud.getChildAt(i).getId());
                int ultimoPersonaEducacion = Persona.Educacion.split(" ").length;
                int ultimoEducacion = rb.getText().toString().split(" ").length;
                if(rb.getText().toString().split(" ")[ultimoEducacion].equals(Persona.Educacion.split(" ")[ultimoPersonaEducacion])){
                    rb.setChecked(true);
                }
            }}*/

        final Button guardar = view1.findViewById(R.id.GUARDARCOMPLETITUD);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(completitud.findViewById(completitud.getCheckedRadioButtonId())!=null){
                RadioButton rb = (RadioButton) completitud.findViewById(completitud.getCheckedRadioButtonId());
                Persona.Educacion = nivel +" "+ rb.getText().toString();}
                ColorAvanceEducacion();
            }
        });
    }

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
        oncologicos = PersonalCheck(mainLayout,"ONCOLOGICOS", TamañoLetra, ColorPares, AltoContenedor);
        mayor60 = PersonalCheck(mainLayout,"MAYOR DE 60", TamañoLetra, ColorImpares, AltoContenedor);
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
                if (oncologicos.isChecked()){
                    if (riesgos==null){
                        riesgos="ONCOLOGICO";
                        //codigoriesgo="";
                    } else {
                        riesgos+=",ONCOLOGICO";
                        //codigoriesgo+="";
                    }}
                if (mayor60.isChecked()){
                    if (riesgos==null){
                        riesgos="MAYOR DE 60";
                        //codigoriesgo="";
                    } else {
                        riesgos+=",MAYOR DE 60";
                        //codigoriesgo+="";
                    }}
                if (otros.isChecked()){
                    if (riesgos==null){
                        riesgos="OTROS";
                        codigoriesgo="16";
                    } else {
                        riesgos+=",OTROS";
                        codigoriesgo+=",16";}}
                if(riesgos==null){
                    Persona.FactoresDeRiesgo="";
                    Persona.CodfigoFactorRiesgo="";
                }
                else{
                Persona.FactoresDeRiesgo=riesgos;
                Persona.CodfigoFactorRiesgo=codigoriesgo;}
                //Toast.makeText(getApplicationContext(), Persona.FactoresDeRiesgo, Toast.LENGTH_SHORT).show();
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

        float avance = 0;
        if (Persona.FactoresDeRiesgo.length()!=0){
            avance+=1;
        }

        if(avance==0){
            factores.setBackgroundResource(R.drawable.rojo);
            avancefactores.setText(getString(R.string.completado)+" 00%");
        }

        /*if(avance>0 && avance<2){
            layout_acompañamiento.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avancediscapacidad.setText(aux);
        }*/

        if(avance==1){
            factores.setBackgroundResource(R.drawable.verde);
            avancefactores.setText(getString(R.string.completado)+" 100%");
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
            if (vac[x].equals("DIABETICOS")){
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
            if (vac[x].equals("ONCOLOGICO")){
                oncologicos.setChecked(true);
            }
            if (vac[x].equals("MAYOR DE 60")){
                mayor60.setChecked(true);
            }
            if (vac[x].equals("HIPERTENSO")){
                HTA.setChecked(true);
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

    public void Contactos(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_contacto, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText celular = view1.findViewById(R.id.CELULAR);
        final EditText fijo = view1.findViewById(R.id.FIJO);
        final EditText mail = view1.findViewById(R.id.MAIL);
        final EditText nombrecontacto = view1.findViewById(R.id.NOMBREAPELLIDOCONTACTO);
        final EditText telcontacto = view1.findViewById(R.id.TELEFONOCONTACTO);
        final EditText parentezco = view1.findViewById(R.id.PARENTESCOCONTACTO);

        // Si ya tengo valores de contactos debo inicializar
        if(Persona.Celular!=""){celular.setText(Persona.Celular);}
        if(Persona.Fijo!=""){fijo.setText(Persona.Fijo);}
        if(Persona.Mail!=""){mail.setText(Persona.Mail);}
        if(Persona.NombreContacto!=""){nombrecontacto.setText(Persona.NombreContacto);}
        if(Persona.TelefonoContacto!=""){telcontacto.setText(Persona.TelefonoContacto);}
        if(Persona.ParentezcoContacto!=""){parentezco.setText(Persona.ParentezcoContacto);}

        Button guardar = view1.findViewById(R.id.GUARDAR2);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Persona.Celular = celular.getText().toString();
                Persona.Fijo = fijo.getText().toString();
                Persona.Mail = mail.getText().toString();
                Persona.NombreContacto = nombrecontacto.getText().toString();
                Persona.TelefonoContacto = telcontacto.getText().toString();
                Persona.ParentezcoContacto = parentezco.getText().toString();
                ColorAvanceContacto();
                dialog.dismiss();
            }
        });

        Button cancelar = view1.findViewById(R.id.CANCELAR2);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceContacto(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.Celular.length()!=0){
            avance+=1;
        }
        if (Persona.Fijo.length()!=0){
            avance+=1;
        }
        if (Persona.Mail.length()!=0){
            avance+=1;
        }
        if (Persona.NombreContacto.length()!=0){
            avance+=1;
        }
        if (Persona.TelefonoContacto.length()!=0){
            avance+=1;
        }
        if (Persona.ParentezcoContacto.length()!=0){
            avance+=1;
        }
        if(avance==1 || avance==2 || avance==3 || avance==4 || avance==5){
            contacto.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/6)*100);
            //Toast.makeText(getApplicationContext(), Double.toString(porcentaje), Toast.LENGTH_SHORT).show();
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avancecontacto.setText(aux);
            //AvContacto.setText(aux);
            //AvContacto.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==6){
            contacto.setBackgroundResource(R.drawable.verde);
            avancecontacto.setText(getString(R.string.completado)+" 100%");
            //Contacto.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvContacto.setText("3/3");
            //AvContacto.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// SECCION DE CARGA DE EFECTORES

    public void Efector(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_efector, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // AUTOCOMPLETE TEXTVIEW DE LOS TRABAJOS
        autoEfector = view1.findViewById(R.id.AutoEfector);
        List<String> efectores = new ArrayList<String>();
        EfectoresSearchAdapter searchAdapter = new EfectoresSearchAdapter(getApplicationContext(), efectores);
        autoEfector.setThreshold(1);
        autoEfector.setAdapter(searchAdapter);
        if(Persona.Efector.length()!=0) autoEfector.setText(Persona.Efector);

        final Button guardar = view1.findViewById(R.id.GUARDAREFECTOR);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Persona.Efector = autoEfector.getText().toString();
                dialog.dismiss();
                ColorAvanceEfector();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELAREFECTOR);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final TextView AgregarManualmente = view1.findViewById(R.id.AGREGARMANUALMENTEEFECTOR);
        AgregarManualmente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EfectorManual();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceEfector(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.Efector.length()!=0){
            avance+=1;
        }

        if(avance==1){
            efector.setBackgroundResource(R.drawable.verde);
            avanceefector.setText(getString(R.string.completado)+" 100%");
        }
    }

    private void EfectorManual(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View viewManual = Inflater.inflate(R.layout.alert_nuevo_encuestador, null);
        builder.setView(viewManual);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView encabezado = viewManual.findViewById(R.id.textView4);
        encabezado.setText(R.string.nuevo_efector);

        final TextView NuevoEfector = viewManual.findViewById(R.id.EditNuevoEncuestador);
        NuevoEfector.setHint(R.string.nuevo_efector);

        final Button guardar = viewManual.findViewById(R.id.GUARDAR);
        guardar.setText(R.string.listo);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Persona.Ocupacion = NuevoTrabajo.getText().toString().toUpperCase();
                autoEfector.setText(NuevoEfector.getText().toString().toUpperCase());
                dialog.dismiss();
            }
        });

        final Button cancelar = viewManual.findViewById(R.id.CANCELAR);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
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
        /*LinearLayout layoutlimp       = new LinearLayout(this);
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
        mainLayout.addView(layoutlimp);*/

        // Defino el Layaout que va a contener los radiobutton
        //LinearLayout mainLayout1       = new LinearLayout(this);
        /*RadioGroup mainLayout1 = new RadioGroup(this);
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
        mainLayout.addView(mainLayout1);*/

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
        layout0.setMinimumHeight(400);
        layout0.addView(obs);


        mainLayout.addView(layout0);

        // Add OK and Cancel buttons
        builder.setPositiveButton("LISTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked OK
                Persona.Observaciones = obs.getText().toString();
                //if(rb1.isChecked()==true){Persona.Limpieza="SI";}
                //if(rb2.isChecked()==true){Persona.Limpieza="NO";}

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
            //if(Persona.Limpieza.equals("SI")){rb1.setChecked(true);}
            //if(Persona.Limpieza.equals("NO")){rb2.setChecked(true);}
        }
    }

    // Cambia los colores de los botones de llenado de Observaciones
    private void ColorAvanceObservaciones(){
        // Cambio los colores de avance
        int avance = 0;
        if (Persona.Observaciones.length()!=0){
            avance+=1;
        }
        /*if (Persona.Limpieza.length()!=0){
            avance+=1;
        }*/

        if(avance==1){
            observaciones.setBackgroundResource(R.drawable.verde);
            avanceobservaciones.setText(getString(R.string.completado)+" 100%");
            //Observacion.setBackgroundColor(Color.parseColor("#FFA07A"));
            //String aux = Integer.toString(avance)+"/2";
            //AvObs.setText(aux);
            //AvObs.setBackgroundColor(Color.parseColor("#FFA07A"));
        }
        if(avance==2){
            observaciones.setBackgroundResource(R.drawable.verde);
            avanceobservaciones.setText(getString(R.string.completado)+" 100%");
            //Observacion.setBackgroundColor(Color.parseColor("#8BC34A"));
            //AvObs.setText("2/2");
            //AvObs.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// SECCION DE DEFINICION DE LAS VISTAS DE LOS CONTACTOS

    public void Ocupacion(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_ocupacion, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // AUTOCOMPLETE TEXTVIEW DE LOS TRABAJOS
        autocomplete = view1.findViewById(R.id.AutoTrabajos);
        List<String> trabajos = new ArrayList<String>();
        TrabajosSearchAdapter searchAdapter = new TrabajosSearchAdapter(getApplicationContext(), trabajos);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(searchAdapter);

        final RadioButton RbtSITrabajo = view1.findViewById(R.id.SITRABAJO);
        final RadioButton RbtNOTrabajo = view1.findViewById(R.id.NOTRABAJO);


        // Si ya tengo valores de contactos debo inicializar
        if(Persona.Ocupacion!=""){
            if(!Persona.Ocupacion.equals("DESOCUPADO")){
                autocomplete.setText(Persona.Ocupacion);
                RbtSITrabajo.setChecked(true);}
            else{
                RbtNOTrabajo.setChecked(true);
                autocomplete.setText(Persona.Ocupacion);
            }}

        final EditText edtPlanSocial = view1.findViewById(R.id.EDTTXTPLANSOCIAL);
        if(Persona.PlanSocial.length()!=0){
            edtPlanSocial.setText(Persona.PlanSocial);
        }

        Button guardar = view1.findViewById(R.id.GUARDAR3);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!RbtSITrabajo.isChecked() && !RbtNOTrabajo.isChecked()){
                    Toast.makeText(getApplicationContext(), "DEBE COMPLETAR SI LA PERSONA TIENE O NO OCUPACION", Toast.LENGTH_SHORT).show();
                }else{
                if(RbtSITrabajo.isChecked()){
                    if(autocomplete.getText().toString().length()!=0){
                        Persona.Ocupacion=autocomplete.getText().toString();
                        Persona.PlanSocial = edtPlanSocial.getText().toString();
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "INGRESE UNA OCUPACIÓN", Toast.LENGTH_SHORT).show();
                    }
                }
                if (RbtNOTrabajo.isChecked()){
                    Persona.Ocupacion = "DESOCUPADO";
                    Persona.PlanSocial = edtPlanSocial.getText().toString();
                    dialog.dismiss();
                }}


                ColorAvanceOcupacion();

            }
        });

        Button cancelar = view1.findViewById(R.id.CANCELAR3);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        final TextView AgregarManualmente = view1.findViewById(R.id.AGREGARMANUALMENTE);
        AgregarManualmente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrabajoManual();
                //dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceOcupacion(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.Ocupacion.length()!=0){
            avance+=1;
        }

        if(Persona.PlanSocial.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_ocupacion.setBackgroundResource(R.drawable.rojo);
            avanceocupacion.setText(getString(R.string.completado)+" 00%");
        }

        if(avance>0 && avance<2){
            layout_ocupacion.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceocupacion.setText(aux);
        }

        if(avance==2){
            layout_ocupacion.setBackgroundResource(R.drawable.verde);
            avanceocupacion.setText(getString(R.string.completado)+" 100%");
        }
    }

    private void TrabajoManual(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View viewManual = Inflater.inflate(R.layout.alert_nuevo_encuestador, null);
        builder.setView(viewManual);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView encabezado = viewManual.findViewById(R.id.textView4);
        encabezado.setText(R.string.nuevo_trabajo);

        final TextView NuevoTrabajo = viewManual.findViewById(R.id.EditNuevoEncuestador);
        NuevoTrabajo.setHint(R.string.nuevo_trabajo);

        final Button guardar = viewManual.findViewById(R.id.GUARDAR);
        guardar.setText(R.string.listo);
        guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Persona.Ocupacion = NuevoTrabajo.getText().toString().toUpperCase();
                        autocomplete.setText(NuevoTrabajo.getText().toString().toUpperCase());
                        dialog.dismiss();
                    }
        });

        final Button cancelar = viewManual.findViewById(R.id.CANCELAR);
        cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
        });
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // VITAMINA D
    public void VitaminaD(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_vitamina, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final RadioGroup vitamina = view1.findViewById(R.id.GROUPVITAMINA);
        final RadioButton siVitamina = view1.findViewById(R.id.SIVITAMINA);
        final RadioButton noVitamina = view1.findViewById(R.id.NOVITAMINA);
        if(Persona.Vitamina.equals("SI")){siVitamina.setChecked(true);}
        if(Persona.Vitamina.equals("NO")){noVitamina.setChecked(true);}

        final Button guardar = view1.findViewById(R.id.GUARDARVITAMINA);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton selec = vitamina.findViewById(vitamina.getCheckedRadioButtonId());
                if(selec!=null){
                String seleccionado = selec.getText().toString();
                Persona.Vitamina = seleccionado;}
                //Toast.makeText(getApplicationContext(), seleccionado, Toast.LENGTH_SHORT).show();
                ColorAvanceVitamina();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARVITAMINA);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceVitamina(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.Vitamina.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_vitamina.setBackgroundResource(R.drawable.rojo);
            avancevitamina.setText(getString(R.string.completado)+" 00%");
        }

        if(avance==1){
            layout_vitamina.setBackgroundResource(R.drawable.verde);
            avancevitamina.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // EMBARAZO
    public void Embarazo(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_embarazo, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView fechaDeParto = view1.findViewById(R.id.FECHADEPARTO);
        if(Persona.FechaParto.length()!=0){fechaDeParto.setText(Persona.FechaParto);}
        final TextView ultimoControl = view1.findViewById(R.id.FECHADECONTROL);
        if(Persona.UltimoControlEmbarazo.length()!=0){ultimoControl.setText(Persona.UltimoControlEmbarazo);}

        // Construyo el widget para la fecha
        Date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int  year, int month, int day) {
                //Log.d(TAG, "onDateSet: date:"+year+"/"+month+"/"+day);
                int mes = month + 1;
                fechaDeParto.setText(Integer.toString(day)+"/"+Integer.toString(mes)+"/"+Integer.toString(year));
            }
        };

        // Construyo el widget para la fecha
        Date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int  year, int month, int day) {
                //Log.d(TAG, "onDateSet: date:"+year+"/"+month+"/"+day);
                int mes = month + 1;
                ColorAvanceEmbarazo();
                ultimoControl.setText(Integer.toString(day)+"/"+Integer.toString(mes)+"/"+Integer.toString(year));
            }
        };

        final EditText editEnfermedad = view1.findViewById(R.id.EDTENFERMEDAD);
        if(Persona.EnfermedadEmbarazo.length()!=0){editEnfermedad.setText(Persona.EnfermedadEmbarazo);}

        final Button guardar = view1.findViewById(R.id.GUARDAREMBARAZO);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Persona.FechaParto = fechaDeParto.getText().toString();
               // Toast.makeText(getBaseContext(), Persona.FechaParto, Toast.LENGTH_SHORT).show();
                Persona.UltimoControlEmbarazo = ultimoControl.getText().toString();
                Persona.EnfermedadEmbarazo = editEnfermedad.getText().toString();
                ColorAvanceEmbarazo();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELAREMBARAZO);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Se inicializa el scrollbar para la fecha
    public void FechaUltimoControl (View view){
        Calendar cal=Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, Date1, year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceEmbarazo(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.FechaParto.length()!=0){
            avance+=1;
        }
        if (Persona.UltimoControlEmbarazo.length()!=0){
            avance+=1;
        }
        if (Persona.EnfermedadEmbarazo.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_embarazo.setBackgroundResource(R.drawable.rojo);
            avanceembarazo.setText(getString(R.string.completado)+" 00%");
        }

        if(avance>0 && avance<3){
            layout_embarazo.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/3)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceembarazo.setText(aux);
        }

        if(avance==3){
            layout_embarazo.setBackgroundResource(R.drawable.verde);
            avanceembarazo.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // DISCAPACIDAD
    public void Discapacidad(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_discapacidad, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final RadioGroup discapacidad = view1.findViewById(R.id.GROUPDISCAPACIDAD);
        final RadioButton siDisacapacidad = view1.findViewById(R.id.SIVDISCAPACIDAD);
        final RadioButton noDiscapacidad = view1.findViewById(R.id.NODISCAPACIDAD);

        final CheckBox defFisicaMotor = view1.findViewById(R.id.DEFICIENCIAFISICAMOTOR);
        final CheckBox defFisicaVisceral = view1.findViewById(R.id.DEFICIENCIAFISICAVISCERAL);
        final CheckBox auditiva = view1.findViewById(R.id.AUDITIVA);
        final CheckBox defIntelectual = view1.findViewById(R.id.DEFICIENCIAINTELECTUAL);
        final CheckBox visual = view1.findViewById(R.id.SENSORIALVISUAL);

        if(Persona.CertificadoDiscapacidad.length()!=0){
            if(Persona.CertificadoDiscapacidad.equals("SI")){
                siDisacapacidad.setChecked(true);
            }
            else{
                noDiscapacidad.setChecked(true);
            }
        }

        String[] vac = Persona.TipoDiscapacidad.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals(defFisicaMotor.getText().toString())){
                defFisicaMotor.setChecked(true);
            }
            if (vac[x].equals(defFisicaVisceral.getText().toString())){
                defFisicaVisceral.setChecked(true);
            }
            if (vac[x].equals(auditiva.getText().toString())){
                auditiva.setChecked(true);
            }
            if (vac[x].equals(defIntelectual.getText().toString())){
                defIntelectual.setChecked(true);
            }
            if (vac[x].equals(visual.getText().toString())){
                visual.setChecked(true);
            }
        }

        final Button guardar = view1.findViewById(R.id.GUARDARDISCAPACIDAD);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton selec = discapacidad.findViewById(discapacidad.getCheckedRadioButtonId());
                if(selec!=null){
                String seleccionado = selec.getText().toString();
                Persona.CertificadoDiscapacidad = seleccionado;}

                String discapacidad = null;
                if (defFisicaMotor.isChecked()){
                    if (discapacidad==null){
                        discapacidad=defFisicaMotor.getText().toString();
                    } else {
                        discapacidad+=","+defFisicaMotor.getText().toString();}}
                if (defFisicaVisceral.isChecked()){
                    if (discapacidad==null){
                        discapacidad=defFisicaVisceral.getText().toString();
                    } else {
                        discapacidad+=","+defFisicaVisceral.getText().toString();}}
                if (auditiva.isChecked()){
                    if (discapacidad==null){
                        discapacidad=auditiva.getText().toString();
                    } else {
                        discapacidad+=","+auditiva.getText().toString();}}
                if (defIntelectual.isChecked()){
                    if (discapacidad==null){
                        discapacidad=defIntelectual.getText().toString();
                    } else {
                        discapacidad+=","+defIntelectual.getText().toString();}}
                if (visual.isChecked()){
                    if (discapacidad==null){
                        discapacidad=visual.getText().toString();
                    } else {
                        discapacidad+=","+visual.getText().toString();}}

                if(discapacidad!=null){
                    Persona.TipoDiscapacidad = discapacidad;
                }else{Persona.TipoDiscapacidad="";}

                ColorAvanceDiscapacidad();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARDISCAPCIDAD);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceDiscapacidad(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.CertificadoDiscapacidad.length()!=0){
            avance+=1;
        }
        if (Persona.TipoDiscapacidad.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_discapacidad.setBackgroundResource(R.drawable.rojo);
            avancediscapacidad.setText(getString(R.string.completado)+" 00%");
        }

        if(avance>0 && avance<2){
            layout_discapacidad.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avancediscapacidad.setText(aux);
        }

        if(avance==2){
            layout_discapacidad.setBackgroundResource(R.drawable.verde);
            avancediscapacidad.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // ACOMPAÑAMIENTO
    public void Acompañamiento(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_acompanamiento, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final CheckBox psicologico = view1.findViewById(R.id.PSICOLOGICO);
        final CheckBox medico = view1.findViewById(R.id.MEDICO);
        final CheckBox institucion = view1.findViewById(R.id.INSTITUCIOSOCIAL);

        String[] vac = Persona.Acompañamiento.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals(psicologico.getText().toString())){
                psicologico.setChecked(true);
            }
            if (vac[x].equals(medico.getText().toString())){
                medico.setChecked(true);
            }
            if (vac[x].equals(institucion.getText().toString())){
                institucion.setChecked(true);
            }
        }

        final Button guardar = view1.findViewById(R.id.GUARDARACOMPAÑAMIENTO);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acompañamiento = null;
                if (psicologico.isChecked()){
                    if (acompañamiento==null){
                        acompañamiento=psicologico.getText().toString();
                    } else {
                        acompañamiento+=","+psicologico.getText().toString();}}
                if (medico.isChecked()){
                    if (acompañamiento==null){
                        acompañamiento=medico.getText().toString();
                    } else {
                        acompañamiento+=","+medico.getText().toString();}}
                if (institucion.isChecked()){
                    if (acompañamiento==null){
                        acompañamiento=institucion.getText().toString();
                    } else {
                        acompañamiento+=","+institucion.getText().toString();}}

                if(acompañamiento!=null){Persona.Acompañamiento = acompañamiento;}
                else{Persona.Acompañamiento="";}

                ColorAvanceAcompañamiento();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARACOMPAÑAMIENTO);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceAcompañamiento(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.Acompañamiento.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_acompañamiento.setBackgroundResource(R.drawable.rojo);
            avanceacompañamiento.setText(getString(R.string.completado)+" 00%");
        }

        /*if(avance>0 && avance<2){
            layout_acompañamiento.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avancediscapacidad.setText(aux);
        }*/

        if(avance==1){
            layout_acompañamiento.setBackgroundResource(R.drawable.verde);
            avanceacompañamiento.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SERVICIOS BASICOS
    public void TrastornosNiños(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_trastornos_ninos, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final CheckBox deficitAtencion =  view1.findViewById(R.id.DEFICITATENCION);
        final CheckBox trastornosConducta = view1.findViewById(R.id.TRASTORNOSCONDUCTA);

        String[] vac = Persona.TrastornoNiños.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals(deficitAtencion.getText().toString())){
                deficitAtencion.setChecked(true);
            }
            if (vac[x].equals(trastornosConducta.getText().toString())){
                trastornosConducta.setChecked(true);
            }
        }

        final Button guardar = view1.findViewById(R.id.GUARDARTRANSTORNOSNIÑOS);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trastornosNiños = null;
                if (deficitAtencion.isChecked()){
                    if (trastornosNiños==null){
                        trastornosNiños=deficitAtencion.getText().toString();
                    } else {
                        trastornosNiños+=","+deficitAtencion.getText().toString();}}
                if (trastornosConducta.isChecked()){
                    if (trastornosNiños==null){
                        trastornosNiños=trastornosConducta.getText().toString();
                    } else {
                        trastornosNiños+=","+trastornosConducta.getText().toString();}}

                if(trastornosNiños!=null){Persona.TrastornoNiños = trastornosNiños;}
                else{Persona.TrastornoNiños = "";}
                ColorAvanceTrastornosNiños();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARTRASTORNOSNIÑOS);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceTrastornosNiños(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.TrastornoNiños.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_trastornosniños.setBackgroundResource(R.drawable.rojo);
            avancetrastornosniños.setText(getString(R.string.completado)+" 00%");
        }

        /*if(avance>0 && avance<2){
            layout_acompañamiento.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avancediscapacidad.setText(aux);
        }*/

        if(avance==1){
            layout_trastornosniños.setBackgroundResource(R.drawable.verde);
            avancetrastornosniños.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SERVICIOS BASICOS
    public void Adicciones(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_adicciones, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final CheckBox alcohol =  view1.findViewById(R.id.ALCOHOL);
        final CheckBox drogas = view1.findViewById(R.id.DROGAS);
        final CheckBox tabaco = view1.findViewById(R.id.TABACO);

        String[] vac = Persona.Adicciones.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals(alcohol.getText().toString())){
                alcohol.setChecked(true);
            }
            if (vac[x].equals(drogas.getText().toString())){
                drogas.setChecked(true);
            }
            if (vac[x].equals(tabaco.getText().toString())){
                tabaco.setChecked(true);
            }
        }

        final Button guardar = view1.findViewById(R.id.GUARDARADICCIONES);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adicciones = null;
                if (alcohol.isChecked()){
                    if (adicciones==null){
                        adicciones=alcohol.getText().toString();
                    } else {
                        adicciones+=","+alcohol.getText().toString();}}
                if (drogas.isChecked()){
                    if (adicciones==null){
                        adicciones=drogas.getText().toString();
                    } else {
                        adicciones+=","+drogas.getText().toString();}}
                if (tabaco.isChecked()){
                    if (adicciones==null){
                        adicciones=tabaco.getText().toString();
                    } else {
                        adicciones+=","+tabaco.getText().toString();}}

                if(adicciones!=null){Persona.Adicciones = adicciones;}
                else{Persona.Adicciones="";}

                ColorAvanceAdicciones();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARADICCIONES);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceAdicciones(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.Adicciones.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_adicciones.setBackgroundResource(R.drawable.rojo);
            avanceadicciones.setText(getString(R.string.completado)+" 00%");
        }

        /*if(avance>0 && avance<2){
            layout_acompañamiento.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avancediscapacidad.setText(aux);
        }*/

        if(avance==1){
            layout_adicciones.setBackgroundResource(R.drawable.verde);
            avanceadicciones.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SERVICIOS BASICOS
    public void TrantornosMentales(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_trastornos_mentales, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final CheckBox organicos =  view1.findViewById(R.id.TRASTORNOORGANICO);
        final CheckBox psicoactivas = view1.findViewById(R.id.SUSTANCIAPSICOACTIVAS);
        final CheckBox esquizofrenia = view1.findViewById(R.id.ESQUISOFRENIA);
        final CheckBox humor =  view1.findViewById(R.id.HUMOR);
        final CheckBox estres = view1.findViewById(R.id.ESTRES);
        final CheckBox sindromes_comportamiento = view1.findViewById(R.id.SINDROMESCOMPORTAMIENTO);
        final CheckBox personalidad =  view1.findViewById(R.id.TRASTORNOSPERSONALIDAD);
        final CheckBox retraso_mental = view1.findViewById(R.id.RETRASOMENTAL);
        final CheckBox desarrollo_psicosocial = view1.findViewById(R.id.DESARROLLOPSICOLOGICO);
        final CheckBox emocionales =  view1.findViewById(R.id.EMOCIONALES);
        final CheckBox no_especificados = view1.findViewById(R.id.TRASTORNOSNOESPECIFICADO);
        final CheckBox lesiones_autoinflijidas = view1.findViewById(R.id.LESIONESAUTOINFLIJIDAS);

        String[] vac = Persona.TrastornosMentales.split(",");
        for (int x = 0; x < vac.length; x++) {
            //Toast.makeText(getApplicationContext(), vac[0], Toast.LENGTH_SHORT).show();
            if (vac[x].equals(organicos.getText().toString())){
                organicos.setChecked(true);
            }
            if (vac[x].equals(psicoactivas.getText().toString())){
                psicoactivas.setChecked(true);
            }
            if (vac[x].equals(esquizofrenia.getText().toString())){
                esquizofrenia.setChecked(true);
            }
            if (vac[x].equals(humor.getText().toString())){
                humor.setChecked(true);
            }
            if (vac[x].equals(estres.getText().toString())){
                estres.setChecked(true);
            }
            if (vac[x].equals(sindromes_comportamiento.getText().toString())){
                sindromes_comportamiento.setChecked(true);
            }
            if (vac[x].equals(personalidad.getText().toString())){
                personalidad.setChecked(true);
            }
            if (vac[x].equals(retraso_mental.getText().toString())){
                retraso_mental.setChecked(true);
            }
            if (vac[x].equals(desarrollo_psicosocial.getText().toString())){
                desarrollo_psicosocial.setChecked(true);
            }
            if (vac[x].equals(emocionales.getText().toString())){
                emocionales.setChecked(true);
            }
            if (vac[x].equals(no_especificados.getText().toString())){
                no_especificados.setChecked(true);
            }
            if (vac[x].equals(lesiones_autoinflijidas.getText().toString())){
                lesiones_autoinflijidas.setChecked(true);
            }
        }

        final Button guardar = view1.findViewById(R.id.GUARDARTRANSTORNOSMENTALES);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trastornosMentales = null;
                if (organicos.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=organicos.getText().toString();
                    } else {
                        trastornosMentales+=","+organicos.getText().toString();}}
                if (psicoactivas.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=psicoactivas.getText().toString();
                    } else {
                        trastornosMentales+=","+psicoactivas.getText().toString();}}
                if (esquizofrenia.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=esquizofrenia.getText().toString();
                    } else {
                        trastornosMentales+=","+esquizofrenia.getText().toString();}}
                if (humor.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=humor.getText().toString();
                    } else {
                        trastornosMentales+=","+humor.getText().toString();}}
                if (estres.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=estres.getText().toString();
                    } else {
                        trastornosMentales+=","+estres.getText().toString();}}
                if (sindromes_comportamiento.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=sindromes_comportamiento.getText().toString();
                    } else {
                        trastornosMentales+=","+sindromes_comportamiento.getText().toString();}}
                if (personalidad.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=personalidad.getText().toString();
                    } else {
                        trastornosMentales+=","+personalidad.getText().toString();}}
                if (retraso_mental.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=retraso_mental.getText().toString();
                    } else {
                        trastornosMentales+=","+retraso_mental.getText().toString();}}
                if (desarrollo_psicosocial.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=desarrollo_psicosocial.getText().toString();
                    } else {
                        trastornosMentales+=","+desarrollo_psicosocial.getText().toString();}}
                if (emocionales.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=emocionales.getText().toString();
                    } else {
                        trastornosMentales+=","+emocionales.getText().toString();}}
                if (no_especificados.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=no_especificados.getText().toString();
                    } else {
                        trastornosMentales+=","+no_especificados.getText().toString();}}
                if (lesiones_autoinflijidas.isChecked()){
                    if (trastornosMentales==null){
                        trastornosMentales=lesiones_autoinflijidas.getText().toString();
                    } else {
                        trastornosMentales+=","+lesiones_autoinflijidas.getText().toString();}}


                if(trastornosMentales!=null){Persona.TrastornosMentales = trastornosMentales;}
                else{Persona.TrastornosMentales="";}


                ColorAvanceTrastornosMentales();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARTRASTORNOSMENTALES);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceTrastornosMentales(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.TrastornosMentales.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_trastornosmentales.setBackgroundResource(R.drawable.rojo);
            avancetrastornosmentales.setText(getString(R.string.completado)+" 00%");
        }

        /*if(avance>0 && avance<2){
            layout_acompañamiento.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avancediscapacidad.setText(aux);
        }*/

        if(avance==1){
            layout_trastornosmentales.setBackgroundResource(R.drawable.verde);
            avancetrastornosmentales.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SERVICIOS BASICOS
    public void Ocio(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_ocio, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText editLugarOcio = view1.findViewById(R.id.LUGAROCIO);
        if(Persona.LugarOcio.length()!=0){editLugarOcio.setText(Persona.LugarOcio);}

        final CheckBox deportes =  view1.findViewById(R.id.DEPORTES);
        final CheckBox musica = view1.findViewById(R.id.MUSICA);
        final CheckBox manualidades = view1.findViewById(R.id.MANUALIDADES);
        final CheckBox baile = view1.findViewById(R.id.BAILE);
        final CheckBox otros = view1.findViewById(R.id.OTROS);

        String[] vac = Persona.ActividadesOcio.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals(deportes.getText().toString())){
                deportes.setChecked(true);
            }
            if (vac[x].equals(musica.getText().toString())){
                musica.setChecked(true);
            }
            if (vac[x].equals(manualidades.getText().toString())){
                manualidades.setChecked(true);
            }
            if (vac[x].equals(baile.getText().toString())){
                baile.setChecked(true);
            }
            if (vac[x].equals(otros.getText().toString())){
                otros.setChecked(true);
            }
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROCIO);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String actividades = null;
                if (deportes.isChecked()){
                    if (actividades==null){
                        actividades=deportes.getText().toString();
                    } else {
                        actividades+=","+deportes.getText().toString();}}
                if (baile.isChecked()){
                    if (actividades==null){
                        actividades=baile.getText().toString();
                    } else {
                        actividades+=","+baile.getText().toString();}}
                if (manualidades.isChecked()){
                    if (actividades==null){
                         actividades=manualidades.getText().toString();
                    } else {
                         actividades+=","+manualidades.getText().toString();}}
                if (musica.isChecked()){
                    if (actividades==null){
                        actividades=musica.getText().toString();
                    } else {
                        actividades+=","+musica.getText().toString();}}
                if (otros.isChecked()){
                    if (actividades==null){
                        actividades=otros.getText().toString();
                    } else {
                        actividades+=","+otros.getText().toString();}}
                if(actividades!=null){Persona.ActividadesOcio = actividades;}
                else{Persona.ActividadesOcio="";}

                Persona.LugarOcio = editLugarOcio.getText().toString();
                ColorAvanceOcio();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELAROCIO);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceOcio(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.ActividadesOcio.length()!=0){
            avance+=1;
        }

        if (Persona.LugarOcio.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_ocio.setBackgroundResource(R.drawable.rojo);
            avanceocio.setText(getString(R.string.completado)+" 00%");
        }

        if(avance>0 && avance<2){
            layout_ocio.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceocio.setText(aux);
        }

        if(avance==2){
            layout_ocio.setBackgroundResource(R.drawable.verde);
            avanceocio.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SERVICIOS BASICOS
    public void Violencia(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_violencia, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final CheckBox fisica =  view1.findViewById(R.id.FISICA);
        final CheckBox psicologica = view1.findViewById(R.id.PSICOLOGICA);
        final CheckBox economica = view1.findViewById(R.id.ECONOMICA);
        final CheckBox sexual = view1.findViewById(R.id.SEXUAL);
        final CheckBox simbolica = view1.findViewById(R.id.SIMBOLICA);

        String[] vac = Persona.TipoViolencia.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals(fisica.getText().toString())){
                fisica.setChecked(true);
            }
            if (vac[x].equals(psicologica.getText().toString())){
                psicologica.setChecked(true);
            }
            if (vac[x].equals(economica.getText().toString())){
                economica.setChecked(true);
            }
            if (vac[x].equals(sexual.getText().toString())){
                sexual.setChecked(true);
            }
            if (vac[x].equals(simbolica.getText().toString())){
                simbolica.setChecked(true);
            }
        }

        final CheckBox domestica =  view1.findViewById(R.id.DOMESTICA);
        final CheckBox laboral = view1.findViewById(R.id.LABORAL);
        final CheckBox institucional = view1.findViewById(R.id.INSTITUCIONAL);
        final CheckBox reproductiva = view1.findViewById(R.id.REPRODUCTIVA);
        final CheckBox obstetrica = view1.findViewById(R.id.OBSTETRICA);
        final CheckBox mediatica = view1.findViewById(R.id.MEDIATICA);
        vac = Persona.ModalidadViolencia.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals(domestica.getText().toString())){
                domestica.setChecked(true);
            }
            if (vac[x].equals(laboral.getText().toString())){
                laboral.setChecked(true);
            }
            if (vac[x].equals(institucional.getText().toString())){
                institucional.setChecked(true);
            }
            if (vac[x].equals(reproductiva.getText().toString())){
                reproductiva.setChecked(true);
            }
            if (vac[x].equals(obstetrica.getText().toString())){
                obstetrica.setChecked(true);
            }
            if (vac[x].equals(mediatica.getText().toString())){
                mediatica.setChecked(true);
            }
        }

        final Button guardar = view1.findViewById(R.id.GUARDARVIOLENCIA);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tipoViolencia = null;
                if (fisica.isChecked()){
                    if (tipoViolencia==null){
                        tipoViolencia=fisica.getText().toString();
                    } else {
                        tipoViolencia+=","+fisica.getText().toString();}}
                if (psicologica.isChecked()){
                    if (tipoViolencia==null){
                        tipoViolencia=psicologica.getText().toString();
                    } else {
                        tipoViolencia+=","+psicologica.getText().toString();}}
                if (economica.isChecked()){
                    if (tipoViolencia==null){
                        tipoViolencia=economica.getText().toString();
                    } else {
                        tipoViolencia+=","+economica.getText().toString();}}
                if (sexual.isChecked()){
                    if (tipoViolencia==null){
                        tipoViolencia=sexual.getText().toString();
                    } else {
                        tipoViolencia+=","+sexual.getText().toString();}}
                if (simbolica.isChecked()){
                    if (tipoViolencia==null){
                        tipoViolencia=simbolica.getText().toString();
                    } else {
                        tipoViolencia+=","+simbolica.getText().toString();}}
                if(tipoViolencia!=null){Persona.TipoViolencia = tipoViolencia;}
                else {Persona.TipoViolencia="";}

                String modalidadViolencia = null;
                if (domestica.isChecked()){
                    if (modalidadViolencia==null){
                        modalidadViolencia=domestica.getText().toString();
                    } else {
                        modalidadViolencia+=","+domestica.getText().toString();}}
                if (laboral.isChecked()){
                    if (modalidadViolencia==null){
                        modalidadViolencia=laboral.getText().toString();
                    } else {
                        modalidadViolencia+=","+laboral.getText().toString();}}
                if (institucional.isChecked()){
                    if (modalidadViolencia==null){
                        modalidadViolencia=institucional.getText().toString();
                    } else {
                        modalidadViolencia+=","+institucional.getText().toString();}}
                if (reproductiva.isChecked()){
                    if (modalidadViolencia==null){
                        modalidadViolencia=reproductiva.getText().toString();
                    } else {
                        modalidadViolencia+=","+reproductiva.getText().toString();}}
                if (obstetrica.isChecked()){
                    if (modalidadViolencia==null){
                        modalidadViolencia=obstetrica.getText().toString();
                    } else {
                        modalidadViolencia+=","+obstetrica.getText().toString();}}
                if (mediatica.isChecked()){
                    if (modalidadViolencia==null){
                        modalidadViolencia=mediatica.getText().toString();
                    } else {
                        modalidadViolencia+=","+mediatica.getText().toString();}}
                if(modalidadViolencia!=null){Persona.ModalidadViolencia = modalidadViolencia;}
                else{Persona.ModalidadViolencia="";}
                ColorAvanceViolencia();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARVIOLENCIA);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceViolencia(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.TipoViolencia.length()!=0){
            avance+=1;
        }

        if (Persona.ModalidadViolencia.length()!=0){
            avance+=1;
        }

        if(avance==0){
            layout_violencia.setBackgroundResource(R.drawable.rojo);
            avanceviolencia.setText(getString(R.string.completado)+" 00%");
        }

        if(avance>0 && avance<2){
            layout_violencia.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceviolencia.setText(aux);
        }

        if(avance==2){
            layout_violencia.setBackgroundResource(R.drawable.verde);
            avanceviolencia.setText(getString(R.string.completado)+" 100%");
        }
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    // SERVICIOS BASICOS
    public void EnfermedadesCronicas(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_enfermedades_cronicas, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final CheckBox cancer =  view1.findViewById(R.id.CANCER);
        final CheckBox diabetes = view1.findViewById(R.id.DIABETES);
        final CheckBox cardiovasculares = view1.findViewById(R.id.ENFERMEDADESCARDIOVASCULARES);
        final CheckBox respiratorias = view1.findViewById(R.id.ENFERMEDADESRESPIRATORIAS);

        String[] vac = Persona.EnfermedadCronica.split(",");
        for (int x = 0; x < vac.length; x++) {
            if (vac[x].equals(cancer.getText().toString())){
                cancer.setChecked(true);
            }
            if (vac[x].equals(diabetes.getText().toString())){
                diabetes.setChecked(true);
            }
            if (vac[x].equals(cardiovasculares.getText().toString())){
                cardiovasculares.setChecked(true);
            }
            if (vac[x].equals(respiratorias.getText().toString())){
                respiratorias.setChecked(true);
            }
        }

        final EditText otrasEnfermedades = view1.findViewById(R.id.EDTOTRASENFERMEDADES);

        final Button guardar = view1.findViewById(R.id.GUARDARENFERMEDADESCRONICAS);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enfermedadcronica = null;
                if (cancer.isChecked()){
                    if (enfermedadcronica==null){
                        enfermedadcronica=cancer.getText().toString();
                    } else {
                        enfermedadcronica+=","+cancer.getText().toString();}}
                if (diabetes.isChecked()){
                    if (enfermedadcronica==null){
                        enfermedadcronica=diabetes.getText().toString();
                    } else {
                        enfermedadcronica+=","+diabetes.getText().toString();}}
                if (cardiovasculares.isChecked()){
                    if (enfermedadcronica==null){
                        enfermedadcronica=cardiovasculares.getText().toString();
                    } else {
                        enfermedadcronica+=","+cardiovasculares.getText().toString();}}
                if (respiratorias.isChecked()){
                    if (enfermedadcronica==null){
                        enfermedadcronica=respiratorias.getText().toString();
                    } else {
                        enfermedadcronica+=","+respiratorias.getText().toString();}}
                if (otrasEnfermedades.getText().toString().length()!=0){
                    if (enfermedadcronica==null){
                        enfermedadcronica=otrasEnfermedades.getText().toString();
                    } else {
                        enfermedadcronica+=","+otrasEnfermedades.getText().toString();}}
                if(enfermedadcronica!=null){Persona.EnfermedadCronica = enfermedadcronica;}
                else{Persona.EnfermedadCronica="";}
                ColorAvanceEnfermedadCronica();
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELARENFERMEDADCRONICA);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Cambia los colores de los botones de llenado de contacto
    private void ColorAvanceEnfermedadCronica(){
        // Cambio los colores de avance
        float avance = 0;
        if (Persona.EnfermedadCronica.length()!=0){
            avance+=1;
        }


        if(avance==0){
            layout_enfermedades_cronicas.setBackgroundResource(R.drawable.rojo);
            avanceenfermedadescronicas.setText(getString(R.string.completado)+" 00%");
        }

        /*if(avance>0 && avance<2){
            layout_violencia.setBackgroundResource(R.drawable.amarillo);
            double porcentaje = Math.round((avance/2)*100);
            String aux = getString(R.string.completado)+" "+ Double.toString(porcentaje)+"%";
            avanceviolencia.setText(aux);
        }*/

        if(avance==1){
            layout_enfermedades_cronicas.setBackgroundResource(R.drawable.verde);
            avanceenfermedadescronicas.setText(getString(R.string.completado)+" 100%");
        }
    }
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
// SECCION DE DEFINICION DE DEFINICION DE LAS OPCIONES DE GUARDADO DE LAS PERSONAS

    // Defino las funciones de guardado de persona
    // Para que sea una funcion disparada por un Button
    public void GuardarPersona(View view){
        // Defino el intent para permitir pasar los datos
        Intent Modif1= new Intent (this, Familia.class);

        // Reviso que no se carguen datos nuevos, si se cargaron los obtengo
        if(txtNombre.getText().toString()!=null){Persona.Nombre=txtNombre.getText().toString();}
        if(txtApellido.getText().toString()!=null){Persona.Apellido=txtApellido.getText().toString();}
        if(txtDni.getText().toString()!=null){Persona.DNI=txtDni.getText().toString();}
        if(txtSexo.getText().toString()!=null){Persona.Sexo=txtSexo.getText().toString();}
        if(Persona.QR!=Boolean.toString(true)){Persona.QR = Boolean.toString(false);}
        //if(Efector.getText().toString()!=null){Persona.Efector=Efector.getText().toString();}

        // Si uno de los campos listados abajo es nulo lo reemplazo por S/D
        if(Persona.Edad==null){Persona.Edad="";}
        if(Persona.UnidadEdad==null){Persona.UnidadEdad="";}
        if(Persona.CodfigoFactorRiesgo==null){Persona.CodfigoFactorRiesgo="";}
        if(Persona.Observaciones==null){Persona.Observaciones="";}

        // Si dni es distinto a null le permito guardar la persona
        //if(Persona.DNI!=""){

        Bundle bundle = new Bundle();
        bundle.putSerializable("PERSONA", Persona);

        Modif1.putExtras(bundle);

        Toast.makeText(this, "PERSONA CARGADA", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, Modif1);
        finish();
        //}
        //else{Toast.makeText(this, "FALTA DNI", Toast.LENGTH_SHORT).show();}
    }
    // Para que se una funcion disparada por el Button de regreso de forma private

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
