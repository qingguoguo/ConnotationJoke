package com.qingguoguo.connotationjoke.doublesevice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;
import com.qingguoguo.connotationjoke.MessageAidl;


/**
 * @author :qingguoguo
 * @datetime ：2018/4/11
 * @describe :
 */

public class MessageService extends Service {
    public final static String TAG = "MessageService";
    private final static int MESSAGE_SERVICE_ID = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    LogUtils.i(TAG, "等待接收消息");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高进程的优先级
        startForeground(MESSAGE_SERVICE_ID, new Notification());
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    private IBinder mIBinder = new MessageAidl.Stub() {

        @Override
        public String getUserName() throws RemoteException {
            return "qingguoguo";
        }

        @Override
        public String getUserPwd() throws RemoteException {
            return "123456";
        }
    };
}
