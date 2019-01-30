package com.chinapex.android.monitor.utils;

import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * @author SteelCabbage
 * @date 2018/11/25
 */
public class MD5Utils {

    private static final String TAG = MD5Utils.class.getSimpleName();
    private static final String ERROR_MD5 = "error_md5";

    public static String getMD5(String s) {
        if (TextUtils.isEmpty(s)) {
            MLog.e(TAG, "getMD5() -> s is null or empty!");
            return ERROR_MD5;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            MLog.e(TAG, "getMD5() -> Exception:" + e.getMessage());
            return ERROR_MD5;
        }
    }

}
