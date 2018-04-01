package com.connotationjoke.qingguoguo.baselibrary.view.customdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/30
 * @describe :
 */

class CusAlertController {

    private AlertDialog mAlertDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;

    public CusAlertController(AlertDialog alertDialog, Window window) {
        this.mAlertDialog = alertDialog;
        this.mWindow = window;
    }

    /**
     * 获取Dialog
     *
     * @return
     */
    public AlertDialog getAlertDialog() {
        return mAlertDialog;
    }

    /**
     * 获取Window
     *
     * @return
     */
    public Window getWindow() {
        return mWindow;
    }

    public void setText(int viewId, CharSequence charSequence) {
        mViewHelper.setText(viewId,charSequence);
    }

    public <T extends View> T findView(int viewId) {
        return mViewHelper.findView(viewId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        mViewHelper.setOnClickListener(viewId,onClickListener);
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    static class AlertParams {
        public Context mContext;
        public int mThemeResId;
        /**
         * 点击空白是否能够取消,默认点击可以取消
         */
        public boolean mCancelable = true;
        /**
         * 取消监听，消失监听，按键监听
         */
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        /**
         * 布局LayoutId 布局view
         */
        public int mViewLayoutResId;
        public View mView;
        /**
         * 存放text 点击事件
         */
        public SparseArray<CharSequence> mTextArray;
        public SparseArray<View.OnClickListener> mClickListenerArray;
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mAnimations = 0;
        public int mGravity = Gravity.CENTER;

        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
            mTextArray = new SparseArray<>();
            mClickListenerArray = new SparseArray<>();
        }

        /**
         * 绑定参数
         *
         * @param cusAlertController
         */
        public void apply(CusAlertController cusAlertController) {
            DialogViewHelper viewHelper = null;
            //1.设置布局
            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            if (mView != null) {
                viewHelper = new DialogViewHelper(mView);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局 setContentView");
            }
            cusAlertController.setViewHelper(viewHelper);
            cusAlertController.getAlertDialog().setContentView(viewHelper.getContentView());

            //2.设置文本
            for (int i = 0; i < mTextArray.size(); i++) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            //3.设置点击事件
            for (int i = 0; i < mClickListenerArray.size(); i++) {
                viewHelper.setOnClickListener(mClickListenerArray.keyAt(i), mClickListenerArray.valueAt(i));
            }
            //4.配置自定义效果，全屏，从底部弹出，动画
            Window window = cusAlertController.getWindow();
            window.setGravity(mGravity);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = mWidth;
            attributes.height = mHeight;
            window.setAttributes(attributes);
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }
        }
    }
}
