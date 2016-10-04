package com.coltrack.codigopolicia;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Medidas extends AppCompatActivity {
    ImageButton imageButton_descargarCodigo;
    String IP="http://192.168.1.61";
    String obtnerCodigo=IP+"/codigoPolicia/login/geter";
    String LOG="nelson";
    boolean errorServidor=false;
    //String[] articulos={"nelson javier rodriguez","andres marquez rodriguez","julian caraballo rodriguez adriana","juan","marcos"};
    AutoCompleteTextView autotext;
    SQLiteDatabase myDB;
    Cursor c;
    int i=0;
    String leido=null;
    String[] articulos=new String[600];
    ListView listView;
    List<Row> rows;
    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    EditText editText;
    TextView Descargar;
    String articulo;
    String nombre;
    String numeroCedula;
    String fechaNacimiento;
    String sexo;
    String tipoSangre;
    String direccion;
    String medida;

    MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidas);
        imageButton_descargarCodigo=(ImageButton)findViewById(R.id.imageButton_descargarCodigo);
        listView = (ListView) findViewById(R.id.listView);
        editText=(EditText)findViewById(R.id.txtsearch);
        Descargar=(TextView)findViewById(R.id.textView33);

        mPlayer = MediaPlayer.create(Medidas.this, R.raw.beep);

        Bundle bundle=getIntent().getExtras();
        nombre=bundle.getString("nombre").toString();
        numeroCedula=bundle.getString("numeroCedula").toString();
        fechaNacimiento=bundle.getString("fechaNacimiento").toString();
        sexo=bundle.getString("sexo").toString();
        tipoSangre=bundle.getString("tipoSangre").toString();
        direccion=bundle.getString("direccion").toString();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Buscar Comportamiento");

//        imageButton_atras.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.start();
//                //Intent i=new Intent(Medidas.this,Resultado.class);
//                //startActivity(i);
//                finish();
//            }
//        });
//
//        imageButton_adelante.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.start();
//                Toast.makeText(getApplicationContext(),"Ingrese texto de busqueda y/o seleccione un articulo!!",Toast.LENGTH_SHORT).show();
//            }
//        });


        Log.i(LOG, "En activity medidas");
        for (i=0;i<articulos.length;i++) {
            articulos[i] = "";
        }
        imageButton_descargarCodigo.setVisibility(View.INVISIBLE);
        Descargar.setVisibility(View.INVISIBLE);

        myDB = openOrCreateDatabase("codigoPolicia", MODE_PRIVATE, null);
        //myDB.execSQL("DROP TABLE IF EXISTS codigo");//borramos tabla
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + "codigo"
                + " (diccionario TEXT, capitulo TEXT, articulo TEXT, numeral TEXT,descripcion1 TEXT, descripcion2 TEXT, conducta TEXT, medida TEXT);");


        c = myDB.rawQuery("SELECT * FROM codigo", null);
        c.moveToFirst();
        Log.i(LOG,"Filas a leer: "+c.getCount());
        i=0;
        if (c.getCount()>0){
            if (c != null && c.getCount()>0) {
                do {
                    leido=c.getString(c.getColumnIndex("diccionario"));
                    //if (i<100){
                        if (i>0){
                            articulo=c.getString(c.getColumnIndex("articulo"));
                            //if (!articulo.equals("Artículo")){
                            Log.i(LOG,"Creadno en ram:"+leido);
                                articulos[i]=leido;
                            //}

                        }
                        i++;

                }while(c.moveToNext());
            }
        }
        c.close();
        initList();
        // Register a callback to be invoked when an item in this AdapterView
// has been clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg) {

                // Sets the visibility of the indeterminate progress bar in the
                // title
                mPlayer.start();
                setProgressBarIndeterminateVisibility(true);
                Log.i(LOG, "Presiono en posicion: " + position);
                String diccionario =(String) adapter.getItemAtPosition(position);
                //Obtenemos valor que se presiona
                Log.i(LOG,"Value is "+ diccionario);
                myDB = openOrCreateDatabase("codigoPolicia", MODE_PRIVATE, null);
                c = myDB.rawQuery("SELECT * FROM codigo WHERE Diccionario='" + diccionario + "';", null);
                c.moveToFirst();
                i=0;



                Log.i(LOG,"c leng: "+c.getCount());
                if (c.getCount()>0){
                    if (c != null && c.getCount()>0) {
                        do {
                            articulo=c.getString(c.getColumnIndex("articulo"));
                            medida=c.getString(c.getColumnIndex("medida"));
                            Log.i(LOG,"Articulo: "+articulo);
                        }while(c.moveToNext());
                    }
                }
                //Abrimos siguiente actividad:
                Intent i=new Intent(getApplicationContext(),Medidas2.class);
                i.putExtra("nombre",nombre);
                i.putExtra("numeroCedula",numeroCedula);
                i.putExtra("fechaNacimiento",fechaNacimiento);
                i.putExtra("sexo",sexo);
                i.putExtra("tipoSangre",tipoSangre);
                i.putExtra("direccion",direccion);
                i.putExtra("diccionario",diccionario);
                i.putExtra("medida",medida);

                startActivity(i);









                c.close();
                myDB.close();



                // Show progress dialog
