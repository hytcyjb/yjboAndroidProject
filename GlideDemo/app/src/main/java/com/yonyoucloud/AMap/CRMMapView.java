package com.yonyoucloud.AMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yonyoucloud.AMap.util.AMapUtil;
import com.yonyoucloud.glidedemo.R;
import com.yonyoucloud.util.ToastUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yjbo on 17/9/5.
 */

public class CRMMapView extends MapView implements
        GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter,
        LocationSource, AMapLocationListener {
    private AMap aMap;
    private Marker regeoMarker;
    private GeocodeSearch geocoderSearch;
    private Context mContext;
    private String addressName;
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
    private ExecutorService mExecutorService;
    protected ProgressDialog progDialog = null;
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

    public CRMMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }

    /**
     * 初始化AMap对象
     */
    public void initMap(Bundle savedInstanceState, Context context) {
        mContext = context;
        onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = getMap();
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.drawable.feiji_market))));
            aMap.setOnMarkerClickListener(this);
        }
        geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(context);
        //绑定信息窗点击事件
        aMap.setOnInfoWindowClickListener(listener);
        aMap.setInfoWindowAdapter(this);//AMap类中

        setUpMap();

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        //更换地图定位图标
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        MyLocationStyle myLocationStyle1 = myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.centre_location));
        aMap.setMyLocationStyle(myLocationStyle1);
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
                Log.e("yjbo测试--", "LocationModeSourceActivity_Old类： onLocationChanged: ==" + amapLocation.getLatitude()
                        + "====" + amapLocation.getLongitude());
//                regeoMarker.setTitle(amapLocation.getAddress());
//                regeoMarker.setSnippet(amapLocation.getLatitude()
//                        + "====" + amapLocation.getLongitude());
            } else {
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

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
//        showDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(address
                        .getLatLonPoint()));
                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();
                ToastUtil.show(mContext, addressName);
            } else {
                ToastUtil.show(mContext, "没有结果");
            }
        } else {
            ToastUtil.showerror(mContext, rCode);
        }
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                ToastUtil.show(mContext, addressName);
            } else {
                ToastUtil.show(mContext, "没有结果");
            }
        } else {
            ToastUtil.showerror(mContext, rCode);
        }
    }

    /**
     * 响应逆地理编码的批量请求
     */
    public void getAddresses(List<LatLonPoint> geopointlist) {
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        for (final LatLonPoint point : geopointlist) {
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        RegeocodeQuery query = new RegeocodeQuery(point, 200,
                                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                        RegeocodeAddress result = geocoderSearch.getFromLocation(query);// 设置同步逆地理编码请求

                        if (result != null && result.getFormatAddress() != null) {
                            aMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(point.getLatitude(), point.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(getResources(), R.drawable.feiji2_market)))
                                    .title(result.getFormatAddress()));
                        }
                    } catch (AMapException e) {
                        Message msg = msgHandler.obtainMessage();
                        msg.arg1 = e.getErrorCode();
                        msgHandler.sendMessage(msg);
                    }
                }
            });
        }
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        showDialog();

        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    private Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            ToastUtil.showerror(mContext, msg.arg1);
        }
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
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

            arg0.setTitle("infowindow clicked");
        }
    };


    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
//如果想修改自定义Infow中内容，请通过view找到它并修改
        ((TextView) view.findViewById(R.id.info_title)).setText("标题：" + marker.getTitle());
        ((TextView) view.findViewById(R.id.info_content)).setText("内容：" + marker.getSnippet());
    }
}
