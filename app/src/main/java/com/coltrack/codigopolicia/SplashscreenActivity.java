package com.coltrack.codigopolicia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;

public class SplashscreenActivity extends AppCompatActivity {

    private static final int DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().setTitle("Código Nacional de Policía y Convivencia");

        // Jump to MainActivity after DELAY milliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //intent.putExtra(MainActivity.OPENED_FROM_LAUNCHER, true);
                startActivity(intent);
                finish();
            }
        }, DELAY);


    }
    @Override
    public void onBackPressed() {
        // do nothing. Protect from exiting the application when splash screen is shown
    }
}
