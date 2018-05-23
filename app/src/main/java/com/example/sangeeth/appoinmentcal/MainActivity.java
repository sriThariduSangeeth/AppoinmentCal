package com.example.sangeeth.appoinmentcal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private Calender calender;
    private Profile profile;
    private Search search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        calender = new Calender();
        profile = new Profile();
        search = new Search();

        setFragment(calender);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_cal:
                        setFragment(calender);
                        return true;

                    case R.id.nav_appo:
                        setFragment(search);
                        return true;

                    case R.id.nav_pro:
                        setFragment(profile);
                        return true;

                    default:
                        return false;
                }
            }


        });

    }

    private void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame , fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //MainActivity.this.getFragmentManager().beginTransaction().remove(MainActivity.this).commit();

        finish();


    }
}
