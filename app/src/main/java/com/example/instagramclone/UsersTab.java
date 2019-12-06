package com.example.instagramclone;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
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
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView usersListView;
    private ArrayList<String> mArrayList;
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

        usersListView.setOnItemClickListener(UsersTab.this);
        usersListView.setOnItemLongClickListener(UsersTab.this);


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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getContext(), UsersPosts.class);

        // get the value on the position clicked
        intent.putExtra("username", mArrayList.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // get the users in a query
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", mArrayList.get(position));

        // GET FIRST IN BACKGROUND
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null){

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                    prettyDialog.setTitle(user.getUsername() + "`s Info")
                            .setMessage(user.get("Bio") + "\n"
                            +user.get("Profession"))
                            .setIcon(R.drawable.info_shape)
                            .addButton("OK",
                                    R.color.pdlg_color_white,
                                    R.color.pdlg_color_green,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                        }
                                    }
                                    ).show();


                }
            }
        });


        // return true for the option to work
        return true;
    }
}
