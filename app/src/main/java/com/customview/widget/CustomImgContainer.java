package com.customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by jianqiang.hu on 2017/6/8.
 */

public class CustomImgContainer extends ViewGroup {
    public CustomImgContainer(Context context) {
        super(context);
    }

    public CustomImgContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImgContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount=getChildCount();
        int cWidth=0;
        int cHeight=0;
        MarginLayoutParams cParams;
        for (int i=0;i<cCount;i++){
            View childView=getChildAt(i);
            cWidth=childView.getMeasuredWidth();
            cHeight=childView.getMeasuredHeight();
            cParams= (MarginLayoutParams) childView.getLayoutParams();
            int cl=0,ct=0,cr=0,cb=0;

            switch (i){
                case 0:
                    cl=cParams.leftMargin;
                    ct=cParams.topMargin;
                    break;
                case 1:
                    cl=getWidth()-cWidth-cParams.rightMargin-cParams.leftMargin;
                    ct=cParams.topMargin;
                    break;
                case 2:
                    cl=cParams.leftMargin;
                    ct=getHeight()-cHeight-cParams.bottomMargin;
                    break;
                case 3:
                    cl=getWidth()-cWidth-cParams.rightMargin-cParams.leftMargin;
                    ct=getHeight()-cHeight-cParams.bottomMargin;
                    break;
            }
            cr=cl+cWidth;
            cb=ct+cHeight;
            childView.layout(cl,ct,cr,cb);

        }


    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width =0;
        int height=0;


        int cCount = getChildCount();

        /**
         * 子view的宽、高
         */
        int cWidth=0;
        int cHeight=0;


        MarginLayoutParams cParams=null;
        //左边高度
        int lHeight=0;
        //右边高度
        int rHeight=0;

        //上边宽度
        int tWidth=0;
        //下边宽度
        int bWidth=0;
        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i=0;i<cCount;i++){
            View childView=getChildAt(i);
            cWidth=childView.getMeasuredWidth();
            cHeight=childView.getMeasuredHeight();
            cParams= (MarginLayoutParams) childView.getLayoutParams();


            if (i==0|| i==1){
                tWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
            }
            if (i==2||i==3){
                bWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
            }

            if (i==0||i==2){
                lHeight+=cHeight+cParams.topMargin+cParams.bottomMargin;
            }
            if (i==1||i==3){
                rHeight+=cHeight+cParams.topMargin+cParams.bottomMargin;
            }

            width=Math.max(tWidth,bWidth);
            height=Math.max(lHeight,rHeight);

            /**
             * 如果是wrap_content设置为我们计算的值
             * 否则：直接设置为父容器计算的值
             */
            setMeasuredDimension(widthMode==MeasureSpec.EXACTLY ? widthSize:width,heightMode==MeasureSpec.EXACTLY?heightSize:height);
        }

    }
}
