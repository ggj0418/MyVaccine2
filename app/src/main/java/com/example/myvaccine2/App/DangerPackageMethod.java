package com.example.myvaccine2.App;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

// 위험한 패키지를 찾는 static 함수 선언
public class DangerPackageMethod {
    public static List<DangerPackageInfo> getDangerPackageInfo(List<PackageInfo> packageInfoList) {
        PackageInfo packageInfo;
        List<String> resultPermissionList = new ArrayList<String>();
        List<DangerPackageInfo> entireList = new ArrayList<DangerPackageInfo>();
        int count = 0;

        // 각각 패키지의 퍼미션을 DangerPermission과 비교
        for(int i = 0; i < packageInfoList.size(); i++) {
            packageInfo = packageInfoList.get(i);

            if (packageInfo.requestedPermissions != null) {
                for (int j = 0; j < packageInfo.requestedPermissions.length; j++) {
                    // 해당 인덱스의 퍼미션이 DangerPermission 안에 포함되어 있으면
                    if (DangerPermission.criticalPermissionList
                            .contains(packageInfo.requestedPermissions[j])) {
                        // 해당 퍼미션과 count를 각각의 리스트에 저장
                        count++;
                    }
                }
            }

            // 최종 리턴 리스트
            entireList.add(new DangerPackageInfo(count, packageInfo));
            count = 0;
            packageInfo = null;
        }


        return entireList;
    }
}
