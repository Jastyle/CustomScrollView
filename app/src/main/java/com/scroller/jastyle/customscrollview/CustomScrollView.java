package com.scroller.jastyle.customscrollview;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by jastyle on 2017/5/11.
 */

public class CustomScrollView extends ViewGroup{
    private static final String TAG = "CustomScrollView";
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private int mLastXIntercept;
    private int mLastYIntercept;
    private int mLastXMove;
    private int mLastYMove;
    public CustomScrollView(Context context) {
        this(context, null);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }
    @TargetApi(21)
    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mLastYIntercept = 0;
        mLastYIntercept = 0;
        mLastXMove = 0;
        mLastYMove = 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i<getChildCount(); i++) {
            View mChildView = getChildAt(i);
            mChildView.layout(getMeasuredWidth()*i, 0, getMeasuredWidth()*(i+1), mChildView.getMeasuredHeight());
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View mChildView = getChildAt(i);
            measureChild(mChildView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,"dispathchTouchEvent()");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        Log.d(TAG,"onInterceptTouchEvent()");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX)>Math.abs(deltaY)) {//父容器根据需要拦截
                    intercepted = true;
                }else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        mLastXMove = x;
        mLastYMove = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,"onTouchEvent()");
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int deltaX = x - mLastXMove;
                int deltaY = y - mLastYMove;
                scrollBy(-deltaX,0);
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                int childIndex = scrollX/getWidth();
                mVelocityTracker.computeCurrentVelocity(1000);
                int mVelocityX = (int) mVelocityTracker.getXVelocity();
                if (Math.abs(mVelocityX) > 50) {//水平滑动速度
                    childIndex = mVelocityX > 0?childIndex-1:childIndex+1;
                }else {
                    childIndex = (scrollX+getWidth()/2)/getWidth();
                }
                childIndex = Math.max(0, Math.min(childIndex, getChildCount()-1));
                int dx = childIndex*getWidth() - scrollX;
                smoothScrollBy(dx, 0);
                mVelocityTracker.clear();
                break;
        }
        return super.onTouchEvent(event);
    }
    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0,dx, 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }
}
