package com.connotationjoke.qingguoguo.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/18 on 14:54
 * 描述:ViewfindById辅助类
 */

public class ViewFinder {
    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        mActivity = activity;
    }

    public ViewFinder(View view) {
        mView = view;
    }

    public View findViewById(int viewId) {
        return mActivity != null ? mActivity.findViewById(viewId) : mView.findViewById(viewId);
    }
}
