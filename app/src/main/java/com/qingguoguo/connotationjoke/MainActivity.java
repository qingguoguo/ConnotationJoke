package com.qingguoguo.connotationjoke;

import android.widget.TextView;
import android.widget.Toast;

import com.connotationjoke.qingguoguo.baselibrary.ioc.CheckNet;
import com.connotationjoke.qingguoguo.baselibrary.ioc.OnClick;
import com.connotationjoke.qingguoguo.baselibrary.ioc.ViewById;

import connotationjoke.qingguoguo.com.framelibrary.BaseSkinActivity;


public class MainActivity extends BaseSkinActivity {

    @ViewById(R.id.test_tv)
    private TextView mTextView;

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        mTextView = viewById(R.id.test_tv);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @CheckNet
    @OnClick({R.id.test_tv, R.id.test_iv})
    private void testOnclick() {
        Toast.makeText(this, "哈哈哈,注入事件成功", Toast.LENGTH_SHORT).show();
    }
}
