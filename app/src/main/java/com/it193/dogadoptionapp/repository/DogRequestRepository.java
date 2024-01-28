package com.it193.dogadoptionapp.repository;

import android.content.Context;
import android.view.LayoutInflater;

import com.it193.dogadoptionapp.data.ResponseCallback;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.AnimationUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogRequestRepository {
    private Context ctx;
    private DogApi dogApi;
    private ResponseCallback callback;
    private DogRequestRepository(Context context) {
        AnimationUtility.getInstance().initialize(context, LayoutInflater.from(context));
        ctx = context;
        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);
    }
    public static DogRequestRepository getRepository(Context ctx) {
        return new DogRequestRepository(ctx);
    }

    public void setCallback(ResponseCallback callback) {
        this.callback = callback;
    }

    public DogRequestRepository userDogRequest(long dogId) {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();
        Dog dog = new Dog();
        dog.setId(dogId);

        AnimationUtility.getInstance().startLoading();
        dogApi.userDogAdopt(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString(),
                dog
        ).enqueue(new Callback<Dog>() {
            @Override
            public void onResponse(Call<Dog> call, Response<Dog> response) {
                AnimationUtility.getInstance().endLoading();
                NotificationUtility.successAlert(
                        ctx,
                        "User Request Dog is successful!"
                );
                callback.onResponseEvent(response.body(), null);
            }

            @Override
            public void onFailure(Call<Dog> call, Throwable t) {
                AnimationUtility.getInstance().endLoading();
                NotificationUtility.errorAlert(
                        ctx,
                        "User Request Dog",
                        "User Request Dog failed!"
                );
                callback.onResponseEvent(null, t.getMessage());
            }
        });

        return this;
    }
}
