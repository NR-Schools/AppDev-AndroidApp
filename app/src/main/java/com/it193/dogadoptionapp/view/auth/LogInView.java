package com.it193.dogadoptionapp.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.retrofit.AccountApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.AnimationUtility;
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInView extends AppCompatActivity {

    private AccountApi accountApi;

    private EditText emailField;
    private EditText passwordField;
    private Button logInButton;
    private Button goToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_view);

        // Initialize AnimationUtility
        AnimationUtility.getInstance().initialize(this, getLayoutInflater());

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();
        accountApi = retrofitService.getRetrofit().create(AccountApi.class);

        // Get UI Fields
        emailField = findViewById(R.id.logInEmailField);
        passwordField = findViewById(R.id.logInPasswordField);
        logInButton = findViewById(R.id.logInButton);
        goToSignUp = findViewById(R.id.logInViewToSignUpView);

        // Log In Action
        goToSignUp.setOnClickListener(this::handleSwitchToSignUp);
        logInButton.setOnClickListener(this::handleLogInAction);
    }

    private void handleSwitchToSignUp(View v) {
        // Start New Activity
        startActivity(new Intent(LogInView.this, SignUpView.class));
    }

    private void handleLogInAction(View v) {
        // Get Raw Data
        String email = emailField.getText().toString();
        String passwd = passwordField.getText().toString();

        // Check if all fields have non-empty input
        if (!InputUtility.stringsAreNotNullOrEmpty(
                email, passwd
        )) {
            NotificationUtility.errorAlert(
                    this,
                    "Log In",
                    "Incomplete/Missing Inputs Found!!"
            );
            return;
        }

        // Construct Model
        Account existingAccount = new Account();
        existingAccount.setEmail(email);
        existingAccount.setPassword(passwd);

        // Attempt LogIn
        AnimationUtility.getInstance().startLoading();
        accountApi.userLogIn(existingAccount)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        AnimationUtility.getInstance().endLoading();
                        NotificationUtility.successAlert(
                                LogInView.this,
                                "Sign Up is Successful!" + response.body().getSessionAuthString()
                        );

                        // Save Account
                        AppStateStorage
                                .getInstance()
                                .setActiveAccount(
                                        response.body()
                                );
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        AnimationUtility.getInstance().endLoading();
                        NotificationUtility.errorAlert(
                                LogInView.this,
                                "Sign Up",
                                "Account Log In Attempt was Unsuccessful!"
                        );
                    }
                });
    }
}