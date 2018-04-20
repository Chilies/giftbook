package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.entity.Comment;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.Reply;
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

public class CommentAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Activity activity;
    private List<Comment> list;
    private AlertDialog alertDialog;
    private EditText editCommentText;
    private String wishCardId, fromUserId, toUserId;
    private String targetNickname;


    public CommentAdapter(LayoutInflater layoutInflater, Activity activity, List<Comment> list) {
        this.layoutInflater = layoutInflater;
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_comment, null);
            holder.textView = (TextView) view.findViewById(R.id.item_comment_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final String commentText;
        final Comment comment = list.get(position);
        if (StringUtils.isBlank(comment.getFromUserNickName())) {
            commentText = comment.getFromUserNickName()
                    + " : "
                    + comment.getDescription();
        } else {
            commentText = comment.getFromUserNickName()
                    + " 回复 "
                    + comment.getToUserNickName()
                    + " : "
                    + comment.getDescription();
        }
        holder.textView.setText(commentText);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishCardId = String.valueOf(comment.getWishCardId());
                fromUserId = String.valueOf(SharePreference.getInstance(activity).getInt(CacheConfig.USER_ID));
                toUserId = String.valueOf(comment.getFromUserId());
                targetNickname = comment.getFromUserNickName();
                newCommentAlertDialog();
                CommonUtil.delayLoadSoftInput(editCommentText);
            }
        });


        return view;
    }

    private static class ViewHolder {
        TextView textView;
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
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay(); //为获取屏幕宽、高
        layoutParams.width = display.getWidth();
        alertDialog.getWindow().setAttributes(layoutParams);
        //替换dialog默认的背景，让dialog宽度全屏
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.abc_dialog_material_background_dark);


        editCommentText = (EditText) layout.findViewById(R.id.dialog_comment_edit);
        editCommentText.setHint("@" + targetNickname);
        final Button sendCommentButton;
        sendCommentButton = (Button) layout.findViewById(R.id.dialog_send_comment_button);
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment(wishCardId, fromUserId, toUserId);
            }
        });
    }

    /**
     * 发布评论
     */
    private String sendComment(String wishCardId, String fromUserId, String toUserId) {
        String description = editCommentText.getText().toString();
        if (StringUtils.isBlank(wishCardId)
                || StringUtils.isBlank(fromUserId)
                || StringUtils.isBlank(description)) {
            return null;
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("wishCardId", wishCardId);
            map.put("fromUserId", fromUserId);
            if (!fromUserId.equals(toUserId)) {
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
                    if (replyJsonBaseList.getCode() == 200
                            && replyJsonBaseList.getMsg().equals("success")) {
                        ToastUtil.makeText(activity, R.string.comment_send_success);
                        alertDialog.dismiss();
                        //发送评论之后，重新刷新评论区
//                        getComment();
                    }
                }
            };
            NetworkController.postMap(URLConfig.URL_COMMENT_PUBLISH, map, sendCommentCallBack);
            return null;
        }
    }

}
