package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.sctu.giftbook.MainActivity;
import edu.sctu.giftbook.R;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.SharePreference;

/**
 * Created by zcy on 2016/8/23.
 */
public class LeadingActivity extends BaseActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = LeadingActivity.this;

        /**
         * 去掉状态栏
         */
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

//        setContentView(R.layout.activity_leading);
        setContentView(R.layout.activity_guide_page);

        if (SharePreference.getInstance(this).ifHaveShare("isFirst")) {
            SharePreference.getInstance(this).setCache("isFirst", true);
            JumpUtil.jumpInActivity(this, MainActivity.class);
        } else {

            final Runnable myRun = new Runnable() {
                @Override
                public void run() {
                    JumpUtil.jumpInActivity(activity, RegisterActivity.class);
                    finish();
                }
            };

            final Handler handler = new Handler();
            handler.postDelayed(myRun, 3000);
        }
    }


//    private void init() {
//
//        //初始化点点点控件
//        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
//        imageViewList = new ImageView[3];
//
//        for (int i = 0; i < imageViewList.length; i++) {
//            ImageView imageView = new ImageView(this);
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
//
//            if (i == 0) {
//                imageView.setBackgroundResource(R.drawable.leading_selected);
//            } else {
//                imageView.setBackgroundResource(R.drawable.leading_un_selected);
//            }
//
//            imageViewList[i] = imageView;
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(30, 30));
//
//            layoutParams.leftMargin = 20;//设置点点点view的左边距
//            layoutParams.rightMargin = 20;//设置点点点view的右边距
//            layoutParams.bottomMargin = 40;
//            group.addView(imageView, layoutParams);
//        }
//
//        viewPager = (ViewPager) findViewById(R.id.leading_viewpager);
//
//        setData();
//
//        adapter = new LeadingViewPagerAdapter(list);
//        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setCurrentItem(0);
//        viewPager.setOnPageChangeListener(changeListener);
//    }
//
//    private void setData() {
//
//        View first = LayoutInflater.from(this).inflate(R.layout.activity_leading_image, null);
//        ImageView firstImageView = (ImageView) first.findViewById(R.id.leading_image);
//        firstImageView.setImageResource(R.drawable.leading_one);
//
//        View two = LayoutInflater.from(this).inflate(R.layout.activity_leading_image, null);
//        ImageView twoImageView = (ImageView) two.findViewById(R.id.leading_image);
//        twoImageView.setImageResource(R.drawable.leading_two);
//
//        View three = LayoutInflater.from(this).inflate(R.layout.activity_leading_image, null);
//        ImageView threeImageView = (ImageView) three.findViewById(R.id.leading_image);
//        threeImageView.setImageResource(R.drawable.leading_three);
//
//        ImageView imageView = (ImageView) three.findViewById(R.id.leading_image_enter);
//        imageView.setImageResource(R.drawable.enter);
//
//        list.add(first);
//        list.add(two);
//        list.add(three);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharePreference.getInstance(LeadingViewPagerActivity.this).setCache("isFirst", true);
//                JumpUtil.jumpInActivity(LeadingViewPagerActivity.this, RegisterActivity.class);
//            }
//        });
//    }
//
//    /**
//     * 监听viewpager的移动
//     */
//    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageSelected(int index) {
//
//            setImageBackground(index);//改变点点点的切换效果
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//    };
//
//    /**
//     * 改变点点点的切换效果
//     *
//     * @param selectItems
//     */
//    private void setImageBackground(int selectItems) {
//        for (int i = 0; i < imageViewList.length; i++) {
//            if (i == selectItems) {
//                imageViewList[i].setBackgroundResource(R.drawable.leading_selected);
//            } else {
//                imageViewList[i].setBackgroundResource(R.drawable.leading_un_selected);
//            }
//        }
//    }

}
