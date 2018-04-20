package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.UserInfoJson;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class PersonalHomeActivity extends BaseActivity implements View.OnClickListener {
    private Activity activity;
    private RoundedImageView avatar;
    private TextView nickName, signature, phoneNumber, sex, area;
    private LinearLayout wishRecord;
    private Integer userId;
    private String avatarSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = PersonalHomeActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_home);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        userId = getIntent().getIntExtra("userId", 0);
        avatarSrc = getIntent().getStringExtra("avatarSrc");

        getViews();
        setUserData();
        setAvatarData();
    }

    public void getViews() {
        ImageView back = (ImageView) findViewById(R.id.fragment_personal_back_img);
        avatar = (RoundedImageView) findViewById(R.id.activity_personal_home_avatar_img);
        nickName = (TextView) findViewById(R.id.activity_personal_home_nickname_text);
        signature = (TextView) findViewById(R.id.activity_personal_home_signature_text);
        phoneNumber = (TextView) findViewById(R.id.activity_personal_home_phone_number_text);
        sex = (TextView) findViewById(R.id.activity_personal_home_sex_text);
        area = (TextView) findViewById(R.id.activity_personal_home_area_text);
        wishRecord = (LinearLayout) findViewById(R.id.activity_personal_home_wish_record_linear);

        back.setOnClickListener(this);
        avatar.setOnClickListener(this);
        wishRecord.setOnClickListener(this);
    }

    private String setUserData() {
        if (userId == 0) {
            return null;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", String.valueOf(userId));
        StringCallback userInfoCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("userInfo", response);
                JsonBaseList<UserInfoJson> userInfoJsonJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<UserInfoJson>>() {
                        }.getType());
                if (userInfoJsonJsonBaseList.getCode() == 200
                        && userInfoJsonJsonBaseList.getMsg().equals("success")) {
                    UserInfoJson userInfoJson = userInfoJsonJsonBaseList.getData().get(0);
                    nickName.setText(userInfoJson.getNickName());
                    phoneNumber.setText(userInfoJson.getTelephone());
                    if (!StringUtils.isBlank(userInfoJson.getSignature())
                            && !"null".equals(userInfoJson.getSignature())) {
                        signature.setText(userInfoJson.getSignature());
                    }
                    if (!StringUtils.isBlank(userInfoJson.getGender())
                            && !"null".equals(userInfoJson.getGender())) {
                        sex.setText(userInfoJson.getGender());
                    }
                    if (!StringUtils.isBlank(userInfoJson.getProvince())
                            && !"null".equals(userInfoJson.getProvince())) {
                        area.setText(userInfoJson.getProvince());
                    }
                }
            }
        };
        NetworkController.postMap(URLConfig.URL_USER_ALL_INFO,
                paramsMap, userInfoCallBack);
        return null;
    }

    private void setAvatarData() {
        if (StringUtils.isBlank(avatarSrc)
                || "null".equals(avatarSrc)) {
            avatar.setImageResource(R.drawable.avatar);
        } else {
            BitmapCallback callBackBitmap = new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    avatar.setImageResource(R.drawable.avatar);
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(Bitmap response, int id) {
                    avatar.setImageBitmap(response);
                }
            };
            NetworkController.getImage(avatarSrc, callBackBitmap);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_personal_back_img:
                finish();
                break;
            case R.id.activity_personal_home_avatar_img:
                Bundle bundle = new Bundle();
                if (!StringUtils.isBlank(avatarSrc)) {
                    bundle.putString("bigImageSrc", avatarSrc);
                }
                JumpUtil.jumpInActivity(activity, BigImageActivity.class, bundle);
                break;
            case R.id.activity_personal_home_wish_record_linear:
                Bundle bundleData = new Bundle();
                bundleData.putInt("userId", userId);
                JumpUtil.jumpInActivity(activity, UserAllWishActivity.class, bundleData);
                break;
            default:
                break;
        }
    }
}
