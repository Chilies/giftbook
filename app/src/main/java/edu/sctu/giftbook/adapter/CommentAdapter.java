package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.entity.Comment;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class CommentAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Activity activity;
    private List<Comment> list;


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

        String commentText;
        Comment comment = list.get(position);
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
        return view;
    }

    private static class ViewHolder {
        TextView textView;
    }


}
