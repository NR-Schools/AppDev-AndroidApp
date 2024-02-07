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

import java.time.format.DateTimeFormatter;

public class DogDetailsView extends CustomDrawerNoFilterView {

    private long dogId;
    private Dog dog;

    private ImageView dogImageView;
    private TextView dogNameField;
    private TextView dogBreedField;
    private TextView dogColorField;
    private TextView dogAgeField;
    private TextView dogSexField;
    private TextView dogDateField;
    private TextView dogArrivedFromField;
    private TextView dogSizeField;
    private TextView dogLocationField;
    private TextView dogMoreDetailsField;
    private Button dogRequestButton;
    private Button goToDogRequest;
    private Button goToDogDashboard;
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
        dogBreedField = findViewById(R.id.dogDetailsBreedField);
        dogColorField = findViewById(R.id.dogDetailsColorField);
        dogAgeField = findViewById(R.id.dogDetailsAgeField);
        dogSexField = findViewById(R.id.dogDetailsSexField);
        dogDateField = findViewById(R.id.dogDetailsDateField);
        dogArrivedFromField = findViewById(R.id.dogDetailsArrivedFromField);
        dogSizeField = findViewById(R.id.dogDetailsSizeField);
        dogLocationField = findViewById(R.id.dogDetailsLocationField);
        dogMoreDetailsField = findViewById(R.id.dogDetailsMoreDetailsField);


        dogRequestButton = findViewById(R.id.dogDetailsRequestDogButton);
        statusButton = findViewById(R.id.dogStatusButton);

        goToDogRequest = customNavView.findViewById(R.id.GoToDogRequestView);
        goToDogDashboard = customNavView.findViewById(R.id.GoToDogDashboard);
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
        dogBreedField.setText(dog.getBreed());
        dogColorField.setText(dog.getColorCoat());
        dogAgeField.setText(Integer.toString(dog.getAge()));
        dogSexField.setText(dog.getSex());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dogDateField.setText(dog.getArrivedDate().format(formatter));
        dogArrivedFromField.setText(dog.getArrivedFrom());
        dogSizeField.setText(dog.getSize());
        dogLocationField.setText(dog.getLocation());
        dogMoreDetailsField.setText(dog.getDescription());




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