package com.qingguoguo.connotationjoke.doublesevice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.qingguoguo.connotationjoke.GuardServiceAidl;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/11
 * @describe :
 */

public class GuardService extends Service {
    public final static String TAG = "GuardService";
    private final static int GUARD_SERVICE_ID = 2;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高进程的优先级
        startForeground(GUARD_SERVICE_ID, new Notification());
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    private IBinder mIBinder = new GuardServiceAidl.Stub() {
    };
}
