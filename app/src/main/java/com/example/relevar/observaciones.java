package com.example.relevar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class observaciones extends AppCompatActivity {
    private EditText Limpieza, Observaciones;
    private String vacuna, observaciones;
    private Button btn4;
    private CheckBox antigripal, vcn13, vcn23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observaciones);
        Observaciones=(EditText) findViewById(R.id.txtObservaciones);
        btn4=(Button) findViewById(R.id.btn4);
        antigripal = (CheckBox) findViewById(R.id.ANTIGRIPAL);
        vcn13 = (CheckBox) findViewById(R.id.VCN13);
        vcn23 = (CheckBox) findViewById(R.id.VCN23);
    }

    public void Guardar(View view) {
        //Estructura de datos
        //apellido; nombre; dni; edad; unidad de la edad (meses años); domicilio (calle N°); latitud longitud; telefono; cant. personas grupo familiar; Efector de salud; factore de riesgo; antigripal; vcn13; vcn23; observaciones
        Bundle datos = this.getIntent().getExtras();
        String recuperada = datos.getString("datos");

        if(antigripal.isChecked()){
                vacuna=antigripal.getText().toString();}
        else{vacuna="NO";}
        if(vcn13.isChecked()){
                vacuna+=";"+vcn13.getText().toString();}
            else{vacuna+=";NO";}
        if(vcn23.isChecked()){
                vacuna+=";"+vcn23.getText().toString();}
            else{vacuna+=";NO";}

        if(Observaciones.getText().toString().length()!=0){
            observaciones=Observaciones.getText().toString().replace("\n", " ");}
        else {observaciones="S/D";}
        String guardar = recuperada + ";" + vacuna + ";" + observaciones+"\n";

        // guardo los datos
        File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(ruta, "DATOS.csv");

        try {
            FileOutputStream fOut = new FileOutputStream(dir, true); //el true es para
            // que se agreguen los datos al final sin perder los datos anteriores
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(guardar);
            myOutWriter.close();
            fOut.close();
            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Datos NO guardados", Toast.LENGTH_SHORT).show();
        }

    }
}
