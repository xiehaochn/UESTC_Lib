package com.hawx.uestc_lib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.base.BaseActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/12.
 */
public class UserCenterActivity extends BaseActivity {
    private final String USER_DATA="USER_DATA";
    private final String FROM_ACTIVITY="FROM_ACTIVITY";
    private String user_name;
    private int bookNum;
    private ArrayList<String> BookDetail=new ArrayList<String>();
    @BindView(R.id.activity_usercenter_name)
    TextView name;
    @BindView(R.id.activity_usercenter_school)
    TextView school;
    @BindView(R.id.activity_usercenter_date)
    TextView date;
    @BindView(R.id.activity_usercenter_date_container)
    LinearLayout dataContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTag("个人中心");
        setContentView(R.layout.activity_usercenter);
        ButterKnife.bind(this);
        if(getIntent().getExtras()!=null) {
            String from=getIntent().getExtras().getString(FROM_ACTIVITY);
            if (from.equals("LoginActivity")) {
                parseData();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void parseData() {
        Bundle bundle=this.getIntent().getExtras();
        String data=bundle.getString(USER_DATA);
        Document doc = Jsoup.parse(data);
        Elements user =doc.getElementsByClass("patNameAddress");
        for (Element user_one :user ) {
            Elements name=user_one.getElementsByTag("strong");
            for (Element name_one :name ) {
                user_name=name_one.text();
            }
        }
        name.setText(user_name);
        String user_string=user.text();
        String school_regular = "("+user_name+")"+"(.*)(证件)";
        Pattern school_pattern = Pattern.compile(school_regular);
        Matcher school_m = school_pattern.matcher(user_string);
        if (school_m.find( )) {
            school.setText(school_m.group(2));
        } else {
            school.setText("没有相关信息");
        }
        String date_regular = "(证件)(.*)(续借)";
        Pattern date_pattern = Pattern.compile(date_regular);
        Matcher date_m = date_pattern.matcher(user_string);
        if (date_m.find( )) {
            date.setText(date_m.group(2));
        } else {
            date.setText("没有相关信息");
        }
        Elements book =doc.getElementsByClass("patFuncEntry");
        bookNum=0;
        for (Element book_one :book ) {
            bookNum+=1;
            Elements book_detail=book_one.getElementsByTag("td");
            for (Element book_final:book_detail ) {
                String test=book_final.text();
                if(test.equals("")){

                } else {
                    BookDetail.add(test);
                }
            }
        }
        setTable();
    }

    private void setTable() {
        TableLayoutDate tl;
        for(int i=0;i<bookNum;i++){
            tl=generateTableView(i);
            dataContainer.addView(tl);
        }
    }
    private TableLayoutDate generateTableView(int i) {
        TableLayoutDate tl_r=new TableLayoutDate(this,BookDetail.get(i*4),BookDetail.get(i*4+1),BookDetail.get(i*4+2));
        return tl_r;
    }
    class TableLayoutDate extends LinearLayout{
        public TableLayoutDate(Context context){
            super(context);

        }
        public TableLayoutDate(Context context,String name,String num,String date){
            super(context);
            LayoutInflater.from(context).inflate(R.layout.widget_tablelayout_borrowedbook,this);
            TextView tv1= (TextView) findViewById(R.id.widget_tablelayout_borrowed_name);
            TextView tv2= (TextView) findViewById(R.id.widget_tablelayout_borrowed_num);
            TextView tv3= (TextView) findViewById(R.id.widget_tablelayout_borrowed_state);
            tv1.setText(name);
            tv2.setText(num);
            tv3.setText(date);
        }

    }
}
