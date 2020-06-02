package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myvaccine2.API.APICallMethod;
import com.example.myvaccine2.Privacy.Method;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

// DB에서 웹 비콘 정보를 받은 후 이를 디바이스에 심는 액티비티
public class VaccineReadyActivity extends AppCompatActivity {

    Disposable disposable;
    Context context;

    Button button;
    ImageView image1, image2, image3;

    public List<String> beaconInfoList;
    private static final int img1 = 0, img2 = 1, img3 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vaccine_ready);
//        APICallMethod.findFromLog("beam_1_SMS.jpg", "beam_2_Call.jpg", "beam_3_Phonebook.jpg");
    }

    @Override
    protected void onStart() {
        context = this;
        initSetting();

        super.onStart();
    }

    @Override
    protected void onResume() {
        // DB에서 받아온 비콘 정보를 리스트에 저장
        beaconInfoList = APICallMethod.getBeaconInfo();
        super.onResume();
    }

    @Override
    protected void onStop() {
        context = null;
        button = null;
        image1 = null;
        image2 = null;
        image3 = null;
        beaconInfoList = null;
        disposable = null;

        super.onStop();
    }

    private void initSetting() {
        button = (Button) findViewById(R.id.vra_start_button);
        image1 = (ImageView) findViewById(R.id.vra_sms_image);
        image2 = (ImageView) findViewById(R.id.vra_call_image);
        image3 = (ImageView) findViewById(R.id.vra_contact_image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSMSTask();
            }
        });
    }

    // AsyncTask를 대신하여 RxJava를 사용
    private void setSMSTask() {
        //onPreExecute

        //doInBackground
        disposable = Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Method.setSMS(beaconInfoList.get(0), context);

                return false;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object result) throws Exception {
                        //onPostExecute
                        image1.setVisibility(View.VISIBLE);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        disposable.dispose();
                        setCallTask();
                    }
                });
    }

    private void setCallTask() {
        //onPreExecute

        //doInBackground
        disposable = Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Method.setCvLog(beaconInfoList.get(1), context);

                return false;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object result) throws Exception {
                        //onPostExecute
                        image2.setVisibility(View.VISIBLE);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        disposable.dispose();
                        setContactTask();
                    }
                });
    }

    private void setContactTask() {
        //onPreExecute

        //doInBackground
        disposable = Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Method.setPhoneContact(beaconInfoList.get(2), context);

                return false;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object result) throws Exception {
                        //onPostExecute
                        image3.setVisibility(View.VISIBLE);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        disposable.dispose();
                        Toast.makeText(getApplicationContext(), "준비가 완료되었습니다", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
