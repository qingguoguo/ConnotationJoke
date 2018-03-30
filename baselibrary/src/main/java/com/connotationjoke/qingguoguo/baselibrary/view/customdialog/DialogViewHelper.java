package com.connotationjoke.qingguoguo.baselibrary.view.customdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/30
 * @describe :AlertDialog view 辅助类
 */

public class DialogViewHelper {
    private View mContextView;
    private View mContentView;

    public DialogViewHelper(Context context, int viewLayoutResId) {
        this.mContextView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
    }

    public DialogViewHelper() {

    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public void setText(int viewId, CharSequence charSequence) {


    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {

    }

    public View getContentView() {
        return mContentView;
    }
}
