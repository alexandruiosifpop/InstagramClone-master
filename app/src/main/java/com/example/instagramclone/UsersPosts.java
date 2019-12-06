package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UsersPosts extends AppCompatActivity {

    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        mLinearLayout = findViewById(R.id.linearLayoutUsersPosts);

        // get the object of type intent passed to this activity
        Intent receivedObjectIntent = getIntent();

        // get the name of the clicked object
        String recievedUsername = receivedObjectIntent.getStringExtra("username");

        setTitle(recievedUsername +"`s posts");

        // query the photos from photos class by username in ordine descrescatoare
        // a datei in care poza a fost postata
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Photos");
        query.whereEqualTo("username", recievedUsername);
        query.orderByDescending("createdAt");

       final ProgressDialog dialog = new ProgressDialog(UsersPosts.this);
        dialog.setMessage("Loading");
        dialog.show();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size()> 0 && e == null){

                    for (final ParseObject post: objects){

                        // create a text view
                        final TextView postDescription = new TextView(UsersPosts.this);

                        if (post.get("pic_caption") == null){
                            postDescription.setAlpha(0);
                        }else{
                        postDescription.setText(post.get("pic_caption").toString());
                           // Toast.makeText(UsersPosts.this, postDescription.getText(), Toast.LENGTH_SHORT).show();
                    }
                        final ParseFile parseImage = (ParseFile) post.get("pic");

                        parseImage.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if (data != null && e == null){

                                    // convert the image from byteArray to Bitmap
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    // Create a ImageView Layout to hold our Bitmap image
                                    ImageView postImageView = new ImageView(UsersPosts.this);
                                    // specify the width and height of image view in the linear layout
                                    LinearLayout.LayoutParams imageView_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);

                                    // margins
                                    imageView_params.setMargins(8, 8, 8, 8);
                                    // setam parametrii PT ImageView
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                                    //assign the bitmap image
                                    postImageView.setImageBitmap(bitmap);

                                    // Create a TextView Layout for the description

                                    LinearLayout.LayoutParams descriptionText_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    descriptionText_params.setMargins(8, 8, 8, 8);
                                    postDescription.setLayoutParams(descriptionText_params);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setTextColor(Color.BLACK);
                                    postDescription.setTextSize(24f);

                                    // Add this 2 views to the parent LinearLayout
                                    mLinearLayout.addView(postImageView);
                                    mLinearLayout.addView(postDescription);

                                }
                                else {
                                    e.getMessage();
                                }
                            }
                        });

                    }
                }
                else
                {
                finish();
                }
                dialog.dismiss();
            }
        });
    }
}
