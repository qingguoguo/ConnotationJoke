package com.qingguoguo.connotationjoke;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.connotationjoke.qingguoguo.baselibrary.ExceptionCrashHandler;
import com.connotationjoke.qingguoguo.baselibrary.fixbug.FixDexManager;
import com.connotationjoke.qingguoguo.baselibrary.http.HttpUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.ToastUtils;
import com.connotationjoke.qingguoguo.baselibrary.view.customdialog.AlertDialog;
import com.qingguoguo.connotationjoke.doublesevice.MessageService;
import com.qingguoguo.connotationjoke.hookstartactivity.HookStartActivityUtils;
import com.qingguoguo.connotationjoke.hookstartactivity.ProxyActivity;
import com.qingguoguo.connotationjoke.hookstartactivity.TestHookActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import connotationjoke.qingguoguo.com.framelibrary.BaseSkinActivity;
import connotationjoke.qingguoguo.com.framelibrary.db.DaoSupportFactory;
import connotationjoke.qingguoguo.com.framelibrary.db.IDaoSupport;
import connotationjoke.qingguoguo.com.framelibrary.http.HttpCallBack;
import connotationjoke.qingguoguo.com.framelibrary.skin.SkinManager;
import connotationjoke.qingguoguo.com.framelibrary.view.selectimage.ImageSelector;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/27 16:20
 * @Describe :
 */
public class MainActivity extends BaseSkinActivity implements View.OnClickListener {

    public final static String TAG = MainActivity.class.getSimpleName();
    int REQUEST_EXTERNAL_STORAGE = 1;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

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

    private void testOnclick() {
        ToastUtils.showShort("测试,阿里热修复,加载补丁");
        //测试,阿里热修复
        //testAliAndFix();
        //fixDexBug();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initPermission() {
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
        initPermission();
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
        //testHttp();
//        testInsert();
//        testQuery();
        //testSkin();
    }

    private void testSkin() {
        Resources resources = getResources();
        Class<AssetManager> assetManagerClass = AssetManager.class;
        AssetManager assetManager = null;
        try {
            assetManager = assetManagerClass.newInstance();
            Method addAssetPath = assetManagerClass.getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager,
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "app-debug.apk");
            Resources newResources = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
            int identifier = newResources.getIdentifier("bg_header", "drawable", "com.blankj.androidutilcode");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testQuery() {
        IDaoSupport<Person> dao = DaoSupportFactory.getFactory().getDao(Person.class);
        List<Person> people = dao.querySupport().queryAll();
        ToastUtils.showShort("数据库文件长度：" + people.size());
    }

    private void testInsert() {
        IDaoSupport<Person> dao = DaoSupportFactory.getFactory().getDao(Person.class);
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new Person("qgg", 20 + i));
        }
        long startTime = System.currentTimeMillis();
        dao.insert(list);
        long endTime = System.currentTimeMillis();
        LogUtils.i(TAG, "插入数据时间 ：" + (endTime - startTime));
    }

    private void testHttp() {
        HttpUtils.with(this)
                .url("http://is.snssdk.com/2/essay/discovery/v3/")
                .addParam("iid", "6152551759")
                .addParam("aid", "7")
                .cache(true)
                .get()
                .execute(new HttpCallBack<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        LogUtils.i(TAG, userBean.getData().getCategories().getName());
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onCancel() {
                        LogUtils.i(TAG, "onCancel");
                    }
                });
    }

    private void testNavigationBar() {
        // ViewGroup viewGroup = findViewById(R.id.main_root);
//        DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar.
//                Builder(this, viewGroup)
//                .setTitle("投稿")
//                .setRightText("发布")
//                .setRightOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ToastUtils.showShort("测试一下");
//                    }
//                })
//                .create();
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

    public void skin(View view) {
        //服务器上下载皮肤
        String SkinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "app-debug.apk";
        // 换肤
        int result = SkinManager.getInstance().loadSkin(SkinPath);
    }

    public void skin1(View view) {
        //恢复默认
        int result = SkinManager.getInstance().restoreDefault();
    }

    private ArrayList<String> mImageList;
    private final int SELECT_IMAGE_REQUEST = 0x0011;

    public void skin2(View view) {
        //跳转
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        Intent intent = new Intent(this,SelectImageActivity.class);
//        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT,9);
//        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE,SelectImageActivity.MODE_MULTI);
//        intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mImageList);
//        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, true);
//        startActivityForResult(intent, SELECT_IMAGE_REQUEST);

        // 第一个只关注想要什么，良好的封装性，不要暴露太多
        ImageSelector.create().count(9).multi().origin(mImageList)
                .showCamera(true).start(this, SELECT_IMAGE_REQUEST);
    }

    public void start(View view) {
        startService(new Intent(this, MessageService.class));
    }

    public void stop(View view) {
        stopService(new Intent(this, MessageService.class));
    }

    public void onBind(View view) {
    }

    public void unbind(View view) {
        HookStartActivityUtils hookStartActivityUtils = new HookStartActivityUtils(this, ProxyActivity.class);
        try {
            hookStartActivityUtils.hookStartActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, TestHookActivity.class));
    }
}
