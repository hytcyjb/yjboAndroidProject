package com.yonyoucloud.AMap.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yonyoucloud.AMap.CRMMapView;
import com.yonyoucloud.AMap.CRMMapViewUtil;
import com.yonyoucloud.AMap.bean.MapClass;
import com.yonyoucloud.AMap.util.AMapUtil;
import com.yonyoucloud.util.ToastUtil;
import com.yonyoucloud.glidedemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 地图显示
 *
 * @aouto yjbo
 * @time 17/9/4 下午10:27
 */
public class ShowMapActivity extends AppCompatActivity implements View.OnClickListener {
    private CRMMapView mapView;
    private TextView poi_txt;
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_base);
        mapView = (CRMMapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mapView.initMap(savedInstanceState, ShowMapActivity.this);
        initView();
        init();
    }

    private void initView() {
        findViewById(R.id.caozuo_1).setOnClickListener(this);
        findViewById(R.id.caozuo_2).setOnClickListener(this);
        findViewById(R.id.caozuo_3).setOnClickListener(this);
        findViewById(R.id.caozuo_4).setOnClickListener(this);
    }


    private void init() {
        poi_txt = (TextView) findViewById(R.id.poi_txt);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        mapView.deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroyMap();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.caozuo_1:
                //1.0 显示Marker标识
                mapView.showMarker(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), "测试地址");

                break;
            case R.id.caozuo_2:
                //1.1 显示当前人的位置
                mapView.setUpMap(0);
                mapView.setMapViewInterface(new CRMMapView.MapViewInterface() {
                    @Override
                    public void curAddr(MapClass mapClass) {
                        Log.e("yjbo测试--", "ShowMapActivity类： curAddr: ==" + mapClass.toString());
                    }
                });

                break;
            case R.id.caozuo_3:
                //1.2 获取兴趣点，并显示出来
                new CRMMapViewUtil().getPosList(ShowMapActivity.this, "肯德基", "北京")
                        .setMapInterface(new CRMMapViewUtil.MapInterface() {
                            @Override
                            public void curAddr(MapClass mapClass) {

                            }

                            @Override
                            public void latlonToAddress(MapClass mapClass) {
                            }

                            @Override
                            public void AddressTolatlon(MapClass mapClass) {
                            }

                            @Override
                            public void poiList(List<MapClass> poiList) {
                                if (poiList != null) {
                                    mapView.showMarkerList(poiList);
//                            for (int i = 0; i < poiList.size(); i++) {
//                                MapClass mapClass = poiList.get(i);
//                                Log.e("yjbo测试--", "testMapActivity类： poiList: ==" + mapClass.toString());
//                            }
                                }
                            }
                        });

                break;
            case R.id.caozuo_4:
                List<LatLng> latLngs = new ArrayList<LatLng>();
                latLngs.add(new LatLng(39.999391,116.135972));
                latLngs.add(new LatLng(39.898323,116.057694));
                latLngs.add(new LatLng(39.900430,116.265061));
                latLngs.add(new LatLng(39.955192,116.140092));
                //画线
                mapView.showLine(latLngs);
                break;
            default:
                break;
        }
    }


}