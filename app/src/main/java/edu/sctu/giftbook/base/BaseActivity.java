package edu.sctu.giftbook.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;


/**
 * Created by zhengsenwen on 2018/3/13.
 */
public class BaseActivity extends FragmentActivity {
    public AppManager appManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //添加到Activity集合
        appManager = AppManager.getInstance(this);
        appManager.addActivity(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从集合中移除
        AppManager.getInstance(this).finishActivity(this);
    }

//    /**
//     * 实现状态栏与App风格一致
//     *
//     * @param activity
//     */
//    public static void initSystemBar(Activity activity, int res) {//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(activity, true);
//        }
//        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//        tintManager.setStatusBarTintEnabled(true);
//
//        tintManager.setTintAlpha(1.0f);
//// 使用颜色资源
//        if (res == 0) {
//            tintManager.setStatusBarTintResource(R.color.top_bac);//R.color.hometext
//        } else {
//            tintManager.setStatusBarTintResource(res);//R.color.hometext
//        }
//    }
//
//    /**
//     * 实现状态栏与App风格一致
//     *
//     * @param activity
//     */
//    public void initSystemBarDDrawable(Activity activity, int res) {//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(activity, true);
//        }
//        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setTintAlpha(0.0f);
//
//        Resources resources = getResources();
//        Drawable drawable = resources.getDrawable(res);
//// 使用颜色资源
//        tintManager.setTintDrawable(drawable);
//
//
//    }

    /**
     * 监听返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @TargetApi(19)
//    private static void setTranslucentStatus(Activity activity, boolean on) {
//        Window window = activity.getWindow();
//        WindowManager.LayoutParams winParams = window.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        window.setAttributes(winParams);
//    }
}
