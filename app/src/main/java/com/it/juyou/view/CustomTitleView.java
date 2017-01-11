package com.it.juyou.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.it.juyou.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by lishuliang on 2016/12/13.
 */

 public class CustomTitleView extends View implements View.OnClickListener {

    private String  mTitleText;
    private int  mTitleTextColor;
    private float  mTitleTextSize ;

    private Rect mBound;
    private Paint mPaint;
    public CustomTitleView(Context context) {
        this(context,null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray =context.obtainStyledAttributes(attrs, R.styleable.CustomTitleView,defStyleAttr,0);
        mTitleText = typedArray.getString(R.styleable.CustomTitleView_titleText);
        mTitleTextColor = typedArray.getColor(R.styleable.CustomTitleView_titleTextColor, Color.CYAN);
        mTitleTextSize = typedArray.getDimension(R.styleable.CustomTitleView_titleTextSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
        this.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }



        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        mPaint.setColor(mTitleTextColor);
       // canvas.drawText(mTitleText, 0 + getPaddingLeft(), getHeight() / 2 + mBound.height() / 2, mPaint);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2 - mBound.left, getHeight() / 2 + mBound.height() / 2, mPaint);
        //canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        int padWL = getPaddingLeft();
        Log.d("CustomTitleView","getPaddingLeft:" + padWL + ":getWidth:"+getWidth() + " left "+mBound.left + ":mBound width: "+mBound.width());
        Log.d("CustomTitleView", "mBound height :" + mBound.height() +":right:"+mBound.right);
        Log.d("CustomTitleView", "mBound width :" + mBound.width() +":left:"+mBound.left);
        Log.d("CustomTitleView", "mBound Top :" +mBound.top  +":bottom:"+mBound.bottom);
    }


    private String randomText()
    {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }

        return sb.toString();
    }

    @Override
    public void onClick(View v) {
         mTitleText = randomText();
         postInvalidate();


    }
}
