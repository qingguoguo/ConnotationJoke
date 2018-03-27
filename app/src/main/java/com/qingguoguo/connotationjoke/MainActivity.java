package com.qingguoguo.connotationjoke;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.connotationjoke.qingguoguo.baselibrary.ExceptionCrashHandler;
import com.connotationjoke.qingguoguo.baselibrary.fixbug.FixDexManager;
import com.connotationjoke.qingguoguo.baselibrary.ioc.CheckNet;
import com.connotationjoke.qingguoguo.baselibrary.ioc.OnClick;
import com.connotationjoke.qingguoguo.baselibrary.ioc.ViewById;
import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import connotationjoke.qingguoguo.com.framelibrary.BaseSkinActivity;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/27 16:20
 * @Describe :
 */
public class MainActivity extends BaseSkinActivity implements View.OnClickListener {
    public final static String TAG = MainActivity.class.getSimpleName();
    @ViewById(R.id.test_tv)
    private TextView mTextView;

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initView() {
        mTextView = viewById(R.id.test_tv);
        mTextView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //崩溃日志上传服务器
        uploadCrashFile();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @CheckNet
    @OnClick({R.id.test_iv})
    private void testOnclick() {
        ToastUtils.showShort("测试,阿里热修复,加载补丁");
        //测试,阿里热修复
        //testAliAndFix();
        fixDexBug();
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

    private void testAliAndFix() {
        //每次启动的时候,从后台获取差分包,fix.patch 修复Bug
        //测试直接从本地获取fix.patch
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        Log.i(TAG, "fix.apatch路径：" + fixFile.getAbsolutePath());
        if (fixFile.exists()) {
            //修复Bug
            try {
                BaseApplication.getPatchManager().addPatch(fixFile.getAbsolutePath());
                ToastUtils.showShort("修复成功");

            } catch (IOException e) {
                ToastUtils.showShort("修复失败");
                e.printStackTrace();
            }
        }
    }

    private void uploadCrashFile() {
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
                LogUtils.i(TAG, "上次的异常信息：" + sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自己写的热修复方式
     */
    private void fixDexBug() {
        File file = new File(Environment.getExternalStorageDirectory(), "fix.dex");
        if (file.exists()) {
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(file.getAbsolutePath());
                ToastUtils.showShort("修复成功");
            } catch (Exception e) {
                ToastUtils.showShort("修复失败");
                e.printStackTrace();
            }
        }
    }
}
