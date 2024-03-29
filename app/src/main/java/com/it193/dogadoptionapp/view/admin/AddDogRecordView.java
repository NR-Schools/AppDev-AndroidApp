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
import com.it193.dogadoptionapp.view.shared.CustomDrawerNoFilterView;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.io.ByteArrayOutputStream;

public class AddDogRecordView extends CustomDrawerNoFilterView {

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

    private Button goToDogRequest;
    private Button goToDogDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog_record_view);

        // Initialize the Drawer
        drawerInit("Add A Dog");

        // Get Inputs
        initComponents();

        // Handle Actions
        selectDogImageButton.setOnClickListener(this::handleSelectDogImageAction);
        dogArrivedDateButton.setOnClickListener(this::handleSelectDateActions);
        addDogButton.setOnClickListener(this::handleAddDogAction);

        // Handle Navigation Menu Actions
        goToDogRequest.setOnClickListener(v -> startActivity(new Intent(AddDogRecordView.this, DogRequestView.class)));
        goToDogDashboard.setOnClickListener(v -> startActivity(new Intent(AddDogRecordView.this, AdminDashboardView.class)));
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
}