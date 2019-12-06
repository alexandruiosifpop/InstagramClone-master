package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

public class SocialMediaActivity extends AppCompatActivity {

    private TabAdapter mTabAdapter;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media APP");
        mToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(mToolbar);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(mTabAdapter);

        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager, false);

    }

    // create tbe menus
    // by inflating them
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if (item.getItemId() == R.id.postImageItem){
            
            if (Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ){

              // TO IMPLEMENT TAKE PICTURE FEATURE
               //    && checkSelfPermission(Manifest.permission.CAMERA)
           // != PackageManager.PERMISSION_GRANTED)
                
                requestPermissions(new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, 3000);
            }
            else {
                captureImage();
            }
        }

        else if (item.getItemId() ==R.id.logoutUserItem){
            ParseUser.getCurrentUser().logOut();

            // call the finish method to exit the activity
            // to eliminate the activity from the stack
            finish();
            Intent intent = new Intent(SocialMediaActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 3000){
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED){

              // TO IMPLEMENT TAKE PICTURE FEATURE
                    //&&
            //grantResults[1] == PackageManager.PERMISSION_GRANTED

                captureImage();

            }
        }

    }

    private void captureImage() {

    // TO IMPLEMENT TAKE PICTURE FEATURE
        //Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePicture, 0);

        Intent chosePicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(chosePicture, 4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 4000) {
                if (resultCode == RESULT_OK && data != null) {

                    //inseamna ca putem folosi o imagine

                    //Do something with the captured image
                    try {
                        // get the data - the image selected
                        Uri selectedImage = data.getData();
                        //get a bitmap object
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(), selectedImage);

                        // TO UPLOAD THE IMAGE WE DO BELOW

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();

                        // upload to server
                        ParseFile parseFile = new ParseFile("jinars.png", bytes);
                        ParseObject parseObject = new ParseObject("Photos");
                        parseObject.put("pic", parseFile);
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        final ProgressDialog dialog = new ProgressDialog(SocialMediaActivity.this);
                        dialog.setMessage("Loading");
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(SocialMediaActivity.this,
                                            "Image succesfully uploaded", Toast.LENGTH_SHORT,
                                            FancyToast.SUCCESS, false).show();
                                } else {
                                    FancyToast.makeText(SocialMediaActivity.this,
                                            e.getMessage(), Toast.LENGTH_SHORT,
                                            FancyToast.ERROR, false).show();
                                }

                                dialog.dismiss();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }



