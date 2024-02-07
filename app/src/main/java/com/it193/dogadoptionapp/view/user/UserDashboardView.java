package com.it193.dogadoptionapp.view.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.it193.dogadoptionapp.MainActivity;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;

import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.view.shared.CustomDrawerView;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.ArrayList;
import java.util.List;

public class UserDashboardView extends CustomDrawerView {

    private List<Dog> dogs;
    private List<Dog> filteredDogs;

    private GridView dogListView;
    private Button goToUserDogRequest;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard_view);

        // Initialize the Drawer
        initCustomDrawerView();

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
        dogListView.setAdapter(null);

        if (dogs.isEmpty())
            return;

        // Set dogs to filtered dogs
        filteredDogs = new ArrayList<>(dogs);

        // Refresh Adapter
        updateFilteredData();
    }
    private void updateFilteredData() {
        UserDogListAdapter dogListAdapter = new UserDogListAdapter(
                getApplicationContext(),
                filteredDogs
        );
        dogListView.setAdapter(dogListAdapter);
    }
    private void initComponents() {
        dogListView = findViewById(R.id.userDashboardDogList);
        goToUserDogRequest = customNavView.findViewById(R.id.GoToDogRequestView);
        logOut = customNavView.findViewById(R.id.logoutbutton);
    }
    private void handleActions() {
        goToUserDogRequest.setOnClickListener(v -> startActivity(new Intent(UserDashboardView.this, DogRequestView.class)));
        dogListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(UserDashboardView.this, DogDetailsView.class);
            intent.putExtra("dogId", dogs.get(position).getId());
            startActivity(intent);
        });
        logOut.setOnClickListener(v -> startActivity(new Intent(UserDashboardView.this, MainActivity.class)));
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
            if (dog.getAge() != Integer.parseInt(dogAgeFilter.getText().toString()))
                continue;

            filteredDogs.add(dog);
        }

        // Refresh Adapter
        updateFilteredData();
    }
}