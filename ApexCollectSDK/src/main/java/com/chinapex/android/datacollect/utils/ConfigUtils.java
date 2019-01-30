package com.chinapex.android.datacollect.utils;

import android.text.TextUtils;

import com.chinapex.android.datacollect.global.ApexCache;
import com.chinapex.android.datacollect.global.Constant;
import com.chinapex.android.datacollect.model.bean.response.UpdateConfigResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author SteelCabbage
 * @date 2019/01/10
 */
public class ConfigUtils {

    private static final String TAG = ConfigUtils.class.getSimpleName();

    /**
     * 通过getPathContainTag得到的list, 生成pageName
     *
     * @param viewPath 长度至少为2, 0:viewPath, 1:activityName, 2+:fragmentName
     * @return
     */
    public static String getPageClassName(ArrayList<String> viewPath) {
        if (null == viewPath) {
            ATLog.e(TAG, "getPageClassName() -> viewPath is null!");
            return null;
        }

        int size = viewPath.size();
        if (viewPath.size() < Constant.VIEW_PATH_SIZE) {
            ATLog.e(TAG, "getPageClassName() -> viewPath is illegal!");
            return null;
        }

        String activityName = viewPath.get(1);
        if (size == Constant.VIEW_PATH_SIZE) {
            ATLog.v(TAG, "viewPath[1] activityName:" + activityName);
            return activityName;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(activityName);
        builder.append(Constant.SEPARATOR_PAGE_NAME);
        int offset = builder.length();
        for (int i = 2; i < size; i++) {
            String fragment = viewPath.get(i);
            if (i == 2) {
                builder.insert(offset, fragment);
            } else {
                builder.insert(offset, Constant.SEPARATOR_PAGE_NAME);
                builder.insert(offset, fragment);
            }
        }
        return builder.toString();
    }

    /**
     * @param object   列表中的每个子条目
     * @param dataPath item#mInfo#nameInner
     */
    public static String getDataKey(Object object, String dataPath) {
        if (null == object || TextUtils.isEmpty(dataPath)) {
            ATLog.e(TAG, "getDataKey() -> object or dataPath is null!");
            return null;
        }

        String[] array;
        try {
            array = dataPath.split(Constant.SEPARATOR_DATA_PATH);
            if (array.length == 0) {
                ATLog.e(TAG, "getDataKey() -> array's length is 0!");
                return null;
            }
        } catch (Exception e) {
            ATLog.e(TAG, "getDataKey() -> split Exception:" + e.getMessage());
            return null;
        }

        if (array.length == 1) {
            if (Constant.DATA_PATH_TAG.equals(array[0])) {
                ATLog.i(TAG, "getDataKey() -> dataKey is baseType!");
                return getStringFromBaseType(object);
            }

            ATLog.e(TAG, "getDataKey() -> dataPath is illegal! It must be start with " + Constant.DATA_PATH_TAG);
            return null;
        }

        Object needReflect = object;
        for (int i = 1; i < array.length; i++) {
            try {
                if (null == needReflect) {
                    ATLog.e(TAG, "getDataKey() -> needReflect is null!");
                    continue;
                }

                Field field = needReflect.getClass().getDeclaredField(array[i]);
                field.setAccessible(true);
                needReflect = field.get(needReflect);
            } catch (Exception e) {
                ATLog.e(TAG, "getDataKey() -> reflect Exception:" + e.getMessage());
            }
        }

        return getStringFromBaseType(needReflect);
    }

    public static String getStringFromBaseType(Object object) {
        if (null == object) {
            ATLog.e(TAG, "getStringFromBaseType() -> object is null!");
            return null;
        }

        String clsName = object.getClass().getSimpleName();
        if (TextUtils.isEmpty(clsName)) {
            ATLog.e(TAG, "getStringFromBaseType() -> clsName is null or empty!");
            return null;
        }

        String dataKey = null;
        switch (clsName) {
            case "Long":
            case "Integer":
            case "Boolean":
            case "Float":
                dataKey = String.valueOf(object);
                break;
            case "String":
                dataKey = (String) object;
                break;
            default:
                break;
        }

        return dataKey;
    }

    public static UpdateConfigResponse.DataBean.Config.ClickBean getConfigClickBean(String viewPathMD5) {
        if (TextUtils.isEmpty(viewPathMD5)) {
            ATLog.e(TAG, "getConfigClickBean() -> viewPathMD5 is null or empty!");
            return null;
        }

        Map<String, UpdateConfigResponse.DataBean.Config.ClickBean> configClick = ApexCache.getInstance().getConfigClick();
        if (null == configClick || configClick.isEmpty()) {
            ATLog.e(TAG, "GenerateClickEventData run() -> configClick is null or empty!");
            return null;
        }

        return configClick.get(viewPathMD5);
    }

    public static UpdateConfigResponse.DataBean.Config.PvBean getConfigPvBean(String viewPathMD5) {
        if (TextUtils.isEmpty(viewPathMD5)) {
            ATLog.e(TAG, "getConfigPvBean() -> viewPathMD5 is null or empty!");
            return null;
        }

        Map<String, UpdateConfigResponse.DataBean.Config.PvBean> configPv = ApexCache.getInstance().getConfigPv();
        if (null == configPv || configPv.isEmpty()) {
            ATLog.e(TAG, "getConfigPvBean() -> configPv is null or empty!");
            return null;
        }

        return configPv.get(viewPathMD5);
    }

    public static UpdateConfigResponse.DataBean.Config.ListBean getConfigListBean(String listIdMD5) {
        if (TextUtils.isEmpty(listIdMD5)) {
            ATLog.e(TAG, "getConfigListBean() -> listIdMD5 is null or empty!");
            return null;
        }

        Map<String, UpdateConfigResponse.DataBean.Config.ListBean> configList = ApexCache.getInstance().getConfigList();
        if (null == configList || configList.isEmpty()) {
            ATLog.e(TAG, "getConfigListBean() -> configList is null or empty!");
            return null;
        }

        return configList.get(listIdMD5);
    }

}
