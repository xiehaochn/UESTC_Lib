package com.hawx.uestc_lib.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/13.
 */
public class SearchDetailData {
    private String title;
    private String url;

    public SearchDetailData(String title, String url) {
        this.title = title;
        this.url = url;
    }
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("title",title);
        jsonObject.put("url",url);
        return jsonObject;
    }
}
