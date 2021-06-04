package com.archy.android.amaptestapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.model.RouteOverlayOptions;
import com.amap.api.navi.view.RouteOverLay;
import com.archy.android.amaptestapplication.utils.LocationUtils;
import com.archy.android.amaptestapplication.utils.PhoneUtil;

import java.util.ArrayList;

import static com.archy.android.amaptestapplication.utils.TimeUtils.TIME_HOUR;
import static com.archy.android.amaptestapplication.utils.TimeUtils.TIME_MINUTE;


/**
 * @Author Archy Wang
 * @Date 2019-12-25
 * @Description
 */
public class StationSearchSmallMapFragment extends BaseFragment implements OnLocationMapCallBack, AMapNaviListener {


    private TextView mSmallDisTv;
    private TextView mSmallTimeTv;

    public static final String mBundleEndLat="mBundleEndLat";
    public static final String mBundleEndLng="mBundleEndLng";
    /***动态设置宽高**/
    public static final String mHeightWidth="mHeightWidth";


    public static final String DEFAULT="DEFAULT";
    public static final String MAIN_SMALL="MAIN_SMALL";
    public static final String MAIN_LARGE="MAIN_LARGE";

    private RelativeLayout mRelativeLayout;
    private boolean isPause=false;


    public static StationSearchSmallMapFragment newInstance(double endLat, double endLng, String type) {
        StationSearchSmallMapFragment fragment = new StationSearchSmallMapFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(mBundleEndLat,endLat);
        bundle.putDouble(mBundleEndLng,endLng);
        bundle.putString(mHeightWidth,type);

        fragment.setArguments(bundle);
        return fragment;
    }


    public static StationSearchSmallMapFragment newInstance() {
        StationSearchSmallMapFragment fragment = new StationSearchSmallMapFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private MapView mMapView;
    private AMap aMap;
    private NaviLatLng mNaviStart;
    private NaviLatLng mNaviEnd ;
    private AMapNavi aMapNavi;
    private RouteOverLay mCurRouteOverLay;

    @Override
    protected int getContentViewID() {
        Bundle arguments = getArguments();
        if (arguments!=null){

            switch (arguments.getString(mHeightWidth,DEFAULT)){
                case  DEFAULT:
                    return R.layout.station_search_small_fragment;

             /*   case MAIN_SMALL:
                    return R.layout.fragment_map_home_small;
                case MAIN_LARGE:
                    return R.layout.fragment_map_home_large;*/
            }
        }
        return R.layout.station_search_small_fragment;
    }

    public interface  MapBitMapCallBack{
        void callBack(long time, Bitmap bitmap, String disStr, String timeStr);
    }

    public MapBitMapCallBack mMapBitMapCallBack;

    public void setMapBitMapCallBack(MapBitMapCallBack mapBitMapCallBack) {
        mMapBitMapCallBack = mapBitMapCallBack;
    }

    @Override
    protected void initViewsAndData(View view) {
        mRelativeLayout = ((RelativeLayout) view.findViewById(R.id.map_parent_rl));
        mMapView = (MapView) view.findViewById(R.id.small_map_view);
        mSmallDisTv= (TextView) view.findViewById(R.id.small_distance_tv);
        mSmallTimeTv= (TextView) view.findViewById(R.id.small_time_tv);
        LinearLayout bottom_distance_ll=view.findViewById(R.id.bottom_distance_ll);
        Bundle arguments = getArguments();
        if (arguments!=null){
            double lat = arguments.getDouble(mBundleEndLat, 0);
            double lng = arguments.getDouble(mBundleEndLng, 0);
            mNaviEnd = new NaviLatLng(lat, lng);

            switch (arguments.getString(mHeightWidth,DEFAULT)){
                case  DEFAULT:
                    break;


                case MAIN_SMALL:
                case MAIN_LARGE:
                    bottom_distance_ll.setVisibility(View.GONE);
                    break;

            }

        }


        mMapView.onCreate(mSavedInstanceState);
        initMapInfo();

    }

    public void setStationLocation(double endLat,double endLng){
        Log.d("StationSearchSmallMapFr", "setStationLocation");
        AMapLocation aMapLocation = LocationUtils.getAMApLocation(mActivity);
        if (aMapLocation!=null&&aMapLocation.getLatitude()!=0&&aMapLocation.getLongitude()!=0){
            LatLng curLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mNaviStart = new NaviLatLng(curLatLng.latitude, curLatLng.longitude);
            Log.d("StationSearchSmallMapFr", "mNaviStart:"+mNaviStart.getLatitude()+"--"+mNaviStart.getLongitude());
        }
        mNaviEnd = new NaviLatLng(endLat, endLng);
        Log.d("StationSearchSmallMapFr", "mNaviEnd:"+mNaviEnd.getLatitude()+"--"+mNaviEnd.getLongitude());
        calculateDriveRoute();

    }

    @Override
    public void onResume() {
        super.onResume();
        isPause=false;
        if(reLocationFlag){
            LocationUtils.startNewBDLocation(mActivity,this);
        }else {
            calculateRoute();
        }
        mMapView.onResume();

    }

    private void initMapInfo() {
        LocationUtils.startNewBDLocation(mActivity, this);
        aMap = mMapView.getMap();
        aMap.getUiSettings().setTiltGesturesEnabled(false); //倾斜手势
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false); // 去除缩放按钮
        aMap.getUiSettings().setScaleControlsEnabled(true); // 控制比例尺
        aMap.getUiSettings().setAllGesturesEnabled(false); // 控制比例尺
        aMap.getUiSettings().setLogoBottomMargin(-200);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
    }
    private boolean initLocationFlag = false; // 首次进入需要定位
    private boolean reLocationFlag = false;
    private boolean initRouteFlag = true; // 首次路径规划
    @Override
    public void onLocationMapSucceed() {
        AMapLocation aMapLocation = LocationUtils.getAMApLocation(mActivity);
        LatLng curLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        if (!initLocationFlag||reLocationFlag) {
            mNaviStart = new NaviLatLng(curLatLng.latitude, curLatLng.longitude);
            initLocationFlag = true;
            if (reLocationFlag){
                initRouteFlag=true;
                calculateRoute();
                reLocationFlag=false;
            }
        }
    }

