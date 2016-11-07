package com.gob.pgutierrezd.e_personas.views;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gob.pgutierrezd.e_personas.R;
import com.gob.pgutierrezd.e_personas.httpconn.HttpManager;
import com.gob.pgutierrezd.e_personas.httpconn.RequestPackage;
import com.gob.pgutierrezd.e_personas.sqlite.DataSource;
import com.gob.pgutierrezd.e_personas.utils.Constants;
import com.gob.pgutierrezd.e_personas.utils.ShowMessageDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistorialActivity extends AppCompatActivity {

    private static final int ENVIAR_ID = Menu.FIRST+1;
    private static final int BORRAR = Menu.FIRST+2;

    private ListView listaEncuestas;
    private ListAdapter adapter;
    private Cursor cursor;
    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        findViews();

        try{
            dataSource = new DataSource(this);
        }catch (SQLException e){
            Log.d("LOGTAG","Error en la base de datos " + e);
        }
        cargarCursor();

        registerForContextMenu(listaEncuestas);
    }

    public void cargarCursor(){
        try{
            cursor = dataSource.getEncuestas();
            adapter = new SimpleCursorAdapter(this, R.layout.row,cursor, new String[]{"_id",Constants.FECHA, Constants.STATUS},new int[]{R.id.idE,R.id.fechaEncuesta,R.id.status},0);
            listaEncuestas.setAdapter(adapter);
        }catch (Exception e){
            Log.d("LOGTAG","Ocurrio un error en el cursor" + e);
        }
    }

    private void findViews(){
        listaEncuestas = (ListView)findViewById(R.id.listaEncuestas);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        menu.add(Menu.NONE,ENVIAR_ID,Menu.NONE,"Enviar");
        menu.add(Menu.NONE,BORRAR,Menu.NONE,"Borrar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent;
        switch(item.getItemId()){
            case ENVIAR_ID:
                try {
                    AdapterView.AdapterContextMenuInfo info1 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    String json = cur2Json(String.valueOf(info1.id));
                    String jsonInfo = jsonInfo(String.valueOf(info1.id));
                    String jsonCoordenadas = jsonCoordenadas(String.valueOf(info1.id));
                    enviarCoordenadas(jsonCoordenadas);
                    enviarEncuesta(json, jsonInfo);
                }catch (Exception e){
                    Log.d("LOGTAG","Error en la opcion ENVIAR_ID " + e);
                }
                break;
            case BORRAR:
                try{
                    AdapterView.AdapterContextMenuInfo info1 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    dataSource.deleteRegistro(Constants.TABLE_ENCUESTAS,String.valueOf(info1.id));
                    /*intent = new Intent();
                    intent.setClass(getApplicationContext(),HistorialActivity.class);*/
                    cargarCursor();
                    Toast.makeText(getApplicationContext(),"El registro se borro exitosamente",Toast.LENGTH_SHORT);
                    Log.d("LOGTAG","El registro se borro exitosamente ");
                    //startActivity(intent);
                }catch (Exception e){
                    Log.d("LOGTAG","Error en al borrar la encuesta " + e);
                }
                break;
        }
        return true;
    }

    public void enviarEncuesta(String json, String jsonInfo){
        String url = getResources().getString(R.string.url);
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setUri(url+"setEncuestas.php");
        requestPackage.setMethod("POST");
        requestPackage.setParams("json", "{\"dto\":{\"Encuesta\":"+json+",\"InformacionComplementaria\":"+jsonInfo+"}}");
        Log.d("LOGTAG","JSON antes de enviar" + "{\"dto\":{\"Encuesta\":"+json+",\"InformacionComplementaria\":"+jsonInfo+"}}");
        EnviarTask enviarTask =  new EnviarTask(requestPackage,getApplicationContext());
        enviarTask.execute(requestPackage);
    }

    public void enviarCoordenadas(String json){
        String url = getResources().getString(R.string.url);
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setUri(url+"setCoordenadas.php");
        requestPackage.setMethod("POST");
        requestPackage.setParams("json", "{\"coordenadas\":"+json +"}");
        Log.d("LOGTAG","JSON de Coordenadas antes de enviar " + "{\"coordenadas\":"+json +"}");
        EnviarCoordenadasTask enviarTask =  new EnviarCoordenadasTask(requestPackage,getApplicationContext());
        enviarTask.execute(requestPackage);
    }
    /**
     * Metodo para crear el json de las encuestas
     * @param id recibe el id de la encuesta
     * @return regresa una cadena con el json
     */
    public String cur2Json(String id) {
        Log.d("LOGTAG", "ID " + id);
        Cursor cursorID = dataSource.getEncuestassById(id);
        JSONObject resultSet = new JSONObject();
        String jsonString = null;
        try {
            //cursor.moveToFirst();
            while (cursorID.isAfterLast() == false) {
                int totalColumn = cursorID.getColumnCount();
                JSONObject rowObject = new JSONObject();
                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            rowObject.put(cursorID.getColumnName(i),
                                    cursorID.getString(i));
                        } catch (Exception e) {
                            Log.d("LOGTAG","Error al buscar en cursor" + e.getMessage());
                        }
                    }
                }
                if (rowObject.length()!=0){
                    resultSet = rowObject;
                }
                cursorID.moveToNext();
            }
            Log.d("LOGTAG","JSON:" + resultSet.toString());
            jsonString = resultSet.toString();
        }catch (Exception e){
            Log.d("LOGTAG","ERROR ALA CONVERTIR A JSON " +e);
        }
        return jsonString;
    }

    /**
     * Metodo para crear el json de las encuestas
     * @param id recibe el id de la encuesta
     * @return regresa una cadena con el json
     */
    public String jsonInfo(String id) {
        Log.d("LOGTAG", "ID " + id);
        Cursor cursorID = dataSource.getInformacionComplementariaById(id);
        JSONObject resultSet = new JSONObject();
        String jsonString = null;
        try {
            //cursor.moveToFirst();
            while (cursorID.isAfterLast() == false) {
                int totalColumn = cursorID.getColumnCount();
                JSONObject rowObject = new JSONObject();
                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            rowObject.put(cursorID.getColumnName(i),
                                    cursorID.getString(i));
                        } catch (Exception e) {
                            Log.d("LOGTAG","Error al buscar en cursor" + e.getMessage());
                        }
                    }
                }
                if (rowObject.length()!=0){
                    resultSet = rowObject;
                }
                cursorID.moveToNext();
            }
            Log.d("LOGTAG","JSON:" + resultSet.toString());
            jsonString = resultSet.toString();
        }catch (Exception e){
            Log.d("LOGTAG","ERROR ALA CONVERTIR A JSON " +e);
        }
        return jsonString;
    }

    /**
     * Metodo para crear el json de las encuestas
     * @param id recibe el id de la encuesta
     * @return regresa una cadena con el json
     */
    public String jsonCoordenadas(String id) {
        Log.d("LOGTAG", "ID " + id);
        Cursor cursorID = dataSource.getCoordenadas(id);
        JSONObject resultSet = new JSONObject();
        String jsonString = null;
        try {
            //cursor.moveToFirst();
            while (cursorID.isAfterLast() == false) {
                int totalColumn = cursorID.getColumnCount();
                JSONObject rowObject = new JSONObject();
                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            rowObject.put(cursorID.getColumnName(i),
                                    cursorID.getString(i));
                        } catch (Exception e) {
                            Log.d("LOGTAG","Error al buscar en cursor" + e.getMessage());
                        }
                    }
                }
                if (rowObject.length()!=0){
                    resultSet = rowObject;
                }
                cursorID.moveToNext();
            }
            Log.d("LOGTAG","JSON:" + resultSet.toString());
            jsonString = resultSet.toString();
        }catch (Exception e){
            Log.d("LOGTAG","ERROR ALA CONVERTIR A JSON " +e);
        }
        return jsonString;
    }

    public class EnviarTask extends AsyncTask<RequestPackage,String,String> {

        private ShowMessageDialog showMessageDialog;
        RequestPackage request;
        private Context context;

        public EnviarTask(RequestPackage request, Context context){
            this.showMessageDialog = new ShowMessageDialog(context);
            this.request = request;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            int aux = 0;
            String content = "";
            do{
                content = HttpManager.getData(params[0]);
                if(!content.equals("")){
                    aux = 2;
                }else{
                    aux++;
                }
            }while(aux != 2);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(!s.equals("")) {
                    JSONObject parent = new JSONObject(s);
                    Log.d("LOGTAG","Trama de respuesta " + parent.toString());
                    if (parent.length() > 1) {
                        JSONObject child1 = new JSONObject(parent.getString("error"));
                        if(child1.getString("clave").equals("OK")){
                            if (parent.getInt("status") == 1) {
                                ContentValues cv = new ContentValues();
                                cv.put(Constants.STATUS, 1);
                                dataSource.Update(Constants.TABLE_ENCUESTAS,cv," "+Constants.REFERENCIA_MOVIL+" = ?",parent.getString("referencia_movil"));
                                Toast.makeText(context,"Envio exitoso",Toast.LENGTH_LONG).show();
                                /*Intent intent = new Intent();
                                intent.setClass(context,HistorialActivity.class);
                                startActivity(intent);*/
                                cargarCursor();
                            }else{
                                Toast.makeText(context,"Error al enviar intente de nuevo",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            //showMessageDialog.showMessageInfo("Error", child1.getString("mensaje"));
                            Log.d("LOGTAG",child1.getString("mensaje"));
                        }
                    }
                }else{
                    showMessageDialog.showMessageInfo("Error", "No se ha podido establecer conexión");
                }
            } catch (JSONException e) {
                Log.d("LOGTAG", "Error al enviar encuesta" + e);
                e.printStackTrace();
            }
        }
    }
    //public class EnviarTask extends AsyncTask<RequestPackage,String,String> {
    public  class EnviarCoordenadasTask extends AsyncTask<RequestPackage,String,String> {

        private ShowMessageDialog showMessageDialog;
        RequestPackage request;
        private Context context;

        public EnviarCoordenadasTask(RequestPackage request, Context context){
            this.showMessageDialog = new ShowMessageDialog(context);
            this.request = request;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            int aux = 0;
            String content = "";
            do{
                content = HttpManager.getData(params[0]);
                if(!content.equals("")){
                    aux = 2;
                }else{
                    aux++;
                }
            }while(aux != 2);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(!s.equals("")) {
                    JSONObject parent = new JSONObject(s);
                    Log.d("LOGTAG","Trama de respuesta " + parent.toString());
                    if (parent.length() > 1) {
                        JSONObject child1 = new JSONObject(parent.getString("error"));
                        if(child1.getString("clave").equals("OK")){

                        }else{
                            //showMessageDialog.showMessageInfo("Error", child1.getString("mensaje"));
                            Log.d("LOGTAG",child1.getString("mensaje"));
                        }
                    }
                }else{
                    showMessageDialog.showMessageInfo("Error", "No se ha podido establecer conexión");
                }
            } catch (JSONException e) {
                Log.d("LOGTAG", "Error al enviar encuesta" + e);
                e.printStackTrace();
            }
        }

    }
}
