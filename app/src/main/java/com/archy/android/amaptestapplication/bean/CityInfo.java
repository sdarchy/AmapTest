package com.archy.android.amaptestapplication.bean;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.io.Serializable;


/**
 * 城市列表 bean
 * @author zhoupan
 * 20170210修改
 *
 * 20190111抛弃旧版本com_qdtevc_teld_app_bean_CityInfo表，新建com_qdtevc_teld_app_bean_CityInfo2表
 * 目的是增加拼音排序支持性
 * */
//表命名统一格式: 包名(点号改成下划线)+ 类名 
public class CityInfo implements Serializable {
	private int Id;
	private String cityCode;
	private String cityName;
	private String cityLat; // 城市维度
	private String cityLng; // 城市经度
	private String firstLetter; // 城市首字母
	private String hasStation; // Y字母
	private String provinceName; //省份
	private String pingyinStr; //拼音
    public CityInfo(){
		
	}
	public CityInfo(boolean defaultFlag){
		this.cityCode = "11";
		this.cityName = "北京";
		this.firstLetter= "B"; 
		this.hasStation = "Y";
		this.cityLat="39.929986";
		this.cityLng="116.395645";
		this.provinceName="中国";
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getCityCode() {
		if(TextUtils.isEmpty(cityCode)){
		cityCode = "11";
		}
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		if(!TextUtils.isEmpty(cityName) && cityName.length() > 1 && cityName.endsWith("市")){
			cityName = cityName.substring(0, cityName.length()-1);
		}
		this.cityName = cityName;
	}
	public String getCityLat() {
		return cityLat;
	}
	public void setCityLat(String cityLat) {
		this.cityLat = cityLat;
	}
	public String getCityLng() {
		return cityLng;
	}
	
	public double getCityLatD() {
		double lat = 0d;
		try{
			lat = Double.parseDouble(cityLat);
		}catch(Exception e){
			
		}
		return lat;
	}
	public double getCityLngD() {
		double lng = 0d;
		try{
			lng = Double.parseDouble(cityLng);
		}catch(Exception e){
			
		}
		return lng;
	}
	public void setCityLng(String cityLng) {
		this.cityLng = cityLng;
	}
	public String getFirstLetter() {
		return firstLetter;
	}
	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}
	@SuppressLint("DefaultLocale")
	public String getHasStation() {
		if(TextUtils.isEmpty(hasStation)){
			return "";
		}
		return hasStation.toUpperCase();
	}
	public void setHasStation(String hasStation) {
		this.hasStation = hasStation;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}


	public String getPingyinStr() {
		return pingyinStr;
	}

	public void setPingyinStr(String pingyinStr) {
		this.pingyinStr = pingyinStr;
	}
}
