package com.hawx.uestc_lib.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hawx.uestc_lib.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/13.
 */
public class TableLayout extends LinearLayout {
    @BindView(R.id.widget_tablelayout_locallib)
    TextView local_lib;
    @BindView(R.id.widget_tablelayout_num)
    TextView num;
    @BindView(R.id.widget_tablelayout_state)
    TextView state;
    @BindView(R.id.widget_tablelayout_localnum)
    TextView local_num;
    @BindView(R.id.widget_tablelayout_remark)
    TextView remark;
    @BindView(R.id.widget_tablelayout_localdetail)
    TextView local_detail;
    @BindView(R.id.widget_tablelayout_loactionbutton)
    ImageButton local_button;
    private String local_final_1;
    private String local_final_2;
    private String local_url="http://10.21.16.217/RFIDWeb/TSDW/GotoFlash.aspx?szBarCode=";
    public TableLayout(Context context) {
        super(context);
    }
    public TableLayout(Context context, String local_lib, String num, String state, final String local_num, String remark, final RequestQueue requestQueue) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_tablelayout,this);
        ButterKnife.bind(this);
        local_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String regular="\\s*|\t|\r|\n";
                Pattern pattern= Pattern.compile(regular);
                Matcher matcher=pattern.matcher(local_num);
                String url =local_url+matcher.replaceAll("");
                requestQueue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "校内网才可查询", Toast.LENGTH_SHORT).show();
                        local_detail.setText("请切换至校内网");
                    }
                }));
            }
        });
        this.local_lib.setText(local_lib);
        this.num.setText(num);
        this.state.setText(state);
        this.local_num.setText(local_num);
        this.remark.setText(remark);
    }

    private void parseData(String response) {
        String regular = "("+"strWZxxxxxx"+")"+"(.*)"+"("+"层"+")";
        Pattern pattern = Pattern.compile(regular);
        Matcher loc= pattern.matcher(response);
        if (loc.find( )) {
            local_final_1=loc.group(2)+"层";
        }
        if(local_final_1!=null){
            String regular2= "(\\[)(.*)";
            Pattern pattern2=Pattern.compile(regular2);
            Matcher matcher2=pattern2.matcher(local_final_1);
            if(matcher2.find()){
                local_final_2="["+matcher2.group(2);
                local_detail.setText(local_final_2);
            }}else{
                local_detail.setText("没有相关信息");
        }
    }
}
