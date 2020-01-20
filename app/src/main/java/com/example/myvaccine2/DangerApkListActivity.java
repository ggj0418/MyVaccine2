package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.myvaccine2.Adapter.DangerApkAdapter;
import com.example.myvaccine2.App.DangerPackageInfo;
import com.example.myvaccine2.App.DangerPackageMethod;

import java.util.ArrayList;
import java.util.List;

public class DangerApkListActivity extends AppCompatActivity {

    Resources res;

    PackageManager packageManager;
    ListView dangerApkListView;
    DangerApkAdapter dangerApkAdapter;

    List<PackageInfo> packageList;
    List<DangerPackageInfo> dangerPackageInfoList = new ArrayList<DangerPackageInfo>();

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

        // 어뎁터 연결
        dangerApkAdapter = new DangerApkAdapter(dangerPackageInfoList, this, packageManager, res);
        dangerApkListView = (ListView) findViewById(R.id.dangerapplist);
        dangerApkListView.setAdapter(dangerApkAdapter);

        dangerApkAdapter.notifyDataSetChanged();
    }
}
