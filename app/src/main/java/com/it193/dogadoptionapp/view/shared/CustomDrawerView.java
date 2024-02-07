package com.it193.dogadoptionapp.view.shared;

import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.R;

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
        resetFilterBtn.setOnClickListener(this::handleResetFilterAction);
    }

    public abstract void handleFilterAction();

    public void handleResetFilterAction(View v) {
        dogBreedFilter.setText("");
        dogColorFilter.setText("");
        dogSizeFilter.setSelection(0);
        dogSexFilter.setSelection(0);
        dogAgeFilter.setText("");
    }
}
