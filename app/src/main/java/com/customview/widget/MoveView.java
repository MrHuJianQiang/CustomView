package com.customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by jianqiang.hu on 2017/6/2.
 *
 * 根据手指移动view
 */

public class MoveView extends View {
    private int lastX=0;
    private int lastY=0;

    public MoveView(Context context) {
        super(context);
    }

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x= (int) event.getX();//手指在view上的x位置
        int y= (int) event.getY();//手指在view上的y的位置
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX=x;
                lastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offSetX=x-lastX;
                int offSetY=y-lastY;
                //使用layout来改变位置
                //layout(getLeft()+offSetX,getTop()+offSetY,getRight()+offSetX,getBottom()+offSetY);

                //和latout相似
               /* offsetLeftAndRight(offSetX);
                offsetTopAndBottom(offSetY);*/


                //通过LayoutParams来改变margin
                //如果父控件是Lineatlayout 可以使用LineatLayout.LayoutParams
                //如果父控件是Relativelayout 可以使用Relativelayout.LayoutParams
                //如果是用ViewGroup，由于ViewGroup.LayoutParams没有leftMargin，所以使用ViewGroup.MarginLayoutParams
                ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) getLayoutParams();
                params.leftMargin=getLeft()+offSetX;
                params.topMargin=getTop()+offSetY;
                setLayoutParams(params);


                //还可以使用动画方式来移动

                //
                //scollTo与scollBy  scollTo(x,y)表示移动到一个具体的坐标点，而scollBy(dx,dy)则表示移动的增量为dx、dy
                break;
        }

        return true;
    }
}
