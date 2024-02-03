package com.it193.dogadoptionapp.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.List;

public class AdminDashboardView extends AppCompatActivity {

    private List<Dog> dogs;

    private GridView dogListView;
    private Button goToAddDog;
    private Button goToDogRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_view);

        // Initialize Components and Data
        initComponents();

        // Handle Actions
        handleActions();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Load Dogs
        DogRepository
                .getRepository(this)
                .getAllDogRecords()
                .setCallback(this::setInitialData);
    }

    private void initComponents() {
        //goToAddDog = findViewById(R.id.adminDashboardViewToAddDogView);
        //goToDogRequest = findViewById(R.id.adminDashboardViewToAdminDogRequestView);
        dogListView = findViewById(R.id.adminDashboardDogList);
    }
    private void handleActions() {
        //goToAddDog.setOnClickListener(v -> startActivity(new Intent(AdminDashboardView.this, AddDogRecordView.class)));
        //goToDogRequest.setOnClickListener(v -> startActivity(new Intent(AdminDashboardView.this, DogRequestView.class)));
        dogListView.setOnItemClickListener((parent, view, position, id) -> {

                if(position==0){
                    startActivity(new Intent(AdminDashboardView.this, AddDogRecordView.class));
                }
                else{
                    position = position - 1;
                    Intent intent = new Intent(AdminDashboardView.this, UpdateDogRecordView.class);
                    intent.putExtra("dogId", dogs.get(position).getId());
                    startActivity(intent);
                }

        });
    }

    private void setInitialData(Object responseObject, String errorMessage) {
        if (responseObject == null)
            return;

        dogs = (List<Dog>) responseObject;
        if (!dogs.isEmpty()) {
            AdminDogListAdapter dogListAdapter = new AdminDogListAdapter(
                    getApplicationContext(),
                    dogs
            );
            dogListView.setAdapter(dogListAdapter);
        }
    }
}