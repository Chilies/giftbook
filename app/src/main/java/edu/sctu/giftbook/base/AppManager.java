package edu.sctu.giftbook.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.utils.SharePreference;


/**
 * 应用配置类
 *
 * @author leehongee
 */
public class AppManager extends Application {


    /**
     * 打开的activity
     **/
    private List<Activity> activities = new ArrayList<Activity>();
    /**
     * 应用实例
     **/
    private static AppManager appManager;

    private Context context;

    public static Map<String, Long> map;//用来存放倒计时的时间

    public AppManager(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
//        appManager = this;
//        PlatformConfig.setWeixin("wx16b2c862001960b5", "3323c4364affd21c325b9062308307fb");
//        PlatformConfig.setSinaWeibo("3120921808", "12e951821c4d8aef3f5624526e892b42");
//        PlatformConfig.setQQZone("1105564855", "AkiHUPvsk4qHcIkF");
    }

    /**
     * 获得实例
     *
     * @return
     */
    public static AppManager getInstance(Context context) {
        if (appManager == null) {
            appManager = new AppManager(context);
        }
        return appManager;
    }

    /**
     * 新建了一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 应用退出，结束所有的activity
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

}