package com.example.settingsapplication;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment{
    CategoryFragment.myFragmentInterface activityCommunicator;
    View view;
    EditText name, location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.profile_layout, container, false);
        name = view.findViewById(R.id.nameEditText);
        location = view.findViewById(R.id.locationEditText);

        //edit button listener.
        ImageButton editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(true);
                location.setEnabled(true);
                name.setFocusableInTouchMode(true);
                location.setFocusableInTouchMode(true);
            }
        });

        //save button listener
        ImageButton saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(false);
                location.setEnabled(false);
                name.setFocusableInTouchMode(false);
                location.setFocusableInTouchMode(false);
            }
        });
        return view;
    }
}
