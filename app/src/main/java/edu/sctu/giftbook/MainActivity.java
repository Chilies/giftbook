package edu.sctu.giftbook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import edu.sctu.giftbook.fragment.FriendFragment;
import edu.sctu.giftbook.fragment.PersonalFragment;
import edu.sctu.giftbook.fragment.WishFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private Fragment wishFragment, friendFragment, personalFragment;
    private RelativeLayout wishButton, friendButton, personalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        wishFragment = new WishFragment();
        friendFragment = new FriendFragment();
        personalFragment = new PersonalFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, wishFragment);
        fragmentTransaction.commit();

        transformFragment();
    }

    private void transformFragment() {
        wishButton = (RelativeLayout) findViewById(R.id.activity_main_wish_button);
        friendButton = (RelativeLayout) findViewById(R.id.activity_main_friend_button);
        personalButton = (RelativeLayout) findViewById(R.id.activity_main_personal_button);

        wishButton.setOnClickListener(this);
        friendButton.setOnClickListener(this);
        personalButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            // TODO: 2018/2/9 这个不起作用
            case R.id.activity_main_wish_button:
                Log.e("click", "wish");
                wishButton.getChildAt(0).setBackgroundResource(R.drawable.main_wish_click);
                fragmentTransaction.replace(R.id.main_fragment, wishFragment);
                fragmentTransaction.commit();
                break;
            case R.id.activity_main_friend_button:
                Log.e("click", "friend");
                friendButton.getChildAt(0).setBackgroundResource(R.drawable.main_friend_click);
                fragmentTransaction.replace(R.id.main_fragment, friendFragment);
                fragmentTransaction.commit();
                break;
            case R.id.activity_main_personal_button:
                Log.e("click", "personal");
                personalButton.getChildAt(0).setBackgroundResource(R.drawable.main_personal_click);
                fragmentTransaction.replace(R.id.main_fragment, personalFragment);
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }
}
