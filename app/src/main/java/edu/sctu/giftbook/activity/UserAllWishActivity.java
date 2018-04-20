package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.WishAdapter;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.WishCardContent;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/4/19.
 */

public class UserAllWishActivity extends BaseActivity implements View.OnClickListener {

    private Activity activity;
    private ListView wishListView;
    private Integer userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = UserAllWishActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_all_wish);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        userId = getIntent().getIntExtra("userId", 0);

        getViews();
        setUserWishList();
    }

    private String setUserWishList() {
        if (userId == 0) {
            return null;
        }
        StringCallback userWishCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("userWishList", response);
                JsonBaseList<WishCardContent> wishCardContentJsonBaseList = JSON.parseObject(
                        response, new TypeReference<JsonBaseList<WishCardContent>>() {
                        }.getType());
                if (wishCardContentJsonBaseList.getCode() == 200
                        && wishCardContentJsonBaseList.getMsg().equals("success")) {
                    final List<WishCardContent> wishCardContentList = wishCardContentJsonBaseList.getData();
                    wishListView.setAdapter(new WishAdapter(activity, wishCardContentList));
                    wishListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("wishCardId", wishCardContentList.get(position).getWishCardId() + "");
                                    bundle.putString("toUserId", wishCardContentList.get(position).getId() + "");
                                    JumpUtil.jumpInActivity(activity, WishDetailsActivity.class, bundle);
                                }
                            });
                }

            }
        };
        NetworkController.getObject(URLConfig.URL_USER_WISH_RECORD + userId, userWishCallBack);
        return null;
    }

    private void getViews() {
        TextView title = (TextView) findViewById(R.id.article_title);
        title.setText("心愿记录");
        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);

        wishListView = (ListView) findViewById(R.id.activity_user_all_wish_listView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            default:
                break;
        }
    }


}
