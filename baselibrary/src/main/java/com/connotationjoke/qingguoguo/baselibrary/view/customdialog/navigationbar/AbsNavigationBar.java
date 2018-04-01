package com.connotationjoke.qingguoguo.baselibrary.view.customdialog.navigationbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者:qingguoguo
 * 创建日期：2018/4/1 on 19:47
 * 描述: 头部基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationBarParams> implements INavigationBar {

    private P mBarParams;

    public AbsNavigationBar(P barParams) {
        mBarParams = barParams;
        createAndBindView();
    }

    /**
     * 创建和绑定View
     */
    private void createAndBindView() {
        //1.创建View
        View navigationView = LayoutInflater.from(mBarParams.mContext)
                .inflate(bindLayoutResId(), mBarParams.mParent, false);
        //2.添加
        mBarParams.mParent.addView(navigationView, 0);
        applyView();
    }

    public abstract static class Builder {

        public Builder(Context context, ViewGroup parent) {

        }

        public abstract AbsNavigationBar create();

        public static class AbsNavigationBarParams {
            public Context mContext;
            public ViewGroup mParent;

            protected AbsNavigationBarParams(Context context, ViewGroup parent) {
                mContext = context;
                mParent = parent;
            }
        }
    }
}
