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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang3.StringUtils;

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
    private Switch selectGroupSwitch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_wish, null);
        sharePreference = SharePreference.getInstance(activity);

        selectGroupSwitch = (Switch) view.findViewById(R.id.fragment_wish_switch_button);
        publish = (ImageView) view.findViewById(R.id.fragment_wish_add_img);
        wishListView = (ListView) view.findViewById(R.id.fragment_wish_listView);
        setWishListData();
        return view;
    }

    private void setWishListData() {
        loadAllWish();
        selectGroupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("check", "is checked");
                    loadAllWish();
                } else {
                    Log.e("check", "un check");
                    loadFriendWish();
                }
            }
        });
    }

    private void loadFriendWish() {
        Integer userId = sharePreference.getInt(CacheConfig.USER_ID);
        if (userId == 0) {
            return;
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("userId", String.valueOf(userId));
            StringCallback callBackWishList = new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("friendWishList", response);
                    JsonBaseList<WishCardContent> wishCardContentJsonBaseList = JSON.parseObject(
                            response, new TypeReference<JsonBaseList<WishCardContent>>() {
                            }.getType());
                    if (wishCardContentJsonBaseList.getCode() == 200
                            && wishCardContentJsonBaseList.getMsg().equals("success")) {
                        final List<WishCardContent> wishCardContentList = wishCardContentJsonBaseList.getData();
                        wishListView.setAdapter(new WishAdapter(activity, wishCardContentList));
                        wishListView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("wishCardId", wishCardContentList.get(position).getWishCardId() + "");
                                        bundle.putString("toUserId", wishCardContentList.get(position).getId() + "");
                                        JumpUtil.jumpInActivity(activity, WishDetailsActivity.class, bundle);
                                    }
                                });
                    }
                }
            };
            NetworkController.getMap(URLConfig.URL_FRIEND_WISH_ALL, map, callBackWishList);
        }
    }

    private void loadAllWish() {
        StringCallback callBackWishList = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("wishAllList", response);
                JsonBaseList<WishCardContent> wishCardContentJsonBaseList = JSON.parseObject(
                        response, new TypeReference<JsonBaseList<WishCardContent>>() {
                        }.getType());
                if (wishCardContentJsonBaseList.getCode() == 200
                        && wishCardContentJsonBaseList.getMsg().equals("success")) {
                    final List<WishCardContent> wishCardContentList = wishCardContentJsonBaseList.getData();
                    wishListView.setAdapter(new WishAdapter(activity, wishCardContentList));
                    wishListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("wishCardId", wishCardContentList.get(position).getWishCardId() + "");
                                    bundle.putString("toUserId", wishCardContentList.get(position).getId() + "");
                                    JumpUtil.jumpInActivity(activity, WishDetailsActivity.class, bundle);
                                }
                            });
                }
            }
        };
        NetworkController.getObject(URLConfig.URL_WISH_ALL, callBackWishList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    private String publishWishCard() {
        Integer userId = sharePreference.getInt(CacheConfig.USER_ID);
        if (userId == 0) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
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
                    //弹出提示让用户上传收钱码
                    popNote();

                } else if (alipayJsonBaseList.getCode() == 3) {
                    JumpUtil.jumpInActivity(activity, PublishWishActivity.class);
                }
            }
        };
        NetworkController.postMap(URLConfig.URL_ALIPAY_CHECK, map, alipayCheckCallBack);
        return null;
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
        switch (requestCode) {
            case PHOTO_FROM_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    String realPath = Environment.getExternalStorageDirectory().getPath()
                            + path.substring(path.indexOf("D") - 1, path.length());
                    //生成缩略图防止OOM 回显图片较小，上传的图片较大
                    bitmap = ImageTools.getImageThumbnail(realPath, 500, 500);
                    // 解析二维码信息
                    String result = QRHelper.getResult(bitmap);
                    String header = result.substring(0, 21);
                    if (header.equals("HTTPS://QR.ALIPAY.COM/")) {
                        receiveCode = result.substring(22, result.length());
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

    private String uploadReceiveCode() {
        Integer userId = sharePreference.getInt(CacheConfig.USER_ID);
        if (userId == 0) {
            return null;
        }
        if (StringUtils.isBlank(receiveCode)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
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
                }
            }
        };
        NetworkController.postMap(URLConfig.URL_ALIPAY_UPLOAD_RECEIVE_CODE,
                map, receiveCodeCallBack);
        return null;
    }


}
