package com.chinapex.android.monitor.utils;

import com.chinapex.android.monitor.bean.UploadConfigRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by SteelCabbage on 2018/3/23.
 */

public class GsonUtils {

    private static final String TAG = GsonUtils.class.getSimpleName();

    private static Gson sGson;

    private GsonUtils() {

    }

    static {
        sGson = new Gson();
    }

    public static String toJsonStr(Object object) {
        String jsonStr = null;
        if (null != sGson) {
            try {
                jsonStr = sGson.toJson(object);
            } catch (Exception e) {
                MLog.e(TAG, "toJsonStr() is exception:" + e.getMessage());
            }
        }
        return jsonStr;
    }

    public static <T> T json2Bean(String jsonStr, Class<T> cls) {
        T t = null;
        if (null != sGson) {
            try {
                t = sGson.fromJson(jsonStr, cls);
            } catch (JsonSyntaxException e) {
                MLog.e(TAG, "json2Bean() is exception:" + e.getMessage());
            }
        }
        return t;
    }

    public static Map<String, UploadConfigRequest.Config.PvBean> json2PvMap(String stringMapJson) {
        Map<String, UploadConfigRequest.Config.PvBean> map = null;
        try {
            Type type = new TypeToken<Map<String, UploadConfigRequest.Config.PvBean>>() {
            }.getType();
            map = sGson.fromJson(stringMapJson, type);
        } catch (JsonSyntaxException e) {
            MLog.e(TAG, "json2StringMap JsonSyntaxException:" + e.getMessage());
        }
        return map;
    }

    public static Map<String, UploadConfigRequest.Config.ClickBean> json2ClickMap(String stringMapJson) {
        Map<String, UploadConfigRequest.Config.ClickBean> map = null;
        try {
            Type type = new TypeToken<Map<String, UploadConfigRequest.Config.ClickBean>>() {
            }.getType();
            map = sGson.fromJson(stringMapJson, type);
        } catch (JsonSyntaxException e) {
            MLog.e(TAG, "json2StringMap JsonSyntaxException:" + e.getMessage());
        }
        return map;
    }

    public static Map<String, UploadConfigRequest.Config.ListBean> json2ListMap(String stringMapJson) {
        Map<String, UploadConfigRequest.Config.ListBean> map = null;
        try {
            Type type = new TypeToken<Map<String, UploadConfigRequest.Config.ListBean>>() {
            }.getType();
            map = sGson.fromJson(stringMapJson, type);
        } catch (JsonSyntaxException e) {
            MLog.e(TAG, "json2StringMap JsonSyntaxException:" + e.getMessage());
        }
        return map;
    }

    public static Map<String, String> json2StringMap(String stringMapJson) {
        Map<String, String> map = null;
        try {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            map = sGson.fromJson(stringMapJson, type);
        } catch (JsonSyntaxException e) {
            MLog.e(TAG, "json2StringMap JsonSyntaxException:" + e.getMessage());
        }
        return map;
    }

    public static <T> List<T> json2List(String listJson, Class<T> cls) {
        Type type = new ParameterizedTypeImpl(cls);
        List<T> list = null;
        try {
            list = sGson.fromJson(listJson, type);
        } catch (JsonSyntaxException e) {
            MLog.e(TAG, "json2List() is exception:" + e.getMessage());
        }
        return list;
    }


    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class mCls;

        ParameterizedTypeImpl(Class cls) {
            mCls = cls;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{mCls};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
