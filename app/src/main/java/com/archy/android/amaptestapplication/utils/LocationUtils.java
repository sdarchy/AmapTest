package com.archy.android.amaptestapplication.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.model.NaviLatLng;
import com.archy.android.amaptestapplication.OnLocationMapCallBack;
import com.archy.android.amaptestapplication.bean.CityInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * 经纬度定位相关工具类
 * 
 * @author zhoupan
 * @create 20150720
 * */
public class LocationUtils {
	private static AMapLocation myAMapLocation = null; // 获取我的坐标
	/**
	 * 定位城市所对应的cityInfo(未必有电站),可能为空, 表示api中没有该城市
	 * 注意其 hasStation值  可能是 非"Y"表示该城市没有电站
	 * */ 
	public static CityInfo locationCityInfo = null;


	/**
	 * 获取原来的amap本身的location 注意得到坐标是高德坐标
	 * */
	public static AMapLocation getAMApLocation(Context context) {
		//新版本已经废弃使用上一次定位结果
//		if (null == LocationUtils.myAMapLocation) {
//			TeldSPUtils teldSPUtils = new TeldSPUtils(context);
//			// 上一次定位的坐标
//			String locationStr = teldSPUtils.getString(
//					"Loaction_MyBDLocation2", null);
//			if (!TextUtils.isEmpty(locationStr)) {
//				LocationUtils.myAMapLocation = JSONObject.parseObject(
//						locationStr, TeldAMapLocation.class);
//				String cityName = LocationUtils.myAMapLocation.getCity();
//				if (!TextUtils.isEmpty(cityName) && cityName.length() > 1
//						&& cityName.endsWith("市")) {
//					cityName = cityName.substring(0, cityName.length() - 1);
//				}
//				LocationUtils.myAMapLocation.setCity(cityName);
//			}
			if (null == LocationUtils.myAMapLocation) {
				// Location lo = new Location(39.989614d, 116.481763d);
				// //默认是北京的某处地址
				LocationUtils.myAMapLocation = new AMapLocation("");// 此处待改
			}
//		}

		return myAMapLocation;
	}

	/**
	 *获取2个经纬度之间的线性距离
	 * */
	public static float getLatLngPointDistance(LatLng startLatLng,LatLng endLatLng){
		if(null == startLatLng || null == endLatLng){
           return 0f;
		}
		try{
		return AMapUtils.calculateLineDistance(startLatLng, endLatLng);
		}catch (Exception e){

		}
		return 0f;
	}

