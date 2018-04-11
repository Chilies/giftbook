package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.CommentAdapter;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.entity.Comment;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.Reply;
import edu.sctu.giftbook.entity.WishCardContent;
import edu.sctu.giftbook.utils.AlipayUtils;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.CommonUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class WishDetailsActivity extends BaseActivity implements View.OnClickListener {

    private Activity activity;
    private ListView commentListView;
    private ImageView avatar, wishType, content, payment;
    private TextView nickName, time, article, type, money;
    private LayoutInflater layoutInflater;
    private EditText editComment, editCommentText;
    private Button sendCommentButton;
    private String wishCardId;
    private String fromUserId;
    private String toUserId;
    private String alipayReceiveCode;
    private SharePreference sharePreference;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = WishDetailsActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wish_details);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        sharePreference = SharePreference.getInstance(activity);

        wishCardId = getIntent().getStringExtra("wishCardId");
        fromUserId = getIntent().getStringExtra("fromUserId");
        toUserId = String.valueOf(sharePreference.getInt(CacheConfig.USER_ID));

        getViews();
        setWishCardData();
        getComment();

    }


    public void getViews() {
        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);
        avatar = (ImageView) findViewById(R.id.activity_wish_details_avatar_img);
        wishType = (ImageView) findViewById(R.id.activity_wish_details_type_img);
        content = (ImageView) findViewById(R.id.activity_wish_details_content_img);
        nickName = (TextView) findViewById(R.id.activity_wish_details_username_text);
        time = (TextView) findViewById(R.id.activity_wish_details_time_text);
        article = (TextView) findViewById(R.id.activity_wish_details_content_text);
        type = (TextView) findViewById(R.id.activity_wish_details_type_text);
        money = (TextView) findViewById(R.id.activity_wish_details_money_text);
        payment = (ImageView) findViewById(R.id.activity_wish_details_payment);

        payment.setOnClickListener(this);

        editComment = (EditText) findViewById(R.id.activity_wish_details_comment_edit);
        editComment.setOnClickListener(this);

        commentListView = (ListView) findViewById(R.id.activity_wish_details_comment_listView);
        layoutInflater = LayoutInflater.from(this);

    }

    /**
     * 获取评论
     */
    private void getComment() {
        if (wishCardId != null && !"".equals(wishCardId)) {
            Map<String, String> commentMap = new HashMap<>();
            commentMap.put("wishCardId", String.valueOf(wishCardId));
            StringCallback commentCallBack = new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("comment", response);
                    JsonBaseList<Comment> commentJsonBaseList = JSON.parseObject(
                            response, new TypeReference<JsonBaseList<Comment>>() {
                            }.getType());

                    if (commentJsonBaseList.getCode() == 200
                            && commentJsonBaseList.getMsg().equals("success")) {
                        List<Comment> commentList = commentJsonBaseList.getData();
                        CommentAdapter commentAdapter = new CommentAdapter(layoutInflater, activity, commentList);
                        setHeight(commentListView, commentAdapter);
                        commentListView.setAdapter(commentAdapter);
                    } else {
                        Log.e("someError", commentJsonBaseList.getCode() + commentJsonBaseList.getMsg());
                    }
                }
            };
            NetworkController.getMap(URLConfig.URL_COMMENT_WISHCARD_DETAILS, commentMap, commentCallBack);
        } else {
            Log.e("error", "wishCardId is null");
        }
    }

    /**
     * 设置心愿单主体内容
     */
    private void setWishCardData() {
        if (wishCardId != null && !"".equals(wishCardId)) {
            StringCallback wishCallBack = new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("wishDetails", response);
                    JsonBaseList<WishCardContent> wishCardContentJsonBaseList
                            = JSON.parseObject(response,
                            new TypeReference<JsonBaseList<WishCardContent>>() {
                            }.getType());
                    if (wishCardContentJsonBaseList.getCode() == 200
                            && wishCardContentJsonBaseList.getMsg()
                            .equals("success")) {
                        WishCardContent wishCardContent = wishCardContentJsonBaseList
                                .getData().get(0);
                        nickName.setText(wishCardContent.getNickName());
                        time.setText(wishCardContent.getCreateTime());
                        article.setText(wishCardContent.getDescription());
                        type.setText(wishCardContent.getType());
                        money.setText(wishCardContent.getPrice());

                        alipayReceiveCode = wishCardContent.getAlipayReceiveCode();

                        CommonUtil.setType(wishCardContent.getType(), wishType);

                        BitmapCallback callBackAvatar = new BitmapCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.makeText(activity, R.string.net_work_error);
                                Log.e("error", e.getMessage(), e);
                            }

                            @Override
                            public void onResponse(Bitmap response, int id) {
                                avatar.setImageBitmap(response);
                            }
                        };
                        NetworkController.getImage(wishCardContent.getAvatarSrc(), callBackAvatar);

                        BitmapCallback callBackArticle = new BitmapCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.makeText(activity, R.string.net_work_error);
                                Log.e("error", e.getMessage(), e);
                            }

                            @Override
                            public void onResponse(Bitmap response, int id) {
                                content.setImageBitmap(response);
                            }
                        };
                        NetworkController.getImage(wishCardContent.getWishCardImgSrc(), callBackArticle);
                    } else {
                        Log.e("someError", wishCardContentJsonBaseList.getCode()
                                + wishCardContentJsonBaseList.getMsg());
                    }
                }
            };
            Log.e("wishone", URLConfig.URL_WISH_ONE + wishCardId);
            NetworkController.getObject(URLConfig.URL_WISH_ONE + wishCardId, wishCallBack);
        } else {
            Log.e("error", "wishCardId is null");
        }
    }

    /**
     * 解决scrollView和listView嵌套问题
     *
     * @param listview
     * @param adapter
     */
    public void setHeight(ListView listview, Adapter adapter) {
        int height = 0;
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View temp = adapter.getView(i, null, listview);
            temp.measure(0, 0);
            height += temp.getMeasuredHeight();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listview.getLayoutParams();
        params.width = LinearLayout.LayoutParams.FILL_PARENT;
        params.height = height;
        listview.setLayoutParams(params);
    }


    /**
     * 弹出评论框
     */
    private void newCommentAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View layout = layoutInflater.inflate(R.layout.dialog_edit_comment, null);
        builder.setView(layout);
        alertDialog = builder.create();
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
        sendCommentButton = (Button) layout.findViewById(R.id.dialog_send_comment_button);
        sendCommentButton.setOnClickListener(this);
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
            case R.id.activity_wish_details_payment:
                if (alipayReceiveCode != null && !"".equals(alipayReceiveCode)) {
                    AlipayUtils.transformMoney(activity, alipayReceiveCode);
                }
                break;
            case R.id.dialog_send_comment_button:
                sendComment();
                break;
            default:
                break;
        }
    }

    /**
     * 发布评论
     */
    private void sendComment() {
        String description = editCommentText.getText().toString();
        Log.e("comment", wishCardId + fromUserId + toUserId + description);
        if ((wishCardId != null && !"".equals(wishCardId))
                && (fromUserId != null && !"".equals(fromUserId))
                && (!"".equals(description))) {

            Map<String, String> map = new HashMap<>();
            map.put("wishCardId", wishCardId);
            map.put("fromUserId", fromUserId);
            if (fromUserId.equals(toUserId)) {

            } else {
                map.put("toUserId", toUserId);
            }
            map.put("description", description);

            StringCallback sendCommentCallBack = new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    alertDialog.dismiss();
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("comment", response);
                    JsonBaseList<Reply> replyJsonBaseList = JSON.parseObject(
                            response, new TypeReference<JsonBaseList<Reply>>() {
                            }.getType());

                    if (replyJsonBaseList.getCode() == 200 && replyJsonBaseList.getMsg().equals("success")) {
                        //发送评论之后，重新刷新评论区
                        ToastUtil.makeText(activity, R.string.comment_send_success);
                        alertDialog.dismiss();
                        getComment();

                    } else {
                        Log.e("someError", replyJsonBaseList.getCode() + replyJsonBaseList.getMsg());
                    }


                }
            };
            NetworkController.postMap(URLConfig.URL_COMMENT_PUBLISH, map, sendCommentCallBack);

        } else {
            Log.e("error", wishCardId + fromUserId + toUserId + description);
        }


    }

    /**
     * 延迟调出软键盘
     *
     * @param editText
     */
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
