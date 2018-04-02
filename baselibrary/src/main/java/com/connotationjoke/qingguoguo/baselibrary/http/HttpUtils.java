package com.connotationjoke.qingguoguo.baselibrary.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/2
 * @describe :
 */

public class HttpUtils {

    private final static int POST_TYPE = 0x0010;
    private final static int GET_TYPE = 0x0011;

    private String mUrl;
    private int mType;
    private Context mContext;
    private Map<String, Object> mParams;

    private static IHttpEngine mIHttpEngine = new DefaultHttpEngine();

    private HttpUtils(Context context) {
        mContext = context;
        mParams = new HashMap<>();
    }

    public static void initEngine(IHttpEngine httpEngine) {
        mIHttpEngine = httpEngine;
    }

    public void exchangeEngine(IHttpEngine httpEngine) {
        HttpUtils.mIHttpEngine = httpEngine;
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    public HttpUtils url(String url) {
        mUrl = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param params
     * @return
     */
    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public void execute(EngineCallBack callBack) {
        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALLBACK;
        }

        //执行网络请求方法判断
        if (mType == POST_TYPE) {
            post(mUrl, mParams, callBack);
        }

        if (mType == GET_TYPE) {
            get(mUrl, mParams, callBack);
        }
    }

    public void execute() {
        execute(null);
    }

    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mIHttpEngine.get(url, params, callBack);
    }

    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mIHttpEngine.post(url, params, callBack);
    }
}
