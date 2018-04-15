package com.qingguoguo.connotationjoke.hookstartactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qingguoguo.connotationjoke.R;

/**
 * 作者:qingguoguo
 * 创建日期：2018/4/15 on 16:27
 * 描述:在注册清单中未配置的测试Activity
 */

public class TestHookActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
