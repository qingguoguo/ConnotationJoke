package connotationjoke.qingguoguo.com.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import connotationjoke.qingguoguo.com.framelibrary.skin.attr.SkinView;
import connotationjoke.qingguoguo.com.framelibrary.skin.callback.ISkinChangeListener;
import connotationjoke.qingguoguo.com.framelibrary.skin.config.SkinConfig;
import connotationjoke.qingguoguo.com.framelibrary.skin.config.SkinPreUtils;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :皮肤管理类
 */

public class SkinManager {
    private static SkinManager mInstance;
    private Context mContext;
    private SkinResource mSkinResource;
    private Map<WeakReference<ISkinChangeListener>, List<SkinView>> mSkinViews = new HashMap<>();

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        if (mInstance == null) {
            synchronized (SkinManager.class) {
                if (mInstance == null) {
                    mInstance = new SkinManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化 getApplicationContext防止内存泄漏
     *
     * @param context
     */
    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        //防止皮肤被删除，做措施
        String skinPath = getSkinPath();
        if (TextUtils.isEmpty(skinPath)) {
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return;
        }

        File file = new File(skinPath);
        if (!file.exists()) {
            //清空皮肤
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return;
        }

        String skinPageName = context.getPackageManager()
                .getPackageArchiveInfo(skinPath, PackageManager.GET_RECEIVERS).packageName;

        if (TextUtils.isEmpty(skinPageName)) {
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return;
        }

        //初始化mSkinResource
        mSkinResource = new SkinResource(mContext, skinPath);
    }

    /**
     * 加载皮肤
     *
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {
        //当前皮肤是不是一样的
        if (getSkinPath().equals(skinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        //判断文件是否存在
        File file = new File(skinPath);
        if (!file.exists()) {
            //清空皮肤
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return SkinConfig.SKIN_FILE_NO_EXISTS;
        }

        String skinPageName = mContext.getPackageManager()
                .getPackageArchiveInfo(skinPath, PackageManager.GET_RECEIVERS).packageName;

        if (TextUtils.isEmpty(skinPageName)) {
            SkinPreUtils.getInstance(mContext).clearSkinInfo();
            return SkinConfig.SKIN_FILE_ERROR;
        }

        //需要做一些判断，校验签名等
        //初始化资源
        mSkinResource = new SkinResource(mContext, skinPath);
        skin();
        //保存皮肤的状态
        saveSkinStatus(skinPath);
        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 保存皮肤的路径
     *
     * @param skinPath
     */
    private void saveSkinStatus(String skinPath) {
        SkinPreUtils.getInstance(mContext).saveSkinPath(skinPath);
    }

    /**
     * 恢复默认
     *
     * @return
     */
    public int restoreDefault() {
        //判断当前是否有皮肤
        if (TextUtils.isEmpty(getSkinPath())) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        //当前apk的资源路径
        mSkinResource = new SkinResource(mContext, mContext.getPackageResourcePath());
        skin();
        SkinPreUtils.getInstance(mContext).clearSkinInfo();
        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 换肤
     */
    public void skin() {
        Set<WeakReference<ISkinChangeListener>> weakReferences = mSkinViews.keySet();
        for (WeakReference<ISkinChangeListener> weakReference : weakReferences) {
            List<SkinView> skinViews = mSkinViews.get(weakReference);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
            //接口回调
            weakReference.get().changeSkin(mSkinResource);
        }
    }

    /**
     * 获取SKinViews
     *
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(ISkinChangeListener activity) {
        return mSkinViews.get(activity);
    }

    /**
     * 注册
     *
     * @param skinChangeListener
     * @param skinViews
     */
    public void register(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mSkinViews.put(new WeakReference<>(skinChangeListener), skinViews);
    }

    /**
     * 获取当前皮肤资源
     *
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    /**
     * 检测当前页面是否需要换肤
     *
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        if (!TextUtils.isEmpty(getSkinPath()) && mSkinResource != null) {
            skinView.skin();
        }
    }

    /**
     * 获取皮肤路径
     *
     * @return
     */
    public String getSkinPath() {
        return SkinPreUtils.getInstance(mContext).getSkinPath();
    }

    /**
     * 防止内存泄漏
     *
     * @param skinChangeListener
     */
    public void unRegister(ISkinChangeListener skinChangeListener) {
        mSkinViews.remove(skinChangeListener);
    }
}
