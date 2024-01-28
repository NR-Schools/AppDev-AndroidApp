package com.it193.dogadoptionapp.view.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.data.ResponseCallback;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.repository.AccountRepository;
import com.it193.dogadoptionapp.retrofit.AccountApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.utils.AnimationUtility;
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpView extends AppCompatActivity {

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button signUpButton;
    private Button goToLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);

        // Get UI Fields
        nameField = findViewById(R.id.signUpNameField);
        emailField = findViewById(R.id.signUpEmailField);
        passwordField = findViewById(R.id.signUpPasswordField);
        confirmPasswordField = findViewById(R.id.signUpConfirmPasswordField);
        signUpButton = findViewById(R.id.signUpButton);
        goToLogIn = findViewById(R.id.signUpViewToLogInView);

        // Handle Actions
        goToLogIn.setOnClickListener(this::handleSwitchToLogIn);
        signUpButton.setOnClickListener(this::handleSignUpAction);
    }

    private void handleSwitchToLogIn(View v) {
        // Start New Activity
        startActivity(new Intent(SignUpView.this, LogInView.class));
    }
    private void handleSignUpAction(View v) {
        // Get Raw Data
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String passwd = passwordField.getText().toString();
        String confirmPasswd = confirmPasswordField.getText().toString();

        // Check if all fields have non-empty input
        if (!InputUtility.stringsAreNotNullOrEmpty(
                name, email, passwd, confirmPasswd
        )) {
            NotificationUtility.errorAlert(
                    this,
                    "Sign Up",
                    "Incomplete/Missing Inputs Found!!"
            );
            return;
        }

        // Check if Password and ConfirmPassword are the same
        if (!passwd.equals(confirmPasswd)) {
            NotificationUtility.errorAlert(
                    this,
                    "Sign Up",
                    "Password and Confirm Password does not match!!"
            );
            return;
        }

        // Construct Model
        Account newAccount = new Account();
        newAccount.setUsername(name);
        newAccount.setEmail(email);
        newAccount.setPassword(passwd);

        // Send Data
        AccountRepository
                .getRepository(SignUpView.this)
                .signUp(newAccount)
                .setCallback(this::handleSignUpResult);
    }

    private void handleSignUpResult(Object responseObject, String errorMessage) {
        //
    }
}