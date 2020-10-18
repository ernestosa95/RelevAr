package com.example.relevar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.relevar.R.drawable.cuadrado_verde;

public class Dengue extends AppCompatActivity {

    ConstraintLayout layout_desuso, layout_botellas, layout_recipientes, layout_maceta, layout_hueco,
            layout_canaleta, layout_cubierta, layout_pileta, layout_tanquesbajos;

    int inspeccionadosTotal=0, tratadosTotal=0, focoaedicoTotal=0;
    ObjetoFamilia datosDengue = new ObjetoFamilia(null);

    CheckBox Inspeccion, Tratamientos, Fumigacion, sinIntervencion, cerrada;
    EditText destruidos;
    RadioButton siLarvicida, noLarvicida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dengue);

        // Seteo el titulo de la action bar del activity
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Dengue");

        if(getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                datosDengue = (ObjetoFamilia) bundle.getSerializable("DATOSDENGUE");
        }}



        layout_desuso = findViewById(R.id.ELEMENTOENDESUSO);
        if(!datosDengue.ElementosDesuso.equals("0;0;0")){
        layout_desuso.setBackgroundResource(R.drawable.cuadrado_verde);}
        layout_botellas = findViewById(R.id.BOTELLASVARIAS);
        if(!datosDengue.Botellas.equals("0;0;0")){
            layout_botellas.setBackgroundResource(R.drawable.cuadrado_verde);}
        layout_recipientes = findViewById(R.id.RECIPIENTEPLASTICOS);
        if(!datosDengue.RecipientesPlasticos.equals("0;0;0")){
            layout_recipientes.setBackgroundResource(R.drawable.cuadrado_verde);}
        layout_maceta = findViewById(R.id.MACETA);
        if(!datosDengue.Macetas.equals("0;0;0")){
            layout_maceta.setBackgroundResource(R.drawable.cuadrado_verde);}
        layout_hueco = findViewById(R.id.HUECO);
        if(!datosDengue.Hueco.equals("0;0;0")){
            layout_hueco.setBackgroundResource(R.drawable.cuadrado_verde);}
        layout_canaleta = findViewById(R.id.CANALETA);
        if(!datosDengue.Canaleta.equals("0;0;0")){
            layout_canaleta.setBackgroundResource(R.drawable.cuadrado_verde);}
        layout_cubierta = findViewById(R.id.CUBIERTAS);
        if(!datosDengue.Cubiertas.equals("0;0;0")){
            layout_cubierta.setBackgroundResource(R.drawable.cuadrado_verde);}
        layout_pileta = findViewById(R.id.PILETAS);
        if(!datosDengue.Piletas.equals("0;0;0")){
            layout_pileta.setBackgroundResource(R.drawable.cuadrado_verde);}
        layout_tanquesbajos =findViewById(R.id.TANQUES);
        if(!datosDengue.Tanques.equals("0;0;0")){
            layout_tanquesbajos.setBackgroundResource(R.drawable.cuadrado_verde);}

        Inspeccion = findViewById(R.id.INSPECCIONDOMICILIARIA);
        Tratamientos = findViewById(R.id.TRATAMIENTOS);
        Fumigacion = findViewById(R.id.FUMIGACION);
        sinIntervencion = findViewById(R.id.SININTERVENCION);

        String[] tipoTrabajo = datosDengue.TipoTrabajo.split(",");
        for(int i=0; i<tipoTrabajo.length; i++){
            if(tipoTrabajo[i].equals("INSPECCION DOMICILIARIA")){
                Inspeccion.setChecked(true);
            }
            if(tipoTrabajo[i].equals("TRATAMIENTOS")){
                Tratamientos.setChecked(true);
            }
            if(tipoTrabajo[i].equals("FUMIGACION")){
                Fumigacion.setChecked(true);
            }
            if(tipoTrabajo[i].equals("-")){
                sinIntervencion.setChecked(true);
            }
        }

        destruidos = findViewById(R.id.EDTDESTRUIDOS);
        if(!datosDengue.Destruidos.equals("0")){
            destruidos.setText(datosDengue.Destruidos);
        }

        siLarvicida = findViewById(R.id.SILARVICIDA);
        noLarvicida = findViewById(R.id.NOLARVICIDA);
        if(datosDengue.Larvicida.equals("SI")){
            siLarvicida.setChecked(true);
        }
        if(datosDengue.Larvicida.equals("NO")){
            noLarvicida.setChecked(true);
        }

        cerrada = findViewById(R.id.CERRADA);
        if(datosDengue.SituacionVivienda.equals("X")){
            cerrada.setChecked(true);
        }
    }

