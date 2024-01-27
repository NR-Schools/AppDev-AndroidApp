package com.it193.dogadoptionapp.view.admin;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.it193.dogadoptionapp.databinding.ActivityAdminDashboardViewBinding;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;

public class AdminDashboardView extends AppCompatActivity {

    private DogApi dogApi;

    private Button goToAddDog;
    private Button goToUpdateDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_view);

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);

        // Initialize Components
        initComponents();

        // Handle Actions
        handleRedirectActions();
    }

    private void initComponents() {
        goToAddDog = findViewById(R.id.adminDashboardViewToAddDogView);
        goToUpdateDog = findViewById(R.id.adminDashboardViewToUpdateDogView);
    }

    private void handleRedirectActions() {
        goToAddDog.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardView.this, AddDogRecordView.class));
        });

        goToUpdateDog.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardView.this, UpdateDogRecordView.class));
        });
    }
}