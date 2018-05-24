package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.alipayapi.PayResult;
import com.ypshengxian.daojia.annotation.ActivityOption;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.mvp.contract.IPayWayContract;
import com.ypshengxian.daojia.mvp.presenter.PayWayPresenter;
import com.ypshengxian.daojia.network.bean.PayBean;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 支付页面(支付方式)
 */
@ActivityOption(reqLogin = true)
public class PayWayActivity extends BaseMVPYpFreshActicity<IPayWayContract.View, PayWayPresenter> implements IPayWayContract.View, View.OnClickListener {


    private ImageView mIv;
    /**
     * ¥ 15.00
     */
    private TextView mTvTotalAmount;
    /**
     * 订单编号：
     */
    private TextView mTvOrder;
    /**
     * 商品名称：
     */
    private TextView mTvGoodsName;
    private ImageView mIvBalance;
    private RelativeLayout mRlBalance;
    private ImageView mIvWx;
    private RelativeLayout mRlWx;
    private ImageView mIvAlipay;
    private RelativeLayout mRlAlipay;

    private String gateway;
    private String orderSn;
    private String type;
    private String coinAmount = "";
    private String payPassword = "";

    private String orderName = "";
    private String orderAmount = "";
    /** 微信API */
    private IWXAPI mWxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderSn = getIntent().getExtras().getString(Count.ORDER_SN);
        orderName = getIntent().getExtras().getString(Count.ORDER_NAME);
        orderAmount = getIntent().getExtras().getString(Count.ORDER_PRICE);
        mWxapi = WXAPIFactory.createWXAPI(this, Count.WX_APP_ID);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_pay_way;
    }

    @Override
    protected PayWayPresenter createPresenter() {
        return new PayWayPresenter();
    }

    @Override
    public void onResponseData(boolean isSuccess, PayBean data) {
        if (isSuccess) {
            switch (gateway) {
                case Count.PAYWAY_ALIPAY:
                    aliPay(data);
                    break;
                case Count.PAYWAY_WX:
                    payForWx(data);
                    break;
                default:
                    break;
            }
        }

    }


    /**
     * 微信支付
     *
     * @param data 数据
     */
    private void payForWx(PayBean data) {
        Observable.create(new Observable.OnSubscribe<PayReq>() {
            @Override
            public void call(Subscriber<? super PayReq> subscriber) {
                PayReq request = new PayReq();
                //调起微信APP的对象
                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
                request.appId = data.platform_data.appid;
                request.partnerId = data.platform_data.partnerid;
                request.prepayId = data.platform_data.prepayid;
                request.packageValue = "Sign=WXPay";
                request.nonceStr = data.platform_data.noncestr;
                request.timeStamp = data.platform_data.timestamp;
                request.sign = data.platform_data.sign;
                mWxapi.sendReq(request);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .delay(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PayReq>() {
                    @Override
                    public void onCompleted() {
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(PayReq payReq) {

                    }
                });

    }

    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.pay_way_title_text));

        mIv = (ImageView) findViewById(R.id.iv_activity_pay_way);
        mTvTotalAmount = (TextView) findViewById(R.id.tv_activity_pay_way_total_amount);
        mTvOrder = (TextView) findViewById(R.id.tv_activity_pay_way_order);
        mTvGoodsName = (TextView) findViewById(R.id.tv_activity_pay_way_goods_name);
        mIvBalance = (ImageView) findViewById(R.id.iv_activity_pay_way_balance);
        mRlBalance = (RelativeLayout) findViewById(R.id.rl_activity_pay_way_balance);
        mIvWx = (ImageView) findViewById(R.id.iv_activity_pay_way_wx);
        mRlWx = (RelativeLayout) findViewById(R.id.rl_activity_pay_way_wx);
        mIvAlipay = (ImageView) findViewById(R.id.iv_activity_pay_way_alipay);
        mRlAlipay = (RelativeLayout) findViewById(R.id.rl_activity_pay_way_alipay);


        mTvOrder.setText(orderSn);
        mTvGoodsName.setText(orderName);
        mTvTotalAmount.setText("￥" + orderAmount);

        mRlBalance.setOnClickListener(this);
        mRlWx.setOnClickListener(this);
        mRlAlipay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.rl_activity_pay_way_balance:
                //余额
                ToastUtils.showShortToast("暂未开通！");
                break;
            case R.id.rl_activity_pay_way_wx:
                gateway = Count.PAYWAY_WX;
                type = Count.PAYWAY_PURCHASE;
                mPresenter.requestData(gateway, orderSn, type, coinAmount, payPassword);
                //微信
                break;
            case R.id.rl_activity_pay_way_alipay:
                //支付宝
                gateway = Count.PAYWAY_ALIPAY;
                type = Count.PAYWAY_PURCHASE;
                mPresenter.requestData(gateway, orderSn, type, coinAmount, payPassword);
                break;
        }
    }

    /**
     * 支付宝支付
     *
     * @param bean 支付宝支付信息
     */
    private void aliPay(PayBean bean) {
        if (null != bean) {
            Observable.create(new Observable.OnSubscribe<Map<String, String>>() {
                @Override
                public void call(Subscriber<? super Map<String, String>> subscriber) {
                    PayTask alipay = new PayTask(PayWayActivity.this);
                    Map<String, String> result = alipay.payV2(bean.platform_data.data, true);
                    subscriber.onNext(result);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Map<String, String>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Map<String, String> map) {
                            PayResult result = new PayResult(map);
                           String status=result.getResultStatus();
                           switch (status){
                               case "9000":
                                   ToastUtils.showShortToast("支付成功");
                                   EventBus.getDefault().post(new OrderEvent());
                                   startActivityEx(PaySuccessStateActivity.class);
                                   finish();
                                   break;
                               default:
                                   ToastUtils.showShortToast("支付失败");
                                   break;
                           }
                        }
                    });
        }
    }
}
