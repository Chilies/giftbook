package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.CommentAdapter;
import edu.sctu.giftbook.utils.JumpUtil;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class WishDetailsActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private ListView commentListView;
    private LayoutInflater layoutInflater;
    private EditText editComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = WishDetailsActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wish_details);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);
        editComment = (EditText) findViewById(R.id.activity_wish_details_comment_edit);
        editComment.setOnClickListener(this);
        editComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    View layout = layoutInflater.inflate(R.layout.dialog_edit_comment, null);
                    builder.setView(layout);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });


        commentListView = (ListView) findViewById(R.id.activity_wish_details_comment_listView);
        layoutInflater = LayoutInflater.from(this);
        commentListView.setAdapter(new CommentAdapter(layoutInflater, this));
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
