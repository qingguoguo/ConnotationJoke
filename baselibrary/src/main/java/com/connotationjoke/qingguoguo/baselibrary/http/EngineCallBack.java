package com.connotationjoke.qingguoguo.baselibrary.http;


/**
 * @author :qingguoguo
 * @datetime ：2018/4/2
 * @describe :
 */

public interface EngineCallBack {

    /**
     * 网络请求成功的回调方法
     *
     * @param result
     * @return
     */
    void onSuccess(String result);

    /**
     * 网络请求失败的回调方法
     *
     * @param e
     */
    void onError(Exception e);

    EngineCallBack DEFAULT_CALLBACK = new EngineCallBack() {
        @Override
        public void onSuccess(String result) {
        }

        @Override
        public void onError(Exception e) {

        }
    };
}
