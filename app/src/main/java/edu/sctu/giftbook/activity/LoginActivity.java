package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.base.BaseActivity;

/**
 * Created by zhengsenwen on 2018/3/13.
 */

public class LoginActivity extends BaseActivity {

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = LoginActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getViews();
    }

    public void getViews() {

    }
}
