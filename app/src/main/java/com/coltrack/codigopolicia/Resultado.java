package com.coltrack.codigopolicia;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.ArrayList;


public class Resultado extends AppCompatActivity {
    String LOG = "nelson";
    String numeroCedula = null;
    String nombre = null;
    String sexo = null;
    String fechaNacimiento = null;
    String tipoSangre = null;
    String direccion = null;
    TextView textView_dijin;
    TextView textView_interpol;

    TextView textView_nombre;
    TextView textView_cedula;
    TextView textView_sexo;
    TextView textView_grupoSanguineo;
    TextView textView_direccion;
    TextView textView_estadoAntecedentes;
    TextView textView_estadoAntecedentesInterpol;
    TextView textView_fechanacimiento;
    TextView imageButton_medidas;
    TextView imageButton_finalizar;
    TextView textView_edad;

    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        textView_nombre = (TextView) findViewById(R.id.textView_nombre);
        textView_fechanacimiento = (TextView) findViewById(R.id.textView_fechanacimiento);
        textView_edad = (TextView) findViewById(R.id.textView_edad);
        textView_cedula = (TextView) findViewById(R.id.textView_cedula);
        textView_sexo = (TextView) findViewById(R.id.textView_sexo);
        textView_grupoSanguineo = (TextView) findViewById(R.id.textView_grupoSanguineo);
        textView_direccion = (TextView) findViewById(R.id.textView_direccion);
        imageButton_medidas = (TextView) findViewById(R.id.imageButton_medidas);
        imageButton_finalizar = (TextView) findViewById(R.id.imageButton_finalizar);
        textView_dijin = (TextView) findViewById(R.id.textView_dijin);
        textView_interpol = (TextView) findViewById(R.id.textView_interpol);
        //textView_estadoAntecedentes=(TextView)findViewById(R.id.textView_estadoAntecedentes);
        //textView_estadoAntecedentesInterpol=(TextView)findViewById(R.id.textView_estadoAntecedentesInterpol)
        Typeface font = Typeface.createFromAsset(getAssets(), "glyphicons-regular.ttf");
        ;
        mPlayer = MediaPlayer.create(Resultado.this, R.raw.beep);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Resultado Consulta");

