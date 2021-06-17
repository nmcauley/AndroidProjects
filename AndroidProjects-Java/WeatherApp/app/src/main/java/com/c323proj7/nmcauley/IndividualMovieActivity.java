package com.c323proj7.nmcauley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IndividualMovieActivity extends AppCompatActivity {

    String mapAPI = "https://api.themoviedb.org/3/genre/movie/list?api_key=b7c5fa86fc6d59ea16fd2977ce78b0aa&language=en-US";

    ImageView posterView;
    TextView titleView, releaseDateView, languageView, genreView, descriptionView;
    Intent intent;
    ProgressDialog progressDialog;
    String jsonData = "";
    JSONArray jsonArray;

    // genre id and corresponding classification stored in Hashmap
    HashMap<Integer, String> genreMap = new HashMap<Integer, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_movie);

        //set views
        posterView = findViewById(R.id.posterView);
        titleView = findViewById(R.id.titleDisplay);
        releaseDateView = findViewById(R.id.releaseDisplay);
        languageView = findViewById(R.id.languageDisplay);
        genreView = findViewById(R.id.genreDisplay);
        descriptionView = findViewById(R.id.descriptionDisplay);

        //fills views
        intent = getIntent();
        //obtain hashMap
        retrieveMovieData();


    }

    private void populateDisplay() {
        intent.getStringExtra("LANGUAGE");
        intent.getIntExtra("GENRE", 0);
        intent.getStringExtra("RELEASE");
        intent.getStringExtra("DESCRIPTION");

        String iconUrl = "https://image.tmdb.org/t/p/w500/" + intent.getStringExtra("LINK");
        //SET POSTER
        Picasso.get().load(iconUrl).into(posterView);
        titleView.setText(intent.getStringExtra("TITLE"));
        releaseDateView.setText("Release Date: " + intent.getStringExtra("RELEASE"));
        languageView.setText("Language: " + intent.getStringExtra("LANGUAGE"));

        //pass through hashMap to obtain classification
        int id = intent.getIntExtra("GENRE", 0);
        Log.i("ID", "" + id);
        String genre = genreMap.get(id);
        Log.i("FINAL_CLASS", String.valueOf(genreMap.size()));
        genreView.setText("Genre: " + genre);
        descriptionView.setText("Overview: " + intent.getStringExtra("DESCRIPTION"));

    }

    //----------------------------------------------------------------------------------------------
    // Overflow menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id) {
            case R.id.movies:
                intent = new Intent(this, MovieActivity.class);
                startActivity(intent);
                return true;
            case R.id.weather:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void retrieveMovieData() {

        progressDialog = ProgressDialog.show(this, "Movie Data", "Unboxing Film...", true, true);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //create separate thread and new task
            //executes with concatenated coordinates onto URL
            Log.i("PATH", mapAPI);
            new DownloadMovieDataTask().execute(mapAPI);
        } else {
            Toast.makeText(this, "No network connection available; Maybe watch a movie in the meantime", Toast.LENGTH_LONG).show();
        }
    }


    private class DownloadMovieDataTask extends AsyncTask<String, Void, String> {

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
            Log.i("JSON", jsonData);
            parseData();
        }
    }

    //adapts information from jsonData to be displayed onto the screen.
    private void parseData() {
        try {
            JSONObject jsonRootObject = new JSONObject(jsonData);
            jsonArray = jsonRootObject.getJSONArray("genres");
//            Log.i("JSON_OBJECT", jsonArray.getJSONObject(0).toString());
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject current = jsonArray.getJSONObject(i);
                String classification = current.getString("name");
                int key = current.getInt("id");

                genreMap.put(key, classification);
            }
            populateDisplay();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}