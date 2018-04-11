package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.AddNewFriendAdapter;
import edu.sctu.giftbook.base.BaseActivity;


/**
 * Created by zhengsenwen on 2018/2/14.
 */

public class AddNewFriendActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private SearchView searchView;
    private ListView newFriendListView, searchListView;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = AddNewFriendActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_new_friend);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getViews();
    }

    public void getViews() {
        TextView title = (TextView) findViewById(R.id.article_title);
        title.setText("新的朋友");
        ImageView backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(this);

        layoutInflater = layoutInflater.from(activity);
        newFriendListView = (ListView) findViewById(R.id.activity_add_new_friend_ListView);
        newFriendListView.setAdapter(new AddNewFriendAdapter(activity, layoutInflater));

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
