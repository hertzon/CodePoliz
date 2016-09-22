package com.coltrack.codigopolicia;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Acciones extends AppCompatActivity {
    ImageButton imgbtn_personas;
    ImageButton imgbtn_vehiculos;
    ImageButton imgbtn_tamir;
    ImageButton imgbtn_contacto;
    ImageButton imgbtn_imeis;
    ImageButton imgbtn_procedimientos;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        imgbtn_personas=(ImageButton)findViewById(R.id.imgbtn_personas);
        imgbtn_vehiculos=(ImageButton)findViewById(R.id.imgbtn_vehiculos);
        imgbtn_tamir=(ImageButton)findViewById(R.id.imgbtn_tamir);
        imgbtn_contacto=(ImageButton)findViewById(R.id.imgbtn_contacto);
        imgbtn_imeis=(ImageButton)findViewById(R.id.imgbtn_imeis);
        imgbtn_procedimientos=(ImageButton)findViewById(R.id.imgbtn_procedimientos);

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
