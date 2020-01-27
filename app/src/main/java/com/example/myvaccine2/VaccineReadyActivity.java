package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.myvaccine2.API.APIClient;
import com.example.myvaccine2.API.APIInterface;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// DB에서 웹 비콘 정보를 받은 후 이를 디바이스에 심는 액티비티
public class VaccineReadyActivity extends AppCompatActivity {

    private APIInterface apiInterface;
    private HashMap<String, String> body = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vaccine_ready);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        body.put("SMS", "beam_1_SMS.jpg");
        body.put("CALL", "beam_2_Call.jpg");
        body.put("PHONE", "beam_3_Phonebook.jpg");

        Call<ResponseBody> findFromLogCall = apiInterface.findFromLog(body);
        findFromLogCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.e("Point", result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
