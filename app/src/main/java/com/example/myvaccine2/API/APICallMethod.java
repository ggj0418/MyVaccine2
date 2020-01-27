package com.example.myvaccine2.API;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// REST API CALL 함수 및 응답값 파싱 함수
public class APICallMethod {
    private static APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    private static HashMap<String, String> body = new HashMap<String, String>();

    public static void findFromLog(String sms, String call, String phone) {
        body.put("SMS", sms);
        body.put("CALL", call);
        body.put("PHONE", phone);

        Call<ResponseBody> findFromLogCall = apiInterface.findFromLog(body);
        findFromLogCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.e("Point", "1");
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
