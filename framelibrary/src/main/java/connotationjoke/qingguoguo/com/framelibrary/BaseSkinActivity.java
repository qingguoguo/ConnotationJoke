package connotationjoke.qingguoguo.com.framelibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.connotationjoke.qingguoguo.baselibrary.base.BaseActivity;
import com.connotationjoke.qingguoguo.baselibrary.util.LogUtils;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

import connotationjoke.qingguoguo.com.framelibrary.skin.SkinAttrSupport;
import connotationjoke.qingguoguo.com.framelibrary.skin.attr.SkinAttr;
import connotationjoke.qingguoguo.com.framelibrary.skin.attr.SkinView;
import connotationjoke.qingguoguo.com.framelibrary.skin.support.SkinAppCompatViewInflater;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/23 on 18:03
 * 描述:
 * 1.拦截View的创建
 * 2.解析属性
 * 3.统一交给SkinManager管理
 */

public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflater.Factory2 {
    private final static String TAG = "BaseSkinActivity";
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //拦截View的创建
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater, this);
        super.onCreate(savedInstanceState);
    }

    /**
     * @param parent
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = createView(parent, name, context, attrs);
        LogUtils.i(TAG, "拦截view:" + view);

        if (view != null) {
            //解析属性
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSKinAttrs(context, attrs);
            SkinView skinView = new SkinView(view, skinAttrs);

            //统一交给SkinManager管理
            managerSkinView(skinView);
        }
        return view;
    }

    /**
     * 统一管理SkinView
     *
     * @param skinView
     */
    private void managerSkinView(SkinView skinView) {

    }

    @SuppressLint("RestrictedApi")
    private View createView(View parent, String name, Context context, AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser) ?
                    ((XmlPullParser) attrs).getDepth() > 1 : shouldInheritContext((ViewParent) parent);
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                return false;
            }
            parent = parent.getParent();
        }
    }
}
