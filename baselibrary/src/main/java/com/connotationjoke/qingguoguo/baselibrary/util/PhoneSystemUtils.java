package com.connotationjoke.qingguoguo.baselibrary.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Locale;

/**
 * @author :qingguoguo
 * @datetime ：2017/12/21
 * @describe :
 */

public class PhoneSystemUtils {

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /***
     * 获取APP版本号
     *
     * @return
     */
    public static String getAppVersion() {
        try {
            PackageManager manager = Utils.getApp().getPackageManager();
            PackageInfo info = manager.getPackageInfo(Utils.getApp().getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
//    public static String getIMEI(Context ctx) {
//        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        } else {
//            if (tm != null) {
//                return tm.getDeviceId();
//            }
//        }
//        return null;
//    }
    public static void log() {
        //Xiaomi,7.0,zh,MIX
        Log.i("DeviceTAG", PhoneSystemUtils.getDeviceBrand() + "," +
                PhoneSystemUtils.getSystemVersion() + "," +
                PhoneSystemUtils.getSystemModel());
    }
}
