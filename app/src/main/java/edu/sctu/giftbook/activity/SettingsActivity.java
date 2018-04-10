package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.MainActivity;
import edu.sctu.giftbook.R;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.UserJson;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/22.
 */

public class SettingsActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private LinearLayout updateInfo, updatePassword, logout;
    private LayoutInflater layoutInflater;
    private SharePreference sharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = SettingsActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        sharePreference = SharePreference.getInstance(activity);

        getViews();

    }


    public void getViews() {
        TextView title = (TextView) findViewById(R.id.article_title);
        title.setText("设置");
        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);

        updateInfo = (LinearLayout) findViewById(R.id.activity_settings_update_userInfo_learLayout);
        updatePassword = (LinearLayout) findViewById(R.id.activity_settings_update_password_learLayout);
        logout = (LinearLayout) findViewById(R.id.activity_settings_logout);

        updateInfo.setOnClickListener(this);
        updatePassword.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_settings_update_userInfo_learLayout:
                JumpUtil.jumpInActivity(activity, UserInfoUpdateActivity.class);
                break;
            case R.id.activity_settings_update_password_learLayout:
                setPassword();
                break;
            case R.id.activity_settings_logout:
                ToastUtil.makeText(activity, "正在开发中");
                break;
            case R.id.back_img:
                finish();
                break;
            default:
                break;
        }
    }

    private void setPassword() {
        final EditText editText = new EditText(activity);
        final AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(activity);
        inputDialog.setMessage("请输入原密码").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldPassword = editText.getText().toString();
                        Log.e("passwordccc", oldPassword);
                        confirmOldPassword(oldPassword);
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        inputDialog.show();
    }

    private void confirmOldPassword(final String oldPassword) {
        int userId = sharePreference.getInt(CacheConfig.USER_ID);
        Map<String, String> map = new HashMap<String, String>();
        Log.e("passwordttt", userId + "");
        map.put("userId", String.valueOf(userId));

        StringCallback passwordCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("password", response);
                JsonBaseList<String> stringJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<String>>() {
                        }.getType());
                if (stringJsonBaseList.getCode() == 200
                        && stringJsonBaseList.getMsg().equals("success")) {
                    String truePassword = stringJsonBaseList.getData().get(0);
                    Log.e("password1", oldPassword + truePassword);
                    if (truePassword.equals(oldPassword)) {
                        //如果密码正确就设置新密码
                        setNewPassword();
                    }
                } else {
                    ToastUtil.makeText(activity, R.string.password_error);
                    Log.e("someError", stringJsonBaseList.getCode() + stringJsonBaseList.getMsg());
                }
            }
        };
        NetworkController.getMap(URLConfig.URL_USER_PASSWORD,
                map, passwordCallBack);
    }

    private void setNewPassword() {
        final EditText newPasswordEidtText = new EditText(activity);
        final AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(activity);
        inputDialog.setMessage("请设定新密码").setView(newPasswordEidtText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = newPasswordEidtText.getText().toString();
                        Log.e("password0", newPassword);
                        updatePassword(newPassword);
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        inputDialog.show();
    }

    private void updatePassword(final String newPassword) {
        int userId = sharePreference.getInt(CacheConfig.USER_ID);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", String.valueOf(userId));
        Log.e("password3", userId + newPassword);
        paramsMap.put("password", newPassword);
        StringCallback newPasswordCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("password", response);
                JsonBaseList<UserJson> userJsonJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<UserJson>>() {
                        }.getType());
                if (userJsonJsonBaseList.getCode() == 200
                        && userJsonJsonBaseList.getMsg().equals("success")) {
                    UserJson userJson = userJsonJsonBaseList.getData().get(0);
                    Log.e("password3", newPassword + userJson.getPwd());
                    if (userJson.getPwd().equals(newPassword)) {
                        ToastUtil.makeText(activity, R.string.update_password_success);
                    } else {
                        ToastUtil.makeText(activity, R.string.update_password_failed);
                        Log.e("password", userJson.getPwd() + newPassword);
                    }
                } else {
                    Log.e("someError", userJsonJsonBaseList.getCode() + userJsonJsonBaseList.getMsg());
                }

            }
        };
        NetworkController.postMap(URLConfig.URL_USER_UPDATE_PASSWORD,
                paramsMap, newPasswordCallBack);
    }

}