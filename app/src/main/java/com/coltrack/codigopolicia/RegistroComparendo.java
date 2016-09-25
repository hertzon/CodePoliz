package com.coltrack.codigopolicia;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.microblink.activity.Pdf417ScanActivity;
import com.microblink.hardware.camera.CameraType;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkbarcode.BarcodeType;
import com.microblink.recognizers.blinkbarcode.bardecoder.BarDecoderRecognizerSettings;
import com.microblink.recognizers.blinkbarcode.bardecoder.BarDecoderScanResult;
import com.microblink.recognizers.blinkbarcode.pdf417.Pdf417RecognizerSettings;
import com.microblink.recognizers.blinkbarcode.pdf417.Pdf417ScanResult;
import com.microblink.recognizers.blinkbarcode.usdl.USDLRecognizerSettings;
import com.microblink.recognizers.blinkbarcode.usdl.USDLScanResult;
import com.microblink.recognizers.blinkbarcode.zxing.ZXingRecognizerSettings;
import com.microblink.recognizers.blinkbarcode.zxing.ZXingScanResult;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.recognizers.settings.RecognizerSettingsUtils;
import com.microblink.results.barcode.BarcodeDetailedData;

import com.microblink.util.RecognizerCompatibility;
import com.microblink.util.RecognizerCompatibilityStatus;
import com.microblink.view.recognition.RecognizerView;

public class RegistroComparendo extends AppCompatActivity {

    //Constantes MicroBlink
    private static final String LICENSE_KEY = "L6DNPD6O-TITPVJIZ-F5U7YPHL-XREWOS75-APW573Y7-QLCKHOYP-FBBQM75Q-MS62BG7T";
    private static final int MY_REQUEST_CODE = 1337;

    ExpandableListAdapter mAdapter;
    String LOG="nelson";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String tipoDocumento=null;
    ImageButton btn_escanear;
    ImageButton imageButton_buscar;

    String s=null;
    String numeroCedula=null;
    String nombre=null;
    String cedula=null;
    String sexo=null;
    String fechaNacimiento=null;
    String tipoSangre=null;
    MediaPlayer mPlayer;
    String ss="";
    StringBuilder aux;
    private String[] campos = new String[30];
    StringBuilder caracteres;
    String apellido;
    String segundoApellido;

    EditText editText_cedula;
    TextView textView_resutado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_comparendo);


        //getSupportActionBar().setTitle("Orden de comparendo y medidas correctivas");

        tipoDocumento="Cedula de Ciudadania";
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.ExpandableListView01);
        // preparing list data
        prepareListData();
        listAdapter=new com.coltrack.codigopolicia.ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        btn_escanear=(ImageButton)findViewById(R.id.button_escanear);
        editText_cedula=(EditText)findViewById(R.id.editText_cedula);
        imageButton_buscar=(ImageButton)findViewById(R.id.imageButton_buscar);
        textView_resutado=(TextView)findViewById(R.id.textView_resultado);

        Log.i(LOG, "BuildVersionString: " + buildVersionString());

        numeroCedula=null;
        mPlayer = MediaPlayer.create(RegistroComparendo.this, R.raw.beep);

        imageButton_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.start();
                if (!TextUtils.isEmpty(editText_cedula.getText().toString())){
                    Intent i=new Intent(getApplicationContext(),Resultado.class);
                    //Enviamos parametros al activity Resultado
                    i.putExtra("nombre",nombre+"\n"+apellido+" "+segundoApellido);
                    i.putExtra("ndocumento",numeroCedula);
                    i.putExtra("fechanacimiento",fechaNacimiento);
                    i.putExtra("sexo",sexo);
                    i.putExtra("tiposangre",tipoSangre);


                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(),"Ingrese o escanee un documento de identidad valido!!",Toast.LENGTH_SHORT).show();
                    //editText_cedula.setError("Ingrese un numero  valido!!");
                }

            }
        });

        btn_escanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPlayer.start();
                //t.makeText(getApplicationContext(), "Abriendo Scanner...", Toast.LENGTH_SHORT).show();
                // check if PDF417.mobi is supported on the device
                RecognizerCompatibilityStatus status = RecognizerCompatibility.getRecognizerCompatibilityStatus(getApplicationContext());
                if (status == RecognizerCompatibilityStatus.RECOGNIZER_SUPPORTED) {
                    //Toast.makeText(getApplicationContext(), "Camara compatible", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Camara no soportada: " + status.name(), Toast.LENGTH_LONG).show();
                }

                Log.i(LOG, "scan will be performed");
                Intent intent = new Intent(getApplicationContext(), Pdf417ScanActivity.class);
                // If you want sound to be played after the scanning process ends,
                // put here the resource ID of your sound file. (optional)
                intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);
                // In order for scanning to work, you must enter a valid licence key. Without licence key,
                // scanning will not work. Licence key is bound the the package name of your app, so when
                // obtaining your licence key from Microblink make sure you give us the correct package name
                // of your app. You can obtain your licence key at http://microblink.com/login or contact us
                // at http://help.microblink.com.
                // Licence key also defines which recognizers are enabled and which are not. Since the licence
                // key validation is performed on image processing thread in native code, all enabled recognizers
                // that are disallowed by licence key will be turned off without any error and information
                // about turning them off will be logged to ADB logcat.
                intent.putExtra(Pdf417ScanActivity.EXTRAS_LICENSE_KEY, LICENSE_KEY);
                // If you want to open front facing camera, uncomment the following line.
                // Note that front facing cameras do not have autofocus support, so it will not
                // be possible to scan denser and smaller codes.
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_TYPE, (Parcelable) CameraType.CAMERA_FRONTFACE);

                // You need to define array of recognizer settings. There are 4 types of recognizers available
                // in PDF417.mobi SDK.
                // Don't enable recognizers and barcode types which you don't actually use because this will
                // significantly decrease the scanning speed.

                // Pdf417RecognizerSettings define the settings for scanning plain PDF417 barcodes.
                Pdf417RecognizerSettings pdf417RecognizerSettings = new Pdf417RecognizerSettings();
                // Set this to true to scan barcodes which don't have quiet zone (white area) around it
                // Use only if necessary because it drastically slows down the recognition process
                pdf417RecognizerSettings.setNullQuietZoneAllowed(true);
                // Set this to true to scan even barcode not compliant with standards
                // For example, malformed PDF417 barcodes which were incorrectly encoded
                // Use only if necessary because it slows down the recognition process
