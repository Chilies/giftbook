package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.BudgetAdapter;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.utils.ToastUtil;

/**
 * Created by zhengsenwen on 2018/2/12.
 */

public class PaymentRecordActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private ListView budgetListView;
    private LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = PaymentRecordActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_record);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getViews();

    }

    public void getViews() {
        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.article_title);
        title.setText("个人账单");

        budgetListView = (ListView) findViewById(R.id.activity_payment_budget_listview);
        layoutInflater = LayoutInflater.from(this);
        budgetListView.setAdapter(new BudgetAdapter(activity, layoutInflater));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_img:
                finish();
                break;

            default:
                break;
        }
    }
}
