package com.connotationjoke.qingguoguo.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/23 on 19:10
 * 描述:单例模式捕获全局异常
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler instance;
    private Thread.UncaughtExceptionHandler mExceptionHandler;
    private Context mContext;
    public final static String CRASH = "crash";
    public final static String CRASH_NAME = "crash_name";

    private ExceptionCrashHandler() {
    }

    public static ExceptionCrashHandler getInstace() {
        if (instance == null) {
            synchronized (ExceptionCrashHandler.class) {
                if (instance == null) {
                    instance = new ExceptionCrashHandler();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //全局异常
        Log.e("MyTag", "报异常了");
        //写入到本地文件  异常信息  当前的版本  手机信息
        //1.崩溃的详细信息
        //2.应用信息,包名,版本号
        //3.手机信息
        //4.保存当期文件,等应用再次启动再上传
        String crashFileName = saveInfoToSD(e);
        cacheCrashFile(crashFileName);
        Log.e("MyTag", "crashFileName:" + crashFileName);
        //让系统默认处理
        mExceptionHandler.uncaughtException(t, e);
    }

    private String saveInfoToSD(Throwable e) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();
        // 1. 手机信息 + 应用信息
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        // 2.崩溃的详细信息
        obtainExceptionInfo(e);
        // 3.保存文件
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            Log.e("MyTag", "mContext.getFilesDir():" + mContext.getFilesDir());
            File dir = new File(mContext.getFilesDir() + File.separator + CRASH + File.separator);

            //先删除之前的异常信息
            if (dir.exists()) {
                //删除该目录下的所有子文件
                deleteDir(dir);
            }

            if (!dir.exists()) {
                dir.mkdirs();
            }

            fileName = dir.toString() + File.separator
                    + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        return fileName;
    }

    private String getAssignTime(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateStr);
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
        return true;
    }

    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", packageInfo.versionName);
        map.put("versionNCode", String.valueOf(packageInfo.versionCode));
        map.put("MODEL", Build.MODEL);
        map.put("SDK_INT", String.valueOf(Build.VERSION.SDK_INT));
        map.put("PRODUCT", Build.PRODUCT);
        map.put("MOBLE_INFO", getMobileInfo());
        return map;
    }

    /**
     * 获取手机信息
     */
    private String getMobileInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        Field[] declaredFields = Build.class.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = field.getName();
            String value = "";
            try {
                value = field.get(null).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            stringBuffer.append(name + "=" + value).append("\n");
        }
        return stringBuffer.toString();
    }

    /**
     * 缓存崩溃文件
     */
    private void cacheCrashFile(String crashFileName) {
        SharedPreferences sp = mContext.getSharedPreferences(CRASH
                , Context.MODE_PRIVATE);
        sp.edit().putString(CRASH_NAME, crashFileName).commit();
    }

    /**
     * 获取系统未捕捉的错误信息
     */
    private String obtainExceptionInfo(Throwable throwable) {
        // Java基础 异常
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }
}