	/**
	 * 将String型(经纬度)转成LatLng (注意此方法不涉及百度坐标跟高德坐标转换)
	 * */
	public static LatLng getLatLng(String latStr, String lngStr) {
		double latD = 0d;
		double lngD = 0d;
		try {
			latD = Double.parseDouble(latStr);
			lngD = Double.parseDouble(lngStr);
		} catch (Exception e) {
		}
		LatLng latLng = new LatLng(latD, lngD);
		return latLng;
	}

//	/**
//	 * 高德坐标转 百度坐标
//	 */
//	public static LatLng getAMap2BaiduLatLng(double amapLat, double amapLng) {
//		double y = amapLat, x = amapLng;
//		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
//		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
//		double bd_lon = z * Math.cos(theta) + 0.0065;
//		double bd_lat = z * Math.sin(theta) + 0.006;
//		return new LatLng(bd_lat, bd_lon);
//	}
	/**
	 * 重新定位
	 * @param onLocationCallBack
	 *            定位后回调
	 * */
	public static void startNewBDLocation(
			final Activity teldBaseActivity,
			final OnLocationMapCallBack onLocationCallBack) {
		if(null == teldBaseActivity){
            return;
		}
		final AMapLocationClient locationClient = new AMapLocationClient(
				teldBaseActivity);
		// 声明mLocationOption对象
		AMapLocationClientOption mLocationOption = null;

		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(true);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		// mLocationOption.setInterval(2000);

		locationClient.setLocationListener(new AMapLocationListener() {

			@Override
			public void onLocationChanged(AMapLocation amapLocation) {
				String s = amapLocation.toString();
				Log.d("App启动", "LocationUtils:"+s);
				if (amapLocation != null) {
					if (amapLocation.getErrorCode() == 0) {
						LocationUtils.myAMapLocation = amapLocation;
						String cityName = LocationUtils.myAMapLocation
								.getCity();
						if (!TextUtils.isEmpty(cityName)
								&& cityName.length() > 1
								&& cityName.endsWith("市")) {
							cityName = cityName.substring(0,
									cityName.length() - 1);
						}
						LocationUtils.myAMapLocation.setCity(cityName);


						/**********************修改定位结果模块********************************/
//						//模拟定位济南（模拟1）
//						LocationUtils.myAMapLocation.setCity("济南");
//						LocationUtils.myAMapLocation.setLatitude(36.69d);
//						LocationUtils.myAMapLocation.setLongitude(117.07d);

//                      //模拟定位北京（模拟2）
//                      LocationUtils.myAMapLocation.setCity("北京");
//						LocationUtils.myAMapLocation.setLatitude(39.92d);
//						LocationUtils.myAMapLocation.setLongitude(116.46d);

//						//（模拟3）模拟子页面定位发生修改（以及首页定位失败，子页面定位成功），返回首页提示切换城市
//						//操作方法： 打开app快速跳转到某个子页面  等待20s,返回到首页
//						new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								LocationUtils.myAMapLocation.setCity("上海");
//								LocationUtils.myAMapLocation.setLatitude(31.48d);
//								LocationUtils.myAMapLocation.setLongitude(121.48d);
//								ConstantUtils.CHANGE_CITY_DIALOG_FLAG = true;
//								updateLocationCity(teldBaseActivity);
//							}
//						},20000L);
                        /******************************修改定位结果模块结束********************************************/


						if (null != onLocationCallBack) {// 成功定位后的回调
							onLocationCallBack.onLocationMapSucceed();
						}
					}else{
						if (null != onLocationCallBack) {// 成功定位后的回调
							onLocationCallBack.onLocationMapFailure();
						}
					}
				} else {
					if (null != onLocationCallBack) {// 成功定位后的回调
						onLocationCallBack.onLocationMapFailure();
					}
				}
				locationClient.stopLocation();//
				locationClient.onDestroy();//
			}
		});
		// 给定位客户端对象设置定位参数
		locationClient.setLocationOption(mLocationOption);
		// 启动定位
		locationClient.startLocation();
	}

