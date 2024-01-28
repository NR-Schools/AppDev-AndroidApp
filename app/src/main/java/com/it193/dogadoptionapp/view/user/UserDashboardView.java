package com.it193.dogadoptionapp.view.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.view.shared.DogDetailsView;
import com.it193.dogadoptionapp.view.shared.DogRequestView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDashboardView extends AppCompatActivity {

    private DogApi dogApi;
    private List<Dog> dogs;

    private ListView dogListView;
    private Button goToUserDogRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard_view);

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);

        // Initialize Components and Data
        initComponents();
        initData();

        // Handle Actions
        handleActions();
    }

    private void initData() {
        dogApi.getAllDogs()
                .enqueue(new Callback<List<Dog>>() {
                    @Override
                    public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                        dogs = response.body();
                        UserDogListAdapter dogListAdapter = new UserDogListAdapter(
                                getApplicationContext(),
                                dogs
                        );
                        dogListView.setAdapter(dogListAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Dog>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
    }
    private void initComponents() {
        dogListView = findViewById(R.id.userDashboardDogList);
        goToUserDogRequest = findViewById(R.id.userDashboardViewToUserDogRequestView);
    }
    private void handleActions() {
        goToUserDogRequest.setOnClickListener(v -> {
            startActivity(new Intent(UserDashboardView.this, DogRequestView.class));
        });
        dogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserDashboardView.this, DogDetailsView.class);
                intent.putExtra("dogId", dogs.get(position).getId());
                startActivity(intent);
            }
        });
    }
}