package com.connotationjoke.qingguoguo.baselibrary.view.customdialog.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作者:qingguoguo
 * 创建日期：2018/4/1 on 19:47
 * 描述: 头部基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationBarParams> implements INavigationBar {

    private P mBarParams;
    private View mNavigationView;

    public AbsNavigationBar(P barParams) {
        mBarParams = barParams;
        createAndBindView();
    }

    public P getBarParams() {
        return mBarParams;
    }

    public void setText(int viewId, String rightText) {
        TextView tv = mNavigationView.findViewById(viewId);
        if (tv != null && !TextUtils.isEmpty(rightText)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(rightText);
        }
    }

    public void setOnClickListenter(int viewId, View.OnClickListener clickListener) {
        View view = mNavigationView.findViewById(viewId);
        if (view != null && clickListener != null) {
            view.setOnClickListener(clickListener);
        }
    }

    public <T extends View> T findViewById(int viewId) {
        return (T) mNavigationView.findViewById(viewId);
    }

    /**
     * 创建和绑定View
     */
    private void createAndBindView() {
        //1.创建View
        if (mBarParams.mParent == null) {
            //获取Activity的根布局 AppCompatActivity
            ViewGroup viewGroup = ((Activity) (mBarParams.mContext))
                    .findViewById(android.R.id.content);
            mBarParams.mParent = (ViewGroup) viewGroup.getChildAt(0);
        }

//        if (mBarParams.mParent == null) {
//            //获取Activity的根布局 AppActivity
//            mBarParams.mParent = (ViewGroup) ((Activity) (mBarParams.mContext))
//                    .getWindow().getDecorView();
//        }

        mNavigationView = LayoutInflater.from(mBarParams.mContext)
                .inflate(bindLayoutResId(), mBarParams.mParent, false);
        //2.添加
        mBarParams.mParent.addView(mNavigationView, 0);
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
