package connotationjoke.qingguoguo.com.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :皮肤View，包含view和需要换肤的属性
 */

public class SkinView {
    private View mView;
    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttrs = skinAttrs;
    }

    /**
     * 换肤
     */
    public void skin() {
        for (SkinAttr skinAttr : mSkinAttrs) {
            skinAttr.skin(mView);
        }
    }
}
