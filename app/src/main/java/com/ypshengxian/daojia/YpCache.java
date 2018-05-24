package com.ypshengxian.daojia;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 缓存
 *
 * @author Yan
 * @date 2018-03-24
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class YpCache  {

    private static Map<String,String> get=new WeakHashMap<String,String>();
    private static Map<String,String> post=new WeakHashMap<String,String>();
    private static String method;
    private static String path;
    private static String cookie;

    public static Map<String, String> getPost() {
        return post;
    }

    public static void setPost(Map<String, String> post) {
        post.clear();
        YpCache.post = post;
    }

    public static String getMethod() {
        return method;
    }

    public static void setMethod(String method) {
        YpCache.method = method;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        YpCache.path = path;
    }

    public static Map<String, String> getMap() {

        return get;
    }

    public static void setMap(Map<String, String> getMap) {
        get.clear();
        YpCache.get = getMap;
    }


    public static void setGet(Map<String, String> get) {
        YpCache.get = get;
    }

    public static String getCookie() {
        return cookie;
    }

    public static void setCookie(String cookie) {
        YpCache.cookie = cookie;
    }

    public static void clearMap(){
        get.clear();
        post.clear();
    }
}
