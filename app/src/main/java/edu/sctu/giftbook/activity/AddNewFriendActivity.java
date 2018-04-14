package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.AddNewFriendAdapter;
import edu.sctu.giftbook.entity.Contact;
import edu.sctu.giftbook.entity.ContactFriend;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.utils.ContactUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;


/**
 * Created by zhengsenwen on 2018/2/14.
 */

public class AddNewFriendActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private SearchView searchView;
    private ListView newFriendListView, searchListView;
    private LayoutInflater layoutInflater;
    private List<Contact> contactList;
    private List<String> samePhoneNumberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = AddNewFriendActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_new_friend);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        layoutInflater = layoutInflater.from(activity);

        getViews();
        getAppUserFriend();
    }

    public void getViews() {
        TextView title = (TextView) findViewById(R.id.article_title);
        title.setText("新的朋友");
        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);

        newFriendListView = (ListView) findViewById(R.id.activity_add_new_friend_ListView);

//        searchView = (SearchView) findViewById(R.id.activity_add_new_friend_searchEdit);
//        searchListView = (ListView) findViewById(R.id.activity_add_new_friend_searchListView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
//            public boolean onQueryTextSubmit(String query)
//            {
//                if(TextUtils.isEmpty(query))
//                {
//                    Toast.makeText(activity, "请输入查找内容！", Toast.LENGTH_SHORT).show();
//                    searchListView.setAdapter(adapter);
//                }
//                else
//                {
//                    findList.clear();
//                    for(int i = 0; i < list.size(); i++)
//                    {
//                        iconInformation information = list.get(i);
//                        if(information.getName().equals(query))
//                        {
//                            findList.add(information);
//                            break;
//                        }
//                    }
//                    if(findList.size() == 0)
//                    {
//                        Toast.makeText(activity, "查找的商品不在列表中", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(activity, "查找成功", Toast.LENGTH_SHORT).show();
//                        findAdapter = new listViewAdapter(activity, findList);
//                        searchListView.setAdapter(findAdapter);
//                    }
//                }
//                return true;
//            }
//            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
//            public boolean onQueryTextChange(String newText)
//            {
//                if(TextUtils.isEmpty(newText))
//                {
//                    searchListView.setAdapter(adapter);
//                }
//                else
//                {
//                    findList.clear();
//                    for(int i = 0; i < list.size(); i++)
//                    {
//                        iconInformation information = list.get(i);
//                        if(information.getName().contains(newText))
//                        {
//                            findList.add(information);
//                        }
//                    }
//                    findAdapter = new listViewAdapter(activity, findList);
//                    findAdapter.notifyDataSetChanged();
//                    searchListView.setAdapter(findAdapter);
//                }
//                return true;
//            }
//        });

    }


    private void setContactFriendData(List<String> samePhoneNumberList) {
        Map<String, String> map = new HashMap<>();

        Log.e("phone", JSON.toJSONString(samePhoneNumberList));
        map.put("phoneListJsonString", JSON.toJSONString(samePhoneNumberList));

        StringCallback friendCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }


            @Override
            public void onResponse(String response, int id) {
                Log.e("friend", response);
                JsonBaseList<ContactFriend> contactFriendJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<ContactFriend>>() {
                        }.getType());

                if (contactFriendJsonBaseList.getCode() == 200
                        && contactFriendJsonBaseList.getMsg().equals("success")) {
                    List<ContactFriend> contactFriendList = contactFriendJsonBaseList.getData();

                    newFriendListView.setAdapter(new AddNewFriendAdapter(
                            activity, layoutInflater, contactFriendList, contactList));

                }

            }
        };
        NetworkController.postMap(URLConfig.URL_FRIEND_CONTACT_FRIEND, map, friendCallBack);

    }


    /**
     * 获取用户手机通讯录APP好友（电话号码）
     */
    public void getAppUserFriend() {
        samePhoneNumberList = new ArrayList<>();
        StringCallback allPhoneCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("phone", response);
                JsonBaseList<String> stringJsonBaseList = JSON.parseObject(
                        response, new TypeReference<JsonBaseList<String>>() {
                        }.getType());
                if (stringJsonBaseList.getCode() == 200
                        && stringJsonBaseList.getMsg().equals("success")) {
                    List<String> phoneList = stringJsonBaseList.getData();
                    contactList = ContactUtil.getContactInfo(activity);
                    for (int i = 0; i < contactList.size(); i++) {
                        for (int j = 0; j < phoneList.size(); j++) {
                            if (contactList.get(i).getPhoneNumber().equals(phoneList.get(j))) {
                                samePhoneNumberList.add(phoneList.get(j));
                            }
                        }
                    }

                    setContactFriendData(samePhoneNumberList);

                } else {
                    Log.e("someError", stringJsonBaseList.getCode() + stringJsonBaseList.getMsg());
                }
            }
        };
        NetworkController.getObject(URLConfig.URL_FRIEND_ALL_PHONE, allPhoneCallBack);
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
