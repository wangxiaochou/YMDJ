package com.ypshengxian.daojia.network.interceptor;

import com.ypshengxian.daojia.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-24
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class DataInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();


        Response response = chain.proceed(request);
        byte[]  sss=response.body().bytes();
        String s=new String(sss,"UTF-8");
        if("[]".equals(s)){
            LogUtils.e("返回了中括号");
        }


        MediaType mediaType = response.body().contentType();
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, sss))
                .build();
    }
}