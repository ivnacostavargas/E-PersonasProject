package com.gob.pgutierrezd.e_personas.views;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.gob.pgutierrezd.e_personas.R;
import com.gob.pgutierrezd.e_personas.interfaces.encuesta.EncuestaPresenter;
import com.gob.pgutierrezd.e_personas.interfaces.encuesta.EncuestaView;
import com.gob.pgutierrezd.e_personas.models.AnswersInterview;
import com.gob.pgutierrezd.e_personas.models.CoordsInterview;
import com.gob.pgutierrezd.e_personas.models.InformationComplement;
import com.gob.pgutierrezd.e_personas.presenters.EncuestaPresenterImpl;
import com.gob.pgutierrezd.e_personas.utils.AlarmReceiver;
import com.gob.pgutierrezd.e_personas.utils.Constants;
import com.gob.pgutierrezd.e_personas.utils.ConvertBase64;
import com.gob.pgutierrezd.e_personas.utils.GpsLocation;
import com.gob.pgutierrezd.e_personas.utils.ShowMessageDialog;
import com.gob.pgutierrezd.e_personas.utils.TakePhoto;
import com.gob.pgutierrezd.e_personas.utils.ValidateFields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EncuestaActivity extends AppCompatActivity implements EncuestaView , View.OnClickListener{

    //Campos de encuesta
    //Colonia segura
    private EditText mTPregunta5R2ColSegura,mTPregunta8R2ColSegura, mTPregunta9R1ColSegura;
    private RadioButton mRPregunta1R1ColSegura,mRPregunta1R2ColSegura,mRPregunta1R3ColSegura,mRPregunta1R4ColSegura;
    private RadioButton mRPregunta4R1ColSegura,mRPregunta4R2ColSegura,mRPregunta4R3ColSegura,mRPregunta4R4ColSegura;
    private RadioButton mRPregunta6R1ColSegura, mRPregunta6R2ColSegura,mRPregunta6R3ColSegura,mRPregunta6R4ColSegura;
    private CheckBox mCPregunta3R1ColSegura,mCPregunta3R2ColSegura,mCPregunta3R3ColSegura,mCPregunta3R4ColSegura;
    private CheckBox mCPregunta7R1ColSegura, mCPregunta7R2ColSegura,mCPregunta7R3ColSegura,mCPregunta7R4ColSegura,mCPregunta7R5ColSegura;
    private Switch mSPregunta2R1ColSegura,mSPregunta5R1ColSegura,mSPregunta8R1ColSegura;
    //Induccion de obra
    private EditText mTPregunta1R1IndObra, mTPregunta3R6IndObra;
    private RadioButton mRPregunta1R2IndObra, mRPregunta1R3IndObra, mRPregunta1R4IndObra, mRPregunta1R5IndObra;
    private CheckBox mCPregunta2R1IndObra, mCPregunta2R2IndObra, mCPregunta2R3IndObra, mCPregunta2R4IndObra;
    private CheckBox mCPregunta3R1IndObra, mCPregunta3R2IndObra, mCPregunta3R3IndObra, mCPregunta3R4IndObra,mCPregunta3R5IndObra;
    //Lotes baldios
    private Switch mSPregunta1R1LotesBaldios, mSPregunta2R1LotesBaldios, mSPregunta3R1LotesBaldios, mSPregunta4R1LotesBaldios;
    private RadioButton mRPregunta5R1LotesBaldios,mRPregunta5R2LotesBaldios, mRPregunta5R3LotesBaldios;
    //Sondeo Alameda
    private EditText mTPregunta5R1SondAlameda;
    private Switch mSPregunta1R1SondAlameda, mSPregunta2R1SondAlameda, mSPregunta3R1SondAlameda, mSPregunta6R1SondAlameda;
    private CheckBox mCPregunta4R1SondAlameda, mCPregunta4R2SondAlameda, mCPregunta4R3SondAlameda;
    private Switch mSwcEncuestaDialog;

    private Spinner mSpnLugar;
    private EditText mTextOtro;

    private Button mBtnEnviarEncuesta;

    //Campos de dialogo informacion complementaria
    private EditText mTextTelefono, mTextCorreo, mTxtFacebook, mTxtTwitter, mTextEdadAprox;
    private RadioButton mRbHombre, mRbMujer;
    private ImageView mImgIdentificacion;
    private ImageButton btnAgregarFoto, btnEliminarFoto;

    private ShowMessageDialog mShowMessageDialog;
    private ValidateFields mValidateFields;

    private Uri mPath;
    private SharedPreferences mPreferences;
    private PendingIntent mAlarmIntent;
    private ArrayAdapter<CharSequence> adapter;
    private Context context;
    private List<CoordsInterview> coords;

    private EncuestaPresenter mEncuestaPresenter;
    private InformationComplement informationComplement;
    private String myBase64Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        context = getApplicationContext();
        myBase64Image = "";
        findViews();
        coords = new ArrayList<>();
        getCoordsPerMinute();
        mEncuestaPresenter = new EncuestaPresenterImpl(this,this);
        mShowMessageDialog = new ShowMessageDialog(this);
        mValidateFields = new ValidateFields();
        mPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_COORDS, MODE_PRIVATE);
        mBtnEnviarEncuesta.setOnClickListener(this);
        mSwcEncuestaDialog.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopGetCoords();
    }

    @Override
    public void onClick(View v) {
        if(v == mBtnEnviarEncuesta){
            AnswersInterview answersInterview = getAnswersInterview();
            if(mSwcEncuestaDialog.isChecked()){
                InformationComplement informationComplement = getInformationComplement();
                mEncuestaPresenter.validateInterview(answersInterview,informationComplement, mSwcEncuestaDialog.isChecked());
            }else{
                mEncuestaPresenter.validateInterview(answersInterview,null, mSwcEncuestaDialog.isChecked());
            }
        }else if(v == mSwcEncuestaDialog){
            informacionComplementaria();
        }
    }

    @Override
    public void showProgress() {
        mShowMessageDialog.showMessageLoad();
    }

    @Override
    public void hideProgress() {
        mShowMessageDialog.closeMessage();
    }

    @Override
    public void setFieldErrorEmptyInterview() {
        mValidateFields = new ValidateFields();
        mValidateFields.validateText(new EditText[]{
                mTPregunta9R1ColSegura, mTPregunta1R1IndObra,mTPregunta5R1SondAlameda
        });
    }

    @Override
    public void setFieldErrorEmptyComplementInformation() {
        mValidateFields = new ValidateFields();
        mValidateFields.validateText(new EditText[]{
                mTextTelefono, mTextCorreo, mTxtFacebook, mTxtTwitter, mTextEdadAprox
        });
    }

    @Override
    public void finishInterview() {
        stopGetCoords();
        backToMain();
    }

    @Override
    public String getCoordsPerMinute() {
        getCoordsAlarm();
        return null;
    }

    public void informacionComplementaria(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View detallesView = inflater.inflate(R.layout.dialog_informacion_complementaria_encuesta, null);
        findViewsDialog(detallesView);

        final TakePhoto takePhoto = new TakePhoto(this);

        btnAgregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto.getPhoto();
            }
        });
        btnEliminarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgIdentificacion.setImageDrawable(getResources().getDrawable(R.drawable.userphoto));
            }
        });

        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.text_dialog_title))
                .setView(detallesView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSwcEncuestaDialog.setChecked(false);
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm;
        switch(requestCode){
            case Constants.CAMARA_REQUEST:
                if(resultCode == Activity.RESULT_OK){
                    bm = (Bitmap) data.getExtras().get("data");
                    mPath = data.getData();
                    /*try{
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mPath);
                    }catch (Exception e){}*/
                    mImgIdentificacion.setImageBitmap(bm);
                    myBase64Image = ConvertBase64.encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100);
                }
                break;
        }
    }

    private void getCoordsAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        mAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        service.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Constants.INTERVALO_SEGUNDOS * 1000, mAlarmIntent);
    }

    private void stopGetCoords(){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(mAlarmIntent);
    }

    private void backToMain(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void findViews(){
        mTPregunta5R2ColSegura = (EditText) findViewById(R.id.pregunta5_respuesta2_colonia_segura);
        mTPregunta8R2ColSegura  = (EditText) findViewById(R.id.pregunta8_respuesta2_colonia_segura);
        mTPregunta9R1ColSegura = (EditText) findViewById(R.id.pregunta9_respuesta_colonia_segura);
        mRPregunta1R1ColSegura = (RadioButton) findViewById(R.id.pregunta1_respuesta1_colonia_segura);
        mRPregunta1R2ColSegura = (RadioButton) findViewById(R.id.pregunta1_respuesta2_colonia_segura);
        mRPregunta1R3ColSegura = (RadioButton) findViewById(R.id.pregunta1_respuesta3_colonia_segura);
        mRPregunta1R4ColSegura = (RadioButton) findViewById(R.id.pregunta1_respuesta4_colonia_segura);
        mRPregunta4R1ColSegura = (RadioButton) findViewById(R.id.pregunta4_respuesta1_colonia_segura);
        mRPregunta4R2ColSegura = (RadioButton) findViewById(R.id.pregunta4_respuesta2_colonia_segura);
        mRPregunta4R3ColSegura = (RadioButton) findViewById(R.id.pregunta4_respuesta3_colonia_segura);
        mRPregunta4R4ColSegura = (RadioButton) findViewById(R.id.pregunta4_respuesta4_colonia_segura);
        mRPregunta6R1ColSegura = (RadioButton) findViewById(R.id.pregunta6_respuesta1_colonia_segura);
        mRPregunta6R2ColSegura = (RadioButton) findViewById(R.id.pregunta6_respuesta2_colonia_segura);
        mRPregunta6R3ColSegura = (RadioButton) findViewById(R.id.pregunta6_respuesta3_colonia_segura);
        mRPregunta6R4ColSegura = (RadioButton) findViewById(R.id.pregunta6_respuesta4_colonia_segura);
        mCPregunta3R1ColSegura = (CheckBox) findViewById(R.id.pregunta3_respuesta1_colonia_segura);
        mCPregunta3R2ColSegura = (CheckBox) findViewById(R.id.pregunta3_respuesta2_colonia_segura);
        mCPregunta3R3ColSegura = (CheckBox) findViewById(R.id.pregunta3_respuesta3_colonia_segura);
        mCPregunta3R4ColSegura = (CheckBox) findViewById(R.id.pregunta3_respuesta4_colonia_segura);
        mCPregunta7R1ColSegura = (CheckBox) findViewById(R.id.pregunta7_respuesta1_colonia_segura);
        mCPregunta7R2ColSegura = (CheckBox) findViewById(R.id.pregunta7_respuesta2_colonia_segura);
        mCPregunta7R3ColSegura = (CheckBox) findViewById(R.id.pregunta7_respuesta3_colonia_segura);
        mCPregunta7R4ColSegura = (CheckBox) findViewById(R.id.pregunta7_respuesta4_colonia_segura);
        mCPregunta7R5ColSegura = (CheckBox) findViewById(R.id.pregunta7_respuesta5_colonia_segura);
        mSPregunta2R1ColSegura = (Switch) findViewById(R.id.pregunta2_respuesta_colonia_segura);
        mSPregunta5R1ColSegura = (Switch) findViewById(R.id.pregunta5_respuesta1_colonia_segura);
        mSPregunta8R1ColSegura = (Switch) findViewById(R.id.pregunta8_respuesta1_colonia_segura);

        mTPregunta1R1IndObra = (EditText) findViewById(R.id.pregunta1_respuesta1_induccion_obra);
        mTPregunta3R6IndObra = (EditText) findViewById(R.id.pregunta3_respuesta6_induccion_obra);
        mRPregunta1R2IndObra = (RadioButton) findViewById(R.id.pregunta1_respuesta2_induccion_obra);
        mRPregunta1R3IndObra = (RadioButton) findViewById(R.id.pregunta1_respuesta3_induccion_obra);
        mRPregunta1R4IndObra = (RadioButton) findViewById(R.id.pregunta1_respuesta4_induccion_obra);
        mRPregunta1R5IndObra = (RadioButton) findViewById(R.id.pregunta1_respuesta5_induccion_obra);
        mCPregunta2R1IndObra = (CheckBox) findViewById(R.id.pregunta2_respuesta1_induccion_obra);
        mCPregunta2R2IndObra = (CheckBox) findViewById(R.id.pregunta2_respuesta2_induccion_obra);
        mCPregunta2R3IndObra = (CheckBox) findViewById(R.id.pregunta2_respuesta3_induccion_obra);
        mCPregunta2R4IndObra = (CheckBox) findViewById(R.id.pregunta2_respuesta4_induccion_obra);
        mCPregunta3R1IndObra = (CheckBox) findViewById(R.id.pregunta3_respuesta1_induccion_obra);
        mCPregunta3R2IndObra = (CheckBox) findViewById(R.id.pregunta3_respuesta2_induccion_obra);
        mCPregunta3R3IndObra = (CheckBox) findViewById(R.id.pregunta3_respuesta3_induccion_obra);
        mCPregunta3R4IndObra = (CheckBox) findViewById(R.id.pregunta3_respuesta4_induccion_obra);
        mCPregunta3R5IndObra = (CheckBox) findViewById(R.id.pregunta3_respuesta5_induccion_obra);

        mSPregunta1R1LotesBaldios = (Switch) findViewById(R.id.pregunta1_respuesta_lotes_baldios);
        mSPregunta2R1LotesBaldios = (Switch) findViewById(R.id.pregunta2_respuesta_lotes_baldios);
        mSPregunta3R1LotesBaldios = (Switch) findViewById(R.id.pregunta3_respuesta_lotes_baldios);
        mSPregunta4R1LotesBaldios = (Switch) findViewById(R.id.pregunta4_respuesta_lotes_baldios);
        mRPregunta5R1LotesBaldios = (RadioButton) findViewById(R.id.pregunta5_respuesta1_lotes_baldios);
        mRPregunta5R2LotesBaldios = (RadioButton) findViewById(R.id.pregunta5_respuesta2_lotes_baldios);
        mRPregunta5R3LotesBaldios = (RadioButton) findViewById(R.id.pregunta5_respuesta3_lotes_baldios);

        mTPregunta5R1SondAlameda = (EditText) findViewById(R.id.pregunta5_respuesta_sondeo_alameda);
        mSPregunta1R1SondAlameda = (Switch) findViewById(R.id.pregunta1_respuesta_sondeo_alameda);
        mSPregunta2R1SondAlameda = (Switch) findViewById(R.id.pregunta2_respuesta_sondeo_alameda);
        mSPregunta3R1SondAlameda = (Switch) findViewById(R.id.pregunta3_respuesta_sondeo_alameda);
        mSPregunta6R1SondAlameda = (Switch) findViewById(R.id.pregunta6_respuesta_sondeo_alameda);
        mCPregunta4R1SondAlameda = (CheckBox) findViewById(R.id.pregunta4_respuesta1_sondeo_alameda);
        mCPregunta4R2SondAlameda = (CheckBox) findViewById(R.id.pregunta4_respuesta2_sondeo_alameda);
        mCPregunta4R3SondAlameda = (CheckBox) findViewById(R.id.pregunta4_respuesta3_sondeo_alameda);
        mSwcEncuestaDialog = (Switch)findViewById(R.id.pregunta6_respuesta_sondeo_alameda);

        mSpnLugar = (Spinner) findViewById(R.id.spnLugar);
        mTextOtro = (EditText) findViewById(R.id.otro);

        adapter = ArrayAdapter.createFromResource(this, R.array.lugar_entrevista_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnLugar.setAdapter(adapter);

        mBtnEnviarEncuesta = (Button) findViewById(R.id.enviarEncuesta);
    }

    private void findViewsDialog(View detallesView){
        mTextTelefono = (EditText) detallesView.findViewById(R.id.telefono);
        mTextCorreo = (EditText) detallesView.findViewById(R.id.correo);
        mTxtFacebook = (EditText) detallesView.findViewById(R.id.fecebook);
        mTxtTwitter = (EditText) detallesView.findViewById(R.id.twitter);
        mRbHombre = (RadioButton) detallesView.findViewById(R.id.rb_hombre);
        mRbMujer = (RadioButton) detallesView.findViewById(R.id.rb_mujer);
        mTextEdadAprox = (EditText) detallesView.findViewById(R.id.edadAprox);
        mImgIdentificacion = (ImageView) detallesView.findViewById(R.id.imgIdentificacion);
        btnAgregarFoto = (ImageButton) detallesView.findViewById(R.id.btnAgregarFoto);
        btnEliminarFoto = (ImageButton) detallesView.findViewById(R.id.btnBorrarFoto);
    }

    public AnswersInterview getAnswersInterview() {
        AnswersInterview answersInterview = new AnswersInterview();
        String respuestas_radio = "", respuesta_checkbox = "";
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_LOGIN, MODE_PRIVATE);
        String id = preferences.getString(Constants.SHARED_PREFERENCES_LOGIN_ID_FLAG, Constants.SHARED_PREFERENCES_LOGIN_ID_FLAG);
        answersInterview.setsIdUsuario(id);
        //colonia segura
        //pregunta1
        if(mRPregunta1R1ColSegura.isChecked()){
            respuestas_radio = mRPregunta1R1ColSegura.getText().toString();
        }else if(mRPregunta1R2ColSegura.isChecked()){
            respuestas_radio = mRPregunta1R2ColSegura.getText().toString();
        }else if(mRPregunta1R3ColSegura.isChecked()){
            respuestas_radio = mRPregunta1R3ColSegura.getText().toString();
        }else if(mRPregunta1R4ColSegura.isChecked()){
            respuestas_radio = mRPregunta1R4ColSegura.getText().toString();
        }
        answersInterview.setsOpinionPolicia(respuestas_radio);
        //pregunta2
        if(mSPregunta2R1ColSegura.isChecked()){
            answersInterview.setsPatrullaje("1");
        }else{
            answersInterview.setsPatrullaje("0");
        }
        //pregunta3
        if(mCPregunta3R1ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta3R1ColSegura.getText().toString()+",";
        }
        if(mCPregunta3R2ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta3R2ColSegura.getText().toString()+",";
        }
        if(mCPregunta3R3ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta3R3ColSegura.getText().toString()+",";
        }
        if(mCPregunta3R4ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta3R4ColSegura.getText().toString();
        }
        answersInterview.setsHorarioPatrullaje(respuesta_checkbox);
        //pregunta 4
        if(mRPregunta4R1ColSegura.isChecked()){
            respuestas_radio = mRPregunta4R1ColSegura.getText().toString();
        }else if(mRPregunta4R2ColSegura.isChecked()){
            respuestas_radio = mRPregunta4R2ColSegura.getText().toString();
        }else if(mRPregunta4R3ColSegura.isChecked()){
            respuestas_radio = mRPregunta4R3ColSegura.getText().toString();
        }else if(mRPregunta4R4ColSegura.isChecked()){
            respuestas_radio = mRPregunta4R4ColSegura.getText().toString();
        }
        answersInterview.setsAccionesMunicipio(respuestas_radio);
        //pregunta5
        if(mSPregunta5R1ColSegura.isChecked()){
            answersInterview.setsTramitePendiente("1");
        }else{
            answersInterview.setsTramitePendiente("0");
        }
        answersInterview.setsTramitePendienteComentario(mTPregunta5R2ColSegura.getText().toString());
        //pregunta6
        if(mRPregunta6R1ColSegura.isChecked()){
            respuestas_radio = mRPregunta6R1ColSegura.getText().toString();
        }else if(mRPregunta6R2ColSegura.isChecked()){
            respuestas_radio = mRPregunta6R2ColSegura.getText().toString();
        }else if(mRPregunta6R3ColSegura.isChecked()){
            respuestas_radio = mRPregunta6R3ColSegura.getText().toString();
        }else if(mRPregunta6R4ColSegura.isChecked()){
            respuestas_radio = mRPregunta6R4ColSegura.getText().toString();
        }
        answersInterview.setsGobiernoMarcosAguilar(respuestas_radio);
        //pregunta7
        respuesta_checkbox = "";
        if(mCPregunta7R1ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta7R1ColSegura.getText().toString()+",";
        }
        if(mCPregunta7R2ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta7R2ColSegura.getText().toString()+",";
        }
        if(mCPregunta7R3ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta7R3ColSegura.getText().toString()+",";
        }
        if(mCPregunta7R4ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta7R4ColSegura.getText().toString()+",";
        }
        if(mCPregunta7R5ColSegura.isChecked()){
            respuesta_checkbox += mCPregunta7R5ColSegura.getText().toString();
        }
        answersInterview.setsEsperaMarcosAguilar(respuesta_checkbox);
        //pregunta8
        if(mSPregunta8R1ColSegura.isChecked()){
            answersInterview.setsPracticaDeportes("1");
        }else{
            answersInterview.setsPracticaDeportes("0");
        }
        answersInterview.setsDeporte(mTPregunta8R2ColSegura.getText().toString());
        //pregunta9
        answersInterview.setsNecesidadColonia(mTPregunta9R1ColSegura.getText().toString());

        //Induccion obra
        //pregunta1
        answersInterview.setoOpinionRealizarObraNombre(mTPregunta1R1IndObra.getText().toString());
        if(mRPregunta1R2IndObra.isChecked()){
            respuestas_radio = mRPregunta1R2IndObra.getText().toString();
        }else if(mRPregunta1R3IndObra.isChecked()){
            respuestas_radio = mRPregunta1R3IndObra.getText().toString();
        }else if(mRPregunta1R4IndObra.isChecked()){
            respuestas_radio = mRPregunta1R4IndObra.getText().toString();
        }else if(mRPregunta1R5IndObra.isChecked()){
            respuestas_radio = mRPregunta1R5IndObra.getText().toString();
        }
        answersInterview.setoOpinionRealizaObra(respuestas_radio);
        //pregunta2
        respuesta_checkbox = "";
        if(mCPregunta2R1IndObra.isChecked()){
            respuesta_checkbox += mCPregunta2R1IndObra.getText().toString()+",";
        }
        if(mCPregunta2R2IndObra.isChecked()){
            respuesta_checkbox += mCPregunta2R2IndObra.getText().toString()+",";
        }
        if(mCPregunta2R3IndObra.isChecked()){
            respuesta_checkbox += mCPregunta2R3IndObra.getText().toString()+",";
        }
        if(mCPregunta2R4IndObra.isChecked()){
            respuesta_checkbox += mCPregunta2R4IndObra.getText().toString();
        }
        answersInterview.setoHorarioMayorRiesgo(respuesta_checkbox);
        //pregunta3
        respuesta_checkbox = "";
        if(mCPregunta3R1IndObra.isChecked()){
            respuesta_checkbox += mCPregunta3R1IndObra.getText().toString()+",";
        }
        if(mCPregunta3R2IndObra.isChecked()){
            respuesta_checkbox += mCPregunta3R2IndObra.getText().toString()+",";
        }
        if(mCPregunta3R3IndObra.isChecked()){
            respuesta_checkbox += mCPregunta3R3IndObra.getText().toString()+",";
        }
        if(mCPregunta3R4IndObra.isChecked()){
            respuesta_checkbox += mCPregunta3R4IndObra.getText().toString()+",";
        }
        if(mCPregunta3R5IndObra.isChecked()){
            respuesta_checkbox += mTPregunta3R6IndObra.getText().toString();
        }
        answersInterview.setoRiesgosFraccionamiento(respuesta_checkbox);

        //Lotes baldios
        //pregunta1
        if(mSPregunta1R1LotesBaldios.isChecked()){
            answersInterview.setbHayLotesBaldios("1");
        }else{
            answersInterview.setbHayLotesBaldios("0");
        }
        //pregunta2
        if(mSPregunta2R1LotesBaldios.isChecked()){
            answersInterview.setbConsideraRiesgos("1");
        }else{
            answersInterview.setbConsideraRiesgos("0");
        }
        //pregunta3
        if(mSPregunta3R1LotesBaldios.isChecked()){
            answersInterview.setbDebenAtenderse("1");
        }else{
            answersInterview.setbDebenAtenderse("0");
        }
        //pregunta4
        if(mSPregunta4R1LotesBaldios.isChecked()){
            answersInterview.setbRealizadoLimpieza("1");
        }else{
            answersInterview.setbRealizadoLimpieza("0");
        }
        //pregunta5
        if(mRPregunta5R1LotesBaldios.isChecked()){
            respuestas_radio = mRPregunta5R1LotesBaldios.getText().toString();
        }else if(mRPregunta5R2LotesBaldios.isChecked()){
            respuestas_radio = mRPregunta5R2LotesBaldios.getText().toString();
        }else if(mRPregunta5R3LotesBaldios.isChecked()){
            respuestas_radio = mRPregunta5R3LotesBaldios.getText().toString();
        }
        answersInterview.setbDesmalezadoLotes(respuestas_radio);
        //SondeoAlameda
        //pregunta1
        if(mSPregunta1R1SondAlameda.isChecked()){
            answersInterview.setaAparienciaAlameda("1");
        }else{
            answersInterview.setaAparienciaAlameda("0");
        }
        //pregunta2
        if(mSPregunta2R1SondAlameda.isChecked()){
            answersInterview.setaSeguroTransitar("1");
        }else{
            answersInterview.setaSeguroTransitar("0");
        }
        //pregunta3
        if(mSPregunta3R1SondAlameda.isChecked()){
            answersInterview.setaAlamedaRecreo("1");
        }else{
            answersInterview.setaAlamedaRecreo("0");
        }
        //pregunta4
        respuesta_checkbox = "";
        if(mCPregunta4R1SondAlameda.isChecked()){
            respuesta_checkbox += mCPregunta4R1SondAlameda.getText().toString()+",";
        }
        if(mCPregunta4R2SondAlameda.isChecked()){
            respuesta_checkbox += mCPregunta4R2SondAlameda.getText().toString()+",";
        }
        if(mCPregunta4R3SondAlameda.isChecked()){
            respuesta_checkbox += mCPregunta4R3SondAlameda.getText().toString();
        }
        answersInterview.setaProblemaAlameda(respuesta_checkbox);
        //pregunta5
        answersInterview.setaMejorarImagen(mTPregunta5R1SondAlameda.getText().toString());

        if(mSpnLugar.getSelectedItem().toString().equals("Otro")){
            answersInterview.setaLugarEncuesta(mTextOtro.getText().toString());
        }else{
            answersInterview.setaLugarEncuesta(mSpnLugar.getSelectedItem().toString());
        }
        answersInterview.setStatus("0");
        GpsLocation gpsLocation = new GpsLocation(this);
        String[] coords = gpsLocation.getLocationGPS();
        answersInterview.setLatitud(coords[0]);
        answersInterview.setLongitud(coords[1]);

        return answersInterview;
    }

    public InformationComplement getInformationComplement() {
        InformationComplement informationComplement = new InformationComplement();
        if(mTextTelefono.getText().toString().equals("")) {
            informationComplement.setTelefono("");
        }else{
            informationComplement.setTelefono(mTextTelefono.getText().toString());
        }
        if(mTextCorreo.getText().toString().equals("")) {
            informationComplement.setEmail("");
        }else{
            informationComplement.setEmail(mTextCorreo.getText().toString());
        }
        if(mTxtFacebook.getText().toString().equals("")) {
            informationComplement.setFacebook("");
        }else{
            informationComplement.setFacebook(mTxtFacebook.getText().toString());
        }
        if(mTxtTwitter.getText().toString().equals("")) {
            informationComplement.setTwitter("");
        }else{
            informationComplement.setTwitter(mTxtTwitter.getText().toString());
        }
        if(mRbHombre.isChecked()) {
            informationComplement.setGenero("Hombre");
        }else{
            informationComplement.setGenero("Mujer");
        }
        if(mTextEdadAprox.getText().toString().equals("")) {
            informationComplement.setEdad(0);
        }else{
            informationComplement.setEdad(Integer.parseInt(mTextEdadAprox.getText().toString()));
        }
        if(mPath != null){
            informationComplement.setFoto(myBase64Image);
        }

        return informationComplement;
    }
}
