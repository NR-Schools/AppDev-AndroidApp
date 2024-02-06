package com.it193.dogadoptionapp.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.MainActivity;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.List;

public class AdminDashboardView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdminDashboardActionListener {

    private List<Dog> dogs;

    private GridView dogListView;
    private Button goToDogRequest;

    private Button logOut;
    private DrawerLayout drawerLayout;
    private View customNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_view);

        // Initialize the Drawer
        drawerInit();

        // Initialize Components and Data
        initComponents();

        // Handle Actions
        handleActions();
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadOrRefreshData();
    }

    private void initComponents() {
        logOut = customNavView.findViewById(R.id.logoutbutton);
        goToDogRequest = customNavView.findViewById(R.id.GoToDogRequestView);
        dogListView = findViewById(R.id.adminDashboardDogList);
    }
    private void handleActions() {
        goToDogRequest.setOnClickListener(v -> startActivity(new Intent(AdminDashboardView.this, DogRequestView.class)));
        logOut.setOnClickListener(v -> startActivity(new Intent(AdminDashboardView.this, MainActivity.class)));
    }
    private void setInitialData(Object responseObject, String errorMessage) {
        if (responseObject == null)
            return;

        dogs = (List<Dog>) responseObject;
        dogListView.setAdapter(null);

        AdminDogListAdapter dogListAdapter = new AdminDogListAdapter(
                getApplicationContext(),
                dogs,
                this
        );
        dogListView.setAdapter(dogListAdapter);

    }
    private void loadOrRefreshData() {
        DogRepository
                .getRepository(this)
                .getAllDogRecords()
                .setCallback(this::setInitialData);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public void drawerInit() {
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(findViewById(R.id.toolbar));
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


    @Override
    public void onAddDogInfo() {
        startActivity(new Intent(AdminDashboardView.this, AddDogRecordView.class));
    }
    @Override
    public void onUpdateDogInfo(Dog dog) {
        Intent intent = new Intent(AdminDashboardView.this, UpdateDogRecordView.class);
        intent.putExtra("dogId", dog.getId());
        startActivity(intent);
    }
    @Override
    public void onDeleteDogInfo() {
        loadOrRefreshData();
    }
}