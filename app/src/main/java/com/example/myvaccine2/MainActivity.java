package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView funcImage1, funcImage2, funcImage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    // Activity가 다시 시작되기 전에 호출, Actvitiy가 멈춘 후 호출되는 함수, Activity가 사용자에게 보여지기 직전에 호출되는 함수
    @Override
    protected void onStart() {
        findViewByIdInit();
        super.onStart();
    }

    // Activity가 비로소 화면에 보여지는 단계, 사용자에게 Focus를 잡은 상태
    @Override
    protected void onResume() {
        super.onResume();
    }

    // Activity가 멈춰있다가 다시 호출될 때 불리는 함수, 즉 Stopped상태였을 때 다시 호출되어 시작될 때 불린다.
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // Activity위에 다른 Activity가 올라와서 focus를 잃었을 때 호출되는 함수. (startActivity로 다른 액티비티 실행시 시작되는 함수)
    @Override
    protected void onPause() {
        super.onPause();
    }

    // Activity위에 다른 Activity가 완전히 올라와 화면에서 100% 가려질 때 호출되는 함수
    @Override
    protected void onStop() {
        super.onStop();
    }

    // Activity가 완전히 스택에서 없어질 때 호출되는 함수
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 처리기 할당 및 클릭 이벤트 처리
    private void findViewByIdInit() {
        funcImage1 = (ImageView) findViewById(R.id.main_function1);
        funcImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toApkListIntent = new Intent(MainActivity.this, ApkListActivity.class);
                startActivity(toApkListIntent);
            }
        });
        funcImage2 = (ImageView) findViewById(R.id.main_function2);
        funcImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDangerApkListIntent = new Intent(MainActivity.this, DangerApkListActivity.class);
                startActivity(toDangerApkListIntent);
            }
        });
        funcImage3 = (ImageView) findViewById(R.id.main_function3);
        funcImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toVaccineReadyIntent = new Intent(MainActivity.this, VaccineReadyActivity.class);
                startActivity(toVaccineReadyIntent);
            }
        });
    }
}
