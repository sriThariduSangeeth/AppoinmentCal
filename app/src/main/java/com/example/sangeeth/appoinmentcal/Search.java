package com.example.sangeeth.appoinmentcal;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.sangeeth.appoinmentcal.Sqlite.DbConnection;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {


    DbConnection dbConnection;
    MaterialSearchView searchView;
    android.support.v7.widget.Toolbar toolbar;


    //lists to store the resulting appointments
    List<Apoogetset> listArr;
    ArrayList<String> arrayList;

    private String valeposition;
    private String date;

    private EditText titleedit , aboutedit , timeedit;


    private SwipeMenuListView listView;
    ArrayAdapter adapter;
    private static final String TAG = "SearchActivity";

    Dialog myDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViwe = inflater.inflate(R.layout.fragment_search, container, false);


        myDialog = new Dialog(getActivity());
        toolbar = (android.support.v7.widget.Toolbar)rootViwe.findViewById(R.id.toolbar);
        listView = (SwipeMenuListView) rootViwe.findViewById(R.id.listView);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


        //creates an instance of the MyDBHandler
        dbConnection = new DbConnection(getActivity(), null, null, 1);

        listArr = dbConnection.displayAppointments();
        arrayList = new ArrayList<>();
        for(int j=0 ; j<listArr.size() ; j++){

            arrayList.add( j+1 + ". " + listArr.get(j).getDate() + " " + listArr.get(j).getTitle()+ " " + listArr.get(j).getTime());
            //Toast.makeText(getBaseContext() ,arrayList.get(j) , Toast.LENGTH_SHORT ).show();

        }
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, arrayList);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "Edit" item
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity().getApplicationContext());
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
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

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                valeposition = listArr.get(position).getTitle();
                date = listArr.get(position).getDate();


                switch (index) {
                    case 0:

                        TextView textclose;
                        Button update;
                        // this is edit list item

                        Log.d(TAG, "onMenuItemClick: "+ valeposition);
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

                                    Toast.makeText(getActivity().getBaseContext(), "Successfully updated the appointment", Toast.LENGTH_LONG).show();

                                } else if (success == -1) {

                                    Toast.makeText(getActivity().getBaseContext(), "Can't Edit Please try again with a valid number.", Toast.LENGTH_SHORT).show();

                                }

                                //refreshes the page
                                myDialog.dismiss();
                                getFragmentManager().beginTransaction().detach(Search.this).attach(Search.this).commit();
                            }
                        });


                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.show();



                        break;
                    case 1:

                        // delete list item one by one
                        Log.d(TAG, "onMenuItemClick: clicked item " + valeposition);
                        Log.d(TAG, "onMenuItemClick: clicked item " + date);

                        errorDialog("Would you like to delete event : “ " +
                                listArr.get(position).getTitle() + " ”?");
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        searchView = (MaterialSearchView) rootViwe.findViewById(R.id.search_view);

        setHasOptionsMenu(true);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                //creates an instance of the MyDBHandler
               // dbConnection = new DbConnection(getActivity(), null, null, 1);

                adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, arrayList);
                listView.setAdapter(adapter);

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()){
                    List<String> listFound = new ArrayList<String>();
                    for (String item : arrayList){

                        if (item.contains(newText))
                            listFound.add(item);
                    }

                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, listFound);
                    listView.setAdapter(adapter);
                }else {

                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, arrayList);
                    listView.setAdapter(adapter);
                }

                return true;
            }
        });





        return rootViwe;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(menuItem);
        super.onCreateOptionsMenu(menu,inflater);

    }


    public void errorDialog(String error)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity() , R.style.SangeethDialTheme);
        builder.setMessage(error);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity().getBaseContext(), "Deleted the " +
                                valeposition +
                                " appointment.", Toast.LENGTH_SHORT).show();
                        dbConnection.deleteAppointments(date , valeposition);
                        //adapter.notifyDataSetChanged(); //refreshes the list, NOT WORKING
                        dialog.dismiss();

                        //bad way to refresh
                        getFragmentManager().beginTransaction().detach(Search.this).attach(Search.this).commit();

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
}
