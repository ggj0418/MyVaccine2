package com.example.myvaccine2.VPN;

import android.content.Intent;
import android.os.ParcelFileDescriptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class VpnService extends android.net.VpnService {
    private Thread mThread;
    private ParcelFileDescriptor mInterface;
    // 인터페이스를 위한 builder 객체 생성
    Builder builder = new Builder();

    // VPN 서비스 인터페이스
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 새로운 스레드 생성으로 새로운 세션 시작
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. TUN 인터페이스 구성 (TUN이란 커널에서 제공하는 NIC를 직접 제어하지 않고 소프트웨어적으로 구현된 가상 네트워크 장치를 제어하는 가상 네트워크 드라이버)
                    mInterface = builder.setSession("VPNService")
                            .addAddress("192.168.0.1", 24)
                            .addDnsServer("8.8.8.8")
                            .addRoute("0.0.0.0", 0)
                            .establish();
                    // 2-1. 발신된 패킷들은 여기 inputStream에 축적
                    FileInputStream in = new FileInputStream(mInterface.getFileDescriptor());
                    // 2-2. 수신된 패킷들은 outputStream에 기록
                    FileOutputStream out = new FileOutputStream(mInterface.getFileDescriptor());
                    // 3. UDP 채널은 ip package를 서버로 보내거나 서버에서 받는 것에 사용
                    DatagramChannel tunnel = DatagramChannel.open();
                    // 서버에 연결하면 localhost는 오직 데모용으로만 사용
                    // InetAddress.getlocalHost()를 127.0.0.1 대신 사용 가능
                    tunnel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8087));
                    // 4. 이 소켓을 보호하므로 패키지로 전송하면 VPN 서비스에 대한 피드백이 아님
                    // 앱의 터널 소켓을 시스템 VPN 외부에 유지하고 순환 연결을 방지
                    protect(tunnel.socket());

                    // 하나의 패킷에 대한 버퍼 할당
                    ByteBuffer packet = ByteBuffer.allocate(32767);
                    // 터널의 상태를 파악하기 위해 타이머를 사용
                    // 입출력 둘다에서 작동
                    // 양수값은 발신 중을 의미하며 다른 값들은 수신 중을 의미
                    // 수신부터 시작
                    int timer = 0;
                    Thread.sleep(1000);
                    // 문제가 발생하기 전까지 forward 상태 유지
                    while(true) {
                        // 첫 반복에서는 아무런 발생이 없다고 가정
                        boolean idle = true;
                        // inputStream에서 발신될 패킷을 read
                        int length = in.read(packet.array());
                        if (length > 0) {
                            // tunnel에 발신될 패킷을 write
                            packet.limit(length);
                            DatagramPacket datagramPacket = new DatagramPacket(packet.array(), packet.array().length);
                            byte[] dataInThePacket = datagramPacket.getData();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(mInterface != null) {
                            mInterface.close();
                            mInterface = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "VpnRunnable");

        return super.onStartCommand(intent, flags, startId);
    }
}
