package com.customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by jianqiang.hu on 2017/6/9.
 */

public class VerticalLinearLayout extends ViewGroup{
    /**
     * 屏幕的高度
     */
    private int mScreenHeight;

    /**
     * 手指抬起时的getScrollY
     */
    private int mScrollStart;

    /**
     * 手指抬起时的getScrollY
     */
    private int mScrollEnd;

    /**
     * 记录移动时的Y
     */
    private int mLastY;

    /**
     * 滚动的辅助类
     */
    private Scroller mScroller;

    /**
     * 是否正在滚动
     */
    private boolean isScrolling;
    /**
     * 加速度检测
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 记录当前页
     */
    private int currentPage=0;



    public VerticalLinearLayout(Context context) {
        super(context);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * 获得屏幕的高度
         */
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetric=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetric);
        mScreenHeight=outMetric.heightPixels;

        //初始化
        mScroller=new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count =getChildCount();
        for (int i=0;i<count;++i){
            View childView=getChildAt(count);
            measureChild(childView,widthMeasureSpec,mScreenHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
        lp.height=childCount* mScreenHeight;
        setLayoutParams(lp);
        for (int i =0;i<childCount;i++){
            View child= getChildAt(i);
            if (child.getVisibility()!=View.GONE){
                child.layout(l,i*mScreenHeight,r,(i+1)*mScreenHeight);// 调用每个自布局的layout
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果当前正在滚动，调用父类的onTouchEvent
        /**
         * 原因是当抬起手指是，view还在滑动，则不响应onTouchEvent
         */
        if (isScrolling){
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        int y = (int) event.getY();
        obtainVelocity(event);//初始化加速度
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mScrollStart=getScrollY();
                mLastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dy=mLastY-y;
                //边界值检查
                int scrollY=getScrollY();
                //已经到达顶端，下拉多少，就往上滚动多少
                if (dy<0 && scrollY+dy<0){
                    dy=-scrollY;
                }
                //已经达到底部，上拉多少，就往下滚动多少
                if (dy>0 && scrollY+dy>getHeight()-mScreenHeight){
                    dy=getHeight()-mScreenHeight-scrollY;
                }
                scrollBy(0,dy);//滑动
                mLastY=y;
                break;
            case MotionEvent.ACTION_UP:
                mScrollEnd=getScrollY();
                int dScrollY=mScrollEnd-mScrollStart;
                if (wantScrollToNext()){//往上滑动
                    if (shouldScrollToNext()){
                        mScroller.startScroll(0,getScrollY(),0,mScreenHeight-dScrollY);
                    }else{
                        mScroller.startScroll(0,getScrollY(),0,-dScrollY);
                    }
                }

                if (wantScrollToNext()){//往下滑动
                    if (shouldScrollToPre()){
                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }
                isScrolling=true;
                postInvalidate();
                recycleVelocity();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

    }

    /**
     * 初始化加速度检测器
     * @param event
     */
    private void obtainVelocity(MotionEvent event){
        if (mVelocityTracker==null){
            mVelocityTracker=VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 根据用户滑动，判断永不的衣服是否是滚动到下一页
     * @return
     */
    private boolean wantScrollToNext(){
        return mScrollEnd>mScrollStart;
    }

    /**
     * 根据滚动距离判断是否能够滚动到下一页
     * @return
     */
    private boolean shouldScrollToNext(){
        return mScrollEnd-mScrollStart>mScreenHeight/2 || Math.abs(getVelocity())>600;
    }

    /**
     * 根据滚动距离判断是否能够滚动到上一页
     * @return
     */
    private boolean shouldScrollToPre(){
        return mScrollStart-mScrollEnd>mScreenHeight/2 || Math.abs(getVelocity())>600;
    }


    /**
     * 获取y方向的加速度
     * @return
     */
    private int getVelocity(){
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getYVelocity();
    }

    /**
     * 释放资源
     */
    private void recycleVelocity(){
        if (mVelocityTracker!=null){
            mVelocityTracker.recycle();
            mVelocityTracker=null;
        }
    }
}
