package edu.sctu.giftbook.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.FriendAdapter;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class FriendFragment extends Fragment {
    private Activity activity;
    private ListView friendListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_friend,null);
        getViews(view);
        return view;
    }

    public void getViews(View view) {
        friendListView = (ListView) view.findViewById(R.id.fragment_friend_listview);

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        friendListView.setAdapter(new FriendAdapter(activity,layoutInflater));
    }
}
