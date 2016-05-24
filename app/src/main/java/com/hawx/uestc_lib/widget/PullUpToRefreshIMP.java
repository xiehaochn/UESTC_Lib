package com.hawx.uestc_lib.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.hawx.uestc_lib.R;

/**
 * Created by Administrator on 2016/5/24.
 */
public class PullUpToRefreshIMP extends FrameLayout {
    private Context context;
    private Paint ringPaint;
    private Paint backgroundPaint;
    private int ringColor;
    private float strokeWidth;
    private float radio;
    private int paddingBottom;
    private static final int defaultPadding=15;
    private static final int defaultDoInterceptDis=15;
    private static final int defaultGap=300;
    private int currentAngle;
    private int currentSweep;
    private boolean shouldIntercept=false;
    private float startY;
    private float currentY;
    private boolean intercepted=false;
    private boolean refreshing=false;
    private startRefreshingListener listener;
    private boolean flag=false;

    public PullUpToRefreshIMP(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public PullUpToRefreshIMP(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    public PullUpToRefreshIMP(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }
    private void init() {
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ringColor=context.getResources().getColor(R.color.colorPrimaryDark,null);
        }else{
            ringColor=context.getResources().getColor(R.color.colorPrimaryDark);
        }
        strokeWidth=context.getResources().getDimensionPixelOffset(R.dimen.widget_pulluptorefresh_strokewidth);
        radio=context.getResources().getDimensionPixelOffset(R.dimen.widget_pulluptorefresh_radio);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int height=getHeight();
        int width=getWidth();
        RectF rectF=new RectF(width/2-radio-strokeWidth/2,height-paddingBottom+defaultPadding,width/2+radio+strokeWidth/2,height+radio*2+strokeWidth-paddingBottom+defaultPadding);
        canvas.drawCircle(width/2,height-paddingBottom+radio+strokeWidth/2+defaultPadding,radio+strokeWidth*2,backgroundPaint);
        canvas.drawArc(rectF,currentAngle,currentSweep,false,ringPaint);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!shouldIntercept||refreshing){
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                startY=ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                currentY=ev.getY();
                if(startY-currentY>=defaultDoInterceptDis){
                    intercepted=true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                break;
            }
            default:

                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(intercepted&&!refreshing){
            getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:{
                    startY=event.getY();
                    break;
                }
                case MotionEvent.ACTION_MOVE:{
                    currentY=event.getY();
                    float change=startY-currentY;
                    if(change>0) {
                        if (change <= defaultGap) {
                            paddingBottom = (int) change;
                        }
                        if (paddingBottom >= radio * 2 + strokeWidth) {
                            currentSweep = (int) Math.min(340 * (change - radio * 2 - strokeWidth) / (defaultGap - radio * 2 - strokeWidth), 340);
                            if (currentSweep >= 340) {
                                currentAngle = (int) (change - paddingBottom);
                            }
                        }
                    }
                    postInvalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:{
                    if(!refreshing) {
                        currentY = event.getY();
                        if (startY - currentY >= defaultGap) {
                            refreshing = true;
                            if (listener != null) {
                                listener.startRefreshing();
                            }
                            new Thread() {
                                public void run() {
                                    while (refreshing) {
                                        try {
                                            Thread.sleep(6);
                                            if (currentSweep >= 240) {
                                                flag = false;
                                            } else if (currentSweep <= 20) {
                                                flag = true;
                                            }
                                            if (currentAngle >= 359) {
                                                currentAngle = 0;
                                            } else {
                                                if (flag) {
                                                    currentAngle++;
                                                } else {
                                                    currentAngle += 2;
                                                }
                                            }

                                            if (flag) {
                                                currentSweep++;
                                            } else {
                                                currentSweep--;
                                            }
                                            postInvalidate();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }.start();

                        } else {
                            int finalPadding = (int) (startY - currentY);
                            ObjectAnimator paddingAnim = ObjectAnimator.ofInt(this, "paddingBottom", finalPadding, 0);
                            paddingAnim.setDuration(500);
                            paddingAnim.setInterpolator(new AccelerateInterpolator());
                            ObjectAnimator sweepAnim = ObjectAnimator.ofInt(this, "currentSweep", currentSweep, 0);
                            sweepAnim.setDuration(500);
                            sweepAnim.setInterpolator(new AccelerateInterpolator());
                            AnimatorSet reset = new AnimatorSet();
                            reset.play(paddingAnim).with(sweepAnim);
                            reset.start();
                        }
                    }
                    intercepted=false;
                    break;
                }
                default:
                    break;
            }
            return true;
        }else{
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

    public void setShouldIntercept(boolean shouldIntercept) {
        this.shouldIntercept = shouldIntercept;
    }

    public void setStartRefreshingListener(startRefreshingListener listener) {
        this.listener = listener;
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
            reset.start();

        }
    }
}
