package com.example.myappnetworking;

import com.example.myappnetworking.model.Planetary;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostApiServer {
    static final String POST_URL = "https://server-networking.onrender.com/";

    PostApiServer POST_API_SERVER = new Retrofit.Builder()
            .baseUrl(POST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApiServer.class);

    @POST("/addPlanetary")
    Call<Void> postPlanetary(@Body Planetary Planetary);


    @POST("deletePlanetary/{id}")
    Call<Void> deletePlanetaryItem(@Path("id") String id);

    @POST("editPlanetary/{id}")
    @FormUrlEncoded
    Call<Void> editPlanetaryItem(
            @Path("id") String id,
            @Field("title") String title,
            @Field("url") String url,
            @Field("date") String date
    );


}
