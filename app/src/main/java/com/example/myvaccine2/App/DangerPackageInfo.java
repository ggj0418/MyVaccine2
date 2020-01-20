package com.example.myvaccine2.App;

import android.content.pm.PackageInfo;

// 위험 패키지에 대한 위험도와 목록 관련 객체 클래스
public class DangerPackageInfo {
    public Integer dangerDegree;
    public PackageInfo dangerPackage;
    public boolean internetBool;

    public DangerPackageInfo(Integer dangerDegree, PackageInfo dangerPackage, boolean internetBool) {
        this.dangerDegree = dangerDegree;
        this.dangerPackage = dangerPackage;
        this.internetBool = internetBool;
    }

    public Integer getDangerDegree() {
        return dangerDegree;
    }

    public void setDangerDegree(Integer dangerDegree) {
        this.dangerDegree = dangerDegree;
    }

    public PackageInfo getDangerPackage() {
        return dangerPackage;
    }

    public void setDangerPackage(PackageInfo dangerPackage) {
        this.dangerPackage = dangerPackage;
    }

    public boolean getInternetBool() {
        return internetBool;
    }

    public void setInternetBool(boolean internetBool) {
        this.internetBool = internetBool;
    }
}
