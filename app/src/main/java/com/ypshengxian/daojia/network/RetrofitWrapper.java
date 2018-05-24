package com.ypshengxian.daojia.network;

import android.content.Context;
import android.text.TextUtils;

import com.ypshengxian.daojia.utils.FileUtils;
import com.ypshengxian.daojia.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Retrofit包装类
 *
 * @author mos
 * @date 2017.02.03
 * @note 1. 若启用ssl，默认会扫描assets/certs目录下的所有证书
 * 2. 若加载证书，即使是CA证书的网站，也必须放入证书文件
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class RetrofitWrapper {
    /** 数据转换器(GSON) */
    public static final String CONVERTER_GSON = "gson";
    /** 数据转换器(String) */
    public static final String CONVERTER_STRING = "string";

    /** 默认连接超时(毫秒) */
    private static final long DEF_CONNECT_TIMEOUT = 45 * 1000;
    /** 默认读取超时(毫秒) */
    private static final long DEF_READ_TIMEOUT = 45 * 1000;
    /** 证书文件目录 */
    private static final String CERT_DIR = "certs";

    /** 连接超时时间 */
    private static long mConnectTimeout = DEF_CONNECT_TIMEOUT;
    /** 读取超时时间 */
    private static long mReadTimeout = DEF_READ_TIMEOUT;
    /** SSL工厂 */
    private static SSLSocketFactory mSSLSocketFactory;
    /** TrustManager */
    private static X509TrustManager mTrustManager;


    /**
     * 私有化构造函数
     */
    private RetrofitWrapper() {
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        sslSetup(context);
    }

    /**
     * 配置SSL(添加https证书)
     *
     * @param context 上下文
     */
    private static void sslSetup(Context context) {
        try {
            // 列出目录中的所有证书
            String[] certFiles = context.getAssets().list(CERT_DIR);
            if (certFiles != null) {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

                // 初始化KeyStore
                keyStore.load(null);

                // 加入证书
                for (String cert : certFiles) {
                    InputStream is = context.getAssets().open(CERT_DIR + "/" + cert);

                    keyStore.setCertificateEntry(cert, certificateFactory.generateCertificate(is));
                    is.close();
                }

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {

                    throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                }
                mTrustManager = (X509TrustManager) trustManagers[0];
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{mTrustManager}, null);
                mSSLSocketFactory = sslContext.getSocketFactory();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param connectTimeout 连接超时时间(毫秒)
     * @param readTimeout 读超时时间(毫秒)
     */
    public static void init(Context context, long connectTimeout, long readTimeout) {
        mConnectTimeout = connectTimeout;
        mReadTimeout = readTimeout;
        sslSetup(context);
    }

    /**
     * 创建默认的Builder
     *
     * @param connectTimeout 连接超时
     * @param readTimeout 读取超时
     * @param sslEnable 是否加载ssl证书
     * @return builder对象
     */
    private static OkHttpClient.Builder createOkHttpBuilder(long connectTimeout, long readTimeout, boolean sslEnable) {
        // 参数修正
        if (connectTimeout <= 0) {
            connectTimeout = mConnectTimeout;
        }
        if (readTimeout <= 0) {
            readTimeout = mReadTimeout;
        }

        // OkHttp初始化
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {

                        return true;
                    }
                });

        // 是否使用SSL支持
        if (mSSLSocketFactory != null && mTrustManager != null && sslEnable) {
            okBuilder.sslSocketFactory(mSSLSocketFactory, mTrustManager);
        }

        return okBuilder;
    }

    /**
     * 获取Retrofit实例
     *
     * @param baseUrl 服务器的URL
     * @param converter 数据转换类型(参见 RetrofitWrapper.CONVERTER_GSON 等，或者自定义converter)
     * @param interceptors 拦截器列表
     * @param connectTimeout 连接超时
     * @param readTimeout 读超时
     * @param sslEnable 是否加载SSL证书
     * @return Retrofit实例
     */
    public static Retrofit createInstance(String baseUrl, Object converter, List<Interceptor> interceptors, long connectTimeout, long readTimeout, boolean sslEnable) {
        // 添加拦截器
        OkHttpClient.Builder builder = createOkHttpBuilder(connectTimeout, readTimeout, sslEnable);
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        // 支持RxJava
        OkHttpClient httpClient = builder.build();
        Retrofit.Builder rtBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient);

        if (converter != null) {
            if (converter instanceof String) {
                String type = (String) converter;
                if (type.equals(CONVERTER_GSON)) {
                    // GSON转换器
                    rtBuilder.addConverterFactory(GsonConverterFactory.create());

                } else if (type.equals(CONVERTER_STRING)) {
                    // String转换器
                    rtBuilder.addConverterFactory(new StringConverterFactory());
                }
            } else if (converter instanceof Converter.Factory) {
                // 自定义转换器
                rtBuilder.addConverterFactory((Converter.Factory) converter);
            }
        }

        return rtBuilder.build();
    }

    /**
     * 获取Retrofit实例
     *
     * @param baseUrl 服务器的URL
     * @param converter 数据转换类型(参见 RetrofitWrapper.CONVERTER_GSON等，或者自定义converter)
     * @param interceptors 拦截器列表
     * @param sslEnable 是否加载SSL证书
     * @return Retrofit实例
     */
    public static Retrofit createInstance(String baseUrl, Object converter, List<Interceptor> interceptors, boolean sslEnable) {

        return createInstance(baseUrl, converter, interceptors, mConnectTimeout, mReadTimeout, sslEnable);
    }

    /**
     * 获取新的实例
     *
     * @param baseUrl 服务器的URL
     * @return Retrofit实例
     * @note 1. 此函数的超时参数，与init函数配置的一致
     * 2. 默认为gson数据转换器
     */
    public static Retrofit createInstance(String baseUrl) {

        return createInstance(baseUrl, CONVERTER_GSON, null, false);
    }

    /**
     * 获取新的实例
     *
     * @param baseUrl 服务器的URL
     * @param sslEnable 是否加载SSL证书
     * @return Retrofit实例
     * @note 1. 此函数的超时参数，与init函数配置的一致
     * 2. 默认为gson数据转换器
     */
    public static Retrofit createInstance(String baseUrl, boolean sslEnable) {

        return createInstance(baseUrl, CONVERTER_GSON, null, sslEnable);
    }

    /**
     * 获取新的实例
     *
     * @param baseUrl 服务器的URL
     * @param converter 数据转换类型(参见 RetrofitWrapper.CONVERTER_GSON 等,，或者自定义converter)
     * @return Retrofit实例
     * @note 1. 此函数的超时参数，与init函数配置的一致
     */
    public static Retrofit createInstance(String baseUrl, Object converter) {

        return createInstance(baseUrl, converter, null, false);
    }

    /**
     * 通过文件创建Multipart
     *
     * @param formDataName 表单名
     * @param contentType 文件的content-type
     * @param file 文件
     * @return multipart对象
     */
    public static MultipartBody.Part createMultipart(String formDataName, String contentType, File file) {
        // 参数检查
        if (TextUtils.isEmpty(formDataName) || file == null || !file.exists()) {

            return null;
        }
        // content-type对应文件的类型，例如：image/jpg, image/png。
        contentType = contentType == null ? "" : contentType;
        RequestBody body = RequestBody.create(MediaType.parse(contentType), FileUtils.readFile2Bytes(file));

        return MultipartBody.Part.createFormData(formDataName, file.getName(), body);
    }

    /**
     * 通过二进制流创建Multipart
     *
     * @param formDataName 表单名
     * @param fileName 文件名(可为null)
     * @param contentType 文件的content-type
     * @param data 数据
     * @return multipart对象
     */
    public static MultipartBody.Part createMultipart(String formDataName, String fileName, String contentType, byte[] data) {
        // 参数检查
        if (TextUtils.isEmpty(formDataName) || data == null) {

            return null;
        }
        // content-type对应文件的类型，例如：image/jpg, image/png。
        contentType = contentType == null ? "" : contentType;
        RequestBody body = RequestBody.create(MediaType.parse(contentType), data);

        return MultipartBody.Part.createFormData(formDataName, fileName, body);
    }

    /**
     * 通过文件创建Multipart
     *
     * @param formDataName 表单名
     * @param file 文件
     * @return multipart对象
     */
    public static MultipartBody.Part createMultipart(String formDataName, File file) {

        return createMultipart(formDataName, "", file);
    }

    /**
     * 创建JSON的RequestBody
     *
     * @param json json数据
     * @return multipart对象
     */
    public static RequestBody createJsonRequestBody(String json) {

        return RequestBody.create(MediaType.parse("application/json"), json);
    }

    /**
     * String转换器
     */
    private static class StringConverterFactory extends Converter.Factory {
        private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (String.class.equals(type)) {

                return new Converter<ResponseBody, String>() {
                    @Override
                    public String convert(ResponseBody value) throws IOException {
                        return value.string();
                    }
                };
            }
            return null;
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

            if (String.class.equals(type)) {
                return new Converter<String, RequestBody>() {
                    @Override
                    public RequestBody convert(String value) throws IOException {
                        return RequestBody.create(MEDIA_TYPE, value);
                    }
                };
            }
            return null;
        }
    }

    /**
     * 延迟重试
     */
    public static class RetryWithDelay implements
            Func1<Observable<? extends Throwable>, Observable<?>> {
        /** 最大重试次数 */
        private final int mMaxRetries;
        /** 重试延迟(毫秒) */
        private final int mRetryDelay;
        /** 已重试次数 */
        private int mRetryCount;

        /**
         * 构造函数
         *
         * @param maxRetries 最大重试次数
         * @param retryDelay 重试延迟(毫秒)
         */
        public RetryWithDelay(int maxRetries, int retryDelay) {
            this.mMaxRetries = maxRetries;
            this.mRetryDelay = retryDelay;
        }

        @Override
        public Observable<?> call(Observable<? extends Throwable> attempts) {
            return attempts
                    .flatMap(new Func1<Throwable, Observable<?>>() {
                        @Override
                        public Observable<?> call(Throwable throwable) {
                            if (++mRetryCount <= mMaxRetries) {
                                LogUtils.d("retry after: " + mRetryDelay + "(" + mRetryCount + ")");
                                return Observable.timer(mRetryDelay,
                                        TimeUnit.MILLISECONDS);
                            }

                            // 超过重试次数，返回错误
                            return Observable.error(throwable);
                        }
                    });
        }
    }
}
