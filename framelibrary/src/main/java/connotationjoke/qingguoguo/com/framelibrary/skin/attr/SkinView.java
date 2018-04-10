package connotationjoke.qingguoguo.com.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :
 */

public class SkinView {
    private View mView;
    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttrs = skinAttrs;
    }

    public void skin() {
        for (SkinAttr skinAttr : mSkinAttrs) {
            skinAttr.skin(mView);
        }
    }
}
