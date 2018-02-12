package edu.sctu.giftbook.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.PublishWishActivity;
import edu.sctu.giftbook.activity.WishDetailsActivity;
import edu.sctu.giftbook.adapter.WishAdapter;
import edu.sctu.giftbook.utils.JumpUtil;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class WishFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private ListView wishListView;
    private ImageView publish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_wish, null);

        getViews(view);


        return view;
    }

    public void getViews(View view) {
        publish = (ImageView) view.findViewById(R.id.fragment_wish_add_img);


        wishListView = (ListView) view.findViewById(R.id.fragment_wish_listView);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        wishListView.setAdapter(new WishAdapter(layoutInflater, activity));
        wishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JumpUtil.jumpInActivity(activity, WishDetailsActivity.class);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnClick();
    }

    private void setOnClick() {
        publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fragment_wish_add_img:
                JumpUtil.jumpInActivity(activity, PublishWishActivity.class);
                break;
            default:
                break;
        }
    }
}
