package edu.sctu.giftbook.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.AddNewFriendActivity;
import edu.sctu.giftbook.activity.PersonalHomeActivity;
import edu.sctu.giftbook.activity.WishDetailsActivity;
import edu.sctu.giftbook.adapter.FriendAdapter;
import edu.sctu.giftbook.entity.AllFriend;
import edu.sctu.giftbook.entity.Contact;
import edu.sctu.giftbook.entity.ContactFriend;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.ContactUtil;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class FriendFragment extends Fragment {
    private Activity activity;
    private ListView friendListView;
    private View newFriendView;
    private SharePreference sharePreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_friend, null);
        sharePreference = SharePreference.getInstance(activity);

        friendListView = (ListView) view.findViewById(R.id.fragment_friend_listview);
        setFellowedFriendData();
        return view;
    }

    private String setFellowedFriendData() {
        Integer userId = sharePreference.getInt(CacheConfig.USER_ID);
        if (userId == 0) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        StringCallback allFriendCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("friend", response);
                JsonBaseList<AllFriend> allFriendJsonBaseList = JSON.parseObject(
                        response, new TypeReference<JsonBaseList<AllFriend>>() {
                        }.getType());
                if (allFriendJsonBaseList.getCode() == 200
                        && allFriendJsonBaseList.getMsg().equals("success")) {
                    final List<AllFriend> allFriendList = allFriendJsonBaseList
                            .getData();
                    LayoutInflater layoutInflater = LayoutInflater.from(activity);
                    newFriendView = layoutInflater.inflate(R.layout.
                            fragment_friend_new_friend, null);
                    friendListView.addHeaderView(newFriendView);
                    friendListView.setAdapter(new FriendAdapter(activity,
                            layoutInflater, allFriendList));
                    friendListView.setOnItemClickListener(new AdapterView.
                            OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            if (position == 0) {
                                JumpUtil.jumpInActivity(activity, AddNewFriendActivity.class);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt("userId", allFriendList
                                        .get(position).getId());
                                bundle.putString("wishCardAvatarSrc",
                                        allFriendList.get(position).getAvatarSrc());
                                JumpUtil.jumpInActivity(activity,
                                        PersonalHomeActivity.class, bundle);
                            }
                        }
                    });
                }
            }
        };
        NetworkController.getMap(URLConfig.URL_ALL_FELLOW_FRIEND,
                map, allFriendCallBack);
        return null;
    }

}
