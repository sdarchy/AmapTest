package com.archy.android.amaptestapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.archy.android.amaptestapplication.utils.LocationUtils;

import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;

public class SmallMapActivity extends FragmentActivity {

    private ImageView mImageView;
    private Button mNaviBtn;
    private LatLng mEndLatLng;
    private StationSearchSmallMapFragment mStationSearchSmallMapFragment;
    private Button mReloadBtn;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    boolean isFirst=true;
    @Override
    protected void onResume() {
        super.onResume();
        LocationUtils.getAMApLocation(SmallMapActivity.this);
        if (!isFirst){
           reloadMap();
        }else {
            isFirst=false;
            refreshSmallMap();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_map);
        mImageView = ((ImageView) findViewById(R.id.map_iv));
        mNaviBtn= ((Button) findViewById(R.id.navi_btn));
        mEndLatLng = new LatLng(39.042573, 117.119196);

        mNaviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AMapLocation curAMapLocation = LocationUtils.getAMApLocation(SmallMapActivity.this);
                LocationUtils.mapNavigate(SmallMapActivity.this,
                        new LatLng(curAMapLocation.getLatitude(), curAMapLocation.getLongitude()),
                        mEndLatLng,
                        new ArrayList<>(),false);
            }
        });
        mReloadBtn = findViewById(R.id.reload_btn);
        this.mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReloadBtn.postDelayed(() -> reloadMap(),100);
                mReloadBtn.postDelayed(() -> reloadMap(),200);
                mReloadBtn.postDelayed(() -> reloadMap(),300);
                mReloadBtn.postDelayed(() -> reloadMap(),400);

            }
        });
    }

    private void reloadMap() {
        mEndLatLng =  new LatLng(39.042573, LocationUtils.randomLonLat(114.163004,120.547434));
        refreshSmallMap();
    }

    private void refreshSmallMap() {
        if (mStationSearchSmallMapFragment!=null){
            LatLng defaultNavEnd = mEndLatLng;
            double staArmLatD =  defaultNavEnd.latitude;
            double staArmLngD = defaultNavEnd.longitude;
            mStationSearchSmallMapFragment.setStationLocation(staArmLatD,staArmLngD);
        }else {

            LatLng defaultNavEnd = mEndLatLng;
            double staArmLatD =  defaultNavEnd.latitude;
            double staArmLngD = defaultNavEnd.longitude;

            mStationSearchSmallMapFragment = StationSearchSmallMapFragment.newInstance(staArmLatD,staArmLngD,StationSearchSmallMapFragment.DEFAULT);
            mStationSearchSmallMapFragment.setMapBitMapCallBack((time,bitmap, disStr, timeStr) -> {


                mImageView.setImageBitmap(bitmap);

            });

            getSupportFragmentManager().beginTransaction().add(R.id.small_fl, mStationSearchSmallMapFragment).commitAllowingStateLoss();
        }
    }

}
