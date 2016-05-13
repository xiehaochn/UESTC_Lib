package com.hawx.uestc_lib.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;
import com.hawx.uestc_lib.utils.Utils;

/**
 * 滑动布局
 * @author Hawx
 * @version 1.0
 */
public class SlideFrameLayout extends FrameLayout {
    private static int DEFAULT_INTERCEPT_DP =8;
    private static int DEFAULT_FINISH_DP=140;
    private boolean doNotIntercept =false;
    private int startX,currentX;
    private int interceptDp= DEFAULT_INTERCEPT_DP;
    private int finishDp= DEFAULT_FINISH_DP;
    private boolean intercepted=false;
    private boolean consum;
    private Context context;
    private BaseActivity baseActivity;
    private AccelerateInterpolator accelerateInterpolator=new AccelerateInterpolator();
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
        if(!doNotIntercept) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN: {

                    startX = (int) ev.getX();
                    intercepted = false;
                    break;
                }
                case MotionEvent.ACTION_MOVE: {

                    currentX = (int) ev.getX();
                    if (startX - currentX > Utils.dpTopx(context, interceptDp)) {
                        intercepted = true;
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    intercepted = false;
                    break;
                }
                default:
                    intercepted = false;
                    break;
            }
            return intercepted;
        }else{
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            intercepted=true;
        }
        if(intercepted) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:{
                    currentX= (int) event.getX();
                    if(currentX-startX<0) {
                        getChildAt(0).setTranslationX(currentX - startX);
                        float alpha=(1.f-(startX-currentX)/ new Float(Utils.getWindowWidth(context)));
                        getChildAt(0).setAlpha(alpha);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    currentX=(int) event.getX();
                    float currentTranslationX=getChildAt(0).getTranslationX();
                    float currentAlpha=getChildAt(0).getAlpha();
                    intercepted = false;
                    if (startX - currentX >Utils.dpTopx(context, finishDp) ) {
                        AnimatorSet finishSet=new AnimatorSet();
                        ObjectAnimator finishAnimTransX = ObjectAnimator.ofFloat(getChildAt(0), "translationX",currentTranslationX,- Utils.getWindowWidth(context));
                        finishAnimTransX.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                baseActivity.finish();
                                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                                    baseActivity.overridePendingTransition(R.anim.anim_slidefinish_helper, 0);
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        finishAnimTransX.setInterpolator(accelerateInterpolator);
                        finishAnimTransX.setDuration(500);
                        ObjectAnimator finishAnimAlpha=ObjectAnimator.ofFloat(getChildAt(0),"alpha",currentAlpha,0);
                        finishAnimAlpha.setInterpolator(accelerateInterpolator);
                        finishAnimAlpha.setDuration(500);
                        finishSet.play(finishAnimTransX).with(finishAnimAlpha);
                        finishSet.start();
                    }else{
                        if(currentTranslationX<0) {
                            AnimatorSet resetSet=new AnimatorSet();
                            ObjectAnimator resetAlpha=ObjectAnimator.ofFloat(getChildAt(0),"alpha",currentAlpha,1);
                            resetAlpha.setInterpolator(accelerateInterpolator);
                            resetAlpha.setDuration(300);
                            ObjectAnimator resetTranX =  ObjectAnimator.ofFloat(this.getChildAt(0), "TranslationX", currentTranslationX, 0);
                            resetTranX.setInterpolator(accelerateInterpolator);
                            resetTranX.setDuration(300);
                            resetSet.play(resetTranX).with(resetAlpha);
                            resetSet.start();
                        }
                    }
                    break;
                }
            }
            consum=true;
        }
        return consum;
    }

    public void setDoNotIntercept(boolean isDrawerOpened){
        this.doNotIntercept =isDrawerOpened;
    }

}
