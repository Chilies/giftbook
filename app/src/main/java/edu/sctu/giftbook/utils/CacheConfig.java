package edu.sctu.giftbook.utils;

import android.os.Environment;

/**
 * Created by zhengsenwen on 2018/3/13.
 */

public class CacheConfig {

    public static String CACHE_HOME =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/giftbook/cache";

    public static String IS_REGISTER = "isRegister";
    public static String IS_LOGIN = "isLogin";
    public static String USER_ID = "userId";
    public static String CACHE_NICKNAME = "nickname";
    public static String CACHE_ALIPAY_ACCOUNT = "alipayAccount";
    public static String CACHE_SIGNATURE = "signature";
    public static String CACHE_PHONE_NUMBER = "phoneNumber";
    public static String CACHE_GENDER = "gender";
    public static String CACHE_ADDRESS = "address";

    public static String CACHE_AVATAR_BITMAP = "avatarBitmap";
    public static String CACHE_AVATAR_SRC = "avatarSrc";

    public static String CACHE_ALIPAY_RECEIVE_CODE = "alipayReceiveCode";

}
