package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;

import com.example.myvaccine2.App.DangerPackageInfo;
import com.example.myvaccine2.App.DangerPackageMethod;

import java.util.ArrayList;
import java.util.List;

public class DangerApkListActivity extends AppCompatActivity {

    PackageManager packageManager;
    ListView dangerApkListView;

    List<PackageInfo> packageList;
    List<DangerPackageInfo> dangerPackageInfoList = new ArrayList<DangerPackageInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_apk_list);

        packageManager = getPackageManager();
        packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        dangerPackageInfoList = DangerPackageMethod.getDangerPackageInfo(packageList);
    }
}
