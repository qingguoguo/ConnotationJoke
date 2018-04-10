package connotationjoke.qingguoguo.com.framelibrary.skin.callback;

import connotationjoke.qingguoguo.com.framelibrary.skin.SkinResource;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/10
 * @describe :
 */

public interface ISkinChangeListener {
    /**
     * 换肤的回调接口，可以处理自定义view属性换肤
     *
     * @param skinResource
     */
    void changeSkin(SkinResource skinResource);
}
