package com.hawx.uestc_lib.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hawx.uestc_lib.R;
import com.hawx.uestc_lib.activity.AboutActivity;
import com.hawx.uestc_lib.activity.CollectionActivity;
import com.hawx.uestc_lib.activity.MainActivity;
import com.hawx.uestc_lib.activity.SearchActivity;
import com.hawx.uestc_lib.activity.SettingActivity;
import com.hawx.uestc_lib.activity.UserCenterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽屉导航适配类
 * @author frogermcs
 * @version 1.0
 */
public class GlobalLeftMenuAdapter extends ArrayAdapter<GlobalLeftMenuAdapter.GlobalMenuItem> {
    private static final int TYPE_MENU_ITEM = 0;
    private static final int TYPE_DIVIDER = 1;
    private final List<GlobalMenuItem> list=new ArrayList<GlobalMenuItem>();
    private NeedCloseDrawerListener listener;
    private final LayoutInflater layoutInflater;
    private String tag;
    private Context context;
    public GlobalLeftMenuAdapter(Context context, String tag) {
        super(context,0);
        this.tag=tag;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        setupMenuItems();
    }

    private void setupMenuItems() {
        list.add(GlobalMenuItem.dividerMenuItem());
        list.add(new GlobalMenuItem(R.mipmap.icon_globalmenuitem_home,"主页"));
        list.add(new GlobalMenuItem(R.mipmap.icon_globalmenuitem_search,"馆藏查询"));
        list.add(new GlobalMenuItem(R.mipmap.icon_globalmenuitem_user,"个人中心"));
        list.add(new GlobalMenuItem(R.mipmap.icon_globalmenuitem_like,"我的收藏"));
        list.add(GlobalMenuItem.dividerMenuItem());
        list.add(new GlobalMenuItem(0,"设置"));
        list.add(new GlobalMenuItem(0,"关于"));
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GlobalMenuItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isDivider?TYPE_DIVIDER:TYPE_MENU_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position)==TYPE_MENU_ITEM){
            MenuItemViewHolder holder;
            if(convertView==null){
                convertView=layoutInflater.inflate(R.layout.base_globalmenu_item,parent,false);
                holder=new MenuItemViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder= (MenuItemViewHolder) convertView.getTag();
            }
            final GlobalMenuItem item=list.get(position);
            holder.iconImage.setImageResource(item.iconResID);
            holder.labelText.setText(item.label);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.needCloseDrawer();
                    switch (item.label){
                        case "主页":{
                            Intent intent=new Intent(context,MainActivity.class);
                            context.startActivity(intent);
                            break;
                        }
                        case "馆藏查询":{
                            Intent intent=new Intent(context,SearchActivity.class);
                            context.startActivity(intent);
                            break;
                        }
                        case "个人中心":{
                            Intent intent=new Intent(context,UserCenterActivity.class);
                            context.startActivity(intent);
                            break;
                        }
                        case "我的收藏":{
                            Intent intent=new Intent(context,CollectionActivity.class);
                            context.startActivity(intent);
                            break;
                        }
                        case "设置":{
                            Intent intent=new Intent(context,SettingActivity.class);
                            context.startActivity(intent);
                            break;
                        }
                        case "关于":{
                            Intent intent=new Intent(context,AboutActivity.class);
                            context.startActivity(intent);
                            break;
                        }
                        default:
                            break;
                    }
                }
            });
            convertView.setBackgroundResource(R.drawable.globalmenu_button_background);
            return convertView;
        }else {
            return layoutInflater.inflate(R.layout.base_globalmenu_divider,parent,false);
        }

    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isDivider;
    }
    static class GlobalMenuItem {
        public int iconResID;
        public String label;
        public boolean isDivider;
        public GlobalMenuItem(){
        }
        public GlobalMenuItem(int iconResID,String label){
            this.iconResID=iconResID;
            this.label=label;
            this.isDivider=false;
        }
        public static GlobalMenuItem dividerMenuItem(){
            GlobalMenuItem globalMenuItem=new GlobalMenuItem();
            globalMenuItem.isDivider=true;
            return globalMenuItem;
        }
    }
    public class MenuItemViewHolder {
        ImageView iconImage;
        TextView labelText;
        public MenuItemViewHolder(View view){
            iconImage= (ImageView) view.findViewById(R.id.base_globalmenuitem_image);
            labelText= (TextView) view.findViewById(R.id.base_globalmenuitem_text);
        }
    }
    public interface NeedCloseDrawerListener{
        void needCloseDrawer();
    }

    public void setListener(NeedCloseDrawerListener listener) {
        this.listener = listener;
    }
}
