package connotationjoke.qingguoguo.com.framelibrary.skin;

import android.content.Context;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :皮肤管理类
 */

public class SkinManager {
    private static SkinManager mInstance;
    private Context mContext;

    private SkinManager() {
    }

    public void init(Context context) {
        this.mContext = context;
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

    public int loadSkin(Context context, String skinPath) {
        //需要做一些判断，校验签名等
        //初始化资源
        SkinResource skinResource = new SkinResource(mContext, skinPath);
        return 0;
    }

    public int restoreDefault() {
        return 0;
    }
}
