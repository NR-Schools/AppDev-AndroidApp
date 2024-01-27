package com.it193.dogadoptionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.it193.dogadoptionapp.view.auth.LogInView;
import com.it193.dogadoptionapp.view.auth.SignUpView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewEntryPoint();
    }

    private void viewEntryPoint() {
        // Get Buttons
        Button goToSignUp = findViewById(R.id.welcomeViewToSignUp);
        Button goToLogIn = findViewById(R.id.welcomeViewToLogIn);

        // Set Actions
        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUpView.class));
        });

        goToLogIn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LogInView.class));
        });
    }
}