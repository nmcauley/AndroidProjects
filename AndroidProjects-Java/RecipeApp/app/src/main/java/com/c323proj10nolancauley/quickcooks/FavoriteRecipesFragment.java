package com.c323proj10nolancauley.quickcooks;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteRecipesFragment extends DialogFragment {
    View view;
    ArrayList<Meal> recipes = new ArrayList<>();
    ListView list;
    ArrayAdapter<Adapter> adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //builder -> inflate -> obtain favorites -> list adapter -> return
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_favorite_layout, null);
        builder.setView(view);

        obtainFavorites();

        return builder.create();
    }

    private void obtainFavorites() {
        MyDBHandler dbHandler = new MyDBHandler(getActivity(), null, null, 1);
        Cursor cursor = dbHandler.getAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "No Favorites", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(1);
                    String name = cursor.getString(2);
                    String path = cursor.getString(3);
                    Meal recipe = new Meal(id, name, path);
                    recipes.add(recipe);
                }while (cursor.moveToNext());
            }
            //send list through adapter for view
            Intent intent = new Intent(view.getContext(), RecipeActivity.class);
            adapter = new CustomListAdapter();
            list = view.findViewById(R.id.favoriteList);
            list.setAdapter(adapter);
            //onclick for item click
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    //obtain category name to pass onto second activity to be used for making second API call
                    intent.putExtra("ID", recipes.get(position).id);
                    intent.putExtra("NAME", recipes.get(position).name);
                    startActivity(intent);
                }
            });
        }
    }
    public class CustomListAdapter extends ArrayAdapter {
        public CustomListAdapter(){
            super(view.getContext(), R.layout.category_list_layout, recipes);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.category_list_layout, parent, false);

            ImageView poster = itemView.findViewById(R.id.catIcon);
            TextView title = itemView.findViewById(R.id.catTitle);

            //obtain current item
            Meal current = recipes.get(position);

            title.setText(current.name);
            //uses picasso to display poster onto ImageView
            Picasso.get().load(current.path).into(poster);

            return itemView;
        }
    }
}
