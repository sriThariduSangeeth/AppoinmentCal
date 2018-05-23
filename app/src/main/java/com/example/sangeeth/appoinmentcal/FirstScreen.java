package com.example.sangeeth.appoinmentcal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class FirstScreen extends Activity {


    private final int DURATION =3000;
    private Thread mSplashThread;
    private ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);


        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mSplashThread = new Thread() {

            @Override
            public void run() {
                synchronized (this) {
                    try {
                        // wait(1000);
                        progressBar.setProgress(25);
                        // wait(2000);
                        progressBar.setProgress(50);
                        //  wait(1500);
                        progressBar.setProgress(80);
                        wait(DURATION);
                    } catch (InterruptedException e) {
                    } finally {
                        progressBar.setProgress(100);
                        finish();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }

        };
        mSplashThread.start();

    }
}
