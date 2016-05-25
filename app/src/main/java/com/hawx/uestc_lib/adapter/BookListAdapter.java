package com.hawx.uestc_lib.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.activity.BookListActivity;
import com.hawx.uestc_lib.data.BookDetailData;
import com.hawx.uestc_lib.utils.Utils;
import com.hawx.uestc_lib.widget.BookDetailDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/10.
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookDetailVH> {
    private ArrayList<BookDetailData> bookDetailDatas=new ArrayList<BookDetailData>();
    private Context context;
    private RequestQueue requestQueue;
    private HashMap<Integer,Bitmap> bitmapHashMap=new HashMap<Integer,Bitmap>();
    private Activity activity;
    public BookListAdapter(ArrayList<BookDetailData> bookDetailDatas, Context context, Activity activity) {
        this.bookDetailDatas = bookDetailDatas;
        this.context=context;
        this.activity=activity;
        requestQueue=Volley.newRequestQueue(context);
    }

    @Override
    public BookDetailVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        return new BookDetailVH(inflater.inflate(R.layout.viewholder_bookdetail,parent,false));
    }

    @Override
    public int getItemCount() {
        return bookDetailDatas.size();
    }

    @Override
    public void onBindViewHolder(BookDetailVH holder, final int position) {
        BookDetailData data=bookDetailDatas.get(position);
        holder.bookTitle.setText(data.getTitle());
        if(data.getTitle().length()>10) {
            holder.bookTitle.setTextSize(18);
        }
        holder.bookReading.setText(data.getReading());
        if(bitmapHashMap.get(position)==null) {
            addBookPic(position,data.getImg(), holder);
        }else{
            holder.bookPic.setImageBitmap(bitmapHashMap.get(position));
        }
        String[] tags=data.getTags().split(" ");
        String tag="";
        for(int i=0;i<5&&i<tags.length;i++){
            tag+=tags[i]+"  ";
        }
        holder.bookTags.setText(tag);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BookDetailDialog bookDetailDialog=new BookDetailDialog(context,R.style.SelectDialog,bookDetailDatas.get(position),bitmapHashMap.get(position));
                bookDetailDialog.setCancelable(true);
                Window dialogWindow=bookDetailDialog.getWindow();
                WindowManager.LayoutParams params=dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.TOP);
                int[] location=new int[2];
                v.getLocationOnScreen(location);
                params.y=location[1]-Utils.getStatusBarHeight(activity);
                dialogWindow.setAttributes(params);
                dialogWindow.setWindowAnimations(R.style.SelectDialogAnim);
                bookDetailDialog.show();
                bookDetailDialog.setListener(new BookDetailDialog.OnBuyTextClickListener() {
                    @Override
                    public void dangdangClicked() {
                        Toast.makeText(context,"dangdangClicked",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void jingdongClicked() {
                        Toast.makeText(context,"jingdongClicked",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addBookPic(final int position, String imageURL, final BookDetailVH holder) {
        final int maxSize=context.getResources().getDimensionPixelSize(R.dimen.viewholder_bookdetail_bookpicsize);
        requestQueue.add(new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.bookPic.setImageBitmap(response);
                bitmapHashMap.put(position,response);
            }
        }, maxSize, maxSize, ImageView.ScaleType.FIT_XY, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bitmap loading_failed=BitmapFactory.decodeResource(context.getResources(),R.drawable.viewholder_bookdetail_errorpic);
                Bitmap scaled=Utils.scaleDown(loading_failed,maxSize,false);
                holder.bookPic.setImageBitmap(scaled);
                bitmapHashMap.put(position,scaled);
            }
        }));
    }

    class BookDetailVH extends RecyclerView.ViewHolder {
        @BindView(R.id.viewholder_bookdetail_cardview)
        CardView cardView;
        @BindView(R.id.viewholder_bookdetail_bookpic)
        ImageView bookPic;
        @BindView(R.id.viewholder_bookdetail_booktitle)
        TextView bookTitle;
        @BindView(R.id.viewholder_bookdetail_reading)
        TextView bookReading;
        @BindView(R.id.viewholder_bookdetail_tags)
        TextView bookTags;
        public BookDetailVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setBookDetailDatas(ArrayList<BookDetailData> bookDetailDatas) {
        this.bookDetailDatas = bookDetailDatas;
        notifyDataSetChanged();
    }

}
