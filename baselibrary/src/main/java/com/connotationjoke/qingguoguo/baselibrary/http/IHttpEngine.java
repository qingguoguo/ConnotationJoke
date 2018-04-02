package com.connotationjoke.qingguoguo.baselibrary.http;

import java.util.Map;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/2
 * @describe : 定义网络请求的规范
 */

public interface IHttpEngine {

    /**
     * get请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    void get(String url, Map<String, Object> params, EngineCallBack callBack);

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    void post(String url, Map<String, Object> params, EngineCallBack callBack);


    //下载文件
    //上传文件
    //https添加证书
}
