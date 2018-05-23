package com.example.sangeeth.appoinmentcal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    Button but1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViwe =  inflater.inflate(R.layout.fragment_profile, container, false);

        but1 = (Button) rootViwe.findViewById(R.id.but1);


        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return rootViwe;

    }

}
