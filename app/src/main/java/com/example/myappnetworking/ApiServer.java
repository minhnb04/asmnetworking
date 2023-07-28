package com.example.myappnetworking;

import com.example.myappnetworking.model.Planetary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServer {
//    https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=2023-07-01
    Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    ApiServer API_SERVER = new Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .build()
            .create(ApiServer.class);

    @GET("planetary/apod")
    Call<Planetary> getPlanetary(@Query("api_key") String api_key,
                                 @Query("date") String date);

}
