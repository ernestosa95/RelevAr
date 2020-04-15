package com.example.relevar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //probando las modificaciones de github
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_POSITION = 1;
    private EditText DNI, Apellido, Nombre, Edad;
    private TextView fecha;
    private Button btn1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private int STORAGE_PERMISSION_CODE =1;
    private String dni, apellido, nombre, edad, unidadedad;
    private DatePickerDialog.OnDateSetListener Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); //evita la rotacion
        setContentView(R.layout.activity_main);
        DNI = (EditText) findViewById(R.id.txtDNI);
        Apellido = (EditText) findViewById(R.id.txtApellido);
        Nombre = (EditText) findViewById(R.id.txtNombre);

        btn1 = (Button) findViewById(R.id.btn1);
        fecha=(TextView) findViewById(R.id.fecha);

        permisosEscribir();

        Date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker,int  year, int month, int day) {
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

    private void permisosEscribir(){
        // Check whether this app has write external storage permission or not.
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // If do not grant write external storage permission.
        if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
        {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
        }

   public void DatosUbicacion(View view){

        if(DNI.getText().toString().length()!=0){
            if(edad!=null){
            dni=DNI.getText().toString();
            DNI.setText("");
            if(Apellido.getText().toString().length()!=0){
                apellido=Apellido.getText().toString();
                Apellido.setText("");}
            else {apellido="S/D";}
            if(Nombre.getText().toString().length()!=0){
                nombre=Nombre.getText().toString();
                Nombre.setText("");}
            else {nombre="S/D";}
            fecha.setText("DD - MM - YYYY");
            String Id =apellido+";"+nombre+";"+dni+";"+edad+";"+unidadedad;
            edad=null;
            Intent Modif= new Intent (this, ubicacion.class);
            Modif.putExtra("identificacion", Id);
            startActivity(Modif);}
            else {Toast.makeText(getApplicationContext(),"Falta Fecha de Nacimiento",Toast.LENGTH_SHORT).show();}}
        else Toast.makeText(getApplicationContext(),"Falta DNI",Toast.LENGTH_SHORT).show();
    }

    public void Fecha(View view){
        Calendar cal=Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, Date, 1955,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
