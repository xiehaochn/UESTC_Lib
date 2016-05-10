package com.hawx.uestc_lib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.data.BookDetailData;
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
    public BookListAdapter(ArrayList<BookDetailData> bookDetailDatas, Context context) {
        this.bookDetailDatas = bookDetailDatas;
        this.context=context;
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
    public void onBindViewHolder(BookDetailVH holder, int position) {
        BookDetailData data=bookDetailDatas.get(position);
        holder.bookTitle.setText(data.getTitle());
        holder.bookReading.setText(data.getReading());
        if(bitmapHashMap.get(position)==null) {
            addBookPic(position,data.getImg(), holder);
        }else{
            holder.bookPic.setImageBitmap(bitmapHashMap.get(position));
        }
    }

    private void addBookPic(final int position, String imageURL, final BookDetailVH holder) {
        int maxSize=context.getResources().getDimensionPixelSize(R.dimen.viewholder_bookdetail_bookpicsize);
        requestQueue.add(new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.bookPic.setImageBitmap(response);
                bitmapHashMap.put(position,response);
            }
        }, maxSize, maxSize, ImageView.ScaleType.FIT_XY, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                holder.bookPic.setImageResource(R.drawable.viewholder_bookdetail_errorpic);
                bitmapHashMap.put(position, BitmapFactory.decodeResource(context.getResources(),R.drawable.viewholder_bookdetail_errorpic));
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
