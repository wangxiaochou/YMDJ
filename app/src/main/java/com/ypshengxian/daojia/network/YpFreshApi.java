package com.ypshengxian.daojia.network;


import com.ypshengxian.daojia.BuildConfig;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.network.interceptor.TokenInterceptor;
import com.ypshengxian.daojia.network.service.YpFreshService;
import com.ypshengxian.daojia.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-02-08
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class YpFreshApi {
    /** 接口定义静态成员,用来调用方法 */
    private static YpFreshApi sSSApi;
    /** 泡泡服务 */
    private YpFreshService mSSService;


    /**
     * 获得本类的实例  ,单例
     */
    private YpFreshApi() {

    }

    public static YpFreshApi newInstance() {
        if (null == sSSApi) {
            synchronized (YpFreshApi.class) {
                if (null == sSSApi) {
                    sSSApi = new YpFreshApi();
                }
            }
        }
        return sSSApi;

    }

    /**
     * 单例
     *
     * @return 基础服务
     */
    public YpFreshService getSSService() {
        if (null == mSSService) {
            List<Interceptor> ssInterceptorList = new ArrayList<>();
            if (AppUtils.isAppDebug()) {
                ssInterceptorList.add(new LoggerInterceptor());
            }
            ssInterceptorList.add(new TokenInterceptor(new TokenInterceptor.ISessionListener() {
                @Override
                public String onGetSession() {
                    return "";
                }
            }));

            mSSService = RetrofitWrapper.createInstance(BuildConfig.api+Count.WORK_URL, RetrofitWrapper.CONVERTER_GSON,
                    ssInterceptorList, false).create(YpFreshService.class);
        }
        return mSSService;
    }


}
