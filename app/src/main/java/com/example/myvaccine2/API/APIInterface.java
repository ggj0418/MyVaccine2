package com.example.myvaccine2.API;

import com.example.myvaccine2.DTO.BeaconInfo;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {
    // 서버 접속 로그에서 해당 비콘의 기록을 찾는 API
    @Headers("Content-Type: application/json")
    @POST("opensource/findFromLog")
    Call<ResponseBody> findFromLog(@Body HashMap<String, String> body);

    // DB로부터 웹 비콘 관련 정보를 받는 API
    @Headers("Content-Type: application/json")
    @GET("opensource/getBeaconInfo")
    Call<List<BeaconInfo>> getBeaconInfo();
}
