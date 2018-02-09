package edu.sctu.giftbook.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class JumpUtil {

    /**
     * activity 与 activity 之间直接跳转
     * @param activity
     * @param targetClass
     */
    public static void jumpInActivity(Activity activity,Class targetClass){
        Intent intent = new Intent(activity,targetClass);
        activity.startActivity(intent);
    }

    /**
     * activity 与 activity 之间带值的跳转
     * @param activity
     * @param targetClass
     * @param bundle
     */
    public static void jumpInActivity(Activity activity, Class targetClass, Bundle bundle){
        Intent intent = new Intent(activity,targetClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void jumpInFragmentAndActivity(Activity activity, Class targetClass, Bundle bundle){
        Intent intent = new Intent(activity,targetClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


}
