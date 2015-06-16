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
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.rangers.soccergo.R;

/**
 * LocatingActivity
 * Desc: 定位测试Activity
 * Date: 2015/6/6
 * Time: 21:24
 * Created by: Wooxxx
 */
public class LocatingActivity extends BaseActivity {

    private Button btnRequest;
    private Button btnGetSchool;

    private TextView tvProvince;
    private TextView tvSchool;

    LatLng latLng = null;

    // 定位Client
    public LocationClient locationClient = null;
    // 定位监听器
    public BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 定位成功后刷新省份信息
            tvProvince.setText(bdLocation.getProvince());
            latLng = new LatLng(
                    bdLocation.getLatitude(),
                    bdLocation.getLongitude()
            );
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
            sb.append("\nlatitude:");
            sb.append(bdLocation.getLatitude());
            sb.append("\nlongtitude");
            sb.append(bdLocation.getLongitude());
            Log.i("Location", sb.toString());
        }
    };

    // POI检索
    PoiSearch poiSearch = null;
    // POI检索监听器
    OnGetPoiSearchResultListener poiSearchResultListener
            = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            // 获取POI检索结果
            if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                if (poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Log.d("PoiSearch", poiResult.error.toString());
                }
                return;
            } else {
            }
            //遍历所有POI，找到类型为公交线路的POI
            for (PoiInfo poi : poiResult.getAllPoi()) {
                Log.d("School", poi.name);
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            // 获取POI详情结果
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
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

        // poi
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(poiSearchResultListener);


    }

    // 组件初始化
    private void initViews() {
        btnRequest = (Button) findViewById(R.id.btnRequest);
        btnGetSchool = (Button) findViewById(R.id.btnGetSchool);
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

        btnGetSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLng != null) {
//                    poiSearch.searchInCity(new PoiCitySearchOption()
//                            .city("成都")
//                            .keyword("学校")
//                            .pageNum(10));
                    poiSearch.searchNearby(new PoiNearbySearchOption()
                            .keyword("学校")
                            .location(latLng)
                            .radius(5000)
                            .pageNum(10));
                } else {
                    Log.d("LatLng", "is null");
                }
            }
        });
    }
}