//--------------------------------------------------------------------------------------------------

    public void ElementosDesuso(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.elementos_desuso);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.ElementosDesuso.equals("0;0;0")) {
            String[] valores = datosDengue.ElementosDesuso.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";

                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.ElementosDesuso = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.ElementosDesuso.equals("0;0;0")){
                    layout_desuso.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_desuso.setBackgroundResource(R.drawable.edit_text_1);}
                dialog.dismiss();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void BotellasVarias(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.botellas_varias);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.Botellas.equals("0;0;0")) {
            String[] valores = datosDengue.Botellas.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";
                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.Botellas = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.Botellas.equals("0;0;0")){
                    layout_botellas.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_botellas.setBackgroundResource(R.drawable.edit_text_1);}
            }
        });
        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void RecipientesPlasticos(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.recipientes_plasticos);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.RecipientesPlasticos.equals("0;0;0")) {
            String[] valores = datosDengue.RecipientesPlasticos.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";
                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.RecipientesPlasticos = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.RecipientesPlasticos.equals("0;0;0")){
                layout_recipientes.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_recipientes.setBackgroundResource(R.drawable.edit_text_1);}
                //Toast.makeText(getApplicationContext(),datosDengue.RecipientesPlasticos, Toast.LENGTH_SHORT).show();
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void Maceta(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.maceta);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.Macetas.equals("0;0;0")) {
            String[] valores = datosDengue.Macetas.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";
                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.Macetas = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.Macetas.equals("0;0;0")){
                    layout_maceta.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_maceta.setBackgroundResource(R.drawable.edit_text_1);}
            }
        });
        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void Hueco(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.hueco);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.Hueco.equals("0;0;0")) {
            String[] valores = datosDengue.Hueco.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";
                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.Hueco = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.Hueco.equals("0;0;0")){
                    layout_hueco.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_hueco.setBackgroundResource(R.drawable.edit_text_1);}
            }
        });
        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void Canaleta(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.canaleta);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.Canaleta.equals("0;0;0")) {
            String[] valores = datosDengue.Canaleta.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";
                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.Canaleta = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.Canaleta.equals("0;0;0")){
                    layout_canaleta.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_canaleta.setBackgroundResource(R.drawable.edit_text_1);}
            }
        });
        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void Cubierta(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.cubiertas);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.Cubiertas.equals("0;0;0")) {
            String[] valores = datosDengue.Cubiertas.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";
                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.Cubiertas = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.Cubiertas.equals("0;0;0")){
                    layout_cubierta.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_cubierta.setBackgroundResource(R.drawable.edit_text_1);}
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void Piletas(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.piletas);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.Piletas.equals("0;0;0")) {
            String[] valores = datosDengue.Piletas.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";
                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.Piletas = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.Piletas.equals("0;0;0")){
                    layout_pileta.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_pileta.setBackgroundResource(R.drawable.edit_text_1);}
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void TanquesBajosAltos(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alert_opciones_dengue, null);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView cabecera = view1.findViewById(R.id.TXTOPCDENGUE);
        cabecera.setText(R.string.tanque);

        final EditText I = view1.findViewById(R.id.EDTINSPECCIONADO);
        final EditText T = view1.findViewById(R.id.EDTTRATADOS);
        final EditText FA = view1.findViewById(R.id.EDTFOCOAEDICO);

        if(!datosDengue.Tanques.equals("0;0;0")) {
            String[] valores = datosDengue.Tanques.split(";");
            I.setText(valores[0]);
            T.setText(valores[1]);
            FA.setText(valores[2]);
        }

        final Button guardar = view1.findViewById(R.id.GUARDAROPCIONESDENGUE);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] auxGuardar = new String[3];
                auxGuardar[0]="0";
                auxGuardar[1]="0";
                auxGuardar[2]="0";
                if(I.getText().toString().length()!=0){
                    auxGuardar[0]=I.getText().toString();
                    inspeccionadosTotal+=Integer.parseInt(I.getText().toString());
                }
                if(T.getText().toString().length()!=0){
                    auxGuardar[1]=T.getText().toString();
                    tratadosTotal+=Integer.parseInt(T.getText().toString());
                }
                if(FA.getText().toString().length()!=0){
                    auxGuardar[2]=FA.getText().toString();
                    focoaedicoTotal+=Integer.parseInt(FA.getText().toString());
                }
                datosDengue.Tanques = auxGuardar[0]+";"+auxGuardar[1]+";"+auxGuardar[2];
                if(!datosDengue.Tanques.equals("0;0;0")){
                    layout_tanquesbajos.setBackgroundResource(R.drawable.cuadrado_verde);}
                else{layout_tanquesbajos.setBackgroundResource(R.drawable.edit_text_1);}
            }
        });

        final Button cancelar = view1.findViewById(R.id.CANCELAROPCIONESDENGUE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void Guardar(View view){

        String situacionVivienda = "";
        if(cerrada.isChecked()){situacionVivienda="X";}
        datosDengue.SituacionVivienda = situacionVivienda;

        String tipoTrabajo = "";
        if (Inspeccion.isChecked()){
            if (tipoTrabajo==""){
                tipoTrabajo="INSPECCION DOMICILIARIA";
            } else {
                tipoTrabajo+=",INSPECCION DOMICILIARIA";}}
        if (Tratamientos.isChecked()){
            if (tipoTrabajo==""){
                tipoTrabajo="TRATAMIENTOS";
            } else {
                tipoTrabajo+=",TRATAMIENTOS";}}
        if (Fumigacion.isChecked()){
            if (tipoTrabajo==""){
                tipoTrabajo="FUMIGACION";
            } else {
                tipoTrabajo+=",FUMIGACION";}}
        if (sinIntervencion.isChecked()){tipoTrabajo="-";}

        datosDengue.TipoTrabajo = tipoTrabajo;

        datosDengue.TotalTratados = Integer.toString(tratadosTotal);
        datosDengue.TotalIspeccionado = Integer.toString(inspeccionadosTotal);
        datosDengue.TotalFocoAedico = Integer.toString(focoaedicoTotal);

        if(destruidos.getText().toString().length()!=0){
        datosDengue.Destruidos = destruidos.getText().toString();}
        else {datosDengue.Destruidos="0";}

        if(siLarvicida.isChecked()){datosDengue.Larvicida = "SI";}
        if(noLarvicida.isChecked()){datosDengue.Larvicida = "NO";}

        if(datosDengue.TipoTrabajo.length()!=0){
        Intent Modif1= new Intent (this, Familia.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DENGUE", datosDengue);

        Modif1.putExtras(bundle);
        //Toast.makeText(this, "PERSONA CARGADA", Toast.LENGTH_SHORT).show();
        setResult(3, Modif1);
        finish();}
        else {Toast.makeText(this, "FALTA CARGAR EL TIPO DE TRABAJO", Toast.LENGTH_SHORT).show();}
    }

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