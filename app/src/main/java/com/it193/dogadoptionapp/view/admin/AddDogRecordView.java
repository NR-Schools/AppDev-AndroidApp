package com.it193.dogadoptionapp.view.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.AnimationUtility;
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class AddDogRecordView extends AppCompatActivity {

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
    private TextView dogArrivedDateDisplayField;
    private View dogArrivedDateButton;
    //endregion
    private EditText dogArrivedFromField;
    private Spinner dogSizeField;
    private EditText dogLocationField;
    private EditText dogDescriptionField;

    private Button addDogButton;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog_record_view);

        // Initialize AnimationUtility
        AnimationUtility.getInstance().initialize(this, getLayoutInflater());

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);

        // Get Inputs
        initComponents();

        // Handle Actions
        handleSelectDogImageAction();
        handleSelectDateActions();
        handleAddDogAction();
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
        dogArrivedDateDisplayField = findViewById(R.id.addDogArrivedDateDisplay);
        dogArrivedFromField = findViewById(R.id.addDogArrivedFromField);
        dogSizeField = findViewById(R.id.addDogSizeField);
        dogLocationField = findViewById(R.id.addDogLocationField);
        dogDescriptionField = findViewById(R.id.addDogDescriptionField);
        addDogButton = findViewById(R.id.addDogButton);

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

    private void handleSelectDogImageAction() {
        selectDogImageButton.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            dogImageActionLauncher.launch(openGalleryIntent);
        });
    }

    private void handleSelectDateActions() {
        dogArrivedDateButton.setOnClickListener(v -> {
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
        });
    }

    private void handleAddDogAction() {
        addDogButton.setOnClickListener(v -> {
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
                dogImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
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
            AnimationUtility.getInstance().startLoading();
            handleSendingData(
                    dogImageBytes, dogName, dogBreed, dogColor, dogAgeStr, dogSex,
                    dogArrivedDate, dogArrivedFrom, dogSize, dogLocation, dogDescription
            );
        });
    }

    // I don't know why we have to do this
    // This should be handled by another class or something
    private void handleSendingData(
            byte[] dogImageBytes, String name, String breed, String color, String age, String sex,
            String arrivedDate, String arrivedFrom, String dogSize, String location, String description
    ) {
        // Get Credentials
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();

        // Setup Request Bodies
        RequestBody dogImageRB = RequestBody.create(MediaType.parse("image/png"), dogImageBytes);
        MultipartBody.Part dogImagePart = MultipartBody.Part.createFormData("photoBytes", "file", dogImageRB);
        RequestBody dogNameRB = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody dogBreedRB = RequestBody.create(MediaType.parse("text/plain"), breed);
        RequestBody dogColorRB = RequestBody.create(MediaType.parse("text/plain"), color);
        RequestBody dogAgeRB = RequestBody.create(MediaType.parse("text/plain"), age);
        RequestBody dogSexRB = RequestBody.create(MediaType.parse("text/plain"), sex);
        RequestBody dogArrivedDateRB = RequestBody.create(MediaType.parse("text/plain"), arrivedDate);
        RequestBody dogArrivedFromRB = RequestBody.create(MediaType.parse("text/plain"), arrivedFrom);
        RequestBody dogSizeRB = RequestBody.create(MediaType.parse("text/plain"), dogSize);
        RequestBody dogLocationRB = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody dogDescriptionRB = RequestBody.create(MediaType.parse("text/plain"), description);

        dogApi.addNewDog(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString(),
                dogImagePart,
                dogNameRB,
                dogBreedRB,
                dogAgeRB,
                dogSexRB,
                dogColorRB,
                dogDescriptionRB,
                dogArrivedDateRB,
                dogArrivedFromRB,
                dogSizeRB,
                dogLocationRB
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                AnimationUtility.getInstance().endLoading();
                NotificationUtility.successAlert(
                        AddDogRecordView.this,
                        "Add Dog Action Successfully!"
                );
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                AnimationUtility.getInstance().endLoading();
                NotificationUtility.errorAlert(
                        AddDogRecordView.this,
                        "Add Dog",
                        "Failed to add new dog!"
                );
            }
        });
    }
}