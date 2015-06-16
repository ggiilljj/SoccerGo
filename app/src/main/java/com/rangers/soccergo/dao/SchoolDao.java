package com.rangers.soccergo.dao;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.rangers.soccergo.entities.School;

import java.util.List;

/**
 * SchoolDao
 * Desc:学校Dao
 * Team: Rangers
 * Date: 2015/4/9
 * Time: 7:50
 * Created by: Wooxxx
 */
public class SchoolDao extends BaseDao {

    private static AVQuery<School> query;


    @Override
    public String getClassName() {
        return School.CLASS_NAME;
    }

    /**
     * 异步地查询出省份索引对应的所有学校
     *
     * @param provinceIdx 省份索引
     * @param callback    查询回调接口
     */
    public static void findByProvince(int provinceIdx,
                                      final FindCallback<School> callback) {
        query = AVQuery.getQuery(School.class);
        query.whereEqualTo(School.PROVINCE_KEY, provinceIdx);
        query.findInBackground(callback);
    }

    /**
     * 同步地查询出省份索引对应的所有学校
     *
     * @param provinceIdx 省份索引
     * @return 学校列表
     */
    public static List<School> findByProvince(int provinceIdx) {
        query = AVQuery.getQuery(School.class);
        query.whereEqualTo(School.PROVINCE_KEY, provinceIdx);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步地查询出城市索引对应的所有学校
     *
     * @param cityIdx  城市索引
     * @param callback 查询回调接口
     */
    public static void findByCity(int cityIdx,
                                  final FindCallback<School> callback) {
        query = AVQuery.getQuery(School.class);
        query.whereEqualTo(School.CITY_KEY, cityIdx);
        query.findInBackground(callback);
    }

    /**
     * 同步地查询出城市索引对应的所有学校
     *
     * @param cityIdx 城市索引
     * @return 学校列表
     */
    public static List<School> findByCity(int cityIdx) {
        query = AVQuery.getQuery(School.class);
        query.whereEqualTo(School.CITY_KEY, cityIdx);
        try {
            return query.find();
        } catch (AVException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步地根据地理坐标获得附近学校
     *
     * @param ctx 上下文环境 (注意传入的是整个应用的会话环境)
     * @param latLng 地理位置
     * @param callback 查询回调接口
     */
    public static void findNearest(Context ctx, LatLng latLng,
                                   final FindCallback<School> callback) {
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
                }
                //遍历所有POI，找到类型为学校的POI
                //得到最近的学校
                String schoolName = poiResult.getAllPoi().get(0).name;
                //依据学校名称获得学校
                query = AVQuery.getQuery(School.class);
                query.whereEqualTo(School.NAME_KEY, schoolName);
                query.findInBackground(callback);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                // 获取POI详情结果
            }
        };
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(poiSearchResultListener);
        poiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword("学校")
                .location(latLng)
                .radius(5000)
                .pageNum(10));
    }



}
