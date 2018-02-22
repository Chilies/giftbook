package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.ToastUtil;

/**
 * Created by zhengsenwen on 2018/2/22.
 */

public class SettingsActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private LinearLayout updateInfo, updatePassword, logout;
    private LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = SettingsActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

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
                ToastUtil.makeText(activity, "正在开发中");
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

}