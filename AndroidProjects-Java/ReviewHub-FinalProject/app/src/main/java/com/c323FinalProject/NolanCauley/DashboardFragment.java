package com.c323FinalProject.NolanCauley;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


public class DashboardFragment extends Fragment {
    //room db
    Database myDb;
    List<TableCategory> tableCategories = new ArrayList<>();

    String[] categories = {"Movies/Tv Series", "Restaurants",  "Books", "Electronics"};
    int[] img = {R.mipmap.tv_category, R.mipmap.restaurants_icon, R.mipmap.book_category, R.mipmap.electronics_category};
    ArrayAdapter<Adapter> adapter;
    ListView list;
    View view;

    //floating action button
    FloatingActionButton catAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = Database.getInstance(getContext());
        prepopulateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        catAdd = view.findViewById(R.id.categoryFloatingButton);
        catAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCategory();
            }
        });

        populateList();
        return view;
    }

    private void prepopulateList() {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //if this is our first time(no categories) then we initialize it with our starter categories
                tableCategories = myDb.tableCategoryDao().getCategoryList();
                Log.i("CATEGORY", tableCategories.size() + "");
                if(tableCategories.isEmpty()){
                    for(int i = 0; i < categories.length; i++){
                        //need to decode resource into bitmap and then into bytearray to store into our DB
                        Bitmap currentImage = BitmapFactory.decodeResource(getResources(), img[i]);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        currentImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        TableCategory tableCategory = new TableCategory(categories[i], byteArray);
                        myDb.tableCategoryDao().insertCategory(tableCategory);
                    }
                }
            }
        });
    }

    private void populateList() {
        list = view.findViewById(R.id.dashboardListView);
        adapter = new CustomListAdapter();
        list.setAdapter(adapter);
        //onclick for item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ReviewListActivity.class);
                intent.putExtra("CATEGORY", tableCategories.get(position).CategoryName);
                startActivity(intent);
            }
        });
    }

    //displays alert dialog and asks the user to select which item they would like added to database for favorites
    public void addNewCategory(){
        AddNewCategoryFragment dialog = new AddNewCategoryFragment();
        dialog.show(getFragmentManager(), "Add New Category");
        adapter.notifyDataSetChanged();
    }

    public class CustomListAdapter extends ArrayAdapter {
        public CustomListAdapter(){
            super(view.getContext(), R.layout.dashboard_category_listview, tableCategories);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.dashboard_category_listview, parent, false);

            ImageView dashboardImage = itemView.findViewById(R.id.reviewListImage);
            TextView dashboardText = itemView.findViewById(R.id.reviewListName);

            TableCategory current = tableCategories.get(position);
            String currentText = current.CategoryName;
            byte[] currentImage = current.Image;
            //need to decode the image
            if(currentImage != null){
            Bitmap decodedImage = BitmapFactory.decodeByteArray(currentImage, 0,
                    currentImage.length);
                dashboardImage.setImageBitmap(decodedImage);
            }
            dashboardText.setText(currentText);

            return itemView;
        }
    }
}