package com.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.customview.R;

/**
 * Created by jianqiang.hu on 2017/6/7.
 */

public class CustomProgressBar extends View {

    /**
     * 第一圈的颜色
     */
    private int mFirstColor;

    /**
     * 第二圈的颜色
     */
    private int mSecondColor;

    /**
     *  圈的宽度
     */
    private int mCircleWidth;

    /**
     * 画笔
     */
    private Paint mPaint;


    /**
     * 当前进度
     */
    private int mProgress;

    /**
     * 速度
     */
    private int mSpeed;


    /**
     * 是否下一个
     */
    private boolean isNext=false;

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgress,defStyleAttr,0);
        mFirstColor=typedArray.getColor(R.styleable.CustomProgress_firstColor, Color.GREEN);
        mSecondColor=typedArray.getColor(R.styleable.CustomProgress_secondColor,Color.RED);
        mCircleWidth=typedArray.getDimensionPixelSize(R.styleable.CustomProgress_circleWidth, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20,getResources().getDisplayMetrics()));
        mSpeed=typedArray.getInt(R.styleable.CustomProgress_speed,20);//默认20

        typedArray.recycle();

        mPaint=new Paint();
        /**
         * 绘图线程
         */
        new Thread(){
            @Override
            public void run() {
                while (true){
                    mProgress++;
                    if (mProgress==360) {
                        mProgress=0;//画完一圈重新置为0
                        if (!isNext) {
                            isNext = true;
                        } else {
                            isNext = false;
                        }
                    }
                        postInvalidate();
                        try {
                            Thread.sleep(mSpeed);
                        } catch (InterruptedException e) {

                        }


                }
            }
        }.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centre=getWidth()/2;//获取圆心x的坐标
        int radius=centre-mCircleWidth/2;//半径
        mPaint.setStrokeWidth(mCircleWidth);//圆环的宽度
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);

        RectF oval=new RectF(centre - radius, centre - radius, centre + radius, centre + radius);// 用于定义的圆弧的形状和大小的界限
        if (!isNext){
            // 第一颜色的圈完整，第二颜色跑
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(centre,centre,radius,mPaint);//画圆圈
            mPaint.setColor(mSecondColor);
            canvas.drawArc(oval,-90,mProgress,false,mPaint);
        }else{
            mPaint.setColor(mSecondColor); // 设置圆环的颜色
            canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
            mPaint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        }
    }
}
