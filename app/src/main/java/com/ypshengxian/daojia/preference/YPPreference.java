package com.ypshengxian.daojia.preference;

import android.util.Base64;

import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.utils.JsonUtil;
import com.ypshengxian.daojia.utils.PhoneUtils;
import com.ypshengxian.daojia.utils.SPUtil;
import com.ypshengxian.daojia.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-03-29
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class YPPreference {

    /** 单例  实例 */
    private static YPPreference sInstance;
    /** SP实例 */
    private SPUtil mSPInstance;

    private YPPreference() {
        mSPInstance = new SPUtil(Utils.getContext(), YPPreference.class.getSimpleName());
    }

    /**
     * 单例
     *
     * @return 实例
     */
    public static YPPreference newInstance() {
        if (null == sInstance) {
            synchronized (YPPreference.class) {
                if (null == sInstance) {
                    sInstance = new YPPreference();
                }
            }
        }

        return sInstance;
    }

    /**
     * 保存用户的token
     */
    public void setUserToken(String token) {

        mSPInstance.putString(Count.USER_TOKEN, token);
    }

    /**
     * 获得用户的token
     *
     * @return token
     */
    public String getUserToken() {
        return mSPInstance.getString(Count.USER_TOKEN, "");
    }

    /**
     * 保存用户的phone
     */
    public void setUserPhone(String phone) {
        if (phone.isEmpty()) {
            return;
        }
        mSPInstance.putString(Count.USER_PHONE, Base64.encodeToString(phone.getBytes(), Base64.DEFAULT));
    }

    /**
     * 获得用户的phone
     *
     * @return phone
     */
    public String getUserPhone() {
        return new String(Base64.decode(mSPInstance.getString(Count.USER_PHONE, "").getBytes(), Base64.DEFAULT));
    }

    /**
     * 保存用户的的登录状态
     *
     * @param isLogin 是否登录
     */
    public void setLogin(boolean isLogin) {

        mSPInstance.putBoolean(Count.IS_LOGIN, isLogin);
    }

    /**
     * 获得用户的登录状态
     *
     * @return 是|否
     */
    public boolean getLogin() {

        return mSPInstance.getBoolean(Count.IS_LOGIN, false);
    }

    /**
     * 保存用户店铺Id
     *
     * @param shopId 店铺Id
     */
    public void setShopId(String shopId) {
        mSPInstance.putString(Count.SHOP_ID, shopId);
    }

    /**
     * 获得用户店铺Id
     */
    public String getShopId() {
        return mSPInstance.getString(Count.SHOP_ID, "");
    }

    /**
     * 设置已选中的门店二级信息
     */
    public void setChooseShopTwo(String cityId) {
        mSPInstance.putString(Count.SHOP_TWO, cityId);
    }

    /**
     * 获得已选中的门店二级信息
     */
    public String getChooseShopTwo() {
        return mSPInstance.getString(Count.SHOP_TWO, "");
    }

    /**
     * 设置已选中的门店三级信息
     */
    public void setChooseShopTheree(String childId) {
        mSPInstance.putString(Count.SHOP_THREE, childId);
    }

    /**
     * 获得已选中的门店三级信息
     */
    public String getChooseShopThree() {
        return mSPInstance.getString(Count.SHOP_THREE, "");
    }


    /**
     * 设置定位的城市信息信息
     *
     * @param cityName 城市名字
     */
    public void setCityName(String cityName) {
        mSPInstance.putString(Count.CITY_NAME, cityName);
    }

    /**
     * 获得定位的城市信息
     */
    public String getCityName() {
        return mSPInstance.getString(Count.CITY_NAME, "");
    }

    /**
     * 设置店铺名称
     *
     * @param shopName 店铺名字
     */
    public void setShopName(String shopName) {
        mSPInstance.putString(Count.SHOP_NAME, shopName);
    }

    /**
     * 获得店铺名称
     */
    public String getShopName() {
        return mSPInstance.getString(Count.SHOP_NAME, "");
    }

    /**
     * 用户是不是第一次进入App
     *
     * @param isFirst 是否
     */
    public void setIsFirst(boolean isFirst) {
        mSPInstance.putBoolean(Count.IS_FIRST, isFirst);
    }

    /**
     * 用户是不是第一次进入App
     *
     * @return 是否
     */
    public boolean getIsFirst() {
        return mSPInstance.getBoolean(Count.IS_FIRST, true);
    }

    /**
     * 设置服务器版本
     *
     * @param type 版本 count.TEST,PRE,RELEASE
     * @see Count
     */
    public void setDebug(String type) {
        mSPInstance.putString(Count.INFORMATION, type);
    }

    /**
     * 获得服务器版本
     *
     * @see Count
     * @return  type   版本 count.TEST,PRE,RELEASE
     */
    public String getDebug() {
        return mSPInstance.getString(Count.INFORMATION, Count.TEST);
    }

    /**
     * 用户是不是第一次定位
     *
     * @param isFirstLocation 是否
     */
    public void setIsFirstLocation(boolean isFirstLocation) {
        mSPInstance.putBoolean(Count.IS_FIRST_LOCATION, isFirstLocation);
    }

    /**
     * 获得用户是不是第一次定位
     *
     * @return 是否
     */
    public boolean getIsFirstLocation() {
        return mSPInstance.getBoolean(Count.IS_FIRST_LOCATION, false);
    }


    /**
     * //保存搜索字段
     *
     * @param id 用户id
     * @param his 输入历史
     */
    public void saveMap(String id, String his) {
        List<String> oldList = getOutHis(PhoneUtils.getUniquePsuedoID());
        if (oldList != null) {
            int size = oldList.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    if (his.equals(oldList.get(i))) {
                        oldList.remove(i);
                        break;
                    }
                }
            }
        } else {
            oldList = new ArrayList<String>();
        }
        oldList.add(his);
        Map<String, List<String>> map = new WeakHashMap<String, List<String>>();
        map.put(id, oldList);
        String json = JsonUtil.listToJsonString(map);
        mSPInstance.putString(Count.SEARCH_HISTORY, json);
    }

    /**
     * 取出对应id的搜索历史
     *
     * @param id 用户id
     * @return 输入集合
     */

    public List<String> getOutHis(String id) {
        String json = mSPInstance.getString(Count.SEARCH_HISTORY, "");
        if (json != null) {
            Map<String, List<String>> ls = JsonUtil.stringJsonToMap(json);
            if (null != ls) {
                for (String key : ls.keySet()) {
                    if (id.equals(key)) {
                        return ls.get(key);
                    }
                }
            }
        }
        return new ArrayList<String>();
    }

    /**
     * 清空这个key
     */
    public void keyDelect() {
        mSPInstance.putString(Count.SEARCH_HISTORY, "");
    }


}
