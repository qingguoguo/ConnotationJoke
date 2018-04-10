package connotationjoke.qingguoguo.com.framelibrary.skin.config;

import android.content.Context;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/10
 * @describe :
 */

public class SkinPreUtils {
    private static SkinPreUtils mInstance;
    private Context mContext;

    private SkinPreUtils() {
    }

    private SkinPreUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static SkinPreUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SkinPreUtils.class) {
                if (mInstance == null) {
                    mInstance = new SkinPreUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存皮肤路径
     *
     * @param path
     */
    public void saveSkinPath(String path) {
        mContext.getSharedPreferences(SkinConfig.SKIN_CONFIG_NAME, Context.MODE_PRIVATE)
                .edit().putString(SkinConfig.SKIN_PATH_NAME, path).commit();
    }

    /**
     * 获取皮肤路径
     */
    public String getSkinPath() {
        return mContext.getSharedPreferences(SkinConfig.SKIN_CONFIG_NAME, Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH_NAME, "");
    }

    /**
     * 清空皮肤
     */
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
