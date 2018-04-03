package edu.sctu.giftbook.utils;

import android.widget.ImageView;

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

}
