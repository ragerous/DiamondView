package com.ragenzq.diamondview.widgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nzq on 16/9/28.
 */
public class DiamondView extends View {

    public int num = 3;
    public ArrayList<Integer> colorList = new ArrayList<>();
    public int defautColor = 0x99ff0000;
    private int mHeight;
    private int mWidth;
    public int mMaxWidth = 45;
    public int mMaxHeight = 90;
    public int mMinWidth = 25;
    public int mMinHeight = 50;
    private int mRangeWidth = mMaxWidth;
    private int mRangeHeight = mMaxHeight;
    private float mAngle;
    private int mDuration = 500;
    private Boolean isBing = false;
    Paint paint;
    int mCurrent = 0;
    private List<AnimatorSet> mAnimList = new ArrayList<>();

    public DiamondView(Context context) {
        super(context);
        init();
    }

    public DiamondView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public DiamondView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        colorList.add(0x99ff0000);
        colorList.add(0xffffbb00);
        colorList.add(0xff0000ee);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < num; i++) {
            int color;
            if (i < colorList.size()) {
                color = colorList.get(i);
            } else {
                color = defautColor;
            }
            paint.setColor(color);

            int mCenterX = mWidth / (num + 1) * (i + 1);
            int mCenterY = mHeight / 2;
            Path path = new Path();

            if (isBing && mCurrent % (num) == i) {
                path.moveTo(mCenterX, mCenterY - mRangeHeight);
                path.lineTo(mCenterX + mRangeWidth, mCenterY);
                path.lineTo(mCenterX, mCenterY + mRangeHeight);
                path.lineTo(mCenterX - mRangeWidth, mCenterY);
                path.close();
                canvas.rotate(mAngle, mCenterX, mCenterY);
                canvas.drawPath(path, paint);
                canvas.rotate(-mAngle, mCenterX, mCenterY);
            } else {
                path.moveTo(mCenterX, mCenterY - mMinHeight);
                path.lineTo(mCenterX + mMinWidth, mCenterY);
                path.lineTo(mCenterX, mCenterY + mMinHeight);
                path.lineTo(mCenterX - mMinWidth, mCenterY);
                path.close();
                canvas.drawPath(path, paint);
            }

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    void onBlingLarge() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator witdhAnimator = ValueAnimator.ofInt(mMinWidth, mMaxWidth);
        witdhAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRangeWidth = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator heightAnimator = ValueAnimator.ofInt(mMinHeight, mMaxHeight);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRangeHeight = (int) valueAnimator.getAnimatedValue();
            }
        });
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0, 360);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAngle = (float) valueAnimator.getAnimatedValue();
            }
        });

        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(mDuration);
        animatorSet.play(witdhAnimator).with(heightAnimator).with(scaleAnimator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isBing == true) {
                    LogMsg("end");
                    onBlingSmall();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();
        isBing = true;
        mAnimList.add(animatorSet);
    }

    void onBlingSmall() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator witdhBackAnimator = ValueAnimator.ofInt(mMaxWidth, mMinWidth);
        witdhBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRangeWidth = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator heightBackAnimator = ValueAnimator.ofInt(mMaxHeight, mMinHeight);
        heightBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRangeHeight = (int) valueAnimator.getAnimatedValue();
            }
        });

        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(mDuration);
        animatorSet.play(witdhBackAnimator).with(heightBackAnimator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                LogMsg("end");
                if (isBing == true) {
                    mCurrent++;
                    onBlingLarge();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();
        isBing = true;
        mAnimList.add(animatorSet);

    }


    public void onStart() {
        mAnimList.clear();
        for (AnimatorSet animator : mAnimList) {
            animator.cancel();
        }
        onBlingLarge();
    }

    public void onStop() {
        isBing = false;
//        LogMsg("mAnimList" + mAnimList.size());
        for (AnimatorSet animator : mAnimList) {
            animator.cancel();
        }
        invalidate();
    }

    private void LogMsg(String msg) {
        Log.e("DiamondView:", msg);
    }
}
