package com.example.relevar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class salud extends AppCompatActivity {
    private EditText Efector, Grupo, Enfermedades, Riesgo;
    private String efector, grupo, enfermedad, riesgo;
    private Button btn3;
    private Spinner Sp1;
    private ArrayList<String> categorias = new ArrayList<>();
    private CheckBox c1,c2,c3,c4,c5,c6,c7,c8;
    private TextView riesgos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salud);
        //Efector=(EditText) findViewById(R.id.txtEfector);
        Grupo=(EditText) findViewById(R.id.txtGrupo);
        Riesgo=(EditText) findViewById(R.id.txtRiesgo);
        Efector=(EditText) findViewById(R.id.txtEfector);
        //Enfermedades=(EditText) findViewById(R.id.txtEnfermedades);
        btn3=(Button) findViewById(R.id.btn3);

        Sp1 = (Spinner) findViewById(R.id.sp1);
        categorias.add("- seleccionar -");
        categorias.add("CAPs ANTARTIDA");
        categorias.add("CAPs SAN MARTIN");
        categorias.add("CRR CARRILLO");
        categorias.add("Hosp. SAN MARTIN");
        categorias.add("Hosp. SAN ROQUE");
        ArrayAdapter<String> comboAdapter = new ArrayAdapter<String>(this, R.layout.spiner_personalizado, categorias);//Cargo el spinner con los datos
        Sp1.setAdapter(comboAdapter);

        c1 = (CheckBox) findViewById(R.id.c1);
        c2 = (CheckBox) findViewById(R.id.c2);
        c3 = (CheckBox) findViewById(R.id.c3);
        c4 = (CheckBox) findViewById(R.id.c4);
        c5 = (CheckBox) findViewById(R.id.c5);
        c6 = (CheckBox) findViewById(R.id.c6);
        c7 = (CheckBox) findViewById(R.id.c7);
        c8 = (CheckBox) findViewById(R.id.c8);
    }

    public void DatosObs(View view){
        Bundle datos = this.getIntent().getExtras();
        String recuperada = datos.getString("datos");
        if(Grupo.getText().toString().length()!=0){
            grupo=Grupo.getText().toString();}
        else {grupo="S/D";}
        efector = Sp1.getSelectedItem().toString();
        if (Efector.getText().toString()!=null){
            efector = Efector.getText().toString();
        }

        if(c1.isChecked()){
            if (riesgo==null){
            riesgo=c1.getText().toString();}
            else{riesgo+=","+c1.getText().toString();}}
        if(c2.isChecked()){
            if (riesgo==null){
                riesgo=c2.getText().toString();}
            else{riesgo+=","+c2.getText().toString();}}
        if(c3.isChecked()){
            if (riesgo==null){
                riesgo=c3.getText().toString();}
            else{riesgo+=","+c3.getText().toString();}}
        if(c4.isChecked()){
            if (riesgo==null){
                riesgo=c4.getText().toString();}
            else{riesgo+=","+c4.getText().toString();}}
        if(c5.isChecked()){
            if (riesgo==null){
                riesgo=c5.getText().toString();}
            else{riesgo+=","+c5.getText().toString();}}
        if(c6.isChecked()){
            if (riesgo==null){
                riesgo=c6.getText().toString();}
            else{riesgo+=","+c6.getText().toString();}}
        if(c7.isChecked()){
            if (riesgo==null){
                riesgo=c7.getText().toString();}
            else{riesgo+=","+c7.getText().toString();}}
        if(c8.isChecked()){
            if (riesgo==null){
                riesgo=c8.getText().toString();}
            else{riesgo+=","+c8.getText().toString();}}
        if(Riesgo.getText().toString().length()!=0 && riesgo!=null){
            riesgo+=","+Riesgo.getText().toString();
        } else {riesgo=Riesgo.getText().toString();}

        String pasar =recuperada+";"+grupo+";"+efector+";"+riesgo;
            Intent Modif= new Intent (this, observaciones.class);
            Modif.putExtra("datos", pasar);
            startActivity(Modif);
        finish();}

    public void GruposRiesgo(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked){
        String riesgo = ((CheckBox) view).getText().toString();
        String riesgos_mostrar = riesgos.getText().toString();
        //riesgos.setText("entro");
        if(riesgos_mostrar.length()==0){
        riesgos.setText(riesgo);}
        else riesgos.setText(riesgos_mostrar+","+riesgo);}
    }
}
