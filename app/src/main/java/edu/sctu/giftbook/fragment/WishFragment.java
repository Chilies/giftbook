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
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.PublishWishActivity;
import edu.sctu.giftbook.activity.WishDetailsActivity;
import edu.sctu.giftbook.adapter.WishAdapter;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.WishCardContent;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

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
        setWishListData();

        return view;
    }


    public void getViews(View view) {
        publish = (ImageView) view.findViewById(R.id.fragment_wish_add_img);

        wishListView = (ListView) view.findViewById(R.id.fragment_wish_listView);

    }

    private void setWishListData() {
        StringCallback callBackWishList = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("wishAllList", response);
                JsonBaseList<WishCardContent> wishCardContentJsonBaseList = JSON.parseObject
                        (response, new TypeReference<JsonBaseList<WishCardContent>>() {
                        }.getType());
                if (wishCardContentJsonBaseList.getCode() == 200
                        && wishCardContentJsonBaseList.getMsg().equals("success")) {
                    List<WishCardContent> wishCardContentList = wishCardContentJsonBaseList.getData();
                    LayoutInflater layoutInflater = LayoutInflater.from(activity);
                    wishListView.setAdapter(new WishAdapter(layoutInflater, activity,wishCardContentList));
                    wishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            JumpUtil.jumpInActivity(activity, WishDetailsActivity.class);
                        }
                    });
                }
            }
        };
        NetworkController.getObject(URLConfig.URL_WISH_ALL, callBackWishList);
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
