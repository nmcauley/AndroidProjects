package com.c323FinalProject.yourname;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String name, email;
    private Bitmap picture;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    ImageView profilePicture;
    TextView navName, navEmail;
    //camera-nav drawer stuff
    boolean hasImg = true;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1889;
    private static final int CAPTURE__PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        //obtain name and email for nav drawer personalization
        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        email = intent.getStringExtra("EMAIL");
        String imgPath = intent.getStringExtra("IMAGE");
        processImage(imgPath);
        //nav drawer setup and view initialization
        initializeViews();
        toggleDrawer();
    }

    //decodes the Base64 into a usable Bitmap
    private void processImage(String imgPath) {
        if(!imgPath.equals("")){
            byte[] decodedByte = Base64.decode(imgPath, 0);
            picture = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        }else{
            hasImg = false;
        }
    }

    public void initializeViews(){
        //toolbar
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("ReviewHub");
        setSupportActionBar(toolbar);
        //nav drawer
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);

        //need to work within right view
        View header = navigationView.getHeaderView(0);
        //user personalization
        profilePicture = header.findViewById(R.id.profilePicture);
        navName = header.findViewById(R.id.navName);
        navEmail = header.findViewById(R.id.navEmail);
        navName.setText(name);
        navEmail.setText(email);
        //sets user img or just uses stock photo
        Picasso.get().load(R.mipmap.default_profile_picture).into(profilePicture);
        if(hasImg){
            Picasso.get().load(R.mipmap.default_profile_picture).into(profilePicture);
        }
    }

    //nav functionality
    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboardMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new DashboardFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.favoriteMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new FavoritesFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.trashMenu:

        }
        return true;
    }
    //Checks if the navigation drawer is open - if so, close it
    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //capture image and process it through onActivityResult
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void takeNavPhoto(View view){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAPTURE__PERMISSION_CODE);
        }
        else
        {
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
                String imgPath = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
                //save the new image to our shared pref
                SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserImage", imgPath);
                editor.commit();
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
                profilePicture.setImageBitmap(bmp);
            }
        }
    }
}