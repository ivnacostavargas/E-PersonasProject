package com.gob.pgutierrezd.e_personas.interactors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.gob.pgutierrezd.e_personas.interfaces.encuesta.EncuestaInteractor;
import com.gob.pgutierrezd.e_personas.models.AnswersInterview;
import com.gob.pgutierrezd.e_personas.models.CoordsInterview;
import com.gob.pgutierrezd.e_personas.models.InformationComplement;
import com.gob.pgutierrezd.e_personas.sqlite.DataBaseOpenHelper;
import com.gob.pgutierrezd.e_personas.utils.Constants;

/**
 * Created by pgutierrezd on 18/10/2016.
 */
public class EncuestaInteractorImpl implements EncuestaInteractor{

    protected SQLiteOpenHelper dbHelper;
    protected SQLiteDatabase database;

    @Override
    public void sendInterview(final AnswersInterview answersInterview, final InformationComplement informationComplement, final boolean bandera, final Context context, final OnInterviewFinishedListener listener) {
        if(answersInterview.getsNecesidadColonia().isEmpty() || answersInterview.getoOpinionRealizarObraNombre().isEmpty()
                || answersInterview.getaMejorarImagen().isEmpty()){
            listener.errorSendInterview();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveInterviewSqlite(answersInterview,informationComplement, bandera, context,listener);
                }
            },2000);
        }
    }

    private void saveInterviewSqlite(AnswersInterview answersInterview, InformationComplement informationComplement,boolean bandera, Context context, OnInterviewFinishedListener listener) {
        dbHelper = new DataBaseOpenHelper(context);
        database = dbHelper.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put(Constants.IDUSUARIO, answersInterview.getsIdUsuario());
            values.put(Constants.CS_OPINION_POLICIA, answersInterview.getsOpinionPolicia());
            values.put(Constants.CS_PATRULLAJE, answersInterview.getsPatrullaje());
            values.put(Constants.CS_HORARIO_PATRULLAJE, answersInterview.getsHorarioPatrullaje());
            values.put(Constants.CS_ACCIONES_MUNICIPIO, answersInterview.getsAccionesMunicipio());
            values.put(Constants.CS_TRAMITE_PENDIENTE_BOOL, answersInterview.getsTramitePendiente());
            values.put(Constants.CS_TRAMITE_PENDIENTE_COM, answersInterview.getsTramitePendienteComentario());
            values.put(Constants.CS_GOBIERNO_MARCOS_AGUILAR, answersInterview.getsGobiernoMarcosAguilar());
            values.put(Constants.CS_ESPERAR_MARCOS, answersInterview.getsEsperaMarcosAguilar());
            values.put(Constants.CS_DEPORTES_BOOL, answersInterview.getsPracticaDeportes());
            values.put(Constants.CS_DEPORTES_COM, answersInterview.getsDeporte());
            values.put(Constants.CS_NECESIDAD_COLONIA, answersInterview.getsNecesidadColonia());
            values.put(Constants.ID_OPINION_REALIZAR_OBRA_NOMBRE, answersInterview.getoOpinionRealizarObraNombre());
            values.put(Constants.ID_OPINION_REALIZAR_OBRA, answersInterview.getoOpinionRealizaObra());
            values.put(Constants.ID_HORARIO_MAYOR_RIESGO, answersInterview.getoHorarioMayorRiesgo());
            values.put(Constants.ID_RIESGOS_FRACCIONAMIENTO, answersInterview.getoRiesgosFraccionamiento());
            values.put(Constants.IB_HAY_LOTES_BALDIOS, answersInterview.getbHayLotesBaldios());
            values.put(Constants.IB_CONSIDERA_RIESGOS, answersInterview.getbConsideraRiesgos());
            values.put(Constants.IB_DEBEN_ATENDERSE, answersInterview.getbDebenAtenderse());
            values.put(Constants.IB_REALIZADO_LIMPIEZA, answersInterview.getbRealizadoLimpieza());
            values.put(Constants.IB_DESMALEZADO_LOTES, answersInterview.getbDesmalezadoLotes());
            values.put(Constants.SA_APARIENCIA_ALAMEDA, answersInterview.getaAparienciaAlameda());
            values.put(Constants.SA_SEGURO_AL_TRANSITAR, answersInterview.getaSeguroTransitar());
            values.put(Constants.SA_ALAMEDA_RECREO, answersInterview.getaAlamedaRecreo());
            values.put(Constants.SA_PROBLEMA_ALAMEDA, answersInterview.getaProblemaAlameda());
            values.put(Constants.SA_MEJORAR_IMAGEN, answersInterview.getaMejorarImagen());
            values.put(Constants.LUGAR_ENCUESTA, answersInterview.getaLugarEncuesta());
            values.put(Constants.FECHA, answersInterview.getFecha());
            values.put(Constants.STATUS, answersInterview.getStatus());
            values.put(Constants.LATITUD, answersInterview.getLatitud());
            values.put(Constants.LONGITUD, answersInterview.getLongitud());
            values.put(Constants.COMENTARIOS, "");
            values.put(Constants.REFERENCIA_MOVIL, answersInterview.getReferenciaMovil());
            int id = (int) database.insert(Constants.TABLE_ENCUESTAS, null,values);

            Cursor cursor = database.rawQuery("SELECT * FROM coords_prueba",null);
            if(cursor.getCount() > 0){
                while(cursor.moveToNext()){
                    ContentValues valuesCoords = new ContentValues();
                    valuesCoords.put(Constants.LATITUD, cursor.getString(cursor.getColumnIndex("latitud")));
                    valuesCoords.put(Constants.LONGITUD, cursor.getString(cursor.getColumnIndex("longitud")));
                    valuesCoords.put(Constants.IDENCUESTA, id);
                    database.insert(Constants.TABLE_COORDINATES, null, valuesCoords);
                }
            }
            if(bandera){
                try{
                    ContentValues valuesExtra = new ContentValues();
                    valuesExtra.put(Constants.IDENCUESTA, id);
                    valuesExtra.put(Constants.TELEFONO, informationComplement.getTelefono());
                    valuesExtra.put(Constants.EMAIL, informationComplement.getEmail());
                    valuesExtra.put(Constants.FACEBOOK, informationComplement.getFacebook());
                    valuesExtra.put(Constants.TWITTER, informationComplement.getTwitter());
                    valuesExtra.put(Constants.SEXO, informationComplement.getGenero());
                    valuesExtra.put(Constants.EDAD, informationComplement.getEdad());
                    valuesExtra.put(Constants.FECHA, informationComplement.getFecha());
                    valuesExtra.put(Constants.FOTO, informationComplement.getFoto());
                    int i = (int) database.insert(Constants.TABLE_INFORMACION_COMPLEMENTARIA, null, valuesExtra);
                    Log.d("AAA",""+i);
                }catch (Exception e){
                    Log.d("AAA",e.getMessage());
                }
            }
            database.execSQL("delete from coords_prueba");
            Cursor cursor3 = database.rawQuery("SELECT * FROM informacion_complementaria;",null);
            Toast.makeText(context, "Encuesta guardada",Toast.LENGTH_LONG).show();
            listener.navigateToHome();
        }catch (Exception e){
         listener.errorSendInterview();
        }

    }

}
