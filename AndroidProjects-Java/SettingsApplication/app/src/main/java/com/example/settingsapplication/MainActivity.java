package com.example.settingsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CategoryFragment.myFragmentInterface{
    // current position in settings category
    int currentPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checks to see what orientation we are in before committing to a layout
        Configuration configuration = getResources().getConfiguration();
        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            populateCategory();
            populateProfile();
        } else if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){

        }

    }

    private void populateProfile() {
        // fragment for profile header
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.profileFrame, profileFragment);
        fragmentTransaction.commit();
    }

    private void populateCategory() {
        // fragment commit for settings list
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CategoryFragment categoryFragment = new CategoryFragment();
        fragmentTransaction.replace(R.id.categoryFrame, categoryFragment);
        fragmentTransaction.commit();
    }

    // implemented from CategoryFragment Interface
    @Override
    public void settingChange(int setting) {
        currentPos = setting;
        TextView textView = findViewById(R.id.settingsText);
        //Battery
        if(setting == 0){
            textView.setText("Battery Life: 80%");
        }
        //Storage
        else if(setting == 1){
            textView.setText("System Storage: 20% used");
        }
        //Software Update
        else if(setting == 2){
            textView.setText("Software up to date.");
        }
        // Volume
        else if(setting == 3){
            textView.setText("Volume: Off");
        }
        else textView.setText("Settings");
    }


    public void nextCategoryListener(View view) {
        if(currentPos == 3){
            settingChange(0);
        }
        else settingChange(currentPos + 1);

    }

}
//    // information for saved name and location
//    SharedPreferences myprefs = getSharedPreferences("SharedPref", MODE_PRIVATE);
//    SharedPreferences.Editor editor = myprefs.edit();
//                editor.putString("NAME", name);
//                        editor.putString("LOCATION", password);
//                        editor.commit();