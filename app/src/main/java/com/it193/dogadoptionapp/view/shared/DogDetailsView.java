package com.it193.dogadoptionapp.view.shared;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.data.ResponseCallback;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.repository.DogRequestRepository;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.AnimationUtility;
import com.it193.dogadoptionapp.utils.InputUtility;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogDetailsView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long dogId;

    private ImageView dogImageView;
    private TextView dogNameField;
    private Button dogRequestButton;

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_details_view);

        // Get Inputs
        initComponents();

        // Initialize the Drawer
        drawer_init();

        // Handle Actions
        dogRequestButton.setOnClickListener(this::handleDogRequestAction);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        dogId = intent.getLongExtra("dogId", -1);

        DogRepository
                .getRepository(this)
                .getDogRecord(dogId)
                .setCallback(this::setInitialData);
    }

    private void initComponents() {
        dogImageView = findViewById(R.id.dogDetailsImageDisplay);
        dogNameField = findViewById(R.id.dogDetailsNameField);
        dogRequestButton = findViewById(R.id.dogDetailsRequestDogButton);
    }

    private void setInitialData(Object responseObject, String errorMessage) {

        if (responseObject == null)
            return;

        Dog dog = (Dog) responseObject;

        dogImageView.setImageBitmap(
                BitmapFactory.decodeByteArray(
                        dog.getPhotoBytes(),
                        0,
                        dog.getPhotoBytes().length
                )
        );

        dogNameField.setText(dog.getName());
    }

    private void handleDogRequestAction(View v) {
        DogRequestRepository
                .getRepository(this)
                .userDogRequest(dogId)
                .setCallback((a, b) -> {});
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
        View customNavView = getLayoutInflater().inflate(R.layout.custom_nav_menu, navigationView, false);
        navigationView.addHeaderView(customNavView);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Dog Information" + "</font>"));
    }
}