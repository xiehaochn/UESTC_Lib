package com.hawx.uestc_lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.data.BookDetailData;
import com.hawx.uestc_lib.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BookDetailDialog extends Dialog {
    private BookDetailData bookDetailData;
    private Context context;
    private Bitmap bitmap;
    private OnBuyTextClickListener listener;
    @BindView(R.id.widget_bookdetaildialog_imageview)
    ImageView bookPic;
    @BindView(R.id.widget_bookdetaildialog_booktitle)
    TextView bookTitle;
    @BindView(R.id.widget_bookdetaildialog_reading)
    TextView reading;
    @BindView(R.id.widget_bookdetaildialog_tag)
    TextView tag;
    @BindView(R.id.widget_bookdetaildialog_brief)
    TextView brief;
    @BindView(R.id.widget_bookdetaildialog_dangdangwang)
    TextView dangdang;
    @BindView(R.id.widget_bookdetaildialog_jingdong)
    TextView jingdong;
    public BookDetailDialog(Context context) {
        super(context);
        this.context=context;
    }

    public BookDetailDialog(Context context, int themeResId,BookDetailData bookDetailData,Bitmap bitmap) {
        super(context, themeResId);
        this.context=context;
        this.bookDetailData=bookDetailData;
        if(bitmap!=null) {
            this.bitmap = bitmap;
        }else{
            this.bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.viewholder_bookdetail_errorpic);
        }
    }

    protected BookDetailDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(Utils.getWindowWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.widget_bookdetaildialog,null);
        setContentView(view,params);
        ButterKnife.bind(this);
        bookPic.setImageBitmap(bitmap);
        bookTitle.setText(bookDetailData.getTitle());
        reading.setText(bookDetailData.getReading());
        tag.setText(bookDetailData.getTags());
        String briefs[]=bookDetailData.getSub2().split("\\n");
        String brief_final="";
        for(int i=0;i<briefs.length;i++){
            brief_final+="\t\t\t\t"+briefs[i]+"\n";
        }
        brief.setText(brief_final);
        dangdang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.dangdangClicked();
                }
            }
        });
        jingdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.jingdongClicked();
                }
            }
        });
    }
    public interface OnBuyTextClickListener{
        void dangdangClicked();
        void jingdongClicked();
    }

    public void setListener(OnBuyTextClickListener listener) {
        this.listener = listener;
    }
}
