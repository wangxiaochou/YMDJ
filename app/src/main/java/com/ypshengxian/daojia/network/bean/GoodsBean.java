package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * @author ZSH
 * @create 2018/3/25
 * @Describe 商品信息
 */

public class GoodsBean extends BaseModuleApiResult {


	/**
	 * id : 464
	 * title : 红西柚 约900g
	 * subtitle : 精品水果  新鲜直达
	 * attribute :
	 * sn : 2110348
	 * maxPurchase : 0
	 * formatNum : 900
	 * stock : 5
	 * price : 19.44
	 * prePrice : 10.80
	 * originPrice : 18.80
	 * salesSum : 0
	 * categoryId : 13836
	 * about :
	 * picture : ["http://admin.ypshengxian.com/files/goods/2018/04-07/173229db2c7a579691.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173229dd72ac923722.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173230e03810752021.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173230e241de235456.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173230e5065d328092.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/173230e72088384109.jpg?version=8.2.20"]
	 * sortSeq : 0
	 * activityType : null
	 * activityId : null
	 * unit : g
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
	public Object activityType;
	public Object activityId;
	public String unit;
	public List<String> picture;
}
