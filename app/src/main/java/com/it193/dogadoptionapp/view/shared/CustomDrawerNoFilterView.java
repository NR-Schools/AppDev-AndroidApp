package com.it193.dogadoptionapp.view.shared;

import android.content.Intent;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.it193.dogadoptionapp.MainActivity;
import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.repository.AccountRepository;
import com.it193.dogadoptionapp.storage.AppStateStorage;

public abstract class CustomDrawerNoFilterView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected View customNavView;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public void drawerInit(String title){
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setBackgroundColor(getResources().getColor(R.color.white));
        customNavView = getLayoutInflater().inflate(R.layout.custom_nav_menu2, navigationView, false);
        navigationView.addHeaderView(customNavView);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + title + "</font>"));

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
}
