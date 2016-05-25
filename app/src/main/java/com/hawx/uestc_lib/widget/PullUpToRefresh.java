package com.hawx.uestc_lib.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.RectF;

import android.util.AttributeSet;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.hawx.uestc_lib.R;


/**
 * PullUpToRefresh
 * @author Hawx
 * @version 1.0
 * @deprecated Use PullUpToRefreshFrameLayout instead
 */

public class PullUpToRefresh extends View {

    private static final int defaultGap=300;
    private static final int defaultDisIntercept=30;
    private static final int defaultPadding=15;
    private startRefreshingListener startRefreshingListener;
    private int ringColor;
    private float radio;
    private float strokeWidth;
    private boolean refreshing=false;
    private Context context;
    private Paint ringPaint;
    private Paint backgroundPaint;
    private int currentAngle;
    private int currentSweep;
    private boolean flag=false;
    private int paddingBottom;
    private float startY;
    private float currentY;
    public PullUpToRefresh(Context context) {
        super(context);
        this.context=context;
        init();
    }



    public PullUpToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        TypedArray a =context.getTheme().obtainStyledAttributes(attrs,R.styleable.PullUpToRefresh,0,0);
        try {
            ringColor=a.getColor(R.styleable.PullUpToRefresh_ringColor, Color.BLUE);
            radio=a.getDimensionPixelSize(R.styleable.PullUpToRefresh_radio,context.getResources().getDimensionPixelOffset(R.dimen.widget_pulluptorefresh_radio));
            strokeWidth=a.getDimensionPixelSize(R.styleable.PullUpToRefresh_strokeWidth,context.getResources().getDimensionPixelOffset(R.dimen.widget_pulluptorefresh_strokewidth));
        }finally {
            a.recycle();
        }
        init();
    }
    private void init() {
        ringPaint=new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setColor(ringColor);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(strokeWidth);
        backgroundPaint=new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height=getHeight();
        int width=getWidth();
        RectF rectF=new RectF(width/2-radio-strokeWidth/2,height-paddingBottom+defaultPadding,width/2+radio+strokeWidth/2,height+radio*2+strokeWidth-paddingBottom+defaultPadding);
        canvas.drawCircle(width/2,height-paddingBottom+radio+strokeWidth/2+defaultPadding,radio+strokeWidth*2,backgroundPaint);
        canvas.drawArc(rectF,currentAngle,currentSweep,false,ringPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(getVisibility()!=VISIBLE){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                if(!refreshing) {
                    startY = event.getY();
                    return true;
                }else{
                    return false;
                }
            }
            case MotionEvent.ACTION_MOVE:{
                if(!refreshing) {
                    currentY = event.getY();
                    float change=startY-currentY;
                    if(change>0) {
                        if (change >= defaultDisIntercept) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        if (change <= defaultGap) {
                            paddingBottom = (int) change;
                        }
                        if (paddingBottom >= radio * 2 + strokeWidth) {
                            currentSweep = (int) Math.min(340 * (change - radio * 2 - strokeWidth) / (defaultGap - radio * 2 - strokeWidth), 340);
                            if (currentSweep >= 340) {
                                currentAngle = (int) (change - paddingBottom);
                            }
                        }
                        postInvalidate();
                        return true;
                    }else if(change<-5){
                        this.setVisibility(INVISIBLE);
                        return false;
                    }
                }else{
                    return false;
                }
            }
            case MotionEvent.ACTION_UP:{
                if(!refreshing) {
                    currentY = event.getY();
                    if(startY-currentY>=defaultGap){
                        refreshing=true;
                        if(startRefreshingListener!=null){
                            startRefreshingListener.startRefreshing();
                        }
                        new Thread(){
                            public void run() {
                                while (refreshing) {
                                    try {
                                        Thread.sleep(6);
                                        if(currentSweep>=240){
                                            flag=false;
                                        }else if(currentSweep<=20){
                                            flag=true;
                                        }
                                        if (currentAngle >= 359) {
                                            currentAngle = 0;
                                        } else {
                                            if(flag) {
                                                currentAngle++;
                                            }else{
                                                currentAngle+=2;
                                            }
                                        }

                                        if (flag) {
                                            currentSweep++;
                                        }else{
                                            currentSweep--;
                                        }
                                        postInvalidate();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }.start();
                        return true;
                    }else{
                        int finalPadding= (int) (startY-currentY);
                        ObjectAnimator paddingAnim=ObjectAnimator.ofInt(this,"paddingBottom",finalPadding,0);
                        paddingAnim.setDuration(500);
                        paddingAnim.setInterpolator(new AccelerateInterpolator());
                        ObjectAnimator sweepAnim=ObjectAnimator.ofInt(this,"currentSweep",currentSweep,0);
                        sweepAnim.setDuration(500);
                        sweepAnim.setInterpolator(new AccelerateInterpolator());
                        AnimatorSet reset=new AnimatorSet();
                        reset.play(paddingAnim).with(sweepAnim);
                        reset.start();
                        return true;
                    }
                }
            }
            default:
                return super.onTouchEvent(event);

        }
    }

    public void setCurrentSweep(int currentSweep) {
        this.currentSweep = currentSweep;
        postInvalidate();
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
        postInvalidate();
    }
    public interface startRefreshingListener{
        void startRefreshing();
    }

    public void setStartRefreshingListener(PullUpToRefresh.startRefreshingListener startRefreshingListener) {
        this.startRefreshingListener = startRefreshingListener;
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        if(!refreshing) {
            ObjectAnimator paddingAnim = ObjectAnimator.ofInt(this, "paddingBottom", defaultGap, 0);
            paddingAnim.setDuration(500);
            paddingAnim.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator sweepAnim = ObjectAnimator.ofInt(this, "currentSweep", currentSweep, 0);
            sweepAnim.setDuration(500);
            sweepAnim.setInterpolator(new AccelerateInterpolator());
            AnimatorSet reset = new AnimatorSet();
            reset.play(paddingAnim).with(sweepAnim);
            reset.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            reset.start();

        }
    }
}
