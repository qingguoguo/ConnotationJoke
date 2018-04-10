package connotationjoke.qingguoguo.com.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import connotationjoke.qingguoguo.com.framelibrary.skin.SkinManager;
import connotationjoke.qingguoguo.com.framelibrary.skin.SkinResource;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/9
 * @describe :
 */

public enum SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorByName(resName);
            if (color == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setTextColor(color);
        }
    },
    BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            //背景是图片
            if (drawable != null) {
                ImageView imageView = (ImageView) view;
                imageView.setBackgroundDrawable(drawable);
            }

            //可能是颜色
            ColorStateList color = skinResource.getColorByName(resName);
            if (color != null) {
                view.setBackgroundColor(color.getDefaultColor());
            }
        }
    },
    SRC("src") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable == null) {
                return;
            }
            ImageView imageView = (ImageView) view;
            imageView.setImageDrawable(drawable);
        }
    };

    public String mResName;

    /**
     * 枚举的构造方法
     *
     * @param resName
     */
    SkinType(String resName) {
        this.mResName = resName;
    }

    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }

    /**
     * 获取皮肤资源
     *
     * @return
     */
    private static SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }
}
