package edu.sctu.giftbook.utils;

/**
 * Created by zhengsenwen on 2018/3/13.
 */

public class URLConfig {

    public static final String MEDIA_TYPE = "applicaton/json; charset=utf-8";
    public static final String BASE_URL = "http://58.87.93.34:8099/api";  //服务器

//    public static final String BASE_URL = "http://192.168.1.17:8099/api"; //寝室
//
//    public static final String BASE_URL = "http://192.168.1.14:8099/api"; //办公室


//    public static final String BASE_URL = "http://192.168.43.101:8099/api"; //手机临时

//    public static final String BASE_URL = "http://172.20.10.8:8099/api"; //贺鹏临时

    public static final String BASE_URL_IMAGE = "http://58.87.93.34:8099";

    public static final String URL_USER_REGISTER = "/user/register";
    public static final String URL_USER_AREA_DATA = "/user/area";
    public static final String URL_USER_LOGIN = "/user/login";
    public static final String URL_USER_UPDATE_AVATAR = "/user/update/avatar";
    public static final String URL_USER_UPDATE_INFO = "/user/update/info";
    public static final String URL_USER_PASSWORD = "/user/password";
    public static final String URL_USER_UPDATE_PASSWORD = "/user/update/password";
    public static final String URL_USER_ALL_INFO = "/user/all/info";


    public static final String URL_WISH_PUBLISH_TYPE = "/wish/publish/type";
    public static final String URL_WISH_PUBLISH = "/wish/publish/one/wishCard";
    public static final String URL_WISH_ALL = "/wish/all";
    public static final String URL_FRIEND_WISH_ALL = "/wish/friend";
    public static final String URL_USER_WISH_RECORD = "/wish/user/";//wish/user/{userId}
    public static final String URL_WISH_ONE = "/wish/"; // wish/{wishCardId}


    public static final String URL_ALIPAY_CHECK = "/alipay/check/";
    public static final String URL_ALIPAY_UPLOAD_RECEIVE_CODE = "/alipay/upload/receive/code/";


    public static final String URL_COMMENT_WISHCARD_DETAILS = "/comment/wish/card/details";
    public static final String URL_COMMENT_PUBLISH = "/comment/publish";

    public static final String URL_FRIEND_ALL_PHONE = "/friend/all/phone";
    public static final String URL_FRIEND_CONTACT_FRIEND = "/friend/contact/friend";
    public static final String URL_FELLOW_FRIEND = "/friend/fellow";
    public static final String URL_ALL_FELLOW_FRIEND = "/friend/all/fellow";


}