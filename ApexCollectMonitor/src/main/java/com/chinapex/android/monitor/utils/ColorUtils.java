package com.chinapex.android.monitor.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * @author wyhusky
 * @date 2019/1/10
 */
public class ColorUtils {

    public static int colorPointer = 0;
    public static Random random = new Random();
    /**
     * 饼图预留颜色
     */
    private static final int[] PRE_COLOR_ARRAY = {

    };

    public static int[] generateColorArray(int nums) {
        if (nums <= PRE_COLOR_ARRAY.length) {
            return PRE_COLOR_ARRAY;
        } else {
            int[] colorArray = new int[nums];
            int i = 0;
            for (; i < PRE_COLOR_ARRAY.length; i++) {
                colorArray[i] = PRE_COLOR_ARRAY[i];
            }
            for (; i < nums; i++) {
                int red = random.nextInt(255);
                int green = random.nextInt(255);
                int blue =random.nextInt(255);
                colorArray[i] = Color.rgb(red, green, blue);
            }
            return colorArray;
        }
    }

    public static int generateColor() {
        if (colorPointer < PRE_COLOR_ARRAY.length) {
            return PRE_COLOR_ARRAY[colorPointer++];
        }

        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue =random.nextInt(255);
        return Color.rgb(red, green, blue);
    }
}
