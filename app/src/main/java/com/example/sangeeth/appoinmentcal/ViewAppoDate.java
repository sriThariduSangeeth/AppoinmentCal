package com.example.sangeeth.appoinmentcal;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.sangeeth.appoinmentcal.Sqlite.DbConnection;
import com.example.sangeeth.appoinmentcal.Thesaurus.*;

public class ViewAppoDate extends AppCompatActivity {

    DbConnection dbConnection;
    private static final String TAG = "ApoinmentListActivity";

    private String date;
    private String time;
    private String about;
    private String title1;
    //store the input from the about Appoinment
    private String inputWord;
    private int hour , min , sec ;
    private Button back;
    private FloatingActionButton addbut;

    private String valetitel;
    private String valedate;
    private String valetime;

    ArrayAdapter adapter;
    private SwipeMenuListView listday;

    //lists to store the resulting appointments
    List<Apoogetset> listArr;
    ArrayList<String> arrayList;

    private Button saveButton , thesaurusBut;
    private TextView dateview , timeset , dateset , colse , closetext;
    private EditText title , detailsEdit ,thresaurustext;
    private EditText titleedit , aboutedit , timeedit;
    private ListView synonymList;

    Dialog myDialog;
    Dialog myDialog1;
    TextView txt_thesaurus;

    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    ThesaurusAdapter thesaurusAdapter;
    ListView synonymlist; //list view to store the synonyms
    PopupWindow popupWindow;

    //constant for the thesaurus service key
    public static final String THESAURUS_KEY = "s8yQ56MR8ffkJrW9eGUL";
    //variable to store the language
    private String lang = "en_US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appo_date);

        Intent intent = getIntent();
        date = intent.getStringExtra("Date");
        Toast.makeText(getBaseContext() , date , Toast.LENGTH_SHORT).show();

        myDialog = new Dialog(this);

        back = (Button) findViewById(R.id.back);
        addbut = (FloatingActionButton) findViewById(R.id.addbut);
        listday = (SwipeMenuListView) findViewById(R.id.listday);
        dateview = (TextView) findViewById(R.id.dateview);
        closetext = (TextView) findViewById(R.id.closetext);

        dateview.setText(date);

        closetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent homeIntent = new Intent(ViewAppoDate.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });





        //creates an instance of the MyDBHandler
        dbConnection = new DbConnection(this, null, null, 1);

        listArr = dbConnection.displayAppointments(date);
        arrayList = new ArrayList<>();

        for(int j=0 ; j<listArr.size() ; j++){

            arrayList.add(j+1 + ". " + listArr.get(j).getTime() + " " + listArr.get(j).getTitle());
            //Toast.makeText(getBaseContext() ,arrayList.get(j) , Toast.LENGTH_SHORT ).show();

        }

        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, arrayList);
        listday.setAdapter(adapter);


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "Edit" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66, 0xff)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.BLACK);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listday.setMenuCreator(creator);

        listday.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                valetitel = listArr.get(position).getTitle();
                valedate = listArr.get(position).getDate();
                valetime = listArr.get(position).getTime();



                switch (index) {
                    case 0:
                        Log.d(TAG, "onMenuItemClick: clicked item " + index);

                        TextView textclose;
                        Button update;
                        // this is edit list item

                        Log.d(TAG, "onMenuItemClick: "+ valetitel);
                        Log.d(TAG, "onMenuItemClick: clicked item " + position);

                        myDialog.setContentView(R.layout.update_data);

                        textclose = (TextView) myDialog.findViewById(R.id.colse);
                        update = (Button) myDialog.findViewById(R.id.update);
                        titleedit = (EditText) myDialog.findViewById(R.id.titleedit);
                        aboutedit = (EditText) myDialog.findViewById(R.id.aboutedit);
                        timeedit = (EditText) myDialog.findViewById(R.id.timeedit);

                        textclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                            }
                        });

                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int success = dbConnection.updateAppointment(listArr.get(position),
                                        timeedit.getText().toString(), titleedit.getText().toString(), aboutedit.getText().toString());

                                if (success == 1) {

                                    Toast.makeText(getBaseContext(), "Successfully updated the appointment", Toast.LENGTH_LONG).show();

                                } else if (success == -1) {

                                    Toast.makeText(getBaseContext(), "Can't Edit Please try again with a valid number.", Toast.LENGTH_SHORT).show();

                                }

                                //refreshes the page
                                myDialog.dismiss();
                                startActivity(getIntent());
                            }
                        });


                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.show();


                        break;
                    case 1:

                        //delete event
                        errorDialogdelete("Would you like to delete event :  " +
                                valetitel + "?");

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


    }


    // This is a add appoinment form.
    public void Showpopup (View v){

        setContentView(R.layout.add_appo);

        title = (EditText) findViewById(R.id.title);
        detailsEdit = (EditText) findViewById(R.id.detailsEdit);
        timeset = (TextView) findViewById(R.id.timeset);
        dateset = (TextView) findViewById(R.id.dateset);
        colse = (TextView) findViewById(R.id.colse);
        saveButton = (Button) findViewById(R.id.saveButton);
        thesaurusBut = (Button) findViewById(R.id.thesaurusBut);
        thresaurustext = (EditText) findViewById(R.id.thresaurustext);

        dateset.setText(date);


        //back button
        colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // When tap this text View TimePicker will open
        timeset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                hour = cal.get(Calendar.HOUR);
                min = cal.get(Calendar.MINUTE);
                sec = cal.get(Calendar.SECOND);

                TimePickerDialog dialog = new TimePickerDialog(ViewAppoDate.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener ,
                        hour,min,true);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        //get the time from TimePicker
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Log.d(TAG, "onTimeSet: " +hourOfDay+" " +minute);

                String gettime = ""+hourOfDay+":"+minute;


                try {
                    //24 hours time format will convert to 12 hours format

                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                    Date _24HourDt = _24HourSDF.parse(gettime);
                    time = _12HourSDF.format(_24HourDt);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeset.setText(time);


            }
        };


        // pass data to SQLit database and Store
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title1 = title.getText().toString();
                about = detailsEdit.getText().toString();

                Log.d(TAG, "title : "+title1);
                Log.d(TAG, "date : "+date);
                Log.d(TAG, "time : "+time);
                Log.d(TAG, "about : "+about);


                Apoogetset appointment = new Apoogetset(date, time, title1, about);
                int i = dbConnection.createAppointment(appointment);
                if (i == 1) {

                    errorDialog("Appointment " + title + " on " + date + " was created successfully.");
                    //printDatabase();

                    Intent homeIntent = new Intent(ViewAppoDate.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();

                } else if (i == -1) {

                    errorDialog("Appointment "+ title +" already exists, please choose a different event title");
                }

            }
        });

        // Thesaurus Option Button
        thesaurusBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWord = thresaurustext.getText().toString();
                System.out.println(inputWord);
                //call to resultPopUp method
                resultPopUp(v);
                //thesaurusBtnAction(false);


            }
        });



    }



    //this is a save Appoinment button. onclick event
    public void addButtonClicked(View v){

    }

    //create the back button onclick event
    public void backto(View v){

        Intent homeIntent = new Intent(ViewAppoDate.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }

    //find the selection word mean onclick event
    public void Thesaurusword(View v){




    }



    //check whether internet connection is Available or not...
    private boolean CheckNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //error dialog popup
    public void errorDialog(String error)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.SangeethDialTheme);
        builder.setMessage(error);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    //This is a Alert box that come up before deleting...
    public void errorDialogdelete(String error)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.SangeethDialTheme);
        builder.setMessage(error);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getBaseContext(), "Deleted the " +
                                valetitel +
                                " appointment.", Toast.LENGTH_SHORT).show();
                        dbConnection.deleteAppointments(valedate , valetitel);
                        //adapter.notifyDataSetChanged(); //refreshes the list, NOT WORKING
                        dialog.dismiss();

                        //bad way to refresh
                        finish();
                        startActivity(getIntent());

                    }
                });
        builder.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }



