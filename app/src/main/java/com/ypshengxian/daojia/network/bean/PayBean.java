package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

/**
 * Created by Joe on 2018/4/4.
 */

public class PayBean extends BaseModuleApiResult {

    /**
     * platform_data : {"data":"alipay_sdk=lokielse%2Fomnipay-alipay&app_id=2018033002473181&biz_content=%7B%22subject%22%3A%22%5Cu5b9c%5Cu54c1%5Cu751f%5Cu9c9c%5Cu8ba2%5Cu5355%22%2C%22body%22%3A%22%22%2C%22out_trade_no%22%3A%222018040416542329906%22%2C%22total_fee%22%3A0.26%2C%22passback_params%22%3A%22%257B%2522user_id%2522%253A%252273%2522%257D%22%7D&charset=UTF-8&format=JSON&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fadmin.ypshengxian.com%2Fcallback%2Falipay%2Fnotify&sign_type=RSA&timestamp=2018-04-04+16%3A54%3A23&version=1.0&sign=vie1GPknfyo2D3RZ7VTtQ2x5H80TaT5p2kfuMzths%2Bsb7A4PCNcGL8rIlgPNXLMTe16dqWE%2B%2BYRtsOjGMj%2FkAp1KVzoL3jdYYJ6g8XQ5nZNlPQAMc%2F7GLZDWJYWuhJnhavs2s5idw4bnsOimA%2BnztYSFVSLr1%2BPJAFnj7fPoBP0sIb1XNHZapL5Sjtij3IZsxmssiLDE7NcaDJD670aKN4ihLn6eRMF9CV3bdMJYZdXb8GGTLeqiG6qAA8UTnIXbIQBv628T%2BIVLAzIkW28NWFKpV6V749mAgBKe5EQnMn8LUSVpCI3QrfzoQ3qIREbb17iOvf%2FNldVwKU1yYsvvZw%3D%3D"}
     * platform : alipay
     */

    public PlatformDataBean platform_data;
    public String platform;

    public static class PlatformDataBean {
        /**
         * data : alipay_sdk=lokielse%2Fomnipay-alipay&app_id=2018033002473181&biz_content=%7B%22subject%22%3A%22%5Cu5b9c%5Cu54c1%5Cu751f%5Cu9c9c%5Cu8ba2%5Cu5355%22%2C%22body%22%3A%22%22%2C%22out_trade_no%22%3A%222018040416542329906%22%2C%22total_fee%22%3A0.26%2C%22passback_params%22%3A%22%257B%2522user_id%2522%253A%252273%2522%257D%22%7D&charset=UTF-8&format=JSON&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fadmin.ypshengxian.com%2Fcallback%2Falipay%2Fnotify&sign_type=RSA&timestamp=2018-04-04+16%3A54%3A23&version=1.0&sign=vie1GPknfyo2D3RZ7VTtQ2x5H80TaT5p2kfuMzths%2Bsb7A4PCNcGL8rIlgPNXLMTe16dqWE%2B%2BYRtsOjGMj%2FkAp1KVzoL3jdYYJ6g8XQ5nZNlPQAMc%2F7GLZDWJYWuhJnhavs2s5idw4bnsOimA%2BnztYSFVSLr1%2BPJAFnj7fPoBP0sIb1XNHZapL5Sjtij3IZsxmssiLDE7NcaDJD670aKN4ihLn6eRMF9CV3bdMJYZdXb8GGTLeqiG6qAA8UTnIXbIQBv628T%2BIVLAzIkW28NWFKpV6V749mAgBKe5EQnMn8LUSVpCI3QrfzoQ3qIREbb17iOvf%2FNldVwKU1yYsvvZw%3D%3D
         */

        public String data;
        /** 微信返回 */
        public String appid;
        public String partnerid;
        public String prepayid;
        public String noncestr;
        public String timestamp;
        public String sign;

    }
}