//        pdf417RecognizerSettings.setUncertainScanning(true);

                // BarDecoderRecognizerSettings define settings for scanning 1D barcodes with algorithms
                // implemented by Microblink team.
                BarDecoderRecognizerSettings oneDimensionalRecognizerSettings = new BarDecoderRecognizerSettings();
                // set this to true to enable scanning of Code 39 1D barcodes
                oneDimensionalRecognizerSettings.setScanCode39(true);
                // set this to true to enable scanning of Code 128 1D barcodes
                oneDimensionalRecognizerSettings.setScanCode128(true);
                // set this to true to use heavier algorithms for scanning 1D barcodes
                // those algorithms are slower, but can scan lower resolution barcodes
//        oneDimensionalRecognizerSettings.setTryHarder(true);

                // USDLRecognizerSettings define settings for scanning US Driver's Licence barcodes
                // options available in that settings are similar to those in Pdf417RecognizerSettings
                // if license key does not allow scanning of US Driver's License, this settings will
                // be thrown out from settings array and error will be logged to logcat.
                USDLRecognizerSettings usdlRecognizerSettings = new USDLRecognizerSettings();

                // ZXingRecognizerSettings define settings for scanning barcodes with ZXing library
                // We use modified version of ZXing library to support scanning of barcodes for which
                // we still haven't implemented our own algorithms.
                ZXingRecognizerSettings zXingRecognizerSettings = new ZXingRecognizerSettings();
                // set this to true to enable scanning of QR codes
                zXingRecognizerSettings.setScanQRCode(false);
                zXingRecognizerSettings.setScanITFCode(false);

                // finally, when you have defined settings for each recognizer you want to use,
                // you should put them into array held by global settings object
                // setup array of recognition settings (described in chapter "Recognition
// settings and results")


                RecognitionSettings recognitionSettings = new RecognitionSettings();
                // add settings objects to recognizer settings array
                // Pdf417Recognizer, BarDecoderRecognizer, USDLRecognizer and ZXingRecognizer
                //  will be used in the recognition process
                recognitionSettings.setRecognizerSettingsArray(
                        new RecognizerSettings[]{pdf417RecognizerSettings, oneDimensionalRecognizerSettings,
                                usdlRecognizerSettings, zXingRecognizerSettings});

                //recognitionSettings.setRecognizerSettingsArray(setupSettingsArray());
                RecognizerSettings[] settArray = setupSettingsArray();
                if (!RecognizerCompatibility.cameraHasAutofocus(CameraType.CAMERA_BACKFACE, getApplicationContext())) {
                    //Toast.makeText(getApplicationContext(), "Camara no soporta autofocus!!", Toast.LENGTH_SHORT).show();
                    settArray = RecognizerSettingsUtils.filterOutRecognizersThatRequireAutofocus(settArray);
                } else {
                    //Toast.makeText(getApplicationContext(), "Camara  soporta autofocus", Toast.LENGTH_SHORT).show();
                }

                // additionally, there are generic settings that are used by all recognizers or the
                // whole recognition process
                recognitionSettings.setAllowMultipleScanResultsOnSingleImage(false);


                // by default, this option is true, which means that it is possible to obtain multiple
                // recognition results from the same image.
                // if you want to obtain one result from the first successful recognizer
                // (when first barcode is found, no matter which type) set this option to false
