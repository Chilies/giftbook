package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.PersonalHomeActivity;
import edu.sctu.giftbook.activity.WishDetailsActivity;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.ToastUtil;

/**
 * Created by zhengsenwen on 2018/2/9.
 */
public class WishAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Activity activity;


    public WishAdapter(LayoutInflater layoutInflater, Activity activity) {
        this.layoutInflater = layoutInflater;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 4;
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
            view = layoutInflater.inflate(R.layout.item_fragment_wish_listview, null);
            holder.avatar = (ImageView) view.findViewById(R.id.item_fragment_wish_avatar_img);
            holder.nikeName = (TextView) view.findViewById(R.id.item_fragment_wish_nickname_text);
            holder.time = (TextView) view.findViewById(R.id.item_fragment_wish_time_text);
            holder.isWishCard = (ImageView) view.findViewById(R.id.item_fragment_wish_isWishCard_img);
            holder.destination = (TextView) view.findViewById(R.id.item_fragment_wish_destination_text);
            holder.article = (ImageView) view.findViewById(R.id.item_fragment_wish_article_img);
            holder.comment = (LinearLayout) view.findViewById(R.id.item_fragment_wish_comment_linearLayout);
            holder.payment = (LinearLayout) view.findViewById(R.id.item_fragment_wish_payment_linearLayout);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,PersonalHomeActivity.class);
                activity.startActivity(intent);
//                JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class);
            }
        });
        holder.nikeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class);
            }
        });
        holder.article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.makeText(activity, "hello");
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.jumpInActivity(activity, WishDetailsActivity.class);
            }
        });
        holder.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.makeText(activity, "正在开发中");
            }
        });

        return view;
    }


    private static class ViewHolder {
        ImageView avatar, isWishCard, article;
        TextView nikeName, time, destination;
        LinearLayout comment, payment;
    }
}
