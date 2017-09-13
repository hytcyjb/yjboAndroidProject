package com.yonyoucloud.AMap.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.yonyoucloud.AMap.CRMMapViewUtil;
import com.yonyoucloud.AMap.bean.MapClass;
import com.yonyoucloud.AMap.util.AMapUtil;
import com.yonyoucloud.glidedemo.R;

import java.util.List;

import static com.yonyoucloud.AMap.util.ChString.address;

/**
 * 测试地图工具类
 *
 * @aouto yjbo
 * @time 17/9/13 下午7:32
 */
public class testMapActivity extends AppCompatActivity implements View.OnClickListener {

    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);
        init();

    }

    private void init() {
        Button regeoButton = (Button) findViewById(R.id.geoButton);
        regeoButton.setOnClickListener(this);

        Button geoButton = (Button) findViewById(R.id.geoDZButton);
        geoButton.setOnClickListener(this);

        Button poiButton = (Button) findViewById(R.id.poiButton);
        poiButton.setOnClickListener(this);

        Button mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);

        CRMMapViewUtil crmMapViewUtil = new CRMMapViewUtil();
        crmMapViewUtil.getCurLoca(testMapActivity.this)
                .setMapInterface(new CRMMapViewUtil.MapInterface() {
                    @Override
                    public void curAddr(MapClass mapClass) {
                        Log.e("yjbo测试--", "testMapActivity类： curAddr: ==" + mapClass.toString());
                    }

                    @Override
                    public void latlonToAddress(MapClass mapClass) {
                    }

                    @Override
                    public void AddressTolatlon(MapClass mapClass) {

                    }

                    @Override
                    public void poiList(List<MapClass> poiList) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        CRMMapViewUtil crmMapViewUtil = new CRMMapViewUtil();

        switch (v.getId()) {
            case R.id.geoButton://响应逆地理编码按钮
                crmMapViewUtil.getAddress(testMapActivity.this, latLonPoint);
                crmMapViewUtil.setMapInterface(new CRMMapViewUtil.MapInterface() {
                    @Override
                    public void curAddr(MapClass mapClass) {
                    }

                    @Override
                    public void latlonToAddress(MapClass mapClass) {
                        Log.e("yjbo测试--", "testMapActivity类： latlonToAddress: ==" + mapClass.toString());
                    }

                    @Override
                    public void AddressTolatlon(MapClass mapClass) {

                    }

                    @Override
                    public void poiList(List<MapClass> poiList) {

                    }
                });

                break;
            case R.id.geoDZButton://响应地理编码按钮
                crmMapViewUtil.getLatlon(testMapActivity.this, "北京市朝阳区方恒国际中心A座", "");
                crmMapViewUtil.setMapInterface(new CRMMapViewUtil.MapInterface() {
                    @Override
                    public void curAddr(MapClass mapClass) {

                    }

                    @Override
                    public void latlonToAddress(MapClass mapClass) {
                    }

                    @Override
                    public void AddressTolatlon(MapClass mapClass) {
                        Log.e("yjbo测试--", "testMapActivity类： AddressTolatlon: ==" + mapClass.toString());
                    }

                    @Override
                    public void poiList(List<MapClass> poiList) {

                    }
                });
                break;
            case R.id.poiButton://搜索poi
                crmMapViewUtil.getPosList(testMapActivity.this, "肯德基", "北京")
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
                            for (int i = 0; i < poiList.size(); i++) {
                                MapClass mapClass = poiList.get(i);
                                Log.e("yjbo测试--", "testMapActivity类： poiList: ==" + mapClass.toString());
                            }
                        }
                    }
                });
                break;
            case R.id.mapButton://显示地图
                startActivity(new Intent(testMapActivity.this, ShowMapActivity.class));
                break;
            default:
                break;
        }
    }

}
