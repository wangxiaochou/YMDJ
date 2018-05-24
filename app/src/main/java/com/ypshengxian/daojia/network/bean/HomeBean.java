package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 *
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class HomeBean  extends BaseModuleApiResult {


    public List<BannerBean> banner;
    public List<NavBean> nav;
    public List<FloorBean> floor;
    public List<FlashSalesBean> flash_sales;
    public List<TopicBean> topic;
    public NewSalesBean new_sales;

    public static class BannerBean {
        /**
         * link : 1
         * img : https://www.ypshengxian.com/public/upload/ad/2018/03-19/7c2dd095116448ec8562b64d7e38eea0.jpg
         * type : 1
         */

        public String link;
        public String img;
        public int type;
    }

    public static class NavBean {
        /**
         * title : 新人才有
         * link : 5
         * img : https://www.ypshengxian.com/public/upload/nav/2018/03-08/0c340436c91bcfe4c6dce1cfa9750925.png
         * type : 1
         */

        public String title;
        public String link;
        public String img;
        public int type;
    }

    public static class FloorBean {
        /**
         * title : 新人才有
         * link : 5
         * img : https://www.ypshengxian.com/public/upload/nav/2018/03-08/0c340436c91bcfe4c6dce1cfa9750925.png
         * type : 1
         * items : [{"id":67,"title":"砀山梨 约1kg（限购1份）","img":"https://www.ypshengxian.com/public/upload/goods/2018/02-24/574691d8ea8e686bb13c8b877928bd85.JPG","tag":"新人专享","subtitle":"精品水果  新鲜直达","price":"0.50元","unit":"斤","origin_price":"3.98元"}]
         */

        public String title;
        public int link;
        public String img;
        public int type;
        public List<ItemsBean> items;

        public static class ItemsBean {
            /**
             * id : 64
             * title : 砀山梨 约1kg（限购1份）
             * subtitle : 精品水果  新鲜直达
             * attribute : 新人专享
             * sn : 21102200
             * maxPurchase : 0
             * stock : 7956
             * price : 1.00
             * prePrice : 0.50
             * salesSum : 19267
             * categoryId : 13834
             * about :
             * picture : ["http://admin.ypshengxian.com/files/goods/2018/04-07/172623f8840e290535.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/172623fa7ceb813932.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/172623fce808354766.jpg?version=8.2.20"]
             * sortSeq : 0
             * unit : g
             */

            public int id;
            public String title;
            public String subtitle;
            public String attribute;
            public String sn;
            public String maxPurchase;
            public String stock;
            /**市场价  */
            public String originPrice;
            /** 商品单价 */
            public String price;
            /** 规格价 */
            public String prePrice;
            public String salesSum;
            public String categoryId;
            public String about;
            public String sortSeq;
            public String thumb;
            public String unit;

            public List<String> picture;
        }
    }

    public static class FlashSalesBean {
        /**
         * expiry : 1521999133
         * price : 0.50元
         * unit : 斤
         * origin_price : 3.98元
         * img : https://www.ypshengxian.com/public/upload/goods/2018/02-24/574691d8ea8e686bb13c8b877928bd85.JPG
         */

        public int expiry;
        public String price;
        public String unit;
        public String origin_price;
        public String img;
        public boolean isSetTime= false;
    }

    public static class TopicBean {
        /**
         * name : 五谷杂粮活动专区
         * id : 10
         * img : https://www.ypshengxian.com/public/upload/goods/2018/02-24/574691d8ea8e686bb13c8b877928bd85.JPG
         */

        public String name;
        public int id;
        public String img;
    }

    public static class NewSalesBean {
        /**
         * name : 新人专享
         * goods : [{"id":"566","title":"左米右麦酸奶条200g","subtitle":"严格生产  保障品质","attribute":"","sn":"6970421140552","maxPurchase":"0","formatNum":"1","stock":"2","price":"3.00","prePrice":"6.90","originPrice":"7.90","salesSum":"0","categoryId":"13839","about":"","picture":["http://admin.ypshengxian.com/files/goods/2018/04-07/17322264a581641439.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173222671329617409.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/17322269106a955348.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732226b0b5a925193.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732226d07d2448453.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732226efeff473063.jpg?version=8.2.20"],"sortSeq":"78","activityType":"newUser","activityId":"0","unit":"杯"},{"id":"567","title":"左米右麦蜜豆吐司110g","subtitle":"严格生产  保障品质","attribute":"","sn":"6970421140057","maxPurchase":"0","formatNum":"1","stock":"5","price":"1.00","prePrice":"3.50","originPrice":"4.80","salesSum":"0","categoryId":"13839","about":"","picture":["http://admin.ypshengxian.com/files/goods/2018/04-07/1732193df860673893.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/17322041d7d1020902.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732204458b4332223.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173220466cd0359642.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/17322048df50638514.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732204c0c34379359.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732204e8642644655.jpg?version=8.2.20"],"sortSeq":"45","activityType":"newUser","activityId":"0","unit":"袋"},{"id":"568","title":"依品园全麦吐司面包300g","subtitle":"严格生产  保障品质","attribute":"","sn":"6957238900043","maxPurchase":"0","formatNum":"1","stock":"3","price":"5.00","prePrice":"6.50","originPrice":"8.80","salesSum":"0","categoryId":"13839","about":"","picture":["http://admin.ypshengxian.com/files/goods/2018/04-07/173223729971338373.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173223748886082332.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/17322376d963122673.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/17322379517b497091.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732237be38f379145.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732237e529e834917.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/17322481a152252783.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/17322483962e511706.jpg?version=8.2.20"],"sortSeq":"34","activityType":"newUser","activityId":"0","unit":"袋"}]
         */

        public String name;
        public List<NewSalesBean.GoodsBean> goods;

        public static class GoodsBean {
            /**
             * id : 566
             * title : 左米右麦酸奶条200g
             * subtitle : 严格生产  保障品质
             * attribute :
             * sn : 6970421140552
             * maxPurchase : 0
             * formatNum : 1
             * stock : 2
             * price : 3.00
             * prePrice : 6.90
             * originPrice : 7.90
             * salesSum : 0
             * categoryId : 13839
             * about :
             * picture : ["http://admin.ypshengxian.com/files/goods/2018/04-07/17322264a581641439.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173222671329617409.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/17322269106a955348.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732226b0b5a925193.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732226d07d2448453.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/1732226efeff473063.jpg?version=8.2.20"]
             * sortSeq : 78
             * activityType : newUser
             * activityId : 0
             * unit : 杯
             */

            public String id;
            public String title;
            public String subtitle;
            public String attribute;
            public String sn;
            public String maxPurchase;
            public String formatNum;
            public String stock;
            public String price;
            public String prePrice;
            public String originPrice;
            public String salesSum;
            public String categoryId;
            public String about;
            public String sortSeq;
            public String activityType;
            public String activityId;
            public String unit;
            public String thumb;
            public List<String> picture;
        }
    }
}
