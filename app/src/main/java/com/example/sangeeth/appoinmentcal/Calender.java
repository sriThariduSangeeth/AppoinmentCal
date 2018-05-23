package com.example.sangeeth.appoinmentcal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Calender extends Fragment {

    private String datepass;
    private int day;
    private int month;
    private int year;
    private static final String TAG = "CalenderActivity";
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private MaterialCalendarView materialCalendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViwe =  inflater.inflate(R.layout.fragment_calender, container, false);

       materialCalendarView = (MaterialCalendarView)rootViwe.findViewById(R.id.calendarView);

       materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2000, 1, 1))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

       materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {


           @Override
           public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

               String d1 = ""+date.getDate();
               day = date.getDay();
               month = date.getMonth()+1;
               year = date.getYear();
               Log.d(TAG, "onDateSelected: "+d1);
               Log.d(TAG, "onDateSelected: "+day);
               Log.d(TAG, "onMonthSelected: "+month);
               Log.d(TAG, "onYearSelected: "+year);

               datepass = ""+year+"/"+month+"/"+day; //set as string in date format

               Intent intent = new Intent(getActivity() , ViewAppoDate.class);
               intent.putExtra("Date" , datepass ); // format - yyyy/mm/dd
               intent.putExtra("Day", day ); // format - day - integer
               intent.putExtra("Month", month ); // format - month - integer
               intent.putExtra("Year", year ); // format - year - integer
               startActivity(intent);

           }

       });


       return rootViwe;


    }


}
