package com.learning.wow.learningassistence.util;

import android.text.TextUtils;

import com.learning.wow.learningassistence.db.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by wow on 2017/7/30.
 */

public class Utility {
    /**
     * 解析和处理服务器返回的文章数据
     */

    public static boolean handleProvinceResponse(String response,News news){
        List<News> mNew;
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                    JSONObject provinceObject = allProvinces.getJSONObject(0);
                    news.setTitle(provinceObject.getString("title"));
                    news.setText(provinceObject.getString("article"));
                    news.save();

                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

}
