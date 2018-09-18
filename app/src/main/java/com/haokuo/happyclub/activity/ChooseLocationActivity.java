package com.haokuo.happyclub.activity;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.PoiAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.haokuo.midtitlebar.MidTitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by zjf on 2018/9/14.
 */
@RuntimePermissions
public class ChooseLocationActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {
    public static final String EXTRA_LATITUDE = "com.haokuo.happyclub.extra.EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "com.haokuo.happyclub.extra.EXTRA_LONGITUDE";
    public static final String EXTRA_ADDRESS = "com.haokuo.happyclub.extra.EXTRA_ADDRESS";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.map_location)
    MapView mMapLocation;
    @BindView(R.id.rv_poi)
    RecyclerView mRvPoi;
    private AMap aMap;
    //    private AMapLocationClient mLocationClient;
    //    private OnLocationChangedListener mListener;
    //    private AMapLocationClientOption mLocationOption;
    private PoiAdapter mPoiAdapter;
    private boolean isCenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapLocation.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapLocation.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapLocation.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapLocation.onDestroy();
        //点位蓝点注销
        //        if (null != mLocationClient) {
        //            mMapLocation.onDestroy();
        //        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapLocation.onSaveInstanceState(outState);
    }

    @Override
    protected int initContentLayout() {
        return R.layout.activity_choose_location;
    }

    @Override
    protected void initData() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapLocation.getMap();
            //最大化缩放
            float maxZoomLevel = aMap.getMaxZoomLevel();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(maxZoomLevel + 10));
        }
        mRvPoi.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvPoi.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mPoiAdapter = new PoiAdapter(R.layout.item_poi);
        mRvPoi.setAdapter(mPoiAdapter);
        ChooseLocationActivityPermissionsDispatcher.startLocationWithPermissionCheck(this);
    }

    @Override
    protected void initListener() {
        mPoiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PoiItem item = mPoiAdapter.getItem(position);
                if (item != null) {
                    LatLonPoint latLonPoint = item.getLatLonPoint();
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_LATITUDE, latLonPoint.getLatitude());
                    intent.putExtra(EXTRA_LONGITUDE, latLonPoint.getLongitude());
                    intent.putExtra(EXTRA_ADDRESS, item.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void startLocation() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.strokeColor(ContextCompat.getColor(this, R.color.colorTransparent));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(ContextCompat.getColor(this, R.color.color25Blue));//设置定位蓝点精度圆圈的填充颜色的方法。
        //TODO 设置定位点图标样式
        //        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
        //                .decodeResource(getResources(), R.drawable.baoliao)))
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);
        // 设置定位监听
        aMap.setOnMyLocationChangeListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChooseLocationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //    @Override
    //    public void onLocationChanged(AMapLocation aMapLocation) {
    //        if (mListener != null && aMapLocation != null) {
    //            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
    //                double longitude = aMapLocation.getLongitude();
    //                double latitude = aMapLocation.getLatitude();
    //                if (!finishPoi) {
    //                    initPoi(longitude, latitude);
    //                }
    //                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
    //            } else {
    //                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
    //                Log.e("AmapErr", errText);
    //            }
    //        }
    //    }

    public void getPois(LatLng latLng) {
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude,
                latLng.longitude), 1000));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                ArrayList<PoiItem> pois = poiResult.getPois();
                mPoiAdapter.setNewData(pois);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (!isCenter) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    marker.setPosition(cameraPosition.target);
                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    LatLng target = cameraPosition.target;
                    marker.setPosition(target);
                    getPois(target);
                    //                    Animation animation = new RotateAnimation(marker.getRotateAngle(), marker.getRotateAngle() + 180, 0, 0, 0);
                    //                    long duration = 1000L;
                    //                    new LatLng(target.latitude,target.longitude);
                    //                    LatLng latLng1 = new LatLng(location.getLatitude() + 0.00001, location.getLongitude());
                    //                    TranslateAnimation animation1 = new TranslateAnimation(latLng1);
                    //                    animation.setDuration(duration);
                    //                    animation.setInterpolator(new LinearInterpolator());
                    //                    marker.setAnimation(animation);
                    //                    marker.startAnimation();
                }
            });
            isCenter = true;
        }
    }
}
