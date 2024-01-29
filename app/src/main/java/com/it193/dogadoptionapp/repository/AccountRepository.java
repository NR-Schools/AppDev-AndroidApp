package com.it193.dogadoptionapp.repository;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import com.it193.dogadoptionapp.data.ResponseCallback;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.retrofit.AccountApi;
import com.it193.dogadoptionapp.retrofit.RetrofitService;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.AnimationUtility;
import com.it193.dogadoptionapp.utils.NotificationUtility;
import com.it193.dogadoptionapp.view.admin.AdminDashboardView;
import com.it193.dogadoptionapp.view.auth.LogInView;
import com.it193.dogadoptionapp.view.auth.SignUpView;
import com.it193.dogadoptionapp.view.user.UserDashboardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {

    private Context ctx;
    private AccountApi accountApi;
    private ResponseCallback callback;

    private AccountRepository(Context context) {
        AnimationUtility.getInstance().initialize(context);
        ctx = context;
        RetrofitService retrofitService = new RetrofitService();
        accountApi = retrofitService.getRetrofit().create(AccountApi.class);
    }
    public static AccountRepository getRepository(Context ctx) {
        return new AccountRepository(ctx);
    }

    public void setCallback(ResponseCallback callback) {
        this.callback = callback;
    }

    public AccountRepository signUp(Account account) {
        AnimationUtility.getInstance().startLoading();
        accountApi.userSignUp(account)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        AnimationUtility.getInstance().endLoading();
                        NotificationUtility.successAlert(
                                ctx,
                                "Sign Up is Successful!"
                        );
                        callback.onResponseEvent(response.body(), null);
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        AnimationUtility.getInstance().endLoading();
                        NotificationUtility.errorAlert(
                                ctx,
                                "Sign Up",
                                "Account was not successfully saved!"
                        );
                        callback.onResponseEvent(null, t.getMessage());
                    }
                });

        return this;
    }

    public AccountRepository logIn(Account account) {
        AnimationUtility.getInstance().startLoading();
        accountApi.userLogIn(account)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        AnimationUtility.getInstance().endLoading();
                        NotificationUtility.successAlert(
                                ctx,
                                "Log In is Successful!"
                        );

                        // Save Account
                        AppStateStorage
                                .getInstance()
                                .setActiveAccount(
                                        response.body()
                                );

                        callback.onResponseEvent(response.body(), null);
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        AnimationUtility.getInstance().endLoading();
                        NotificationUtility.errorAlert(
                                ctx,
                                "Sign Up",
                                "Account Log In Attempt was Unsuccessful!"
                        );
                        callback.onResponseEvent(null, t.getMessage());
                    }
                });

        return this;
    }

    public AccountRepository logOut(Account account) {
        AnimationUtility.getInstance().startLoading();
        accountApi.userLogOut(account)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        AnimationUtility.getInstance().endLoading();
                        NotificationUtility.successAlert(
                                ctx,
                                "Log Out is Successful!"
                        );
                        callback.onResponseEvent(response.body(), null);
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        AnimationUtility.getInstance().endLoading();
                        NotificationUtility.errorAlert(
                                ctx,
                                "Log Out",
                                "Log Out is Unsuccessful!"
                        );
                        callback.onResponseEvent(null, t.getMessage());
                    }
                });

        return this;
    }
}
