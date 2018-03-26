package com.connotationjoke.qingguoguo.baselibrary.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/26
 * @describe :
 */

public class ToastUtils {

    public ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void showLong(String msg) {
        Toast.makeText(Utils.getApp(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showLong(@StringRes int id) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getResources().getText(id), Toast.LENGTH_LONG).show();
    }

    public static void showShort(String msg) {
        Toast.makeText(Utils.getApp(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(@StringRes int id) {
        Toast.makeText(Utils.getApp(), Utils.getApp().getResources().getText(id), Toast.LENGTH_SHORT).show();
    }
}
