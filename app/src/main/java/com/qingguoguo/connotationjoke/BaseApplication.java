package com.qingguoguo.connotationjoke;

import android.app.Application;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;
import com.connotationjoke.qingguoguo.baselibrary.ExceptionCrashHandler;
import com.connotationjoke.qingguoguo.baselibrary.fixbug.FixDexManager;
import com.connotationjoke.qingguoguo.baselibrary.http.HttpUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.PhoneSystemUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.Utils;
import com.squareup.leakcanary.LeakCanary;

import connotationjoke.qingguoguo.com.framelibrary.http.OkHttpEngine;
import connotationjoke.qingguoguo.com.framelibrary.skin.SkinManager;

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
        HttpUtils.initEngine(new OkHttpEngine());
        //初始化异常处理器
        ExceptionCrashHandler.getInstace().init(this);
        initLeakCanary();
        initLogUtils();

        Utils.init(this);
        //初始化阿里热修复
        initAliAndFix();
        //初始化自己的热修复
        initFixDex();
        SkinManager.getInstance().init(this);
    }

    private void initFixDex() {
        FixDexManager fixDexManager = new FixDexManager(this);
        try {
            fixDexManager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAliAndFix() {
        mPatchManager = new PatchManager(this);
        mPatchManager.init(PhoneSystemUtils.getAppVersion());
        //加载之前的patch包
        mPatchManager.loadPatch();
    }

    private void initLogUtils() {
        //用ApplicationInfo的属性去判断是否是 Debug 版本
        LogUtils.syncIsDebug(getApplicationContext());
        Log.i(TAG, "BuildConfig.is_DEBUG：" + BuildConfig.is_DEBUG + " , LogUtils.isDebug()" + LogUtils.isDebug());
    }

    private void initLeakCanary() {
        // 内存泄露检查工具
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
