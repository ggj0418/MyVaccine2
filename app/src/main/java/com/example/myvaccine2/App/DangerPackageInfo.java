package com.example.myvaccine2.App;

import android.content.pm.PackageInfo;

import java.util.List;

// 위험 패키지에 대한 위험도와 목록 관련 객체 클래스
public class DangerPackageInfo {
    public List<Integer> dangerDegreeList;
    public List<PackageInfo> dangerPackageList;

    public DangerPackageInfo(List<Integer> dangerDegreeList, List<PackageInfo> dangerPackageList) {
        this.dangerDegreeList = dangerDegreeList;
        this.dangerPackageList = dangerPackageList;
    }

    public void setDangerDegreeList(List<Integer> dangerDegreeList) {
        this.dangerDegreeList = dangerDegreeList;
    }

    public void setDangerPackageList(List<PackageInfo> dangerPackageList) {
        this.dangerPackageList = dangerPackageList;
    }

    public List<Integer> getDangerDegreeList() {
        return dangerDegreeList;
    }

    public List<PackageInfo> getDangerPackageList() {
        return dangerPackageList;
    }
}
