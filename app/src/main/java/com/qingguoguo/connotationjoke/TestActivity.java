package com.qingguoguo.connotationjoke;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

import com.connotationjoke.qingguoguo.baselibrary.view.QQStepView;

import connotationjoke.qingguoguo.com.framelibrary.BaseSkinActivity;

/**
 * @author :qingguoguo
 * @datetime ：2018/3/28 9:52
 * @Describe :
 */
public class TestActivity extends BaseSkinActivity {

    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test;
    }

    @Override
    protected void initTitle() {
    }
    QQStepView qqStepView;
    @Override
    protected void initView() {
        qqStepView = findViewById(R.id.qq_step);
    }

    @Override
    protected void initData() {
        qqStepView.setMaxStep(5000);
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(0,3000);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentStep = (int) animation.getAnimatedValue();
                qqStepView.setCurrentStep(currentStep);
            }
        });
        valueAnimator.start();
    }
}
