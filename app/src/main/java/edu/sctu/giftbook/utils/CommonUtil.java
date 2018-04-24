package edu.sctu.giftbook.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

import edu.sctu.giftbook.R;

/**
 * Created by zhengsenwen on 2018/3/24.
 */

public class CommonUtil {

    public static void setType(String type, ImageView imageView) {
        switch (type) {
            case "生日":
                imageView.setBackgroundResource(R.drawable.type_birthday);
                break;
            case "婚恋":
                imageView.setBackgroundResource(R.drawable.type_marry);
                break;
            case "节日":
                imageView.setBackgroundResource(R.drawable.type_festival);
                break;
            case "日常":
                imageView.setBackgroundResource(R.drawable.type_daily);
                break;
            case "其他":
                imageView.setBackgroundResource(R.drawable.type_other);
                break;
            default:
                break;
        }
    }

    /**
     * 延迟调出软键盘
     *
     * @param editText
     */
    public static void delayLoadSoftInput(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (editText != null) {
                    //设置可获得焦点
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    //请求获得焦点
                    editText.requestFocus();
                    //调用系统输入法
                    InputMethodManager inputManager = (InputMethodManager) editText
                            .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                }
            }
        }, 200);
    }



    /**
     * 解决scrollView和listView嵌套问题
     *
     * @param listview
     * @param adapter
     */
    public static void setHeight(ListView listview) {
        int height = 0;
        Adapter adapter = listview.getAdapter();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View temp = adapter.getView(i, null, listview);
            temp.measure(0, 0);
            height += temp.getMeasuredHeight();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listview.getLayoutParams();
        params.width = LinearLayout.LayoutParams.FILL_PARENT;
        params.height = height;
        listview.setLayoutParams(params);
    }

    public static String getAbsolutePath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

}
