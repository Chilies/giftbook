package edu.sctu.giftbook.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengsenwen on 2017/3/3.
 */
public class ImageTools {

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     * @param activity
     * @return
     * @throws IOException
     */
    public static Bitmap getBitmapFromUri(Activity activity, Uri uri) throws IOException {
        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();

        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;
        if (originalWidth == -1 || originalHeight == -1) {
            return null;
        }

        float height = 800f;
        float width = 480f;
        int be = 1;  //be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > width) {
            be = (int) (originalWidth / width);
        } else if (originalWidth < originalHeight && originalHeight > height) {
            be = (int) (originalHeight / height);
        }

        if (be <= 0) {
            be = 1;
        }
        BitmapFactory.Options bitmapOptinos = new BitmapFactory.Options();
        bitmapOptinos.inSampleSize = be;
        bitmapOptinos.inDither = true;
        bitmapOptinos.inPreferredConfig = Bitmap.Config.ARGB_8888;
        inputStream = activity.getContentResolver().openInputStream(uri);

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptinos);
        inputStream.close();

        return compressImage(bitmap);
//        return bitmap;
    }

    /**
     * 质量压缩方法
     *
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        int options = 50;
        while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {
            byteArrayOutputStream.reset();
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);
            options -= 10;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Bitmap bitmapImage = BitmapFactory.decodeStream(byteArrayInputStream, null, null);
        return bitmapImage;
    }

    /**
     * 获取缩略图
     *
     * @param imagePath:文件路径
     * @param width:缩略图宽度
     * @param height:缩略图高度
     * @return
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //关于inJustDecodeBounds的作用将在下文叙述
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        int h = options.outHeight;//获取图片高度
        int w = options.outWidth;//获取图片宽度
        int scaleWidth = w / width; //计算宽度缩放比
        int scaleHeight = h / height; //计算高度缩放比
        int scale = 1;//初始缩放比
        if (scaleWidth < scaleHeight) {//选择合适的缩放比
            scale = scaleWidth;
        } else {
            scale = scaleHeight;
        }
        if (scale <= 0) {//判断缩放比是否符合条件
            scale = 1;
        }
        options.inSampleSize = scale;
        Log.e("file",scale+"");
        // 重新读入图片，读取缩放后的bitmap，注意这次要把inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
