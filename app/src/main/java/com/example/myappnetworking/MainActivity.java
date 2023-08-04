package com.example.myappnetworking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText edt_date;
    TextView txv_result;
    ImageView imv_result;
    Button btn_getValue, btn_list;
    Planetary currentPlanetary;

    private static final String TAG = MainActivity.class.getSimpleName();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_date = findViewById(R.id.edt_date);
        txv_result = findViewById(R.id.txv_result);
        imv_result = findViewById(R.id.imv_result);
        btn_getValue = findViewById(R.id.btn_getValue);
        btn_list = findViewById(R.id.btn_list);


        btn_getValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGetApi();
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
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
                            txv_result.setText(planetary.getTitle());
                            String date = planetary.getDate();
                            String title = planetary.getTitle();
                            String url = planetary.getUrl();

                            currentPlanetary = new Planetary(title, url, date);
                            Picasso.with(MainActivity.this).load(planetary.getUrl()).into(imv_result);

                            try {
                                callPostApi();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<Planetary> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Call API error", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void callPostApi() throws Exception {
        // Gọi API POST và nhận dữ liệu trả về
        PostApiServer.POST_API_SERVER.postPlanetary(currentPlanetary)
        .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Post Value",
                        Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    Log.d(TAG, "Dữ liệu đã được POST thành công!");
                    Toast.makeText(MainActivity.this, "Dữ liệu đã được POST thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "POST không thành công. Mã trạng thái: " + response.code());
                    Toast.makeText(MainActivity.this, "Dữ liệu chưa được POST!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Lỗi khi gọi API POST: " + t.getMessage());
            }

        });

    }

}