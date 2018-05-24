package com.ypshengxian.daojia.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-04-06
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class SignUtils {
    public static String sign(String method, String appSecret, Map<String, String> headersParams, String pathWithParameter, Map<String, String> queryParams, Map<String, String> formParam)  {
        try {
            Mac e = Mac.getInstance("HmacSHA256");
            byte[] keyBytes =appSecret.getBytes("UTF-8");
            e.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
            String signString = buildStringToSign(method, headersParams, pathWithParameter, queryParams, formParam);
            byte[] signResult = e.doFinal(signString.getBytes("UTF-8"));
            byte[] base64Bytes = Base64.encode(signResult, Base64.NO_WRAP);
                return new String(base64Bytes, "UTF-8");
        } catch (InvalidKeyException|NoSuchAlgorithmException|UnsupportedEncodingException e){
            throw new  RuntimeException(e);
        }
    }

    private static String buildStringToSign(String method, Map<String, String> headerParams, String pathWithParameter, Map<String, String> queryParams, Map<String, String> formParams) {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append("\n");
        if(headerParams.get("Accept") != null) {
            sb.append((String)headerParams.get("Accept"));
        }

        sb.append("\n");
        if(headerParams.get("Content-MD5") != null) {
            sb.append((String)headerParams.get("Content-MD5"));
        }

        sb.append("\n");
        if(headerParams.get("Content-Type") != null) {
            sb.append((String)headerParams.get("Content-Type"));
        }

        sb.append("\n");
        if(headerParams.get("Date") != null) {
            sb.append((String)headerParams.get("Date"));
        }

        sb.append("\n");
        sb.append(buildHeaders(headerParams));
        sb.append(buildResource(pathWithParameter, queryParams, formParams));
        return sb.toString();
    }

    private static String buildResource(String pathWithParameter, Map<String, String> queryParams, Map<String, String> formParams) {
        StringBuilder result = new StringBuilder();
        result.append(pathWithParameter);
        TreeMap parameter = new TreeMap();
        if(null != queryParams && queryParams.size() > 0) {
            parameter.putAll(queryParams);
        }

        if(null != formParams && formParams.size() > 0) {
            parameter.putAll(formParams);
        }

        if(parameter.size() > 0) {
            result.append("?");
            boolean isFirst = true;
            Iterator var6 = parameter.keySet().iterator();

            while(var6.hasNext()) {
                String key = (String)var6.next();
                if(!isFirst) {
                    result.append("&");
                } else {
                    isFirst = false;
                }

                result.append(key);
                String value = (String)parameter.get(key);
                if(null != value && !"".equals(value)) {
                    result.append("=").append(value);
                }
            }
        }

        return result.toString();
    }

    private static String buildHeaders(Map<String, String> headers) {
        TreeMap headersToSign = new TreeMap();
        StringBuilder sb;
        if(headers != null) {
            sb = new StringBuilder();
            int flag = 0;
            Iterator e = headers.entrySet().iterator();

            while(e.hasNext()) {
                Map.Entry header = (Map.Entry)e.next();
                if(((String)header.getKey()).startsWith("X-Ca-")) {
                    if(flag != 0) {
                        sb.append(",");
                    }

                    ++flag;
                    sb.append((String)header.getKey());
                    headersToSign.put(header.getKey(), header.getValue());
                }
            }

            headers.put("X-Ca-Signature-Headers", sb.toString());
        }

        sb = new StringBuilder();
        Iterator var6 = headersToSign.entrySet().iterator();

        while(var6.hasNext()) {
            Map.Entry var7 = (Map.Entry)var6.next();
            sb.append((String)var7.getKey()).append(':').append((String)var7.getValue()).append("\n");
        }

        return sb.toString();
    }
}
