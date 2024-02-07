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
import com.it193.dogadoptionapp.view.shared.CustomDrawerView;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardView extends CustomDrawerView implements AdminDashboardActionListener {

    private List<Dog> dogs;
    private List<Dog> filteredDogs;
    private GridView dogListView;
    private Button goToDogRequest;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_view);

        // Initialize Components and Data
        initCustomDrawerView();
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

        // Set dogs to filtered dogs
        filteredDogs = new ArrayList<>(dogs);

        // Refresh Adapter
        updateFilteredData();
    }
    private void updateFilteredData() {
        AdminDogListAdapter dogListAdapter = new AdminDogListAdapter(
                getApplicationContext(),
                filteredDogs,
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

    @Override
    public void handleFilterAction() {
        // Clear List
        filteredDogs.clear();

        // Re-add dogs in the list
        for (Dog dog : dogs) {
            if (!dog.getBreed().toLowerCase().trim().contains(dogBreedFilter.getText().toString().toLowerCase().trim()))
                continue;
            if (!dog.getColorCoat().toLowerCase().trim().contains(dogColorFilter.getText().toString().toLowerCase().trim()))
                continue;
            if (!dog.getSize().toLowerCase().trim().contains(dogSizeFilter.getSelectedItem().toString().toLowerCase().trim()))
                continue;
            if (!dog.getSex().toLowerCase().trim().contains(dogSexFilter.getSelectedItem().toString().toLowerCase().trim()))
                continue;
            if (!Integer.toString(dog.getAge()).contains(dogAgeFilter.getText().toString().toLowerCase().trim()))
                continue;

            filteredDogs.add(dog);
        }

        // Refresh Adapter
        updateFilteredData();

        super.handleFilterAction();
    }
}