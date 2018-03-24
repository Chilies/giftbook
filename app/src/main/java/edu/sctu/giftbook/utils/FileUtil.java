package edu.sctu.giftbook.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhengsenwen on 2018/3/22.
 */

public class FileUtil {

    /**
     * 判断存储卡是否可用
     *
     * @return
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 把头像存放在本地相册并且通知广播刷新
     *
     * @param uri
     */
    public static void storeAndUpdateInDCIM(Activity activity, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        activity.sendBroadcast(intent);

        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "", "");
    }


    /**
     * 创建存放头像的文件夹，返回自定义名称的文件
     *
     * @return
     */
    @Nullable
    public static File createStoragePathForAvatar(String fileSavePath) {
        if (FileUtil.hasSdcard()) {
            File appDir = new File(fileSavePath);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String time = simpleDateFormat.format(date);
            String fileName = time + ".jpg";
            File file = new File(appDir, fileName);
            Log.e("scale", file.getAbsolutePath());
            return file;
        } else {
            Log.e("sd", "is not load");
            return null;
        }
    }

    public void saveBitmapFile(Bitmap bitmap,String filePath) {
        File file = new File("");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 将Bitmap转换成文件
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm,String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path , fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }


}
