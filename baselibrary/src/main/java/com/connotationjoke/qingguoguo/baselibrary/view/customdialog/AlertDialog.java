package com.connotationjoke.qingguoguo.baselibrary.view.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.connotationjoke.qingguoguo.baselibrary.R;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/30
 * @describe :自定义 万能 AlertDialog
 */
public class AlertDialog extends Dialog {
    private CusAlertController mAlert;

    public AlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new CusAlertController(this, this.getWindow());
    }

    public void setText(int viewId, CharSequence charSequence) {
        mAlert.setText(viewId,charSequence);
    }

    public <T extends View> T findView(int viewId) {
        return mAlert.findView(viewId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        mAlert.setOnClickListener(viewId,onClickListener);
    }

    public static class Builder {

        private CusAlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new CusAlertController.AlertParams(context, themeResId);
        }

        /**
         * 构造AlertDialog
         *
         * @return
         */
        public AlertDialog create() {
            AlertDialog dialog = new AlertDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            if (P.mOnCancelListener != null) {
                dialog.setOnCancelListener(P.mOnCancelListener);
            }
            if (P.mOnDismissListener != null) {
                dialog.setOnDismissListener(P.mOnDismissListener);
            }
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public Builder setContentView(@LayoutRes int viewLayoutResId) {
            P.mView = null;
            P.mViewLayoutResId = viewLayoutResId;
            return this;
        }

        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * 设置文本
         *
         * @param viewId
         * @param text
         * @return
         */
        public Builder setText(int viewId, CharSequence text) {
            P.mTextArray.put(viewId, text);
            return this;
        }

        /**
         * 设置点击事件
         *
         * @param viewId
         * @param clickListener
         * @return
         */
        public Builder setOnClickListener(int viewId, View.OnClickListener clickListener) {
            P.mClickListenerArray.put(viewId, clickListener);
            return this;
        }

        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimations = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        public Builder setWidthAndHeight(int windth, int height) {
            P.mWidth = windth;
            P.mHeight = height;
            return this;
        }

        public Builder addDefaultAnimation() {
            P.mAnimations = R.style.dialog_scale_anim;
            return this;
        }

        public Builder addAnimations(int styleAnimation) {
            P.mAnimations = styleAnimation;
            return this;
        }

        public AlertDialog show() {
            AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
