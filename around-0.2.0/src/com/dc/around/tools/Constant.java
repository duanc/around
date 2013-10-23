package com.dc.around.tools;

import com.baidu.mapapi.map.LocationData;
import com.dc.around.model.Information;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-14
 * Time: 下午4:58
 * To change this template use File | Settings | File Templates.
 */
public class Constant {
    public static final String SINA_ACCESS_TOKEN = "2.00MeOHKEPOdZtD3add77b4914H_sjC";   //2.00MeOHKER5AgYDb6f3146eb7ErwDKD
    public static final String POI_GEO_URL = "https://api.weibo.com/2/location/pois/search/by_geo.json";
    public static final String GEO_ADDRESS_URL = "https://api.weibo.com/2/location/geo/geo_to_address.json";
    public static final String BAIDU_MAP_KEY = "9a22d5554ff44ea9dc51231f637b0948";
    public static final String LIST_SUFFIX = ".list";
    public static LocationData locData = new LocationData();
    public static Information information = new Information();
    public static List<Information> informationList;
}
