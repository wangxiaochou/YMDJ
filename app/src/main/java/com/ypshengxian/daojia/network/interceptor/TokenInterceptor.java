package com.ypshengxian.daojia.network.interceptor;


import android.text.TextUtils;

import com.ypshengxian.daojia.BuildConfig;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.network.exception.SessionStatusException;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.PhoneUtils;
import com.ypshengxian.daojia.utils.SignUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 用户等毒后为每个网络请求添加公共参数
 *
 * @author Yan
 * @date 2017-11-20
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class TokenInterceptor implements Interceptor {
    /** 重新调用源请求的次数 */
    private static final int REQUEST_MAX_TIMES = 1;
    /** Session状态监听(全局) */
    private static ISessionStatusListener sSessionStatusListener;
    /** Session创建监听(全局) */
    private static ISessionCreateListener sSessionCreateListener;
    /** Session监听器 */
    private ISessionListener mSessionListener;
    /** 方法 */
    private String method;
    /** 路径 */
    private String path;
    /** header */
    private Map<String, String> map = new WeakHashMap<String,String>();
    /**get */
    private Map<String, String> queryParams = new WeakHashMap<String,String>();
    /**post */
    private Map<String, String> formParams = new WeakHashMap<String,String>();
    /** 服务器返回的cookie */
    private String cookie="";


    /**
     * 构造函数
     *
     * @param sessionListener Session监听
     */
    public TokenInterceptor(ISessionListener sessionListener) {
        mSessionListener = sessionListener;
    }

    /**
     * 设置Session状态监听
     *
     * @param listener 监听器
     */
    public static void setSessionStatusListener(ISessionStatusListener listener) {
        sSessionStatusListener = listener;
    }

    /**
     * 设置Session创建监听
     *
     * @param listener 监听器
     * @note 监听器必须实现重新创建session的逻辑，并且满足如下条件：
     * 1. 创建成功，抛出创建成功异常。
     * 2. 创建失败，抛出创建失败异常。
     */
     /*
      * 示例代码：
      *
      *
      *
      *  private <R> Observable<R> onCreateSession() {
      *      // 重新创建会话
      *      AgentService as = OpenPlatApi.getAgentService();
      *      AgentSP sp = AgentSP.getInstance();
      *
      *      return as.createSession(sp.getSn(), DynamicValues.getIMSI(), DynamicValues.getIMEI(),
      *              ENVs.PLATFORM_NAME, android.os.Build.MODEL, String.valueOf(AppUtils.getAppVersionCode()),
      *              AppUtils.getAppVersionName()).flatMap(new Func1<AgentLogin, Observable<R>>() {
      *          @Override
      *          public Observable<R> call(AgentLogin data) {
      *              if (data != null && data.code == OpenPlatCode.CODE_SUCCESS && data.data != null) {
      *                  AgentSP asp = AgentSP.getInstance();
      *                  AgentLogin.Data agentData = data.data;
      *                  asp.saveLoginInfo(agentData.opName, agentData.sn, agentData.msisdn);
      *                  // 设置动态密钥
      *                  OpenPlatApi.setDynamicDESKeyFromRSA(agentData.encodeKey);
      *
      *                  throw new SessionStatusException(SessionStatusException.STATUS_CREATED);
      *              }
      *
      *              throw new SessionStatusException(SessionStatusException.STATUS_CREATE_FAILED);
      *          }
      *      });
      *  }
      */
    public static void setSessionCreateListener(ISessionCreateListener listener) {
        sSessionCreateListener = listener;
    }

    /**
     * 使用Session转换器
     *
     * @return 用于Session过期后，重新创建Session
     */
    public static <R> Observable.Transformer<R, R> applySessionCreator() {

        return new Observable.Transformer<R, R>() {
            /** 会话创建重试次数 */
            private int mRequestCount = 0;

            @Override
            public Observable<R> call(Observable<R> observable) {

                return observable.onErrorResumeNext(new Func1<Throwable, Observable<? extends R>>() {
                    @Override
                    public Observable<? extends R> call(Throwable throwable) {
                        if (throwable instanceof SessionStatusException) {
                            SessionStatusException exception = (SessionStatusException) throwable;
                            if (exception.getSessionStatus() == SessionStatusException.STATUS_TIMEOUT &&
                                    mRequestCount < REQUEST_MAX_TIMES) {
                                // 第一次会话过期
                                mRequestCount++;

                                if (sSessionCreateListener != null) {

                                    return sSessionCreateListener.onCreateSession();
                                }
                            }
                        }

                        return Observable.error(throwable);
                    }
                }).retry(new Func2<Integer, Throwable, Boolean>() {
                    @Override
                    public Boolean call(Integer integer, Throwable throwable) {
                        if (throwable instanceof SessionStatusException) {
                            SessionStatusException exception = (SessionStatusException) throwable;
                            if (exception.getSessionStatus() == SessionStatusException.STATUS_CREATED) {
                                // 会话已重新创建
                                return true;

                            } else {
                                if (sSessionStatusListener != null) {
                                    sSessionStatusListener.onCreateSessionFailed();
                                }
                            }
                        }

                        return false;
                    }
                });
            }
        };
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取Session
       queryParams.clear();
       formParams.clear();
        Request request = chain.request();
        method = request.method();
        String url = request.url().toString();
        if (TextUtils.equals(Count.METHOD_POST, method)) {
            //post请求
            String[] postStrings = url.split(Count.WORK_URL);
            path = postStrings[1];
            RequestBody reqBody = request.body();
            if (reqBody instanceof FormBody) {
                FormBody body = (FormBody) reqBody;
                int size = body.size();
                for (int i = 0; i < size; i++) {
                    //这个 是post的参数
                    formParams.put(body.name(i), body.value(i));
                }


            }
        } else {
            String queryString=null;
            // get请求
            // Request:http://api.ypshengxian.com/homepage?rtretr=2
            String[] getStrings = url.split(Count.WORK_URL);
            String need = getStrings[1];
            if(need.contains(Count.SYMBOL)){
                String[] needs = need.split("\\?");
                path = needs[0];
                queryString=needs[1];
            }else {
                path=need;
            }

            queryParams = transStringToMap(queryString);
        }

        // 处理请求
        //处理get请求编码问题
       String oldString=queryParams.get("search");
        if(!TextUtils.isEmpty(oldString)){
            queryParams.put("search",URLDecoder.decode(oldString,"UTF-8"));
        }




        Request newRequest = processRequest(chain.request(), method, path, queryParams, formParams);
        if (newRequest == null) {
            return chain.proceed(chain.request());
        }
        Response response = chain.proceed(newRequest);
        // 执行请求


        // 是否忽略检测Session
        if (ignoreCheckSession()) {

            return response;
        }

        // 服务器返回的"cookie"
        cookie = response.header("Set-Cookie");
        YpCache.setCookie(cookie);

        return response;
    }

    /**
     * 忽略检测Session
     *
     * @return true -- 忽略(不检测Session过期) false -- 不忽略(检测Session过期)
     * @note 子类可重写此方法，进行拦截
     */

    protected boolean ignoreCheckSession() {
        return false;
    }

    /**
     * string  转map
     * @param mapString str
     * @return map
     */
    private static Map<String,String> transStringToMap(String mapString){
        if(!TextUtils.isEmpty(mapString)) {
            Map<String, String> map = new HashMap<String, String>();
            java.util.StringTokenizer items;
            for (StringTokenizer entrys = new StringTokenizer(mapString, "&"); entrys.hasMoreTokens();
                 map.put(items.nextToken(), items.hasMoreTokens() ? ((String) (items.nextToken())) : "")) {
                items = new StringTokenizer(entrys.nextToken(), "=");
            }
            return map;
        }
        return  new WeakHashMap<String,String>();
    }


    /**
     * 处理请求
     * 
     * @param oldRequest 老的地址
     * @param method 方法
     * @param path  路径
     * @param get  get的参数
     * @param post post的参数
     * @return 请求
     * @throws UnsupportedEncodingException 错误
     */
    private Request processRequest(Request oldRequest, String method, String path,
                                   Map<String, String> get, Map<String, String> post)
            throws UnsupportedEncodingException {
        map.clear();

        String time = String.valueOf(System.currentTimeMillis());
        // 更新header,添加公共參數
        Headers.Builder headersBuilder = oldRequest.headers().newBuilder();
        headersBuilder.add("X-Ca-Key", BuildConfig.appKey);
        headersBuilder.add("X-Ca-Timestamp", time);
        headersBuilder.add("X-Ca-Nonce", PhoneUtils.getUniquePsuedoID()+ time);
        headersBuilder.add("Accept", "application/json");
        headersBuilder.add("X-Ca-Signature-Headers", "X-Ca-Key,X-Ca-Timestamp,X-Ca-Nonce");
        map.put("X-Ca-Key", BuildConfig.appKey);
        map.put("X-Ca-Timestamp", time);
        map.put("X-Ca-Nonce", PhoneUtils.getUniquePsuedoID() + time);
        map.put("Accept", "application/json");
        if(Count.METHOD_POST.equals(method)){
                map.put("Content-Type", "application/x-www-form-urlencoded");
        }

        if(Count.METHOD_GET.equals(method)){
            if(map.containsKey("Content-Type")){
                map.remove("Content-Type");
            }
        }
        if(map.containsKey("X-Ca-Signature-Headers")){
            map.remove("X-Ca-Signature-Headers");
        }
        LogUtils.e(post.get("reasonImg[]"));


        String s = SignUtils.sign(method, BuildConfig.appId+Count.ALI_APP_ID, map, path, get, post);
        LogUtils.e(s);
        headersBuilder.add("X-Ca-Signature", s);
        headersBuilder.add("X-Ca-Stage",YPPreference.newInstance().getDebug());
       //添加Cookie
      if(null!=YpCache.getCookie()){
          headersBuilder.add("Cookie",YpCache.getCookie());
      }

      if(YPPreference.newInstance().getLogin()){
          String token=YPPreference.newInstance().getUserToken();
          if(!TextUtils.isEmpty(token)) {
              headersBuilder.add("X-Auth-Token",token );
          }
      }

        // 创建请求
        Request.Builder builder = oldRequest.newBuilder();
        builder.url(oldRequest.url())
                .headers(headersBuilder.build());
        //清理数据
        YpCache.clearMap();
        return builder.build();
    }

    /**
     * Session监听器
     */
    public interface ISessionListener {
        /**
         * 获取Session值
         *
         * @return Session字符串
         */
        String onGetSession();

    }

    /**
     * Session创建监听
     */
    public interface ISessionCreateListener {
        /**
         * 创建Session
         *
         * @return 创建Session的可观测序列
         * @note 请阅读示例代码：setSessionCreateListener
         */
        <R> Observable<R> onCreateSession();
    }

    /**
     * Session状态监听
     */
    public interface ISessionStatusListener {
        /**
         * 创建Session失败
         */
        void onCreateSessionFailed();
    }
}
