package edu.sctu.giftbook.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.utils.ImageTools;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class PersonalFragment extends Fragment {
    private Activity activity;
    private RoundedImageView avatar;

    private static final int PHOTO_FROM_GALLERY = 1;
    private static final int PHOTO_FROM_CAMERA = 2;
    private ImageView imageView;
    private File appDir;
    private Uri uriForCamera;
    private Date date;
    private String str = "";
    private String uriStr;
//    private SharePreference sharePreference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_personal, null);
        avatar = (RoundedImageView) view.findViewById(R.id.fragment_personal_avatar_img);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"拍摄", "从手机相册中选择"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(activity);
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                selectAvatarFromCamera();
                                break;
                            case 1:
                                selectAvatarFromLocal();
                                break;
                            default:
                                break;
                        }
                    }
                });
                listDialog.show();
            }
        });
    }

    private void selectAvatarFromLocal() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, PHOTO_FROM_GALLERY);
    }

    private void selectAvatarFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //createImageStoragePath: return one uri
        uriForCamera = Uri.fromFile(createStoragePathForAvatar());
        uriStr = String.valueOf(uriForCamera);
//        sharePreference.setCache("uri", String.valueOf(uriForCamera));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForCamera);
        activity.startActivityForResult(intent, PHOTO_FROM_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_FROM_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
//                    imageView.setImageURI(uri);
                    avatar.setImageURI(uri);
                }
                break;
            case PHOTO_FROM_CAMERA:
                if (resultCode == -1) {
//                    Uri uri = Uri.parse(uriStr);
//                    createStoragePathForAvatar();
//                    storeAndUpdateInDCIM(uri);
//                try {
//                    //getBitmapFromUri return a zoomed bitmap，refuse OOM
//                    Bitmap bitmap = ImageTools.getBitmapFromUri(uri, this);
//                    imageView.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                    // remove sharePreference
//                    removeCache("uri");
                } else {
                    Log.e("result", "is not ok" + resultCode);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 把头像存放在本地相册并且通知广播刷新
     *
     * @param uri
     */
    private void storeAndUpdateInDCIM(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        activity.sendBroadcast(intent);

        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "", "");
    }

    /**
     * 创建存放头像的文件夹
     *
     * @return
     */
    private File createStoragePathForAvatar() {
        if (hasSdcard()) {
            appDir = new File("/sdcard/giftbook/");
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            date = new Date();
            str = simpleDateFormat.format(date);
            String fileName = str + ".jpg";
            File file = new File(appDir, fileName);
            return file;
        } else {
            Log.e("sd", "is not load");
            return null;
        }
    }

    /**
     * 判断存储卡是否可用
     *
     * @return
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

//    /**
//     * remove sharePreference
//     * @param cache
//     */
//    private void removeCache(String cache) {
//        if (sharePreference.ifHaveShare(cache)) {
//            sharePreference.removeOneCache(cache);
//        } else {
//            Log.e("this cache", "is not exist.");
//        }
//    }


}
