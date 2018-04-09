package connotationjoke.qingguoguo.com.framelibrary.skin.attr;

import android.view.View;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :
 */

public class SkinAttr {
    private SkinType mSkinType;
    private String mResName;

    public SkinAttr(SkinType sKinType, String attributeName) {
        this.mResName = attributeName;
        this.mSkinType = sKinType;
    }

    public void skin(View view) {
        mSkinType.skin(view, mResName);
    }
}
