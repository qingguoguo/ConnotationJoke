package connotationjoke.qingguoguo.com.framelibrary;

import android.content.Context;
import android.view.ViewGroup;

import com.connotationjoke.qingguoguo.baselibrary.view.customdialog.navigationbar.AbsNavigationBar;

/**
 * 作者:qingguoguo
 * 创建日期：2018/4/1 on 22:10
 * 描述: 默认的 NavigationBar
 */

public class DefaultNavigationBar extends AbsNavigationBar {

    public DefaultNavigationBar(Builder.AbsNavigationBarParams barParams) {
        super(barParams);
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        //绑定效果
    }

    public static class Builder extends AbsNavigationBar.Builder {
        DefaultNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }
        //1.设置所有效果

        @Override
        public DefaultNavigationBar create() {
            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(P);
            return defaultNavigationBar;
        }

        public static class DefaultNavigationParams extends AbsNavigationBarParams {
            //2.放置所有效果

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
