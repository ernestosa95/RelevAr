package com.example.relevar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class vacunas extends AppCompatActivity {
    private CheckBox hepA,hepaB,bcg,rotavirus,quintuple,tripleviral,varicela,dtp,vph,tripleacelular,dt,antigripal,vcn23,vcn13,meningococica,ipv,sabin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacunas);


    }

    public void RetornarVacunas(View view){
        String cad = "DEVOLVIO";
        Intent data = new Intent();
        data.setData(Uri.parse(cad));
        setResult(RESULT_OK, data);
        finish();
    }
}
