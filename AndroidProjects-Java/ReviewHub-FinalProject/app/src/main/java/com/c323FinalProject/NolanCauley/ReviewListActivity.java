package com.c323FinalProject.NolanCauley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity {

    //room db
    Database myDb;
    List<TableReviewList> tableReviewList = new ArrayList<>();

    ArrayAdapter<Adapter> adapter;
    ListView list;
    View view;

    //floating action button
    FloatingActionButton catAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = Database.getInstance(this);
        prepopulateList();

        populateList();
    }

    private void prepopulateList() {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //if this is our first time(no categories) then we initialize it with our starter categories
                tableReviewList = myDb.tableReviewListDao().getReviewList();
            }
        });
    }

    private void populateList() {
        list = view.findViewById(R.id.reviewListView);
        adapter = new CustomListAdapter();
        list.setAdapter(adapter);
        //onclick for item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ReviewListActivity.class);
                intent.putExtra("Review", tableReviewList.get(position).CategoryId);
                startActivity(intent);
            }
        });
    }

    //displays alert dialog and asks the user to select which item they would like added to database for favorites
    public void addNewCategory(){
//        AddNewCategoryFragment dialog = new AddNewCategoryFragment();
//        dialog.show(getFragmentManager(), "Add New Category");
//        adapter.notifyDataSetChanged();
    }

    public class CustomListAdapter extends ArrayAdapter {
        public CustomListAdapter(){
            super(view.getContext(), R.layout.dashboard_category_listview, tableReviewList);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.layout_reviewlist, parent, false);

            ImageView reviewListImage = itemView.findViewById(R.id.reviewListImage);
            TextView reviewListText = itemView.findViewById(R.id.reviewListName);

            TableReviewList current = tableReviewList.get(position);
            String currentText = current.ReviewName;
            byte[] currentImage = current.Image;
            //need to decode the image
            if(currentImage != null){
                Bitmap decodedImage = BitmapFactory.decodeByteArray(currentImage, 0,
                        currentImage.length);
                reviewListImage.setImageBitmap(decodedImage);
            }
            reviewListText.setText(currentText);

            return itemView;
        }
    }
}