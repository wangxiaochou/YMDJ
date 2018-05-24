package com.ypshengxian.daojia.network.service;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.AllGoodsBean;
import com.ypshengxian.daojia.network.bean.ArticleBean;
import com.ypshengxian.daojia.network.bean.CateGoryBean;
import com.ypshengxian.daojia.network.bean.CityBean;
import com.ypshengxian.daojia.network.bean.CodeBean;
import com.ypshengxian.daojia.network.bean.CreateOrderBean;
import com.ypshengxian.daojia.network.bean.EventBean;
import com.ypshengxian.daojia.network.bean.FlashBean;
import com.ypshengxian.daojia.network.bean.GoodsBean;
import com.ypshengxian.daojia.network.bean.HomeBean;
import com.ypshengxian.daojia.network.bean.LineItemBean;
import com.ypshengxian.daojia.network.bean.LoginBean;
import com.ypshengxian.daojia.network.bean.MyOrderBean;
import com.ypshengxian.daojia.network.bean.OrderBean;
import com.ypshengxian.daojia.network.bean.PayBean;
import com.ypshengxian.daojia.network.bean.PayOrderBean;
import com.ypshengxian.daojia.network.bean.RegisterBean;
import com.ypshengxian.daojia.network.bean.ShopBean;
import com.ypshengxian.daojia.network.bean.SmsBean;
import com.ypshengxian.daojia.network.bean.TopBean;
import com.ypshengxian.daojia.network.bean.UploadBean;
import com.ypshengxian.daojia.network.bean.UserBean;
import com.ypshengxian.daojia.network.bean.UserCashFlowBean;
import com.ypshengxian.daojia.network.bean.UserCoinBean;
import com.ypshengxian.daojia.network.bean.UserPointFlowBean;
import com.ypshengxian.daojia.network.bean.UserProfileBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 宜品生鲜api调用
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public interface YpFreshService {

    /**
     * 首页
     *
     * @return 实体
     */
    @GET("/homepage")
    Observable<HomeBean> getHomePage();

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 实体
     */
    @POST("/login")
    @FormUrlEncoded
    Observable<LoginBean> login(@Field("_username") String username,
                                @Field("_password") String password);

    /**
     * 商品详情
     *
     * @param goodsId id
     * @return 实体
     */
    @GET("/goods/{id}")
    Observable<GoodsBean> getGoodsInfo(@Path("id") String goodsId);

    /**
     * 获取商品
     *
     * @param map 请求数据
     * @return 实体
     */
    @GET("/goods")
    Observable<AllGoodsBean> getGoods(@QueryMap Map<String, String> map);

    /**
     * 获取商品分类
     *
     * @return 实体
     */
    @GET("/category")
    Observable<List<CateGoryBean>> getCategory();

    /**
     * 获取个人信息
     *
     * @return 实体
     */
    @GET("/me")
    Observable<UserBean> getMe();

    /**
     * 获取验证码
     *
     * @param phoneNumber 数据
     * @return 实体
     */
    @GET("/sms_send")
    Observable<SmsBean> getSmsSend(@QueryMap Map<String, String> phoneNumber);

    /**
     * 注册
     *
     * @param password      密码
     * @param nickname      昵称
     * @param phone         电话
     * @param smsCode       验证码
     * @param registeredWay 终端
     * @param smsToken 验证码token
     * @return 实体
     */
    @POST("/register")
    @FormUrlEncoded
    Observable<RegisterBean> getRegister(@Field("password") String password,
                                         @Field("nickname") String nickname,
                                         @Field("phone") String phone,
                                         @Field("smsCode") String smsCode,
                                         @Field("registeredWay") String registeredWay,
                                         @Field("smsToken") String smsToken);

    /**
     * 短信重置密码
     *
     * @param password      密码
     * @param phone         电话
     * @param smsCode       验证码
     * @param smsToken 验证码token
     * @return 实体
     */
    @POST("/resetBySms")
    @FormUrlEncoded
    Observable<BaseModuleApiResult> resetBySms(@Field("password") String password,
                                               @Field("phone") String phone,
                                               @Field("smsCode") String smsCode,
                                               @Field("smsToken") String smsToken);

    /**
     * 注销
     *
     * @return 实体
     */
    @GET("/logout")
    Observable<BaseModuleApiResult> getLogout();

    /**
     * 第三方登录
     *
     * @param channelType 渠道
     * @param oauthCode   id
     * @return 实体
     */
    @POST("/loginBind")
    @FormUrlEncoded
    Observable<LoginBean> getLoginBind(@Field("code") String oauthCode,
                                       @Field("type") String channelType);

    /**
     * 活动详情
     *
     * @return 实体
     */
    @GET("/topic")
    Observable<List<EventBean>> getTopPic();

    /**
     * 获得专题详情
     *
     * @param id id
     * @return 实体
     */
    @GET("/topic/{id}")
    Observable<TopBean> getTop(@Path("id") String id);


    /**
     * 获得新人详情
     *
     * @param map 数据
     * @return 实体
     */
    @GET("/getNewSales")
    Observable<TopBean> getNewSales(@QueryMap Map<String, String> map);

    /**
     * 添加购物车
     *
     * @param map 数据
     * @return 实体
     */
    @GET("/cart/add")
    Observable<AddCartBean> addCart(@QueryMap Map<String, String> map);

    /**
     * 删除购物车
     *
     * @param map 数据
     * @return 实体
     */
    @GET("/cart/delete")
    Observable<AddCartBean> deletedCart(@QueryMap Map<String, String> map);

    /**
     * 获得购物车数据
     *
     * @return 实体
     */
    @GET("/cart")
    Observable<AddCartBean> getCart();

    /**
     * 获得用户账户信息
     *
     * @return 实体
     */
    @GET("getUserCoin")
    Observable<UserCoinBean> getUserCoin();

    /**
     * 上传头像
     *
     * @param fileByte 文件流
     * @return 实体
     */
    @POST("uploadImage")
    @FormUrlEncoded
    Observable<UploadBean> uploadAvatar(@Field("file") String fileByte);

    /**
     * 显示特惠
     *
     * @return 实体
     */
    @GET("/flashSales")
    Observable<List<FlashBean>> getFlashSales();

    /**
     * 上传用户信息
     *
     * @param id       图片ID
     * @param gender   男女
     * @param name     昵称
     * @param birthday 生日
     * @return 实体
     */
    @POST("/updateUserProfile")
    @FormUrlEncoded
    Observable<UserProfileBean> updateUserProfile(@Field("profile[nickname]") String name,
                                                  @Field("profile[gender]") String gender,
                                                  @Field("profile[birthday]") String birthday,
                                                  @Field("fileId") String id);

    /**
     * 绑定电话号码
     *
     * @param smsCode 验证码
     * @param phone   电话
     * @param smsToken   验证码TOKEN
     * @return 实体
     */
    @POST("/bindPhone")
    @FormUrlEncoded
    Observable<BaseModuleApiResult> bindPhone(@Field("smsCode") String smsCode,
                                              @Field("phone") String phone,
                                              @Field("smsToken") String smsToken);

    /**
     * 获得门店列表
     *
     * @return 实体
     */
    @GET("/shop")
    Observable<ArrayList<ShopBean>> getShop();

    /**
     * 获得城市列表
     *
     * @return 实体
     */
    @GET("/citys")
    Observable<ArrayList<CityBean>> getCitys();

    /**
     * 短信登录
     *
     * @param phone   手机号码
     * @param smsCode 验证码
     * @param  smsToken 验证码TOKEN
     * @return 实体
     */
    @POST("/loginBySms")
    @FormUrlEncoded
    Observable<LoginBean> loginBySms(@Field("phone") String phone,
                                     @Field("smsCode") String smsCode,
                                     @Field("smsToken") String smsToken);

    /**
     * 获得订单数量
     *
     * @return 实体
     */
    @GET("/order_count")
    Observable<OrderBean> getOrderCount();

    /**
     * 创建订单
     *
     * @param map 数据
     * @return 实体
     */
    @GET("/getPayOrder")
    Observable<PayOrderBean> getPayOrder(@QueryMap Map<String, String> map);

    /**
     * 提交订单
     *
     * @param shopId       商铺Id
     * @param couponId     优惠卷ID
     * @param packetId     购物袋ID
     * @param deliveryTime 取货时间
     * @return 实体
     */
    @POST("/createOrder")
    @FormUrlEncoded
    Observable<CreateOrderBean> createOrder(@Field("shopId") String shopId,
                                            @Field("couponId") String couponId,
                                            @Field("packetId") String packetId,
                                            @Field("deliveryTime") String deliveryTime);

    /**
     * 获得我的订单
     *
     * @param map map
     * @return 实体
     */
    @GET("/getMyOrder")
    Observable<MyOrderBean> getMyOrder(@QueryMap Map<String, String> map);


    /**
     * 支付订单
     *
     * @param gateway     支付平台
     * @param orderSn     付款订单SN
     * @param type        付款类型(purchase/recharge)
     * @param coinAmount  余额支付数量
     * @param payPassword 支付密码
     * @return 实体
     */
    @POST("/payOrder")
    @FormUrlEncoded
    Observable<PayBean> payOrder(@Field("gateway") String gateway,
                                 @Field("orderSn") String orderSn,
                                 @Field("type") String type,
                                 @Field("coinAmount") String coinAmount,
                                 @Field("payPassword") String payPassword);

    /**
     * 获得提货码
     *
     * @return 实体
     */
    @GET("getDeliveryCode")
    Observable<List<CodeBean>> getDeliveryCode();

    /**
     * 获得订单详情
     *
     * @param map 数据
     * @return 实体
     */
    @GET("/getOrder")
    Observable<LineItemBean> getOrder(@QueryMap Map<String, String> map);

    /**
     * 退款或申请售后
     *
     * @param orderSn      订单
     * @param refundAmount 金额
     * @param reason       原因
     * @param id           id
     * @return 实体
     */
    @POST("/refundOrder")
    @FormUrlEncoded
    Observable<BaseModuleApiResult> refundOrder(@Field("orderSn") String orderSn,
                                                @Field("refundAmount") String refundAmount,
                                                @Field("reason") String reason,
                                                @Field("reasonImg[]") String[] id);

    /**
     * 取消未付款的订单
     *
     * @param map 数据
     * @return 实体
     */
    @GET("/closeOrder")
    Observable<BaseModuleApiResult> closeOrder(@QueryMap Map<String, String> map);

    /**
     * 修改购物车选中状态
     *
     * @param map 数据
     * @return 实体
     */
    @GET("/cart/update")
    Observable<AddCartBean> updateCart(@QueryMap Map<String, String> map);

    /**
     * 是否全选
     *
     * @param map 书记
     * @return 实体
     */
    @GET("/cart/batchUpdate")
    Observable<AddCartBean> batchUpdateCart(@QueryMap Map<String, String> map);

    /**
     * 获取用户现金流水
     *
     * @param map map
     * @return 实体
     */
    @GET("/getUserCashFlow")
    Observable<UserCashFlowBean> getUserCashFlow(@QueryMap Map<String, String> map);

    /**
     * 获取用户积分流水
     *
     * @param map map
     * @return 实体
     */
    @GET("/getUserPointFlow")
    Observable<UserPointFlowBean> getUserPointFlow(@QueryMap Map<String, String> map);

    /**
     * 获取文章详情
     *
     * @param map map
     * @return 实体
     */
    @GET("/getArticle")
    Observable<ArticleBean> getArticle(@QueryMap Map<String, String> map);


    /**
     * 设置用户的支付密码
     *
     * @param phone       手机号
     * @param smsCode     验证码
     * @param payPassword 支付密码
     * @param smsToken 验证码token
     * @return 实体
     */
    @POST("/setUserPayPassword")
    @FormUrlEncoded
    Observable<BaseModuleApiResult> setUserPayPassword(@Field("phone") String phone,
                                                       @Field("smsCode") String smsCode,
                                                       @Field("payPassword") String payPassword,
                                                       @Field("smsToken") String smsToken);
}

