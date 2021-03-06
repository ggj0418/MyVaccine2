package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myvaccine2.Adapter.ApkAdapter;
import com.example.myvaccine2.App.AppData;

import java.util.ArrayList;
import java.util.List;

// 사용자 디바이스에 존재하는 패키지들을 리스트로 표시해주고, Spinner를 통해 조건별로 리스트를 구분해주는 액티비티
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

    int listViewIndex = 0, spinnerPosition = 0;
    boolean isHomePressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_apk_list);
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
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

    // Activity가 다시 시작되기 전에 호출, Actvitiy가 멈춘 후 호출되는 함수, Activity가 사용자에게 보여지기 직전에 호출되는 함수
    @Override
    protected void onStart() {
        initSetting();
        super.onStart();
    }

    // Activity가 비로소 화면에 보여지는 단계, 사용자에게 Focus를 잡은 상태
    @Override
    protected void onResume() { super.onResume(); }

    // Activity가 멈춰있다가 다시 호출될 때 불리는 함수, 즉 Stopped상태였을 때 다시 호출되어 시작될 때 불린다.
    @Override
    protected void onRestart() { super.onRestart(); }

    // Activity위에 다른 Activity가 올라와서 focus를 잃었을 때 호출되는 함수. (startActivity로 다른 액티비티 실행시 시작되는 함수)
    @Override
    protected void onPause() {
        super.onPause();
    }

    // Activity위에 다른 Activity가 완전히 올라와 화면에서 100% 가려질 때 호출되는 함수
    @Override
    protected void onStop() {
        packageManager = null;
        apkList = null;
        spinner = null;
        apkAdapter = null;

        packageList = null;
        packageListSort = null;

        super.onStop();
    }

    // Activity가 완전히 스택에서 없어질 때 호출되는 함수
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initSetting() {
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // 모든 패키지 선택 시, 디바이스에 있는 모든 패키지 출력
                    case SELECT_ALL:
                        packageListSort.clear();
                        listViewIndex = 0;

                        for (PackageInfo pi : packageList) {
                            packageListSort.add(pi);
                        }
                        apkAdapter.notifyDataSetChanged();
                        // 스피너 선택 시 생성되는 리스트는 항상 최상단에 포커싱이 가도록 유지
                        apkList.setSelection(listViewIndex);
                        break;
                    // 시스템 제외 패키지 선택 시, 시스템 어플리케이션을 제외한 패키지 출력
                    case SELECT_WITHOUT_SYSTEM:
                        packageListSort.clear();
                        listViewIndex = 0;

                        for (PackageInfo pi : packageList) {
                            boolean b = isSystemPackage(pi);
                            if (!b) {
                                packageListSort.add(pi);
                            }
                        }
                        apkAdapter.notifyDataSetChanged();
                        // 스피너 선택 시 생성되는 리스트는 항상 최상단에 포커싱이 가도록 유지
                        apkList.setSelection(listViewIndex);
                        break;
                    // 시스템 패키지만 선택 시, 시스템 어플리케이션에 해당하는 패키지 출력
                    case SELECT_ONLY_SYSTEM:
                        packageListSort.clear();
                        listViewIndex = 0;

                        for (PackageInfo pi : packageList) {
                            boolean b = isSystemPackage(pi);
                            if (b) {
                                packageListSort.add(pi);
                            }
                        }
                        apkAdapter.notifyDataSetChanged();
                        // 스피너 선택 시 생성되는 리스트는 항상 최상단에 포커싱이 가도록 유지
                        apkList.setSelection(listViewIndex);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 권한에 대한 정보를 받아오는 PackageManger를 통해 설치된 패키지들을 불러옴
        packageManager = getPackageManager();
        packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        packageListSort = new ArrayList<PackageInfo>();

        // 어뎁터 설정
        apkAdapter = new ApkAdapter(this, packageListSort, packageManager);
        apkList = (ListView) findViewById(R.id.applist);
        apkList.setAdapter(apkAdapter);
        apkList.setOnItemClickListener(this);

        // 홈버튼을 누른 뒤 돌아왔을 때, 해당하는 스피너 리스트 위치를 기억했다가 포커싱
        if(isHomePressed) {
            if(spinnerPosition == 0) {
                packageListSort.clear();
                for (PackageInfo pi : packageList) {
                    packageListSort.add(pi);
                }
                apkAdapter.notifyDataSetChanged();
                setListViewFocusing(listViewIndex);
            } else if(spinnerPosition == 1) {
                packageListSort.clear();
                for (PackageInfo pi : packageList) {
                    boolean b = isSystemPackage(pi);
                    if (!b) {
                        packageListSort.add(pi);
                    }
                }
                apkAdapter.notifyDataSetChanged();
                setListViewFocusing(listViewIndex);
            } else {
                packageListSort.clear();
                for (PackageInfo pi : packageList) {
                    boolean b = isSystemPackage(pi);
                    if (b) {
                        packageListSort.add(pi);
                    }
                }
                apkAdapter.notifyDataSetChanged();
                setListViewFocusing(listViewIndex);
            }
        }
    }

    @Override
    protected void onUserLeaveHint() {
        listViewIndex = apkList.getFirstVisiblePosition();
        spinnerPosition = spinner.getSelectedItemPosition();
        isHomePressed = true;
        super.onUserLeaveHint();
    }

    // 홈버튼으로 back에서 fore로 돌아온 경우 사용자가 보고 있던 위치 재 포커싱
    private void setListViewFocusing(int position) {
        apkList.setSelection(listViewIndex);
        listViewIndex = 0;
        isHomePressed = false;
    }
}
