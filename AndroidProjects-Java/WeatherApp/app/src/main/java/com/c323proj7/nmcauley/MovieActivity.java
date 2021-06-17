package com.c323proj7.nmcauley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MovieActivity extends AppCompatActivity {
    String apiKey = "b7c5fa86fc6d59ea16fd2977ce78b0aa";
    String movieListURL = "https://api.themoviedb.org/3/discover/movie?api_key=b7c5fa86fc6d59ea16fd2977ce78b0aa&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false& page=1";

    ArrayAdapter<Adapter> adapter;
    JSONArray jsonArray;
    String jsonData = "";
    ProgressDialog progressDialog;
    ListView list;

    //poster link, title, Language, genre ID, Release date, and Description stored in Movie object stored in Array List
    ArrayList<Movie> movieList = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        list = findViewById(R.id.movieList);
        retrieveMovieData();
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
                //does nothing since we are already at MovieActivity
                return true;
            case R.id.weather:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //----------------------------------------------------------------------------------------------
    //retrieving data for the current lat/long
    public void retrieveMovieData() {

        progressDialog = ProgressDialog.show(this, "Movie Data", "Going to the Movies...", true, true);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //create separate thread and new task
            //executes with concatenated coordinates onto URL
            String destination = movieListURL;
            Log.i("PATH", destination);
            new DownloadMovieDataTask().execute(destination);
        } else {
            Toast.makeText(this, "No network connection available; Maybe watch a movie in the meantime", Toast.LENGTH_LONG).show();
        }
    }

    private class DownloadMovieDataTask extends AsyncTask<String, Void, String>{

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
            jsonArray = jsonRootObject.getJSONArray("results");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject current = jsonArray.getJSONObject(i);
                //poster link, title, genre ID, Release date, and Description stored in Movie object stored in Array List
                String posterLink = current.optString("poster_path");
                String title = current.getString("title");
                //obtains first genre in the JSONArray
                int genreID = current.getJSONArray("genre_ids").getInt(0);
                String description = current.getString("overview");
                String release = current.optString("release_date");
                String language = current.optString("original_language");

                //create movie object
                Movie movie = new Movie(posterLink, title, language, genreID, description, release);
                movieList.add(movie);
            }
            populateList();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //populates the list with the information
    private void populateList() {
        Intent intent = new Intent(this, IndividualMovieActivity.class);
        adapter = new CustomListAdapter();
        list.setAdapter(adapter);
        //onclick for item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                intent.putExtra("LINK", movieList.get(position).link);
                intent.putExtra("TITLE", movieList.get(position).movieTitle);
                intent.putExtra("LANGUAGE", movieList.get(position).language);
                intent.putExtra("GENRE", movieList.get(position).gID);
                intent.putExtra("RELEASE", movieList.get(position).release_date);
                intent.putExtra("DESCRIPTION", movieList.get(position).overview);
                startActivity(intent);
            }
        });
    }

    public class CustomListAdapter extends ArrayAdapter {
        public CustomListAdapter(){
            super(MovieActivity.this, R.layout.movie_list_layout, movieList);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.movie_list_layout, parent, false);

            ImageView poster = itemView.findViewById(R.id.moviePoster);
            TextView title = itemView.findViewById(R.id.movieTitle);

            //obtain current item
            Movie current = movieList.get(position);

            String iconUrl = "https://image.tmdb.org/t/p/w500/" + current.link;
            title.setText(current.movieTitle);
            //uses picasso to display poster onto ImageView
            Picasso.get().load(iconUrl).into(poster);

            return itemView;
        }
    }

}
