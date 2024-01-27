package com.it193.dogadoptionapp.view.admin;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.it193.dogadoptionapp.databinding.ActivityAdminDashboardViewBinding;

import com.it193.dogadoptionapp.R;

public class AdminDashboardView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_view);

        Button goToAddDog = findViewById(R.id.adminDashboardViewToAddDogView);
        Button goToUpdateDog = findViewById(R.id.adminDashboardViewToUpdateDogView);

        goToAddDog.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardView.this, AddDogRecordView.class));
        });

        goToUpdateDog.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardView.this, UpdateDogRecordView.class));
        });
    }
}