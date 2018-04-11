package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.List;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.BigImageActivity;
import edu.sctu.giftbook.activity.PersonalHomeActivity;
import edu.sctu.giftbook.activity.WishDetailsActivity;
import edu.sctu.giftbook.entity.WishCardContent;
import edu.sctu.giftbook.utils.AlipayUtils;
import edu.sctu.giftbook.utils.CommonUtil;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/9.
 */
public class WishAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Activity activity;
    private List<WishCardContent> list;


    public WishAdapter(LayoutInflater layoutInflater, Activity activity, List<WishCardContent> list) {
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
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            Log.e("getView", "view is null");
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_fragment_wish_listview, null);
            holder.avatar = (ImageView) view.findViewById(R.id.item_fragment_wish_avatar_img);
            holder.nickname = (TextView) view.findViewById(R.id.item_fragment_wish_nickname_text);
            holder.time = (TextView) view.findViewById(R.id.item_fragment_wish_time_text);
            holder.wishType = (ImageView) view.findViewById(R.id.item_fragment_wish_card_type_img);
            holder.destination = (TextView) view.findViewById(R.id.item_fragment_wish_destination_text);
            holder.article = (ImageView) view.findViewById(R.id.item_fragment_wish_article_img);
            holder.comment = (LinearLayout) view.findViewById(R.id.item_fragment_wish_comment_linearLayout);
            holder.payment = (LinearLayout) view.findViewById(R.id.item_fragment_wish_payment_linearLayout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            Log.e("getView", "ok");
        }

        holder.nickname.setText(list.get(position).getNickName());
        holder.time.setText(list.get(position).getCreateTime());
        holder.destination.setText(list.get(position).getDescription());
        CommonUtil.setType(list.get(position).getType(), holder.wishType);

        BitmapCallback callBackAvatar = new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                holder.avatar.setImageResource(R.drawable.avatar);
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                holder.avatar.setImageBitmap(response);
            }
        };
        NetworkController.getImage(list.get(position).getAvatarSrc(), callBackAvatar);

        BitmapCallback callBackArticle = new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                holder.article.setImageBitmap(response);
            }
        };
        NetworkController.getImage(list.get(position).getWishCardImgSrc(), callBackArticle);

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", list.get(position).getId());
                bundle.putString("wishCardAvatarSrc", list.get(position).getAvatarSrc());
                JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class, bundle);
            }
        });

        holder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", list.get(position).getId());
                bundle.putString("wishCardAvatarSrc", list.get(position).getAvatarSrc());
                JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class, bundle);
            }
        });
        holder.article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("bigImageSrc", list.get(position).getWishCardImgSrc() + "");
                Log.e("bigImageSrc", list.get(position).getWishCardImgSrc() + "");
                JumpUtil.jumpInActivity(activity, BigImageActivity.class, bundle);
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("wishCardId", list.get(position).getWishCardId() + "");
                Log.e("wishCardId", list.get(position).getWishCardId() + "  " + list.get(position).getId());
                bundle.putString("fromUserId", list.get(position).getId() + "");
                JumpUtil.jumpInActivity(activity, WishDetailsActivity.class, bundle);
            }
        });
        holder.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alipayReceiveCode = list.get(position).getAlipayReceiveCode();
                if (alipayReceiveCode != null && !"".equals(alipayReceiveCode)) {
                    AlipayUtils.transformMoney(activity, alipayReceiveCode);
                }
            }
        });

        return view;
    }

    private static class ViewHolder {
        ImageView avatar, wishType, article;
        TextView nickname, time, destination;
        LinearLayout comment, payment;
    }
}
