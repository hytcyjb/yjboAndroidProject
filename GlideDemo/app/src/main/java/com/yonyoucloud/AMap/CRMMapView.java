package com.yonyoucloud.AMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yonyoucloud.AMap.bean.MapClass;
import com.yonyoucloud.AMap.util.AMapUtil;
import com.yonyoucloud.glidedemo.R;
import com.yonyoucloud.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yjbo on 17/9/5.
 */

public class CRMMapView extends MapView implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter,
        LocationSource, AMapLocationListener {
    private AMap aMap;
    private Marker regeoMarker;
    private Context mContext;
    private String addressName;

    private ExecutorService mExecutorService;
    //定位
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;

    public CRMMapView(Context context) {
        super(context);
    }

    public CRMMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CRMMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /**
     * 初始化AMap对象
     */
    public void initMap(Bundle savedInstanceState, Context context) {
        mContext = context;

        if (aMap == null) {
            aMap = getMap();
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.drawable.feiji_market))));
            aMap.setOnMarkerClickListener(this);
        }

        //绑定信息窗点击事件
        aMap.setOnInfoWindowClickListener(listener);
        aMap.setInfoWindowAdapter(this);//AMap类中

//        setUpMap();
    }

    /**
     * 设置一些amap的属性，并启动定位功能
     * 修改定位图片：myLocationStyle.myLocationIcon
     *
     * @param aMapIcon 修改定位的默认图片
     */
    public void setUpMap(int aMapIcon) {

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        if (aMapIcon == 0) {
//            aMapIcon = R.drawable.centre_location;
            //显示默认图片
        } else {
            //更换地图定位图标
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            MyLocationStyle myLocationStyle1 = myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(aMapIcon));
            aMap.setMyLocationStyle(myLocationStyle1);
        }
        setLocaMode(0);
    }

    private void setLocaMode(int i) {
        switch (i) {
            case 0:
                // 设置定位的类型为定位模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

                mLocationOption.setOnceLocation(true);
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
                break;
            case 1:
                // 设置定位的类型为 跟随模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                mLocationOption.setOnceLocation(false);
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
                break;
            case 2:
                // 设置定位的类型为根据地图面向方向旋转
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                mLocationOption.setOnceLocation(false);
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
                break;
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {

                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                mlocationClient.stopLocation();

                mapVewIinterface.curAddr(new MapClass(amapLocation.getAddress(), "",
                        amapLocation.getLatitude(), amapLocation.getLongitude()));
            } else {
                mapVewIinterface.curAddr(null);
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (TextUtils.isEmpty(marker.getTitle())) {
            return true;
        } else {
            marker.showInfoWindow();
        }
        return false;
    }

    /**
     * 方法必须重写
     */
    public void onDestroyMap() {
        super.onDestroy();
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    View infoWindow = null;

    //http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-marker
    @Override
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null) {
            infoWindow = LayoutInflater.from(mContext).inflate(
                    R.layout.custom_info_window, null);
        }
        render(marker, infoWindow);
        return infoWindow;
    }

    //http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-marker
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker arg0) {

//            arg0.setTitle("infowindow clicked");
        }
    };


    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        if ("".equals(marker.getTitle()) || marker.getPosition() == null) {
        } else {
            //如果想修改自定义Infow中内容，请通过view找到它并修改
            ((TextView) view.findViewById(R.id.info_title)).setText("标题：" + marker.getTitle());
            ((TextView) view.findViewById(R.id.info_content)).setText("内容：" + marker.getPosition().latitude
                    + "=" + marker.getPosition().longitude);
        }
    }

    //接口回调
    MapViewInterface mapVewIinterface;

    public void setMapViewInterface(MapViewInterface mInterface) {
        mapVewIinterface = mInterface;
    }

    public interface MapViewInterface {
        //showMarker添加Marker
        void curAddr(MapClass mapClass);//当前位置

    }


    /**
     * 地图上显示位置
     *
     * @param mapClassList 列表
     */
    public void showMarkerList(List<MapClass> mapClassList) {
        if (mapClassList == null) {
            return;
        }
        for (int i = 0; i < mapClassList.size(); i++) {
            MapClass mapClass = mapClassList.get(i);
            showMarker(new LatLng(mapClass.getLatitude(), mapClass.getLongitude()), mapClass.getAdressAll());
        }
    }

    /**
     * 地图上显示位置
     *
     * @param latLng
     */
    public void showMarker(LatLng latLng, String addressStr) {
        //这是在原来的marker上进行移动
//        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                latLng, 15));
//        regeoMarker.setPosition(latLng);
//        regeoMarker.setTitle(addressStr);
//        regeoMarker.setSnippet(latLng.latitude + "=" + latLng.longitude);

        //这里是新增marker
        aMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.feiji2_market)))
                .title(addressStr));
    }

    /**
     * 传入经纬度画线
     */
    public void showLine(List<LatLng> latLngs) {
        aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.RED));
        List<MapClass> mapClassList = new ArrayList<>();
        for (int i = 0; i < latLngs.size(); i++) {
            mapClassList.add(new MapClass("测试画线" + i, "", latLngs.get(i).latitude, latLngs.get(i).longitude));
        }
        showMarkerList(mapClassList);
//        //用一个数组来存放颜色，四个点对应三段颜色
//        List<Integer> colorList = new ArrayList<Integer>();
//        colorList.add(Color.RED);
//        colorList.add(Color.YELLOW);
//        colorList.add(Color.GREEN);
////		colorList.add(Color.BLACK);
//
//        PolylineOptions options = new PolylineOptions();
//        options.width(20);//设置宽度
//        //加入四个点
//        options.add(A,B,C,D);
//        //加入对应的颜色,使用colorValues 即表示使用多颜色，使用color表示使用单色线
//        options.colorValues(colorList);
//        aMap.addPolyline(options);

    }
}
