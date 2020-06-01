package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.myvaccine2.App.AppData;

// ApkListActivity에서 클릭한 패키지 정보를 표시하는 액티비티
public class ApkInfoActivity extends AppCompatActivity {

    TextView appLabel, packageName, version, features;
    TextView permissions, andVersion, installed, lastModify, path;
    PackageInfo packageInfo;
    AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_apk_info);
    }

    private void findViewsById() {
        appLabel = (TextView) findViewById(R.id.applabel);
        packageName = (TextView) findViewById(R.id.package_name);
        version = (TextView) findViewById(R.id.version_name);
        features = (TextView) findViewById(R.id.req_feature);
        permissions = (TextView) findViewById(R.id.req_permission);
        andVersion = (TextView) findViewById(R.id.andversion);
        path = (TextView) findViewById(R.id.path);
        installed = (TextView) findViewById(R.id.insdate);
        lastModify = (TextView) findViewById(R.id.last_modify);
    }

    private void setValues() {
        // PackageManager에서 불러온 어플리케이션 이름 초기화
        appLabel.setText(getPackageManager().getApplicationLabel(packageInfo.applicationInfo));

        // PackageInfo에서 불러온 패키지 이름 초기화
        packageName.setText(packageInfo.packageName);

        // PackageInfo에서 불러온 버전명 초기화
        version.setText(packageInfo.versionName);

        // PackageInfo에서 불러온 타겟 SDK 버전 초기화
        andVersion.setText(Integer.toString(packageInfo.applicationInfo.targetSdkVersion));

        // PackageInfo에서 불러온 패키지 경로 초기화
        path.setText(packageInfo.applicationInfo.sourceDir);

        // PackageInfo에서 불러온 패키지 초기 설치 날짜 초기화
        installed.setText(setDateFormat(packageInfo.firstInstallTime));

        // PackageInfo에서 불러온 패키지 가장 최근 업데이트 날짜 초기화
        lastModify.setText(setDateFormat(packageInfo.lastUpdateTime));

        // PackageInfo에서 불러온 패키지 특징 초기화
        if (packageInfo.reqFeatures != null)
            features.setText(getFeatures(packageInfo.reqFeatures));
        else
            features.setText("-");

        // PackageInfo에서 불러온 패키지 퍼미션 초기화
        if (packageInfo.requestedPermissions != null)
            permissions.setText(getPermissions(packageInfo.requestedPermissions));
        else
            permissions.setText("-");
    }

    @SuppressLint("SimpleDateFormat")
    private String setDateFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = formatter.format(date);
        return strDate;
    }

    // PackageInfo에서 불러온 패키지 퍼미션들을 ',' 으로 구분자 삼아서 하나의 String 객체에 저장
    private String getPermissions(String[] requestedPermissions) {
        String permission = "";
        int size = requestedPermissions.length;
        for (int i = 0; i < size - 1; i++) {
            permission = permission + requestedPermissions[i] + ",\n";
        }
        permission = permission + requestedPermissions[size - 1];

        return permission;
    }

    // PackageInfo에서 불러온 패키지 특징들을 ',' 으로 구분자 삼아서 하나의 String 객체에 저장
    private String getFeatures(FeatureInfo[] reqFeatures) {
        String features = "";
        for (int i = 0; i < reqFeatures.length; i++) {
            features = features + reqFeatures[i] + ",\n";
        }
        return features;
    }

    @Override
    protected void onResume() {
        findViewsById();

        appData = (AppData) getApplicationContext();
        packageInfo = appData.getPackageInfo();

        setValues();

        super.onResume();
    }

    @Override
    protected void onStop() {
        packageInfo = null;
        appData = null;

        super.onStop();
    }
}
