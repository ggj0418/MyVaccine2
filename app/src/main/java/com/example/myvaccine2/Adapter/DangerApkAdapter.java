package com.example.myvaccine2.Adapter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.myvaccine2.App.DangerPackageInfo;
import com.example.myvaccine2.R;

import java.util.List;

public class DangerApkAdapter extends BaseAdapter {

    List<DangerPackageInfo> dangerPackageInfoList;
    Activity context;
    PackageManager packageManager;
    Resources res;

    public DangerApkAdapter(List<DangerPackageInfo> dangerPackageInfoList, Activity context, PackageManager packageManager, Resources res) {
        super();
        this.dangerPackageInfoList = dangerPackageInfoList;
        this.context = context;
        this.packageManager = packageManager;
        this.res = res;
    }

    private class ViewHolder {
        TextView apkName;
        TextView internetBool;
        ImageView apkIcon;
        ImageView dangerIcon;
    }

    @Override
    public int getCount() {
        return dangerPackageInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return dangerPackageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.danger_apk_list_item, null);
            holder = new ViewHolder();

            holder.apkName = (TextView) convertView.findViewById(R.id.appname);
            holder.internetBool = (TextView) convertView.findViewById(R.id.internetBool);
            holder.apkIcon = (ImageView) convertView.findViewById(R.id.appicon);
            holder.dangerIcon = (ImageView) convertView.findViewById(R.id.dangericon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 인덱스로부터 DangerPackageInfo 객체 캐스팅
        DangerPackageInfo dangerPackageInfo = (DangerPackageInfo) getItem(position);

        // 리스트뷰에 넣을 객체 필드 초기화
        int dangerDegree = dangerPackageInfo.getDangerDegree();
        PackageInfo packageInfo = dangerPackageInfo.getDangerPackage();
        boolean internetBool = dangerPackageInfo.getInternetBool();

        String internet;
        if(internetBool)
            internet = "O";
        else
            internet = "X";
        // String degree = Integer.toString(dangerDegree);

        // packageInfo로부터 어플리케이션 기본정보 획득
        Drawable appIcon = packageManager.getApplicationIcon(packageInfo.applicationInfo);
        String appname = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();

        // 위험한 퍼미션 사용 개수에 따라 달라지는 아이콘 선언
        Drawable warning_red = ResourcesCompat.getDrawable(res, R.drawable.ic_mark_red, null);
        Drawable warning_yellow = ResourcesCompat.getDrawable(res, R.drawable.ic_mark_yellow, null);
        Drawable warning_green = ResourcesCompat.getDrawable(res, R.drawable.ic_mark_green, null);

        // 리스트뷰 아이템 값 설정
        holder.apkName.setText(appname);
        holder.internetBool.setText(internet);
        holder.apkIcon.setImageDrawable(appIcon);

        // 위험한 퍼미션 사용 개수에 따라 각기 다른 아이콘 설정
        if(dangerDegree <= 23 && dangerDegree > 15)
            holder.dangerIcon.setImageDrawable(warning_red);
        else if(dangerDegree <= 15 && dangerDegree > 7)
            holder.dangerIcon.setImageDrawable(warning_yellow);
        else if(dangerDegree <= 7 && dangerDegree > 0)
            holder.dangerIcon.setImageDrawable(warning_green);
        else    // 이 else 구문이 없으니 dangerDegree가 0인 값들은 warning red, yellow, green중에 랜덤한 값으로 출력되고 스크롤 때마다 값이 바뀌는 오류가 발생
            holder.dangerIcon.setImageDrawable(null);

        return convertView;
    }
}
