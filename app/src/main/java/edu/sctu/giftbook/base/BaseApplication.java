package edu.sctu.giftbook.base;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/4/28.
 */
public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    private ExecutorService threadPool;
    public ExecutorService getThreadPool(){
        if (threadPool == null){
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }


}
