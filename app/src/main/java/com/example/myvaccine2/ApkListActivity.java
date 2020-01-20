package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myvaccine2.Adapter.ApkAdapter;
import com.example.myvaccine2.App.AppData;

import java.util.ArrayList;
import java.util.List;

public class ApkListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    PackageManager packageManager;
    ListView apkList;
    Spinner spinner;
    ApkAdapter apkAdapter;

    List<PackageInfo> packageList;
    List<PackageInfo> packageListSort;

    private static final int SELECT_ALL = 0;
    private static final int SELECT_WITHOUT_SYSTEM = 1;
    private static final int SELECT_ONLY_SYSTEM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_apk_list);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // 모든 패키지 선택 시, 디바이스에 있는 모든 패키지 출력
                    case SELECT_ALL:
                        packageList.clear();
                        for (PackageInfo pi : packageListSort) {
                            packageList.add(pi);
                        }
                        apkAdapter.notifyDataSetChanged();
                        break;
                    // 시스템 제외 패키지 선택 시, 시스템 어플리케이션을 제외한 패키지 출력
                    case SELECT_WITHOUT_SYSTEM:
                        packageList.clear();
                        for (PackageInfo pi : packageListSort) {
                            boolean b = isSystemPackage(pi);
                            if (!b) {
                                packageList.add(pi);
                            }
                        }
                        apkAdapter.notifyDataSetChanged();
                        break;
                    // 시스템 패키지만 선택 시, 시스템 어플리케이션에 해당하는 패키지 출력
                    case SELECT_ONLY_SYSTEM:
                        packageList.clear();
                        for (PackageInfo pi : packageListSort) {
                            boolean b = isSystemPackage(pi);
                            if (b) {
                                packageList.add(pi);
                            }
                        }
                        apkAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 권한을 얻는 PackageManager 초기화
        packageManager = getPackageManager();
        packageListSort = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        packageList = new ArrayList<PackageInfo>();

        // 초기에는 모든 패키지 출력
        for (PackageInfo pi : packageListSort) {
            packageList.add(pi);
        }

        apkAdapter = new ApkAdapter(this, packageList, packageManager);
        apkList = (ListView) findViewById(R.id.applist);
        apkList.setAdapter(apkAdapter);

        apkList.setOnItemClickListener(this);
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PackageInfo packageInfo = (PackageInfo) parent
                .getItemAtPosition(position);
        AppData appData = (AppData) getApplicationContext();
        appData.setPackageInfo(packageInfo);

        Intent appInfo = new Intent(getApplicationContext(), ApkInfoActivity.class);
        startActivity(appInfo);
    }
}
