package com.connotationjoke.qingguoguo.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/18 on 14:51
 * 描述:
 */

public class ViewUtils {

    /**
     * 注入到Activity
     */
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    /**
     * 目前使用
     */
    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    /**
     * 为后期考虑
     */
    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    /**
     * 兼容方法
     */
    public static void inject(ViewFinder finder, Object object) {
        injectField(finder, object);
        injectEvent(finder, object);
    }

    /**
     * 注入属性
     */
    public static void injectField(ViewFinder finder, Object object) {
        Class<?> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        if (declaredFields != null) {
            for (Field field : declaredFields) {
                ViewById annotation = field.getAnnotation(ViewById.class);
                if (annotation != null) {
                    int viewId = annotation.value();
                    View view = finder.findViewById(viewId);
                    if (view != null) {
                        //能够注入到私有属性里
                        field.setAccessible(true);
                        try {
                            //动态注入到View
                            field.set(object, view);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 注入事件
     */
    private static void injectEvent(ViewFinder finder, Object object) {
        Class<?> aClass = object.getClass();
        Method[] Methods = aClass.getDeclaredMethods();
        if (Methods != null) {
            for (Method method : Methods) {
                OnClick annotation = method.getAnnotation(OnClick.class);
                //是否有检测网络的注解
                boolean isCheckNet = checkNet(method);
                if (annotation != null) {
                    int[] viewIds = annotation.value();
                    for (int viewId : viewIds) {
                        View view = finder.findViewById(viewId);
                        if (view != null) {
                            view.setOnClickListener(new InjectClickListener(method, object, isCheckNet));
                        }
                    }
                }
            }
        }
    }

    private static boolean checkNet(Method method) {
        CheckNet annotation = method.getAnnotation(CheckNet.class);
        if (annotation != null) {
            return true;
        }
        return false;
    }

    private static class InjectClickListener implements View.OnClickListener {
        private Method mMethod;
        private Object mObject;
        private boolean mIsCheckNet;

        public InjectClickListener(Method method, Object object, boolean isCheckNet) {
            mMethod = method;
            mObject = object;
            mIsCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View v) {
            if (mIsCheckNet) {
                if (!networkAvailable(v.getContext())) {
                    Toast.makeText(v.getContext(), "亲,您的网络不太给力", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            mMethod.setAccessible(true);
            try {
                mMethod.invoke(mObject, v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }

        /**
         * 检测网络
         */
        private boolean networkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            }
            return false;
        }
    }
}
