package com.oohoo.spacestationspringbootstarter.utils;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/9
 */
public class MyStringUtils {

    public static String getClassName(String name) {
        String substring = name.substring(name.lastIndexOf(".")+1);
        char[] chars = substring.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }
}