//        recognitionSettings.setAllowMultipleScanResultsOnSingleImage(false);

                // finally send that settings object over intent to scan activity
                // use Pdf417ScanActivity.EXTRAS_RECOGNITION_SETTINGS to set recognizer settings
                intent.putExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_SETTINGS, recognitionSettings);

                // if you do not want the dialog to be shown when scanning completes, add following extra
                // to intent
                intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_DIALOG_AFTER_SCAN, false);
                intent.putExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_RESULTS, true);


                // if you want to enable pinch to zoom gesture, add following extra to intent
                intent.putExtra(Pdf417ScanActivity.EXTRAS_ALLOW_PINCH_TO_ZOOM, true);

                // if you want Pdf417ScanActivity to display rectangle where camera is focusing,
                // add following extra to intent
                intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_FOCUS_RECTANGLE, true);

                // if you want to use camera fit aspect mode to letterbox the camera preview inside
                // available activity space instead of cropping camera frame (default), add following
                // extra to intent.
                // Camera Fit mode does not look as nice as Camera Fill mode on all devices, especially on
                // devices that have very different aspect ratios of screens and cameras. However, it allows
                // all camera frame pixels to be processed - this is useful when reading very large barcodes.
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_ASPECT_MODE, (Parcelable) CameraAspectMode.ASPECT_FIT);

                // Start Activity
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });





        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();
                tipoDocumento=listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition);
                Log.i(LOG,"Tipo de documento: "+tipoDocumento);
                listDataHeader.clear();
                prepareListData();
                listAdapter=new com.coltrack.codigopolicia.ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);
                expListView.collapseGroup(groupPosition);
                return false;
            }
        });
    }


    private RecognizerSettings[] setupSettingsArray() {
        Pdf417RecognizerSettings sett = new Pdf417RecognizerSettings();
        // disable scanning of white barcodes on black background
        sett.setInverseScanning(false);
        // allow scanning of barcodes that have invalid checksum
        sett.setUncertainScanning(true);
        // disable scanning of barcodes that do not have quiet zone
        // as defined by the standard
        sett.setNullQuietZoneAllowed(false);

        // now add sett to recognizer settings array that is used to configure
        // recognition
        return new RecognizerSettings[] { sett };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE && resultCode == Pdf417ScanActivity.RESULT_OK) {
            Log.i(LOG,"En onActivityResult");
            // First, obtain recognition result
            RecognitionResults results = data.getParcelableExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_RESULTS);
            // Get scan results array. If scan was successful, array will contain at least one element.
            // Multiple element may be in array if multiple scan results from single image were allowed in settings.
            BaseRecognitionResult[] resultArray = results.getRecognitionResults();

            // Each recognition result corresponds to active recognizer. As stated earlier, there are 4 types of
            // recognizers available (PDF417, Bardecoder, ZXing and USDL), so there are 4 types of results
            // available.

            StringBuilder sb = new StringBuilder();

            for(BaseRecognitionResult res : resultArray) {
                if(res instanceof Pdf417ScanResult) { // check if scan result is result of Pdf417 recognizer
                    Pdf417ScanResult result = (Pdf417ScanResult) res;
                    // getStringData getter will return the string version of barcode contents
                    //String barcodeData = result.getStringData();
                    //Log.i(LOG,"getStringData: "+barcodeData);
                    // isUncertain getter will tell you if scanned barcode contains some uncertainties
                    //boolean uncertainData = result.isUncertain();
                    //Log.i(LOG,"uncertainData: "+uncertainData);
                    // getRawData getter will return the raw data information object of barcode contents
                    BarcodeDetailedData rawData = result.getRawData();


                    //Otro algoritmo:


                    // BarcodeDetailedData contains information about barcode's binary layout, if you
                    // are only interested in raw bytes, you can obtain them with getAllData getter
                    byte[] rawDataBuffer = rawData.getAllData();
                    //Log.i(LOG,"rawDataBuffer: "+rawDataBuffer);
                    if (rawDataBuffer != null) {
                        //sb.append("\n");
                        //sb.append("PDF417 raw data merged:\n");
                        sb.append("{");
                        for (int i = 0; i < rawDataBuffer.length; ++i) {

                            if (((int) rawDataBuffer[i] & 0x0FF)==0){
                                sb.append("32");
                                if (i != rawDataBuffer.length - 1) {
                                    sb.append(",");
                                }
                            }else {
                                sb.append((int) rawDataBuffer[i] & 0x0FF);
                                if (i != rawDataBuffer.length - 1) {
                                    sb.append(",");
                                }
                            }




                        }
                    }
                    sb.append("}");


                    String resultado=sb.toString();
                    Log.i(LOG,"Resultado: "+resultado);
                    resultado=resultado.replaceAll(" ","");

                    resultado=resultado.replaceAll("\\{" ,"");
                    resultado=resultado.replaceAll("\\}" ,"");
                    Log.i(LOG,"RAw sb:"+resultado);

                    String[] parts=resultado.split(",");
                    caracteres = new StringBuilder();
                    Log.i(LOG,"npartes: "+parts.length);
                    for (int i=0;i<parts.length;i++){
                        //if ((Integer.parseInt(parts[i])>=32 && Integer.parseInt(parts[i])<=127) || Integer.parseInt(parts[i])==209 ){
                            caracteres.append((char)Integer.parseInt(parts[i]));
                            //caracteres.append(Character.toString((char)Integer.parseInt(parts[i])));
                            //caracteres.append(",");

                    }
                    String respuesta=caracteres.toString();
                    respuesta=respuesta.replaceAll("\\p{C}", "?");//remove not printable characters
                    respuesta=respuesta.replaceAll("\\s+", " ");// remove multiple spaces

                    Log.i(LOG, "Caracteres: " + respuesta);

                    Pattern pattern=Pattern.compile("[0-9]{10}[a-zA-ZñÑ]{2,}");
                    Matcher matcher=pattern.matcher(respuesta);
                    if (matcher.find()){
                        Log.i(LOG,"encontrado: "+matcher.group(0));
                        Log.i(LOG,"start: "+matcher.start());
                        respuesta=respuesta.substring(matcher.start(),respuesta.length());
                        numeroCedula=respuesta.substring(0, 10);
                        numeroCedula=numeroCedula.replaceFirst("^0+(?!$)", "");
                        respuesta=respuesta.substring(10, respuesta.length());

                        pattern=Pattern.compile("^[a-zA-ZñÑ]{2,} ");
                        matcher=pattern.matcher(respuesta);
                        if (matcher.find()){
                            apellido=matcher.group(0);
                            apellido=apellido.substring(0,apellido.length()-1);
                            Log.i(LOG,"apellido: "+apellido);
                            respuesta=respuesta.substring(matcher.group(0).length(),respuesta.length());

                            pattern=Pattern.compile("^[a-zA-ZñÑ]{2,} ");
                            matcher=pattern.matcher(respuesta);
                            if (matcher.find()){
                                segundoApellido=matcher.group(0);
                                segundoApellido=segundoApellido.substring(0, segundoApellido.length() - 1);
                                Log.i(LOG,"segundo apellido: "+segundoApellido);
                                respuesta=respuesta.substring(matcher.group(0).length(), respuesta.length());

                                pattern=Pattern.compile("^[a-zA-ZñÑ]+ ");
                                matcher=pattern.matcher(respuesta);
                                nombre="";
                                while (matcher.find()){
                                    nombre=nombre+matcher.group(0);
                                    Log.i(LOG,"nombre: "+nombre);
                                    respuesta=respuesta.substring(matcher.group(0).length(),respuesta.length());
                                    matcher=pattern.matcher(respuesta);
                                }
                                nombre=nombre.substring(0,nombre.length()-1);
                                Log.i(LOG,"nombre: "+nombre);


                                pattern=Pattern.compile("0[MF]{1}[0-9]{8}[0-9]*[ABO]{1,2}. ");
                                matcher=pattern.matcher(respuesta);
                                if (matcher.find()){
                                    sexo=matcher.group(0);
                                    sexo=sexo.substring(1, 2);
                                    Log.i(LOG,"Sexo: "+sexo);
                                    fechaNacimiento=matcher.group(0).substring(2,10);
                                    fechaNacimiento=fechaNacimiento.substring(0,4)+"/"+fechaNacimiento.substring(4,6)+"/"+fechaNacimiento.substring(6,8);
                                    Log.i(LOG,"fecha nacimiento: "+fechaNacimiento);

                                    pattern=Pattern.compile("[ABO]{1,2}. $");
                                    matcher=pattern.matcher(matcher.group(0));
                                    if (matcher.find()){
                                        tipoSangre=matcher.group(0);
                                        tipoSangre=tipoSangre.substring(0,tipoSangre.length()-1);
                                        Log.i(LOG,"Tipo de sangre: "+tipoSangre);
                                    }

                                }

                            }




                        }
                    }
                    editText_cedula.setText(numeroCedula);
                    Log.i(LOG,"Caracteres: "+respuesta);
                    StringBuilder stringb=new StringBuilder();
                    stringb.append("NOMBRE: "+nombre+" "+apellido+" "+segundoApellido+"\n");
                    stringb.append("SEXO: "+sexo+"\n");
                    stringb.append("FECHA DE NACIMIENTO: "+fechaNacimiento+"\n");
                    stringb.append("GRUPO SANGUINEO: "+tipoSangre+"\n");
                    textView_resutado.setText(stringb.toString());



                    if (false){
                        return;
                    } else {
                        // add data to string builder
//                        sb.append("PDF417 scan data");
//                        if (uncertainData) {
//                            sb.append("This scan data is uncertain!\n\n");
//                        }
//                        sb.append(" string data:\n");
//                        sb.append(barcodeData);
//                        if (rawData != null) {
//                            sb.append("\n\n");
//                            sb.append("PDF417 raw data:\n");
//                            sb.append(rawData.toString());
//                            sb.append("\n");
//                            sb.append("PDF417 raw data merged:\n");
//                            sb.append("{");
//                            for (int i = 0; i < rawDataBuffer.length; ++i) {
//                                sb.append((int) rawDataBuffer[i] & 0x0FF);
//                                if (i != rawDataBuffer.length - 1) {
//                                    sb.append(", ");
//                                }
//                            }
//                            sb.append("}\n\n\n");
//                        }
                    }
                    //Log.i(LOG,"RAW: "+sb.toString());
                } else if(res instanceof BarDecoderScanResult) { // check if scan result is result of BarDecoder recognizer
                    Log.i(LOG,"Is barcode");
                    BarDecoderScanResult result = (BarDecoderScanResult) res;
                    // with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
                    BarcodeType type = result.getBarcodeType();
                    // as with PDF417, getStringData will return the string contents of barcode
                    String barcodeData = result.getStringData();
//                    if(checkIfDataIsUrlAndCreateIntent(barcodeData)) {
//                        return;
//                    } else {
//                        sb.append(type.name());
//                        sb.append(" string data:\n");
//                        sb.append(barcodeData);
//                        sb.append("\n\n\n");
//                    }
                } else if(res instanceof ZXingScanResult) { // check if scan result is result of ZXing recognizer
                    Log.i(LOG,"Is ZXingScanResult");
                    ZXingScanResult result= (ZXingScanResult) res;
                    // with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
                    BarcodeType type = result.getBarcodeType();
                    // as with PDF417, getStringData will return the string contents of barcode
                    String barcodeData = result.getStringData();
                    //if(checkIfDataIsUrlAndCreateIntent(barcodeData)) {
                    if (false){
                        return;
                    } else {
                        sb.append(type.name());
                        sb.append(" string data:\n");
                        sb.append(barcodeData);
                        sb.append("\n\n\n");
                    }
                } else if(res instanceof USDLScanResult) { // check if scan result is result of US Driver's Licence recognizer
                    USDLScanResult result = (USDLScanResult) res;
                    Log.i(LOG,"Is USDLScanResult");
                    // USDLScanResult can contain lots of information extracted from driver's licence
                    // you can obtain information using the getField method with keys defined in
                    // USDLScanResult class

                    String name = result.getField(USDLScanResult.kCustomerFullName);
                    Log.i(LOG, "Customer full name is " + name);

                    sb.append(result.getTitle());
                    sb.append("\n\n");
                    sb.append(result.toString());
                }
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            startActivity(Intent.createChooser(intent, getString(R.string.UseWith)));
        }
    }

    private String buildVersionString() {
        String nativeVersionString = RecognizerView.getNativeLibraryVersionString();
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String appVersion = pInfo.versionName;
            int appVersionCode = pInfo.versionCode;

            StringBuilder infoStr = new StringBuilder();
            infoStr.append("Application version: ");
            infoStr.append(appVersion);
            infoStr.append(", build ");
            infoStr.append(appVersionCode);
            infoStr.append("\nLibrary version: ");
            infoStr.append(nativeVersionString);
            return infoStr.toString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.clear();
        listDataHeader.add(tipoDocumento);


        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Cedula de Ciudadanía");
        top250.add("Cedula de Extranjería");
        top250.add("Pasaporte");
        top250.add("Registro Civil");
        top250.add("Tarjeta de Identidad");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data

    }





}
