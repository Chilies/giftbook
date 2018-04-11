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

import java.util.HashMap;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.entity.JsonBaseObject;
import edu.sctu.giftbook.entity.UserJson;
import edu.sctu.giftbook.utils.DesUtils;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/3/13.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {


    private Activity activity;
    private EditText phoneNumberEdit, passwordEdit, confirmPasswordEdit, nicknameEdit, alipayAccountEdit;
    private String phoneNumber, password, confirmPassword, nickname, alipayAccount;
    private Button registerButton;
    private TextView agreement, toLogin;


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


    private void register() {
        Map<String, String> map = new HashMap<>();

        phoneNumber = phoneNumberEdit.getText().toString();
        password = passwordEdit.getText().toString();
        confirmPassword = confirmPasswordEdit.getText().toString();
        nickname = nicknameEdit.getText().toString();
        alipayAccount = alipayAccountEdit.getText().toString();
        System.out.println(password + phoneNumber + nickname + alipayAccount);

        if ((phoneNumber != null) && !"".equals(phoneNumber)
                && (password != null) && !"".equals(password)
                && (confirmPassword != null && !"".equals(confirmPassword))
                && (nickname != null) && !"".equals(nickname)
                && (alipayAccount != null) && !"".equals(alipayAccount)) {

            if (password.equals(confirmPassword)) {
                map.put("phoneNumber", DesUtils.encrypt(phoneNumber));
                map.put("password", DesUtils.encrypt(password));
                map.put("nickname", nickname);
                map.put("alipayAccount", alipayAccount);

                NetworkController.postMap(URLConfig.URL_USER_REGISTER, map, callBack);

            } else {
                ToastUtil.makeText(activity, R.string.password_confirm);
            }
        } else {
            ToastUtil.makeText(activity, R.string.not_null);
        }
    }

    //callBack----网络请求回调接口
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
                JumpUtil.jumpInActivity(activity, LoginActivity.class);
            } else {
                Log.e("someError", userJsonJsonBaseObject.getCode() + userJsonJsonBaseObject.getMsg());
            }
        }
    };

}