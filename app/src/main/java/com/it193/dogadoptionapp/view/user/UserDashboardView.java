package com.it193.dogadoptionapp.view.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;

import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.view.shared.DogDetailsView;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.List;

public class UserDashboardView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private List<Dog> dogs;
    private GridView dogListView;
    private Button goToUserDogRequest;
    private DrawerLayout drawerLayout;
    private View customNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard_view);

        // Initialize the Drawer
        drawer_init();

        // Initialize Components and Data
        initComponents();

        // Handle Actions
        handleActions();
    }

    @Override
    protected void onStart() {
        super.onStart();

        DogRepository
                .getRepository(this)
                .getAllDogRecords()
                .setCallback(this::setInitialData);
    }

    private void setInitialData(Object responseObject, String errorMessage) {
        if (responseObject == null)
            return;

        dogs = (List<Dog>) responseObject;
        if (!dogs.isEmpty()) {
            UserDogListAdapter dogListAdapter = new UserDogListAdapter(
                    getApplicationContext(),
                    dogs
            );
            dogListView.setAdapter(dogListAdapter);
        }


    }
    private void initComponents() {
        dogListView = findViewById(R.id.userDashboardDogList);
        goToUserDogRequest = customNavView.findViewById(R.id.GoToDogRequestView);
    }
    private void handleActions() {
        goToUserDogRequest.setOnClickListener(v -> startActivity(new Intent(UserDashboardView.this, DogRequestView.class)));
        dogListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(UserDashboardView.this, DogDetailsView.class);
            intent.putExtra("dogId", dogs.get(position).getId());
            startActivity(intent);
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void drawer_init(){
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setBackgroundColor(getResources().getColor(R.color.white));
        customNavView = getLayoutInflater().inflate(R.layout.custom_nav_menu, navigationView, false);
        navigationView.addHeaderView(customNavView);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Dashboard" + "</font>"));
    }


}