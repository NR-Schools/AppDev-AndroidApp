package com.it193.dogadoptionapp.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.retrofit.AccountApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInView extends AppCompatActivity {

    private AccountApi accountApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_view);

        RetrofitService retrofitService = new RetrofitService();
        accountApi = retrofitService.getRetrofit().create(AccountApi.class);

        // Initialize Components
        initComponents();
    }

    private void initComponents() {
        // Get Inputs
        EditText emailField = new EditText(this);
        EditText passwordField = new EditText(this);
        Button LogInButton = new Button(this);

        // Handle LogIn Action
        LogInButton.setOnClickListener(v -> {
            // Get Raw Data
            String email = emailField.getText().toString();
            String passwd = passwordField.getText().toString();

            // Check if all fields have non-empty input
            if (InputUtility.stringsAreNotNullOrEmpty(
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
            accountApi.userLogIn(existingAccount)
                    .enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            NotificationUtility.successAlert(
                                    LogInView.this,
                                    "Sign Up is Successful!"
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
                            NotificationUtility.errorAlert(
                                    LogInView.this,
                                    "Sign Up",
                                    "Account Log In Attempt was Unsuccessful!"
                            );
                        }
                    });
        });
    }
}