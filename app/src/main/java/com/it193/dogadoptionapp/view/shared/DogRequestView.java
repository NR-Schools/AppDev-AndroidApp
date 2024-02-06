package com.it193.dogadoptionapp.view.shared;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.MainActivity;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRequestRepository;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.view.admin.AddDogRecordView;
import com.it193.dogadoptionapp.view.admin.AdminDashboardView;
import com.it193.dogadoptionapp.view.admin.UpdateDogRecordView;
import com.it193.dogadoptionapp.view.user.UserDashboardView;

import java.util.List;

public class DogRequestView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DogRequestListAdapter.RequestActionListener {

    private List<Dog> dogs;
    private GridView dogRequestListView;
    private DrawerLayout drawerLayout;
    private View customNavView;
    private Button goToDogDashboard;
    private Account currentAccount;

    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_request_view);

        // Initialize the Drawer
        drawer_init();

        // Initialize Components and Data
        initComponents();

        logOut.setOnClickListener(v -> startActivity(new Intent(DogRequestView.this, MainActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentAccount = AppStateStorage.getInstance().getActiveAccount();

        DogRequestRepository dogRequestRepository = DogRequestRepository.getRepository(this);
        if (currentAccount.getEmail().equals("Admin")){
            dogRequestRepository = dogRequestRepository.adminViewAllDogRequest();
            goToDogDashboard.setOnClickListener(v -> startActivity(new Intent(DogRequestView.this, AdminDashboardView.class)));
        }
        else{
            dogRequestRepository = dogRequestRepository.userViewAllDogRequest();
            goToDogDashboard.setOnClickListener(v -> startActivity(new Intent(DogRequestView.this, UserDashboardView.class)));
        }
        dogRequestRepository.setCallback(this::setInitialData);

    }

    private void initComponents() {
        goToDogDashboard = customNavView.findViewById(R.id.GoToDogDashboard);
        dogRequestListView = findViewById(R.id.userDogRequestListView);
        logOut = customNavView.findViewById(R.id.logoutbutton);
    }

    private void setInitialData(Object responseObject, String errorMessage) {
        if (responseObject == null)
            return;

        dogs = (List<Dog>) responseObject;
        if (!dogs.isEmpty()) {
            DogRequestListAdapter dogListAdapter = new DogRequestListAdapter(
                    getApplicationContext(),
                    dogs,
                    this
            );
            dogRequestListView.setAdapter(dogListAdapter);
        }

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
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setBackgroundColor(getResources().getColor(R.color.white));
        customNavView = getLayoutInflater().inflate(R.layout.custom_nav_menu2, navigationView, false);
        navigationView.addHeaderView(customNavView);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Dog Requests" + "</font>"));

    }
    @Override
    public void refreshGrid() {
        startActivity(new Intent(DogRequestView.this, DogRequestView.class));
    }
}