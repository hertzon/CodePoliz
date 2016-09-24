package com.coltrack.codigopolicia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class Resultado extends AppCompatActivity {
    String LOG="nelson";
    String numeroCedula=null;
    String nombre=null;
    String sexo=null;
    String fechaNacimiento=null;
    String tipoSangre=null;
    String direccion=null;
    TextView textView_resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        textView_resultado=(TextView)findViewById(R.id.textView_resultado);


        Log.i(LOG,"\n\n\n\n\nEn activity resultado...");
        ////////////////////////////////////////////////////////
        //Leemos parametros entrantes
        Bundle bundle=getIntent().getExtras();
        numeroCedula=bundle.getString("ndocumento");
        nombre=bundle.getString("nombre");
        sexo=bundle.getString("sexo");
        fechaNacimiento=bundle.getString("fechanacimiento");
        tipoSangre=bundle.getString("tiposangre");
        Log.i(LOG,"Parametros entrantes: ");
        Log.i(LOG,"Numero Cedula: "+numeroCedula);
        Log.i(LOG,"nombre: "+nombre);
        Log.i(LOG,"sexo: "+sexo);
        Log.i(LOG,"fechaNacimiento: "+fechaNacimiento);
        Log.i(LOG,"tipoSangre: "+tipoSangre);


        ////////////////////////////////////////////////////////
        StringBuilder resultadob = new StringBuilder();
        resultadob.append("Nombre: "+nombre+"\n");
        resultadob.append("Numero de documento: "+numeroCedula+"\n");
        resultadob.append("Fecha nacimiento: "+fechaNacimiento+"\n");
        resultadob.append("Sexo: "+sexo+"\n");
        resultadob.append("Tipo de sangre: "+tipoSangre+"\n");
        resultadob.append("Direccion: "+direccion+"\n");
        textView_resultado.setText(resultadob.toString());

    }
}
