package edu.sctu.giftbook.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zcy on 2016/8/23.
 */
public class LeadingViewPagerAdapter  extends PagerAdapter {

    private List<View> list;

    public LeadingViewPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (!list.isEmpty() && list.size() > 0) {
            return list.size();
        } else {

            return 0;
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(list.get(position));
        return list.get(position);
    }


    @Override
    public boolean isViewFromObject(View view, Object o) {

        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
