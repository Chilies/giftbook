package edu.sctu.giftbook.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.sctu.giftbook.R;
import edu.sctu.giftbook.activity.PaymentRecordActivity;
import edu.sctu.giftbook.activity.SettingsActivity;
import edu.sctu.giftbook.entity.AvatarJson;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.UserInfoJson;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.FileUtil;
import edu.sctu.giftbook.utils.ImageTools;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhengsenwen on 2018/2/9.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private ImageView settings;
    private RoundedImageView avatar;
    private TextView nickName, signature, phoneNumber, alipayAccount, sex, area;
    private LinearLayout wishRecord, paymentRecord;

    private static final int PHOTO_FROM_GALLERY = 1;
    private static final int PHOTO_FROM_CAMERA = 2;
    private SharePreference sharePreference;
    private String fileSavePath = "/sdcard/giftbook/avatar/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_personal, null);
        sharePreference = SharePreference.getInstance(activity);

        getViews(view);
        setUserData();
        setAvatarData();

        return view;
    }


    private void setAvatarData() {
        if (sharePreference.ifHaveShare(CacheConfig.CACHE_AVATAR_BITMAP)) {
            avatar.setImageBitmap(sharePreference.getBitmapFromSharedPreferences(CacheConfig.CACHE_AVATAR_BITMAP));
        } else if (sharePreference.ifHaveShare(CacheConfig.CACHE_AVATAR_SRC)) {
            BitmapCallback callBackBitmap = new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(Bitmap response, int id) {
                    avatar.setImageBitmap(response);
                    sharePreference.saveBitmapToSharedPreferences(CacheConfig.CACHE_AVATAR_BITMAP, response);
                }
            };
            Log.e("url22", CacheConfig.CACHE_AVATAR_SRC);
            NetworkController.getImage(sharePreference.getString(CacheConfig.CACHE_AVATAR_SRC), callBackBitmap);
        } else {
            avatar.setImageResource(R.drawable.avatar);
        }
    }

    private void getViews(View view) {
        settings = (ImageView) view.findViewById(R.id.fragment_personal_settings_img);
        avatar = (RoundedImageView) view.findViewById(R.id.fragment_personal_avatar_img);
        nickName = (TextView) view.findViewById(R.id.fragment_personal_nickename_text);
        signature = (TextView) view.findViewById(R.id.fragment_personal_signature_text);
        phoneNumber = (TextView) view.findViewById(R.id.fragment_personal_phone_number_text);
        alipayAccount = (TextView) view.findViewById(R.id.fragment_personal_phone_alipay_text);
        sex = (TextView) view.findViewById(R.id.fragment_personal_sex_text);
        area = (TextView) view.findViewById(R.id.fragment_personal_phone_area_text);

        wishRecord = (LinearLayout) view.findViewById(R.id.fragment_personal_wish_record);
        paymentRecord = (LinearLayout) view.findViewById(R.id.fragment_personal_payment_record);

    }


    private void setUserData() {
        final int userId = sharePreference.getInt(CacheConfig.USER_ID);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", String.valueOf(userId));

        StringCallback userInfoCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //无网状态下从sharepreference中找数据
                getDataFromSharePreference();
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("userInfo", response);
                JsonBaseList<UserInfoJson> userInfoJsonJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<UserInfoJson>>() {
                        }.getType());
                if (userInfoJsonJsonBaseList.getCode() == 200
                        && userInfoJsonJsonBaseList.getMsg().equals("success")) {
                    UserInfoJson userInfoJson = userInfoJsonJsonBaseList.getData().get(0);
                    nickName.setText(userInfoJson.getNickName());
                    phoneNumber.setText(userInfoJson.getTelephone());
                    alipayAccount.setText(userInfoJson.getAlipayAccount());

                    if ((userInfoJson.getSignature() == null)
                            || ("null".equals(userInfoJson.getSignature()))
                            || "".equals(userInfoJson.getSignature())) {

                    } else {
                        signature.setText(userInfoJson.getSignature());
                    }
                    if ((userInfoJson.getGender() == null)
                            || ("null".equals(userInfoJson.getGender()))
                            || "".equals(userInfoJson.getGender())) {

                    } else {
                        sex.setText(userInfoJson.getGender());
                    }
                    if ((userInfoJson.getProvince() == null)
                            || ("null".equals(userInfoJson.getProvince()))
                            || "".equals(userInfoJson.getProvince())) {

                    } else {
                        area.setText(userInfoJson.getProvince());
                    }


                } else {
                    //无网状态下从sharepreference中找数据
                    getDataFromSharePreference();
                    Log.e("someError", userInfoJsonJsonBaseList.getCode() + userInfoJsonJsonBaseList.getMsg());
                }
            }
        };
        NetworkController.postMap(URLConfig.URL_USER_ALL_INFO,
                paramsMap, userInfoCallBack);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnClick();
    }

    private void setOnClick() {
        settings.setOnClickListener(this);
        avatar.setOnClickListener(this);
        wishRecord.setOnClickListener(this);
        paymentRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fragment_personal_settings_img:
                JumpUtil.jumpInActivity(activity, SettingsActivity.class);
                break;
            case R.id.fragment_personal_avatar_img:
                changeAvatar();
                break;
            case R.id.fragment_personal_wish_record:
                ToastUtil.makeText(activity, "正在开发中");
                break;
            case R.id.fragment_personal_payment_record:
                JumpUtil.jumpInActivity(activity, PaymentRecordActivity.class);
                break;
//            case R.id.fragment_personal_nickename_text:
//                JumpUtil.jumpInActivity(activity, UpdateInformationActivity.class);
//            break;
//            case R.id.fragment_personal_signature_text:
//                JumpUtil.jumpInActivity(activity, UpdateInformationActivity.class);
//                break;
            default:
                break;
        }
    }


    private void changeAvatar() {
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

    private void selectAvatarFromLocal() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PHOTO_FROM_GALLERY);
    }

    private void selectAvatarFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtil.createStoragePathForAvatar(fileSavePath);
        Uri uriForCamera = Uri.fromFile(file);
        sharePreference.setCache("uri", String.valueOf(uriForCamera));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForCamera);
        startActivityForResult(intent, PHOTO_FROM_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("now", "is here");
        switch (requestCode) {
            case PHOTO_FROM_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    Log.e("uri", uri.toString() + "  " + uri.getPath());
                    String path = uri.getPath();
                    String realPath = Environment.getExternalStorageDirectory().getPath()
                            + path.substring(path.indexOf("D") - 1, path.length());
                    //生成缩略图防止OOM
                    Bitmap bitmap = ImageTools.getImageThumbnail(realPath, 100, 150);
                    uploadAvatar(bitmap);
                } else {
                    ToastUtil.makeText(activity, R.string.get_image_failed);
                }
                break;
            case PHOTO_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    Uri uri = Uri.parse(sharePreference.getString("uri"));
                    Log.e("uri", uri.toString() + "  " + uri.getPath());
                    FileUtil.storeAndUpdateInDCIM(activity, uri);
                    //生成缩略图防止OOM
                    Bitmap bitmap = ImageTools.getImageThumbnail(uri.getPath(), 100, 150);
                    uploadAvatar(bitmap);
                } else {
                    ToastUtil.makeText(activity, R.string.get_image_failed);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 上传头像
     *
     * @param bitmap
     */
    private void uploadAvatar(final Bitmap bitmap) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, File> fileMap = new HashMap<>();

        int userId = sharePreference.getInt(CacheConfig.USER_ID);

        String avatarFileName = sharePreference.getString(CacheConfig.CACHE_PHONE_NUMBER) + ".jpg";
        File file = null;
        try {
            file = FileUtil.saveFile(bitmap, fileSavePath, avatarFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        paramsMap.put("userId", String.valueOf(userId));
        fileMap.put("file", file);
        StringCallback callBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("updateAvatar", response);
                JsonBaseList<AvatarJson> avatarJsonJsonBaseList =
                        JSON.parseObject(response,
                                new TypeReference<JsonBaseList<AvatarJson>>() {
                                }.getType());

                if (avatarJsonJsonBaseList.getCode() == 200
                        && avatarJsonJsonBaseList.getMsg().equals("success")) {
                    avatar.setImageBitmap(bitmap);
                    ToastUtil.makeText(activity, R.string.avatar_upload_success);
                    sharePreference.saveBitmapToSharedPreferences(
                            CacheConfig.CACHE_AVATAR_BITMAP, bitmap);
                    AvatarJson avatarJson = avatarJsonJsonBaseList.getData().get(0);
                    sharePreference.setCache(CacheConfig.CACHE_AVATAR_SRC,
                            avatarJson.getAvatarSrc());
                } else {
                    ToastUtil.makeText(activity, R.string.login_failed);
                    setAvatarData();

                    Log.e("someError", avatarJsonJsonBaseList.getCode() + avatarJsonJsonBaseList.getMsg());

                }
            }
        };
        NetworkController.postFile(URLConfig.URL_USER_UPDATE_AVATAR,
                paramsMap, fileMap, callBack);
    }

    /**
     * 网络连接出错情况下，从sharepreference中获得用户资料数据
     */
    public void getDataFromSharePreference() {
        Log.e("userInfo", "get from share");
        if (sharePreference.ifHaveShare(CacheConfig.CACHE_NICKNAME)) {
            nickName.setText(sharePreference.getString(CacheConfig.CACHE_NICKNAME));
        }
        if (sharePreference.ifHaveShare(CacheConfig.CACHE_SIGNATURE)) {
            signature.setText(sharePreference.getString(CacheConfig.CACHE_SIGNATURE));
        }

        if (sharePreference.ifHaveShare(CacheConfig.CACHE_PHONE_NUMBER)) {
            phoneNumber.setText(sharePreference.getString(CacheConfig.CACHE_PHONE_NUMBER));
        }
        if (sharePreference.ifHaveShare(CacheConfig.CACHE_ALIPAY_ACCOUNT)) {
            alipayAccount.setText(sharePreference.getString(CacheConfig.CACHE_ALIPAY_ACCOUNT));
        }
        if (sharePreference.ifHaveShare(CacheConfig.CACHE_GENDER)) {
            sex.setText(sharePreference.getString(CacheConfig.CACHE_GENDER));
        }
        if (sharePreference.ifHaveShare(CacheConfig.CACHE_ADDRESS)) {
            area.setText(sharePreference.getString(CacheConfig.CACHE_ADDRESS));
        }
    }
}
