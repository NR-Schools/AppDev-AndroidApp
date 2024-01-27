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
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import java.io.ByteArrayOutputStream;

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
        dogNameField = new EditText(this);
        dogBreedField = new EditText(this);
        dogColorField = new EditText(this);
        dogAgeField = new EditText(this);
        dogSexField = new Spinner(this);
        dogArrivedDateButton = new View(this);
        dogArrivedDateDisplayField = new TextView(this);
        dogArrivedFromField = new EditText(this);
        dogSizeField = new Spinner(this);
        dogLocationField = new EditText(this);
        dogDescriptionField = new EditText(this);
        addDogButton = new Button(this);

        dogImageActionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        displayImage.setImageURI(data.getData());

                        isPhotoSet = true;

                        // DEBUG
                        Bitmap dogImageBitmap = ((BitmapDrawable) displayImage.getDrawable()).getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        dogImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] dogImageBytes = byteArrayOutputStream.toByteArray();

                        // R_DEBUG
                        Bitmap bmp = BitmapFactory.decodeByteArray(dogImageBytes, 0, dogImageBytes.length);
                        displayImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, displayImage.getWidth(), displayImage.getHeight(), false));

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
                    (view, year, month, dayOfMonth) ->
                            dogArrivedDateDisplayField.setText(year + "-" + month + "-" + dayOfMonth)
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
            byte[] dogImageBytes = null;
            if (isPhotoSet) {
                Bitmap dogImageBitmap = ((BitmapDrawable) displayImage.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                dogImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                dogImageBytes = byteArrayOutputStream.toByteArray();
            }
            //endregion


            // Check if all fields have non-empty input
            if (InputUtility.stringsAreNotNullOrEmpty(
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

            // Try to cast age
            int dogAge = Integer.parseInt(dogAgeStr);

            // Get Credentials
            Account currentAccount = AppStateStorage.getInstance().getActiveAccount();

            // Send Data
            dogApi.addNewDog(
                    currentAccount.getEmail(),
                    currentAccount.getSessionAuthString(),
                    MultipartBody.Part.createFormData("photoBytes", "file", RequestBody.create(MediaType.parse("image*/"), dogImageBytes)),
                    dogName,
                    dogBreed,
                    dogAge,
                    dogSex,
                    dogColor,
                    dogDescription,
                    dogArrivedDate,
                    dogArrivedFrom,
                    dogSize,
                    dogLocation
            ).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    NotificationUtility.successAlert(
                            AddDogRecordView.this,
                            "Dog Record added successfully!"
                    );
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    NotificationUtility.errorAlert(
                            AddDogRecordView.this,
                            "Add Dog",
                            "Dog Record failed to be added!"
                    );
                }
            });
        });
    }
}