package com.c323proj7.nmcauley;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String apiKey = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid=19659b826c4a8eaaef1ad02d7b8b4cb8";
    LocationManager locationManager;
    Geocoder geocoder;
    LocationListener locationListener;
    TextView gps, todayWeather, weatherDescription, temperature, realFeel;
    ImageButton getData;
    EditText enterLong, enterLat;
    //stores x and y coordinates to be used in retrieve data
    // [0] = latitude [1] = longitude
    float[] coord;

    //for picture (using picasso)
    ImageView pictureFrame;

    String jsonData = "";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData = findViewById(R.id.getDataButton);
        pictureFrame = findViewById(R.id.pictureFrame);
        //user input
        enterLong = findViewById(R.id.enterLong);
        enterLat = findViewById(R.id.enterLat);

        //tag all of the textViews for output data
        todayWeather = findViewById(R.id.todayWeather);
        weatherDescription = findViewById(R.id.weatherDescription);
        temperature = findViewById(R.id.weatherTemp);
        realFeel = findViewById(R.id.weatherFeel);

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
        switch(id){
            case R.id.movies:
                intent = new Intent(this, MovieActivity.class);
                startActivity(intent);
                return true;
            case R.id.weather:
                //does nothing since we are already at MainActivity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//----------------------------------------------------------------------------------------------
    // Data handling & Async Task Class

    //retrieving data for the current lat/long
    public void retrieveData(View view) {
        //checks if user has input any coordinates. in that case it is prioritized
        if(!enterLong.getText().toString().isEmpty() && !enterLat.getText().toString().isEmpty()){
            coord = new float[2];
            coord[0] = Float.parseFloat(enterLat.getText().toString());
            coord[1] = Float.parseFloat(enterLong.getText().toString());
        }
        //checks to see if there is no current coordinates set
        if(coord == null){
            Toast.makeText(this, "Please set a location", Toast.LENGTH_LONG).show();
        }else {
//        if(enterLat.getText() == null && enterLong.getText() == null)
        progressDialog = ProgressDialog.show(this, "Weather Data", "Checking Your Forecast...", true, true);


            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                //create separate thread and new task
                //executes with concatenated coordinates onto URL
                String destination = "https://api.openweathermap.org/data/2.5/weather?lat=" + coord[0] + "&lon=" + coord[1] + "&units=imperial&appid=19659b826c4a8eaaef1ad02d7b8b4cb8";
                Log.i("PATH", destination);
                new DownloadWeatherDataTask().execute(destination);
            } else {
                todayWeather.setText("No network connection available; I hope it's sunny for you");
            }
        }
    }

    private class DownloadWeatherDataTask extends AsyncTask<String, Void, String>{
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
            //need to access individual Json Objects to obtain required info
            //inside of an array located in the index1 and index2 icon at index3
            // array --> object --> string
            JSONObject nestedObject = jsonRootObject.getJSONArray("weather").getJSONObject(0);
            String forecast = nestedObject.getString("main");
            String description = nestedObject.getString("description");
            //temps
            String temp = jsonRootObject.getJSONObject("main").getString("temp").toString();
            String feel = jsonRootObject.getJSONObject("main").getString("feels_like").toString();

            String iconUrl = "https://openweathermap.org/img/w/" + nestedObject.getString("icon") + ".png";
            Log.i("ICON", iconUrl);
//            Picasso.get().setLoggingEnabled(true);
            Picasso.get().load(iconUrl).resize(pictureFrame.getWidth(), pictureFrame.getHeight()).into(pictureFrame);
            todayWeather.setText("Todays Weather: " + forecast);
            weatherDescription.setText("Description: " + description);
            temperature.setText("Temperature: " + temp);
            realFeel.setText("Feels Like: " + feel);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------

    // Geocoder and coordinates methods


    //handling of geocoder in order to access current location. Displays current city and state in location view
    // but saves current coordinates into instance variable to be passed into retrieveData
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void populateLocation(View view) {
        gps = findViewById(R.id.locationView);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // geocoder
        geocoder = new Geocoder(this, Locale.getDefault());

        // logging for location services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.i("SENSOR", "Location enabled: " + locationManager.isProviderEnabled(locationManager.GPS_PROVIDER));
            Log.i("SENSOR", "Location enabled: " + locationManager.isLocationEnabled());
        } else {
            Log.i("SENSOR", "Location/Provider disabled");
        }
        locationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            //results of the location (Longitude and latitude)
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // geocoder stores information in list of Address type.
                List<Address> address;
                try {
                    address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    // returns city value from geocoder class
                    String city = address.get(0).getLocality();
                    String state = address.get(0).getAdminArea();
                    gps.setText(city + ", " + state);
                    coord = new float[2];
                    coord[1] = (float)location.getLongitude();
                    coord[0] = (float)location.getLatitude();

                } catch (IOException e) {
                    e.printStackTrace();
                    //If you have this problem then it is probably your emulator and you either need to clear cache or
                    //run a different AVD.
                    gps.setText("GPS unavailable (GRPC failure).");
                }

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 8);
            return;
        } else {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 100, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 8:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 100, 0, locationListener);
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    //movie list button listener
    public void movieCallback(View view) {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }
}