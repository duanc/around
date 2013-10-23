package com.dc.around;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-16
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.dc.around.Manage.Manage;
import com.dc.around.model.Information;
import com.dc.around.tools.Constant;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class PoiActivity extends Activity {

    private static final String[] fanweis = {"范围1000m以内", "范围2000m以内", "范围3000m以内", "范围4000m以内", "范围5000m以内"};
    int fanwei = 3000;
    private TextView textView;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    Button button;
    ImageButton backButton;
    ImageButton  refreshButton;
    ImageButton pre_stepButton;
    ImageButton next_stepButton;
    ImageButton locationButton;



    ListView listView;
    private PoiOverlay<OverlayItem> itemItemizedOverlay;
    String name;
    int mappage;
    BaseAdapter baseAdapter;
    private MapView mapView;
    LocationData locData = new LocationData();
    private LayoutInflater layoutInflater;
    private ItemizedOverlay myLocationOverlay;
    MyLocationOverlay mylocationOverlay;
    LinearLayout linearLayout;
    private MapController mapController;
    private View mapPopWindow;
    private ProgressDialog progressDialog;
    List<Information> informationList;


    @Override
    protected void onPause() {
     //   ((DemoApplication)this.getApplication()).mBMapManager.stop();
      mapView.onPause();

        super.onPause();
    }
    @Override
    protected void onResume() {
        mapView.onResume();
       // ((DemoApplication)this.getApplication()).mBMapManager.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapView.destroy();
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poilist);

        //创建地图
        linearLayout = (LinearLayout) findViewById(R.id.bmapViewLinearLayout);
        mapView = (MapView) findViewById(R.id.bmapView);
        listView = (ListView) findViewById(R.id.poiListView);
        linearLayout.setVisibility(View.GONE);
        mappage =1;
//        Drawable marker = getResources().getDrawable(R.drawable.icon_marka);
//
//        itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);
//        myLocationOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.icon_markc), mapView);
        mapView.getOverlays().add(myLocationOverlay);
        mapController = mapView.getController();
        mapController.setZoom(17);
       // 移动到经纬度点
          final GeoPoint geoPoint = new GeoPoint((int) (Constant.locData.latitude * 1E6),(int) (Constant.locData.longitude * 1E6));
       // 设置中心点
           mapController.setCenter(geoPoint);

//        for (int i = 0; i <  informationList.size(); i++) {
//            GeoPoint point = new GeoPoint((int)( Double.parseDouble(informationList.get
//                    (i).getX())*1E6),(int) (Double.parseDouble(informationList.get(i).getY())*1E6));
//            OverlayItem overlayItem = new OverlayItem(point, informationList.get(i).getName
//                    () ,informationList.get(i).getAddress());
//            itemItemizedOverlay.addItem(overlayItem);
//        }
//        mapView.getOverlays().add(itemItemizedOverlay);
//        layoutInflater = LayoutInflater.from(this);
//        //添加弹出窗口
//        mapPopWindow = layoutInflater.inflate(R.layout.map_pop_window, null);
//        mapPopWindow.setVisibility(View.GONE);
//        mapView.addView(mapPopWindow);
//
////        locateMeButton = (Button) findViewById(R.id.locateMeButton);
////        locateMeButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                getMyLocation();
////            }
////        });
//
//        myLocationOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.icon_markc), mapView);
//        mapView.getOverlays().add(myLocationOverlay);







        // itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker,mapView );





        listView = (ListView) findViewById(R.id.poiListView);

    //添加按钮事件
        //f返回按钮
        backButton = (ImageButton) findViewById(R.id.poiback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PoiActivity.this.finish();
            }
        });
        // 下一页poi点
           pre_stepButton = (ImageButton) findViewById(R.id.next_step);
        pre_stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mappage++;
                mapView.getOverlays().remove(itemItemizedOverlay);
                progressDialog = ProgressDialog.show(PoiActivity.this, "Loading...", "正在加载中...", true, false);
                final Manage manage =  new Manage();

                AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        try {

                            Constant.informationList =  informationList = manage.getInformationList(name, Constant.locData.longitude +"",  Constant.locData.latitude+"", fanwei + "", mappage+"");

//                    if(informationList==null){
//                        Toast.makeText(PoiActivity.this,"没有找到", Toast.LENGTH_LONG).show();
//
//                    }

                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (JSONException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        if(informationList==null){
                            progressDialog.dismiss();
                            Toast.makeText(PoiActivity.this,"已经是最后一页了", Toast.LENGTH_LONG).show();
                            return;
                        }




                        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
                        itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);
                        myLocationOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.icon_markc), mapView);
                        mapView.getOverlays().add(myLocationOverlay);

                        for (int i = 0; i <  Constant.informationList.size(); i++) {
                            GeoPoint point = new GeoPoint((int)( Double.parseDouble( Constant.informationList.get
                                    (i).getY())*1E6),(int) (Double.parseDouble( Constant.informationList.get(i).getX())*1E6));
                            //  GeoPoint point = new GeoPoint((int)(Constant.locData.latitude*1E6),(int)(Constant.locData.longitude*1E6));
                            Log.e("point"+":"+point.toString(),"");
                            OverlayItem overlayItem = new OverlayItem(point,  Constant.informationList.get(i).getName
                                    () , Constant.informationList.get(i).getAddress());
                            //   OverlayItem overlayItem = new OverlayItem(point, "" , "");
                            itemItemizedOverlay.addItem(overlayItem);
                        }
                        // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                        mapView.getController().zoomToSpan(itemItemizedOverlay.getLatSpanE6(), itemItemizedOverlay.getLonSpanE6());

                        mapView.getOverlays().add(itemItemizedOverlay);

                        layoutInflater = LayoutInflater.from(PoiActivity.this);
                        //添加弹出窗口
                        mapPopWindow = layoutInflater.inflate(R.layout.map_pop_window, null);
                        mapPopWindow.setVisibility(View.GONE);
                        mapView.addView(mapPopWindow);

                        progressDialog.dismiss();


                        myLocationOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.icon_markc), mapView);
                        mapView.getOverlays().add(myLocationOverlay);



                    }


                };


                asyncTask.execute(null);

            }
        });
        //上一页poi点
        next_stepButton = (ImageButton) findViewById(R.id.pre_step);
        next_stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mappage<2){
                    Toast.makeText(PoiActivity.this,"没有上一页了", Toast.LENGTH_SHORT).show();
                     return;

                }
                progressDialog = ProgressDialog.show(PoiActivity.this, "Loading...", "正在加载上一页...", true, false);
                mappage--;
                mapView.getOverlays().remove(itemItemizedOverlay);
                final Manage manage =  new Manage();
                AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        try {

                            Constant.informationList =  informationList = manage.getInformationList(name, Constant.locData.longitude +"",  Constant.locData.latitude+"", fanwei + "", mappage+"");

//                    if(informationList==null){
//                        Toast.makeText(PoiActivity.this,"没有找到", Toast.LENGTH_LONG).show();
//
//                    }

                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (JSONException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        if(informationList==null){
                            Toast.makeText(PoiActivity.this,"没有找到您要的信息", Toast.LENGTH_LONG).show();
                            return;
                        }




                        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
                        itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);
                        myLocationOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.icon_markc), mapView);
                        mapView.getOverlays().add(myLocationOverlay);

                        for (int i = 0; i <  Constant.informationList.size(); i++) {
                            GeoPoint point = new GeoPoint((int)( Double.parseDouble( Constant.informationList.get
                                    (i).getY())*1E6),(int) (Double.parseDouble( Constant.informationList.get(i).getX())*1E6));
                            //  GeoPoint point = new GeoPoint((int)(Constant.locData.latitude*1E6),(int)(Constant.locData.longitude*1E6));
                            Log.e("point"+":"+point.toString(),"");
                            OverlayItem overlayItem = new OverlayItem(point,  Constant.informationList.get(i).getName
                                    () , Constant.informationList.get(i).getAddress());
                            //   OverlayItem overlayItem = new OverlayItem(point, "" , "");
                            itemItemizedOverlay.addItem(overlayItem);
                        }
                        // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                        mapView.getController().zoomToSpan(itemItemizedOverlay.getLatSpanE6(), itemItemizedOverlay.getLonSpanE6());

                        mapView.getOverlays().add(itemItemizedOverlay);

                        layoutInflater = LayoutInflater.from(PoiActivity.this);
                        //添加弹出窗口
                        mapPopWindow = layoutInflater.inflate(R.layout.map_pop_window, null);
                        mapPopWindow.setVisibility(View.GONE);
                        mapView.addView(mapPopWindow);
                        progressDialog.dismiss();



                        myLocationOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.icon_markc), mapView);
                        mapView.getOverlays().add(myLocationOverlay);



                    }


                };


                asyncTask.execute(null);

            }
        });

        //我的坐标
        locationButton = (ImageButton) findViewById(R.id.location);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(PoiActivity.this, "Loading...", "正在定位...", true, false);
