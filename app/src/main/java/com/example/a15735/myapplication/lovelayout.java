package com.example.a15735.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class lovelayout extends RelativeLayout {

    private Drawable mRed, mYellow, mBlue;
    private Drawable[] mDrawables;
    private Interpolator[] mInterpolators;
    private int mDrawableHeight, mDrawableWidth;
    private int mWidth, mHeight;
    private LayoutParams params;
    private Random mRandom = new Random();

    public lovelayout(Context context) {
        this(context, null);
    }

    public lovelayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public lovelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        initDrawable();
        initInterpolator(); // 初始化
        params = new LayoutParams(mDrawableWidth, mDrawableHeight); // 父容器水平居中
        params.addRule(CENTER_HORIZONTAL, TRUE); // 父容器的底部
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
    }
    /*** 初始化几种插补器 */
    private void initInterpolator() {
        mInterpolators = new Interpolator[4];
        mInterpolators[0] = new LinearInterpolator();// 线性
        mInterpolators[1] = new AccelerateDecelerateInterpolator();// 先加速后减速
        mInterpolators[2] = new AccelerateInterpolator();// 加速
        mInterpolators[3] = new DecelerateInterpolator();// 减速
    }
    private void initDrawable() {
        mRed = getResources().getDrawable(R.drawable.heart1);
        mYellow = getResources().getDrawable(R.drawable.heart2);
        mBlue = getResources().getDrawable(R.drawable.heart3);
        mDrawables = new Drawable[3];
        mDrawables[0] = mRed;
        mDrawables[1] = mYellow;
        mDrawables[2] = mBlue;
        // 得到图片的实际宽高
        mDrawableWidth = mRed.getIntrinsicWidth();
        mDrawableHeight = mRed.getIntrinsicHeight();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void addlove() {
        final ImageView loveIv = new ImageView(getContext());
        loveIv.setImageDrawable(mDrawables[mRandom.nextInt(mDrawables.length)]);
        loveIv.setLayoutParams(params);
        addView(loveIv); // 最终的属性动画集合
        AnimatorSet finalSet = getAnimator(loveIv);
        finalSet.start();


    }

    //改变缩放和透明度
    private AnimatorSet getAnimator(final ImageView loveIv) {
// 1.alpha动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(loveIv, "alpha", 0.3f, 1f); // 2.缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(loveIv, "scaleX", 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(loveIv, "scaleY", 0.2f, 1f); // 刚刚进入动画集合
        AnimatorSet enter = new AnimatorSet();
        AnimatorSet other=new AnimatorSet();
        enter.setDuration(500);
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(loveIv);
        other.playSequentially(enter,getBezierAnimator(loveIv));
        other.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView( loveIv);
            }
        });
        other.start();
        return enter;


    }

    public Animator getBezierAnimator(final ImageView loveIv) {
// 曲线的两个顶点
        PointF pointF2 = getPonitF(2);
        PointF pointF1 = getPonitF(1); // 起点位置
        PointF pointF0 = new PointF((mWidth - mDrawableWidth) / 2, mHeight - mDrawableHeight);
        // 结束的位置
        PointF pointF3 = new PointF(mRandom.nextInt(mWidth), 0); // 估值器Evaluator,来控制view的行驶路径（不断的修改point.x,point.y）
        LoveTypeEvaluator evaluator = new LoveTypeEvaluator(pointF1, pointF2); // 属性动画不仅仅改变View的属性，还可以改变自定义的属性
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, pointF0, pointF3);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) { // 不断改变ImageView的x,y的值
                PointF pointF = (PointF) animation.getAnimatedValue();
                loveIv.setX(pointF.x);
                loveIv.setY(pointF.y);
                loveIv.setAlpha(1 - animation.getAnimatedFraction() + 0.1f);// 得到百分比
            }
        });
        animator.setTarget(loveIv);
        animator.setDuration(3000);
        return animator;


    }
    private  PointF getPonitF(int index){
        return  new PointF(mRandom.nextInt(mWidth),mRandom.nextInt(mHeight/2)+(index-1)*(mHeight/2));
    }
}
