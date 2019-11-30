package com.example.instagramclone;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment {

    private ListView usersListView;
    private ArrayList mArrayList;
    private ArrayAdapter<String> mArrayAdapter;
    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        usersListView = view.findViewById(R.id.usersListView);
        mArrayList = new ArrayList();
        //context, layout pentru rand(cum arata fiecare rand) si arrayul care trebuie populat
        mArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mArrayList);


        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                if (e == null){

                    if (users.size() > 0){
                        for (ParseUser user : users){

                            mArrayList.add(user.getUsername());
                        }
                        //set the adapter to make the connection between
                        //the array of users and the list that has to be populated

                        usersListView.setAdapter(mArrayAdapter);
                    }
                }
            }
        });

        return view;
    }

}
