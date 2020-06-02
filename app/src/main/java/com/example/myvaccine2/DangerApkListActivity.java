package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myvaccine2.Adapter.DangerApkAdapter;
import com.example.myvaccine2.App.DangerPackageInfo;
import com.example.myvaccine2.App.DangerPackageMethod;

import java.util.ArrayList;
import java.util.List;

// 전체 패키지 리스트에서 퍼미션에 의거한 위험도를 판단한 후, 이를 시각화해서 보여주는 액티비티
// Spinner를 이용하여 위험도로 리스트를 구분 가능
public class DangerApkListActivity extends AppCompatActivity {
    Resources res;

    PackageManager packageManager;
    ListView dangerApkListView;
    Spinner spinner;
    DangerApkAdapter dangerApkAdapter;

    List<PackageInfo> packageList;
    List<DangerPackageInfo> dangerPackageInfoList;
    List<DangerPackageInfo> dangerPackageInfoListCopy;

    private static final int SELECT_ALL = 0;
    private static final int SELECT_RED = 1;
    private static final int SELECT_YELLOW = 2;
    private static final int SELECT_GREEN = 3;

    int listViewIndex = 0, spinnerPosition = 0;
    boolean isHomePressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_danger_apk_list);
    }

    @Override
    protected void onStart() {
        initSetting();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        res = null;

        packageManager = null;
        dangerApkListView = null;
        spinner = null;
        dangerApkAdapter = null;

        packageList = null;
        dangerPackageInfoList = null;
        dangerPackageInfoListCopy = null;

        super.onStop();
    }

    private void initSetting() {
        // Drawable 사용을 위한 리소스 선언
        res = getResources();
        // onStop에서 해제 이후 재선언을 위한 코드
        dangerPackageInfoListCopy = new ArrayList<DangerPackageInfo>();

        // 퍼미션 정보 열람 목적으로 설치된 패키지 불러오기
        packageManager = getPackageManager();
        packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        // 모든 패키지들을 위험도 여부 판단하는 함수로 넘김
        dangerPackageInfoList = DangerPackageMethod.getDangerPackageInfo(packageList);

        // 어뎁터 연결
        dangerApkAdapter = new DangerApkAdapter(dangerPackageInfoListCopy, this, packageManager, res);
        dangerApkListView = (ListView) findViewById(R.id.dangerapplist);
        dangerApkListView.setAdapter(dangerApkAdapter);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // 모든 패키지 선택 시, 디바이스에 있는 모든 패키지 출력
                    case SELECT_ALL:
                        dangerPackageInfoListCopy.clear();
                        listViewIndex = 0;

                        dangerPackageInfoListCopy.addAll(dangerPackageInfoList);
                        dangerApkAdapter.notifyDataSetChanged();
                        // 스피너 선택 시 생성되는 리스트는 항상 최상단에 포커싱이 가도록 유지
                        dangerApkListView.setSelection(listViewIndex);
                        break;
                    // 위험 패키지 선택 시, 위험군에 속하는 패키지 출력
                    case SELECT_RED:
                        dangerPackageInfoListCopy.clear();
                        listViewIndex = 0;

                        for (int i = 0; i < dangerPackageInfoList.size(); i++) {
                            DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoList.get(i);
                            if (dangerPackageInfo.getDangerDegree() <= 23 && dangerPackageInfo.getDangerDegree() > 15) {
                                dangerPackageInfoListCopy.add(dangerPackageInfo);
                            }
                        }
                        dangerApkAdapter.notifyDataSetChanged();
                        // 스피너 선택 시 생성되는 리스트는 항상 최상단에 포커싱이 가도록 유지
                        dangerApkListView.setSelection(listViewIndex);
                        break;
                    // 주의 패키지 선택 시, 주의해야할 패키지 출력
                    case SELECT_YELLOW:
                        dangerPackageInfoListCopy.clear();
                        listViewIndex = 0;

                        for (int i = 0; i < dangerPackageInfoList.size(); i++) {
                            DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoList.get(i);
                            if (dangerPackageInfo.getDangerDegree() <= 15 && dangerPackageInfo.getDangerDegree() > 7) {
                                dangerPackageInfoListCopy.add(dangerPackageInfo);
                            }
                        }
                        dangerApkAdapter.notifyDataSetChanged();
                        // 스피너 선택 시 생성되는 리스트는 항상 최상단에 포커싱이 가도록 유지
                        dangerApkListView.setSelection(listViewIndex);
                        break;
                    // 안전 패키지 선택 시, 퍼미션적으로 안전한 패키지 출력
                    case SELECT_GREEN:
                        dangerPackageInfoListCopy.clear();
                        listViewIndex = 0;

                        for (int i = 0; i < dangerPackageInfoList.size(); i++) {
                            DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoList.get(i);
                            if (dangerPackageInfo.getDangerDegree() <= 7 && dangerPackageInfo.getDangerDegree() >= 0) {
                                dangerPackageInfoListCopy.add(dangerPackageInfo);
                            }
                        }
                        dangerApkAdapter.notifyDataSetChanged();
                        // 스피너 선택 시 생성되는 리스트는 항상 최상단에 포커싱이 가도록 유지
                        dangerApkListView.setSelection(listViewIndex);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 홈버튼을 누른 뒤 돌아왔을 때, 해당하는 스피너 리스트 위치를 기억했다가 포커싱
        if(isHomePressed) {
            if(spinnerPosition == 0) {
                dangerPackageInfoListCopy.clear();
                dangerPackageInfoListCopy.addAll(dangerPackageInfoList);
                dangerApkAdapter.notifyDataSetChanged();
                setListViewFocusing(listViewIndex);
            } else if(spinnerPosition == 1) {
                dangerPackageInfoListCopy.clear();

                for (int i = 0; i < dangerPackageInfoList.size(); i++) {
                    DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoList.get(i);
                    if (dangerPackageInfo.getDangerDegree() <= 23 && dangerPackageInfo.getDangerDegree() > 15) {
                        dangerPackageInfoListCopy.add(dangerPackageInfo);
                    }
                }
                dangerApkAdapter.notifyDataSetChanged();
                setListViewFocusing(listViewIndex);
            } else if(spinnerPosition == 2){
                dangerPackageInfoListCopy.clear();

                for (int i = 0; i < dangerPackageInfoList.size(); i++) {
                    DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoList.get(i);
                    if (dangerPackageInfo.getDangerDegree() <= 15 && dangerPackageInfo.getDangerDegree() > 7) {
                        dangerPackageInfoListCopy.add(dangerPackageInfo);
                    }
                }
                dangerApkAdapter.notifyDataSetChanged();
                setListViewFocusing(listViewIndex);
            } else {
                dangerPackageInfoListCopy.clear();

                for (int i = 0; i < dangerPackageInfoList.size(); i++) {
                    DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) dangerPackageInfoList.get(i);
                    if (dangerPackageInfo.getDangerDegree() <= 7 && dangerPackageInfo.getDangerDegree() >= 0) {
                        dangerPackageInfoListCopy.add(dangerPackageInfo);
                    }
                }
                dangerApkAdapter.notifyDataSetChanged();
                setListViewFocusing(listViewIndex);
            }
        }

    }

    @Override
    protected void onUserLeaveHint() {
        listViewIndex = dangerApkListView.getFirstVisiblePosition();
        spinnerPosition = spinner.getSelectedItemPosition();
        isHomePressed = true;
        super.onUserLeaveHint();
    }

    // 홈버튼으로 back에서 fore로 돌아온 경우 사용자가 보고 있던 위치 재 포커싱
    private void setListViewFocusing(int position) {
        dangerApkListView.setSelection(listViewIndex);
        listViewIndex = 0;
        isHomePressed = false;
    }
}
