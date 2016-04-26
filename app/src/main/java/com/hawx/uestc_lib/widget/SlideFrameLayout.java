package com.hawx.uestc_lib.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.hawx.uestc_lib.base.BaseActivity;
import com.hawx.uestc_lib.utils.Utils;

/**
 * 滑动布局
 * @author Hawx
 * @version 1.0
 */
public class SlideFrameLayout extends LinearLayout {
    private static int DEFAULT_INTERCEPT_DP =8;
    private static int DEFAULT_FINISH_DP=200;
    private int startX,currentX;
    private int interceptDp= DEFAULT_INTERCEPT_DP;
    private int finishDp= DEFAULT_FINISH_DP;
    private boolean intercepted=false;
    private boolean consum;
    private Context context;
    private BaseActivity baseActivity;
    public SlideFrameLayout(Context context, BaseActivity baseActivity) {
        super(context);
        this.context=context;
        this.baseActivity=baseActivity;
    }

    public SlideFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("SlideFrameLayout","onInterceptTouchEvent");
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                Log.d("SlideFrameLayout","onInterceptTouchEvent:ACTION_DOWN");
                startX= (int) ev.getX();
                intercepted=false;
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                Log.d("SlideFrameLayout", "onInterceptTouchEvent:ACTION_MOVE");
                currentX= (int) ev.getX();
                if (startX - currentX > Utils.dpTopx(context, interceptDp)) {
                    Log.d("SlideFrameLayout", "Intercepted");
                    intercepted = true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                Log.d("SlideFrameLayout","onInterceptTouchEvent:ACTION_UP");
                intercepted=false;
                break;
            }
            default:
                intercepted=false;
                break;
        }
        Log.d("SlideFrameLayout","onInterceptTouchEvent Result:"+intercepted);
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("SlideFrameLayout","onTouchEvent:"+event.getAction());
        if(intercepted) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:{
                    currentX= (int) event.getX();
                    if(currentX-startX<0) {
                        this.getChildAt(0).setTranslationX(currentX - startX);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    Log.d("SlideFrameLayout", "on touchEvent:finalX:" + currentX);
                    currentX=(int) event.getX();
                    intercepted = false;
                    if (startX - currentX >Utils.dpTopx(context, finishDp) ) {
                        Log.d("SlideFrameLayout", "slideFinish Start");
                        ObjectAnimator finishAnim = new ObjectAnimator().ofFloat(this.getChildAt(0), "translationX",currentX-startX,- Utils.getWindowWidth(context));
                        finishAnim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                baseActivity.finish();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        finishAnim.setInterpolator(new AccelerateInterpolator());
                        finishAnim.setDuration(1000);
                        finishAnim.start();
                    }else{
                        if(currentX-startX<0) {
                            ObjectAnimator reset = new ObjectAnimator().ofFloat(this.getChildAt(0), "TranslationX", currentX - startX, 0);
                            reset.setInterpolator(new AccelerateInterpolator());
                            reset.setDuration(500);
                            reset.start();
                        }
                    }
                    break;
                }
            }
            consum=true;
        }else{
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:{
                    consum=true;
                    break;
                }
                case MotionEvent.ACTION_MOVE:{
                    currentX= (int) event.getX();
                    if (startX - currentX > Utils.dpTopx(context, interceptDp)) {
                        intercepted = true;
                    }
                    consum=true;
                    break;
                }
                default:
                    consum=true;
                    break;
            }

        }
        return consum;
    }

}
