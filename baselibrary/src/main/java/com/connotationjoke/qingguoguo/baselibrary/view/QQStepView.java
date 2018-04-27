package com.connotationjoke.qingguoguo.baselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.connotationjoke.qingguoguo.baselibrary.R;


/**
 * @author :qingguoguo
 * @datetime ：2018/4/26
 * @describe :仿QQ计步
 */

public class QQStepView extends View {

    private int mTextSize;
    private int mMaxStep;
    private int mStep;
    private int mInnerCircleColor = Color.RED;
    private int mOutCircleColour = Color.BLUE;
    private int mTextColour = Color.BLUE;
    private int mOutCircleWidth;
    private Paint outCirclePaint;
    private Paint textPaint;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOutCircleColour = ta.getColor(R.styleable.QQStepView_outCircleColour, mOutCircleColour);
        mInnerCircleColor = ta.getColor(R.styleable.QQStepView_innerCircleColor, mInnerCircleColor);
        mTextColour = ta.getColor(R.styleable.QQStepView_textColour, mTextColour);
        mOutCircleWidth = ta.getDimensionPixelSize(R.styleable.QQStepView_outCircleWidth, 20);
        mTextSize = ta.getDimensionPixelSize(R.styleable.QQStepView_textSize, 15);
        ta.recycle();

        outCirclePaint = new Paint();
        outCirclePaint.setAntiAlias(true);
        outCirclePaint.setColor(mOutCircleColour);
        outCirclePaint.setStrokeWidth(mOutCircleWidth);
        outCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        outCirclePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextColour);
        textPaint.setTextSize(mTextSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //正方形显示
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSize > heightSize) {
            widthSize = heightSize;
        } else {
            heightSize = widthSize;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 设置最大值
     *
     * @param maxStep
     */
    public void setMaxStep(int maxStep) {
        mMaxStep = maxStep;
    }

    /**
     * 设置当前值
     *
     * @param currentStep
     */
    public void setCurrentStep(int currentStep) {
        mStep = currentStep;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int centreX = width / 2;
        int centreY = height / 2;

        //画底部的圆弧
        //float left, float top, float right, float bottom
        RectF rectF = new RectF(mOutCircleWidth / 2, mOutCircleWidth / 2,
                getWidth() - mOutCircleWidth / 2, getHeight() - mOutCircleWidth / 2);
        //@NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter,@NonNull Paint paint
        canvas.drawArc(rectF, 135, 270, false, outCirclePaint);

        //画上面的圆弧
        outCirclePaint.setColor(mInnerCircleColor);
        float sweepAngle = (float) mStep / mMaxStep;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, outCirclePaint);

        //画文字
        String text = String.valueOf(mStep);
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        int startX = centreX - rect.width() / 2;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int baseLine = centreY + (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        canvas.drawText(text, startX, baseLine, textPaint);
    }
}
