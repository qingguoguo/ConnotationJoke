package connotationjoke.qingguoguo.com.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :皮肤资源
 */

public class SkinResource {
    private Resources mSkinResource;
    private String mSkinPageName;

    public SkinResource(Context context, String skinPath) {
        Resources resources = context.getResources();
        Class<AssetManager> assetManagerClass = AssetManager.class;
        AssetManager assetManager = null;
        try {
            assetManager = assetManagerClass.newInstance();
            Method addAssetPath = assetManagerClass.getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager,
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + skinPath);
            mSkinResource = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
            mSkinPageName = context.getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_RECEIVERS).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过名字获取drawable
     *
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int identifier = mSkinResource.getIdentifier(resName, "drawable", mSkinPageName);
            Drawable drawable = mSkinResource.getDrawable(identifier);
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过名字获取颜色
     *
     * @param resName
     * @return
     */
    public ColorStateList getColorByName(String resName) {
        try {
            int identifier = mSkinResource.getIdentifier(resName, "color", mSkinPageName);
            ColorStateList color = mSkinResource.getColorStateList(identifier);
            return color;
        } catch (Exception e) {
            return null;
        }
    }

//    public Drawable getSrcByName(String resName) {
//
//    }
}
