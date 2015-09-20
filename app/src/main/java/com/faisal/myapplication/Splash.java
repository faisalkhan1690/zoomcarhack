package com.faisal.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by faisal khan on 9/13/2015.
 */
public class Splash extends AppCompatActivity {

    private final int DELAY_TIME=2000; // delay time for handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // wait for 2 second then go to Home activity of application
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this,Home.class));
                finish();
            }
        },DELAY_TIME);
    }
}

