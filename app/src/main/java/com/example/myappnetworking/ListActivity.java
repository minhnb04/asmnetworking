package com.example.myappnetworking;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myappnetworking.model.Planetary;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlanetaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                callApiListPlanetary();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                callApiListPlanetary();
            }
        });

        thread1.start();
        thread2.start();

    }

    private void callApiListPlanetary(){
        GetApiServer.GET_API_SERVER.getPlanetary()
                .enqueue(new Callback<List<Planetary>>() {

                    @Override
                    public void onResponse(Call<List<Planetary>> call, Response<List<Planetary>> response) {
                        if (response.isSuccessful()) {
                            List<Planetary> planetaryItems = response.body();
                            adapter = new PlanetaryAdapter(ListActivity.this,planetaryItems);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Planetary>> call, Throwable t) {
                        Toast.makeText(ListActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}