	/**
	 * 带超时功能
	 * 超过指定时间 默认返回定位失败
	 * 重新定位
	 * @param onLocationCallBack
	 *            定位后回调
	 * */
	public static void startNewBDLocation(
			final Activity teldBaseActivity,
			final OnLocationMapCallBack onLocationCallBack,long outTimeDuring) {
		final boolean[] startCurLocFlag = {true};
		final AMapLocationClient locationClient = new AMapLocationClient(teldBaseActivity);
		if(null != onLocationCallBack){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if(startCurLocFlag[0]){//按照定位失败处理
						startCurLocFlag[0] = false;
						onLocationCallBack.onLocationMapFailure();
					}
				}
			},outTimeDuring);
		}

		// 声明mLocationOption对象
		AMapLocationClientOption mLocationOption = null;

		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(true);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		// mLocationOption.setInterval(2000);

		locationClient.setLocationListener(new AMapLocationListener() {

			@Override
			public void onLocationChanged(AMapLocation amapLocation) {
				if (amapLocation != null) {
					if (amapLocation.getErrorCode() == 0) {
						LocationUtils.myAMapLocation = amapLocation;
						String cityName = LocationUtils.myAMapLocation
								.getCity();
						if (!TextUtils.isEmpty(cityName)
								&& cityName.length() > 1
								&& cityName.endsWith("市")) {
							cityName = cityName.substring(0,
									cityName.length() - 1);
						}
						LocationUtils.myAMapLocation.setCity(cityName);
						if (startCurLocFlag[0] && null != onLocationCallBack) {// 成功定位后的回调
							startCurLocFlag[0] = false;
							onLocationCallBack.onLocationMapSucceed();
						}
					}else{
						if (startCurLocFlag[0] && null != onLocationCallBack) {// 成功定位后的回调
							startCurLocFlag[0] = false;
							onLocationCallBack.onLocationMapFailure();
						}
					}
				} else {
					if (startCurLocFlag[0] && null != onLocationCallBack) {// 成功定位后的回调
						startCurLocFlag[0] = false;
						onLocationCallBack.onLocationMapFailure();
					}
				}
				locationClient.stopLocation();//
				locationClient.onDestroy();//
			}
		});
		// 给定位客户端对象设置定位参数
		locationClient.setLocationOption(mLocationOption);
		// 启动定位
		locationClient.startLocation();
	}

    /**
     * 重新定位
     *后台切换到前台专用，请勿使用
     *            定位后回调
     * */
    public static void startNewBDLocation2(
            final Activity teldBaseActivity) {
        // 声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        final AMapLocationClient locationClient = new AMapLocationClient(
                teldBaseActivity);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为2000ms
        // mLocationOption.setInterval(2000);
        locationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        LocationUtils.myAMapLocation = amapLocation;
                        String cityName = LocationUtils.myAMapLocation
                                .getCity();
                        if (!TextUtils.isEmpty(cityName)
                                && cityName.length() > 1
                                && cityName.endsWith("市")) {
                            cityName = cityName.substring(0,
                                    cityName.length() - 1);
                        }
                        LocationUtils.myAMapLocation.setCity(cityName);
                    }
                }
                locationClient.stopLocation();//
                locationClient.onDestroy();//
            }
        });
        // 给定位客户端对象设置定位参数
        locationClient.setLocationOption(mLocationOption);
        // 启动定位
        locationClient.startLocation();
    }



	/**
	 * 判断GPS是否开启
	 * @param application
	 * @return false 未开启GPS  true 开启GPS
	 */
	public static  boolean checkGPSEnable(@NonNull Application application) throws NullPointerException {
		LocationManager mLocationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
		try{
			return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}catch (Throwable t){}
		return true;
	}




	/**-------------------------------------------GPS处理工具类--------------------------------------------*/
	/**
	 * (注意 参数传入 应该 是转换后的坐标(百度坐标到高德)) 导航入口
	 * @param mWayPointList 途经点
	 * @param footWalkFlag 步行模式
	 * */
	public static void mapNavigate(final Activity activity,
                                   final LatLng startLatLng, final LatLng endLatLng, ArrayList<NaviLatLng> mWayPointList, boolean footWalkFlag) {


		if (footWalkFlag){
			Bundle bundle = new Bundle();
			bundle.putDouble("startLat", startLatLng.latitude);
			bundle.putDouble("startLng", startLatLng.longitude);
			bundle.putDouble("endLat", endLatLng.latitude);
			bundle.putDouble("endLng", endLatLng.longitude);
			bundle.putBoolean("footWalkFlag",footWalkFlag);
			bundle.putSerializable("wayPointList",mWayPointList);
//			activity.startNextActivity(bundle, BNavigator2Activity.class);
		}else {
			Poi start = new Poi("我的位置", new LatLng(startLatLng.latitude,startLatLng.longitude), "");
			Poi end = new Poi("终点", new LatLng(endLatLng.latitude, endLatLng.longitude), "");
			List<Poi> wayList = new ArrayList();//途径点目前最多支持3个。
			if(mWayPointList!=null&&mWayPointList.size()>0){
				if (mWayPointList.size()==1){
					wayList.add(new Poi("途径点",new LatLng(mWayPointList.get(0).getLatitude(),mWayPointList.get(0).getLongitude()),""));
				}else {
					for (int i = 0; i < mWayPointList.size(); i++) {
						wayList.add(new Poi("途径点"+(i+1),new LatLng(mWayPointList.get(i).getLatitude(),mWayPointList.get(i).getLongitude()),""));
					}
				}

			}
			AmapNaviPage.getInstance().showRouteActivity(activity, new AmapNaviParams(start, wayList, end, AmapNaviType.DRIVER, AmapPageType.NAVI), null);
		}
	}
	/**
	 * (注意 参数传入 应该 是转换后的坐标(百度坐标到高德)) 导航入口
	 * @param footWalkFlag 步行模式
	 * */
	public static void mapNavigate(final Activity activity,
			final LatLng startLatLng, final LatLng endLatLng,boolean footWalkFlag) {

		if (footWalkFlag){
			Bundle bundle = new Bundle();
			bundle.putDouble("startLat", startLatLng.latitude);
			bundle.putDouble("startLng", startLatLng.longitude);
			bundle.putDouble("endLat", endLatLng.latitude);
			bundle.putDouble("endLng", endLatLng.longitude);
			bundle.putBoolean("footWalkFlag",footWalkFlag);
//			activity.startNextActivity(bundle, BNavigator2Activity.class);
		}else {
			Poi start = new Poi("我的位置", new LatLng(startLatLng.latitude,startLatLng.longitude), "");
			Poi end = new Poi("终点", new LatLng(endLatLng.latitude, endLatLng.longitude), "");
			AmapNaviPage.getInstance().showRouteActivity(activity, new AmapNaviParams(start, null, end,footWalkFlag?AmapNaviType.WALK: AmapNaviType.DRIVER, AmapPageType.NAVI), null);



		}




	}

	// 我的位置,城市搜索处理
	// 山东省青岛市崂山区S293(滨海公路),崂山区,青岛市,山东省
	public static String myLocSearchAddr() {
		if (null == LocationUtils.myAMapLocation) {
			return "";
		}
		String addr = LocationUtils.myAMapLocation.getAddress();
		addr += ",";
		addr += LocationUtils.myAMapLocation.getDistrict();
		addr += ",";
		addr += LocationUtils.myAMapLocation.getCity();
		addr += ",";
		addr += LocationUtils.myAMapLocation.getProvince();
		return addr;
	}

	/**
	 * 各地图API坐标系统比较与转换;
	 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
	 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
	 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
	 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
	 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。 chenhua
	 */
	public static double pi = 3.1415926535897932384626;

	/**
	 * 比较两个定位点之间的距离是否大于200米
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static boolean compareToPoint(LatLng point1,LatLng point2){
		//如果新旧都没有获取到经纬度信息，看作为没有改变
		if(LocationUtils.myAMapLocation != null){

			float distance = AMapUtils.calculateLineDistance(point1,point2);
			//如果距离变化小于200米，则认为没有改变
			if(distance < 200){
				return false;
			}else{
				return true;
			}

		}
		return false;
	}

	/**
	 * 对比新定位的地理坐标 是否 相对于原来位置发声了改变
	 * @param
	 * @return
	 */
	public static boolean isLocationChanged(double lng, double lat){

		//todo 应该用地图的请求经纬度和列表的请求经纬度比较


		//如果新旧都没有获取到经纬度信息，看作为没有改变
		if(LocationUtils.myAMapLocation != null){

			float distance = AMapUtils.calculateLineDistance
					(new LatLng(LocationUtils.myAMapLocation.getLatitude(), LocationUtils.myAMapLocation.getLongitude()), new LatLng(lat,lng));



//			float distance = AMapUtils.calculateLineDistance
//					(new LatLng(testRandom(),testRandom()), new LatLng(lat,lng));




			//如果距离变化小于1000米，则认为没有改变
			if(distance < 200){
				return false;
			}else{
				return true;
			}

		}
		return false;
	}


	public static double randomLonLat(double MinLon, double MaxLon) {
		BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
		double lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();// 小数后6位
		return lon;
	}

}
