package connotationjoke.qingguoguo.com.framelibrary.view.selectimage;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;
import com.connotationjoke.qingguoguo.baselibrary.util.StatusBarUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import connotationjoke.qingguoguo.com.framelibrary.BaseSkinActivity;
import connotationjoke.qingguoguo.com.framelibrary.DefaultNavigationBar;
import connotationjoke.qingguoguo.com.framelibrary.R;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/13 16:01
 * @Describe :选择图片的Activity
 */
public class SelectImageActivity extends BaseSkinActivity implements View.OnClickListener, SelectImageListener {

    private static final String TAG = "SelectImageActivity";

    /**
     * 传过来的 Key
     * 是否显示相机的 EXTRA_SHOW_CAMERA
     * 总共可以选择多少张图片 EXTRA_SELECT_COUNT
     * 原始的图片路径的 EXTRA_DEFAULT_SELECTED_LIST
     * 选择模式的 EXTRA_SELECT_MODE
     * 加载所有的数据 LOADER_TYPE
     */
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";
    private static final int LOADER_TYPE = 0x0021;

    /**
     * 获取传递过来的参数
     * 选择图片的模式 - 多选 MODE_MULTI
     * 选择图片的模式 - 单选 MODE_SINGLE
     * 单选或者多选，int类型的 type mMode
     * int 类型的图片张数 mMaxCount
     * boolean 类型的是否显示拍照按钮
     * ArrayList<String> 已经选择好的图片 mResultList
     */
    public static final int MODE_MULTI = 0x0011;
    public static int MODE_SINGLE = 0x0012;
    private int mMode = MODE_MULTI;
    private int mMaxCount = 9;
    private boolean mShowCamera = true;
    private ArrayList<String> mResultList;

    private RecyclerView mImageListRv;
    private TextView mSelectNumTv;
    private TextView mSelectPreview;
    private File mImageFile = null;

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_image_selector;
    }

    @Override
    protected void initView() {
        mImageListRv = findViewById(R.id.image_list_rv);
        mSelectNumTv = findViewById(R.id.select_num);
        mSelectPreview = findViewById(R.id.select_preview);
        findViewById(R.id.select_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 选择好的图片回传过去
                Intent intent = new Intent();
                intent.putStringArrayListExtra(ImageSelector.EXTRA_RESULT, mResultList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new
                DefaultNavigationBar.Builder(this)
                .setTitle("所有图片")
                .create();
        // 改变状态栏的颜色
        StatusBarUtil.setStatusBarTintColor(this, Color.parseColor("#261f1f"));
    }

    @Override
    protected void initData() {
        //获取传递过来的参数
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }
        //初始化本地图片数据
        initImageList();
        //改变布局显示
        exchangeViewShow();
    }

    /**
     * 改变布局显示 需要及时更新，每次点击的地方
     */
    private void exchangeViewShow() {
        // 预览是不是可以点击，显示什么颜色
        if (mResultList.size() > 0) {
            // 至少选择了一张
            mSelectPreview.setEnabled(true);
            mSelectPreview.setOnClickListener(this);
        } else {
            // 一张都没选
            mSelectPreview.setEnabled(false);
            mSelectPreview.setOnClickListener(null);
        }
        // 中间图片的张数也要显示
        mSelectNumTv.setText(mResultList.size() + "/" + mMaxCount);
    }

    /**
     * ContentProvider获取内存卡中所有的图片
     */
    private void initImageList() {
        // 耗时操作，开线程，AsyncTask，最好的方式是用LoaderManager
        // int id 查询全部
        getLoaderManager().initLoader(LOADER_TYPE, null, mLoaderCallback);
    }

    /**
     * 加载图片的CallBack
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                private final String[] IMAGE_PROJECTION = {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.MIME_TYPE,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media._ID};

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    LogUtils.i(TAG, "onCreateLoader id:" + id + ",args:" + args);
                    // 查询数据库一样 语句
                    CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR "
                                    + IMAGE_PROJECTION[3] + "=? ",
                            new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                    return cursorLoader;
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    LogUtils.i(TAG, "loader:" + loader + ",Cursor:" + data);
                    // 解析，封装到集合  只保存String路径
                    if (data != null && data.getCount() > 0) {
                        ArrayList<String> images = new ArrayList<>();
                        // 如果需要显示拍照，就在第一个位置上加一个空String
                        if (mShowCamera) {
                            images.add("");
                        }

                        // 不断的遍历循环
                        while (data.moveToNext()) {
                            // 只保存路径
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            images.add(path);
                        }
                        // 显示列表数据
                        showImageList(images);
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                }
            };

    /**
     * 展示获取到的图片显示到列表
     *
     * @param images
     */
    private void showImageList(ArrayList<String> images) {
        SelectImageListAdapter listAdapter = new SelectImageListAdapter(this, images, mResultList, mMaxCount);
        listAdapter.setOnSelectImageListener(this);
        mImageListRv.setLayoutManager(new GridLayoutManager(this, 3));
        mImageListRv.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        // 图片预览
    }

    @Override
    public void select() {
        exchangeViewShow();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCamera() {
        //打开相机
        showCameraAction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 1.第一个要把图片加到集合
        // 2.调用sureSelect()方法
        // 3.通知系统本地有图片改变，下次进来可以找到这张图片
        // notify system the image has change
        // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempFile));
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mImageFile != null) {
                    saveImage(mImageFile);
                }
            } else {
                while (mImageFile != null && mImageFile.exists()) {
                    boolean success = mImageFile.delete();
                    if (success) {
                        mImageFile = null;
                    }
                }
            }
        }
    }

    private void saveImage(File imageFile) {
        if(imageFile != null) {
            // notify system the image has change
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
            Intent data = new Intent();
            mResultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(ImageSelector.EXTRA_RESULT, mResultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    /**
     * 打开相机
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showCameraAction() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.mis_permission_rationale_write_storage),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(this.getPackageManager()) != null) {
                try {
                    mImageFile = FileUtils.createTmpFile(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mImageFile != null && mImageFile.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(this, R.string.mis_error_image_not_exist, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.mis_msg_no_camera, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission(final String permission, String rationale, final int requestCode){
        if(shouldShowRequestPermissionRationale(permission)){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        }else{
            requestPermissions(new String[]{permission}, requestCode);
        }
    }
}
