package com.it193.dogadoptionapp.view.admin;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.it193.dogadoptionapp.databinding.ActivityAdminDashboardViewBinding;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardView extends AppCompatActivity {

    private DogApi dogApi;
    private List<Dog> dogs;

    private ListView dogListView;
    private Button goToAddDog;
    private Button goToDogRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_view);

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);

        // Initialize Components and Data
        initComponents();
        initData();

        // Handle Actions
        handleActions();
    }

    private void initData() {
        dogApi.getAllDogs()
                .enqueue(new Callback<List<Dog>>() {
                    @Override
                    public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                        dogs = response.body();
                        AdminDogListAdapter dogListAdapter = new AdminDogListAdapter(
                                getApplicationContext(),
                                dogs
                        );
                        dogListView.setAdapter(dogListAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Dog>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
    }
    private void initComponents() {
        goToAddDog = findViewById(R.id.adminDashboardViewToAddDogView);
        goToDogRequest = findViewById(R.id.adminDashboardViewToAdminDogRequestView);
        dogListView = findViewById(R.id.adminDashboardDogList);
    }
    private void handleActions() {
        goToAddDog.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardView.this, AddDogRecordView.class));
        });

        goToDogRequest.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardView.this, DogRequestView.class));
        });

        dogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdminDashboardView.this, UpdateDogRecordView.class);
                intent.putExtra("dogId", dogs.get(position).getId());
                startActivity(intent);
            }
        });
    }
}