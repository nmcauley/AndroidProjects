package com.c323proj10.nolancauley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState,0);

    }

    //initialize views
    private void initializeViews() {
//        coordinatorLayout = findViewById(R.id.coordLay);
        //bottom sheet
        LinearLayout bottomSheet = findViewById(R.id.bottomSheet);
        //behavior
        BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);
        bsb.setPeekHeight(80);
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                bsb.setPeekHeight(200);
                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        //begins collapsed
//        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);


        //toolbar
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);
        //nav drawer
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }

    //fragment time
    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex) {
        if (savedInstanceState == null) {
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.nav_playlist:
//                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new PlaylistFragment())
//                        .commit();
//                closeDrawer();
//                break;
            case R.id.nav_videos:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new VideoFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.nav_songs:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new SongFragment())
                        .commit();
                closeDrawer();
                break;
            default:
                //playlist is default
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new PlaylistFragment())
                        .commit();
                closeDrawer();
                break;
        }
        return true;
    }

    //Checks if the navigation drawer is open - if so, close it
    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //Iterates through all the items in the navigation menu and deselects them:
    //removes the selection color
    private void deSelectCheckedState(){
        int noOfItems = navigationView.getMenu().size();
        for (int i=0; i<noOfItems;i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }
}