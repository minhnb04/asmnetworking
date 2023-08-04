package com.example.myappnetworking;

import com.example.myappnetworking.model.Planetary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface GetApiServer {

    String BASE_URL = "https://server-networking.onrender.com/";

    Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    GetApiServer GET_API_SERVER = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .build()
            .create(GetApiServer.class);


    @GET("getPlanetary")
    Call<List<Planetary>> getPlanetary();

}
