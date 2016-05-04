package com.hawx.uestc_lib.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * CustomViewPager
 * @author Hawx
 * @version 1.0
 */
public class CustomViewPager extends ViewPager {
    private onTouchEventDownListener listenerDown;
    private onTouchEventUpListener listenerUp;
    private boolean isDragging=false;
    public CustomViewPager(Context context) {
        super(context);
        recyclePlay();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        recyclePlay();
    }
    private void recyclePlay() {
        if(!isDragging) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCurrentItem(getCurrentItem() + 1);
                    recyclePlay();
                }
            }, 3000);
        }else{
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclePlay();
                }
            }, 3000);
        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            if(this.listenerDown !=null) {
                isDragging=true;
                listenerDown.onTouchEventDown();
            }
        }else if(ev.getAction()==MotionEvent.ACTION_UP){
            if(this.listenerUp!=null){
                isDragging=false;
                listenerUp.onTouchEventUp();
            }
        }
        return super.onTouchEvent(ev);
    }
    public interface onTouchEventDownListener{
        void onTouchEventDown();
    }
    public interface onTouchEventUpListener{
        void onTouchEventUp();
    }
    public void setListener(onTouchEventDownListener listener) {
        this.listenerDown = listener;
    }

    public void setListenerUp(onTouchEventUpListener listenerUp) {
        this.listenerUp = listenerUp;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }
}
