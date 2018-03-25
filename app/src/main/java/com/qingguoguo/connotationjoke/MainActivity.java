package com.qingguoguo.connotationjoke;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.connotationjoke.qingguoguo.baselibrary.ExceptionCrashHandler;
import com.connotationjoke.qingguoguo.baselibrary.ioc.CheckNet;
import com.connotationjoke.qingguoguo.baselibrary.ioc.OnClick;
import com.connotationjoke.qingguoguo.baselibrary.ioc.ViewById;
import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;

import java.io.File;
import java.io.FileInputStream;

import connotationjoke.qingguoguo.com.framelibrary.BaseSkinActivity;


public class MainActivity extends BaseSkinActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    @ViewById(R.id.test_tv)
    private TextView mTextView;

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        mTextView = viewById(R.id.test_tv);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 2 / 0;
            }
        });
    }

    @Override
    protected void initData() {
        //崩溃日志上传服务器
        File crashFile = ExceptionCrashHandler.getInstace().getCrashFile();
        if (crashFile != null && crashFile.exists()) {
            StringBuffer sb = new StringBuffer();
            try {
                FileInputStream fileInputStream = new FileInputStream(crashFile);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = fileInputStream.read(bytes)) != -1) {
                    sb.append(new String(bytes, 0, len)).append("\n");
                }
                LogUtils.i(TAG, sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @CheckNet
    @OnClick({R.id.test_tv, R.id.test_iv})
    private void testOnclick() {
        Toast.makeText(this, "哈哈哈,注入事件成功", Toast.LENGTH_SHORT).show();
        initData();
    }
}
