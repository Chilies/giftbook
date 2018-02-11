package edu.sctu.giftbook.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * 保存数据到内存中
 * 
 * @author huang
 * 
 */
@SuppressLint("WorldReadableFiles")
public class SharePreference {

	private static SharePreference tool = null;

	private SharedPreferences shareprefece;
	private SharedPreferences.Editor editor;

	/**
	 * Construct
	 */
	@SuppressLint("WorldWriteableFiles")
	private SharePreference(Context context) {
		// Preferences对象
		shareprefece = context.getSharedPreferences("SharePrefece", Context.MODE_APPEND + Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		editor = shareprefece.edit();
	}

	/**
	 * 获取单例 Create at 2013-6-17
	 * 
	 * @author huang
	 * @param context
	 * @return SharePreference
	 */
	public static SharePreference getInstance(Context context) {
		if (tool == null) {
			tool = new SharePreference(context);
		}
		return tool;
	}

	public boolean isEmpty(String key) {
		return shareprefece.contains(key);
	}

	/**
	 * 清理缓存 Create at 2013-7-1
	 * 
	 */
	public void clearCache() {
		editor.clear();
		editor.commit();
	}

	/**
	 * 清理一个缓存
	 * @param key
	 */
	public void removeOneCache(String key){
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 缓存一个集合
	 * @param key
	 * @param strings
	 */
	public void setSetCache(String key,Set<String> strings){
		editor.putStringSet(key, strings);
		editor.commit();
	}

	/**
	 * 读取集合缓存
	 * @param key key
	 * @param defValues Values to return if this preference does not exist.
	 * @return
	 */
	public Set<String> getSetCache(String key,Set<String> defValues){
		return shareprefece.getStringSet(key,defValues);
	}
	/**
	 * 设置SharedPrefere缓存 Create at 2013-6-17
	 * 
	 * @param
	 *            key 键值
	 * @param
	 *            value 缓存内容
	 */
	public void setCache(String key, Object value) {

		if (ifHaveShare(key)){
			removeOneCache(key);
		}

		if (value instanceof Boolean)// 布尔对象
			editor.putBoolean(key, (Boolean) value);
		else if (value instanceof String)// 字符串
			editor.putString(key, (String) value);
		else if (value instanceof Integer)// 整型数
			editor.putInt(key, (Integer) value);
		else if (value instanceof Long)// 长整型
			editor.putLong(key, (Long) value);
		else if (value instanceof Float)// 浮点数
			editor.putFloat(key, (Float) value);
		editor.commit();
	}

	/**
	 * 读取缓存中的字符串 Create at 2013-6-17
	 * 
	 * @param key
	 * @return String
	 */
	public String getString(String key) {
		return shareprefece.getString(key, "");
	}

	/**
	 * 读取缓存中的布尔型缓存 Create at 2013-6-17
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean getBoolean(String key) {
		return shareprefece.getBoolean(key, false);
	}

	/**
	 * 读取缓存中的整型数 Create at 2013-6-17
	 * 
	 * @param key
	 * @return int
	 */
	public int getInt(String key) {
		return shareprefece.getInt(key, 0);
	}

	/**
	 * 读取缓存中的长整型数 Create at 2013-6-17
	 * 
	 * @param key
	 * @return long
	 */
	public long getLong(String key) {
		return shareprefece.getLong(key, 0);
	}

	/**
	 * 读取缓存中的浮点数 Create at 2013-6-17
	 * 
	 * @param key
	 * @return float
	 */
	public float getFloat(String key) {
		return shareprefece.getFloat(key, 0.0f);
	}

	/**
	 * 判断是否有缓存
	 * 
	 * @param string
	 * @return
	 */
	public boolean ifHaveShare(String string) {
		return shareprefece.contains(string);
	}


}
