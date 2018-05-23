package com.example.sangeeth.appoinmentcal;

import android.widget.EditText;

/**
 * Created by Sangeeth on 5/1/18.
 */

public class Apoogetset {


    private String date;
    private String time;
    private String title;
    private String about ;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Apoogetset(String date, String time, String title, String about) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.about = about;
    }




}
