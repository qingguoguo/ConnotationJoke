package com.qingguoguo.connotationjoke;

import android.util.Log;
import android.view.View;

import com.connotationjoke.qingguoguo.baselibrary.util.ToastUtils;

import connotationjoke.qingguoguo.com.framelibrary.BaseSkinActivity;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/28 9:52
 * @Describe :
 */
public class TestActivity extends BaseSkinActivity implements View.OnClickListener {

    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        viewById(R.id.test_fix).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
//        Log.i(TAG, "修复后");
//        int i = 2 / 2;
//        ToastUtils.showShort("修复后:" + i);

        Log.i(TAG, "修复前");
        ToastUtils.showShort("修复前:" + 0);
        int i = 2 / 0;
    }
}
