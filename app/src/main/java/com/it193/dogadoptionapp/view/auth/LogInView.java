package com.it193.dogadoptionapp.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.repository.AccountRepository;
import com.it193.dogadoptionapp.retrofit.AccountApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.AnimationUtility;
import com.it193.dogadoptionapp.utils.InputUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;
import com.it193.dogadoptionapp.view.admin.AdminDashboardView;
import com.it193.dogadoptionapp.view.user.UserDashboardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInView extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button logInButton;
    private TextView goToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_view);

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
        AccountRepository
                .getRepository(LogInView.this)
                .logIn(existingAccount)
                .setCallback(this::handleLogInResult);
    }

    private void handleLogInResult(Object responseObject, String errorMessage) {
        if (responseObject == null) {
            return;
        }

        // Determine whether to go to UserDashboard or AdminDashboard
        Account activeAccount = AppStateStorage.getInstance().getActiveAccount();

        Class classToRedirect = UserDashboardView.class;
        if (activeAccount.getEmail().equals("Admin"))
            classToRedirect = AdminDashboardView.class;

        startActivity(new Intent(LogInView.this, classToRedirect));

        //Clear Fields
        emailField.setText("");
        passwordField.setText("");
    }
}