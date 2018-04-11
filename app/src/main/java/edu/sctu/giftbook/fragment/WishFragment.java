package edu.sctu.giftbook.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.PublishWishActivity;
import edu.sctu.giftbook.activity.WishDetailsActivity;
import edu.sctu.giftbook.adapter.WishAdapter;
import edu.sctu.giftbook.entity.Alipay;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.WishCardContent;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.ImageTools;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.QRHelper;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class WishFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private ListView wishListView;
    private ImageView publish;
    private SharePreference sharePreference;

    private static final int PHOTO_FROM_GALLERY = 1;
    private Bitmap bitmap;
    private String receiveCode;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_wish, null);

        sharePreference = SharePreference.getInstance(activity);
        getViews(view);
        setWishListData();

        return view;
    }


    public void getViews(View view) {
        publish = (ImageView) view.findViewById(R.id.fragment_wish_add_img);
        wishListView = (ListView) view.findViewById(R.id.fragment_wish_listView);
    }

    private void setWishListData() {
        StringCallback callBackWishList = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("wishAllList", response);
                JsonBaseList<WishCardContent> wishCardContentJsonBaseList
                        = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<WishCardContent>>() {
                        }.getType());
                if (wishCardContentJsonBaseList.getCode() == 200
                        && wishCardContentJsonBaseList.getMsg().equals("success")) {
                    final List<WishCardContent> wishCardContentList
                            = wishCardContentJsonBaseList.getData();
                    LayoutInflater layoutInflater = LayoutInflater.from(activity);
                    wishListView.setAdapter(new WishAdapter(layoutInflater,
                            activity, wishCardContentList));
                    wishListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    Log.e("wishCardId", wishCardContentList.get(position)
                                            .getWishCardId() + "");
                                    Bundle bundle = new Bundle();
                                    bundle.putString("wishCardId", wishCardContentList.get(position).getWishCardId() + "");
                                    bundle.putString("fromUserId", wishCardContentList.get(position).getId() + "");
                                    JumpUtil.jumpInActivity(activity, WishDetailsActivity.class, bundle);
                                }
                            });
                } else {
                    Log.e("someError", wishCardContentJsonBaseList.getCode()
                            + wishCardContentJsonBaseList.getMsg());
                }
            }
        };
        NetworkController.getObject(URLConfig.URL_WISH_ALL, callBackWishList);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnClick();
    }

    private void setOnClick() {
        publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fragment_wish_add_img:
                publishWishCard();
                break;
            default:
                break;
        }
    }

    private void publishWishCard() {
        Map<String, String> map = new HashMap<>();
        int userId = sharePreference.getInt(CacheConfig.USER_ID);
        map.put("userId", String.valueOf(userId));

        StringCallback alipayCheckCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("alipay", response);
                JsonBaseList<Alipay> alipayJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<Alipay>>() {
                        }.getType());
                if (alipayJsonBaseList.getCode() == 200
                        && alipayJsonBaseList.getMsg().equals("success")) {
                    //请求成功说明用户没有上传过收钱码
                    popNote();
                } else if (alipayJsonBaseList.getCode() == 3) {
                    JumpUtil.jumpInActivity(activity, PublishWishActivity.class);
                } else {
                    Log.e("someError", alipayJsonBaseList.getCode() + alipayJsonBaseList.getMsg());
                }

            }
        };
        NetworkController.postMap(URLConfig.URL_ALIPAY_CHECK, map, alipayCheckCallBack);
    }


    private void popNote() {
        final String[] items = {"稍后再来", "相册里有，去上传"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(activity);
        listDialog.setTitle("收赞助需提供支付宝收钱码");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1:
                        getReceiveCodeFromAlbum();
                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void getReceiveCodeFromAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PHOTO_FROM_GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("now", "is here");
        switch (requestCode) {
            case PHOTO_FROM_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    Log.e("uri", uri.toString() + "  " + uri.getPath());
                    String path = uri.getPath();
                    String realPath = Environment.getExternalStorageDirectory().getPath()
                            + path.substring(path.indexOf("D") - 1, path.length());
                    //生成缩略图防止OOM 回显图片较小，上传的图片较大
                    bitmap = ImageTools.getImageThumbnail(realPath, 500, 500);
                    // TODO: 2018/4/3  解析图片信息

                    String result = QRHelper.getResult(bitmap);
                    String header = result.substring(0, 21);
                    if (header.equals("HTTPS://QR.ALIPAY.COM/")) {
                        receiveCode = result.substring(22, result.length());
                        Log.e("result", result + requestCode);
                        uploadReceiveCode();
                    } else {
                        ToastUtil.makeText(activity, R.string.wish_choose_right_receive_code);
                    }
                } else {
                    ToastUtil.makeText(activity, R.string.get_image_failed);
                }
                break;
            default:
                break;
        }
    }

    private void uploadReceiveCode() {
        Map<String, String> map = new HashMap<>();
        int userId = sharePreference.getInt(CacheConfig.USER_ID);
        map.put("userId", String.valueOf(userId));
        map.put("receiveCode", receiveCode);

        StringCallback receiveCodeCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("alipay", response);
                JsonBaseList<Alipay> alipayJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<Alipay>>() {
                        }.getType());
                if (alipayJsonBaseList.getCode() == 200
                        && alipayJsonBaseList.getMsg().equals("success")) {
                    Alipay alipay = alipayJsonBaseList.getData().get(0);
                    sharePreference.setCache(CacheConfig.CACHE_ALIPAY_RECEIVE_CODE,
                            alipay.getReceiveCode());
                    JumpUtil.jumpInActivity(activity, PublishWishActivity.class);
                } else {
                    Log.e("someError", alipayJsonBaseList.getCode() + alipayJsonBaseList.getMsg());
                }
            }
        };
        NetworkController.postMap(URLConfig.URL_ALIPAY_UPLOAD_RECEIVE_CODE, map, receiveCodeCallBack);
    }


}
