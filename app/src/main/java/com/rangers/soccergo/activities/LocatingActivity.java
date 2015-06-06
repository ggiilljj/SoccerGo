package com.rangers.soccergo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.rangers.soccergo.R;

/**
 * LocatingActivity
 * Desc:
 * Date: 2015/6/6
 * Time: 21:24
 * Created by: Wooxxx
 */
public class LocatingActivity extends BaseActivity {

    private Button btnRequest;

    private TextView tvProvince;
    private TextView tvSchool;

    // 定位Client
    public LocationClient locationClient = null;
    // 定位监听器
    public BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 定位成功后刷新省份信息
            tvSchool.setText(bdLocation.getProvince());
            // 停止定位
            //locationClient.stop();
            StringBuilder sb = new StringBuilder();
            String error = String.valueOf(bdLocation.getLocType());
            if (error.equals("161")) {

            }
            sb.append("\nerror:");
            sb.append(error);
            String city = bdLocation.getCity();
            sb.append("\ncity:");
            sb.append(city);
            String district = bdLocation.getDistrict();
            sb.append("\ndistrict:");
            sb.append(district);
            String street = bdLocation.getStreet();
            sb.append("\nstreet:");
            sb.append(street);
            String floor = bdLocation.getAddrStr();
            sb.append("\nfloor:");
            sb.append(floor);
            Log.i("Location", sb.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_locating);
        initViews();
        setListeners();
        //初始化百度定位
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(locationListener);
        //设置定位模式
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        locOption.setCoorType("bd09ll");
        locOption.setScanSpan(5000);
        locOption.setOpenGps(false);
        locOption.setIsNeedAddress(true);
        locationClient.setLocOption(locOption);
        locationClient.start();

    }

    // 组件初始化
    private void initViews() {
        btnRequest = (Button) findViewById(R.id.btnRequest);
        tvProvince = (TextView) findViewById(R.id.tvProvince);
        tvSchool = (TextView) findViewById(R.id.tvSchool);
    }

    // 各种监听
    private void setListeners() {
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后开始定位
                if (locationClient != null
                        && locationClient.isStarted())
                    locationClient.requestLocation();
            }
        });
    }
}
