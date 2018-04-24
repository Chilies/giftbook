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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.PersonalHomeActivity;
import edu.sctu.giftbook.entity.Contact;
import edu.sctu.giftbook.entity.ContactFriend;
import edu.sctu.giftbook.entity.Friend;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/14.
 */
public class AddNewFriendAdapter extends BaseAdapter {
    private Activity activity;
    private List<ContactFriend> list;
    private List<Contact> contactList;
    private SharePreference sharePreference;

    public AddNewFriendAdapter(Activity activity, List<ContactFriend> list,
                               List<Contact> contactList) {
        this.activity = activity;
        this.list = list;
        this.contactList = contactList;
        sharePreference = SharePreference.getInstance(activity);
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_activity_add_new_friend_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (RoundedImageView) view.findViewById(R.id.item_activity_add_new_friend_listView_avatar);
            viewHolder.contactName = (TextView) view.findViewById(R.id.item_activity_add_new_friend_listView_contact_name_text);
            viewHolder.nickname = (TextView) view.findViewById(R.id.item_activity_add_new_friend_listView_nickname_text);
            viewHolder.fellow = (Button) view.findViewById(R.id.item_activity_add_new_friend_listView_fellow_button);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.nickname.setText(list.get(position).getNickName());
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getPhoneNumber().equals(list.get(position).getPhoneNumber())) {
                viewHolder.contactName.setText(contactList.get(i).getName());
            }
        }

        if (list.get(position).getFellowStatus() == null) {

        } else if (list.get(position).getFellowStatus() == 0) {
            viewHolder.fellow.setBackgroundColor(activity.getResources().getColor(R.color.white));
            viewHolder.fellow.setTextColor(activity.getResources().getColor(R.color.thirdTitle));
            viewHolder.fellow.setText(activity.getResources().getString(R.string.already_fellow));
        } else if (list.get(position).getFellowStatus() == 1) {
            viewHolder.fellow.setBackgroundColor(activity.getResources().getColor(R.color.white));
            viewHolder.fellow.setTextColor(activity.getResources().getColor(R.color.thirdTitle));
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

        viewHolder.fellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fellowTheFriend(list.get(position).getId(), viewHolder.fellow);
            }
        });
        return view;
    }

    private String fellowTheFriend(Integer friendId, final Button fellowButton) {
        if (friendId == null || friendId == 0) {
            return null;
        }
        Integer userId = sharePreference.getInt(CacheConfig.USER_ID);
        if (userId == 0) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("friendId", String.valueOf(friendId));
        StringCallback fellowCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("fellow", response);
                JsonBaseList<Friend> friendJsonBaseList = JSON.parseObject(
                        response, new TypeReference<JsonBaseList<Friend>>() {
                        }.getType());

                if (friendJsonBaseList.getCode() == 200
                        && friendJsonBaseList.getMsg().equals("success")) {
                    fellowButton.setBackgroundColor(activity.getResources().getColor(R.color.white));
                    fellowButton.setTextColor(activity.getResources().getColor(R.color.thirdTitle));
                    fellowButton.setText("已关注");
                }
            }
        };
        NetworkController.postMap(URLConfig.URL_FELLOW_FRIEND, map, fellowCallBack);
        return null;
    }

    private static class ViewHolder {
        RoundedImageView avatar;
        TextView contactName;
        TextView nickname;
        Button fellow;
    }


}
