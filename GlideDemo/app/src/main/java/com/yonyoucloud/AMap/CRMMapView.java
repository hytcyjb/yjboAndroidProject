package com.yonyoucloud.AMap;

import android.content.Context;
import android.util.AttributeSet;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;

/**
 * Created by yjbo on 17/9/5.
 */

public class CRMMapView extends MapView {
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
}
