package com.hawx.uestc_lib.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/9.
 */
public class ResultData {
    private JSONArray data;
    private String totalNum;
    private int pn;
    private String rn;
    public static ResultData jsonToData(JSONObject jsonObject) throws JSONException {
        ResultData resultData=new ResultData();
        resultData.data=jsonObject.getJSONArray("data");
        resultData.totalNum=jsonObject.getString("totalNum");
        resultData.pn=jsonObject.getInt("pn");
        resultData.rn=jsonObject.getString("rn");
        return resultData;
    }
}
