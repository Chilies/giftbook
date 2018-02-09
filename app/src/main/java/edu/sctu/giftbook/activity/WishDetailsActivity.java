package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;


import edu.sctu.giftbook.R;
import edu.sctu.giftbook.adapter.CommentAdapter;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class WishDetailsActivity extends Activity{

    private Activity activity;
    private ListView commentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_details);

        commentListView = (ListView)findViewById(R.id.activity_wish_details_comment_listView);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        commentListView.setAdapter(new CommentAdapter(layoutInflater,this));
    }
}
