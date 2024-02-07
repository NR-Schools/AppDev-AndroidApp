package com.it193.dogadoptionapp.view.shared;

import android.content.Intent;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.MainActivity;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.repository.AccountRepository;
import com.it193.dogadoptionapp.storage.AppStateStorage;

public abstract class CustomDrawerView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected View customNavView;

    //region Filters
    protected EditText dogBreedFilter;
    protected EditText dogColorFilter;
    protected Spinner dogSizeFilter;
    protected Spinner dogSexFilter;
    protected EditText dogAgeFilter;
    //endregion

    public void initCustomDrawerView() {
        drawerInit();
        filterInit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public void drawerInit() {
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setBackgroundColor(getResources().getColor(R.color.white));
        customNavView = getLayoutInflater().inflate(R.layout.custom_nav_menu, navigationView, false);
        navigationView.addHeaderView(customNavView);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Dashboard" + "</font>"));

        // Get Account
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();

        // Logout handling
        Button logoutBtn = customNavView.findViewById(R.id.logoutbutton);
        logoutBtn.setOnClickListener(v -> {
            AccountRepository
                    .getRepository(this)
                    .logOut(currentAccount);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // Setup Proper Credentials in header
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.navHeaderUserName);
        TextView navUserEmail = headerView.findViewById(R.id.navHeaderUserEmail);

        navUsername.setText(currentAccount.getUsername());
        navUserEmail.setText(currentAccount.getEmail());
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // Filter Function
    public void filterInit() {
        // Get Filter Fields
        dogBreedFilter = customNavView.findViewById(R.id.breedFieldFilter);
        dogColorFilter = customNavView.findViewById(R.id.colorFieldFilter);
        dogSizeFilter = customNavView.findViewById(R.id.sizeFieldFilter);
        dogSexFilter = customNavView.findViewById(R.id.sexFieldFilter);
        dogAgeFilter = customNavView.findViewById(R.id.ageFieldFilter);

        // Get Buttons
        Button filterBtn = customNavView.findViewById(R.id.dogFilterBtn);
        Button resetFilterBtn = customNavView.findViewById(R.id.resetDogFilterBtn);

        // Handle Actions
        filterBtn.setOnClickListener(v -> handleFilterAction());
        resetFilterBtn.setOnClickListener(v -> handleResetFilterAction());
    }

    public void handleFilterAction() {
        // Close Drawer
        closeDrawer();
    }

    public void handleResetFilterAction() {
        dogBreedFilter.setText("");
        dogColorFilter.setText("");
        dogSizeFilter.setSelection(0);
        dogSexFilter.setSelection(0);
        dogAgeFilter.setText("");

        // Close Drawer
        closeDrawer();
    }
}
