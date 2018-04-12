package com.qingguoguo.connotationjoke.doublesevice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.connotationjoke.qingguoguo.baselibrary.util.ToastUtils;
import com.qingguoguo.connotationjoke.GuardServiceAidl;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/11
 * @describe :守护进程,不需要有业务逻辑，只需要唤醒通信即可
 */

public class GuardService extends Service {
    public final static String TAG = "GuardService";
    private final static int GUARD_SERVICE_ID = 2;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    private IBinder mIBinder = new GuardServiceAidl.Stub() {
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高进程的优先级
        startForeground(GUARD_SERVICE_ID, new Notification());
        //绑定建立连接
        bindService(new Intent(this, MessageService.class),
                mConnection, Context.BIND_IMPORTANT);
        return START_STICKY;

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接上
            ToastUtils.showShort("建立连接 MessageService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接，重新启动，绑定
            startService(new Intent(GuardService.this, MessageService.class));
            bindService(new Intent(GuardService.this, MessageService.class), mConnection, Context.BIND_IMPORTANT);
        }
    };
}
