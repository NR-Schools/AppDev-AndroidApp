package com.it193.dogadoptionapp.view.shared;

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
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRequestRepository;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.view.admin.AdminDashboardView;
import com.it193.dogadoptionapp.view.user.UserDashboardView;

import java.util.List;

public class DogRequestView extends CustomDrawerNoFilterView implements DogRequestActionListener {

    private List<Dog> dogs;
    private GridView dogRequestListView;
    private Button goToDogDashboard;
    private Account currentAccount;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_request_view);

        // Initialize the Drawer
        drawerInit();

        // Initialize Components and Data
        initComponents();

        logOut.setOnClickListener(v -> startActivity(new Intent(DogRequestView.this, MainActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadOrRefreshData();
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
        dogRequestListView.setAdapter(null);

        if (dogs.isEmpty())
            return;

        DogRequestListAdapter dogListAdapter = new DogRequestListAdapter(
                getApplicationContext(),
                dogs,
                this
        );
        dogRequestListView.setAdapter(dogListAdapter);
    }
    private void loadOrRefreshData() {
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


    @Override
    public void onRefreshData() {
        loadOrRefreshData();
    }
}