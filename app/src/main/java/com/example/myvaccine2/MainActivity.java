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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myvaccine2.Adapter.ApkAdapter;
import com.example.myvaccine2.App.AppData;
import com.example.myvaccine2.App.CriticalPermission;

import java.util.ArrayList;
import java.util.List;

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

            }
        });
        funcImage3 = (ImageView) findViewById(R.id.main_function3);
        funcImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
