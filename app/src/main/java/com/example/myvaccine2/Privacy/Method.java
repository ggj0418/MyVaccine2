package com.example.myvaccine2.Privacy;

import android.app.Activity;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;

import android.widget.Toast;

import android.provider.CallLog;
import android.provider.ContactsContract;

import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Random;

import android.os.RemoteException;

public class Method {
    // SMS에 비콘을 심는 메서드
    public static void setSMS(String phoneNo, String smsText, Context context) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        // SMS case 처리
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        // 서버로부터 비콘을 제대로 수신하지 못한 경우
        if (smsText == null) {
            Toast.makeText(context, "비콘이 제대로 수신되지 않았습니다", Toast.LENGTH_SHORT).show();
        } else {
            SmsManager mSmsManager = SmsManager.getDefault();
            mSmsManager.sendTextMessage(phoneNo, null, smsText, sentIntent, deliveredIntent);
        }
    }

    // 통화기록에 비콘을 심는 메서드
    public static void setCvLog(String cvLogText, Context context) {
        Random rand = new Random();
        int duration = rand.nextInt(60) * 100 + rand.nextInt(60);
        int type = CallLog.Calls.OUTGOING_TYPE;
        String phoneNo = Integer.toString(rand.nextInt(1000000000)) + Integer.toString(rand.nextInt(1000000));

        ContentValues cv = new ContentValues();
        cv.put(CallLog.Calls.NUMBER, phoneNo);
        cv.put(CallLog.Calls.DURATION, duration);
        cv.put(CallLog.Calls.NEW, 1);
        cv.put(CallLog.Calls.DATE, System.currentTimeMillis());
        cv.put(CallLog.Calls.TYPE, type);
        cv.put(CallLog.Calls.CACHED_NAME, cvLogText);
        cv.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        cv.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
        cv.put(CallLog.Calls.CACHED_PHOTO_URI, 0);

        try {
            context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, cv);
        } catch (SecurityException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // 주소록에 비콘을 심는 메서드
    public static void setPhoneContact(Context context, String ctText) {

        Random rand = new Random();

        String phoneNo = Integer.toString(rand.nextInt(1000000000)) + Integer.toString(rand.nextInt(100));
        String label = Integer.toString(rand.nextInt(10000) / 1000 * 1000);

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        // DISPLAY NAME(성명)
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, Method.getName()).build());

        // 휴대폰 번호
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNo)
                .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, label)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        // EMAIL
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, Method.getEmail())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());

        //메모
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, ctText).build());

        try {
            // 연락처 제공자는 applyBatch()에서의 모든 작업을 하나의 트랜잭션으로서 수행
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        } catch (OperationApplicationException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    // 랜덤 한글 이름 생성
    public static String getName() {
        Random rand = new Random();
        StringBuffer buf = new StringBuffer();
        int size = 3;

        for(int i = 0; i < size; i++) {
            char c = (char)(rand.nextInt(11172) + 0xAC00);
            buf.append(c);
        }

        return buf.toString();
    }

    // 랜덤 메일 생성
    public static String getEmail() {
        Random rand = new Random();
        StringBuffer buf = new StringBuffer();

        // 11자리의 대소문자, 숫자가 포함된 랜덤한 문자열 생성
        for(int i=0;i<11;i++) {
            int index = rand.nextInt(3);
            switch (index) {
                // 소문자
                case 0:
                    buf.append((char) ((int) (rand.nextInt(26)) + 97));
                    break;
                // 대문자
                case 1:
                    buf.append((char) ((int) (rand.nextInt(26)) + 65));
                    break;
                // 숫자
                case 2:
                    buf.append(rand.nextInt(10));
                    break;
            }
        }

        String email = buf.toString() + "@gmail.com";

        return email;
    }
}