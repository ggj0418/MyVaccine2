package com.example.myvaccine2.App;

import android.app.Application;
import android.content.pm.PackageInfo;

// 패키지 정보 클래스
public class AppData extends Application {

    PackageInfo packageInfo;

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }
}
