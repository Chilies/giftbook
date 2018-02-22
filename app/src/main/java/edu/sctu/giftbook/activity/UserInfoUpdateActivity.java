package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.utils.ToastUtil;

/**
 * Created by zhengsenwen on 2018/2/12.
 */

public class UserInfoUpdateActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private EditText nickName, signature, sex;
    private Spinner area;
    private Button saveAndUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = UserInfoUpdateActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_personal_infomation);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getViews();

    }

    public void getViews() {
        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.article_title);
        title.setText("修改资料");

        nickName = (EditText) findViewById(R.id.activity_update_information_nickname_text);
        updateText(nickName);

        signature = (EditText) findViewById(R.id.activity_update_information_signture_text);
        updateText(signature);

        sex = (EditText) findViewById(R.id.activity_update_information_sex_text);
        updateText(sex);

        saveAndUpdate = (Button) findViewById(R.id.activity_update_information_update_button);
        saveAndUpdate.setOnClickListener(this);
    }

    private void updateText(EditText editText) {
        editText.setSelection(editText.getText().length());
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_img:
                finish();
                break;
            case R.id.activity_update_information_update_button:
                ToastUtil.makeText(activity,"正在开发中");
                break;

            default:
                break;
        }
    }


}
