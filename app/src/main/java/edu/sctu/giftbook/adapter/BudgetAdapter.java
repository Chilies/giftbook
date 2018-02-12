package edu.sctu.giftbook.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import edu.sctu.giftbook.R;

/**
 * Created by zhengsenwen on 2018/2/12.
 */
public class BudgetAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater layoutInflater;

    public BudgetAdapter(Activity activity, LayoutInflater layoutInflater) {
        this.activity = activity;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return 5;
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
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_activity_payment_record_listview, null);
            holder = new ViewHolder();
            holder.avatar = (RoundedImageView) view.findViewById(R.id.item_activity_payment_record_avatar);
            holder.nickname = (TextView) view.findViewById(R.id.item_activity_payment_record_nickname_text);
            holder.time = (TextView) view.findViewById(R.id.item_activity_payment_record_time_text);
            holder.money = (TextView) view.findViewById(R.id.item_activity_payment_record_money);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private static class ViewHolder {
        RoundedImageView avatar;
        TextView nickname, time, money;
    }
}
