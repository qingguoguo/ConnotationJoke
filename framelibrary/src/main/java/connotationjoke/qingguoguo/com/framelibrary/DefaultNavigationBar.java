package connotationjoke.qingguoguo.com.framelibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.connotationjoke.qingguoguo.baselibrary.view.customdialog.navigationbar.AbsNavigationBar;

/**
 * 作者:qingguoguo
 * 创建日期：2018/4/1 on 22:10
 * 描述: 默认的 NavigationBar
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {

    public DefaultNavigationBar(Builder.DefaultNavigationParams barParams) {
        super(barParams);
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        //绑定效果
        setText(R.id.title, getBarParams().mTitle);
        setText(R.id.right_text, getBarParams().mRightText);
        setOnClickListenter(R.id.right_text, getBarParams().mRightClickListener);
        //左边写一个默认的关闭 Activity
        setOnClickListenter(R.id.back, getBarParams().mLeftClickListener);
    }

    public static class Builder extends AbsNavigationBar.Builder {
        DefaultNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
        }

        //1.设置所有效果

        public DefaultNavigationBar.Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        public DefaultNavigationBar.Builder setRightText(String rightText) {
            P.mRightText = rightText;
            return this;
        }

        public DefaultNavigationBar.Builder setRightOnClickListener(
                View.OnClickListener rightClickListener) {
            P.mRightClickListener = rightClickListener;
            return this;
        }

        public DefaultNavigationBar.Builder setLeftOnClickListener(
                View.OnClickListener leftClickListener) {
            P.mLeftClickListener = leftClickListener;
            return this;
        }

        @Override
        public DefaultNavigationBar create() {
            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(P);
            return defaultNavigationBar;
        }

        public static class DefaultNavigationParams extends AbsNavigationBarParams {
            //2.放置所有效果
            public String mTitle;
            public String mRightText;
            public View.OnClickListener mRightClickListener;
            public View.OnClickListener mLeftClickListener =new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) mContext).finish();
                }
            };

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
