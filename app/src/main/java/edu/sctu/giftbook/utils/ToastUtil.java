package edu.sctu.giftbook.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class ToastUtil {

    public static  void makeText(Activity activity,CharSequence text) {
        Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
    }
}
