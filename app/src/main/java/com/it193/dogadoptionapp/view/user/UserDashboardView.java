package com.it193.dogadoptionapp.view.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.data.ResponseCallback;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.view.shared.DogDetailsView;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDashboardView extends AppCompatActivity {

    private List<Dog> dogs;
    private ListView dogListView;
    private Button goToUserDogRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard_view);

        // Initialize Components and Data
        initComponents();

        // Handle Actions
        handleActions();
    }

    @Override
    protected void onStart() {
        super.onStart();

        DogRepository
                .getRepository(this)
                .getAllDogRecords()
                .setCallback(this::setInitialData);
    }

    private void setInitialData(Object responseObject, String errorMessage) {
        UserDogListAdapter dogListAdapter = new UserDogListAdapter(
                getApplicationContext(),
                dogs
        );
        dogListView.setAdapter(dogListAdapter);
    }
    private void initComponents() {
        dogListView = findViewById(R.id.userDashboardDogList);
        goToUserDogRequest = findViewById(R.id.userDashboardViewToUserDogRequestView);
    }
    private void handleActions() {
        goToUserDogRequest.setOnClickListener(v -> startActivity(new Intent(UserDashboardView.this, DogRequestView.class)));
        dogListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(UserDashboardView.this, DogDetailsView.class);
            intent.putExtra("dogId", dogs.get(position).getId());
            startActivity(intent);
        });
    }
}