        textView_dijin.setTypeface(font);
        textView_interpol.setTypeface(font);
        imageButton_medidas.setTypeface(font);
        imageButton_finalizar.setTypeface(font);


//        imageButton_atras.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.start();
//                Intent i=new Intent(Resultado.this,RegistroComparendo.class);
//                startActivity(i);
//            }
//        });
//        imageButton_adelante.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.start();
//                Toast.makeText(getApplicationContext(),"Selccione boton Finalizar o Medidas!!",Toast.LENGTH_SHORT).show();
//            }
//        });
        imageButton_medidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo1();











            }
        });
        imageButton_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.start();
                Log.i(LOG, "Click en finalizar....");
                Intent i = new Intent(Resultado.this, Acciones.class);
                startActivity(i);
            }
        });


        Log.i(LOG, "\n\n\n\n\nEn activity Resultado...");
        ////////////////////////////////////////////////////////
        //Leemos parametros entrantes
        Bundle bundle = getIntent().getExtras();
        numeroCedula = bundle.getString("ndocumento");
        nombre = bundle.getString("nombre");
        sexo = bundle.getString("sexo");
        fechaNacimiento = bundle.getString("fechanacimiento");
        tipoSangre = bundle.getString("tiposangre");
        Log.i(LOG, "Parametros entrantes: ");
        Log.i(LOG, "Numero Cedula: " + numeroCedula);
        Log.i(LOG, "nombre: " + nombre);
        if (sexo.equals("M")) {
            sexo = "Masculino";
        } else if (sexo.equals("F")) {
            sexo = "Femenino";
        } else {
            sexo = "ND";
        }
        fechaNacimiento = fechaNacimiento.replaceAll("/", "-");
        Log.i(LOG, "sexo: " + sexo);
        Log.i(LOG, "fechaNacimiento: " + fechaNacimiento);
        Log.i(LOG, "tipoSangre: " + tipoSangre);

        textView_nombre.setText(nombre);
        textView_fechanacimiento.setText(fechaNacimiento);

        textView_cedula.setText(numeroCedula);
        textView_sexo.setText(sexo);
        textView_grupoSanguineo.setText(tipoSangre);
        direccion = "Calle 30a # 6-22";
        textView_direccion.setText(direccion);

        int ano = Integer.parseInt(fechaNacimiento.substring(0, 4));
        int mes = Integer.parseInt(fechaNacimiento.substring(5, 7));
        int dia = Integer.parseInt(fechaNacimiento.substring(8, 10));

        Log.i(LOG, "Ano: " + ano + " mes: " + mes + " dia: " + dia);

        LocalDate birthdate = new LocalDate(ano, mes, dia);
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(birthdate, now);
        textView_edad.setText(+age.getYears() + " años");


        ////////////////////////////////////////////////////////
        StringBuilder resultadob = new StringBuilder();
        resultadob.append("Nombre: " + nombre + "\n");
        resultadob.append("Numero de documento: " + numeroCedula + "\n");
        fechaNacimiento = fechaNacimiento.replaceAll("/", "-");
        resultadob.append("Fecha nacimiento: " + fechaNacimiento + "\n");
        resultadob.append("Sexo: " + sexo + "\n");
        resultadob.append("Tipo de sangre: " + tipoSangre + "\n");
        resultadob.append("Direccion: " + direccion + "\n");


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder1.setTitle("¡Alerta!");
        builder1.setMessage(nombre + " " + numeroCedula + " presenta antecedentes en INTERPOL, antes de cualquier accion favor comunicarse a la OCN INTERPOL Colombia a los numeros +57-1-4266206, desea llamar?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Llamar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialContactPhone("+57-1-3380075");
                    }
                });

        builder1.setNegativeButton(
                "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


//        new AlertDialog.Builder(this)
//                .setTitle("¡Alerta!")
//                .setMessage(nombre+ " "+numeroCedula+" presenta antecedentes en INTERPOL, antes de cualquier accion favor comunicarse a la OCN INTERPOL Colombia a los numeros +57-1-4266206, desea llamar???")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();




    }

    private void dialogo1() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder1.setTitle("¡Alerta!");
        builder1.setMessage("Esta acción inicia un proceso de medida sobre el ciudadano por favor SEA Policía recuerde saludar, escuchar y actuar, antes de proceder.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Proceder",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mPlayer.start();
                        Log.i(LOG, "Click en medidas....");
                        Intent i = new Intent(Resultado.this, DatosCiudadano.class);
                        i.putExtra("nombre", nombre);
                        i.putExtra("numeroCedula", numeroCedula);
                        i.putExtra("fechaNacimiento", fechaNacimiento);
                        i.putExtra("sexo", sexo);
                        i.putExtra("tipoSangre", tipoSangre);
                        i.putExtra("direccion", direccion);
                        startActivity(i);

                    }
                });

        builder1.setNegativeButton(
                "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
//        final CharSequence[] items = {"Acepto"};
//// arraylist to keep the selected items
//        final ArrayList seletedItems=new ArrayList();
//
//        AlertDialog dialog = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
//                .setMessage("Esta acción inicia un proceso")
//                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
//                        if (isChecked) {
//                            // If the user checked the item, add it to the selected items
//                            seletedItems.add(indexSelected);
//                        } else if (seletedItems.contains(indexSelected)) {
//                            // Else, if the item is already in the array, remove it
//                            seletedItems.remove(Integer.valueOf(indexSelected));
//                        }
//                    }
//                }).setPositiveButton("Proceder", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Your code when user clicked on OK
//                        //  You can write the code  to save the selected item here
//                        mPlayer.start();
//                        Log.i(LOG, "Click en medidas....");
//                        Intent i = new Intent(Resultado.this, Medidas.class);
//                        i.putExtra("nombre", nombre);
//                        i.putExtra("numeroCedula", numeroCedula);
//                        i.putExtra("fechaNacimiento", fechaNacimiento);
//                        i.putExtra("sexo", sexo);
//                        i.putExtra("tipoSangre", tipoSangre);
//                        i.putExtra("direccion", direccion);
//
//                        startActivity(i);
//                    }
//                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Your code when user clicked on Cancel
//                    }
//                }).create();
//        dialog.show();
    }

    private void dialContactPhone(String s) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", s, null)));
    }
}
