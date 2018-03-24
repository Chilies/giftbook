package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import java.util.Timer;
import java.util.TimerTask;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.CommentAdapter;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class WishDetailsActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private ListView commentListView;
    private LayoutInflater layoutInflater;
    private EditText editComment, editCommentText;

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
                newCommentAlertDialog();
                delayLoadSoftInput(editCommentText);
                break;
            default:
                break;
        }
    }

    private void newCommentAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View layout = layoutInflater.inflate(R.layout.dialog_edit_comment, null);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay(); //为获取屏幕宽、高
        layoutParams.width = display.getWidth();
        alertDialog.getWindow().setAttributes(layoutParams);
        //替换dialog默认的背景，让dialog宽度全屏
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.abc_dialog_material_background_dark);

        editCommentText = (EditText) layout.findViewById(R.id.dialog_comment_edit);
    }

    private void delayLoadSoftInput(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (editText != null) {
                    //设置可获得焦点
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    //请求获得焦点
                    editText.requestFocus();
                    //调用系统输入法
                    InputMethodManager inputManager = (InputMethodManager) editText
                            .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                }
            }
        }, 200);
    }

}
