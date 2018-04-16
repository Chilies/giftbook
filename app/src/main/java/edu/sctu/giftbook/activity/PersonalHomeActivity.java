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

import java.util.HashMap;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.UserInfoJson;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class PersonalHomeActivity extends Activity implements View.OnClickListener {
    private Activity activity;
    private RoundedImageView avatar;
    private TextView nickName, signature, phoneNumber, sex, area;
    private LinearLayout wishRecord;
    private int userId;
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
        back.setOnClickListener(this);

        avatar = (RoundedImageView) findViewById(R.id.activity_personal_home_avatar_img);
        nickName = (TextView) findViewById(R.id.activity_personal_home_nickname_text);
        signature = (TextView) findViewById(R.id.activity_personal_home_signature_text);
        phoneNumber = (TextView) findViewById(R.id.activity_personal_home_phone_number_text);
        sex = (TextView) findViewById(R.id.activity_personal_home_sex_text);
        area = (TextView) findViewById(R.id.activity_personal_home_area_text);

        wishRecord = (LinearLayout) findViewById(R.id.activity_personal_home_wish_record_linear);
        avatar.setOnClickListener(this);
        wishRecord.setOnClickListener(this);

    }

    private void setUserData() {
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

                    if ((userInfoJson.getSignature() == null)
                            || ("null".equals(userInfoJson.getSignature()))
                            || "".equals(userInfoJson.getSignature())) {

                    } else {
                        signature.setText(userInfoJson.getSignature());
                    }
                    if ((userInfoJson.getGender() == null)
                            || ("null".equals(userInfoJson.getGender()))
                            || "".equals(userInfoJson.getGender())) {

                    } else {
                        sex.setText(userInfoJson.getGender());
                    }
                    if ((userInfoJson.getProvince() == null)
                            || ("null".equals(userInfoJson.getProvince()))
                            || "".equals(userInfoJson.getProvince())) {

                    } else {
                        area.setText(userInfoJson.getProvince());
                    }
                } else {
                    Log.e("someError", userInfoJsonJsonBaseList.getCode() + userInfoJsonJsonBaseList.getMsg());
                }
            }
        };
        NetworkController.postMap(URLConfig.URL_USER_ALL_INFO,
                paramsMap, userInfoCallBack);
    }

    private void setAvatarData() {
        if (avatarSrc != null
                && !"".equals(avatarSrc)
                && !"null".equals(avatarSrc)) {
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
            Log.e("avatarSrc", avatarSrc);
            NetworkController.getImage(avatarSrc, callBackBitmap);
        } else {
            avatar.setImageResource(R.drawable.avatar);
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
                Log.e("bigImageSrc", avatarSrc);
                if (avatarSrc != null && !"".equals(avatarSrc)) {
                    bundle.putString("bigImageSrc", avatarSrc);
                }
                JumpUtil.jumpInActivity(activity, BigImageActivity.class, bundle);
                break;
            case R.id.activity_personal_home_wish_record_linear:
                break;
            default:
                break;
        }
    }
}
