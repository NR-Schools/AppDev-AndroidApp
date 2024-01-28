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
import com.it193.dogadoptionapp.view.admin.AddDogRecordView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogRepository {
    private Context ctx;
    private DogApi dogApi;
    private ResponseCallback callback;
    private DogRepository(Context context) {
        AnimationUtility.getInstance().initialize(context, LayoutInflater.from(context));
        ctx = context;
        RetrofitService retrofitService = new RetrofitService();
        dogApi = retrofitService.getRetrofit().create(DogApi.class);
    }
    public static DogRepository getRepository(Context ctx) {
        return new DogRepository(ctx);
    }

    public void setCallback(ResponseCallback callback) {
        this.callback = callback;
    }

    public DogRepository addDogRecord(
            byte[] dogImageBytes, String name, String breed, String color, String age, String sex,
            String arrivedDate, String arrivedFrom, String dogSize, String location, String description
    ) {
        // Get Credentials
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();

        // Setup Request Bodies
        RequestBody dogImageRB = RequestBody.create(MediaType.parse("image/png"), dogImageBytes);
        MultipartBody.Part dogImagePart = MultipartBody.Part.createFormData("photoBytes", "file", dogImageRB);
        RequestBody dogNameRB = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody dogBreedRB = RequestBody.create(MediaType.parse("text/plain"), breed);
        RequestBody dogColorRB = RequestBody.create(MediaType.parse("text/plain"), color);
        RequestBody dogAgeRB = RequestBody.create(MediaType.parse("text/plain"), age);
        RequestBody dogSexRB = RequestBody.create(MediaType.parse("text/plain"), sex);
        RequestBody dogArrivedDateRB = RequestBody.create(MediaType.parse("text/plain"), arrivedDate);
        RequestBody dogArrivedFromRB = RequestBody.create(MediaType.parse("text/plain"), arrivedFrom);
        RequestBody dogSizeRB = RequestBody.create(MediaType.parse("text/plain"), dogSize);
        RequestBody dogLocationRB = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody dogDescriptionRB = RequestBody.create(MediaType.parse("text/plain"), description);

        AnimationUtility.getInstance().startLoading();
        dogApi.addNewDog(
                currentAccount.getEmail(), currentAccount.getSessionAuthString(),
                dogImagePart, dogNameRB, dogBreedRB, dogAgeRB, dogSexRB, dogColorRB,
                dogDescriptionRB, dogArrivedDateRB, dogArrivedFromRB, dogSizeRB, dogLocationRB
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                AnimationUtility.getInstance().endLoading();
                NotificationUtility.successAlert(
                        ctx,
                        "Add Dog Action Successfully!"
                );
                callback.onResponseEvent(response.body(), null);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                AnimationUtility.getInstance().endLoading();
                NotificationUtility.errorAlert(
                        ctx,
                        "Add Dog",
                        "Failed to add new dog!"
                );
                callback.onResponseEvent(null, t.getMessage());
            }
        });

        return this;
    }


}
