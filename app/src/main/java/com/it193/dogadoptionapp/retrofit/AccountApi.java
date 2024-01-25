package com.it193.dogadoptionapp.retrofit;

import com.it193.dogadoptionapp.model.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountApi {

    @POST("/api/account/signup")
    Call<Boolean> userSignUp(@Body Account account);

    @POST("/api/account/login")
    Call<Account> userLogIn(@Body Account account);

    @POST("/api/account/logout")
    Call<Account> userLogOut(@Body Account account);

}
