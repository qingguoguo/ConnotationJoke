package connotationjoke.qingguoguo.com.framelibrary.skin.attr;

import android.view.View;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :
 */

public enum SkinType {

    TEXT_COLCOR("textColor") {
        @Override
        public void skin(View view, String resName) {

        }
    },
    BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {

        }
    },
    SRC("src") {
        @Override
        public void skin(View view, String resName) {

        }
    };

    public String mResName;

    SkinType(String resName) {
        this.mResName=resName;
    }

    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }
}
