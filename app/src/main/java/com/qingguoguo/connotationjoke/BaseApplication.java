package com.qingguoguo.connotationjoke;

import android.app.Application;
import com.connotationjoke.qingguoguo.baselibrary.ExceptionCrashHandler;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/23 on 21:41
 * 描述:
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstace().init(this);
    }
}
