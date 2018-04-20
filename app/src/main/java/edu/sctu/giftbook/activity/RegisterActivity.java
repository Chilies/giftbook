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

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.entity.JsonBaseObject;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Activity activity;
    private EditText phoneNumberEdit, passwordEdit,
            confirmPasswordEdit, nicknameEdit, alipayAccountEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = RegisterActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getViews();
    }

    public void getViews() {
        phoneNumberEdit = (EditText) findViewById(R.id.register_input_phone_number);
        passwordEdit = (EditText) findViewById(R.id.register_input_password);
        confirmPasswordEdit = (EditText) findViewById(R.id.register_input_confirm_password);
        nicknameEdit = (EditText) findViewById(R.id.register_input_nickname);
        alipayAccountEdit = (EditText) findViewById(R.id.register_input_alipay_account);

        Button registerButton;
        TextView agreement, toLogin;
        registerButton = (Button) findViewById(R.id.register_button);
        agreement = (TextView) findViewById(R.id.register_agreement_text);
        toLogin = (TextView) findViewById(R.id.register_login_text);

        registerButton.setOnClickListener(this);
        agreement.setOnClickListener(this);
        toLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                register();
                break;
            case R.id.register_agreement_text:
                break;
            case R.id.register_login_text:
                JumpUtil.jumpInActivity(activity, LoginActivity.class);
                finish();
                break;
            default:
                break;
        }
    }

    private String register() {
        String phoneNumber, password, confirmPassword, nickname, alipayAccount;
        phoneNumber = phoneNumberEdit.getText().toString();
        password = passwordEdit.getText().toString();
        confirmPassword = confirmPasswordEdit.getText().toString();
        nickname = nicknameEdit.getText().toString();
        alipayAccount = alipayAccountEdit.getText().toString();
        if (StringUtils.isBlank(phoneNumber)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(confirmPassword)
                || StringUtils.isBlank(nickname)
                || StringUtils.isBlank(alipayAccount)) {
            ToastUtil.makeText(activity, R.string.not_null);
            return null;
        } else if (!password.equals(confirmPassword)) {
            ToastUtil.makeText(activity, R.string.password_confirm);
            return null;
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("phoneNumber", DesUtils.encrypt(phoneNumber));
            map.put("password", DesUtils.encrypt(password));
            map.put("nickname", nickname);
            map.put("alipayAccount", alipayAccount);
            StringCallback callBack = new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error: ", e.getMessage(), e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("register", response);
                    JsonBaseObject<UserJson> userJsonJsonBaseObject = JSON.parseObject(response,
                            new TypeReference<JsonBaseObject<UserJson>>() {
                            }.getType());

                    if (userJsonJsonBaseObject.getCode() == 200
                            && userJsonJsonBaseObject.getMsg().equals("success")) {
                        ToastUtil.makeText(activity, R.string.register_success);
                        SharePreference.getInstance(activity).setCache(CacheConfig.IS_REGISTER, true);
                        JumpUtil.jumpInActivity(activity, LoginActivity.class);
                    }
                }
            };
            NetworkController.postMap(URLConfig.URL_USER_REGISTER, map, callBack);
        }
        return null;
    }


}