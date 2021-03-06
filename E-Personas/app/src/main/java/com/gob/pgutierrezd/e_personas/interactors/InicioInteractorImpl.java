package com.gob.pgutierrezd.e_personas.interactors;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.gob.pgutierrezd.e_personas.R;
import com.gob.pgutierrezd.e_personas.httpconn.HttpManager;
import com.gob.pgutierrezd.e_personas.httpconn.RequestPackage;
import com.gob.pgutierrezd.e_personas.interfaces.inicio.InicioInteractor;
import com.gob.pgutierrezd.e_personas.models.LoginRegister;
import com.gob.pgutierrezd.e_personas.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgutierrezd on 26/10/2016.
 */
public class InicioInteractorImpl implements InicioInteractor{

    private LoginRegister mLoginRegister;

    @Override
    public void login(LoginResult loginResult, final Context context, final OnLoginFinishedListener listener) {
        if(loginResult != null){
            // Facebook Email address
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object,GraphResponse response) {
                    response.getJSONObject().toString();
                    try {
                        String[] completeName = object.getString("name").split(" ");
                        mLoginRegister = new LoginRegister();
                        mLoginRegister.setmNombre(completeName[0]);
                        mLoginRegister.setmApellidos(lastName(completeName));
                        mLoginRegister.setmTelefono("");
                        mLoginRegister.setmFechaNacimiento("");
                        mLoginRegister.setmCorreo(object.getString("email"));
                        mLoginRegister.setmPassword("");
                        mLoginRegister.setmGenero(object.getString("gender"));
                        Log.d("AA", completeName[0] + lastName(completeName) + "," + object.getString("email") + "," + object.getString("gender"));
                        requestDataLogin(context.getResources().getString(R.string.url), mLoginRegister, listener, context,object.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,gender,email");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void requestDataLogin(String url, LoginRegister user, OnLoginFinishedListener listener, Context context, String email){
        RequestPackage requestPackage = new RequestPackage();
        String peticion = "{\"nombre\":\""+user.getmNombre()+"\","
                        +"\"apellidos\":\""+user.getmApellidos()+"\","
                        +"\"telefono\":\""+user.getmTelefono()+"\","
                        +"\"fechaNacimiento\":\""+user.getmFechaNacimiento()+"\","
                        +"\"correo\":\""+user.getmCorreo()+"\","
                        +"\"password\":\""+user.getmPassword()+"\","
                        +"\"sexo\":\""+user.getmGenero()+"\","
                        +"\"facebook\":\"true\"}";
        requestPackage.setUri(url+"login.php");
        requestPackage.setMethod("POST");
        requestPackage.setParams("json", peticion);
        LoginTask loginTask = new LoginTask(listener, context,email);
        loginTask.execute(requestPackage);
    }

    private class LoginTask extends AsyncTask<RequestPackage, String, String> {

        private OnLoginFinishedListener listener;
        private Context context;
        private String email;

        public LoginTask(OnLoginFinishedListener listener, Context context,String email){
            this.listener = listener;
            this.context = context;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            while (content.equals("")){
                content = HttpManager.getData(params[0]);
            }
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(!s.equals("")) {
                    JSONObject parent = new JSONObject(s);
                    if (parent.length() > 1) {
                        JSONObject child1 = new JSONObject(parent.getString("error"));
                        if(child1.getString("clave").equals("OK")){
                            SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_LOGIN, context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Constants.SHARED_PREFERENCES_LOGIN_ID_FLAG, parent.getString("idUsuario"));
                            editor.putString(Constants.SHARED_PREFERENCES_LOGIN_EMAIL_FLAG, this.email);
                            editor.commit();
                            Toast.makeText(context, child1.getString("mensaje"), Toast.LENGTH_LONG).show();
                            listener.onSuccess();
                        }else{
                            listener.onFail();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    private String lastName(String[] lastName){
        String newLastName = "";
        for (int i = 1; i < lastName.length; i++){
            newLastName+= " " + lastName[i];
        }
        return newLastName;
    }

}
