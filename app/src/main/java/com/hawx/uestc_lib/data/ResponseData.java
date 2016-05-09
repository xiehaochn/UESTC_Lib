package com.hawx.uestc_lib.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/9.
 */
public class ResponseData {
    private String resultcode;
    private String reason;
    private JSONObject result;
    private int error_code;
    public static ResponseData jsonToData(JSONObject jsonObject) throws JSONException {
        ResponseData responseData=new ResponseData();
        responseData.resultcode=jsonObject.getString("resultcode");
        responseData.reason=jsonObject.getString("reason");
        responseData.result=jsonObject.getJSONObject("result");
        responseData.error_code=jsonObject.getInt("error_code");
        return responseData;
    }
}
