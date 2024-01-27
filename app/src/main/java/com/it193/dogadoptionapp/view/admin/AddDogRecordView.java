package com.it193.dogadoptionapp.view.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;

public class AddDogRecordView extends AppCompatActivity {

    private DogApi dogApi;

    private ActivityResultLauncher<Intent> launcher;

    private ImageView displayImage;
    private Button selectDogImageButton;

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
        handleAddDogAction();
    }

    private void initComponents() {
        selectDogImageButton = findViewById(R.id.addDogSelectImageButton);
        displayImage = findViewById(R.id.addDogDisplayImageView);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        displayImage.setImageURI(data.getData());
                    }
                }
        );
    }

    private void handleSelectDogImageAction() {
        selectDogImageButton.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcher.launch(openGalleryIntent);
        });
    }

    private void handleAddDogAction() {
    }
}