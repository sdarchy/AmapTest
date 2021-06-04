package com.archy.android.amaptestapplication.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @Author Archy Wang
 * @Date 2017/12/28
 * @Description
 */

public class PhoneUtil {


    public static int getScreenWidth(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return width;
    }

    public static int getScreenHeight(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int height = outMetrics.heightPixels;
        return height;
    }

    public static int px2dip(int pxValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static float dip2px(float dipValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (dipValue * scale + 0.5f);
    }
    public static int getVersionCode(Context context) {
        ComponentName cn = new ComponentName(context, context.getClass());
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(cn.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
        return info.versionCode;
    }
    public static String getVersionName(Context context) {
        ComponentName cn = new ComponentName(context, context.getClass());
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(cn.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        return info.versionName;
    }


}
