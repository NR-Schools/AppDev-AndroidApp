package com.it193.dogadoptionapp.retrofit;

import com.it193.dogadoptionapp.model.Dog;

import java.time.LocalDate;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DogApi {

    @Multipart
    @POST("/dog/add-dog")
    Call<Boolean> addNewDog(
            @Header("email") String email,
            @Header("session-auth-string") String sessionAuthString,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name,
            @Part("breed") RequestBody breed,
            @Part("age") RequestBody age,
            @Part("sex") RequestBody sex,
            @Part("colorCoat") RequestBody colorCoat,
            @Part("description") RequestBody description,
            @Part("arrivedDate") RequestBody arrivedDate,
            @Part("arrivedFrom") RequestBody arrivedFrom,
            @Part("size") RequestBody size,
            @Part("location") RequestBody location
    );

    @GET("/dog/dogs")
    Call<List<Dog>> getAllDogs();

    @GET("/dog/show-dog/{dogId}")
    Call<Dog> getDog(@Path("dogId") long dogId);

    @Multipart
    @PUT("/dog/update-dog")
    Call<Dog> updateDog(
            @Header("email") String email,
            @Header("session-auth-string") String sessionAuthString,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part file,
            @Part("isPhotoUpdated") RequestBody isPhotoUpdated,
            @Part("name") RequestBody name,
            @Part("breed") RequestBody breed,
            @Part("age") RequestBody age,
            @Part("sex") RequestBody sex,
            @Part("colorCoat") RequestBody colorCoat,
            @Part("description") RequestBody description,
            @Part("arrivedDate") RequestBody arrivedDate,
            @Part("arrivedFrom") RequestBody arrivedFrom,
            @Part("size") RequestBody size,
            @Part("location") RequestBody location
    );

    @DELETE("/dog/delete-dog/{dogId}")
    Call<Boolean> deleteDog(
            @Header("email") String email,
            @Header("session-auth-string") String sessionAuthString,
            @Path("dogId") long dogId
    );



    // For Adoption
    @GET("/dog-adopt/user-view-all-adopt-req")
    Call<List<Dog>> userViewAllDogAdoptReq(
            @Header("email") String email,
            @Header("session-auth-string") String sessionAuthString
    );

    @POST("/dog-adopt/user-dog-adopt")
    Call<Dog> userDogAdopt(
            @Header("email") String email,
            @Header("session-auth-string") String sessionAuthString,
            @Body Dog dog
    );

    @POST("/dog-adopt/user-cancel-dog-adopt-req")
    Call<Boolean> userCancelDogAdoptRequest(
            @Header("email") String email,
            @Header("session-auth-string") String sessionAuthString,
            @Body Dog dog
    );

    @GET("/dog-adopt/admin-view-all-adopt-req")
    Call<List<Dog>> adminViewAllDogAdoptReq(
            @Header("email") String email,
            @Header("session-auth-string") String sessionAuthString
    );

    @POST("/dog-adopt/admin-confirm-req")
    Call<Boolean> adminConfirmReq(
            @Header("email") String email,
            @Header("session-auth-string") String sessionAuthString,
            @Body Dog dog
    );
}
