package com.it193.dogadoptionapp.repository;

import android.content.Context;

import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.AnimationUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogRequestRepository extends RepositoryBase {
    private DogApi dogApi;

    private DogRequestRepository(Context context) {
        super(context);

        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);
    }
    public static DogRequestRepository getRepository(Context ctx) {
        return new DogRequestRepository(ctx);
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

    public DogRequestRepository userViewAllDogRequest() {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();

        AnimationUtility.getInstance().startLoading();
        dogApi.userViewAllDogAdoptReq(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString()
        ).enqueue(new Callback<List<Dog>>() {
            @Override
            public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                AnimationUtility.getInstance().endLoading();
                callback.onResponseEvent(response.body(), null);
            }

            @Override
            public void onFailure(Call<List<Dog>> call, Throwable t) {
                AnimationUtility.getInstance().endLoading();
                callback.onResponseEvent(null, t.getMessage());
            }
        });

        return this;
    }

    public DogRequestRepository userCancelDogRequest(Dog dogReqItem) {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();
        Dog dog = new Dog();
        dog.setId(dogReqItem.getId());
        dog.setAdoptAccepted(dogReqItem.isAdoptAccepted());
        dog.setAdoptRequested(false);
        dog.setAccount(dogReqItem.getAccount());

        //AnimationUtility.getInstance().startLoading();
        dogApi.userCancelDogAdoptRequest(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString(),
                dog
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                //AnimationUtility.getInstance().endLoading();
                NotificationUtility.successAlert(
                        ctx,
                        "User Cancel Dog Request Successful"
                );
                callback.onResponseEvent(response.body(), null);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                //AnimationUtility.getInstance().endLoading();
                NotificationUtility.errorAlert(
                        ctx,
                        "Dog Request",
                        "User Cancel Dog Request Failed"
                );
                callback.onResponseEvent(null, t.getMessage());
            }
        });

        return this;
    }

    public DogRequestRepository adminViewAllDogRequest() {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();

        AnimationUtility.getInstance().startLoading();
        dogApi.adminViewAllDogAdoptReq(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString()
        ).enqueue(new Callback<List<Dog>>() {
            @Override
            public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                AnimationUtility.getInstance().endLoading();
                callback.onResponseEvent(response.body(), null);
            }

            @Override
            public void onFailure(Call<List<Dog>> call, Throwable t) {
                AnimationUtility.getInstance().endLoading();
                callback.onResponseEvent(null, t.getMessage());
            }
        });

        return this;
    }

    public DogRequestRepository adminConfirmRequest(Dog dogReqItem, boolean isAccepted) {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();
        Dog dog = new Dog();
        dog.setId(dogReqItem.getId());
        dog.setAdoptAccepted(isAccepted);
        dog.setAdoptRequested(dogReqItem.isAdoptRequested());
        dog.setAccount(dogReqItem.getAccount());

        dogApi.adminConfirmReq(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString(),
                dog
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                NotificationUtility.successAlert(
                        ctx,
                        "Admin Confirm Dog Request Successful"
                );
                callback.onResponseEvent(response.body(), null);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                NotificationUtility.errorAlert(
                        ctx,
                        "Dog Request",
                        "Admin Confirm Dog Request Failed"
                );
                callback.onResponseEvent(null, t.getMessage());
            }
        });

        return this;
    }
}
