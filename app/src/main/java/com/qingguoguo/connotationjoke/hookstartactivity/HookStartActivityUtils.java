package com.qingguoguo.connotationjoke.hookstartactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * 作者:qingguoguo
 * 创建日期：2018/4/15 on 15:44
 * 描述:
 */

public class HookStartActivityUtils {
    private static final String TAG = "HookStartActivityUtils";
    private static final String EXTRA_OLD_INTENT = "extra_old_intent";
    public static final int LAUNCH_ACTIVITY = 100;

    private Context mContext;
    private Class<?> mProxyClass;

    public HookStartActivityUtils(Context context, Class<?> aClass) {
        mContext = context.getApplicationContext();
        mProxyClass = aClass;
    }

    public void hookStartActivity() throws Exception {
        //创建参数
        //5.0 创建IActivityManager的实例 getDefault--->Singleton--->mInstance
        Object singletonObject;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Field iActivityManagerSingletonField = Class.forName("android.app.ActivityManagerNative").getDeclaredField("gDefault");
            iActivityManagerSingletonField.setAccessible(true);
            singletonObject = iActivityManagerSingletonField.get(null);
        } else {
            Field iActivityManagerSingletonField = Class.forName("android.app.ActivityManager").getDeclaredField("IActivityManagerSingleton");
            iActivityManagerSingletonField.setAccessible(true);
            singletonObject = iActivityManagerSingletonField.get(null);
        }

        //获取 mInstance
        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field instanceField = singletonClazz.getDeclaredField("mInstance");
        instanceField.setAccessible(true);
        Object iamInstance = instanceField.get(singletonObject);

        InvocationHandler iamInvocationHandler = new IAMInvocationHandler(iamInstance);
        Class<?> IActivityManagerIntercept = Class.forName("android.app.IActivityManager");
        //需要的参数 ClassLoader loader,Class<?>[] interfaces,InvocationHandler h
        Object proxyInstance = Proxy.newProxyInstance(HookStartActivityUtils.class.getClassLoader(),
                new Class<?>[]{IActivityManagerIntercept}, iamInvocationHandler);
        //替换掉 mInstance
        instanceField.set(singletonObject, proxyInstance);
    }

    public void hookLaunchActivity() throws Exception {
        // private static volatile ActivityThread sCurrentActivityThread;
        // final H mH = new H();
        Class<?> atClass = Class.forName("android.app.ActivityThread");
        Field atField = atClass.getDeclaredField("sCurrentActivityThread");
        atField.setAccessible(true);
        Object currentActivityThread = atField.get(null);
        Field mHField = atClass.getDeclaredField("mH");
        mHField.setAccessible(true);

        Object mH = mHField.get(currentActivityThread);
        // final Callback mCallback;
        Class<Handler> handlerClass = Handler.class;
        Field mCallbackField = handlerClass.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        mCallbackField.set(mH, new HandlerCallback());
    }

    private class HandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            // public static final int LAUNCH_ACTIVITY = 100;
            if (msg.what == LAUNCH_ACTIVITY) {
                handleLaunchActivity(msg);
            }
            return false;
        }

        /**
         * 拦截 替换 ActivityClientRecord的 intent
         *
         * @param msg
         */
        private void handleLaunchActivity(Message msg) {
            // final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
            //从Message 拿到 ActivityClientRecord
            Object activityRecord = msg.obj;
            try {
                /**ActivityClientRecord r = new ActivityClientRecord();
                 // r.token = token;
                 // r.ident = ident;
                 // r.intent = intent;
                 //static final class ActivityClientRecord {
                 //    IBinder token;
                 //    int ident;
                 //    Intent intent;
                 //从ActivityClientRecord  safeIntent*/
                Field intentField = activityRecord.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                Intent safeIntent = (Intent) intentField.get(activityRecord);
                //从safeIntent拿到oldIntent
                Intent oldIntent = safeIntent.getParcelableExtra(EXTRA_OLD_INTENT);
                if (oldIntent != null) {
                    intentField.set(activityRecord, oldIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ActivityManagerNative的InvocationHandler
     */
    private class IAMInvocationHandler implements InvocationHandler {
        private Object mObject;

        public IAMInvocationHandler(Object object) {
            mObject = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            LogUtils.i(TAG, "method:" + method.getName());
            if ("startActivity".equals(method.getName())) {
                //拿到原来的Intent
                Intent oldIntent = (Intent) args[2];
                //创建一个安全的Intent
                Intent safeIntent = new Intent(mContext, mProxyClass);
                args[2] = safeIntent;
                //把原来的Intent添加到safeIntent的Extra，带过去
                safeIntent.putExtra(EXTRA_OLD_INTENT, oldIntent);
            }
            return method.invoke(mObject, args);
        }
    }
}
