package com.coltrack.codigopolicia;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Acciones extends AppCompatActivity {
    TextView imgbtn_personas;
    TextView imgbtn_vehiculos;
    TextView imgbtn_tamir;
    TextView imgbtn_contacto;
    TextView imgbtn_imeis;
    TextView imgbtn_procedimientos;
    TextView imgbtn_codigo;
    TextView imgbtn_salir;
    MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        imgbtn_personas=(TextView) findViewById(R.id.imgbtn_personas);
        imgbtn_vehiculos=(TextView) findViewById(R.id.imgbtn_vehiculos);
        imgbtn_tamir=(TextView) findViewById(R.id.imgbtn_tamir);
        imgbtn_contacto=(TextView) findViewById(R.id.imgbtn_contacto);
        imgbtn_imeis=(TextView) findViewById(R.id.imgbtn_imeis);
        imgbtn_procedimientos=(TextView) findViewById(R.id.imgbtn_procedimientos);
        imgbtn_codigo=(TextView) findViewById(R.id.imgbtn_codigo);
        imgbtn_salir=(TextView) findViewById(R.id.imgbtn_salir);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Actividades");



        Typeface font = Typeface.createFromAsset(getAssets(), "glyphicons-regular.ttf");

        imgbtn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.start();
                Intent i=new Intent(Acciones.this,MainActivity.class);
                startActivity(i);
            }
        });



        imgbtn_personas.setTypeface(font);
        imgbtn_tamir.setTypeface(font);
        imgbtn_vehiculos.setTypeface(font);
        imgbtn_imeis.setTypeface(font);
        imgbtn_contacto.setTypeface(font);
        imgbtn_procedimientos.setTypeface(font);
        imgbtn_codigo.setTypeface(font);
        imgbtn_salir.setTypeface(font);



        mPlayer = MediaPlayer.create(Acciones.this, R.raw.beep);

        imgbtn_personas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.start();
                Intent i=new Intent(Acciones.this,RegistroComparendo.class);
                startActivity(i);
            }
        });







    }
}
