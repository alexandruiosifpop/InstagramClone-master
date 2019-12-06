package com.example.instagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener {

    private ImageView chosenPicture;
    private EditText pictureDescriptionEdt;
    private Button sharePictureBtn;
    private Bitmap recievedImageBitmap;

    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        chosenPicture = view.findViewById(R.id.chosePictureId);
        pictureDescriptionEdt = view.findViewById(R.id.enterDescriptionEdtID);
        sharePictureBtn = view.findViewById(R.id.sharePictureBtn);

        chosenPicture.setOnClickListener(SharePictureTab.this);
        sharePictureBtn.setOnClickListener(SharePictureTab.this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.chosePictureId:
                // verificam daca versiunea este mai mare decat 23 (Marshmallow sau mai sus)
                // pentru ca de de 23 incepand avem RUNTIME PERMISSIONS - (DANGEROUS ONES)

                if (Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){

                    requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},1000);

                    // array of Strings == Array de requesturi
                    //requestPermission primeste un raspuns DA SAU NU
                    // pe care il verificam mai jos cu functia onRequestPermissionsResult
                }
                else {
                    // Daca SDK <23 SAU Userul deja a acceptat requestul

                    getChosenImage();
                }
                break;

                // verificam daca userul a ales o poza
            case R.id.sharePictureBtn:
                if (recievedImageBitmap != null){
                    if (pictureDescriptionEdt.getText().toString().equals("")){
                        FancyToast.makeText(getContext(), "Error: Update the image description", Toast.LENGTH_SHORT,
                                FancyToast.ERROR, false).show();
                    }
                    else {

                        //WE HAVE TO convert the image into an array of bytes - we shrink the file
                        // create an output stream of bytes
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        // compress the image
                        // we NEED the FORMAT, QUALITY and the Stream of bytes
                        recievedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                        // the conversion to ARRAY OF BYTES
                        byte[] bytes = byteArrayOutputStream.toByteArray();

                        ParseFile parseFile = new ParseFile("servs.png",bytes);
                        ParseObject parseObject = new ParseObject("Photos");

                        //upload the picture on a column named "pic"
                        parseObject.put("pic", parseFile);
                        parseObject.put("pic_caption", pictureDescriptionEdt.getText().toString());

                        // specify the user who uploads the picture
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading");
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null){
                                    FancyToast.makeText(getContext(), "Picture uploaded", Toast.LENGTH_SHORT,
                                            FancyToast.SUCCESS, false).show();
                                }
                                else {
                                    FancyToast.makeText(getContext(), "Picture upload failed \n"+ e.getMessage(), Toast.LENGTH_SHORT,
                                            FancyToast.ERROR, false).show();
                                }
                                dialog.dismiss();
                            }
                        });

                    }
                }
                else {
                    FancyToast.makeText(getContext(), "Error: Upload an image first" , Toast.LENGTH_SHORT,
                            FancyToast.ERROR, false).show();
                }

                break;
        }
    }

    private void getChosenImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // startActivityForResult pentru ca vrem ca rezultatul sa fie null sau o imagine
        // + unique code request
        startActivityForResult(intent, 2000);

        // OnActivityResult este functia care ne da rezultatul
        // o folosim mai jos

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // verificam daca request codul este corect
        if (requestCode == 1000){

            // grant request [0] pentru ca este primul request necesar din lista
            // atribuita functiei requestPermissions

            if (grantResults.length >0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
            {
                getChosenImage();
            }
        }
    }

    // rezultatul la startActivityForResult(intent, 2000);
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // verificam request code-ul
        if (requestCode == 2000){
            if (resultCode == Activity.RESULT_OK){

                //inseamna ca putem folosi o imagine

                //FOLOSIM METODA ASTA CAND VREM SA ASIGNAM IMAGINEA
                //DACA DOAR ALEGEM O IMAGINE AVEM SOLUTIA URMATOARE

//                Uri selectedImage = data.getData();
//                //get a bitmap object
//                Bitmap bitmap = MediaStore.Images.Media
//                        .getBitmap(this.getContentResolver(), selectedImage);

                //Do something with the captured image
              try {
                  // get the data - the image selected
                  Uri selectedImage = data.getData();
                  // String array for the file path
                  String[] filePathColumn = {MediaStore.Images.Media.DATA};
                  // We are inside a fragment so we have to call the getACTIVITY
                  // query(the image, a projection which is the path and the rest are null)
                  Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                  cursor.moveToFirst();
                  // mergem la primul obiect

                  // get the index of the first file of the path
                  int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                  // this is the path of our actual image
                  String picturePath = cursor.getString(columnIndex);
                  cursor.close();
                  // we close the cursor because we don`t need it anymore.
                  // we have the image path

                  // Acum decodam imaginea.
                  // Convertim din String de BYTES in BITMAP
                  recievedImageBitmap = BitmapFactory.decodeFile(picturePath);

                  chosenPicture.setImageBitmap(recievedImageBitmap);
              }
              catch (Exception e){
                  e.printStackTrace();
              }
            }
        }
    }
}
