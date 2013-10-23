package com.dc.around;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.dc.around.Manage.Manage;
import com.dc.around.model.Category;
import com.dc.around.model.Information;
import com.dc.around.tools.Constant;
import com.dc.around.tools.Tools;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    ListView listView;
    List<Category> categoryList;
    private ProgressDialog progressDialog;
    Manage manage = new Manage();
    LocationData locData = new LocationData();

    private void getMyLocation() {
        Log.d("BaiduMapDemo", "getMyLocation");

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);

        LocationClient locationClient = new LocationClient(this);
        locationClient.setLocOption(option);

                TextView textView1  = (TextView) findViewById(R.id.locationmessage);
                textView1.setText("正在定位...");
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceiveLocation address " + bdLocation.getAddrStr());
                Log.d("BaiduMapDemo", "onReceiveLocation Latitude " + bdLocation.getLatitude());
                Log.d("BaiduMapDemo", "onReceiveLocation Longitude " + bdLocation.getLongitude());

                //double lng = bdLocation.getLongitude();
                //double lat = bdLocation.getLatitude();
                locData.latitude = bdLocation.getLatitude();
                locData.longitude = bdLocation.getLongitude();
//                //如果不显示定位精度圈，将accuracy赋值为0即可
//                locData.accuracy = bdLocation.getRadius();
//                locData.direction = bdLocation.getDerect();
                Constant.locData = locData;



                TextView textView1  = (TextView) findViewById(R.id.locationmessage);
                if(bdLocation.getAddrStr()==null){
                    textView1.setText("获取位置信息失败，请重试...");
                } else {
                    textView1.setText(bdLocation.getAddrStr());
                }


                progressDialog.dismiss();



            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceivePoi ");
            }
        });

        locationClient.start();
        locationClient.requestLocation();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        progressDialog = ProgressDialog.show(MyActivity.this, "Loading...", "正在定位请稍等...", true, false);
        /**
         * 手动触发一次定位请求
         */
        ImageButton imageButton = (ImageButton) findViewById(R.id.location);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();


            }
        });






        ImageButton aboutButton = (ImageButton) findViewById(R.id.about);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, AboutActivity.class);

                startActivity(intent);

            }
        });

        ImageButton searchNameButton = (ImageButton) findViewById(R.id.searchname);
        searchNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, SearchActivity.class);

                startActivity(intent);

            }
        });


        try {

            categoryList = manage.getCategoryList(MyActivity.this, "0000");
            Log.e("categoryList", "categoryList:" + categoryList.size());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //   textView = (TextView) findViewById(R.id.text);
//        textView.setText(categoryList.get(0).getName());


        listView = (ListView) findViewById(R.id.listView);

        // asyncTask.execute(null);


        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return categoryList.size();  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getItem(int position) {
                return categoryList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public long getItemId(int position) {
                return position;  //To change body of implemented methods use File | Settings | File Templates.
            }


            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.categorylist, parent, false);
//                if (convertView == null) {
//                    LayoutInflater layoutInflater = getLayoutInflater();
//                    convertView = layoutInflater.inflate(R.layout.categorylist, parent, false);
//                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyActivity.this, PoiActivity.class);
                        intent.putExtra("name", categoryList.get(position).getName());
                        intent.putExtra("code", categoryList.get(position).getCode());
                        startActivity(intent);

                    }
                });

                TextView nameTextView = (TextView) convertView.findViewById(R.id.textlist);
                Button nextButton = (Button) convertView.findViewById(R.id.btnlist);


                nameTextView.setText(categoryList.get(position).getName());
                if (!categoryList.get(position).isIschildren()) {

                    nextButton.setVisibility(View.INVISIBLE);
                } else {
                    nextButton.setId(position);//Integer.parseInt(categoryList.get(position).getCode()));//会报错
                    Log.e("nextButton position", "position  " + categoryList.get(position).getCode());
                    nextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //页面跳转  下级菜单
                            Intent intent = new Intent(MyActivity.this, TwoActivity.class);
                            intent.putExtra("name", categoryList.get(view.getId()).getName());
                            intent.putExtra("code", categoryList.get(view.getId()).getCode());
                            startActivity(intent);


                        }
                    });
                }


                return convertView;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        getMyLocation();
        listView.setAdapter(baseAdapter);

    }
}