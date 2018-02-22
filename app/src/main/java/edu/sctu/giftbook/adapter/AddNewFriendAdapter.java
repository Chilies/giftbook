package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import edu.sctu.giftbook.R;

/**
 * Created by zhengsenwen on 2018/2/14.
 */
public class AddNewFriendAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;

    public AddNewFriendAdapter(Activity activity, LayoutInflater inflater) {
        this.activity = activity;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return 10;
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
    public View getView(int position, View view, ViewGroup parent) {
       ViewHolder viewHolder;
        if(view == null){
            view = inflater.inflate(R.layout.item_activity_add_new_friend_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (RoundedImageView) view.findViewById(R.id.item_activity_add_new_friend_listView_avatar);
            viewHolder.nickname = (TextView) view.findViewById(R.id.item_activity_add_new_friend_listView_nickname_text);
            viewHolder.fellow = (Button) view.findViewById(R.id.item_activity_add_new_friend_listView_fellow_button);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private static class ViewHolder{
        RoundedImageView avatar;
        TextView nickname;
        Button fellow;
    }
}
