package edu.sctu.giftbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sctu.giftbook.MainActivity;
import edu.sctu.giftbook.R;
import edu.sctu.giftbook.entity.JsonBaseList;
import edu.sctu.giftbook.entity.WishCard;
import edu.sctu.giftbook.utils.CacheConfig;
import edu.sctu.giftbook.utils.DesUtils;
import edu.sctu.giftbook.utils.FileUtil;
import edu.sctu.giftbook.utils.ImageTools;
import edu.sctu.giftbook.utils.JumpUtil;
import edu.sctu.giftbook.utils.NetworkController;
import edu.sctu.giftbook.utils.SharePreference;
import edu.sctu.giftbook.utils.ToastUtil;
import edu.sctu.giftbook.utils.URLConfig;
import okhttp3.Call;

/**
 * Created by zhengsenwen on 2018/2/12.
 */

public class PublishWishActivity extends Activity implements View.OnClickListener {

    private Activity activity;
    private EditText destinationEdit, valueEdit;
    private String destination, value, type;
    private TextView cancel;
    private ImageView choosePic, updateImg;
    private Button publish;
    private Spinner typeSpinner;
    private ArrayAdapter<String> adapter;
    private SharePreference sharePreference;
    private List<String> dataList;

    private static final int PHOTO_FROM_GALLERY = 1;
    private static final int PHOTO_FROM_CAMERA = 2;
    private String fileSavePath = "/sdcard/giftbook/wishcard/";
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = PublishWishActivity.this;
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_publish_wish);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        sharePreference = SharePreference.getInstance(activity);
        getViews();

    }

    public void getViews() {
        cancel = (TextView) findViewById(R.id.activity_publish_wish_cancel_text);
        publish = (Button) findViewById(R.id.activity_publish_wish_publish_button);
        destinationEdit = (EditText) findViewById(R.id.activity_publish_wish_destination_edit);
        choosePic = (ImageView) findViewById(R.id.activity_publish_wish_choose_pic_img);
        updateImg = (ImageView) findViewById(R.id.activity_publish_wish_img);
        valueEdit = (EditText) findViewById(R.id.activity_publish_wish_value_eidt);
        typeSpinner = (Spinner) findViewById(R.id.activity_publish_wish_type_spinner);

        cancel.setOnClickListener(this);
        publish.setOnClickListener(this);
        choosePic.setOnClickListener(this);

        setWishTypeData();
    }

    private void setWishTypeData() {
        StringCallback typeCallBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.makeText(activity, R.string.net_work_error);
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("publish", response);
                JsonBaseList<String> stringJsonBaseList = JSON.parseObject(response,
                        new TypeReference<JsonBaseList<String>>() {
                        }.getType());
                if (stringJsonBaseList.getCode() == 200
                        && stringJsonBaseList.getMsg().equals("success")) {

                    dataList = stringJsonBaseList.getData();
                    adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, dataList);
                    adapter.setDropDownViewResource(
                            android.support.design.R.layout.support_simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(adapter);
                    typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.e("position", adapter.getItem(position));
                            type = adapter.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } else {
                    Log.e("someError", stringJsonBaseList.getCode() + stringJsonBaseList.getMsg());
                }
            }
        };
        NetworkController.getObject(URLConfig.URL_WISH_PUBLISH_TYPE, typeCallBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_publish_wish_cancel_text:
                finish();
                break;
            case R.id.activity_publish_wish_publish_button:
                publishOneWish();
                break;
            case R.id.activity_publish_wish_choose_pic_img:
                chooseWayToGetPicture();
                break;
            default:
                break;
        }
    }

    private void chooseWayToGetPicture() {
        final String[] items = {"拍摄", "从手机相册中选择"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(activity);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        getPictureFormCamera();
                        break;
                    case 1:
                        getPictureFromAlbum();
                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void getPictureFromAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PHOTO_FROM_GALLERY);
    }

    private void getPictureFormCamera() {
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
                    //生成缩略图防止OOM 回显图片较小，上传的图片较大
                    bitmap = ImageTools.getImageThumbnail(realPath, 300, 200);
                    updateImg.setVisibility(View.VISIBLE);
                    updateImg.setImageBitmap(ImageTools.getImageThumbnail(realPath, 150, 150));
                } else {
                    ToastUtil.makeText(activity, R.string.get_image_failed);
                }
                break;
            case PHOTO_FROM_CAMERA:
                if (resultCode == RESULT_OK) {

                    Uri uri = Uri.parse(sharePreference.getString("uri"));
                    Log.e("uri", uri.toString() + "  " + uri.getPath());
                    FileUtil.storeAndUpdateInDCIM(activity, uri);
                    //生成缩略图防止OOM  回显图片较小，上传的图片较大
                    bitmap = ImageTools.getImageThumbnail(uri.getPath(), 300, 200);
                    updateImg.setVisibility(View.VISIBLE);
                    updateImg.setImageBitmap(ImageTools.getImageThumbnail(uri.getPath(), 150, 150));
                } else {
                    ToastUtil.makeText(activity, R.string.get_image_failed);
                }
                break;
            default:
                break;
        }
    }

    public void publishOneWish() {
        destination = destinationEdit.getText().toString();
        value = valueEdit.getText().toString();

        if ((destination != null) && !"".equals(destination)
                && (value != null) && !"".equals(value)
                && (type != null) && !"".equals(type)
                && (value != null) && !"".equals(value)) {

            Map<String, File> fileHashMap = new HashMap<>();
            Map<String, String> map = new HashMap<>();

            String wishCardFileName = sharePreference.getString(CacheConfig.CACHE_PHONE_NUMBER) + ".jpg";
            File file = null;
            try {
                file = FileUtil.saveFile(bitmap, fileSavePath, wishCardFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileHashMap.put("file", file);
            map.put("phoneNumber", sharePreference.getString(CacheConfig.CACHE_PHONE_NUMBER));
            map.put("content", destination);
            map.put("price", value);
            map.put("type", type);
            Log.e("wish", map.get("phoneNumber") + destination + value + type);

            StringCallback callBackPublishWish = new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtil.makeText(activity, R.string.net_work_error);
                    Log.e("error", e.getMessage(), e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("wish", response);
                    JsonBaseList<WishCard> wishCardJsonBaseList = JSON.parseObject(response,
                            new TypeReference<JsonBaseList<WishCard>>() {
                            }.getType());
                    if (wishCardJsonBaseList.getCode() == 200
                            && wishCardJsonBaseList.getMsg().equals("success")) {
                        ToastUtil.makeText(activity, R.string.wish_card_upload_success);
                        JumpUtil.jumpInActivity(activity, MainActivity.class);
                    }
                }
            };
            NetworkController.postFile(URLConfig.URL_WISH_PUBLISH, map, fileHashMap, callBackPublishWish);
        } else {
            ToastUtil.makeText(activity, R.string.content_not_be_null);
        }

    }
}
