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
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
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
 * 高德地图的工具类
 * Created by yjbo on 17/9/12.
 */

public class CRMMapViewUtil implements
        GeocodeSearch.OnGeocodeSearchListener,
        AMapLocationListener, PoiSearch.OnPoiSearchListener {
    //    LocationSource,
    private static GeocodeSearch geocoderSearch;
    private String addressName;
    private ExecutorService mExecutorService;
    protected static ProgressDialog progDialog = null;
    //定位
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
//    private OnLocationChangedListener mListener;

    public CRMMapViewUtil() {
    }

    /**
     * 初始化AMap对象
     */
    public void initMap(Context context) {

    }

    /**
     * 获取当前的位置
     */
    public CRMMapViewUtil getCurLoca(Context context) {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(context);
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
        return this;
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mapInterface.curAddr(new MapClass(amapLocation.getAddress(), ""
                        , amapLocation.getLatitude(), amapLocation.getLongitude()));
                mlocationClient.stopLocation();//只定位一次
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


    /**
     * 响应逆地理编码--通过经纬度进行查找地址
     */
    public void getAddress(Context context, final LatLonPoint latLonPoint) {
        if (latLonPoint == null) {
            return;
        }
        geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(this);
        showDialog(context);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200 * 1000,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    /**
     * 逆地理编码回调--通过经纬度进行查找地址
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                //result.getRegeocodeQuery().getPoint().getLatitude()
                mapInterface.latlonToAddress(new MapClass(addressName, "", 0.0, 0.0));
            } else {
                mapInterface.latlonToAddress(null);
            }
        } else {
            mapInterface.latlonToAddress(null);
        }
    }

    /**
     * 响应地理编码---通过地址去查找经纬度
     */
    public void getLatlon(Context context, String name, String city) {
        geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(this);
        showDialog(context);
        GeocodeQuery query = new GeocodeQuery(name, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，010
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 地理编码查询回调---通过地址去查找经纬度
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                List<GeocodeAddress> geocodeAddressList = result.getGeocodeAddressList();
                GeocodeAddress address = geocodeAddressList.get(0);
                addressName = address.getFormatAddress();
//                ToastUtil.show(mContext, addressName);
                mapInterface.AddressTolatlon(new MapClass(addressName, "",
                        address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude()));
            } else {
//                ToastUtil.show(mContext, "没有结果");
                mapInterface.latlonToAddress(null);
            }
        } else {
//            ToastUtil.showerror(mContext, rCode);
            mapInterface.latlonToAddress(null);
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
                            Log.e("yjbo-", "getAddresses: " + point.getLatitude() + "===" + point.getLongitude() + "===" + result.getFormatAddress());
                        }
                    } catch (AMapException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    /**
     * 显示进度条对话框
     */
    public static void showDialog(Context context) {
        progDialog = new ProgressDialog(context);
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
     * 查poi兴趣点
     *
     * @param context
     * @param keyWord 关键词
     * @param city    城市 不能为空
     */
    public CRMMapViewUtil getPosList(Context context, String keyWord, String city) {
        showDialog(context);
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码

        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
        return this;
    }

    //兴趣点返回的
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        dismissDialog();
        ArrayList<PoiItem> pois = poiResult.getPois();
        List<MapClass> poiList = new ArrayList<>();
        for (int h = 0; h < pois.size(); h++) {
            PoiItem poiItem = pois.get(h);
            MapClass mapClass = new MapClass(poiItem.getTitle(), "",
                    poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
            poiList.add(mapClass);
        }
        mapInterface.poiList(poiList);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    //接口回调
    MapInterface mapInterface;

    public void setMapInterface(MapInterface mInterface) {
        mapInterface = mInterface;
    }

    public interface MapInterface {
        void curAddr(MapClass mapClass);//当前位置

        void latlonToAddress(MapClass address);//经纬度转地址

        void AddressTolatlon(MapClass address);//地址转经纬度

        void poiList(List<MapClass> poiList);//获取兴趣点列表
    }
}
