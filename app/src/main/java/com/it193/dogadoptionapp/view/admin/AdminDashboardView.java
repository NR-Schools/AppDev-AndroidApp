package com.it193.dogadoptionapp.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.List;

public class AdminDashboardView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private List<Dog> dogs;

    private GridView dogListView;
    private Button goToAddDog;
    private Button goToDogRequest;

    private DrawerLayout drawerLayout;
    private View customNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_view);


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

        // Load Dogs
        DogRepository
                .getRepository(this)
                .getAllDogRecords()
                .setCallback(this::setInitialData);
    }

    private void initComponents() {
        //goToAddDog = findViewById(R.id.adminDashboardViewToAddDogView);
        goToDogRequest = customNavView.findViewById(R.id.GoToDogRequestView);
        dogListView = findViewById(R.id.adminDashboardDogList);
    }
    private void handleActions() {
        //goToAddDog.setOnClickListener(v -> startActivity(new Intent(AdminDashboardView.this, AddDogRecordView.class)));
        goToDogRequest.setOnClickListener(v -> startActivity(new Intent(AdminDashboardView.this, DogRequestView.class)));
        dogListView.setOnItemClickListener((parent, view, position, id) -> {

                if(position==0){
                    startActivity(new Intent(AdminDashboardView.this, AddDogRecordView.class));
                }
                else{
                    position = position - 1;
                    Intent intent = new Intent(AdminDashboardView.this, UpdateDogRecordView.class);
                    intent.putExtra("dogId", dogs.get(position).getId());
                    startActivity(intent);
                }

        });
    }

    private void setInitialData(Object responseObject, String errorMessage) {
        if (responseObject == null)
            return;

        dogs = (List<Dog>) responseObject;
        AdminDogListAdapter dogListAdapter = new AdminDogListAdapter(
                getApplicationContext(),
                dogs
        );
        dogListView.setAdapter(dogListAdapter);

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