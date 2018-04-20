package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import edu.sctu.giftbook.MainActivity;
import edu.sctu.giftbook.R;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.UserJson;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.DesUtils;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/3/13.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Activity activity;
    private EditText phoneNumberEdit, passwordEdit;

    private SharePreference sharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = LoginActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        sharePreference = SharePreference.getInstance(activity);

        getViews();
    }

    public void getViews() {
        Button loginButton;
        TextView toRegister;
        loginButton = (Button) findViewById(R.id.login_button);
        toRegister = (TextView) findViewById(R.id.login_register_text);

        phoneNumberEdit = (EditText) findViewById(R.id.login_input_phone_number);
        passwordEdit = (EditText) findViewById(R.id.login_input_password);

        loginButton.setOnClickListener(this);
        toRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                login();
                break;
            case R.id.register_agreement_text:
                break;
            case R.id.login_register_text:
                JumpUtil.jumpInActivity(activity, RegisterActivity.class);
                finish();
                break;
            default:
                break;
        }
    }

    private String login() {
        String phoneNumber, password;
        phoneNumber = phoneNumberEdit.getText().toString();
        password = passwordEdit.getText().toString();
        if (StringUtils.isBlank(phoneNumber)
                || (StringUtils.isBlank(password))) {
            ToastUtil.makeText(activity, R.string.not_null);
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("phoneNumber", DesUtils.encrypt(phoneNumber));
        map.put("password", DesUtils.encrypt(password));
        StringCallback callBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("login", response);
                JsonBaseList<UserJson> userJsonJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<UserJson>>() {
                        }.getType());
                if (userJsonJsonBaseList.getCode() == 200
                        && userJsonJsonBaseList.getMsg().equals("success")) {
                    ToastUtil.makeText(activity, R.string.login_success);
                    JumpUtil.jumpInActivity(activity, MainActivity.class);

                    UserJson userJson = userJsonJsonBaseList.getData().get(0);
                    sharePreference.setCache(CacheConfig.IS_LOGIN, true);
                    sharePreference.setCache(CacheConfig.USER_ID, userJson.getId());
                    sharePreference.setCache(CacheConfig.CACHE_NICKNAME, userJson.getNickName());
                    sharePreference.setCache(CacheConfig.CACHE_ALIPAY_ACCOUNT, userJson.getAlipayAccount());
                    sharePreference.setCache(CacheConfig.CACHE_SIGNATURE, userJson.getSignature());
                    sharePreference.setCache(CacheConfig.CACHE_PHONE_NUMBER, userJson.getTelephone());
                } else {
                    ToastUtil.makeText(activity, R.string.login_failed);
                }
            }
        };
        NetworkController.postMap(URLConfig.URL_USER_LOGIN, map, callBack);
        return null;
    }

}
