package com.gob.pgutierrezd.e_personas.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gob.pgutierrezd.e_personas.utils.Constants;

/**
 * Created by pgutierrezd on 10/08/2016.
 */
public class DataBaseOpenHelper extends SQLiteOpenHelper {

    //tabla de encuestas
    private final String CREATE_TABLE_ENCUESTAS =
            "CREATE TABLE " + Constants.TABLE_ENCUESTAS + "(" +
                    Constants.IDENCUESTA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Constants.IDUSUARIO + " INTEGER DEFAULT NULL," +
                    Constants.CS_OPINION_POLICIA + " VARCHAR(25) DEFAULT NULL," +
                    Constants.CS_PATRULLAJE + " INTEGER DEFAULT NULL," +
                    Constants.CS_HORARIO_PATRULLAJE + " VARCHAR(60) DEFAULT NULL," +
                    Constants.CS_ACCIONES_MUNICIPIO + " VARCHAR(25) DEFAULT NULL," +
                    Constants.CS_TRAMITE_PENDIENTE_BOOL + " INTEGER DEFAULT NULL," +
                    Constants.CS_TRAMITE_PENDIENTE_COM + " VARCHAR(200) DEFAULT NULL," +
                    Constants.CS_GOBIERNO_MARCOS_AGUILAR + " VARCHAR(25) DEFAULT NULL," +
                    Constants.CS_ESPERAR_MARCOS + " VARCHAR(100) DEFAULT NULL," +
                    Constants.CS_DEPORTES_BOOL + " INTEGER(1) DEFAULT NULL," +
                    Constants.CS_DEPORTES_COM + " VARCHAR(200) DEFAULT NULL," +
                    Constants.CS_NECESIDAD_COLONIA + " VARCHAR(250) DEFAULT NULL," +
                    Constants.ID_OPINION_REALIZAR_OBRA_NOMBRE + " VARCHAR(200) DEFAULT NULL," +
                    Constants.ID_OPINION_REALIZAR_OBRA + " INTEGER DEFAULT NULL," +
                    Constants.ID_HORARIO_MAYOR_RIESGO + " VARCHAR(200) DEFAULT NULL," +
                    Constants.ID_RIESGOS_FRACCIONAMIENTO + " VARCHAR(200) DEFAULT NULL," +
                    Constants.IB_HAY_LOTES_BALDIOS + " INTEGER DEFAULT NULL," +
                    Constants.IB_CONSIDERA_RIESGOS + " INTEGER DEFAULT NULL," +
                    Constants.IB_DEBEN_ATENDERSE + " INTEGER DEFAULT NULL," +
                    Constants.IB_REALIZADO_LIMPIEZA + " INTEGER DEFAULT NULL," +
                    Constants.IB_DESMALEZADO_LOTES + " VARCHAR(20) DEFAULT NULL," +
                    Constants.SA_APARIENCIA_ALAMEDA + " INTEGER DEFAULT NULL," +
                    Constants.SA_SEGURO_AL_TRANSITAR + " INTEGER DEFAULT NULL," +
                    Constants.SA_ALAMEDA_RECREO + " INTEGER DEFAULT NULL," +
                    Constants.SA_PROBLEMA_ALAMEDA + " VARCHAR(150) DEFAULT NULL," +
                    Constants.SA_MEJORAR_IMAGEN + " VARCHAR(250) DEFAULT NULL," +
                    Constants.LUGAR_ENCUESTA + " VARCHAR(100) DEFAULT NULL," +
                    Constants.FECHA + " DATE DEFAULT NULL," +
                    Constants.STATUS + " INTEGER DEFAULT NULL," +
                    Constants.LATITUD + " VARCHAR(50) DEFAULT NULL," +
                    Constants.LONGITUD + " VARCHAR(50) DEFAULT NULL," +
                    Constants.COMENTARIOS + " VARCHAR(250) DEFAULT NULL," +
                    Constants.REFERENCIA_MOVIL + " VARCHAR(30) DEFAULT NULL"+
    ");";

    //TABLA DE INFORMACION COMPLEMENTARIA
    private final String CREATE_TABLE_INFORMACION =
            "CREATE TABLE " + Constants.TABLE_INFORMACION_COMPLEMENTARIA + "("+
                    Constants.IDINFO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.IDENCUESTA + " INTEGER DEFAULT NULL," +
                    Constants.TELEFONO + " VARCHAR(20) DEFAULT NULL, " +
                    Constants.EMAIL + " VARCHAR(100) DEFAULT NULL, " +
                    Constants.FACEBOOK + " VARCHAR(70) DEFAULT NULL, " +
                    Constants.TWITTER + " VARCHAR(40) DEFAULT NULL, " +
                    Constants.SEXO + " VARCHAR(10) DEFAULT NULL, " +
                    Constants.EDAD + " INTEGER DEFAULT NULL, " +
                    Constants.FECHA + " DATE DEFAULT NULL, " +
                    Constants.FOTO + " TEXT DEFAULT NULL " +
                    ");";
    private final String CREATE_TABLE_COORDENADAS =
            "CREATE TABLE " + Constants.TABLE_COORDINATES + "(" +
                    Constants.ID_COORDS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.LATITUD + " VARCHAR(50) DEFAULT NULL, " +
                    Constants.LONGITUD + " VARCHAR(50) DEFAULT NULL, " +
                    Constants.IDENCUESTA + " INTEGER DEFAULT NULL)";

    private final String CREATE_TABLE_COORDENADAS_PRUEBA =
            "CREATE TABLE coords_prueba(" +
                    Constants.ID_COORDS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.LATITUD + " VARCHAR(50) DEFAULT NULL, " +
                    Constants.LONGITUD + " VARCHAR(50) DEFAULT NULL);";

    public DataBaseOpenHelper(Context context) {
        super(context,Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ENCUESTAS);
        db.execSQL(CREATE_TABLE_INFORMACION);
        db.execSQL(CREATE_TABLE_COORDENADAS);
        db.execSQL(CREATE_TABLE_COORDENADAS_PRUEBA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_ENCUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_INFORMACION_COMPLEMENTARIA);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_COORDINATES);
        db.execSQL("DROP TABLE IF EXISTS coords_prueba");
        onCreate(db);
    }
}