//Thresaurus Section
//--------------------------------------------------------------------------------------------------

    public void resultPopUp (View v) {

        try {

            TextView textclose;


            myDialog.setContentView(R.layout.thesauruslist);
            textclose = (TextView) myDialog.findViewById(R.id.colse1);
            //synonymList = (ListView) myDialog.findViewById(R.id.synonymList);
            txt_thesaurus = (TextView)myDialog.findViewById(R.id.txt_thesaurus);


            textclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });

            if(CheckNetworkAvailable()){

                SitesDownloadTask download = new SitesDownloadTask();
                download.execute();
            }else{

                Toast.makeText(getBaseContext() , "No internet Connection. Please connect " +
                        "your device to the internet and try again" , Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //perameter , progress , result
    private class SitesDownloadTask extends AsyncTask<Void, Void, Void> {

        public List<Synonym> synonymList = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                DownloadFromUrl("http://thesaurus.altervista.org/thesaurus/v1?word=" + inputWord +
                                "&language="+ lang +"&%20key="+ THESAURUS_KEY +"&output=xml",
                        openFileOutput("synonyms.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){

            //setup our Adapter and set it to the ListView.
//            thesaurusAdapter = new ThesaurusAdapter(ViewAppoDate.this, -1,
//                    ThesaurusXMLPullParser.getSynonymsFromFile(ViewAppoDate.this));
//            //System.out.println(thesaurusAdapter);
//            synonymlist.setAdapter(thesaurusAdapter);

            synonymList  = ThesaurusXMLPullParser.getSynonymsFromFile(ViewAppoDate.this);
            List<String> list = new ArrayList<>();
            for(Synonym synonym : synonymList){

                try{

                    String[] arr = synonym.getSynonyms().split("\\|");

                    if(arr.length>0){
                        for(int i = 0;i<arr.length;i++){
                            list.add(arr[i].toString() + "\n");
                        }
                    }else {
                        list.add(synonym.getSynonyms().toString()+"\n");
                    }

                }catch (Exception e){

                }

            }
            System.out.println(synonymList);
            txt_thesaurus.setText(list.toString());


        }
    }


    public static void DownloadFromUrl(String URL, FileOutputStream fos) {
        try {

            java.net.URL url = new URL(URL); //URL of the file

			/* Open a connection to that URL. */
            URLConnection connection = url.openConnection();


            //input stream that'll read from the connection
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            //buffer output stream that'll write to the xml file
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            //write to the file while reading
            byte data[] = new byte[1024];
            int count;
            //loop and read the current chunk
            while ((count = bis.read(data)) != -1) {
                //write this chunk
                bos.write(data, 0, count);
            }

            bos.flush();
            bos.close();

        } catch (IOException e) {
        }
    }
//==================================================================================================

//    public void setList(List<Synonyms> synonymsList){
//        this.synonymsList = synonymsList;
//    }

}


