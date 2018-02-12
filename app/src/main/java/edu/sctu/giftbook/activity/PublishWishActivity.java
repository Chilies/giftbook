package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.utils.ToastUtil;

/**
 * Created by zhengsenwen on 2018/2/12.
 */

public class PublishWishActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private Spinner type;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = PublishWishActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_publish_wish);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getViews();

    }

    public void getViews() {
        type = (Spinner) findViewById(R.id.activity_publish_wish_type_spinner);
        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getData());
        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.makeText(activity,adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

    }

    public List<String> getData() {
        dataList = new ArrayList<>();
        dataList.add("婚恋");
        dataList.add("生日");
        dataList.add("节庆");
        dataList.add("其他");
        return dataList;
    }

    @Override
    public void onClick(View v) {

    }
}