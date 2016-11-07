package com.gob.pgutierrezd.e_personas.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.gob.pgutierrezd.e_personas.utils.Constants;

/**
 * Created by iacostav on 04/11/2016.
 */

public class DataSource {

    private SQLiteDatabase db;
    Context context;

    public DataSource(Context context){
        this.context =context;
        db = new DataBaseOpenHelper(context).getWritableDatabase();
    }

    public Cursor getEncuestas() {
        return db.rawQuery("SELECT * FROM " + Constants.TABLE_ENCUESTAS,null);
    }

    public Cursor getEncuestassById(String id){
        return db.rawQuery("SELECT * FROM " + Constants.TABLE_ENCUESTAS + " WHERE _id=?",new String[]{id});
    }

    public Cursor getCoordenadas(String id){
        return db.rawQuery("SELECT * FROM " + Constants.TABLE_COORDINATES + " WHERE " + Constants.IDENCUESTA +"=?",new String[]{id});
    }

    public void Update(String tabla, ContentValues cv, String condicion, String id){
        db.update(tabla, cv, condicion, new String[]{id});
    }

    public void deleteRegistro(String table, String id){
        db.delete(table," _id=? ",new String[]{id});
        db.delete(Constants.TABLE_INFORMACION_COMPLEMENTARIA, Constants.IDENCUESTA +"=?",new String[]{id});
    }

    public Cursor getInformacionComplementariaById(String id){
        return db.rawQuery("SELECT * FROM " + Constants.TABLE_INFORMACION_COMPLEMENTARIA + " WHERE "+Constants.IDENCUESTA+"=?", new String[]{id});
    }

}
