package com.coltrack.codigopolicia;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Medidas extends AppCompatActivity {
    ImageButton imageButton_descargarCodigo;
    String IP="http://192.168.0.12";
    String obtnerCodigo=IP+"/codigoPolicia/login/geter";
    String LOG="nelson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidas);
        imageButton_descargarCodigo=(ImageButton)findViewById(R.id.imageButton_descargarCodigo);
        Log.i(LOG,"En activity medidas");

        imageButton_descargarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG,"Oprimio descargar codigo!!");
                ObtenerWebService envia=new ObtenerWebService();
                //Enviamos el telefono, el servidor debe responder con ack o fail
                envia.execute(obtnerCodigo);

            }
        });

    }

    public class ObtenerWebService extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.i(LOG,"En doInBackground");
            HttpClient client = new DefaultHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            ;

            HttpPost httpPost  = new HttpPost(strings[0]);
            //httpPost.setEntity(new UrlEncodedFormEntity(params));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                params.add(new BasicNameValuePair("telefono","hola"));
                HttpResponse httpResponse = client.execute(httpPost);
                String jsonResult = inputStreamToString(httpResponse.getEntity().getContent()).toString();
                JSONObject respuestaJSON = new JSONObject(jsonResult.toString());
                Log.i(LOG,"Id: "+respuestaJSON.toString());
                //Nodo Login:
                JSONArray respLogin=respuestaJSON.getJSONArray("codigo");
                Log.i(LOG,"Ideeee: "+respLogin.toString());
                for (int i=0;i<respLogin.length();i++){
                    JSONObject object3 = respLogin.getJSONObject(i);
                    Log.i(LOG,"Ide: "+object3.get("id")+"\t"+object3.get("Libro")+"\t"+object3.get("Diccionario"));
                }




            } catch (IOException e) {
                e.printStackTrace();
                Log.i(LOG,"Error1: "+e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(LOG,"Error2: "+e.toString());
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
    private StringBuilder inputStreamToString(InputStream is){
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try
        {
            while ((rLine = rd.readLine()) != null)
            {
                answer.append(rLine);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return answer;
    }
}
