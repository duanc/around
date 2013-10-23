package com.dc.around.tools;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.dc.around.MyActivity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-14
 * Time: 下午9:17
 * To change this template use File | Settings | File Templates.
 */
public class Tools {
    public static String getJson(String url) throws IOException {

        String strURL = URLEncoder.encode(url, "utf-8");

        HttpGet request = new HttpGet(url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("charset", "UTF-8");
        HttpResponse httpResponse = new DefaultHttpClient().execute(request);

        String msg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        return msg;

    }

    public static String getAssetsJson(Activity activity, String fileName) throws IOException {

        InputStream inputStream = activity.getResources().getAssets().open(fileName + Constant.LIST_SUFFIX);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte buff[] = new byte[1024 * 8];
        int length = 0;
        while ((length = inputStream.read(buff)) != -1) {
            byteArrayOutputStream.write(buff, 0, length);
        }
        String json = byteArrayOutputStream.toString().substring(2);
        return json;
    }


}
