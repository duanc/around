package com.dc.around.Manage;

import android.app.Activity;
import android.util.Log;
import com.dc.around.model.Category;
import com.dc.around.model.Information;
import com.dc.around.tools.Constant;
import com.dc.around.tools.Tools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-14
 * Time: 下午11:12
 * To change this template use File | Settings | File Templates.
 */
public class Manage {


    public List<Information> getInformationList(String POIName, String X, String Y, String range, String page) throws JSONException, IOException {

        // POIName = URLEncoder.encode(POIName, "UTF-8");
        //116.322479    39.980781     +"&range="+range
        String Json = Tools.getJson(Constant.POI_GEO_URL + "?q=" + POIName + "&coordinate=" + X + "%2C" + Y + "&count=20" + "&range=" + range + "&page=" + page + "&access_token=" + Constant.SINA_ACCESS_TOKEN);
        JSONTokener jsonTokener = new JSONTokener(Json);

        List<Information> informationList = new ArrayList<Information>();
        JSONObject person = (JSONObject) jsonTokener.nextValue();

        JSONArray jsonArray = person.optJSONArray("poilist");
        if (jsonArray == null) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            Information information = new Information();
            JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
            information.setAddress(jsonObject.optString("address"));
            information.setDistance(jsonObject.optString("distance"));
            information.setImageid(jsonObject.optString("imageid"));
            information.setName(jsonObject.optString("name"));
            information.setTel(jsonObject.optString("tel"));
            information.setTimestamp(jsonObject.optString("timestamp"));
            information.setType(jsonObject.optString("type"));
            information.setX(jsonObject.optString("x"));
            information.setY(jsonObject.optString("y"));
            informationList.add(information);


        }


        return informationList;

    }

    public List<Category> getCategoryList(Activity activity, String code) throws JSONException, IOException {


        String Json = Tools.getAssetsJson(activity, code);


        JSONTokener jsonTokener = new JSONTokener(Json);


        JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();

        // JSONTokener jsonTokener = new JSONTokener(Json);
        // JSONObject person = (JSONObject) jsonTokener.nextValue();

        List<Category> categoryList = new ArrayList<Category>();


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
            Category category = new Category();
            category.setCode(jsonObject.optString("code"));
            category.setIschildren(jsonObject.optBoolean("ischildren"));
            category.setName(jsonObject.optString("name"));
            categoryList.add(category);
            Log.e("getCategoryList", "categoryList" + categoryList.get(i).getName());
        }

        Log.e("getCategoryList", "categoryList" + categoryList.get(0).getName());
        return categoryList;

    }

}
