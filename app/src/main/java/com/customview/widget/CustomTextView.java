package com.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.customview.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Created by jianqiang.hu on 2017/6/4.
 */

public class CustomTextView extends View{

    /**
     *文本内容
     */
    private  String mText;
    /**
     * 文本颜色
     */
    private int mTextColor;

    /**
     * 文本大小
     */
    private int mTextSize;

    /**
     * 背景
     */
    private int background;

    private Paint mPaint;
    private Rect mBound;

    private Paint.FontMetricsInt fm;
    public CustomTextView(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public CustomTextView(Context context){
        this(context,null);
    }


    /**
     * 获取自定义属性
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomTextView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView,defStyle,0);
        int count =typedArray.getIndexCount();
        //设置默认值，因为count获取的是自定义属性的个数，如果不设置自定义属性，则不会走for循环，也就显示出来了
        mTextColor=Color.BLACK;
        mTextSize= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,context.getResources().getDisplayMetrics());
        mText="您还没有设置内容";
        background=Color.GRAY;
        for (int i=0;i<count;i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.CustomTitleView_titleText:
                    mText=typedArray.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTextSize=typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,context.getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    mTextColor=typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleBackground:
                    background=typedArray.getColor(attr,Color.YELLOW);
                    break;
            }
        }

        typedArray.recycle();

        mPaint=new Paint();
        mPaint.setTextSize(mTextSize);

        mBound=new Rect();
      //  这个方法需要提供一个参数 Rect 矩形区域，这个方法将文字的区域传递到 Rect
        mPaint.getTextBounds(mText,0,mText.length(),mBound);



        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mText=random();
                postInvalidate();
            }
        });




    }

    private String random(){
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size()<4){
            set.add(random.nextInt(10));
        }

        StringBuffer str=new StringBuffer();
      for (Integer i:set){
          str.append(""+i);
      }
        return str.toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(background);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);


        mPaint.setColor(mTextColor);


        //bound 为mText所在的矩形，所以mBound.width为mtext的宽度mBound.height为文字的高度

         fm= mPaint.getFontMetricsInt();
        /**
         * 参数含义
         * 第一个为文字，第二个为距离View左边的位置
         * 第三个为距离view top的位置,而top初始指的是文字底部距离view的位置mBound.width() 宽度不是很准确
         *getHeight()/2+mBound.height()/2 因为mBound.height()不是很准确，所以使用fm
         int startY = ;
         *
         * 第四个为笔
         */
        canvas.drawText(mText,getWidth()/2-/*mBound.width()/2*/mPaint.measureText(mText)/2,getHeight() / 2 - fm.descent + (fm.bottom - fm.ascent) / 2,mPaint);
    }


    /***
     * 计算view宽高 如果为wrap_content
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      //  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;
        /**
         * MeasureSpec的specMode,一共三种类型：
         EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
         AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
         UNSPECIFIED：表示子布局想要多大就多大，很少使用
         */
        if (widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText,0,mText.length(),mBound);
            //mBound.width()获取到的宽度偏小
            //float textWidth=mBound.width();
            float textWidth=mPaint.measureText(mText);
            int dersired= (int) (getPaddingLeft()+textWidth+getPaddingRight());
            width=dersired;
        }


        if (heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText,0,mText.length(),mBound);
            //如果是因为获取到的mBound.height()不够，详情百度
          //  float textHeight=mBound.height();
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float textHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));
            int desired= (int) (getPaddingTop()+textHeight+getPaddingBottom());
            height=desired;

        }
        setMeasuredDimension(width,height);
    }

    public int getTitleBackground(){
        return background;
    }

    public String getText(){
        return mText;
    }

    public int getTextColor(){
        return mTextColor;
    }

    public void setText(String text){
        this.mText=text;
    }
    public void setTitleBackground(int background){
        this.background=background;
    }
    public void setTextColor(int tielecolor){
        this.mTextColor=tielecolor;
    }

    public void setTextSize(int textSize){
        this.mTextSize=textSize;
    }


}
