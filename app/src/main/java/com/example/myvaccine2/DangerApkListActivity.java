package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myvaccine2.Adapter.DangerApkAdapter;
import com.example.myvaccine2.App.DangerPackageInfo;
import com.example.myvaccine2.App.DangerPackageMethod;

import java.util.ArrayList;
import java.util.List;

public class DangerApkListActivity extends AppCompatActivity {

    Resources res;

    PackageManager packageManager;
    ListView dangerApkListView;
    Spinner spinner;
    DangerApkAdapter dangerApkAdapter;

    List<PackageInfo> packageList;
    List<DangerPackageInfo> dangerPackageInfoList = new ArrayList<DangerPackageInfo>();
    List<DangerPackageInfo> dangerPackageInfoListCopy = new ArrayList<DangerPackageInfo>();

    private static final int SELECT_ALL = 0;
    private static final int SELECT_RED = 1;
    private static final int SELECT_YELLOW = 2;
    private static final int SELECT_GREEN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_danger_apk_list);

        // Drawable 사용을 위한 리소스 선언
        res = getResources();

        // 퍼미션 정보 열람 목적으로 설치된 패키지 불러오기
        packageManager = getPackageManager();
        packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        // 모든 패키지들을 위험도 여부 판단하는 함수로 넘김
        dangerPackageInfoList = DangerPackageMethod.getDangerPackageInfo(packageList);
        // 리스트 조건 정렬을 위해 동일한 값을 Copy 리스트에 저장
        dangerPackageInfoListCopy.addAll(dangerPackageInfoList);

        // 어뎁터 연결
        dangerApkAdapter = new DangerApkAdapter(dangerPackageInfoList, this, packageManager, res);
        dangerApkListView = (ListView) findViewById(R.id.dangerapplist);
        dangerApkListView.setAdapter(dangerApkAdapter);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // 모든 패키지 선택 시, 디바이스에 있는 모든 패키지 출력
                    case SELECT_ALL:
                        dangerPackageInfoList.clear();
                        dangerPackageInfoList.addAll(dangerPackageInfoListCopy);
                        dangerApkAdapter.notifyDataSetChanged();
                        break;
                    // 위험 패키지 선택 시, 위험군에 속하는 패키지 출력
                    case SELECT_RED:
                        dangerPackageInfoList.clear();
                        for (int i = 0; i < dangerPackageInfoListCopy.size(); i++) {
                            DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoListCopy.get(i);
                            if (dangerPackageInfo.getDangerDegree() <= 23 && dangerPackageInfo.getDangerDegree() > 15) {
                                dangerPackageInfoList.add(dangerPackageInfo);
                            }
                        }
                        dangerApkAdapter.notifyDataSetChanged();
                        break;
                    // 주의 패키지 선택 시, 주의해야할 패키지 출력
                    case SELECT_YELLOW:
                        dangerPackageInfoList.clear();
                        for (int i = 0; i < dangerPackageInfoListCopy.size(); i++) {
                            DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoListCopy.get(i);
                            if (dangerPackageInfo.getDangerDegree() <= 15 && dangerPackageInfo.getDangerDegree() > 7) {
                                dangerPackageInfoList.add(dangerPackageInfo);
                            }
                        }
                        dangerApkAdapter.notifyDataSetChanged();
                        break;
                    // 안전 패키지 선택 시, 퍼미션적으로 안전한 패키지 출력
                    case SELECT_GREEN:
                        dangerPackageInfoList.clear();
                        for (int i = 0; i < dangerPackageInfoListCopy.size(); i++) {
                            DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoListCopy.get(i);
                            if (dangerPackageInfo.getDangerDegree() <= 7 && dangerPackageInfo.getDangerDegree() >= 0) {
                                dangerPackageInfoList.add(dangerPackageInfo);
                            }
                        }
                        dangerApkAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
