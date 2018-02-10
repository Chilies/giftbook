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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.sctu.giftbook.fragment.FriendFragment;
import edu.sctu.giftbook.fragment.PersonalFragment;
import edu.sctu.giftbook.fragment.WishFragment;
import edu.sctu.giftbook.utils.ToastUtil;

import static edu.sctu.giftbook.R.id.activity_main_personal_text;
import static edu.sctu.giftbook.R.id.textView;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private Fragment wishFragment, friendFragment, personalFragment;
    private RelativeLayout wishButton, friendButton, personalButton;
    private ImageView wishImage, friendImage, personalImage;
    private TextView wishText, friendText, personalText;

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

        getViews();
    }

    private void getViews() {
        wishButton = (RelativeLayout) findViewById(R.id.activity_main_wish_button);
        friendButton = (RelativeLayout) findViewById(R.id.activity_main_friend_button);
        personalButton = (RelativeLayout) findViewById(R.id.activity_main_personal_button);

        wishButton.setOnClickListener(this);
        friendButton.setOnClickListener(this);
        personalButton.setOnClickListener(this);

        wishImage = (ImageView) findViewById(R.id.activity_main_wish_img);
        wishText = (TextView) findViewById(R.id.activity_main_wish_text);
        friendImage = (ImageView) findViewById(R.id.activity_main_friend_img);
        friendText = (TextView) findViewById(R.id.activity_main_friend_text);
        personalImage = (ImageView) findViewById(R.id.activity_main_personal_img);
        personalText = (TextView) findViewById(activity_main_personal_text);

        wishImage.setImageResource(R.drawable.main_wish_click);
        wishText.setTextColor(getResources().getColor(R.color.themeRed));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.activity_main_wish_button:
                changeTabColor(wishImage, wishText, R.drawable.main_wish_click);
                fragmentTransaction.replace(R.id.main_fragment, wishFragment);
                fragmentTransaction.commit();
                break;
            case R.id.activity_main_friend_button:
                changeTabColor(friendImage, friendText, R.drawable.main_friend_click);
                fragmentTransaction.replace(R.id.main_fragment, friendFragment);
                fragmentTransaction.commit();
                break;
            case R.id.activity_main_personal_button:
                changeTabColor(personalImage, personalText, R.drawable.main_personal_click);
                fragmentTransaction.replace(R.id.main_fragment, personalFragment);
                fragmentTransaction.commit();
                break;
            default:
                break;

        }
    }

    public void changeTabColor(ImageView imageView, TextView textView, int resource) {
        wishImage.setImageResource(R.drawable.main_wish_unclick);
        wishText.setTextColor(getResources().getColor(R.color.thirdTitle));
        friendImage.setImageResource(R.drawable.main_friend_unclick);
        friendText.setTextColor(getResources().getColor(R.color.thirdTitle));
        personalImage.setImageResource(R.drawable.main_personal_unclick);
        personalText.setTextColor(getResources().getColor(R.color.thirdTitle));

        imageView.setImageResource(resource);
        textView.setTextColor(getResources().getColor(R.color.themeRed));
    }
}
