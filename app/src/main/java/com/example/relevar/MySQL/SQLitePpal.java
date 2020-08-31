package com.example.relevar.MySQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLitePpal extends SQLiteOpenHelper {
    final String CREAR_TABLA_EFECTORES = "CREATE TABLE EFECTORES (NOMBRE TEXT, PROVINCIA TEXT)";
    final String CREAR_TABLA_TRABAJOS = "CREATE TABLE TRABAJOS (TRABAJO TEXT)";
    final String CREAR_TABLA_ENCUESTADORES = "CREATE TABLE ENCUESTADOR (ID TEXT,APELLIDO TEXT,ACTIVO BOOLEAN)";

    final ArrayList<String> datos = new ArrayList<String>();

    public SQLitePpal(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_EFECTORES);
        db.execSQL(CREAR_TABLA_TRABAJOS);
        db.execSQL(CREAR_TABLA_ENCUESTADORES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Verifica si existe una version mas antigua de la base de datos
        db.execSQL("DROP TABLE IF EXISTS EFECTORES");
        db.execSQL("DROP TABLE IF EXISTS TRABAJOS");
        db.execSQL("DROP TABLE IF EXISTS ENCUESTADOR");
        onCreate(db);
    }


    public ArrayList<String> Buscar_Registros(String Nombre){
        datos.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        String aux = "SELECT DISTINCT NOMBRE FROM EFECTORES WHERE NOMBRE LIKE '%"+Nombre+"%'";
        //String aux1 = "SELECT * FROM EFECTORES";
        Cursor registros = db.rawQuery(aux, null);

        if(registros.moveToFirst()){
            String auxData = "";
            auxData = registros.getString(0);
            datos.add(auxData);

        while (registros.moveToNext()){
            String auxData1 = "";
            auxData1 = registros.getString(0);
            datos.add(auxData1);}
        }

        else{
            String auxData1 = "";
            datos.add(auxData1);
        }
        db.close();
        registros.close();
        return datos;
    }

    public boolean ExisteProvincia(String Provincia){
        SQLiteDatabase db = this.getReadableDatabase();
        String aux = "SELECT DISTINCT PROVINCIA FROM EFECTORES WHERE PROVINCIA = '"+Provincia+"'";
        Cursor registros = db.rawQuery(aux, null);
        if(registros.getCount()==0){
            db.close();
            return false;}
        else {
            db.close();
            return true;}
    }

    public boolean ExisteTrabajos(){
        SQLiteDatabase db = this.getReadableDatabase();

        //String aux1 = "SELECT name FROM sqlite_master WHERE name='TRABAJOS'";
        String aux2 = "SELECT * FROM TRABAJOS";

        Cursor registros = db.rawQuery(aux2, null);

        if(registros.getCount()==0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean TieneElementosTrabajos(){
        SQLiteDatabase db = this.getReadableDatabase();
        String aux = "SELECT DISTINCT * FROM TRABAJOS";
        String aux1 = "SELECT name FROM sqlite_master WHERE name='TRABAJOS'";

        Cursor registros = db.rawQuery(aux, null);
        if(registros.getCount()==0){
            db.close();
            return false;}
        else {
            db.close();
            return false;}
    }

    public ArrayList<String> BuscarTrabajo(String Trabajo){
        datos.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        String aux = "SELECT DISTINCT TRABAJO FROM TRABAJOS WHERE TRABAJO LIKE '%"+Trabajo+"%'";
        String aux1 = "SELECT * FROM TRABAJOS";
        Cursor registros = db.rawQuery(aux, null);

        if(registros.moveToFirst()){
            String auxData = "";
            auxData = registros.getString(0);
            datos.add(auxData);

            while (registros.moveToNext()){
                String auxData1 = "";
                auxData1 = registros.getString(0);
                datos.add(auxData1);}
        }

        else{
            String auxData1 = "";
            datos.add(auxData1);
        }
        db.close();
        registros.close();
        return datos;
    }

    public void CrearUsuario(String Nombre, String Apellido){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("ID", Nombre);
        registro.put("APELLIDO", Apellido);
        db.insert("ENCUESTADOR", null, registro);
        db.close();
    }

    public ArrayList<String> Encuestadores(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> encuestadores = new ArrayList<>();
        String consulta ="SELECT * FROM ENCUESTADOR";
        Cursor a = db.rawQuery(consulta, null);
        while (a.moveToNext()){
            encuestadores.add(a.getString(0));//+" "+a.getString(1));
        }
        db.close();
        return encuestadores;
    }
}
