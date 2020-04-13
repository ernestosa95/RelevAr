package com.example.relevar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class observaciones extends AppCompatActivity {
    private EditText Limpieza, Observaciones;
    private String limpieza, observaciones;
    private Button btn4;
    private RadioButton rb1, rb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observaciones);
        Observaciones=(EditText) findViewById(R.id.txtObservaciones);
        btn4=(Button) findViewById(R.id.btn4);
        rb1 = (RadioButton) findViewById(R.id.SI);
        rb2 = (RadioButton) findViewById(R.id.NO);
    }

    public void Guardar(View view) {
        Bundle datos = this.getIntent().getExtras();
        String recuperada = datos.getString("datos");
        if(rb1.isChecked() == true){
            limpieza = "SI";
        }else {limpieza = "NO";}

        //if(Limpieza.getText().toString().length()!=0){
        //    limpieza=Limpieza.getText().toString();}
        //else {limpieza="S/D";}
        if(Observaciones.getText().toString().length()!=0){
            observaciones=Observaciones.getText().toString().replace("\n", " ");}
        else {observaciones="S/D";}
        String guardar = recuperada + ";" + limpieza + ";" + observaciones+"\n";

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
            //Toast.makeText(this, "Datos NO guardados", Toast.LENGTH_SHORT).show();
        }

    }
}
