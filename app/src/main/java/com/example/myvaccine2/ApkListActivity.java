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

    List<PackageInfo> packageList1;
    List<PackageInfo> packageList;

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
                    case SELECT_ALL:
                        packageList1.clear();
                        for (PackageInfo pi : packageList) {
                            packageList1.add(pi);
                        }
                        apkAdapter.notifyDataSetChanged();
                        break;
                    case SELECT_WITHOUT_SYSTEM:
                        packageList1.clear();
                        for (PackageInfo pi : packageList) {
                            boolean b = isSystemPackage(pi);
                            if (!b) {
                                packageList1.add(pi);
                            }
                        }
                        apkAdapter.notifyDataSetChanged();
                        break;
                    case SELECT_ONLY_SYSTEM:
                        packageList1.clear();
                        for (PackageInfo pi : packageList) {
                            boolean b = isSystemPackage(pi);
                            if (b) {
                                packageList1.add(pi);
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
        packageManager = getPackageManager();
        packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        packageList1 = new ArrayList<PackageInfo>();

        for (PackageInfo pi : packageList) {
            packageList1.add(pi);
        }

        apkAdapter = new ApkAdapter(this, packageList1, packageManager);
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
