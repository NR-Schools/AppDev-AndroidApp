package com.it193.dogadoptionapp.view.shared;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRequestRepository;
import com.it193.dogadoptionapp.storage.AppStateStorage;

import java.util.List;

public class DogRequestView extends AppCompatActivity {

    private List<Dog> dogs;
    private ListView dogRequestListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_request_view);

        // Initialize Components and Data
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();

        DogRequestRepository dogRequestRepository = DogRequestRepository.getRepository(this);
        if (currentAccount.getEmail().equals("Admin"))
            dogRequestRepository = dogRequestRepository.adminViewAllDogRequest();
        else
            dogRequestRepository = dogRequestRepository.userViewAllDogRequest();
        dogRequestRepository.setCallback(this::setInitialData);
    }

    private void initComponents() {
        dogRequestListView = findViewById(R.id.userDogRequestListView);
    }

    private void setInitialData(Object responseObject, String errorMessage) {
        DogRequestListAdapter dogListAdapter = new DogRequestListAdapter(
                getApplicationContext(),
                dogs
        );
        dogRequestListView.setAdapter(dogListAdapter);
    }

}