package com.c323proj10nolancauley.quickcooks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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

public class SelectedCategoryActivity extends AppCompatActivity {
    //obtained from intent in activity 1
    String category;
    String foodListAPI = "https://www.themealdb.com/api/json/v1/1/filter.php?c=";
    ProgressDialog progressDialog;
    Toolbar toolbar;

    ArrayAdapter<Adapter> adapter;
    JSONArray jsonArray;
    String jsonData = "";
    ListView list;

    ArrayList<Meal> mealList = new ArrayList<Meal>();
    //text view to display what category we are on
    TextView label;

    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);
        //initialize views first
        //set our category onto our API call
        category = getIntent().getStringExtra("CATEGORY");
        foodListAPI = foodListAPI.concat(category);
        list = findViewById(R.id.selectedListView);
        label = findViewById(R.id.labelView);
        label.setText(category);

        //interstitial ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-5459372456370034/3425088581");
        //plays interstitial at start of this activity and reward on item click
        //loads an ad everytime an item is clicked, waits till it is loaded otherwise we move on
        interstitialAd.loadAd(new AdRequest.Builder().build());

        //listens if it is does; if so -> proceed, if wait for ad to play
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();

                retrieveFromSelectedCategory();
            }
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                retrieveFromSelectedCategory();
            }
        });
        //reward ad
        rewardedAd = new RewardedAd(this, "ca-app-pub-5459372456370034/7748650430");

    }

    // makes api call to retrieve all food items
    private void retrieveFromSelectedCategory() {
        progressDialog = ProgressDialog.show(this, "Meal Categories", "Getting the cookbooks...", true, true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //create separate thread and new task
            //executes with concatenated coordinates onto URL
            String destination = foodListAPI;
            new DownloadSelectedDataTask().execute(destination);
        } else {
            Toast.makeText(this, "No network connection available... hope you're not hungry", Toast.LENGTH_LONG).show();
        }
    }

    private class DownloadSelectedDataTask extends AsyncTask<String, Void, String> {
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
            jsonArray = jsonRootObject.getJSONArray("meals");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject current = jsonArray.getJSONObject(i);
                //each object has their id , name, and image path taken -> made into an object -> added to array
                String mealID = current.getString("idMeal");
                String mealName = current.getString("strMeal");
                String mealIconLink = current.optString("strMealThumb");

                //create meal object
                Meal meal = new Meal(mealID, mealName, mealIconLink);
                mealList.add(meal);
            }
            populateList();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateList() {
        Intent intent = new Intent(this, RecipeActivity.class);
        adapter = new CustomListAdapter();
        list.setAdapter(adapter);
        //onclick for item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //listener for RewardedAdLoadCallback; navigates to the selected recipe
                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                    }
                    @Override
                    public void onRewardedAdClosed() {
                        //obtain category name to pass onto second activity to be used for making second API call
                        intent.putExtra("ID", mealList.get(position).id);
                        intent.putExtra("NAME", mealList.get(position).name);
                        startActivity(intent);
                    }

                    @Override
                    public void onRewardedAdFailedToShow(AdError adError) {
                        //obtain category name to pass onto second activity to be used for making second API call
                        intent.putExtra("ID", mealList.get(position).id);
                        intent.putExtra("NAME", mealList.get(position).name);
                        startActivity(intent);
                    }
                };
                RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback(){
                    @Override
                    public void onRewardedAdLoaded() {
                        rewardedAd.show(SelectedCategoryActivity.this, adCallback);
                    }
                    @Override
                    public void onRewardedAdFailedToLoad(LoadAdError adError) {
                        rewardedAd.show(SelectedCategoryActivity.this, adCallback);
                    }
                };
                // load and only play once it is loaded or fails to load in order to navigate further
                rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
            }
        });

    }
    public class CustomListAdapter extends ArrayAdapter {
        public CustomListAdapter(){
            super(SelectedCategoryActivity.this, R.layout.category_list_layout, mealList);
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
            Meal current = mealList.get(position);

            title.setText(current.name);
            //uses picasso to display poster onto ImageView
            Picasso.get().load(current.path).into(poster);

            return itemView;
        }
    }
}
