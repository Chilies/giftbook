package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.entity.Area;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.UserInfoJson;
import edu.sctu.giftbook.entity.UserJson;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/12.
 */
public class UserInfoUpdateActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private EditText nickNameEdit, signatureEdit, sexEdit;
    private String nickname, signature, sex, area;
    private Spinner areaSpinner;
    private Button saveAndUpdate;
    private ArrayAdapter<String> adapter;
    private List<String> dataList;
    private SharePreference sharePreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = UserInfoUpdateActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_personal_infomation);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        sharePreference = SharePreference.getInstance(activity);
        getViews();
        getAreaData();
    }


    public void getViews() {
        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.article_title);
        title.setText("修改资料");

        nickNameEdit = (EditText) findViewById(R.id.activity_update_information_nickname_text);
        signatureEdit = (EditText) findViewById(R.id.activity_update_information_signture_text);
        sexEdit = (EditText) findViewById(R.id.activity_update_information_sex_text);
        areaSpinner = (Spinner) findViewById(R.id.activity_update_information_area_spinner);
        saveAndUpdate = (Button) findViewById(R.id.activity_update_information_update_button);

        setData();

        editSettings(nickNameEdit);
        editSettings(signatureEdit);
        editSettings(sexEdit);

        saveAndUpdate.setOnClickListener(this);
    }

    private void setData() {
        if (sharePreference.ifHaveShare(CacheConfig.CACHE_NICKNAME)) {
            nickNameEdit.setText(sharePreference.getString(CacheConfig.CACHE_NICKNAME));
        }
        if (sharePreference.ifHaveShare(CacheConfig.CACHE_SIGNATURE)) {
            signatureEdit.setText(sharePreference.getString(CacheConfig.CACHE_SIGNATURE));
        }

        if (sharePreference.ifHaveShare(CacheConfig.CACHE_GENDER)) {
            sexEdit.setText(sharePreference.getString(CacheConfig.CACHE_GENDER));
        }

    }

    private void editSettings(EditText editText) {
        editText.setSelection(editText.getText().length());
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    private void getAreaData() {
        StringCallback areaCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("areaData", response);
                JsonBaseList<String> stringJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<String>>() {
                        }.getType());

                if (stringJsonBaseList.getCode() == 200
                        && stringJsonBaseList.getMsg().equals("success")) {

                    dataList = stringJsonBaseList.getData();
                    adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, dataList);
                    adapter.setDropDownViewResource(
                            android.support.design.R.layout.support_simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(adapter);
                    areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.e("position", adapter.getItem(position));
                            area = adapter.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } else {
                    Log.e("someError", stringJsonBaseList.getCode() + stringJsonBaseList.getMsg());
                }
            }
        };
        NetworkController.getObject(URLConfig.URL_USER_AREA_DATA, areaCallBack);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_img:
                finish();
                break;
            case R.id.activity_update_information_update_button:
                updateUserInfo();
                break;
            default:
                break;
        }
    }

    private void updateUserInfo() {
        nickname = nickNameEdit.getText().toString();
        signature = signatureEdit.getText().toString();
        sex = sexEdit.getText().toString();

        if ((nickname != null) && (!"".equals(nickname))
                && (signature != null) && (!"".equals(signature))
                && (sex != null) && (!"".equals(sex))
                && (area != null) && (!"".equals(area))) {
            int userId = sharePreference.getInt(CacheConfig.USER_ID);
            Map<String, String> map = new HashMap<>();
            map.put("nickname", nickname);
            map.put("signature", signature);
            map.put("gender", sex);
            map.put("address", area);
            map.put("userId", String.valueOf(userId));

            StringCallback callBack = new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("updateUser", response);
                    JsonBaseList<UserInfoJson> userJsonJsonBaseList = JSON.parseObject(response,
                            new TypeReference<JsonBaseList<UserInfoJson>>() {
                            }.getType());

                    if (userJsonJsonBaseList.getCode() == 200
                            && userJsonJsonBaseList.getMsg().equals("success")) {
                        ToastUtil.makeText(activity, R.string.user_info_upload_success);
                        UserInfoJson userInfoJson = (UserInfoJson) userJsonJsonBaseList.getData().get(0);
                        sharePreference.removeOneCache(CacheConfig.CACHE_NICKNAME);
                        sharePreference.removeOneCache(CacheConfig.CACHE_SIGNATURE);
                        sharePreference.removeOneCache(CacheConfig.CACHE_GENDER);
                        sharePreference.removeOneCache(CacheConfig.CACHE_ADDRESS);

                        sharePreference.setCache(CacheConfig.CACHE_NICKNAME, userInfoJson.getNickName());
                        sharePreference.setCache(CacheConfig.CACHE_SIGNATURE, userInfoJson.getSignature());
                        sharePreference.setCache(CacheConfig.CACHE_GENDER, userInfoJson.getGender());
                        sharePreference.setCache(CacheConfig.CACHE_ADDRESS, userInfoJson.getProvince());
                    }else {
                        Log.e("someError", userJsonJsonBaseList.getCode() + userJsonJsonBaseList.getMsg());
                    }
                }
            };
            NetworkController.postMap(URLConfig.URL_USER_UPDATE_INFO, map, callBack);

        } else {
            ToastUtil.makeText(activity, R.string.content_not_be_null);
        }
    }
}
