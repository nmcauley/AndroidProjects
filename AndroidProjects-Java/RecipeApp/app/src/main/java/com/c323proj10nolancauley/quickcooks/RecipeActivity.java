package com.c323proj10nolancauley.quickcooks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class RecipeActivity extends AppCompatActivity {
    //obtained from intent in activity 1
    String recipe, recipeName, imagePath;
    String recipeAPI = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
    ProgressDialog progressDialog;

    ArrayAdapter<Adapter> adapter;
    JSONArray jsonArray;
    String jsonData = "";
    ListView list;

    ImageButton addButton;

    //text view to display what category we are on
    TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        //set our category onto our API call and obtain the name
        recipe = intent.getStringExtra("ID");
        recipeAPI = recipeAPI.concat(recipe);
        recipeName = intent.getStringExtra("NAME");

        //add favorite buttone listener and implementation
        setupAddButton();

        retrieveRecipe();
    }

    private void setupAddButton() {
        addButton = findViewById(R.id.addFavButton);
        //listener prompts a dialog which confirms whether the user wants to add the recipe
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(RecipeActivity.this).create();
                alertDialog.setTitle("Confirm Add");
                alertDialog.setMessage("Do you want to add " + recipeName + " to your favorites?");
                //confirm -> DB handler
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                addFav();
                                dialog.dismiss();
                            }
                        });
                //deny -> closes dialog
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    //adds recipe to DB; only the id
    private void addFav(){
        MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
        //add to db
        myDBHandler.addRecipe(recipe, recipeName, imagePath);
        Toast.makeText(this, "Recipe added to favorites", Toast.LENGTH_LONG).show();
    }

    private void retrieveRecipe() {
        progressDialog = ProgressDialog.show(this, recipeName, "Getting everything you need", true, true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //create separate thread and new task
            //executes with concatenated coordinates onto URL
            String destination = recipeAPI;
            new DownloadRecipeDataTask().execute(destination);
        } else {
            Toast.makeText(this, "No network connection available... hope you're not hungry", Toast.LENGTH_LONG).show();
        }
    }
    private class DownloadRecipeDataTask extends AsyncTask<String, Void, String> {
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
        TextView name, originView, cat, instruction;
        ImageView image;
        try {
            JSONObject jsonRootObject = new JSONObject(jsonData);
            jsonArray = jsonRootObject.getJSONArray("meals");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject current = jsonArray.getJSONObject(i);
                // obtain relevant information
                String recipeID = current.getString("idMeal");
                String recipeName = current.getString("strMeal");
                String origin = current.getString("strArea");
                String instructions = current.getString("strInstructions");
                imagePath = current.getString("strMealThumb");
                String category = current.getString("strCategory");

                //places recipe items onto text and image Views
                name = findViewById(R.id.recipeName);
                originView = findViewById(R.id.recipeOriginView);
                cat = findViewById(R.id.recipeCatView);
                instruction = findViewById(R.id.recipeInstructView);
                image = findViewById(R.id.recipeImage);

                name.setText(recipeName);
                originView.setText(origin);
                cat.setText(category);
                instruction.setText(instructions);
                Picasso.get().load(imagePath).into(image);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}