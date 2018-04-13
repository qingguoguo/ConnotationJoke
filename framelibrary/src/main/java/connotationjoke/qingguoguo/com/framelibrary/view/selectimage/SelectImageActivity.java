package connotationjoke.qingguoguo.com.framelibrary.view.selectimage;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;

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
     * 是否显示相机的 EXTRA_KEY
     * 总共可以选择多少张图片的 EXTRA_KEY
     * 原始的图片路径的 EXTRA_KEY
     * 选择模式的 EXTRA_KEY
     * 返回选择图片列表的 EXTRA_KEY
     * 加载所有的数据 LOADER_TYPE
     */
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
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
    private int mMaxCount = 8;
    private boolean mShowCamera = true;
    private ArrayList<String> mResultList;

    private RecyclerView mImageListRv;
    private TextView mSelectNumTv;
    private TextView mSelectPreview;

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
                intent.putStringArrayListExtra(EXTRA_RESULT, mResultList);
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
        //StatusBarUtil.statusBarTintColor(this, Color.parseColor("#261f1f"));
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

    @Override
    public void onCamera() {
        File imageFile = null;
        try {
            imageFile = FileUtils.createTmpFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imageFile == null) {
            return;
        }
        this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(imageFile)));
        Intent data = new Intent();
        this.mResultList.add(imageFile.getAbsolutePath());
        data.putStringArrayListExtra(EXTRA_RESULT, this.mResultList);
        this.setResult(-1, data);
        //this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 1.第一个要把图片加到集合
        // 2.调用sureSelect()方法
        // 3.通知系统本地有图片改变，下次进来可以找到这张图片
        // notify system the image has change
        // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempFile));
    }
}
