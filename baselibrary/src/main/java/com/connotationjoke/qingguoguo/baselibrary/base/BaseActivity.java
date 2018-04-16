package com.connotationjoke.qingguoguo.baselibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.connotationjoke.qingguoguo.baselibrary.ioc.ViewUtils;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/23 on 16:44
 * 描述:
 */

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置布局
        setContentView(getLayoutID());
        ViewUtils.inject(this);
        //初始化界面
        initView();
        //初始化头部
        initTitle();
        //初始化数据
        initData();
    }

    @LayoutRes
    protected abstract int getLayoutID();

    protected abstract void initView();

    protected abstract void initTitle();

    protected abstract void initData();

    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected <T extends View> T viewById(@IdRes int id) {
        return findViewById(id);
    }
}