//            progressDialog = ProgressDialog.show(Medidas.this, "ProgressDialog", "Loading!");
//
//            // Tells JavaScript to open windows automatically.
//            webView.getSettings().setJavaScriptEnabled(true);
//
//            // Sets our custom WebViewClient.
//            webView.setWebViewClient(new myWebClient());
//
//            // Loads the given URL
//            Item item = (Item) listView.getAdapter().getItem(position);
//            webView.loadUrl(item.getUrl());
            }
        });



        editText.addTextChangedListener(new TextWatcher() {

            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {


            }


            @Override

            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
                initList();
                if (s.toString().equals("")) {

                    // reset listview

                    initList();

                } else {

                    // perform search

                    searchItem(s.toString());

                }

            }


            @Override

            public void afterTextChanged(Editable s) {


            }

        });


        imageButton_descargarCodigo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view){
                Log.i(LOG, "Oprimio descargar codigo!!");
                ObtenerWebService envia = new ObtenerWebService();
                //Enviamos el telefono, el servidor debe responder con ack o fail
                envia.execute(obtnerCodigo);

            }
            }

        );

        }
    public void searchItem(String textToSearch){
        textToSearch=Normalizer.normalize(textToSearch.toLowerCase(),Normalizer.Form.NFD);
        textToSearch=textToSearch.replaceAll("[^\\p{ASCII}]", "");
        Log.i(LOG,"textToSearch: "+textToSearch);
        String temp;
        for (int i=0;i<articulos.length;i++){
            temp=Normalizer.normalize(articulos[i].toLowerCase(),Normalizer.Form.NFD);
            temp=temp.replaceAll("[^\\p{ASCII}]", "");
            if (!temp.contains(textToSearch)){
                listItems.remove(articulos[i]);
            }
        }
//        for(String item:articulos){
//            item=item.toLowerCase();
//            Log.i(LOG,"Item lower: "+item);
//
//
//            //if(!item.toLowerCase().replaceAll("[^a-z ]","").contains(textToSearch.toLowerCase().replaceAll("[^a-z ]",""))){
//            if(!Normalizer.normalize(item,Normalizer.Form.NFD).contains(textToSearch)){
//                listItems.remove(item);
//
//
//
//            }
//
//        }

        adapter.notifyDataSetChanged();

    }

    private void initList() {
        //items=new String[]{"Canada","China","Japan","USA"};
        listItems=new ArrayList<>(Arrays.asList(articulos));
        adapter=new ArrayAdapter<String>(this,
                R.layout.list_item3, R.id.txtitem, listItems);
        listView.setAdapter(adapter);
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
                    String diccionario= (String) object3.get("Diccionario");
                    String capitulo= (String) object3.get("Capitulo");
                    String articulo= (String) object3.get("Articulo");
                    String numeral= (String) object3.get("Numeral");
                    String descripcion1= (String) object3.get("Descripcion_1");
                    String descripcion2= (String) object3.get("Descripcion_2");
                    String conducta= (String) object3.get("Conducta");
                    String medida= (String) object3.get("Medida");

                    Log.i(LOG, "Ide: " + diccionario);
                    //(diccionario TEXT, capitulo TEXT, articulo TEXT, numeral TEXT,descripcion1 TEXT, descripcion2 TEXT, conducta TEXT, medida TEXT,);");
                    //(diccionario TEXT, capitulo TEXT, articulo TEXT, numeral TEXT,descripcion1 TEXT, descripcion2 TEXT, conducta TEXT, medida TEXT);");
                    myDB.execSQL("INSERT INTO "
                            + "codigo"
                            + " (diccionario,capitulo,articulo,numeral,descripcion1,descripcion2,conducta,medida)"
                            +" VALUES('"+diccionario+"',"
                            +"'"+capitulo+"',"
                            +"'"+articulo+"',"
                            +"'"+numeral+"',"
                            +"'"+descripcion1+"',"
                            +"'"+descripcion2+"',"
                            +"'"+conducta+"',"
                            +"'"+medida+"');");

                }
                myDB.close();




            } catch (IOException e) {
                e.printStackTrace();
                Log.i(LOG, "Error1: " + e.toString());
                errorServidor=true;

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
            if (errorServidor){
                errorServidor=false;
                Toast.makeText(getApplicationContext(),"Error conectando a servidor!!!",Toast.LENGTH_SHORT).show();
            }


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
