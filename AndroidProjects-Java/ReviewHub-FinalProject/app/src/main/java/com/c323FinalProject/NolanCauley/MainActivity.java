package com.c323FinalProject.NolanCauley;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c323FinalProject.NolanCauley.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    TextView nameView, emailView;
    ImageView profileImage;
    boolean hasUploaded = false;

    //path for profile img will be encoded and decoded
    Bitmap capturedImage;
    String imgPath = "";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private static final int CAPTURE__PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //as soon as app is opened we need to check if a login already exists in our shared preferences
        preferences = getPreferences(Context.MODE_PRIVATE);
        checkLoginState();
    }

    //obtains SharedPref and looks if "isUserLogin" exists from obtained pref
    // and if so then we can proceed to the navigation drawer activity
    private void checkLoginState() {
        if(preferences.contains("isUserLogin")){
            //Obtain name and email to pass as extras
            String name = preferences.getString("UserName", "");
            String email = preferences.getString("UserEmail", "");
            imgPath = preferences.getString("UserImage", "");
            Intent intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
            intent.putExtra("NAME", name);
            intent.putExtra("EMAIL", email);
            intent.putExtra("IMAGE", imgPath);
            startActivity(intent);
        }else{
            //initialize our views and proceed as usual
            initializeViews();
        }
    }

    private void initializeViews() {
        nameView = findViewById(R.id.loginName);
        emailView = findViewById(R.id.loginEmail);
        profileImage = findViewById(R.id.loginProfileImage);
        Picasso.get().load(R.mipmap.default_profile_picture).into(profileImage);
    }

    //login callback/ puts info in sharedPref and sets "isUserLogin" to true
    public void LoginCallback(View view) {
        //pull info from textviews
        String name = nameView.getText().toString();
        String email = emailView.getText().toString();

        //store information
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUserLogin", true);
        editor.putString("UserName", name);
        editor.putString("UserEmail", email);
        //checks if user has updated profile picture; if so, saves and sends
        if(hasUploaded){
            editor.putString("UserImage", imgPath);
        }
        editor.commit();
        Toast.makeText(this, "Successful login/account creation", Toast.LENGTH_LONG).show();

        //proceed to next activity sends name and email
        Intent intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
        intent.putExtra("NAME", name);
        intent.putExtra("EMAIL", email);
        intent.putExtra("IMAGE", imgPath);
        startActivity(intent);
    }


    //capture image and process it through onActivityResult
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void takePhoto(View view){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAPTURE__PERMISSION_CODE);

        }else{
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //byte array encoded and saved
                imgPath = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
                // convert byte array to Bitmap
                capturedImage = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                profileImage.setImageBitmap(capturedImage);
                //notify we have a new image
                hasUploaded = true;
                Toast.makeText(this, "Profile image saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}