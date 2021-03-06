package com.edu.nuc.selftip.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.edu.nuc.selftip.MyApplication;
import com.edu.nuc.selftip.R;



public class MapsFragment extends Fragment {


    private com.baidu.mapapi.map.MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyApplication myApplication;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getContext().getApplicationContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        MyApplication myApplication = (MyApplication)getActivity().getApplication();

        mMapView = view.findViewById(R.id.map);

        BaiduMapOptions options = new BaiduMapOptions();
        mBaiduMap = mMapView.getMap();
        options.mapType(BaiduMap.MAP_TYPE_SATELLITE);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        //???????????????
        mLocationClient = new LocationClient(getContext());

        //??????LocationClientOption??????LocationClient????????????
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // ??????gps
        option.setCoorType("bd09ll"); // ??????????????????
        option.setScanSpan(1000);

        //??????locationClientOption
        mLocationClient.setLocOption(option);

        //??????LocationListener?????????
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //????????????????????????
        mLocationClient.start();

        LatLng cenpt = new LatLng(myApplication.Latitude,myApplication.Longitude);
        //??????????????????
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //??????MapStatusUpdate??????????????????????????????????????????????????????


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //??????????????????
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        //getActivity().setContentView(mMapView);



        return view;
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView ???????????????????????????????????????
            if (location == null || mMapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // ?????????????????????????????????????????????????????????0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}