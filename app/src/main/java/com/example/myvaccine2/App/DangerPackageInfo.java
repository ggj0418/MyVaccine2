package com.example.myvaccine2.App;

import android.content.pm.PackageInfo;

import java.util.List;

// 위험 패키지에 대한 위험도와 목록 관련 객체 클래스
public class DangerPackageInfo {
    public Integer dangerDegree;
    public PackageInfo dangerPackage;
    public List<String> dangerPermissionList;

    public DangerPackageInfo(Integer dangerDegree, PackageInfo dangerPackage, List<String> dangerPermissionList) {
        this.dangerDegree = dangerDegree;
        this.dangerPackage = dangerPackage;
        this.dangerPermissionList = dangerPermissionList;
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

    public List<String> getDangerPermissionList() {
        return dangerPermissionList;
    }

    public void setDangerPermissionList(List<String> dangerPermissionList) {
        this.dangerPermissionList = dangerPermissionList;
    }
}
