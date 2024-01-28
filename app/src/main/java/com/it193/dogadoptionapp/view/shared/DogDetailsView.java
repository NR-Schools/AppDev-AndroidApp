package com.it193.dogadoptionapp.view.shared;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
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

public class DogDetailsView extends AppCompatActivity {

    private DogApi dogApi;
    private long dogId;

    private ImageView dogImageView;
    private TextView dogNameField;
    private Button dogRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_details_view);

        // Initialize AnimationUtility
        AnimationUtility.getInstance().initialize(this, getLayoutInflater());

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);

        // Get Inputs
        initComponents();
        initValues();

        // Handle Actions
        dogRequestButton.setOnClickListener(this::handleDogRequestAction);
    }

    private void initComponents() {
        dogImageView = findViewById(R.id.dogDetailsImageDisplay);
        dogNameField = findViewById(R.id.dogDetailsNameField);
        dogRequestButton = findViewById(R.id.dogDetailsRequestDogButton);
    }

    private void initValues() {
        // Get Id from Intent
        Intent intent = getIntent();
        dogId = intent.getLongExtra("dogId", -1);

        // Fetch info
        AnimationUtility.getInstance().startLoading();
        dogApi.getDog(dogId)
                .enqueue(new Callback<Dog>() {
                    @Override
                    public void onResponse(Call<Dog> call, Response<Dog> response) {
                        AnimationUtility.getInstance().endLoading();
                        Dog dog = response.body();

                        dogImageView.setImageBitmap(
                                BitmapFactory.decodeByteArray(
                                        dog.getPhotoBytes(),
                                        0,
                                        dog.getPhotoBytes().length
                                )
                        );

                        dogNameField.setText(dog.getName());
                    }

                    @Override
                    public void onFailure(Call<Dog> call, Throwable t) {
                        System.out.println(t.getMessage());
                        AnimationUtility.getInstance().endLoading();
                    }
                });
    }

    private void handleDogRequestAction(View v) {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();
        Dog dog = new Dog();
        dog.setId(dogId);

        dogApi.userDogAdopt(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString(),
                dog
        ).enqueue(new Callback<Dog>() {
            @Override
            public void onResponse(Call<Dog> call, Response<Dog> response) {
                //
            }

            @Override
            public void onFailure(Call<Dog> call, Throwable t) {
                //
            }
        });
    }
}