    @Override
    public void onLocationMapFailure() {

    }


    private void calculateRoute() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    aMapNavi = AMapNavi.getInstance(mActivity);
                    aMapNavi.addAMapNaviListener(StationSearchSmallMapFragment.this);
                    if(initRouteFlag){
                        if(null == mNaviStart){
                            AMapLocation aMapLocation = LocationUtils.getAMApLocation(mActivity);
                            mNaviStart = new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        }
                        calculateDriveRoute(); //计算驾车路径
                        initRouteFlag = false;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        },400l);
    }

    // 起点终点列表
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mCurrentWayPoint=new ArrayList<>();

    boolean isFirstDefault=true;

    // 计算驾车路线
    private void calculateDriveRoute() {



        mStartPoints.clear();
        mEndPoints.clear();
        mStartPoints.add(mNaviStart);
        mEndPoints.add(mNaviEnd);
        boolean isSuccessFlag = false;
        try{
            if(aMapNavi==null){
                calculateRoute();
            }
            else {
            isSuccessFlag = aMapNavi.calculateDriveRoute(mStartPoints, mEndPoints, mCurrentWayPoint, PathPlanningStrategy.DRIVING_DEFAULT);
            }
//            if (mMapBitMapCallBack!=null&&isFirstDefault&&mNaviStart.getLatitude()>1d&&mNaviStart.getLongitude()>1d){
//                isFirstDefault=false;
//                mMapBitMapCallBack.callBack(0,BitmapFactory.decodeResource(getResources(), R.drawable.small_location_defatult, null),"","");
//            }

        }catch(Exception e){
            e.printStackTrace();
        }

        if (!isSuccessFlag) {
            calculateRouteError();
        }
    }

    private void calculateRouteError() {

//        if (!isPause){
//            if (mActivity==null){
//                ToolUtils.showToast(TeldCarApplication.sIntance, "定位失败,请检查网络或是否允许定位", Toast.LENGTH_SHORT);
//            }else {
//                ToolUtils.showToast(mActivity, "定位失败,请检查网络或是否允许定位", Toast.LENGTH_SHORT);
//            }
//        }



//        stationDistance = "路径规划失败";
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        Log.d("StationSearchSmallMapFr", "i:" + i);
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }



    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        if (mCurRouteOverLay!=null){
            mCurRouteOverLay.removeFromMap();
        }
        BitmapFactory.Options op3 = new BitmapFactory.Options();
        op3.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap pointBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mappoint_nullcolor);
        Bitmap enterPoint = BitmapFactory.decodeResource(getResources(), R.drawable.small_map_end_icon,op3);
        Bitmap startPoint = BitmapFactory.decodeResource(getResources(), R.drawable.small_map_start_icon,op3);
        // 获取路径规划线路，显示到地图上
        AMapNaviPath naviPath = aMapNavi.getNaviPath();
        mCurRouteOverLay = new RouteOverLay(aMap, naviPath, mActivity);
        RouteOverlayOptions routeOverlayOptions = new RouteOverlayOptions();
        routeOverlayOptions.setNormalRoute(BitmapFactory.decodeResource(getResources(), R.drawable.small_map_route_line_arr));
        routeOverlayOptions.setUnknownTraffic(BitmapFactory.decodeResource(getResources(),R.drawable.small_map_route_line_arr));
        routeOverlayOptions.setSmoothTraffic(BitmapFactory.decodeResource(getResources(), R.drawable.small_map_route_line_arr));
        routeOverlayOptions.setSlowTraffic(BitmapFactory.decodeResource(getResources(), R.drawable.small_map_route_line_arr));
        routeOverlayOptions.setJamTraffic(BitmapFactory.decodeResource(getResources(), R.drawable.small_map_route_line_arr));
        routeOverlayOptions.setVeryJamTraffic(BitmapFactory.decodeResource(getResources(), R.drawable.small_map_route_line_arr));
        routeOverlayOptions.setLineWidth(40);
        mCurRouteOverLay.setLightsVisible(false);
        mCurRouteOverLay.showStartMarker(true);
        mCurRouteOverLay.setArrowOnRoute(true);
        mCurRouteOverLay.setStartPointBitmap(startPoint);
        mCurRouteOverLay.setEndPointBitmap(enterPoint);
        mCurRouteOverLay.setWayPointBitmap(pointBitmap);
        mCurRouteOverLay.setCartoFootBitmap(pointBitmap);
        mCurRouteOverLay.setRouteOverlayOptions(routeOverlayOptions);
        mCurRouteOverLay.addToMap();
        String distance = aMapPathFormat(naviPath.getAllLength());
        String[] timeStrs = convertToTimeStr(naviPath.getAllTime());
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(timeStrs[0])){
            sb.append(timeStrs[0]).append("小时");
        }
        if (!TextUtils.isEmpty(timeStrs[1])){
            sb.append(timeStrs[1]).append("分钟");
        }
        Log.d("StationSearchSmallMapFr", "行驶" + distance);
        Log.d("StationSearchSmallMapFr", "约"+sb.toString()+"到达");
        mSmallDisTv.setText("行驶"+distance);
        mSmallTimeTv.setText("约"+sb.toString()+"到达");

        if (mCurRouteOverLay != null && mCurRouteOverLay.getAMapNaviPath().getCoordList().size() > 0){
//			mCurRouteOverLay.zoomToSpan( (int) PhoneUtil.dip2px(100), (int) PhoneUtil.dip2px(100), (int) PhoneUtil.dip2px(133), (int) PhoneUtil.dip2px(120),mCurRouteOverLay.getAMapNaviPath());
            try {
                //	newLatLngBoundsRect(LatLngBounds latlngbounds, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom)
                CameraUpdate var7 = CameraUpdateFactory.newLatLngBoundsRect(mCurRouteOverLay.getAMapNaviPath().getBoundsForPath(),
//                        (int) PhoneUtil.dip2px(12), (int) PhoneUtil.dip2px(12), (int) PhoneUtil.dip2px(22), (int) PhoneUtil.dip2px(20));
                        (int) PhoneUtil.dip2px(2), (int) PhoneUtil.dip2px(2), (int) PhoneUtil.dip2px(15), (int) PhoneUtil.dip2px(0));
                aMap.animateCamera(var7, 20L,null);
                setCutPic(50);
                setCutPic(200);
                setCutPic(600);
                setCutPic(1000);

            }catch (Exception e){
                e.printStackTrace();
            }


        }



    }

    private void setCutPic(long time) {
        mSmallDisTv.postDelayed(() -> {
            Log.d("StationSearchSmallMapFr", "aMap.getMaxZoomLevel():" + aMap.getMaxZoomLevel());
            aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
                @Override
                public void onMapScreenShot(Bitmap bitmap) {
                    mMapBitMapCallBack.callBack(time,bitmap,mSmallDisTv.getText().toString(),mSmallTimeTv.getText().toString());
                }

                @Override
                public void onMapScreenShot(Bitmap bitmap, int i) {

                }
            });

        },time);
    }

    public static String aMapPathFormat(int pathStr) {

        if (pathStr <= 100) {
            return pathStr + "m";
        }
        if (pathStr < 1000000) {
            if (pathStr % 1000 == 0) {
                return pathStr / 1000 + "km";
            }
            return Math.round(pathStr * 1f / 100) * 1f / 10 + "km";
        }
        return pathStr / 1000 + "km";
    }
    public  String[] convertToTimeStr(int time) {
        time=time*1000;
        String hourStr="";
        String minStr="";
        int hour = (int) (time / TIME_HOUR);
        if (hour==0){
            hourStr="";
        }else if (hour<10){
            hourStr= String.valueOf(hour);
        }else {
            hourStr= String.valueOf(hour);
        }
        int minutes = (int) ((time % TIME_HOUR) / TIME_MINUTE);
        if (minutes==0){
            minStr="";
        }else if (minutes<10){
            minStr= String.valueOf(minutes);
        }else {
            minStr= String.valueOf(minutes);
        }
        return new String[]{hourStr, minStr};
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            isPause=true;
            mMapView.onPause();
            aMapNavi.pauseNavi();
            aMapNavi.destroy();
        }catch(Exception r){
            r.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            mMapView.onDestroy();
            aMapNavi.destroy();
        }catch(Exception r){
            r.printStackTrace();
        }
    }
    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }


    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }



    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }
}
