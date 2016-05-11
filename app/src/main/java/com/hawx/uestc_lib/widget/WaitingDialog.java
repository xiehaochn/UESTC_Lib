package com.hawx.uestc_lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.utils.Utils;

/**
 * WaitingDialog Custom View
 * @author Hawx
 * @version 1.0
 */
public class WaitingDialog extends View {
    private static final int DEFAULT_SIZE=50;
    /**旋转速度 圆弧/每秒*/
    private int speed;
    /**提示文字*/
    private String text;
    /**是否顺时针转动*/
    private boolean clockWise;
    /**提示文字位置
     *true为dialog上方
     * false为dialog下方
     */
    private boolean textTop;
    /**提示文字大小*/
    private float textSize;
    /**提示文字颜色*/
    private int textColor;
    /**是否显示提示文字*/
    private boolean isTextShow;
    /**arc color*/
    private int arcColor;
    private Paint textPaint;
    private Paint arcPaint;
    private Rect textBound;
    private RectF arcBound;
    private Context context;
    private int defaultSizePx;
    private float defaultStroke;
    private int currentArcNum =0;
    private int defaultPaddingMidDp=5;
    public WaitingDialog(Context context) {
        super(context);
        this.context=context;
    }

    public WaitingDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        TypedArray a=context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaitingDialog,0,0);
        try{
            speed=a.getInt(R.styleable.WaitingDialog_speed,1);
            text=a.getString(R.styleable.WaitingDialog_text);
            clockWise=a.getBoolean(R.styleable.WaitingDialog_clockWise,true);
            textTop=a.getBoolean(R.styleable.WaitingDialog_textTop,true);
            textSize=a.getDimensionPixelSize(R.styleable.WaitingDialog_textSize,getResources().getDimensionPixelSize(R.dimen.widget_waitingdialog_defaultextsize));
            textColor=a.getColor(R.styleable.WaitingDialog_textColor, Color.GRAY);
            isTextShow=a.getBoolean(R.styleable.WaitingDialog_isTextShow,false);
            arcColor=a.getColor(R.styleable.WaitingDialog_arcColor,Color.GRAY);
        }finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        defaultStroke=Utils.dpTopx(context,2);
        defaultSizePx=Utils.dpTopx(context,DEFAULT_SIZE);
        arcPaint =new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(arcColor);
        arcPaint.setStrokeWidth(defaultStroke);
        arcPaint.setStyle(Paint.Style.STROKE);
        if(isTextShow){
            textPaint=new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);
            textBound=new Rect();
            textPaint.getTextBounds(text,0,text.length(),textBound);
            textBound=new Rect(0,0,textBound.width(),textBound.height()+Utils.dpTopx(context,defaultPaddingMidDp));
        }
        new Thread(){
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 /speed);
                        if(clockWise) {
                            //顺时针
                            if (currentArcNum < 7) {
                                currentArcNum++;
                            } else {
                                currentArcNum = 0;
                            }
                        }else{
                            //逆时针
                            if(currentArcNum>1){
                                currentArcNum--;
                            }else {
                                currentArcNum=8;
                            }
                        }
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft=getPaddingLeft();
        final int paddingRight=getPaddingRight();
        final int paddingTop=getPaddingTop();
        final int paddingBottom=getPaddingBottom();
        int width=getWidth()-paddingLeft-paddingRight;;
        int height=getHeight()-paddingTop-paddingBottom;
        if(isTextShow){
            canvas.drawText(text,width/2-textBound.width()/2,textBound.height()-Utils.dpTopx(context,defaultPaddingMidDp),textPaint);
            arcBound=new RectF(width/2-(height-textBound.height())/2+defaultStroke,textBound.height()+defaultStroke,width/2+(height-textBound.height())/2-defaultStroke,height-defaultStroke);
            if(clockWise) {
                //顺时针
                for (int i = 0; i < 8; i++) {
                    if (i == currentArcNum) {
                        arcPaint.setAlpha(255);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    } else if (i < currentArcNum) {
                        arcPaint.setAlpha(255 - (currentArcNum - i) * 255 / 8);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    } else {
                        arcPaint.setAlpha((i - currentArcNum) * 255 / 8);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    }
                }
            }else{
                //逆时针
                for (int i = 0; i < 8; i++) {
                    if (i == currentArcNum) {
                        arcPaint.setAlpha(255);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    } else if (i < currentArcNum) {
                        arcPaint.setAlpha((currentArcNum - i) * 255 / 8);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    } else {
                        arcPaint.setAlpha(255 - (i - currentArcNum) * 255 / 8);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    }
                }
            }
        }else{
            arcBound=new RectF(defaultStroke,defaultStroke,width-defaultStroke,height-defaultStroke);
            if(clockWise) {
                //顺时针
                for (int i = 0; i < 8; i++) {
                    if (i == currentArcNum) {
                        arcPaint.setAlpha(255);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    } else if (i < currentArcNum) {
                        arcPaint.setAlpha(255 - (currentArcNum - i) * 255 / 8);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    } else {
                        arcPaint.setAlpha((i - currentArcNum) * 255 / 8);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    }
                }
            }else{
                //逆时针
                for (int i = 0; i < 8; i++) {
                    if (i == currentArcNum) {
                        arcPaint.setAlpha(255);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    } else if (i < currentArcNum) {
                        arcPaint.setAlpha((currentArcNum - i) * 255 / 8);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    } else {
                        arcPaint.setAlpha(255 - (i - currentArcNum) * 255 / 8);
                        canvas.drawArc(arcBound, 2.5f + i * 45f, 40f, false, arcPaint);
                    }
                }
            }
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        if(isTextShow) {
            int circleWidth;
            int circleHeight;
            if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
                circleWidth=defaultSizePx;
                circleHeight=defaultSizePx;
            }else if(widthSpecMode==MeasureSpec.AT_MOST){
                circleWidth=defaultSizePx;
                circleHeight=heightSpecSize;
            }else if(heightSpecMode==MeasureSpec.AT_MOST){
                circleWidth=widthSpecSize;
                circleHeight=defaultSizePx;
            }else{
                circleWidth=widthSpecSize;
                circleHeight=heightSpecSize;
            }
            setMeasuredDimension(circleWidth+textBound.width(),circleHeight+textBound.height());
        }else {
            if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(defaultSizePx,defaultSizePx);
            }else if(widthSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(defaultSizePx,heightSpecSize);
            }else if(heightSpecMode==MeasureSpec.AT_MOST){
                setMeasuredDimension(widthSpecSize,defaultSizePx);
            }else{
                super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    public void setSpeed(int speed){
        this.speed=speed;
    }
    public void setClockWise(boolean isClockWise){
        this.clockWise=isClockWise;
    }

}