//            //    Drawable marker = getResources().getDrawable(R.drawable.ic_localbar_location);
//                //定位图层初始化
//                mylocationOverlay = new MyLocationOverlay(mapView);
//                //设置定位数据
//                mylocationOverlay.setData(Constant.locData);
//                //添加定位图层
//             //   GeoPoint locationGeoPoint = new GeoPoint((int)mylocationOverlay.getMyLocation().latitude,(int) mylocationOverlay.getMyLocation().longitude);
//              //  mapController.setCenter(locationGeoPoint);
//                mapView.getOverlays().add(mylocationOverlay);
//                mapView.refresh();
                getMyLocation();

            }
        });


        refreshButton = (ImageButton) findViewById(R.id.listmap);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView = (ListView) findViewById(R.id.poiListView);
                linearLayout = (LinearLayout) findViewById(R.id.bmapViewLinearLayout);

                if(listView.getVisibility()!=View.GONE){
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    final Manage manage =  new Manage();
                    AsyncTask asyncTask = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object... objects) {
                            try {

                                Constant.informationList =  informationList = manage.getInformationList(name, Constant.locData.longitude +"",  Constant.locData.latitude+"", fanwei + "", "1");

//                    if(informationList==null){
//                        Toast.makeText(PoiActivity.this,"没有找到", Toast.LENGTH_LONG).show();
//
//                    }

                            } catch (IOException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (JSONException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            if(informationList==null){
                                Toast.makeText(PoiActivity.this,"没有找到您要的信息", Toast.LENGTH_LONG).show();
                                return;
                            }




                            Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
                            itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);
                            myLocationOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.icon_markc), mapView);
                            mapView.getOverlays().add(myLocationOverlay);

                            for (int i = 0; i <  Constant.informationList.size(); i++) {
                                GeoPoint point = new GeoPoint((int)( Double.parseDouble( Constant.informationList.get
                                        (i).getY())*1E6),(int) (Double.parseDouble( Constant.informationList.get(i).getX())*1E6));
                                //  GeoPoint point = new GeoPoint((int)(Constant.locData.latitude*1E6),(int)(Constant.locData.longitude*1E6));
                                   Log.e("point"+":"+point.toString(),"");
                                OverlayItem overlayItem = new OverlayItem(point,  Constant.informationList.get(i).getName
                                        () , Constant.informationList.get(i).getAddress());
                                //   OverlayItem overlayItem = new OverlayItem(point, "" , "");
                                itemItemizedOverlay.addItem(overlayItem);
                            }
                            // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                            mapView.getController().zoomToSpan(itemItemizedOverlay.getLatSpanE6(), itemItemizedOverlay.getLonSpanE6());

                            mapView.getOverlays().add(itemItemizedOverlay);

                            layoutInflater = LayoutInflater.from(PoiActivity.this);
                            //添加弹出窗口
                            mapPopWindow = layoutInflater.inflate(R.layout.map_pop_window, null);
                            mapPopWindow.setVisibility(View.GONE);
                            mapView.addView(mapPopWindow);



                            myLocationOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.icon_markc), mapView);
                            mapView.getOverlays().add(myLocationOverlay);



                        }


                    };


                    asyncTask.execute(null);

                } else{
                    linearLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }





            }


        });

        Intent intent = getIntent();
         name = intent.getStringExtra("name");
        String code = intent.getStringExtra("code");
        textView = (TextView) findViewById(R.id.poititle);
        textView.setText(name);
        button = (Button) findViewById(R.id.fanwei);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog alertDialog = new AlertDialog.Builder(PoiActivity.this).setItems(fanweis, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String msg = fanweis[i];
                        button.setText(msg);
                        fanwei = (i + 1) * 1000;
                        getasyncTask().execute(null);

                    }
                }).show();

            }
        });

        getasyncTask().execute(null);



    }


    public AsyncTask getasyncTask(){
        final Manage manage =  new Manage();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object... objects) {
                try {

                     informationList = manage.getInformationList(name, Constant.locData.longitude +"",  Constant.locData.latitude+"", fanwei + "", "1");

//                    if(informationList==null){
//                        Toast.makeText(PoiActivity.this,"没有找到", Toast.LENGTH_LONG).show();
//
//                    }

                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
              return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                     if(informationList==null){
                        Toast.makeText(PoiActivity.this,"没有找到您要的信息", Toast.LENGTH_LONG).show();
                    return;
                    }


                listView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return informationList.size();  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public Object getItem(int position) {
                        return informationList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;  //To change body of implemented methods use File | Settings | File Templates.
                    }


                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {

                        LayoutInflater layoutInflater = getLayoutInflater();
                        convertView = layoutInflater.inflate(R.layout.informationlist, parent, false);

//                if (convertView == null) {
//                    LayoutInflater layoutInflater = getLayoutInflater();
//                    convertView = layoutInflater.inflate(R.layout.categorylist, parent, false);
//                }

                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            Intent intent = new Intent(PoiActivity.this, PoiderailsActivity.class);
                               Constant.information =  informationList.get(position);
                            startActivity(intent);

                            }
                        });
                        TextView nameTextView = (TextView) convertView.findViewById(R.id.textname);
                        TextView addressTextView = (TextView) convertView.findViewById(R.id.textaddress);
                        TextView distanceTextView = (TextView) convertView.findViewById(R.id.distance);


                        nameTextView.setText(informationList.get(position).getName());
                        addressTextView.setText(informationList.get(position).getAddress());
                        distanceTextView.setText(informationList.get(position).getDistance() + "米");


                        return convertView;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                });


            }


        };
        return asyncTask;
    }
    class PoiOverlay<OverlayItem> extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d("BaiduMapDemo", "onTap " + i);
            com.baidu.mapapi.map.OverlayItem item = itemItemizedOverlay.getItem(i);
            GeoPoint point = item.getPoint();
            String title = item.getTitle();
            String content = item.getSnippet();

            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.titleTextView);
            TextView contentTextView = (TextView) mapPopWindow.findViewById(R.id.contentTextView);
            titleTextView.setText(title);
            contentTextView.setText(content);
            contentTextView.setVisibility(View.VISIBLE);

            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -50,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);

            mapPopWindow.setVisibility(View.VISIBLE);

            mapPopWindow.setLayoutParams(layoutParam);

            mapController.animateTo(point);

            return super.onTap(i);
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Log.d("BaiduMapDemo", "onTap geoPoint " + geoPoint);

            mapPopWindow.setVisibility(View.GONE);

            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
        }


    }
    private void getMyLocation() {
        Log.d("BaiduMapDemo", "getMyLocation");

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);

        LocationClient locationClient = new LocationClient(this);
        locationClient.setLocOption(option);

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
                           if(mylocationOverlay!=null){
                               mapView.getOverlays().remove(mylocationOverlay);
                           }

                //扎点
                mylocationOverlay = new MyLocationOverlay(mapView);
                //设置定位数据
                mylocationOverlay.setData(locData);

                //添加定位图层
                GeoPoint locationGeoPoint = new GeoPoint((int)(locData.latitude*1E6),(int)(locData.longitude*1E6));
                mapController.animateTo(locationGeoPoint);

                mapView.getOverlays().add(mylocationOverlay);
                mapView.refresh();
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

}

