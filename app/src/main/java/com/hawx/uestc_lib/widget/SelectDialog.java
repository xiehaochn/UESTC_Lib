package com.hawx.uestc_lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/6.
 */
public class SelectDialog extends Dialog implements View.OnClickListener{
    @BindView(R.id.select_dialog_242)
    Button button242;
    @BindView(R.id.select_dialog_252)
    Button button252;
    @BindView(R.id.select_dialog_244)
    Button button244;
    @BindView(R.id.select_dialog_248)
    Button button248;
    @BindView(R.id.select_dialog_257)
    Button button257;
    @BindView(R.id.select_dialog_243)
    Button button243;
    @BindView(R.id.select_dialog_247)
    Button button247;
    @BindView(R.id.select_dialog_251)
    Button button251;
    @BindView(R.id.select_dialog_253)
    Button button253;
    @BindView(R.id.select_dialog_250)
    Button button250;
    @BindView(R.id.select_dialog_249)
    Button button249;
    @BindView(R.id.select_dialog_245)
    Button button245;
    @BindView(R.id.select_dialog_256)
    Button button256;
    @BindView(R.id.select_dialog_254)
    Button button254;
    @BindView(R.id.select_dialog_246)
    Button button246;
    @BindView(R.id.select_dialog_255)
    Button button255;
    @BindView(R.id.select_dialog_258)
    Button button258;
    @BindView(R.id.select_dialog_back)
    Button button_back;
    private Context context;
    private dialogSelectedListener listener;
    private int catalog_ID;
    private String catalog;
    public SelectDialog(Context context) {
        super(context);
        this.context=context;
    }

    public SelectDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
    }

    protected SelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(Utils.getWindowWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.widget_selectdialog,null);
        setContentView(view,params);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.select_dialog_242, R.id.select_dialog_252, R.id.select_dialog_244,R.id.select_dialog_248,R.id.select_dialog_257,
            R.id.select_dialog_243,R.id.select_dialog_247,R.id.select_dialog_251,R.id.select_dialog_253,R.id.select_dialog_250,
            R.id.select_dialog_249,R.id.select_dialog_245,R.id.select_dialog_256,R.id.select_dialog_254,R.id.select_dialog_246,
            R.id.select_dialog_255,R.id.select_dialog_258,R.id.select_dialog_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_dialog_242:{
                catalog_ID=242;
                catalog="中国文学";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_252:{
                catalog_ID=252;
                catalog="人物传记";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_244:{
                catalog_ID=244;
                catalog="儿童文学";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_248:{
                catalog_ID=248;
                catalog="历史";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_257:{
                catalog_ID=257;
                catalog="哲学";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_243:{
                catalog_ID=243;
                catalog="外国文学";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_247:{
                catalog_ID=247;
                catalog="小说";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_251:{
                catalog_ID=251;
                catalog="心灵鸡汤";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_253:{
                catalog_ID=253;
                catalog="心理学";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_250:{
                catalog_ID=250;
                catalog="成功励志";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_249:{
                catalog_ID=249;
                catalog="教育";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_245:{
                catalog_ID=245;
                catalog="散文";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_256:{
                catalog_ID=256;
                catalog="理财";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_254:{
                catalog_ID=254;
                catalog="管理";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_246:{
                catalog_ID=246;
                catalog="经典名著";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_255:{
                catalog_ID=255;
                catalog="经济";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_258:{
                catalog_ID=258;
                catalog="计算机";
                if(listener!=null){
                    listener.dialogSelected();
                }
                this.cancel();
                break;
            }
            case R.id.select_dialog_back:{
                this.cancel();
                break;
            }
            default:
                break;
        }
    }
    public interface dialogSelectedListener{
        void dialogSelected();
    };

    public void setListener(dialogSelectedListener listener) {
        this.listener = listener;
    }

    public int getCatalog_ID() {
        return catalog_ID;
    }

    public String getCatalog() {
        return catalog;
    }
}
