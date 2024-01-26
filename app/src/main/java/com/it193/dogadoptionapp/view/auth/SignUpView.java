package com.it193.dogadoptionapp.view.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.retrofit.AccountApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpView extends AppCompatActivity {

    private AccountApi accountApi;

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();
        accountApi = retrofitService.getRetrofit().create(AccountApi.class);

        // Get UI Fields
        nameField = new EditText(this);
        emailField = new EditText(this);
        passwordField = new EditText(this);
        confirmPasswordField = new EditText(this);
        signUpButton = new Button(this);

        // Sign Up Action
        handleSignUpAction();
    }

    private void handleSignUpAction() {
        // Handle SignUp Action
        signUpButton.setOnClickListener(v -> {
            // Get Raw Data
            String name = nameField.getText().toString();
            String email = emailField.getText().toString();
            String passwd = passwordField.getText().toString();
            String confirmPasswd = confirmPasswordField.getText().toString();

            // Check if all fields have non-empty input
            if (InputUtility.stringsAreNotNullOrEmpty(
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
            accountApi.userSignUp(newAccount)
                    .enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            NotificationUtility.successAlert(
                                    SignUpView.this,
                                    "Sign Up is Successful!"
                            );
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            NotificationUtility.errorAlert(
                                    SignUpView.this,
                                    "Sign Up",
                                    "Account was not successfully saved!"
                            );
                        }
                    });
        });
    }
}