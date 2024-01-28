package com.it193.dogadoptionapp.view.shared;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogRequestView extends AppCompatActivity {

    private DogApi dogApi;
    private List<Dog> dogs;
    private ListView dogRequestListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_request_view);

        // Initialize Retrofit
        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);

        // Initialize Components and Data
        initComponents();
        initData();
    }

    private void initComponents() {
        dogRequestListView = findViewById(R.id.userDogRequestListView);
    }

    private void initData() {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();

        Call<List<Dog>> dogApiCall = dogApi.userViewAllDogAdoptReq(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString()
        );
        if (currentAccount.getEmail().equals("Admin")) {
            dogApiCall = dogApi.adminViewAllDogAdoptReq(
                    currentAccount.getEmail(),
                    currentAccount.getSessionAuthString()
            );
        }

        dogApiCall.enqueue(new Callback<List<Dog>>() {
                    @Override
                    public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                        dogs = response.body();
                        DogRequestListAdapter dogListAdapter = new DogRequestListAdapter(
                                getApplicationContext(),
                                dogs,
                                dogApi
                        );
                        dogRequestListView.setAdapter(dogListAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Dog>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
    }

}