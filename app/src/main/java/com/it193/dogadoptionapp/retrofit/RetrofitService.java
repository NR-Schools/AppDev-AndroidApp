package com.it193.dogadoptionapp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Base64;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }

    private Gson customGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Create Custom Deserializer for dog class
        JsonDeserializer<Dog> dogJsonDeserializer = (json, typeOfT, context) -> {

            JsonObject jsonObject = json.getAsJsonObject();

            Account account = null;
            if (!jsonObject.get("account").isJsonNull()) {
                account = new Gson().fromJson(jsonObject.get("account"), Account.class);
            }

            return new Dog(
                    jsonObject.get("id").getAsLong(),
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("breed").getAsString(),
                    jsonObject.get("age").getAsInt(),
                    jsonObject.get("sex").getAsString(),
                    jsonObject.get("colorCoat").getAsString(),
                    jsonObject.get("description").getAsString(),
                    LocalDate.parse(jsonObject.get("arrivedDate").getAsString()),
                    jsonObject.get("arrivedFrom").getAsString(),
                    jsonObject.get("size").getAsString(),
                    jsonObject.get("location").getAsString(),
                    Base64.getDecoder().decode(jsonObject.get("photoBytes").getAsString()),
                    jsonObject.get("adoptRequested").isJsonNull() ? null : jsonObject.get("adoptRequested").getAsBoolean(),
                    jsonObject.get("adoptAccepted").isJsonNull() ? null : jsonObject.get("adoptAccepted").getAsBoolean(),
                    account
            );
        };

        gsonBuilder.registerTypeAdapter(Dog.class, dogJsonDeserializer);
        return gsonBuilder.create();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:18080/api/")
                .addConverterFactory(GsonConverterFactory.create(this.customGson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
