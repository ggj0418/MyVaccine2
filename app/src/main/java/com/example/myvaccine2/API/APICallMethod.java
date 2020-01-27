package com.example.myvaccine2.API;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.MainThread;

import com.example.myvaccine2.DTO.BeaconInfo;
import com.example.myvaccine2.VaccineReadyActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// REST API CALL 함수 및 응답값 파싱 함수(비동기)
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
        // 최종 반환 리스트 초기화
        List<String> resultList = new ArrayList<>();
        final Call<List<BeaconInfo>> beaconInfoCall = apiInterface.getBeaconInfo();
        try {
            // 동기적으로 서버 응답값을 처리하기 위해 AysncTask 안에서 응답값 처리
            resultList = new AsyncTask<Void, Void, List<String>>() {
                @Override
                protected List<String> doInBackground(Void... voids) {
                    List<String> stringList = new ArrayList<String>();
                    List<BeaconInfo> list;

                    try {
                        // retrofit call을 동기적 실행
                        list = beaconInfoCall.execute().body();

                        if(list != null) {
                            for(int i=0;i<list.size();i++) {
                                BeaconInfo beaconInfo = list.get(i);
                                stringList.add(beaconInfo.getFileName());
                            }
                        } else {
                            stringList.add("None");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // resultList에 반환값 전달
                    return stringList;
                }
            }.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
