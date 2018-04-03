package edu.sctu.giftbook.utils;

public class DesUtils {
	/** 
     * 使用默认密钥进行DES加密 
     */  
    public static String encrypt(String plainText) {  
        try {  
            return new DES().encrypt(plainText);  
        } catch (Exception e) {  
            return null;  
        }  
    }  
  
      
    /** 
     * 使用指定密钥进行DES解密 
     */  
    public static String encrypt(String plainText, String key) {  
        try {  
            return new DES(key).encrypt(plainText);  
        } catch (Exception e) {  
            return null;  
        }  
    }  
      
  
    /** 
     * 使用默认密钥进行DES解密 
     */  
    public static String decrypt(String plainText) {  
        try {  
            return new DES().decrypt(plainText);  
        } catch (Exception e) {  
            return null;  
        }  
    }  
  
      
    /** 
     * 使用指定密钥进行DES解密 
     */  
    public static String decrypt(String plainText, String key) {  
        try {  
            return new DES(key).decrypt(plainText);  
        } catch (Exception e) {  
            return null;  
        }  
    } 

//    public static void main(String[] args) {
//        String phonestr = DesUtils.encrypt("18328023199");
//        System.out.println("加密手机号：" + phonestr);
//        String password = DesUtils.encrypt("qaz147");
//        System.out.println("加密密码：" + password);
//
//        String abs = "HTTPS://QR.ALIPAY.COM/FKX01971ADK0PRYJG5OC13";
//        System.out.println(abs.substring(22,abs.length()));
//    }
}
