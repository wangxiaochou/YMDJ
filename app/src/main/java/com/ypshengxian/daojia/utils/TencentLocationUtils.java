package com.ypshengxian.daojia.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * 腾讯定位服务
 *
 * @author Yan
 * @date 2018-04-01
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class TencentLocationUtils {
    /** 单例  实例 */
    private static TencentLocationUtils sInstance;
    /** 定位请求 */
    private TencentLocationRequest mRequest;
    /** 锁对象 */
    private Object mObjLock = new Object();
    /** 定位周期 */
    private static  final  int TIME=3000;

    private TencentLocationManager  mManager;
    /** 定位结果接口 */
    private ILocationResult sILocationResult;
    /** 定位服务 */
    private TencentLocationListener mTencentLocationListener=new TencentLocationListener() {
        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            if(TencentLocation.ERROR_OK==i){
                LogUtils.e(tencentLocation.getCity());
                LogUtils.e(tencentLocation.getAddress());
                LogUtils.e(tencentLocation.getName());
                if(null!=tencentLocation&&!TextUtils.isEmpty(tencentLocation.getCity())){
                    sILocationResult.onReceiveLocation(tencentLocation);
                    stopLocation(mTencentLocationListener);
                }
            }
        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    };
    /**
     * 单例
     *
     * @return 实例
     */
    public static TencentLocationUtils newInstance() {
        if (null == sInstance) {
            synchronized (TencentLocationUtils.class) {
                if (null == sInstance) {
                    sInstance = new TencentLocationUtils(Utils.getContext());
                }
            }
        }

        return sInstance;
    }

    /**
     * 私有构造函数
     *
     *@param context  上下文
     */
    private TencentLocationUtils(Context context) {
        synchronized (mObjLock) {
            if ( mRequest== null) {
                mRequest = TencentLocationRequest.create();
                mRequest.setInterval(TIME);
                mRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
                mRequest.setAllowCache(true);
                mManager=TencentLocationManager.getInstance(context);
                int error=mManager.requestLocationUpdates(mRequest,mTencentLocationListener);
                LogUtils.e("当前错误码"+error);
            }
        }
    }
    /**
     * 停止位置服务
     *
     * @param listener 监听
     */
    private void startLocation(TencentLocationListener listener){
        if (null==mManager){
            mManager=TencentLocationManager.getInstance(Utils.getContext());
        }
        mManager.requestLocationUpdates(mRequest,listener);
    }

    /**
     * 开启定位服务
     *
     * @param listener 位置结果监听器
     */
    public  void startService(ILocationResult listener) {
        startService(mTencentLocationListener);
        sILocationResult = listener;
    }

    /**
     * 注销定位服务
     *
     * @param listener 位置监听器
     */
    public void stopService(ILocationResult listener) {
       stopService(mTencentLocationListener);
    }

    /**
     * 注销定位服务
     *
     * @param listener 位置监听器
     */
    public  void stopService(TencentLocationListener listener) {
        TencentLocationUtils locationService = newInstance();
        locationService.stopLocation(listener);
    }
    /**
     * 开启定位服务
     *
     * @param listener 位置数据监听器
     */
    public void startService(TencentLocationListener listener) {
        TencentLocationUtils locationService = newInstance();
        locationService.startLocation(listener);
    }


    /**
     * 停止位置服务
     *
     * @param listener 监听
     */
    private void stopLocation(TencentLocationListener listener){
        if (null==mManager){
            mManager=TencentLocationManager.getInstance(Utils.getContext());
        }
        mManager.removeUpdates(listener);
    }
    /**
     * 定位结果接口
     */
    public interface ILocationResult {
        /**
         * 获取到了定位信息
         *
         * @param location 位置数据
         * @note 1. 此方法只会在真正获取到位置数据后才会回调
         * 2. 获取到了数据会自动关闭百度定位服务
         */
        void onReceiveLocation(TencentLocation location);
    }

}
