package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.myvaccine2.API.APICallMethod;
import com.example.myvaccine2.API.APIClient;
import com.example.myvaccine2.API.APIInterface;
import com.example.myvaccine2.DTO.BeaconInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// DB에서 웹 비콘 정보를 받은 후 이를 디바이스에 심는 액티비티
public class VaccineReadyActivity extends AppCompatActivity {

    public List<String> beaconInfoList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vaccine_ready);

        // DB에서 받아온 비콘 정보를 리스트에 저장
        beaconInfoList = APICallMethod.getBeaconInfo();
//        APICallMethod.findFromLog("beam_1_SMS.jpg", "beam_2_Call.jpg", "beam_3_Phonebook.jpg");

        Toast.makeText(getApplicationContext(), beaconInfoList.get(0) + " " + beaconInfoList.get(1) + " " + beaconInfoList.get(2), Toast.LENGTH_SHORT).show();
    }
}
