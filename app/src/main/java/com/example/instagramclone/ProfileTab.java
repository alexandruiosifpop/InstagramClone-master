package com.example.instagramclone;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText profileNameEdit, bioEdit, professionEdit, hobbieEdit, favSportEdit;
    private Button updateProfileBtn;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        profileNameEdit = view.findViewById(R.id.profileNameEditId);
        bioEdit = view.findViewById(R.id.bioEditId);
        professionEdit = view.findViewById(R.id.professionEditId);
        hobbieEdit = view.findViewById(R.id.hobbieEditId);
        favSportEdit = view.findViewById(R.id.favSportEditId);
        updateProfileBtn = view.findViewById(R.id.updateProfileId);


        //verificam daca avem profilul updatat
        final ParseUser parseUser = ParseUser.getCurrentUser();

        if (parseUser.get("ProfileName") == null){
            profileNameEdit.setText("");
        }else {
            parseUser.get("ProfileName").toString();
        }

        if (parseUser.get("Bio") == null){
            bioEdit.setText("");
        }else {
            parseUser.get("Bio").toString();
        }

        if (parseUser.get("Profession") == null){
            professionEdit.setText("");
        }else {
            parseUser.get("Profession").toString();
        }

        if (parseUser.get("Hobbie") == null){
            hobbieEdit.setText("");
        }else {
            parseUser.get("Hobbie").toString();
        }

        if (parseUser.get("FavoriteSport") == null){
            favSportEdit.setText("");
        }else {
            parseUser.get("FavoriteSport").toString();
        }

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseUser.put("ProfileName", profileNameEdit.getText().toString());
                parseUser.put("Bio", bioEdit.getText().toString());
                parseUser.put("Profession", professionEdit.getText().toString());
                parseUser.put("Hobbie", hobbieEdit.getText().toString());
                parseUser.put("FavoriteSport", favSportEdit.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(getContext(),
                                    "Profile is updated",
                                    Toast.LENGTH_LONG,
                                    FancyToast.INFO,
                                    false).show();
                        }
                        else {
                            FancyToast.makeText(getContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_LONG,
                                    FancyToast.ERROR,
                                    false).show();
                        }
                    }
                });
            }
        });

        return view;
    }

}
