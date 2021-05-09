package com.c323FinalProject.yourname;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class DashboardFragment extends Fragment {
    String[] categories = {"Movies/Tv Series", "Restaurants",  "Books", "Electronics"};
    int[] img = {R.mipmap.tv_category, R.mipmap.restaurants_icon, R.mipmap.book_category, R.mipmap.electronics_category};
    ArrayAdapter<Adapter> adapter;
    ListView list;
    View view;

    //floating action button
    FloatingActionButton catAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        populateList();

        catAdd = view.findViewById(R.id.categoryFloatingButton);
        catAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategoryFavorite();
            }
        });
        return view;
    }

    //displays alert dialog and asks the user to select which item they would like added to database for favorites
    public void addCategoryFavorite(){
        //initialize dialog builder
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(view.getContext());
        builderSingle.setTitle("Add Category");
        //adapter with standard list and then adding our categories to the adapter
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(categories);

        //cancel button
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //adds confirmation and then sends the selected item to the database
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(view.getContext());
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
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
                intent.putExtra("CATEGORY", categories[position]);
                startActivity(intent);
            }
        });
    }

    public class CustomListAdapter extends ArrayAdapter {
        public CustomListAdapter(){
            super(view.getContext(), R.layout.dashboard_category_listview, categories);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.dashboard_category_listview, parent, false);

            ImageView dashboardImage = itemView.findViewById(R.id.dashboardImageView);
            TextView dashboardText = itemView.findViewById(R.id.dashboardTextView);

            String currentText = categories[position];
            int currentImage = img[position];
            dashboardText.setText(currentText);
            dashboardImage.setImageResource(currentImage);

            return itemView;
        }
    }
}