package com.ypshengxian.daojia.utils;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;


/**
 * <p> 手机相关工具类 </p><br>
 *
 * @author lwc
 * @date 2017/3/10 15:58
 * @note -
 * isPhone            : 判断设备是否是手机
 * getIMEI            : 获取IMEI码
 * getIMSI            : 获取IMSI码
 * getPhoneType       : 获取移动终端类型
 * isSimCardReady     : 判断sim卡是否准备好
 * getSimOperatorName : 获取Sim卡运营商名称
 * getSimOperatorByMnc: 获取Sim卡运营商名称
 * getPhoneStatus     : 获取手机状态信息
 * dial               : 跳至拨号界面
 * call               : 拨打phoneNumber
 * sendSms            : 跳至发送短信界面
 * sendSmsSilent      : 发送短信
 * getAllContactInfo  : 获取手机联系人
 * getContactNum      : 打开手机联系人界面点击联系人后便获取该号码
 * getAllSMS          : 获取手机短信并保存到xml中
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class PhoneUtils {
    /**
     * 构造类
     */
    private PhoneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断设备是否是手机
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPhone() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取IMEI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @return IMEI码
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm != null ? tm.getDeviceId() : null;
        } catch (Exception ignored) {

        }
        return getUniquePsuedoID();
    }

    /**
     * 通过读取设备的ROM版本号、厂商名、CPU型号和其他硬件信息来组合出一串15位的号码
     * 其中“Build.SERIAL”这个属性来保证ID的独一无二，当API < 9 无法读取时，使用AndroidId
     *
     * @return 伪唯一ID
     */
    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10;

        String serial;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {
            //获取失败，使用AndroidId
            serial = DeviceUtils.getAndroidID();
            if (TextUtils.isEmpty(serial)) {
                serial = "serial";
            }
        }

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取IMSI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @return IMSI码
     */
    @SuppressLint("HardwareIds")
    public static String getIMSI() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm != null ? tm.getSubscriberId() : null;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取移动终端类型
     *
     * @return 手机制式
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为GSM，移动和联通</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为CDMA，电信</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
     * </ul>
     */
    public static int getPhoneType() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }

    /**
     * 判断sim卡是否准备好
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSimCardReady() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * 获取Sim卡运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return sim卡运营商名称
     */
    public static String getSimOperatorName() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimOperatorName() : null;
    }

    /**
     * 获取Sim卡运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 移动网络运营商名称
     */
    public static String getSimOperatorByMnc() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm != null ? tm.getSimOperator() : null;
        if (operator == null) {
            return null;
        }
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
                return "中国移动";
            case "46001":
                return "中国联通";
            case "46003":
                return "中国电信";
            default:
                return operator;
        }
    }

    /**
     * 获取Sim卡序列号
     * <p>
     * Requires Permission:
     * {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     *
     * @return 序列号
     */
    public static String getSimSerialNumber() {
        try {
            TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String serialNumber = tm != null ? tm.getSimSerialNumber() : null;

            return serialNumber != null ? serialNumber : "";
        } catch (Exception e) {
        }

        return "";
    }

    /**
     * 获取Sim卡的国家代码
     *
     * @return 国家代码
     */
    public static String getSimCountryIso() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimCountryIso() : null;
    }

    /**
     * 读取电话号码
     * <p>
     * Requires Permission:
     * {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     * OR
     * {@link android.Manifest.permission#READ_SMS}
     * <p>
     *
     * @return 电话号码
     */
    public static String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm != null ? tm.getLine1Number() : null;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获得卡槽数，默认为1
     *
     * @return 返回卡槽数
     */
    public static int getSimCount() {
        int count = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            try {
                SubscriptionManager mSubscriptionManager = (SubscriptionManager) Utils.getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                if (mSubscriptionManager != null) {
                    count = mSubscriptionManager.getActiveSubscriptionInfoCountMax();
                    return count;
                }
            } catch (Exception ignored) {
            }
        }
        try {
            count = Integer.parseInt(getReflexMethod("getPhoneCount"));
        } catch (MethodNotFoundException ignored) {
        }
        return count;
    }

    /**
     * 获取Sim卡使用的数量
     *
     * @return 0, 1, 2
     */
    public static int getSimUsedCount() {
        int count = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            try {
                SubscriptionManager mSubscriptionManager = (SubscriptionManager) Utils.getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                count = mSubscriptionManager.getActiveSubscriptionInfoCount();
                return count;
            } catch (Exception ignored) {
            }
        }

        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            count = 1;
        }
        try {
            if (Integer.parseInt(getReflexMethodWithId("getSimState", "1")) == TelephonyManager.SIM_STATE_READY) {
                count = 2;
            }
        } catch (MethodNotFoundException ignored) {
        }
        return count;
    }

    /**
     * 获取多卡信息
     *
     * @return 多Sim卡的具体信息
     */
    public static List<SimInfo> getSimMultiInfo() {
        List<SimInfo> infos = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //1.版本超过5.1，调用系统方法
            SubscriptionManager mSubscriptionManager = (SubscriptionManager) Utils.getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            List<SubscriptionInfo> activeSubscriptionInfoList = null;
            if (mSubscriptionManager != null) {
                try {
                    activeSubscriptionInfoList = mSubscriptionManager.getActiveSubscriptionInfoList();
                } catch (Exception ignored) {
                }
            }
            if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() > 0) {
                //1.1.1 有使用的卡，就遍历所有卡
                for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                    SimInfo simInfo = new SimInfo();
                    simInfo.mCarrierName = subscriptionInfo.getCarrierName();
                    simInfo.mIccId = subscriptionInfo.getIccId();
                    simInfo.mSimSlotIndex = subscriptionInfo.getSimSlotIndex();
                    simInfo.mNumber = subscriptionInfo.getNumber();
                    simInfo.mCountryIso = subscriptionInfo.getCountryIso();
                    try {
                        simInfo.mImei = getReflexMethodWithId("getDeviceId", String.valueOf(simInfo.mSimSlotIndex));
                        simInfo.mImsi = getReflexMethodWithId("getSubscriberId", String.valueOf(simInfo.mSimSlotIndex));
                    } catch (MethodNotFoundException ignored) {
                    }
                    infos.add(simInfo);
                }
            }
        }

        //2.版本低于5.1的系统，首先调用数据库，看能不能访问到
        Uri uri = Uri.parse("content://telephony/siminfo"); //访问raw_contacts表
        ContentResolver resolver = Utils.getContext().getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "icc_id", "sim_id", "display_name", "carrier_name", "name_source", "color", "number", "display_number_format", "data_roaming", "mcc", "mnc"}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SimInfo simInfo = new SimInfo();
                simInfo.mCarrierName = cursor.getString(cursor.getColumnIndex("carrier_name"));
                simInfo.mIccId = cursor.getString(cursor.getColumnIndex("icc_id"));
                simInfo.mSimSlotIndex = cursor.getInt(cursor.getColumnIndex("sim_id"));
                simInfo.mNumber = cursor.getString(cursor.getColumnIndex("number"));
                simInfo.mCountryIso = cursor.getString(cursor.getColumnIndex("mcc"));
                try {
                    simInfo.mImei = getReflexMethodWithId("getDeviceId", String.valueOf(simInfo.mSimSlotIndex));
                    simInfo.mImsi = getReflexMethodWithId("getSubscriberId", String.valueOf(simInfo.mSimSlotIndex));
                } catch (MethodNotFoundException ignored) {
                }
                infos.add(simInfo);
            }
            cursor.close();
        }

        //3.通过反射读取卡槽信息，最后通过IMEI去重
        for (int i = 0; i < getSimCount(); i++) {
            infos.add(getReflexSimInfo(i));
        }
        List<SimInfo> simInfos = ConvertUtils.removeDuplicateWithOrder(infos);
        if (simInfos.size() < getSimCount()) {
            for (int i = simInfos.size(); i < getSimCount(); i++) {
                simInfos.add(new SimInfo());
            }
        }
        return simInfos;
    }

    /**
     * 通过反射获得SimInfo的信息
     * 当index为0时，读取默认信息
     *
     * @param index 位置,用来当subId和phoneId
     * @return {@link SimInfo} sim信息
     */
    @NonNull
    private static SimInfo getReflexSimInfo(int index) {
        SimInfo simInfo = new SimInfo();
        simInfo.mSimSlotIndex = index;
        try {
            simInfo.mImei = getReflexMethodWithId("getDeviceId", String.valueOf(simInfo.mSimSlotIndex));
            //slotId,比较准确
            simInfo.mImsi = getReflexMethodWithId("getSubscriberId", String.valueOf(simInfo.mSimSlotIndex));
            //subId,很不准确
            simInfo.mCarrierName = getReflexMethodWithId("getSimOperatorNameForPhone", String.valueOf(simInfo.mSimSlotIndex));
            //PhoneId，基本准确
            simInfo.mCountryIso = getReflexMethodWithId("getSimCountryIso", String.valueOf(simInfo.mSimSlotIndex));
            //subId，很不准确
            simInfo.mIccId = getReflexMethodWithId("getSimSerialNumber", String.valueOf(simInfo.mSimSlotIndex));
            //subId，很不准确
            simInfo.mNumber = getReflexMethodWithId("getLine1Number", String.valueOf(simInfo.mSimSlotIndex));
            //subId，很不准确
        } catch (MethodNotFoundException ignored) {
        }
        return simInfo;
    }

    /**
     * 获取手机状态信息
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @return DeviceId(IMEI) = 99000311726612<br>
     * DeviceSoftwareVersion = 00<br>
     * Line1Number =<br>
     * NetworkCountryIso = cn<br>
     * NetworkOperator = 46003<br>
     * NetworkOperatorName = 中国电信<br>
     * NetworkType = 6<br>
     * honeType = 2<br>
     * SimCountryIso = cn<br>
     * SimOperator = 46003<br>
     * SimOperatorName = 中国电信<br>
     * SimSerialNumber = 89860315045710604022<br>
     * SimState = 5<br>
     * SubscriberId(IMSI) = 460030419724900<br>
     * VoiceMailNumber = *86<br>
     */
    public static String getPhoneStatus() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String str = "";
        str += "DeviceId(IMEI) = " + getIMEI() + "\n";
        str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
        str += "Line1Number = " + getPhoneNumber() + "\n";
        str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
        str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
        str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
        str += "NetworkType = " + tm.getNetworkType() + "\n";
        str += "PhoneType = " + tm.getPhoneType() + "\n";
        str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
        str += "SimOperator = " + tm.getSimOperator() + "\n";
        str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
        str += "SimSerialNumber = " + getSimSerialNumber() + "\n";
        str += "SimState = " + tm.getSimState() + "\n";
        str += "SubscriberId(IMSI) = " + getIMSI() + "\n";
        str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
        return str;
    }

    /**
     * 跳至拨号界面
     *
     * @param phoneNumber 电话号码
     */
    public static void dial(String phoneNumber) {
        Utils.getContext().startActivity(IntentUtils.getDialIntent(phoneNumber));
    }

    /**
     * 拨打电话
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
     *
     * @param phoneNumber 电话号码
     */
    public static void call(String phoneNumber) {
        Utils.getContext().startActivity(IntentUtils.getCallIntent(phoneNumber));
    }

    /**
     * 跳至发送短信界面
     *
     * @param phoneNumber 接收号码
     * @param content 短信内容
     */
    public static void sendSms(String phoneNumber, String content) {
        Utils.getContext().startActivity(IntentUtils.getSendSmsIntent(phoneNumber, content));
    }

    /**
     * 发送短信
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.SEND_SMS"/>}</p>
     *
     * @param phoneNumber 接收号码
     * @param content 短信内容
     */
    public static void sendSmsSilent(String phoneNumber, String content) {
        if (StringUtils.isEmpty(content)) {
            return;
        }
        PendingIntent sentIntent = PendingIntent.getBroadcast(Utils.getContext(), 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    /**
     * 获取手机联系人
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
     *
     * @return 联系人链表
     */
    public static List<HashMap<String, String>> getAllContactInfo() {
        SystemClock.sleep(3000);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        // 1.获取内容解析者
        ContentResolver resolver = Utils.getContext().getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询raw_contacts,查询contact_id
        // projection : 查询的字段
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        try {
            // 5.解析cursor
            while (cursor.moveToNext()) {
                // 6.获取查询的数据
                String contact_id = cursor.getString(0);
                // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
                // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
                // 判断contact_id是否为空
                if (!StringUtils.isEmpty(contact_id)) {//null   ""
                    // 7.根据contact_id查询view_data表中的数据
                    // selection : 查询条件
                    // selectionArgs :查询条件的参数
                    // sortOrder : 排序
                    // 空指针: 1.null.方法 2.参数为null
                    Cursor c = resolver.query(date_uri, new String[]{"data1",
                                    "mimetype"}, "raw_contact_id=?",
                            new String[]{contact_id}, null);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // 8.解析c
                    while (c.moveToNext()) {
                        // 9.获取数据
                        String data1 = c.getString(0);
                        String mimetype = c.getString(1);
                        // 10.根据类型去判断获取的data1数据并保存
                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            // 电话
                            map.put("phone", data1);
                        } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            // 姓名
                            map.put("name", data1);
                        }
                    }
                    // 11.添加到集合中数据
                    list.add(map);
                    // 12.关闭cursor
                    c.close();
                }
            }
        } finally {
            // 12.关闭cursor
            cursor.close();
        }
        return list;
    }

    /**
     * 打开手机联系人界面点击联系人后便获取该号码
     * <p>参照以下注释代码</p>
     */
    public static void getContactNum() {
        Log.d("tips", "U should copy the following code.");
        /*
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 0);

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                Uri uri = data.getData();
                String num = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(uri,
                        null, null, null, null);
                while (cursor.moveToNext()) {
                    num = cursor.getString(cursor.getColumnIndex("data1"));
                }
                cursor.close();
                num = num.replaceAll("-", "");//替换的操作,555-6 -> 5556
            }
        }
        */
    }

    /**
     * 获取手机短信并保存到xml中
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_SMS"/>}</p>
     */
    public static void getAllSMS() {
        // 1.获取短信
        // 1.1获取内容解析者
        ContentResolver resolver = Utils.getContext().getContentResolver();
        // 1.2获取内容提供者地址   sms,sms表的地址:null  不写
        // 1.3获取查询路径
        Uri uri = Uri.parse("content://sms");
        // 1.4.查询操作
        // projection : 查询的字段
        // selection : 查询的条件
        // selectionArgs : 查询条件的参数
        // sortOrder : 排序
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
        // 设置最大进度
        int count = cursor.getCount();//获取短信的个数
        // 2.备份短信
        // 2.1获取xml序列器
        XmlSerializer xmlSerializer = Xml.newSerializer();
        try {
            // 2.2设置xml文件保存的路径
            // os : 保存的位置
            // encoding : 编码格式
            xmlSerializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backupsms.xml")), "utf-8");
            // 2.3设置头信息
            // standalone : 是否独立保存
            xmlSerializer.startDocument("utf-8", true);
            // 2.4设置根标签
            xmlSerializer.startTag(null, "smss");
            // 1.5.解析cursor
            while (cursor.moveToNext()) {
                SystemClock.sleep(1000);
                // 2.5设置短信的标签
                xmlSerializer.startTag(null, "sms");
                // 2.6设置文本内容的标签
                xmlSerializer.startTag(null, "address");
                String address = cursor.getString(0);
                // 2.7设置文本内容
                xmlSerializer.text(address);
                xmlSerializer.endTag(null, "address");
                xmlSerializer.startTag(null, "date");
                String date = cursor.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag(null, "date");
                xmlSerializer.startTag(null, "type");
                String type = cursor.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag(null, "type");
                xmlSerializer.startTag(null, "body");
                String body = cursor.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null, "body");
                xmlSerializer.endTag(null, "sms");
                System.out.println("address:" + address + "   date:" + date + "  type:" + type + "  body:" + body);
            }
            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();
            // 2.8将数据刷新到文件中
            xmlSerializer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是MIUI系统
     *
     * @return true -- 是 false -- 否
     */
    public static boolean isMIUI() {

        return MIUIUtils.isMIUI();
    }

    /**
     * 是否为Flyme系统
     *
     * @return true -- 是 false -- 否
     */
    public static boolean isFlyme() {

        return FlymeUtils.isFlyme();
    }

    /**
     * 通过反射调取@hide的方法
     *
     * @param predictedMethodName 方法名
     * @return 返回方法调用的结果
     * @throws MethodNotFoundException 方法没有找到
     */
    private static String getReflexMethod(String predictedMethodName) throws MethodNotFoundException {
        String result = null;
        TelephonyManager telephony = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Method getSimID = telephonyClass.getMethod(predictedMethodName);
            Object ob_phone = getSimID.invoke(telephony);
            if (ob_phone != null) {
                result = ob_phone.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MethodNotFoundException(predictedMethodName);
        }
        return result;
    }

    /**
     * 通过反射调取@hide的方法
     *
     * @param predictedMethodName 方法名
     * @param id 参数
     * @return 返回方法调用的结果
     * @throws MethodNotFoundException 方法没有找到
     */
    private static String getReflexMethodWithId(String predictedMethodName, String id) throws MethodNotFoundException {
        String result = null;
        TelephonyManager telephony = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
            Class<?>[] parameterTypes = getSimID.getParameterTypes();
            Object[] obParameter = new Object[parameterTypes.length];
            if (parameterTypes[0].getSimpleName().equals("int")) {
                obParameter[0] = Integer.valueOf(id);
            } else {
                obParameter[0] = id;
            }
            Object ob_phone = getSimID.invoke(telephony, obParameter);
            if (ob_phone != null) {
                result = ob_phone.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MethodNotFoundException(predictedMethodName);
        }
        return result;
    }

    /**
     * SIM 卡信息
     */
    public static class SimInfo {
        /** 运营商信息：中国移动 中国联通 中国电信 */
        public CharSequence mCarrierName;
        /** 卡槽ID，SimSerialNumber */
        public CharSequence mIccId;
        /** 卡槽id， -1 - 没插入、 0 - 卡槽1 、1 - 卡槽2 */
        public int mSimSlotIndex;
        /** 号码 */
        public CharSequence mNumber;
        /** 城市 */
        public CharSequence mCountryIso;
        /** 设备唯一识别码 */
        public CharSequence mImei = getIMEI();
        /** SIM的编号 */
        public CharSequence mImsi;

        /**
         * 通过 IMEI 判断是否相等
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof SimInfo && (TextUtils.isEmpty(((SimInfo) obj).mImei) || ((SimInfo) obj).mImei.equals(mImei));
        }
    }

    /**
     * 反射未找到方法
     */
    private static class MethodNotFoundException extends Exception {

        public static final long serialVersionUID = -3241033488141442594L;

        MethodNotFoundException(String info) {
            super(info);
        }
    }

    /**
     * MIUI工具
     */
    public static class MIUIUtils {
        private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

        /**
         * 是否是MIUI系统
         *
         * @return true -- 是  false -- 否
         */
        public static boolean isMIUI() {
            //1.通过硬件设备的制造商判断
            if (Build.MANUFACTURER.equals("Xiaomi")) {
                return true;
            }
            try {
                final BuildProperties prop = BuildProperties.newInstance();
                return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                        || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                        || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
            } catch (final IOException e) {
                return false;
            }
        }
    }

    /**
     * Flyme工具
     */
    public static class FlymeUtils {
        /**
         * 是否是Flyme系统
         *
         * @return true -- 是  false -- 否
         */
        public static boolean isFlyme() {
            //1.通过硬件设备的制造商判断
            if (Build.MANUFACTURER.equals("Meizu")) {
                return true;
            }
            try {
                //2.Invoke Build.hasSmartBar()，魅族5.1以下有效
                final Method method = Build.class.getMethod("hasSmartBar");
                return method != null;
            } catch (Exception ignored) {
            }
            //3.通过系统表示来判断
            return isMeizuFlymeOS();
        }

        /**
         * 判断魅族系统操作版本标识
         */
        private static boolean isMeizuFlymeOS() {
            return getSystemProperty().toLowerCase().contains("flyme");
        }

        /**
         * 获取系统操作版本标识
         */
        private static String getSystemProperty() {
            try {
                Class<?> clz = Class.forName("android.os.SystemProperties");
                Method get = clz.getMethod("get", String.class, String.class);
                return (String) get.invoke(clz, "ro.build.display.id", "");
            } catch (Exception ignored) {
            }
            return "";
        }
    }

    /**
     * 属性工具
     */
    public static class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }
    }
}