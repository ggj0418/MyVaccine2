package com.example.myvaccine2.API;

import android.util.Log;

import com.example.myvaccine2.DTO.BeaconInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// REST API CALL 함수 및 응답값 파싱 함수
public class APICallMethod {
    private static APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    private static HashMap<String, String> body = new HashMap<String, String>();

    // 접속로그에서 비콘에 관련된 로그 추출
    public static void findFromLog(String sms, String call, String phone) {
        // BODY 초기화
        body.clear();
        body.put("SMS", sms);
        body.put("CALL", call);
        body.put("PHONE", phone);

        // 선언해둔 findFromLog 메서드에 BODY 전달
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
                Log.e("findFromLogCall", "Fail", t);
            }
        });
    }

    // DB에서 웹 비콘 정보 획득
    public static List<String> getBeaconInfo() {
        // 리턴해줄 List 선언
        final List<String> resultList = new ArrayList<String>();
        // 선언해둔 getBeaconInfo 메서드 콜백 선언
        Call<List<BeaconInfo>> getBeaconInfoCall = apiInterface.getBeaconInfo();
        getBeaconInfoCall.enqueue(new Callback<List<BeaconInfo>>() {
            @Override
            public void onResponse(Call<List<BeaconInfo>> call, Response<List<BeaconInfo>> response) {
                ArrayList<BeaconInfo> list = (ArrayList<BeaconInfo>) response.body();
                // 로그 파싱에 쓰일 비콘 이름 획득
                if(list != null) {
                    for(int i=0;i<list.size();i++) {
                        BeaconInfo beaconInfo = list.get(i);
                        resultList.add(beaconInfo.getFileName());
                    }
                } else {
                    resultList.add("None");
                }
            }

            @Override
            public void onFailure(Call<List<BeaconInfo>> call, Throwable t) {
                Log.e("getBeaconInfoCall", "Fail", t);
            }
        });

        return resultList;
    }
}
