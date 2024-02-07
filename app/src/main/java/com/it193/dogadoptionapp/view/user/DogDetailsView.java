package com.it193.dogadoptionapp.view.user;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.MainActivity;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.repository.DogRequestRepository;
import com.it193.dogadoptionapp.utils.NotificationUtility;
import com.it193.dogadoptionapp.view.shared.CustomDrawerNoFilterView;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

public class DogDetailsView extends CustomDrawerNoFilterView {

    private long dogId;
    private Dog dog;

    private ImageView dogImageView;
    private TextView dogNameField;
    private Button dogRequestButton;
    private Button goToDogRequest;
    private Button goToDogDashboard;
    private Button logOut;
    private Button statusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_details_view);

        // Initialize the Drawer
        drawerInit("View A Dog");

        // Get Inputs
        initComponents();

        // Handle Navigation Actions
        goToDogRequest.setOnClickListener(v -> startActivity(new Intent(DogDetailsView.this, DogRequestView.class)));
        goToDogDashboard.setOnClickListener(v -> startActivity(new Intent(DogDetailsView.this, UserDashboardView.class)));
        logOut.setOnClickListener(v -> startActivity(new Intent(DogDetailsView.this, MainActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        dogId = intent.getLongExtra("dogId", -1);

        loadOrRefreshData();
    }

    private void initComponents() {
        dogImageView = findViewById(R.id.dogDetailsImageDisplay);
        dogNameField = findViewById(R.id.dogDetailsNameField);
        dogRequestButton = findViewById(R.id.dogDetailsRequestDogButton);
        statusButton = findViewById(R.id.dogStatusButton);

        goToDogRequest = customNavView.findViewById(R.id.GoToDogRequestView);
        goToDogDashboard = customNavView.findViewById(R.id.GoToDogDashboard);
        logOut = customNavView.findViewById(R.id.logoutbutton);
    }

    private void setInitialData(Object responseObject, String errorMessage) {
        if (responseObject == null)
            return;

        dog = (Dog) responseObject;
        dogImageView.setImageBitmap(
                BitmapFactory.decodeByteArray(
                        dog.getPhotoBytes(),
                        0,
                        dog.getPhotoBytes().length
                )
        );
        dogNameField.setText(dog.getName());

        // Check if dog is already adopted
        if (dog.isAdoptRequested()) {
            statusButton.setBackgroundColor(Color.RED);
            statusButton.setText("Unavailable");
            dogRequestButton.setOnClickListener(v -> NotificationUtility.infoAlert(
                    this,
                    "Dog is already requested"
            ));
        } else {
            dogRequestButton.setOnClickListener(this::handleDogRequestAction);
        }
    }

    private void handleDogRequestAction(View v) {
        DogRequestRepository
                .getRepository(this)
                .userDogRequest(dogId)
                .setCallback((responseObject, errorMessage) -> {
                    loadOrRefreshData();
                });
    }

    private void loadOrRefreshData() {
        DogRepository
                .getRepository(this)
                .getDogRecord(dogId)
                .setCallback(this::setInitialData);
    }
}