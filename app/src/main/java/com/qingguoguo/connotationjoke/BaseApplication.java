package com.qingguoguo.connotationjoke;

import android.app.Application;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;
import com.connotationjoke.qingguoguo.baselibrary.ExceptionCrashHandler;
import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/23 on 21:41
 * 描述:
 */

public class BaseApplication extends Application {

    public final static String TAG = BaseApplication.class.getSimpleName();
    public static BaseApplication instance;
    private static PatchManager mPatchManager;

    public static BaseApplication getInstance() {
        return instance;
    }

    public static PatchManager getPatchManager() {
        return mPatchManager;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        initLogUtils();
        initAliAndFix();//初始化阿里热修复
        ExceptionCrashHandler.getInstace().init(this); //初始化异常处理器
    }

    private void initAliAndFix() {
        mPatchManager = new PatchManager(this);
        mPatchManager.init("1.0");
        mPatchManager.loadPatch();//加载之前的patch包
    }

    private void initLogUtils() {
        //用ApplicationInfo的属性去判断是否是 Debug 版本
        LogUtils.syncIsDebug(getApplicationContext());
        Log.i(TAG, "BuildConfig.is_DEBUG：" + BuildConfig.is_DEBUG + " , LogUtils.isDebug()" + LogUtils.isDebug());
    }
}
