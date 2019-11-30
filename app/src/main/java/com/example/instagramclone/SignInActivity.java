package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootLayourLogIn;
    private Button signInBtn;
    private EditText usernameSignIn, passowrdSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("SIGN IN");

        signInBtn = findViewById(R.id.signInActivityBtn);
        usernameSignIn = findViewById(R.id.usernameSignIn);
        passowrdSignIn = findViewById(R.id.passwordSignIn);
        rootLayourLogIn = findViewById(R.id.rootLayoutLogIn);

        signInBtn.setOnClickListener(SignInActivity.this);
        rootLayourLogIn.setOnClickListener(SignInActivity.this);

        if (ParseUser.getCurrentUser() != null) ParseUser.getCurrentUser().logOut();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.signInActivityBtn:

            ParseUser.logInInBackground(usernameSignIn.getText().toString(), passowrdSignIn.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && e == null) {
                        FancyToast.makeText(SignInActivity.this,
                                user.getUsername() + " has loged in",
                                Toast.LENGTH_LONG, FancyToast.SUCCESS,
                                true).show();
                        transitionToSocialMediaActivity();
                    } else {
                        FancyToast.makeText(SignInActivity.this,
                                "ERROR: " + e.getMessage(),
                                Toast.LENGTH_LONG, FancyToast.ERROR,
                                true).show();
                    }
                }
            });
            break;

            case R.id.rootLayoutLogIn:

                //Cand userul da click oriunde inafara de tastatura,
                //tastatura dispare
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(SignInActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
