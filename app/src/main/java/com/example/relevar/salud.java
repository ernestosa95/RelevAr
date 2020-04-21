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
    private EditText Grupo, Enfermedades, OtroRiesgo;
    private String efector, grupo, enfermedad, riesgo="S/D";
    private Button btn3;
    private ArrayList<String> categorias = new ArrayList<>();
    private CheckBox c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15;
    private TextView riesgos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salud);

        OtroRiesgo=(EditText) findViewById(R.id.txtRiesgo);

        btn3=(Button) findViewById(R.id.btn3);

        c1 = (CheckBox) findViewById(R.id.C1);
        c2 = (CheckBox) findViewById(R.id.C2);
        c3 = (CheckBox) findViewById(R.id.C3);
        c4 = (CheckBox) findViewById(R.id.C4);
        c5 = (CheckBox) findViewById(R.id.C5);
        c6 = (CheckBox) findViewById(R.id.C6);
        c7 = (CheckBox) findViewById(R.id.C7);
        c8 = (CheckBox) findViewById(R.id.C8);
        c9 = (CheckBox) findViewById(R.id.C9);
        c10 = (CheckBox) findViewById(R.id.C10);
        c11 = (CheckBox) findViewById(R.id.C11);
        c12 = (CheckBox) findViewById(R.id.C12);
        c13 = (CheckBox) findViewById(R.id.C13);
        c14 = (CheckBox) findViewById(R.id.C14);
        c15 = (CheckBox) findViewById(R.id.C15);


    }

    public void DatosObs(View view){
        Bundle datos = this.getIntent().getExtras();
        String recuperada = datos.getString("datos");

        if(c1.isChecked()){
            if (riesgo==null){
            riesgo="1";}
            else{riesgo+=",1";}}
        if(c2.isChecked()){
            if (riesgo==null){
                riesgo="2";}
            else{riesgo+=",2";}}
        if(c3.isChecked()){
            if (riesgo==null){
                riesgo="3";}
            else{riesgo+=",3";}}
        if(c4.isChecked()){
            if (riesgo==null){
                riesgo="4";}
            else{riesgo+=",4";}}
        if(c5.isChecked()){
            if (riesgo==null){
                riesgo="5";}
            else{riesgo+=",5";}}
        if(c6.isChecked()){
            if (riesgo==null){
                riesgo="6";}
            else{riesgo+=",6";}}
        if(c7.isChecked()){
            if (riesgo==null){
                riesgo="7";}
            else{riesgo+=",7";}}
        if(c8.isChecked()){
            if (riesgo==null){
                riesgo="8";}
            else{riesgo+=",8";}}
        if(c9.isChecked()){
            if (riesgo==null){
                riesgo="9";}
            else{riesgo+=",9";}}
        if(c10.isChecked()){
            if (riesgo==null){
                riesgo="10";}
            else{riesgo+=",10";}}
        if(c11.isChecked()){
            if (riesgo==null){
                riesgo="11";}
            else{riesgo+=",11";}}
        if(c12.isChecked()){
            if (riesgo==null){
                riesgo="12";}
            else{riesgo+=",12";}}
        if(c13.isChecked()){
            if (riesgo==null){
                riesgo="13";}
            else{riesgo+=",13";}}
        if(c14.isChecked()){
            if (riesgo==null){
                riesgo="14";}
            else{riesgo+=",14";}}
        if(c15.isChecked()){
            if (riesgo==null){
                riesgo="15";}
            else{riesgo+=",15";}}

        String aux=OtroRiesgo.getText().toString();
        if(aux!=null && aux.length()!=0){
            if (riesgo==null){
            riesgo=aux;}
            else {riesgo+=";"+aux;}}

        if(riesgo!="S/D"){
        String pasar =recuperada+";"+efector+";"+riesgo;
            Intent Modif= new Intent (this, observaciones.class);
            Modif.putExtra("datos", pasar);
            startActivity(Modif);
            finish();}
        else {
            Toast.makeText(this, "Falta cargar Factor de Riesgo", Toast.LENGTH_SHORT).show();}
    }

}
