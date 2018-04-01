package com.connotationjoke.qingguoguo.baselibrary.view.customdialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/30
 * @describe :AlertDialog View 辅助类
 */

public class DialogViewHelper {
    private View mContentView;
    /**
     * 缓存View,减少findViewById次数
     */
    private SparseArray<WeakReference<View>> mViewArray;

    public DialogViewHelper(View contentView) {
        this.mContentView = contentView;
        this.mViewArray = new SparseArray<>();
    }

    public DialogViewHelper(Context context, int viewLayoutResId) {
        this.mContentView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
        this.mViewArray = new SparseArray<>();
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param charSequence
     */
    public void setText(int viewId, CharSequence charSequence) {
        TextView view = findView(viewId);
        if (view != null) {
            view.setText(charSequence);
        }
    }

    /**
     * 缓存findViewById过的View
     *
     * @param viewId
     * @return
     */
    public  <T extends View> T findView(int viewId) {
        View view;
        WeakReference<View> viewWeakReference = mViewArray.get(viewId);
        if (viewWeakReference == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViewArray.put(viewId, new WeakReference<>(view));
            }
        } else {
            view = viewWeakReference.get();
        }
        return (T) view;
    }

    /**
     * 给布局的View设置监听
     *
     * @param viewId
     * @param onClickListener
     */
    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view = findView(viewId);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    /**
     * 获取布局ContentView
     *
     * @return
     */
    public View getContentView() {
        return mContentView;
    }
}
