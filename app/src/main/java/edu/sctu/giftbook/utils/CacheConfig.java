package edu.sctu.giftbook.utils;

import android.os.Environment;

/**
 * Created by zhengsenwen on 2018/3/13.
 */

public class CacheConfig {

    public static String CACHE_HOME =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/giftbook/cache";

    public static String IS_FIRST = "isFirst";
    public static String USER_ID = "userId";
    public static String CACHE_NICKNAME = "nickname";
    public static String CACHE_ALIPAY_ACCOUNT = "alipayAccount";
    public static String CACHE_SIGNATURE = "signature";
    public static String CACHE_PHONE_NUMBER = "phoneNumber";
    public static String CACHE_GENDER = "gender";
    public static String CACHE_ADDRESS = "address";


    public static String CACHE_AVATAR_BITMAP = "avatarBitmap";
    public static String CACHE_AVATAR_SRC = "avatarSrc";


    public static String CACHE_IMG_PATH_SET_KEY = "cacheImgPath";
    public static String CACHE_UPLOAD_NEWS_TEXT = "cacheQuestionText";
    public static String CACHE_SELECTED_TEAM_ID = "cacheSelectedTeamId";
    public static String CACHE_SELECTED_TEAM_NAME = "cacheSelectedTeamName";
    public static String CACHE_TOKEN = "token";
    public static String CACHE_INIT_TOKEN = "Basic Y2xpZW50YXBwOjEyMzQ1Ng==";
    public static String CACHE_REFRESH_TOKEN = "refresh_token";
    public static final String CACHE_MATCH_ID = "matchId";
    public static final String CACHE_VOTE_ID = "voteId";
    public static final String CACHE_VOTE_FLAG = "voteFlag";
    public static final String CACHE_VOTE_ITEM_ID = "itemId";
    //    public static String CACHE_USER_ID = "userId";
    public static String CACHE_USER_PASSWORD = "userPassword";
    public static String CACHE_USER_PHONE = "phone";
    public static String CACHE_PORTRAIT = "portrait";
    public static String CACHE_PORTRAIT_PATH = "portraitPath";
    public static String CACHE_NICKNMAE = "nickname";
    public static String CACHE_ROUTE_ID = "routeId";
    public static String CACHE_USER_INTRODUCTION_SELF = "introduction";
    public static String CACHE_ADD_MEMBER_REAL_NAME = "addMemberRealName";
    public static String CACHE_ADD_MEMBER_ID_CARD = "addMemberIDCard";
    public static String CACHE_ADD_MEMBER_SEX = "addMemberSex";
    public static String CACHE_ADD_MEMBER_PHONE = "addMemberPhone";
    public static String CACHE_TEAM_ID = "teamID";
    public static String CACHE_IS_CLICK_UPDATE_CANCEL = "cacheIsClickUpdateCancel";
    public static String CACHE_IS_UPDATE = "isUpdate";
    public static String CACHE_LOCAL_VERSION = "localVersion";
    public static String IS_UPDATE_PORTRAIT = "isUpdatePortrait";
    public static String IS_EQUALS_USER = "isEqualsUser";
}
