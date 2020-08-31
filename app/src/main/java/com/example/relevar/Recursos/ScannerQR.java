package com.example.relevar.Recursos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.relevar.Familia;
import com.example.relevar.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerQR extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_q_r);

        mScanner = new ZXingScannerView(this);
        setContentView(mScanner);
        mScanner.setResultHandler(this);
        mScanner.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        //txt.setText(result.getText().toString());
        //mScanner.stopCamera();
        //mScanner.resumeCameraPreview(this);
        String escaneado = result.getText().toString();
        String[] datos = escaneado.split("@");
        Intent intent = new Intent(this, Familia.class);
        int tamaño = datos.length;
        intent.putExtra("APELLIDO_ESCANEADO", datos[1]);
        intent.putExtra("NOMBRE_ESCANEADO", datos[2]);
        intent.putExtra("SEXO_ESCANEADO", datos[3]);
        intent.putExtra("DNI_ESCANEADO", datos[4]);
        intent.putExtra("FECHA_NACIMIENTO_ESCANEADO", datos[6]);
        intent.putExtra("ESCANEADO", escaneado);
        setResult(RESULT_OK, intent);
        finish();
    }
}
