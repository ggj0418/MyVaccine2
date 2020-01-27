package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.myvaccine2.API.APICallMethod;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vaccine_ready);

        APICallMethod.findFromLog("beam_1_SMS.jpg", "beam_2_Call.jpg", "beam_3_Phonebook.jpg");
    }
}
