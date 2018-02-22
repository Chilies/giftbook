package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.makeramen.roundedimageview.RoundedImageView;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.utils.ToastUtil;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class PersonalHomeActivity extends Activity implements View.OnClickListener {
    private Activity activity;
    private RoundedImageView avatar;
    private TextView nickName, signature, phoneNumber, sex, area;
    private LinearLayout wishRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_home);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getViews();
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
        wishRecord.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_personal_back_img:
                finish();
                break;
            case R.id.activity_personal_home_wish_record_linear:
                ToastUtil.makeText(activity,"正在开发中");
                break;
            default:
                break;
        }
    }
}
