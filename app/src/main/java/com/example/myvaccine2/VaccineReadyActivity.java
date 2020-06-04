package com.example.myvaccine2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myvaccine2.LocalVPN.VpnService;

// DB에서 웹 비콘 정보를 받은 후 이를 디바이스에 심는 액티비티
public class VaccineReadyActivity extends AppCompatActivity {

    /*Disposable disposable;
    Context context;

    Button button;
    ImageView image1, image2, image3;

    public List<String> beaconInfoList;*/

    private static final int VPN_REQUEST_CODE = 0x0F;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";

    private boolean waitingForVPNStart;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            waitingForVPNStart = true;
            startService(new Intent(this, VpnService.class));
//            enableButton(false);
        }
    }

    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (VpnService.BROADCAST_VPN_STATE.equals(intent.getAction())) {
                if (intent.getBooleanExtra("running", false))
                    waitingForVPNStart = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vaccine_ready);
//        APICallMethod.findFromLog("beam_1_SMS.jpg", "beam_2_Call.jpg", "beam_3_Phonebook.jpg");

        final Button button = (Button) findViewById(R.id.vra_vpn_start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVPN();
                notificationSetting();
            }
        });
        final Button button2 = (Button) findViewById(R.id.vra_vpn_stop_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopVPNIntent = new Intent("stop");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(stopVPNIntent);
            }
        });
        waitingForVPNStart = false;
        LocalBroadcastManager.getInstance(this).registerReceiver(vpnStateReceiver, new IntentFilter(VpnService.BROADCAST_VPN_STATE));
    }

    private void startVPN() {
        Intent vpnIntent = VpnService.prepare(this);
        if (vpnIntent != null)
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);
        else
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
    }

    private void enableButton(boolean enable) {
        final Button vpnButton = (Button) findViewById(R.id.vra_vpn_start_button);
        if (enable) {
            vpnButton.setEnabled(true);
            vpnButton.setText(R.string.start_vpn);
        } else {
            vpnButton.setEnabled(false);
            vpnButton.setText(R.string.stop_vpn);
        }
    }

    // VPN 연결 혹은 해제 시 notification 팝업
    private void notificationSetting() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)) //BitMap 이미지 요구
                .setContentTitle(getString(R.string.app_name))
                .setContentText("VPN이 실행중입니다")
                // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                // .setStyle(new NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);
                // .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_mine); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName  = "노티페케이션 채널";
            String description = "오레오 이상을 위한 것임";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        } else
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(Integer.parseInt(NOTIFICATION_CHANNEL_ID), builder.build()); // 고유숫자로 노티피케이션 동작시킴
    }

    @Override
    protected void onResume() {
        // DB에서 받아온 비콘 정보를 리스트에 저장
//        beaconInfoList = APICallMethod.getBeaconInfo();
        super.onResume();
//        VpnService vpnService = new VpnService();
//        vpnService.cleanup();
//        enableButton(!waitingForVPNStart && !VpnService.isRunning());
    }
}

    /*@Override
    protected void onStart() {
        context = this;
        initSetting();
        super.onStart();
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
    }*/