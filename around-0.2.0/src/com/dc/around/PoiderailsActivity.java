package com.dc.around;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-16
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.dc.around.model.Category;
import com.dc.around.tools.Constant;


public class PoiderailsActivity extends Activity {

    //浏览路线节点相关
    Button mBtnPre = null;//上一个节点
    Button mBtnNext = null;//下一个节点
    int nodeIndex = -2;//节点索引,供浏览节点时使用
    MKRoute route = null;//保存驾车/步行路线数据的变量，供浏览节点时使用
    TransitOverlay transitOverlay = null;//保存公交路线图层数据的变量，供浏览节点时使用
    RouteOverlay routeOverlay = null;
    boolean useDefaultIcon = false;
    int searchType = -1;//记录搜索的类型，区分驾车/步行和公交
    private PopupOverlay   pop  = null;//弹出泡泡图层，浏览节点时使用
    private TextView  popupText = null;//泡泡view
    private View viewCache = null;



    private MapView mapView;
   ImageButton backButton;
    TextView poiname;
    TextView poiaddress;
    TextView dstance;
    TextView  phone;
    MyLocationOverlay mylocationOverlay;
    private MapController mapController;

    MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用

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
        setContentView(R.layout.poidetails);
        DemoApplication app = (DemoApplication)this.getApplication();

        mapView = (MapView) findViewById(R.id.bmapView);

        mapView.setBuiltInZoomControls(true);
        //卫星图层
       // mapView.setSatellite(true);
        //交通图层
       // mapView.setTraffic(true);


        mapController = mapView.getController();
        //控制缩放等级
         mapController.setZoom(15);

        //移动到经纬度点
    //   final GeoPoint geoPoint = new GeoPoint((int) (Constant.locData.latitude * 1E6),(int) (Constant.locData.longitude * 1E6));
        //设置中心点
     //  mapController.setCenter(geoPoint);


        poiname = (TextView) findViewById(R.id.poiname);
        poiname.setText(Constant.information.getName());

        poiaddress = (TextView) findViewById(R.id.poiaddress);
        poiaddress.setText(Constant.information.getAddress());

        phone  =   (TextView) findViewById(R.id.phone);
        phone.setText(Constant.information.getTel());

        dstance = (TextView) findViewById(R.id.address1);                                                  //6000m 60min
        dstance.setText("距离"+Constant.information.getDistance()+"米,大约"+(int)(Double.parseDouble(Constant.information.getDistance())/100)+"分钟");

        //定位图层初始化
         mylocationOverlay = new MyLocationOverlay(mapView);
        //设置定位数据
    //    mylocationOverlay.setData(Constant.locData);
        //添加定位图层
      //  mapView.getOverlays().add(mylocationOverlay);
      //  mylocationOverlay.enableCompass();
        //修改定位数据后刷新图层生效
      //  mapView.removeAllViews();
        mapView.refresh();





        backButton = (ImageButton) findViewById(R.id.Poiderailsback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.destroy();
                PoiderailsActivity.this.finish();

       }
        });


        // 初始化搜索模块，注册事件监听
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){

            public void onGetDrivingRouteResult(MKDrivingRouteResult res,
                                                int error) {
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR){
//                    遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
//                    return;
                }
                // 错误号可参考MKEvent中的定义
                if (error != 0 || res == null) {
                    Toast.makeText(null , "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                //searchType = 0;
                routeOverlay = new RouteOverlay(PoiderailsActivity.this, mapView);
                // 此处仅展示一个方案作为示例
                routeOverlay.setData(res.getPlan(0).getRoute(0));
                //清除其他图层
                mapView.getOverlays().clear();
                //添加路线图层
                mapView.getOverlays().add(routeOverlay);
                //执行刷新使生效
                mapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                mapView.getController().animateTo(res.getStart().pt);
                //将路线数据保存给全局变量
                route = res.getPlan(0).getRoute(0);
                //重置路线节点索引，节点浏览时使用
//                nodeIndex = -1;
//                mBtnPre.setVisibility(View.VISIBLE);
//                mBtnNext.setVisibility(View.VISIBLE);
                mapView.refresh();
            }

            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
                                                int error) {
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR){
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                if (error != 0 || res == null) {
                    Toast.makeText(PoiderailsActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                searchType = 2;
                routeOverlay = new RouteOverlay(PoiderailsActivity.this, mapView);
                // 此处仅展示一个方案作为示例
                routeOverlay.setData(res.getPlan(0).getRoute(0));
                //清除其他图层
                mapView.getOverlays().clear();
                //添加路线图层
                mapView.getOverlays().add(routeOverlay);
                //执行刷新使生效
                mapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                mapView.getController().animateTo(res.getStart().pt);
                //将路线数据保存给全局变量
                route = res.getPlan(0).getRoute(0);
                //重置路线节点索引，节点浏览时使用
                nodeIndex = -1;
//                mBtnPre.setVisibility(View.VISIBLE);
//                mBtnNext.setVisibility(View.VISIBLE);

            }
            public void onGetAddrResult(MKAddrInfo res, int error) {
            }
            public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
            }

            @Override
            public void onGetTransitRouteResult(MKTransitRouteResult mkTransitRouteResult, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }

            @Override
            public void onGetPoiDetailSearchResult(int type, int iError) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult result, int type,
                                            int error) {
                // TODO Auto-generated method stub

            }
        });


        SearchButtonProcess();


        }

    void SearchButtonProcess() {
        //重置浏览节点的路线数据
        route = null;
        routeOverlay = null;
        transitOverlay = null;
//        mBtnPre.setVisibility(View.INVISIBLE);
//        mBtnNext.setVisibility(View.INVISIBLE);
        // 处理搜索按钮响应
//        EditText editSt = (EditText)findViewById(R.id.start);
//        EditText editEn = (EditText)findViewById(R.id.end);

        // 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
        MKPlanNode stNode = new MKPlanNode();
       // stNode.name = editSt.getText().toString();
        GeoPoint stGeoPoint = new GeoPoint((int) (Constant.locData.latitude*1E6),(int) (Constant.locData.longitude*1E6));
//        stGeoPoint.setLatitudeE6((int) (Constant.locData.latitude*1E6));
//        stGeoPoint.setLongitudeE6((int) (Constant.locData.longitude*1E6));
       stNode.pt = stGeoPoint;

        MKPlanNode enNode = new MKPlanNode();
        GeoPoint enGeoPoint = new GeoPoint((int) (Double.parseDouble(Constant.information.getY())*1E6),(int) (Double.parseDouble(Constant.information.getX())*1E6));
        enNode.pt = enGeoPoint;


     //   enNode.name = editEn.getText().toString();

        // 实际使用中请对起点终点城市进行正确的设定

          //  mSearch.drivingSearch("北京", stNode, "北京", enNode);

          //  mSearch.transitSearch("北京", stNode, enNode);


        mSearch.walkingSearch("西安", stNode, "西安", enNode);


        mapView.refresh();
        }

}

