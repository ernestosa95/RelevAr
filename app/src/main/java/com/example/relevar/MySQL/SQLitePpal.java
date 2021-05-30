package com.example.relevar.MySQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.SolverVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLitePpal extends SQLiteOpenHelper {
    final String CREAR_TABLA_EFECTORES = "CREATE TABLE EFECTORES (NOMBRE TEXT, PROVINCIA TEXT)";
    final String CREAR_TABLA_TRABAJOS = "CREATE TABLE TRABAJOS (TRABAJO TEXT)";
    final String CREAR_TABLA_UNIFICADOS = "CREATE TABLE UNIFICADOS (FECHA TEXT)";
    final String CREAR_TABLA_BOTONES = "CREATE TABLE BOTONES (BOTON TEXT, ACTIVO BOOLEAN)";
    final String CREAR_TABLA_ENCUESTADORES = "CREATE TABLE ENCUESTADOR (ID TEXT,APELLIDO TEXT,PROVINCIA TEXT,DNI TEXT, ACTIVO BOOLEAN)";

    final ArrayList<String> datos = new ArrayList<String>();

    public SQLitePpal(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_EFECTORES);
        db.execSQL(CREAR_TABLA_TRABAJOS);
        db.execSQL(CREAR_TABLA_ENCUESTADORES);
        db.execSQL(CREAR_TABLA_BOTONES);
        db.execSQL(CREAR_TABLA_UNIFICADOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Verifica si existe una version mas antigua de la base de datos
        db.execSQL("DROP TABLE IF EXISTS EFECTORES");
        db.execSQL("DROP TABLE IF EXISTS TRABAJOS");
        db.execSQL("DROP TABLE IF EXISTS ENCUESTADOR");
        db.execSQL("DROP TABLE IF EXISTS BOTONES");
        db.execSQL("DROP TABLE IF EXISTS UNIFICADOS");
        onCreate(db);
    }

    public void CrearTablaUnificados(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS UNIFICADOS (FECHA TEXT)");
        db.close();
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

    public boolean ExisteEfectores(String Provincia){
        SQLiteDatabase db = this.getReadableDatabase();

        //String aux1 = "SELECT name FROM sqlite_master WHERE name='TRABAJOS'";
        String aux2 = "SELECT * FROM EFECTORES WHERE PROVINCIA='"+Provincia+"'";

        Cursor registros = db.rawQuery(aux2, null);

        if(registros.getCount()==0){
            return false;
        }
        else{
            return true;
        }
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

    public void CrearUsuario(String Nombre, String Apellido, String DNI, String Provincia){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("ID", Nombre);
        registro.put("APELLIDO", Apellido);
        registro.put("PROVINCIA", Provincia);
        registro.put("DNI", DNI);
        registro.put("ACTIVO", true);
        db.insert("ENCUESTADOR", null, registro);
        db.close();
    }

    public ArrayList<String> Encuestadores(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> encuestadores = new ArrayList<>();
        String consulta ="SELECT * FROM ENCUESTADOR";
        Cursor a = db.rawQuery(consulta, null);
        while (a.moveToNext()){
            encuestadores.add(a.getString(0)+","+a.getString(1));
        }
        db.close();
        return encuestadores;
    }

    public boolean existeEncuestador(String Nombre, String Apellido){
        SQLiteDatabase db = this.getReadableDatabase();

        String aux2 = "SELECT * FROM ENCUESTADOR WHERE ID='"+Nombre+"' AND APELLIDO='"+Apellido+"'";

        Cursor registros = db.rawQuery(aux2, null);

        if(registros.getCount()==0){
            return false;
        }
        else{
            return true;
        }
    }

    public void desactivarUsuarios(){
        SQLiteDatabase dbRead = this.getReadableDatabase();
        String cantidad = "SELECT DISTINCT * FROM ENCUESTADOR";
        Cursor cant = dbRead.rawQuery(cantidad, null);
        if(cant.getCount()!=0){
        SQLiteDatabase db = this.getWritableDatabase();
            //Establecemos los campos-valores a actualizar
            ContentValues valores = new ContentValues();
            valores.put("ACTIVO",Boolean.FALSE);

            //Actualizamos el registro en la base de datos
            db.update("ENCUESTADOR", valores, null, null);
        db.close();}
        dbRead.close();
    }

    public void activarUsuario(String Nombre, String Apellido){
        SQLiteDatabase db = this.getWritableDatabase();
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put("ACTIVO",Boolean.TRUE);
        //Actualizamos el registro en la base de datos
        String[] args = new String[]{Nombre, Apellido};
        db.update("ENCUESTADOR", valores, "ID=? AND APELLIDO=?" , args);
        db.close();
    }

    public String ObtenerActivado(){
        String encuestador="";
        SQLiteDatabase db = this.getReadableDatabase();
        String consulta ="SELECT ID, APELLIDO FROM ENCUESTADOR WHERE ACTIVO=1";
        Cursor a = db.rawQuery(consulta, null);
        a.moveToFirst();
        if(a.getCount()!=0){
        encuestador = a.getString(0)+" "+a.getString(1);}//+" "+a.getString(1);}
        return encuestador;
    }

    public HashMap<String, String> encuestadorActivado(){
        HashMap<String, String> encuestador = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String consulta ="SELECT ID, APELLIDO, PROVINCIA, DNI FROM ENCUESTADOR WHERE ACTIVO=1";
        Cursor a = db.rawQuery(consulta, null);
        a.moveToFirst();
        if(a.getCount()!=0) {
            encuestador.put("NOMBRE", a.getString(0));
            encuestador.put("APELLIDO", a.getString(1));
            encuestador.put("PROVINCIA", a.getString(2));
            encuestador.put("DNI", a.getString(3));
        }
        return encuestador;
    }

    public String ObtenerDniActivado(){
        String encuestador="";
        SQLiteDatabase db = this.getReadableDatabase();
        String consulta ="SELECT DNI FROM ENCUESTADOR WHERE ACTIVO=1";
        Cursor a = db.rawQuery(consulta, null);
        a.moveToFirst();
        encuestador = a.getString(0);
        return encuestador;
    }

    public void DesactivarBotones(){
        SQLiteDatabase dbRead = this.getReadableDatabase();
        String cantidad = "SELECT DISTINCT * FROM BOTONES";

        Cursor cant = dbRead.rawQuery(cantidad, null);
        if(cant.getCount()!=0){
            SQLiteDatabase db = this.getWritableDatabase();
            //Establecemos los campos-valores a actualizar
            ContentValues valores = new ContentValues();
            valores.put("ACTIVO",Boolean.FALSE);

//Actualizamos el registro en la base de datos
            db.update("BOTONES", valores, null, null);
            db.close();}
        dbRead.close();

    }

    public void ActivarBoton(String nombreBoton){
        SQLiteDatabase db = this.getWritableDatabase();
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put("ACTIVO",Boolean.TRUE);

        //Actualizamos el registro en la base de datos
        String[] args = new String[]{nombreBoton};
        db.update("BOTONES", valores, "BOTON=?" , args);
        db.close();
    }

    public void DesactivarBoton(String nombreBoton){
        SQLiteDatabase db = this.getWritableDatabase();
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put("ACTIVO",Boolean.FALSE);

        //Actualizamos el registro en la base de datos
        String[] args = new String[]{nombreBoton};
        db.update("BOTONES", valores, "BOTON=?" , args);
        db.close();
    }

    public Boolean EstadoBoton(String nombreBoton){
        SQLiteDatabase db = this.getReadableDatabase();

        //String aux1 = "SELECT name FROM sqlite_master WHERE name='TRABAJOS'";
        String aux2 = "SELECT ACTIVO FROM BOTONES WHERE BOTON='"+nombreBoton+"'";

        Cursor registros = db.rawQuery(aux2, null);
        registros.moveToFirst();
        if (registros.isNull(0) || registros.getShort(0) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<String> Botones(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> botones = new ArrayList<>();
        String consulta ="SELECT * FROM BOTONES";
        Cursor a = db.rawQuery(consulta, null);
        while (a.moveToNext()){
            botones.add(a.getString(0));
        }
        db.close();
        return botones;
    }

    public boolean ExisteFechaUnificados(String Fecha){
        SQLiteDatabase db = this.getReadableDatabase();

        //String aux1 = "SELECT name FROM sqlite_master WHERE name='TRABAJOS'";
        String aux2 = "SELECT * FROM UNIFICADOS WHERE FECHA='"+Fecha+"'";

        Cursor registros = db.rawQuery(aux2, null);

        if(registros.getCount()==0){
            return false;
        }
        else{
            return true;
        }
    }
}
