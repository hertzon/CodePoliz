package com.coltrack.codigopolicia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Resultado extends AppCompatActivity {
    String LOG="nelson";
    String numeroCedula=null;
    String nombre=null;
    String sexo=null;
    String fechaNacimiento=null;
    String tipoSangre=null;
    String direccion=null;

    TextView textView_nombre;
    TextView textView_cedula;
    TextView textView_sexo;
    TextView textView_grupoSanguineo;
    TextView textView_direccion;
    TextView textView_estadoAntecedentes;
    TextView textView_estadoAntecedentesInterpol;
    TextView textView_fechanacimiento;
    ImageButton imageButton_medidas;
    ImageButton imageButton_finalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        textView_nombre=(TextView)findViewById(R.id.textView_nombre);
        textView_fechanacimiento=(TextView)findViewById(R.id.textView_fechanacimiento);
        textView_cedula=(TextView)findViewById(R.id.textView_cedula);
        textView_sexo=(TextView)findViewById(R.id.textView_sexo);
        textView_grupoSanguineo=(TextView)findViewById(R.id.textView_grupoSanguineo);
        textView_direccion=(TextView)findViewById(R.id.textView_direccion);
        imageButton_medidas=(ImageButton)findViewById(R.id.imageButton_medidas);
        imageButton_finalizar=(ImageButton)findViewById(R.id.imageButton_finalizar);
        //textView_estadoAntecedentes=(TextView)findViewById(R.id.textView_estadoAntecedentes);
        //textView_estadoAntecedentesInterpol=(TextView)findViewById(R.id.textView_estadoAntecedentesInterpol);


        imageButton_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Resultado.this,Acciones.class);
                startActivity(i);
            }
        });


        Log.i(LOG,"\n\n\n\n\nEn activity Resultado...");
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

        textView_nombre.setText(nombre);
        textView_fechanacimiento.setText(fechaNacimiento);
        textView_cedula.setText(numeroCedula);
        textView_sexo.setText(sexo);
        textView_grupoSanguineo.setText(tipoSangre);
        textView_direccion.setText(direccion);


        ////////////////////////////////////////////////////////
        StringBuilder resultadob = new StringBuilder();
        resultadob.append("Nombre: "+nombre+"\n");
        resultadob.append("Numero de documento: "+numeroCedula+"\n");
        resultadob.append("Fecha nacimiento: "+fechaNacimiento+"\n");
        resultadob.append("Sexo: "+sexo+"\n");
        resultadob.append("Tipo de sangre: "+tipoSangre+"\n");
        resultadob.append("Direccion: "+direccion+"\n");


    }
}
