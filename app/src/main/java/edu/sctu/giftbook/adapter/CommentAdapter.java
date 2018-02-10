package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.sctu.giftbook.R;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class CommentAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Activity activity;


    public CommentAdapter(LayoutInflater layoutInflater, Activity activity) {
        this.layoutInflater = layoutInflater;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 7;
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

        holder.textView.setText("小鱼儿：星期五去钓鱼吧！");

        return view;
    }


    private static class ViewHolder {
        TextView textView;
    }
}
