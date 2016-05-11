package com.hawx.uestc_lib.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hawx.uestc_lib.R;

/**
 * ViewPagerTabPoints
 * @author Hawx
 * @version 1.0
 */
public class ViewPagerTabPoints extends View {
    private int pointsColor;
    private float radio;
    private float interval;
    private Paint paint;
    private int pointsNum;
    private int currentPoint;
    public ViewPagerTabPoints(Context context) {
        super(context);
    }
    public ViewPagerTabPoints(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a=context.getTheme().obtainStyledAttributes(attrs,R.styleable.ViewPagerTabPoints,0,0);
        try{
            pointsColor=a.getColor(R.styleable.ViewPagerTabPoints_pointsColor,Color.GRAY);
            radio =a.getDimensionPixelSize(R.styleable.ViewPagerTabPoints_pointsSize,getResources().getDimensionPixelOffset(R.dimen.widget_viewpagertabpoints_defaultsize));
            interval=a.getDimensionPixelSize(R.styleable.ViewPagerTabPoints_interval,getResources().getDimensionPixelOffset(R.dimen.widget_viewpagertabpoints_defaultinterval));
        }finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(pointsColor);
        paint.setStyle(Paint.Style.FILL);
    }

    public ViewPagerTabPoints(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width= (int) ((pointsNum-1)*(interval+2*radio)+3*radio);
        int height= (int) (radio *3);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width=getWidth()-getPaddingLeft()-getPaddingRight();
        int height=getHeight()-getPaddingTop()-getPaddingBottom();
        if(pointsNum==1){
            return;
        }else{
                for (int i = 0; i < pointsNum; i++) {
                    if (i == currentPoint) {
                        canvas.drawCircle(width / 2 - (pointsNum - 1) * interval / 2 + interval * i, height / 2, (float) (1.5 * radio), paint);
                    } else {
                        canvas.drawCircle(width / 2 - (pointsNum - 1) * interval / 2 + interval * i, height / 2, radio, paint);
                    }
               }
        }
    }

    public void setPointsNum(int pointsNum) {
        this.pointsNum = pointsNum;
        invalidate();
    }

    public void setCurrentPoint(int currentPoint) {
        this.currentPoint = currentPoint;
        invalidate();
    }


}
