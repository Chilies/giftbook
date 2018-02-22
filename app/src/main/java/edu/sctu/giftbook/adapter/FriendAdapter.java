package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.AddNewFriendActivity;
import edu.sctu.giftbook.activity.PersonalHomeActivity;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.ToastUtil;

/**
 * Created by zhengsenwen on 2018/2/13.
 */
public class FriendAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;

    public FriendAdapter(Activity activity, LayoutInflater inflater) {
        this.activity = activity;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return 15;
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
            view = inflater.inflate(R.layout.item_fragment_friend_listview, null);
            holder.avatar = (RoundedImageView) view.findViewById(R.id.item_fragment_friend_listView_avatar);
            holder.nickname = (TextView) view.findViewById(R.id.item_fragment_friend_listView_nickname_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class);
            }
        });
        holder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class);
            }
        });

        return view;
    }

    private static class ViewHolder {
        RoundedImageView avatar;
        TextView nickname;
    }
}
