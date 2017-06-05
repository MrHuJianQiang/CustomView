package com.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.customview.R;

/**
 * Created by jianqiang.hu on 2017/6/5.
 */

public class CustomImageView extends View {

    /**
     * image 文字描述
     */
    private String imageTitle;

    /**
     * image 文字大小
     */
    private int imageTitleColor;

    /**
     * image 文字大小
     */
    private int imageTitleSize;

    /**
     * image
     */
    private Bitmap mImage;

    /**
     * image 拉伸
     */
    private int imageScale;


    private Paint mPaint;
    private Rect rect;
    private Rect mBound;

    int mWidth=0; //view的宽
    int mHeight=0;//view的高


    private static final int IMAGE_SCALE_FITXY=0;
    private static final int IMAGE_SCALE_CENTER=1;

    public CustomImageView(Context context) {
        super(context);
    }
    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 获取自定义属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomImageView,defStyleAttr ,0);
        imageTitle=typedArray.getString(R.styleable.CustomImageView_imageTitle);
        //默认值将sp转为px
        imageTitleSize=typedArray.getDimensionPixelSize(R.styleable.CustomImageView_imageTitleSize, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,context.getResources().getDisplayMetrics()));
        imageTitleColor=typedArray.getColor(R.styleable.CustomImageView_imageTitleColor, Color.BLACK);
        mImage= BitmapFactory.decodeResource(getResources(),typedArray.getResourceId(R.styleable.CustomImageView_image,0));
        imageScale=typedArray.getInt(R.styleable.CustomImageView_imageScaleType,0);

        rect=new Rect();
        mBound=new Rect();
        mPaint=new Paint();
        mPaint.setTextSize(imageTitleSize);
        mPaint.getTextBounds(imageTitle,0,imageTitle.length(),mBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 边框
         */
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);


        rect.left=getPaddingLeft();
        rect.top=getPaddingTop();
        rect.right=mWidth-getPaddingRight();
        rect.bottom=mHeight-getPaddingBottom();

        mPaint.setColor(imageTitleColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(imageTitleSize);
        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
         */
        Paint.FontMetricsInt fm =mPaint.getFontMetricsInt();
        if (mPaint.measureText(imageTitle)>mWidth){
            TextPaint paint=new TextPaint(mPaint);
            String msg= TextUtils.ellipsize(imageTitle,paint,mWidth-getPaddingLeft()-getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),mHeight-getPaddingBottom(),mPaint);
        }else{
            canvas.drawText(imageTitle,mWidth/2-mPaint.measureText(imageTitle)/2,mHeight-fm.descent-getPaddingBottom(),mPaint);
        }

        //取消使用掉的快
        if (fm!=null){
            rect.bottom-=(fm.bottom-fm.top);
        }else{
            rect.bottom-=mBound.height();
        }

        if (imageScale==IMAGE_SCALE_FITXY){
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }else{
            //计算居中的矩形范围
            rect.left=mWidth/2-mImage.getWidth()/2;
            rect.top=(mHeight-(fm.bottom-fm.top))/2-mImage.getHeight()/2;
            rect.right=mWidth/2+mImage.getWidth()/2;
            rect.bottom=(mHeight-(fm.bottom-fm.top))/2+mImage.getHeight()/2;
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }


    }


    /**
     * 测量宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode=MeasureSpec.getMode(widthMeasureSpec);
        int spedSize=MeasureSpec.getSize(widthMeasureSpec);


        if (specMode==MeasureSpec.EXACTLY){//match_parent或者固定宽高
            mWidth=spedSize;
        }else{
            //由图片决定宽
            int desireByImg=getPaddingLeft()+mImage.getWidth()+getPaddingRight();
            //由文字决定宽
            int desireByTitle=getPaddingLeft()+ (int)mPaint.measureText(imageTitle)+getPaddingRight();
            if (specMode==MeasureSpec.AT_MOST){//wap_content
               int desire=Math.max(desireByImg,desireByTitle);
                mWidth=Math.min(spedSize,desire);
            }
        }



        specMode=MeasureSpec.getMode(heightMeasureSpec);
        spedSize=MeasureSpec.getSize(heightMeasureSpec);

        if (specMode==MeasureSpec.EXACTLY){
            mHeight=spedSize;
        }else{
            Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
            int textHeight= (int) Math.abs((fontMetrics.bottom-fontMetrics.top));//获取文字高度

            int desire=getPaddingTop()+mImage.getHeight()+textHeight+getPaddingBottom();//计算高度
            if (specMode==MeasureSpec.AT_MOST){
                mHeight=Math.min(spedSize,desire);
            }
        }
        setMeasuredDimension(mWidth,mHeight);
    }
}
