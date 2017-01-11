package com.it.juyou.view;

/**
 * Created by lishuliang on 2016/12/13.
 */

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class DragMessageView extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private ViewGroup backView;
    private ViewGroup frontView;
    private int mWidth;
    private int mHeight;
    private int mRange;

    private static enum Status {
        CLOSE,
        OPEN,
        DRAGING
    }

    public interface OnDragChangeListener {
        void onOpen();

        void onClose();

        void onDraging();
    }

    private OnDragChangeListener mOnDragChangeListener;

    public OnDragChangeListener getmOnDragChangeListener() {
        return mOnDragChangeListener;
    }

    public void setmOnDragChangeListener(OnDragChangeListener mOnDragChangeListener) {
        this.mOnDragChangeListener = mOnDragChangeListener;
    }

    private Status status = Status.CLOSE;

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        //第三步：处理监听
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            if (changedView == frontView) {
                backView.offsetLeftAndRight(dx);
            } else if (changedView == backView) {
                frontView.offsetLeftAndRight(dx);
            }

            dispatchDragEvents();

            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            if (xvel < 0) {
                open();
            } else if (xvel == 0 && frontView.getLeft() < -(mRange / 2)) {
                open();
            } else {
                close();
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            if (child == frontView) {
                left = fixLeft(left);
            }

            if (child == backView) {
                left = computeLeft(left);
            }

            return left;
        }
    };

    private void dispatchDragEvents() {

        Status lastStatus = status;
        status = updateStatus();

        if (lastStatus != status) {
            if (mOnDragChangeListener != null) {
                if (status == Status.CLOSE) {
                    mOnDragChangeListener.onClose();
                } else if (status == Status.OPEN) {
                    mOnDragChangeListener.onOpen();
                } else {
                    mOnDragChangeListener.onDraging();
                }
            }
        }
    }

    private Status updateStatus() {
        int left = frontView.getLeft();
        if (left == 0) {
            return Status.CLOSE;
        } else if (left == -mRange) {
            return Status.OPEN;
        } else {
            return Status.DRAGING;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void close() {
        close(true);
    }

    private void close(boolean isSmooth) {
        if (isSmooth) {
            if (mViewDragHelper.smoothSlideViewTo(frontView, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutViews(false);
        }
    }

    private void open() {
        open(true);
    }

    private void open(boolean isSmooth) {
        if (isSmooth) {
            if (mViewDragHelper.smoothSlideViewTo(frontView, -mRange, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutViews(true);
        }
    }

    private int computeLeft(int left) {
        if (left < mWidth - mRange) {
            return mWidth - mRange;
        } else if (left > mWidth) {
            return mWidth;
        } else {
            return left;
        }
    }

    private int fixLeft(int left) {
        if (left > 0) {
            return 0;
        } else if (left < -mRange) {
            return -mRange;
        } else {
            return left;
        }
    }

    public DragMessageView(Context context) {
        this(context, null);
    }


    public DragMessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //第一步：初始化ViewDragHelper的实例对象
        mViewDragHelper = ViewDragHelper.create(this, mCallback);
    }

    //第二步：托管触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            mViewDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() < 2) {
            throw new IllegalArgumentException("This ViewGroup must have two childern at least!");
        }

        if (!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException("The children must be instanceof ViewGroup!");
        }

        backView = (ViewGroup) getChildAt(0);
        frontView = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mRange = backView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        layoutViews(false);
    }

    /**
     * 初始化控件的位置
     *
     * @param isOpen
     */
    private void layoutViews(boolean isOpen) {
        Rect backRect = layoutBackView(isOpen);
        backView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);

        Rect frontRect = layoutFrontView(isOpen);
        frontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
    }

    private Rect layoutBackView(boolean isOpen) {
        int newLeft = mWidth;
        if (isOpen) {
            newLeft = mWidth - mRange;
        }
        return new Rect(newLeft, 0, newLeft + mWidth, mHeight);
    }

    private Rect layoutFrontView(boolean isOpen) {
        int newLeft = 0;
        if (isOpen) {
            newLeft = -mRange;
        }
        return new Rect(newLeft, 0, newLeft + mWidth, mHeight);
    }
}