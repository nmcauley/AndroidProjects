package com.c323proj10nolancauley.quickcooks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    View view;
    ArrayList<RecipeCategory> categoryList = new ArrayList();

    String apiKey = "1d06d68f33mshaadef849c92d6f7p13e780jsne21b24d379f7";
    String categoryListURL = "https://www.themealdb.com/api/json/v1/1/categories.php";

    ArrayAdapter<Adapter> adapter;
    JSONArray jsonArray;
    String jsonData = "";
    ProgressDialog progressDialog;
    ListView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category_layout,container,false);
        retrieveCategories();

        //list for API call
        list = view.findViewById(R.id.recipeList);

        return view;
    }


    //make api call and retrieve info
    private void retrieveCategories() {
        progressDialog = ProgressDialog.show(view.getContext(), "Meal Categories", "Getting the cookbooks...", true, true);
        ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //create separate thread and new task
            //executes with concatenated coordinates onto URL
            String destination = categoryListURL;
            new DownloadCategoryDataTask().execute(destination);
        } else {
            Toast.makeText(view.getContext(), "No network connection available... hope you're not hungry", Toast.LENGTH_LONG).show();
        }
    }

    //to make api call for categories
    private class DownloadCategoryDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            jsonData = downloadFromURL(urls[0]);
            return jsonData;
        }

        private String downloadFromURL(String url) {
            InputStream is = null;
            StringBuffer result = new StringBuffer();
            URL myUrl = null;
            try {
                myUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK){
                    throw new IOException("HTTP error: "  + responseCode);
                }
                is = connection.getInputStream();
                progressDialog.dismiss();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result.append(line);
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseData();
        }
    }
    //adapts information from jsonData to be displayed onto the screen.
    private void parseData() {
        try {
            JSONObject jsonRootObject = new JSONObject(jsonData);
            jsonArray = jsonRootObject.getJSONArray("categories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject current = jsonArray.getJSONObject(i);
                //each object has their id , name, and image path taken -> made into an object -> added to array
                String catId = current.getString("idCategory");
                String title = current.getString("strCategory");
                String catImgLink = current.optString("strCategoryThumb");


                //create movie object
                RecipeCategory recipe = new RecipeCategory(catId, title, catImgLink);
                categoryList.add(recipe);
            }
            populateList();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //adapt our list of categories which will then be funnelled through our custom list adapter
    private void populateList() {
        Intent intent = new Intent(view.getContext(), SelectedCategoryActivity.class);
        adapter = new CustomListAdapter();
        list.setAdapter(adapter);
        //onclick for item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //obtain category name to pass onto second activity to be used for making second API call
                intent.putExtra("CATEGORY", categoryList.get(position).name);
                startActivity(intent);
            }
        });
    }

    public class CustomListAdapter extends ArrayAdapter {
        public CustomListAdapter(){
            super(view.getContext(), R.layout.category_list_layout, categoryList);
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
            RecipeCategory current = categoryList.get(position);

            title.setText(current.name);
            //uses picasso to display poster onto ImageView
            Picasso.get().load(current.imagePath).into(poster);

            return itemView;
        }
    }
}
