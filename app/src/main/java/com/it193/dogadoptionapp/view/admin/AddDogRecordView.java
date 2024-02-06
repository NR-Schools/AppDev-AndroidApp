package com.it193.dogadoptionapp.view.admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.MainActivity;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;
import com.it193.dogadoptionapp.view.shared.DogRequestView;
import com.it193.dogadoptionapp.view.user.UserDashboardView;

import java.io.ByteArrayOutputStream;

public class AddDogRecordView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DogApi dogApi;

    private ActivityResultLauncher<Intent> dogImageActionLauncher;

    //region Form Fields

    //region Handle Dog Image
    private ImageView displayImage;
    private Button selectDogImageButton;
    private boolean isPhotoSet;
    //endregion

    private EditText dogNameField;
    private EditText dogBreedField;
    private EditText dogColorField;
    private EditText dogAgeField;
    private Spinner dogSexField;

    //region Handle arrivedDate with separate view and text view
    private EditText dogArrivedDateDisplayField;
    private View dogArrivedDateButton;
    //endregion
    private EditText dogArrivedFromField;
    private Spinner dogSizeField;
    private EditText dogLocationField;
    private EditText dogDescriptionField;

    private Button addDogButton;
    //endregion
    private DrawerLayout drawerLayout;

    private Button goToDogRequest;

    private Button goToDogDashboard;
    private View customNavView;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog_record_view);


        // Initialize the Drawer
        drawer_init();
        // Get Inputs
        initComponents();

        // Handle Actions
        selectDogImageButton.setOnClickListener(this::handleSelectDogImageAction);
        dogArrivedDateButton.setOnClickListener(this::handleSelectDateActions);
        addDogButton.setOnClickListener(this::handleAddDogAction);

        // Handle Navigation Menu Actions
        goToDogRequest.setOnClickListener(v -> startActivity(new Intent(AddDogRecordView.this, DogRequestView.class)));
        goToDogDashboard.setOnClickListener(v -> startActivity(new Intent(AddDogRecordView.this, AdminDashboardView.class)));
        logOut.setOnClickListener(v -> startActivity(new Intent(AddDogRecordView.this, MainActivity.class)));
    }

    private void initComponents() {
        selectDogImageButton = findViewById(R.id.addDogSelectImageButton);
        displayImage = findViewById(R.id.addDogDisplayImageView);
        dogNameField = findViewById(R.id.addDogNameField);
        dogBreedField = findViewById(R.id.addDogBreedField);
        dogColorField = findViewById(R.id.addDogColorField);
        dogAgeField = findViewById(R.id.addDogAgeField);
        dogSexField = findViewById(R.id.addDogSexField);
        dogArrivedDateButton = findViewById(R.id.addDogArrivedDateButton);
        dogArrivedDateDisplayField = findViewById(R.id.addDogArrivedDateButton);
        dogArrivedFromField = findViewById(R.id.addDogArrivedFromField);
        dogSizeField = findViewById(R.id.addDogSizeField);
        dogLocationField = findViewById(R.id.addDogLocationField);
        dogDescriptionField = findViewById(R.id.addDogDescriptionField);
        addDogButton = findViewById(R.id.addDogButton);
        goToDogRequest = customNavView.findViewById(R.id.GoToDogRequestView);
        goToDogDashboard = customNavView.findViewById(R.id.GoToDogDashboard);
        logOut = customNavView.findViewById(R.id.logoutbutton);

        dogImageActionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        displayImage.setImageURI(data.getData());

                        isPhotoSet = true;
                    }
                }
        );
    }

    private void handleSelectDogImageAction(View v) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        dogImageActionLauncher.launch(openGalleryIntent);
    }

    private void handleSelectDateActions(View v) {
        DatePickerDialog dialog = new DatePickerDialog(this);
        dialog.setOnDateSetListener(
                (view, year, month, dayOfMonth) -> {
                    String dateString = String.format("%d-%02d-%02d", year, month+1, dayOfMonth);

                    dogArrivedDateDisplayField.setText(
                            dateString
                    );
                }
        );
        dialog.show();
    }

    private void handleAddDogAction(View v) {
        // Get Raw Data
        String dogName = dogNameField.getText().toString();
        String dogBreed = dogBreedField.getText().toString();
        String dogColor = dogColorField.getText().toString();
        String dogAgeStr = dogAgeField.getText().toString();
        String dogSex = dogSexField.getSelectedItem().toString();
        String dogArrivedDate = dogArrivedDateDisplayField.getText().toString();
        String dogArrivedFrom = dogArrivedFromField.getText().toString();
        String dogSize = dogSizeField.getSelectedItem().toString();
        String dogLocation = dogLocationField.getText().toString();
        String dogDescription = dogDescriptionField.getText().toString();

        //region Extract Bytes from ImageView
        byte[] dogImageBytes = new byte[0];
        if (isPhotoSet) {
            Bitmap dogImageBitmap = Bitmap.createScaledBitmap(
                    ((BitmapDrawable) displayImage.getDrawable()).getBitmap(),
                    200,200,
                    false
            );
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dogImageBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
            dogImageBytes = byteArrayOutputStream.toByteArray();
        }
        //endregion


        // Check if all fields have non-empty input
        if (!InputUtility.stringsAreNotNullOrEmpty(
                dogName, dogBreed, dogColor, dogAgeStr, dogSex, dogArrivedDate,
                dogArrivedFrom, dogSize, dogLocation, dogDescription
        )) {
            NotificationUtility.errorAlert(
                    this,
                    "Add Dog",
                    "Incomplete/Missing Inputs Found!!"
            );
            return;
        }

        // Check for age (must be a valid int)
        try { Integer.parseInt(dogAgeStr); }
        catch (Exception ex) { return; }

        // Send Data
        DogRepository
                .getRepository(this)
                .addDogRecord(
                        dogImageBytes, dogName, dogBreed, dogColor, dogAgeStr, dogSex,
                        dogArrivedDate, dogArrivedFrom, dogSize, dogLocation, dogDescription
                )
                .setCallback((a, b) -> {});

        //Return to Admin Dashboard
        startActivity(new Intent(AddDogRecordView.this, AdminDashboardView.class));
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
        customNavView = getLayoutInflater().inflate(R.layout.custom_nav_menu2, navigationView, false);
        navigationView.addHeaderView(customNavView);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Add A Dog" + "</font>"));
    }

}