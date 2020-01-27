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

        findViewByIdInit();
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
