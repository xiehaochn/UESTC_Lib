package com.hawx.uestc_lib.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.activity.WebViewActivity;
import com.hawx.uestc_lib.data.RecommendTestData;
import com.hawx.uestc_lib.widget.BookDetailDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/27.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendVH> {
    private Context context;

    public RecommendAdapter(Context context) {
        this.context = context;
        RecommendTestData.initBookDetailDatas();
    }

    @Override
    public RecommendVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.viewholder_recommend,parent,false);
        return new RecommendVH(view);
    }

    @Override
    public void onBindViewHolder(RecommendVH holder, final int position) {
        holder.name.setText(RecommendTestData.bookNames[position]);
        final Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),RecommendTestData.bookPicResource[position]);
        holder.pic.setImageBitmap(bitmap);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BookDetailDialog bookDetailDialog=new BookDetailDialog(context,R.style.SelectDialog,RecommendTestData.bookDetailDatas.get(position),bitmap);
                bookDetailDialog.setCancelable(true);
                Window dialogWindow=bookDetailDialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER_VERTICAL);
                dialogWindow.setWindowAnimations(R.style.SelectDialogAnim);
                bookDetailDialog.show();
                bookDetailDialog.setListener(new BookDetailDialog.OnBuyTextClickListener() {
                    @Override
                    public void dangdangClicked() {
                        String location[]=RecommendTestData.bookDetailDatas.get(position).getOnline().split(" ");
                        String regular = "(当当网:)"+"(.*)";
                        Pattern pattern = Pattern.compile(regular);
                        boolean find=false;
                        String url="";
                        for(String loc:location){
                            Matcher matcher = pattern.matcher(loc);
                            if (matcher.find()) {
                                url=matcher.group(2);
                                find=true;
                                break;
                            }
                        }
                        if(!find){
                            Toast.makeText(context, "链接无效", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("URL",url);
                            intent.putExtra("TITLE","当当网-"+RecommendTestData.bookDetailDatas.get(position).getTitle());
                            context.startActivity(intent);
                            bookDetailDialog.dismiss();
                        }
                    }

                    @Override
                    public void jingdongClicked() {
                        String location[]=RecommendTestData.bookDetailDatas.get(position).getOnline().split(" ");
                        String regular = "(京东商城:)"+"(.*)";
                        Pattern pattern = Pattern.compile(regular);
                        boolean find=false;
                        String url="";
                        for(String loc:location){
                            Matcher matcher = pattern.matcher(loc);
                            if (matcher.find()) {
                                url=matcher.group(2);
                                find=true;
                                break;
                            }
                        }
                        if(!find){
                            Toast.makeText(context, "链接无效", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent=new Intent(context, WebViewActivity.class);
                            intent.putExtra("URL",url);
                            intent.putExtra("TITLE","京东-"+RecommendTestData.bookDetailDatas.get(position).getTitle());
                            context.startActivity(intent);
                            bookDetailDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return RecommendTestData.bookNames.length;
    }

    class RecommendVH extends RecyclerView.ViewHolder{
        @BindView(R.id.viewholder_pic)
        ImageView pic;
        @BindView(R.id.viewholder_name)
        TextView name;
        @BindView(R.id.viewholder_relativelayout)
        RelativeLayout relativeLayout;
        public RecommendVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
