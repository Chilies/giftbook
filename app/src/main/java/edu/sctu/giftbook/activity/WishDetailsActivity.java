package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = WishDetailsActivity.this;
        setContentView(R.layout.activity_wish_details);

        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);
        EditText editComment = (EditText) findViewById(R.id.activity_wish_details_comment_edit);
        editComment.setOnClickListener(this);

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
            case R.id.activity_wish_details_comment_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View layout = layoutInflater.inflate(R.layout.dialog_edit_comment, null);
                builder.setView(layout);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            default:
                break;
        }
    }
}
