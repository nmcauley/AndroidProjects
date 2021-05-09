package com.c323FinalProject.NolanCauley;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddNewCategoryFragment extends DialogFragment {
    View view;
    Database myDb;

    EditText catName;
    ImageView catImage;
    ImageButton cameraButton;
    byte[] byteArray;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1889;
    private static final int CAPTURE__PERMISSION_CODE = 100;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //builder -> inflate -> obtain favorites -> list adapter -> return
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //obtain db instance
        myDb = Database.getInstance(getContext());

        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_new_category, null);
        builder.setView(view)
        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createFavorite();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        initializeViews();
        return builder.create();
    }

    private void initializeViews() {
        catName = view.findViewById(R.id.newCatName);
        catImage = view.findViewById(R.id.newCatPictureFrame);
        cameraButton = view.findViewById(R.id.reviewListCameraButton);
        //set on click to take photo; similar to other activities
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
    }

    private void createFavorite() {
        String CategoryName = catName.getText().toString();

        final TableCategory tableCategory = new TableCategory(CategoryName, byteArray);
        //after everything is complete we obtain information create new Category object and pass it through
        //execute
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                myDb.tableCategoryDao().insertCategory(tableCategory);
            }
        });
        Toast.makeText(view.getContext(), CategoryName + " successfully added", Toast.LENGTH_SHORT).show();
    }



    //capture image and process it through onActivityResult
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void takePhoto(){
        if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
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
                byteArray = stream.toByteArray();

                //byte array encoded and saved
                String imgPath = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
                catImage.setImageBitmap(bmp);
            }
        }
    }
}