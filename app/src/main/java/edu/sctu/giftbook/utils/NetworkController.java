package edu.sctu.giftbook.utils;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Map;

import edu.sctu.giftbook.base.BaseApplication;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by zhengsenwen on 2018/3/13.
 */
public class NetworkController {

    private static BaseApplication app;

    public static void setApp(BaseApplication app) {
        NetworkController.app = app;
    }

    public static final String MEDIA_TYPE = URLConfig.MEDIA_TYPE;
    public static final String BASE_URL = URLConfig.BASE_URL;
    public static final String BASE_URL_IMAGE = URLConfig.BASE_URL_IMAGE;

    public static final String LOGIN = "";


    /**
     * 将参数转为Json字符串并post到服务器
     *
     * @param url      要发送请求的url
     * @param param    请求参数
     * @param callback 请求响应回调
     */
    public static void postMap(String url, Map param, StringCallback callback) {
        Log.d("postObject", "URL:" + BASE_URL + url + " ,param:" + JSON.toJSONString(param));
        PostFormBuilder builder = OkHttpUtils.post()
                .url(NetworkController.BASE_URL + url);
        for (Object key : param.keySet()) {
            builder.addParams(String.valueOf(key), String.valueOf(param.get(key)));
        }
        builder.build().execute(callback);
    }


    /**
     * 带参数上传文件
     *
     * @param url
     * @param params
     * @param fileHashMap
     * @param callback
     */
    public static void postFile(String url, Map<String, String> params, Map<String, File> fileHashMap, StringCallback callback) {
        Log.e("postObject", "URL:" + BASE_URL + url + " ,param:" + JSON.toJSONString(params));
        OkHttpUtils.post()
                .files("file", fileHashMap)
                .url(NetworkController.BASE_URL + url)
                .params(params)
                .build()
                .execute(callback);
    }

    /**
     * 无参数的get请求方式去获取网络数据
     *
     * @param url
     * @param callback
     */
    public static void getObject(String url, StringCallback callback) {
        Log.d("getObject", "URL:" + BASE_URL + url);
        OkHttpUtils.get()
                .url(BASE_URL + url)
                .build()
                .execute(callback);
    }

    /**
     * 将参数转为Json字符串并get到服务器
     *
     * @param url      要发送请求的url
     * @param paramsMap    请求参数
     * @param callback 请求响应回调
     */
    public static void getMap(String url, Map<String, String> paramsMap, StringCallback callback) {
        Log.d("getObject", "URL:" + BASE_URL + url + " ,param:" + JSON.toJSONString(paramsMap));
        OkHttpUtils.get()
                .url(NetworkController.BASE_URL + url)
                .params(paramsMap)
                .build()
                .execute(callback);
    }

    /**
     * 获取图片
     *
     * @param url      要发送请求的url
     * @param callback 请求响应回调
     */
    public static void getImage(String url, BitmapCallback callback) {
        Log.e("getImage", "URL:" + url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);

    }




    /**
     * 将参数转为Json字符串并post到服务器
     *
     * @param url      要发送请求的url
     * @param param    请求参数
     * @param callback 请求响应回调
     */
    public static void postObject(String url, Object param, StringCallback callback) {
        Log.e("postObject", "URL:" + BASE_URL + url + " ,param:" + JSON.toJSONString(param));
        OkHttpUtils.postString()
                .url(NetworkController.BASE_URL + url)
                .mediaType(MediaType.parse(MEDIA_TYPE))
                .content(JSON.toJSONString(param))
                .build()
                .execute(callback);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @param fileHashMap
     * @param callback
     */
    public static void postFiles(String url, Map<String, String> params, Map<String, File> fileHashMap, StringCallback callback) {
//         Log.d("postObject", "URL:" + BASE_URL + url + " ,param:" + JSON.toJSONString(param));
        OkHttpUtils.post()
                .files("files", fileHashMap)
                .url(NetworkController.BASE_URL + url)
                .params(params)
                .build()
                .execute(callback);
    }





    /**
     * post Json格式的字符串参数到服务器
     *
     * @param url      要发送请求的url
     * @param json     请求参数
     * @param callback 请求响应回调
     */
    private static RequestCall postJSONString(String url, @Nullable String json, StringCallback callback) {
        Log.d("postObject", "URL:" + BASE_URL + url + " ,param:" + json);
        RequestCall requestCall = OkHttpUtils.postString()
                .url(NetworkController.BASE_URL + url)
                .mediaType(MediaType.parse(MEDIA_TYPE))
                .content(json == null ? "{}" : json)
                .build();
        requestCall.execute(callback);
        return requestCall;
    }

    /**
     * 无参数的get请求方式去加载图片
     *
     * @param url
     */
    public static void setImage(final String url, final ImageView mImageView, final int errorImage) {
        Log.d("getObject", "imgURL:" + url);
//        String BitmapName = url.substring(0, url.lastIndexOf("."));
//        Bitmap bitmap = BitmapUtil.getBitmap(BitmapName);
//        if (bitmap != null) {
//            mImageView.setImageBitmap(bitmap);
//        }else {
//            System.out.println("请求网络"+url);
        try {
            OkHttpUtils
                    .get()
                    .url(BASE_URL_IMAGE + url)
                    .build()
                    .execute(new BitmapCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            mImageView.setImageResource(errorImage);
                        }

                        @Override
                        public void onResponse(Bitmap response, int id) {

                            mImageView.setImageBitmap(response);
                            //判断外部储存是否连接正常，结果为true时存储bitmap
//                            if ( BitmapUtil.isTure()){
//                                BitmapUtil.saveBitmap(url.substring(0, url.lastIndexOf(".")), response);
//                            }

                        }

                    });
        } catch (Exception e) {
            System.out.println(e);
        }

    }


//    }

    /**
     * 下载文件
     *
     * @param url      要发送请求的url
     * @param callback 请求响应回调
     */
    public static void getFile(String url, StringCallback callback, String editionName) {
        OkHttpUtils//
                .get()//
                .url(BASE_URL + url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), editionName)//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }
                });
    }


}
