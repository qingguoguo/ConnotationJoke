package com.qingguoguo.connotationjoke;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.connotationjoke.qingguoguo.baselibrary.ExceptionCrashHandler;
import com.connotationjoke.qingguoguo.baselibrary.fixbug.FixDexManager;
import com.connotationjoke.qingguoguo.baselibrary.ioc.CheckNet;
import com.connotationjoke.qingguoguo.baselibrary.ioc.OnClick;
import com.connotationjoke.qingguoguo.baselibrary.ioc.ViewById;
import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.ToastUtils;
import com.connotationjoke.qingguoguo.baselibrary.view.customdialog.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import connotationjoke.qingguoguo.com.framelibrary.BaseSkinActivity;
import connotationjoke.qingguoguo.com.framelibrary.DefaultNavigationBar;
import connotationjoke.qingguoguo.com.framelibrary.db.DaoSupportFactory;
import connotationjoke.qingguoguo.com.framelibrary.db.IDaoSupport;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/27 16:20
 * @Describe :
 */
public class MainActivity extends BaseSkinActivity implements View.OnClickListener {

    public final static String TAG = MainActivity.class.getSimpleName();
    int REQUEST_EXTERNAL_STORAGE = 1;

    @ViewById(R.id.test_tv)
    private TextView mTextView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTextView = viewById(R.id.test_tv);
        mTextView.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        testNavigationBar();
    }

    @Override
    protected void initData() {
        //崩溃日志上传服务器
        uploadCrashFile();
    }

    @CheckNet
    @OnClick({R.id.test_iv})
    private void testOnclick() {
        ToastUtils.showShort("测试,阿里热修复,加载补丁");
        //测试,阿里热修复
        //testAliAndFix();
        fixDexBug();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initPremiss() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        initPremiss();
    }

    @Override
    public void onClick(View v) {
//        Log.i(TAG, "修复后");
//        int i = 2 / 2;
//        ToastUtils.showShort("修复后:" + i);

//        Log.i(TAG, "修复前");
//        ToastUtils.showShort("修复前:" + 0);
//        int i = 2 / 0;
//        startActivity(TestActivity.class);

//        testShowDialog();

//        HttpUtils.with(this)
//                .url("http://is.snssdk.com/2/essay/discovery/v3/")
//                .addParam("iid", "6152551759")
//                .exchangeEngine(new OkHttpEngine())
//                .addParam("aid", "7")
//                .get()
//                .execute(new HttpCallBack<UserBean>() {
//                    @Override
//                    public void onSuccess(UserBean userBean) {
//                        LogUtils.i(TAG,userBean.getData().getCategories().getName());
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        LogUtils.i(TAG,"onCancel");
//                    }
//                });
        IDaoSupport dao = DaoSupportFactory.getFactory().getDao(Person.class);
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            list.add(new Person("qgg", 20 + i));
        }
        long startTime = System.currentTimeMillis();
        dao.insert(list);
        long endTime = System.currentTimeMillis();
        LogUtils.i(TAG, "插入数据时间 ：" + (endTime - startTime));
    }

    private void testNavigationBar() {
        ViewGroup viewGroup = findViewById(R.id.main_root);
        DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar.
                Builder(this, viewGroup)
                .setTitle("投稿")
                .setRightText("发布")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("测试一下");
                    }
                })
                .create();
    }

    private void testShowDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setContentView(R.layout.detail_comment_dialog)
                .setText(R.id.submit_btn, "微博分享嘎嘎")
                .setOnClickListener(R.id.submit_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("微博分享嘎嘎");
                    }
                })
                .addDefaultAnimation()
                .fullWidth()
                .fromBottom(true)
                .show();

        final EditText view = dialog.findView(R.id.comment_editor);
        dialog.setOnClickListener(R.id.account_icon_weibo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(view.getText().toString());
            }
        });
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
