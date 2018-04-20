package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zhy.http.okhttp.callback.BitmapCallback;

import org.apache.commons.lang3.StringUtils;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.base.BaseActivity;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.ToastUtil;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/4/10.
 */

public class BigImageActivity extends BaseActivity {

    private Activity activity;
    private ImageView bigImage;
    private String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = BigImageActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_big_image);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        imageUrl = getIntent().getStringExtra("bigImageSrc");
        bigImage = (ImageView) findViewById(R.id.activity_big_image);
        setBigImage();
    }


    private void setBigImage() {
        if (StringUtils.isBlank(imageUrl)) {
            bigImage.setImageResource(R.drawable.avatar);
        } else {
            BitmapCallback callBackBitmap = new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    bigImage.setImageResource(R.drawable.avatar);
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(Bitmap response, int id) {
                    bigImage.setImageBitmap(response);
                }
            };
            NetworkController.getImage(imageUrl, callBackBitmap);
        }
    }



}
