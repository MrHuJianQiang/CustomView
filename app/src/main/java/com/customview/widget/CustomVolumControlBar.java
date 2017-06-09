package com.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.customview.R;

/**
 * Created by jianqiang.hu on 2017/6/8.
 */

public class CustomVolumControlBar extends View {

    /**
     * 第一圈颜色
     */
    private int mFirstColor;
    /**
     * 第二圈颜色
     */

    private int mSecondColor;

    /**
     *圆环宽度
     */
    private int mCircleWidth;

    /**
     * 当前进度
     */
    private int mCurrentCount=3;

    /**
     * 画笔
     *
     */
    private Paint mPaint;


    /**
     * 中间图片
     */
    private Bitmap mImage;

    /**
     * 每个小块的间隙
     */
    private  int mSpliteSize;

    /**
     * 个数
     */
    private int mCount;

    private Rect mRect;

    public CustomVolumControlBar(Context context) {
        super(context);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 获取自定义属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolum,defStyleAttr,0);
        mFirstColor=typedArray.getColor(R.styleable.CustomVolum_volumFirstColor, Color.parseColor("#30000000"));
        mSecondColor=typedArray.getColor(R.styleable.CustomVolum_volumSecondColor,Color.WHITE);
        mCircleWidth=typedArray.getDimensionPixelSize(R.styleable.CustomVolum_volumCircleWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20,getResources().getDisplayMetrics()));
        mImage= BitmapFactory.decodeResource(getResources(),typedArray.getResourceId(R.styleable.CustomVolum_volumBg,0));
        mCount=typedArray.getInt(R.styleable.CustomVolum_volumDotCount,20);
        mSpliteSize=typedArray.getInt(R.styleable.CustomVolum_volumSpliteSize,20);

        typedArray.recycle();

        mPaint=new Paint();
        mRect=new Rect();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStrokeWidth(mCircleWidth);//宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔触
        mPaint.setStyle(Paint.Style.STROKE);//空心


        int centre=getWidth()/2;//圆心坐标
        int radius=centre-mCircleWidth/2;//半径

        /**
         * 画块块去
         */
        drawOval(canvas,centre,radius);

        /**
         * 计算内切正方形的位置
         */

        int relRadius=radius-mCircleWidth/2;// 获得内圆的半径

        /**
         * 内切正方形的距离左边的距离
         */
        mRect.left=mCircleWidth+(int)(relRadius-((Math.sqrt(2)*1.0/2)*radius));
        /**
         * 内切正方形的距离顶边的距离
         */
        mRect.top=mCircleWidth+(int)(relRadius-((Math.sqrt(2)*1.0/2)*radius));
        mRect.right=mRect.left+(int)(Math.sqrt(2)*relRadius);
        mRect.bottom=mRect.top+(int)(Math.sqrt(2)*relRadius);



        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */
        if (mImage.getWidth()<Math.sqrt(2)*radius){
            mRect.left=(int)(getWidth()*1.0f/2-mImage.getWidth()*1.0f/2);
            mRect.top=(int)(getWidth()*1.0f/2-mImage.getHeight()*1.0f/2);
            mRect.right=(int)(getWidth()*1.0f/2+mImage.getWidth()*1.0f/2);
            mRect.bottom=(int)(getWidth()*1.0f/2+mImage.getHeight()*1.0f/2);
        }
        canvas.drawBitmap(mImage,null,mRect,mPaint);
    }


    /**
     * 根据参数画出每个小块
     * @param canvas
     * @param centre
     * @param radius
     */
    private void drawOval(Canvas canvas,int centre,int radius){
        /**
         * 根据需要画的个数以及间隙计算每个块块所占的比例*360
         */
        float itemSize=(360*1.0f-mCount*mSpliteSize)/mCount;
        RectF oval=new RectF(centre-radius,centre-radius,centre+radius,centre+radius);// 用于定义的圆弧的形状和大小的界限
        mPaint.setColor(mFirstColor);
        /**drawArc 参数含义
         * oval :指定圆弧的外轮廓矩形区域。
         startAngle: 圆弧起始角度，单位为度。
         sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。关键是这个变量，下面将会详细介绍。
         paint: 绘制圆弧的画板属性，如颜色，是否填充等
         */
        for (int i =0;i<mCount;i++){//每次画一个弧度为itemsize度的圆弧
            canvas.drawArc(oval,i*(itemSize+mSpliteSize),itemSize,false,mPaint);
        }
        mPaint.setColor(mSecondColor);
        for (int i =0;i<mCurrentCount;i++){
            canvas.drawArc(oval,i*(itemSize+mSpliteSize),itemSize,false,mPaint);
        }
    }


    private void up(){
        if (mCurrentCount<mCount){
            mCurrentCount++;
        }
        postInvalidate();
    }

    private void down(){
        if (mCurrentCount>0){
            mCurrentCount--;
            postInvalidate();
        }

    }

    private int xDown, xUp;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp>xDown){//下滑
                    down();
                }else{
                    up();
                }

                break;
        }
        return true;

    }
}
