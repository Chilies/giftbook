package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.util.List;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.AddNewFriendActivity;
import edu.sctu.giftbook.activity.PersonalHomeActivity;
import edu.sctu.giftbook.entity.AllFriend;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.ToastUtil;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/13.
 */
public class FriendAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<AllFriend> list;

    public FriendAdapter(Activity activity, LayoutInflater inflater, List<AllFriend> list) {
        this.activity = activity;
        this.inflater = inflater;
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
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_fragment_friend_listview, null);
            viewHolder.avatar = (RoundedImageView) view.findViewById(R.id.item_fragment_friend_listView_avatar);
            viewHolder.nickname = (TextView) view.findViewById(R.id.item_fragment_friend_listView_nickname_text);
            viewHolder.fellow = (Button) view.findViewById(R.id.item_fragment_friend_listView_fellow_status);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.nickname.setText(list.get(position).getNickName());

        if (list.get(position).getFellowStatus() == 0) {

        } else if (list.get(position).getFellowStatus() == 1) {
            viewHolder.fellow.setText(activity.getResources().getString(R.string.fellow_each_other));
        }

        if (!StringUtils.isBlank(list.get(position).getAvatarSrc())
                && !"null".equals(list.get(position).getAvatarSrc())) {
            BitmapCallback callBackArticle = new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    viewHolder.avatar.setImageResource(R.drawable.avatar);
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(Bitmap response, int id) {
                    viewHolder.avatar.setImageBitmap(response);
                }
            };
            NetworkController.getImage(list.get(position).getAvatarSrc(), callBackArticle);
        }

        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", list.get(position).getId());
                bundle.putString("avatarSrc", list.get(position).getAvatarSrc());
                JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class, bundle);
            }
        });

        viewHolder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", list.get(position).getId());
                bundle.putString("avatarSrc", list.get(position).getAvatarSrc());
                JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class, bundle);
            }
        });

        return view;
    }

    private static class ViewHolder {
        RoundedImageView avatar;
        TextView nickname;
        Button fellow;
    }


}
