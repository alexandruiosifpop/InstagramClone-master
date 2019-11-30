package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private String allUsers;
    private Button signUpBtn, signInBtn;
    private TextView termsConds;
    private EditText emailEdt, usernameEdt, passwordEdt;
    private View rootLayoutMainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("SIGN UP");

        rootLayoutMainActivity = findViewById(R.id.rootLayoutId);
        signUpBtn = (Button) findViewById(R.id.signUpBtnId);
        signInBtn = (Button) findViewById(R.id.signInBtnId);
        termsConds = (TextView) findViewById(R.id.termsConditionsId);
        emailEdt = (EditText) findViewById(R.id.emailId);
        passwordEdt = (EditText) findViewById(R.id.passwordId);
        usernameEdt = findViewById(R.id.usernameId);

        rootLayoutMainActivity.setOnClickListener(MainActivity.this);
        signUpBtn.setOnClickListener(MainActivity.this);
        signInBtn.setOnClickListener(MainActivity.this);
        termsConds.setOnClickListener(MainActivity.this);

        // Save the current Installation to Back4App
        //every time a user downloads the app this method is called
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Feature: cand userul apasa pe enter functioneaza ca si butonul SIGN UP
        passwordEdt.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                event.getAction() == KeyEvent.ACTION_DOWN)
                {
                onClick(signUpBtn);
                }

                return false;
            }
        });

        //daca avem un user logat, in delogam
        if (ParseUser.getCurrentUser() != null){
           // ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }

    }

    @Override
    public void onClick(View v) {
        allUsers = "";
        switch (v.getId()) {

            //Cand userul da click oriunde inafara de tastatura,
            //tastatura dispare
            case R.id.rootLayoutId:
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case R.id.signUpBtnId:
                if (emailEdt.getText().toString().equals("")){
                    Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if (usernameEdt.getText().toString().equals("")){
                    Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
                }
                else if (passwordEdt.getText().toString().equals("")){
                    Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else {

                    try {
                        final ParseUser users = new ParseUser();
                        users.setEmail(emailEdt.getText().toString());
                        users.setPassword(passwordEdt.getText().toString());
                        users.setUsername(usernameEdt.getText().toString());

                        final ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Signing up " + usernameEdt.getText().toString());
                        progressDialog.show();

                        users.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(MainActivity.this,
                                            users.getUsername() + " is signed up",
                                            Toast.LENGTH_LONG,
                                            FancyToast.SUCCESS,
                                            false).show();
                                    transitionToSocialMediaActivity();
                                } else {
                                    FancyToast.makeText(MainActivity.this,
                                            "ERROR: " + e.getMessage(),
                                            Toast.LENGTH_LONG,
                                            FancyToast.ERROR,
                                            false).show();
                                }

                                progressDialog.dismiss();
                            }

                        });

                    } catch (Exception e) {
                        FancyToast.makeText(MainActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG,
                                FancyToast.ERROR,
                                false)
                                .show();
                    }
                }

            break;
            case R.id.signInBtnId:
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
                break;

            case R.id.termsConditionsId:
                try{

                    //get more info from a field
                    //use getInBackground in you want a specific user --- you need to have an ID
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Users") ;
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null){
                            if (objects != null){

                                for (ParseObject parseObject : objects) {
                                    allUsers += parseObject.get("PhoneNo") + "\n";
                                }
                                termsConds.setText(allUsers);
                            }
                        }
                    }
                });

        }catch (Exception e){
                    FancyToast.makeText(MainActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false)
                            .show();
                }
        }

    }

    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(MainActivity.this, SocialMediaActivity.class);
        startActivity(intent);

    }
}
