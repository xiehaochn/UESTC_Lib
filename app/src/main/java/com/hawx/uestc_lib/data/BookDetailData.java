package com.hawx.uestc_lib.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/9.
 */
public class BookDetailData {
    private String title;
    private String catalog;
    private String tags;
    private String sub1;
    private String sub2;
    private String img;
    private String reading;
    private String online;
    private String bytime;
    public static BookDetailData jsonToData(JSONObject jsonObject) throws JSONException {
        BookDetailData bookDetailData=new BookDetailData();
        bookDetailData.title=jsonObject.getString("title");
        bookDetailData.catalog=jsonObject.getString("catalog");
        bookDetailData.tags=jsonObject.getString("tags");
        bookDetailData.sub1=jsonObject.getString("sub1");
        bookDetailData.sub2=jsonObject.getString("sub2");
        bookDetailData.img=jsonObject.getString("img");
        bookDetailData.reading=jsonObject.getString("reading");
        bookDetailData.online=jsonObject.getString("online");
        bookDetailData.bytime=jsonObject.getString("bytime");
        return bookDetailData;
    }

    public String getTitle() {
        return title;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getTags() {
        return tags;
    }

    public String getSub1() {
        return sub1;
    }

    public String getSub2() {
        return sub2;
    }

    public String getImg() {
        return img;
    }

    public String getReading() {
        return reading;
    }

    public String getOnline() {
        return online;
    }

    public String getBytime() {
        return bytime;
    }
}
