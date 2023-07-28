package com.example.myappnetworking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappnetworking.model.Planetary;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText edt_date;
    TextView txv_result, txv_resultBase64;
    ImageView imv_result;
    Button btn_getValue, btn_postValue;

    private static final String GET_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_date = findViewById(R.id.edt_date);
        txv_result = findViewById(R.id.txv_result);
        txv_resultBase64 = findViewById(R.id.txv_resultBase64);
        imv_result = findViewById(R.id.imv_result);
        btn_getValue = findViewById(R.id.btn_getValue);
        btn_postValue = findViewById(R.id.btn_postValue);

        btn_getValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGetApi();
            }
        });


        btn_postValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    callPostApi();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Call PostAPI error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

    private void callGetApi() {
        String date = edt_date.getText().toString();
        ApiServer.API_SERVER.getPlanetary("DEMO_KEY", date)
                .enqueue(new Callback<Planetary>() {
                    @Override
                    public void onResponse(Call<Planetary> call, Response<Planetary> response) {
                        Planetary planetary = response.body();
                        if(planetary != null){
                            txv_result.setText(planetary.getUrl());
                            Picasso.with(MainActivity.this).load(planetary.getUrl()).into(imv_result);
                        }
                    }
                    @Override
                    public void onFailure(Call<Planetary> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Call API error", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void callPostApi() throws Exception {
        Toast.makeText(MainActivity.this, "Chưa Post được!!!", Toast.LENGTH_LONG).show();
    }



}