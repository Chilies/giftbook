package edu.sctu.giftbook.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.AddNewFriendActivity;
import edu.sctu.giftbook.activity.PersonalHomeActivity;
import edu.sctu.giftbook.activity.WishDetailsActivity;
import edu.sctu.giftbook.adapter.FriendAdapter;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.ToastUtil;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class FriendFragment extends Fragment {
    private Activity activity;
    private ListView friendListView;
    private View newFriendView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_friend, null);
        getViews(view);
        return view;
    }

    public void getViews(View view) {

        friendListView = (ListView) view.findViewById(R.id.fragment_friend_listview);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        newFriendView = layoutInflater.inflate(R.layout.fragment_friend_new_friend, null);
        friendListView.addHeaderView(newFriendView);
        friendListView.setAdapter(new FriendAdapter(activity, layoutInflater));
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    JumpUtil.jumpInActivity(activity, AddNewFriendActivity.class);
                } else {
                    JumpUtil.jumpInActivity(activity, PersonalHomeActivity.class);
                }
            }
        });
    }


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setOnClick();
//    }
//
//    private void setOnClick() {
//        newFriendView.setOnClickListener(this);
//
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.fragment_friend_new_friend_linearlayout:
//                JumpUtil.jumpInActivity(activity, AddNewFriendActivity.class);
//                break;
//            default:
//                break;
//        }
//    }

}
