package com.customview.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by jianqiang.hu on 2017/6/9.
 */

public class ViewDragHelpLayout extends LinearLayout{
    private ViewDragHelper mDragger;


    private View mDraggerView;
    private View mAutoBackView;
    private View mEdgeTrackerView;


    private Point mAutoBackOriginPos=new Point();

    public ViewDragHelpLayout(Context context) {
        super(context);
    }

    public ViewDragHelpLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //第二个参数为sensitivity，查看源码发现 第二个参数越大，mTouchSlop越小
        mDragger=ViewDragHelper.create(this, 10.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                return child!=mEdgeTrackerView;
            }

            /**
             * 横向滑动距离
             * @param child
             * @param left
             * @param dx
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int leftBound=getPaddingLeft();
                int rightBound=getWidth()-child.getWidth()-leftBound;
               // Math.max(left,leftBound)  限制左边距离最小为paddleft，即当left小于leftBound的时候，取leftBound
                //Math.min(Math.max(left,leftBound),rightBound) 当Math.max(left,leftBound)的距离大于rightBound，取rightBound，这个是最大象限
                int newLeft=Math.min(Math.max(left,leftBound),rightBound);//限制拖拽的左右边界
                return newLeft;
            }

            /**
             * 纵向滑动距离
             * @param child
             * @param top
             * @param dy
             * @return
             */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBound=getPaddingTop();
                int bottomBound=getHeight()-child.getHeight()-getPaddingTop();
                int newTop=Math.min(Math.max(top,topBound),bottomBound);
                return newTop;
            }

            /**
             * 手指释放时调用
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {

                if (releasedChild==mAutoBackView){//手指释放时，返回原位置
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x,mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                Log.e("pointerId","pointerId-->"+pointerId);
                mDragger.captureChildView(mEdgeTrackerView,pointerId);
            }

            /**
             * 如果子view为可点击控件，则必须重写这2个方法，因为如果是可点击空间，会先执行onInterceptTouchEvent，判断是够可以捕捉，
             * 而在会执行onInterceptTouchEvent调用了shouldInterceptTouchEvent，在这个方法中判断了getViewHorizontalDragRange和getViewVerticalDragRange
             * 返回值是否大于0，所以需要重写这2个方法，并且返回值大于0，才能捕捉
             *
             * 如果子view为非可点击控件，整个手势都是会直接进去onTouchEvent，在onTouchEvent的DOWN的时候就确定了captureView
             *
             * @param child
             * @return
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return child.getMeasuredHeight();
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

    }

    /**
     *必须重写此方法，否则mAutoBackView 自动返回无效
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragger.continueSettling(true)){
           invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x=mAutoBackView.getLeft();//记录backview的初始x坐标
        mAutoBackOriginPos.y=mAutoBackView.getTop();//记录backview的初始y坐标
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDraggerView=getChildAt(0);//第一个view为可拖拽
        mAutoBackView=getChildAt(1);//第二个view为拖拽返回原位置
        mEdgeTrackerView=getChildAt(2);//第三个活动边界移动
    }

}
