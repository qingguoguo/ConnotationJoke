package connotationjoke.qingguoguo.com.framelibrary;

import android.content.Context;

import com.connotationjoke.qingguoguo.baselibrary.http.EngineCallBack;
import com.connotationjoke.qingguoguo.baselibrary.http.HttpUtils;
import com.google.gson.Gson;

import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/3
 * @describe : 再次封装 EngineCallBack，包含业务逻辑参数
 */

public abstract class HttpCallBack<T> implements EngineCallBack {

    @Override
    public void onSuccess(String result) {
        T obj = (T) new Gson().fromJson(result, HttpUtils.analysisClazzInfo(this));
        onSuccess(obj);
    }

    /**
     * 封装与业务逻辑相关的固定参数
     *
     * @param context
     * @param params
     */
    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        params.put("app_name", "joke_essay");
        params.put("version_name", "5.7.0");
        params.put("ac", "wifi");
        params.put("device_id", "30036118478");
        params.put("device_brand", "Xiaomi");
        params.put("update_version_code", "5701");
        params.put("manifest_version_code", "570");
        params.put("longitude", "113.000366");
        params.put("latitude", "28.171377");
        params.put("device_platform", "android");

        onPreExecute();
    }

    @Override
    public void onCancel() {

    }

    /**
     * 开始执行,可显示进度条
     */
    protected void onPreExecute() {
    }

    public abstract void onSuccess(T t);
}
