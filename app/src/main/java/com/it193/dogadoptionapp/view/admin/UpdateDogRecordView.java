package com.it193.dogadoptionapp.view.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class UpdateDogRecordView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityResultLauncher<Intent> dogImageActionLauncher;
    private long dogId;


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
    private TextView dogArrivedDateDisplayField;
    private View dogArrivedDateButton;
    //endregion
    private EditText dogArrivedFromField;
    private Spinner dogSizeField;
    private EditText dogLocationField;
    private EditText dogDescriptionField;

    private Button updateDogButton;
    //endregion

    private Button goToDogRequest;

    private Button goToDogDashboard;

    private DrawerLayout drawerLayout;

    private View customNavView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dog_record_view);


        // Initialize the Drawer
        drawer_init();

        // Get Inputs
        initComponents();



        // Handle Actions
        selectDogImageButton.setOnClickListener(this::handleSelectDogImageAction);
        dogArrivedDateButton.setOnClickListener(this::handleSelectDateActions);
        updateDogButton.setOnClickListener(this::handleUpdateDogAction);

        // Handle Navigation Menu Actions
        goToDogRequest.setOnClickListener(v -> startActivity(new Intent(UpdateDogRecordView.this, DogRequestView.class)));
        goToDogDashboard.setOnClickListener(v -> startActivity(new Intent(UpdateDogRecordView.this, AdminDashboardView.class)));
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
        selectDogImageButton = findViewById(R.id.updateDogSelectImageButton);
        displayImage = findViewById(R.id.updateDogDisplayImageView);
        dogNameField = findViewById(R.id.updateDogNameField);
        dogBreedField = findViewById(R.id.updateDogBreedField);
        dogColorField = findViewById(R.id.updateDogColorField);
        dogAgeField = findViewById(R.id.updateDogAgeField);
        dogSexField = findViewById(R.id.updateDogSexField);
        dogArrivedDateButton = findViewById(R.id.updateDogArrivedDateButton);
        dogArrivedDateDisplayField  = findViewById(R.id.updateDogArrivedDateButton);
        //dogArrivedDateDisplayField = findViewById(R.id.updateDogArrivedDateDisplay);
        dogArrivedFromField = findViewById(R.id.updateDogArrivedFromField);
        dogSizeField = findViewById(R.id.updateDogSizeField);
        dogLocationField = findViewById(R.id.updateDogLocationField);
        dogDescriptionField = findViewById(R.id.updateDogDescriptionField);
        updateDogButton = findViewById(R.id.updateDogButton);
        goToDogRequest = customNavView.findViewById(R.id.GoToDogRequestView);
        goToDogDashboard = customNavView.findViewById(R.id.GoToDogDashboard);

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

    private void setInitialData(Object responseObject, String errorMessage) {

        if (responseObject == null)
            return;

        Dog dog = (Dog) responseObject;

        displayImage.setImageBitmap(
                BitmapFactory.decodeByteArray(
                        dog.getPhotoBytes(),
                        0,
                        dog.getPhotoBytes().length
                )
        );

        dogNameField.setText(dog.getName());
        dogBreedField.setText(dog.getBreed());
        dogColorField.setText(dog.getColorCoat());
        dogAgeField.setText(String.valueOf(dog.getAge()));
        dogSexField.setSelection(
                InputUtility.getIndexFromObject(dog.getSex(), Arrays.asList(getResources().getStringArray(R.array.dog_sex_entries)))
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dogArrivedDateDisplayField.setText(dog.getArrivedDate().format(formatter));
        dogArrivedFromField.setText(dog.getArrivedFrom());
        dogSizeField.setSelection(
                InputUtility.getIndexFromObject(dog.getSize(), Arrays.asList(getResources().getStringArray(R.array.dog_size_entries)))
        );
        dogLocationField.setText(dog.getLocation());
        dogDescriptionField.setText(dog.getDescription());
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

    private void handleUpdateDogAction(View v) {
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

        // Send Data
        DogRepository
                .getRepository(this)
                .updateDogRecord(
                        dogId, dogImageBytes, isPhotoSet, dogName, dogBreed, dogColor, dogAgeStr,
                        dogSex, dogArrivedDate, dogArrivedFrom, dogSize, dogLocation, dogDescription
                )
                .setCallback((a, b) -> {});

        //Return to Admin Dashboard
        startActivity(new Intent(UpdateDogRecordView.this, AdminDashboardView.class));
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
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Update Information" + "</font>"));
    }
}