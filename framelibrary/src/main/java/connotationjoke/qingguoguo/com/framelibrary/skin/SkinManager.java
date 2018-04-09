package connotationjoke.qingguoguo.com.framelibrary.skin;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import connotationjoke.qingguoguo.com.framelibrary.skin.attr.SkinView;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :皮肤管理类
 */

public class SkinManager {
    private static SkinManager mInstance;
    private Context mContext;
    private SkinResource mSkinResource;

    private SkinManager() {
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
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

    private Map<Activity, List<SkinView>> mSkinViews = new HashMap<>();

    public int loadSkin(String skinPath) {
        //需要做一些判断，校验签名等
        //初始化资源
        mSkinResource = new SkinResource(mContext, skinPath);
        Set<Activity> activities = mSkinViews.keySet();
        for (Activity key : activities) {
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
        return 0;
    }

    public int restoreDefault() {
        return 0;
    }

    /**
     * 获取SKinViews
     *
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    public void register(Activity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity, skinViews);
    }

    public SkinResource getSkinResource() {
        return mSkinResource;
    }
}
