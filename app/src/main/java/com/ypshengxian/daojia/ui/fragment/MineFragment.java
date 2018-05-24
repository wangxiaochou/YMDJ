package com.ypshengxian.daojia.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IMineContract;
import com.ypshengxian.daojia.mvp.presenter.MinePresenter;
import com.ypshengxian.daojia.network.bean.OrderBean;
import com.ypshengxian.daojia.network.bean.UploadBean;
import com.ypshengxian.daojia.network.bean.UserBean;
import com.ypshengxian.daojia.network.bean.UserCoinBean;
import com.ypshengxian.daojia.ui.activity.IntegralDetailActivity;
import com.ypshengxian.daojia.ui.activity.ManageAddressActivity;
import com.ypshengxian.daojia.ui.activity.MyCouponActivity;
import com.ypshengxian.daojia.ui.activity.MyWalletActivity;
import com.ypshengxian.daojia.ui.activity.OrderActivity;
import com.ypshengxian.daojia.ui.activity.SettingActivity;
import com.ypshengxian.daojia.ui.activity.UserDataActivity;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.view.BadgeRadioButton;
import com.ypshengxian.daojia.view.CircularImageView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class MineFragment extends BaseMVPYpFreshFragment<IMineContract.View, MinePresenter> implements IMineContract.View, View.OnClickListener {
    private CircularImageView mIvUserImg;
    private TextView mTvUserName;
    private TextView mTvUserPhone;
    private BadgeRadioButton mRbNotify;
    private TextView mTvIntegral;
    private TextView mTvMoney;
    private LinearLayout mLlOrder;
    private LinearLayout mLlWallet;
    private LinearLayout mLlIntegral;
    /**
     * 待付款
     */
    private BadgeRadioButton mRb0;
    /**
     * 待自提
     */
    private BadgeRadioButton mRb1;
    /**
     * 待收货
     */
    private BadgeRadioButton mRb2;
    /**
     * 已完成
     */
    private BadgeRadioButton mRb3;
    /**
     * 售后
     */
    private BadgeRadioButton mRb4;
    private RadioGroup mRg;
    private LinearLayout mLlSiteLayout;
    private LinearLayout mLlSale;
    private LinearLayout mLlPerson;
    private LinearLayout mLlSet;
    /**
     * 数据源
     */
    private UserBean data;

    /**
     * 单列
     *
     * @return this
     */
    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    public void loadData(boolean firstLoad) {
        super.loadData(firstLoad);
        mPresenter.requestData();
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onCompleted();
            }
        })
                .delay(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mPresenter.getUserCoin();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                    }
                });
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onCompleted();
            }
        })
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mPresenter.getOrder();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                    }
                });

    }

    @Override
    public void initView() {
        mIvUserImg = (CircularImageView) findViewById(R.id.iv_fragment_mine_user_img);
        mTvUserName = (TextView) findViewById(R.id.tv_fragment_mine_user_name);
        mTvUserPhone = (TextView) findViewById(R.id.tv_fragment_mine_user_phone);
        mRbNotify = (BadgeRadioButton) findViewById(R.id.rb_fragment_mine_notify);
        mTvIntegral = (TextView) findViewById(R.id.tv_fragment_mine_integral);
        mTvMoney = (TextView) findViewById(R.id.tv_fragment_mine_money);
        mLlOrder = (LinearLayout) findViewById(R.id.ll_fragment_mine_order);
        mRb0 = (BadgeRadioButton) findViewById(R.id.rb_fragment_mine_0);
        mRb1 = (BadgeRadioButton) findViewById(R.id.rb_fragment_mine_1);
        mRb2 = (BadgeRadioButton) findViewById(R.id.rb_fragment_mine_2);
        mRb3 = (BadgeRadioButton) findViewById(R.id.rb_fragment_mine_3);
        mRb4 = (BadgeRadioButton) findViewById(R.id.rb_fragment_mine_4);
        mRg = (RadioGroup) findViewById(R.id.rg_fragment_mine);
        mLlSiteLayout = (LinearLayout) findViewById(R.id.ll_fragment_mine_site_layout);
        mLlSale = (LinearLayout) findViewById(R.id.ll_fragment_mine_sale);
        mLlPerson = (LinearLayout) findViewById(R.id.ll_fragment_mine_person);
        mLlSet = (LinearLayout) findViewById(R.id.ll_fragment_mine_set);
        mLlWallet = (LinearLayout) findViewById(R.id.ll_fragment_wallet);
        mLlIntegral = (LinearLayout) findViewById(R.id.ll_fragment_integral);

        mLlSale.setOnClickListener(this);
        mLlPerson.setOnClickListener(this);
        mLlSet.setOnClickListener(this);
        mLlOrder.setOnClickListener(this);
        mLlWallet.setOnClickListener(this);
        mLlIntegral.setOnClickListener(this);
        mLlSiteLayout.setOnClickListener(this);

        mRb0.setOnClickListener(this);
        mRb1.setOnClickListener(this);
        mRb2.setOnClickListener(this);
        mRb3.setOnClickListener(this);
        mRb4.setOnClickListener(this);
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }


    /**
     * 设置用户数据
     *
     * @param data 数据
     */
    private void initData(UserBean data) {
        if (!TextUtils.isEmpty(data.largeAvatar)) {
            LoaderFactory.getLoader().loadNet(mIvUserImg, data.largeAvatar);
        } else {
            LoaderFactory.getLoader().loadResource(mIvUserImg, R.mipmap.icon_app);
        }
        mTvUserName.setText(data.nickname);
        mTvUserPhone.setText("账号：" + data.verifiedMobile);
    }

    /**
     * 设置用户数据
     *
     * @param data 数据
     */
    private void initMoneyData(UserCoinBean data) {
        mTvIntegral.setText(String.valueOf(data.point));
        mTvMoney.setText(String.format(ResUtils.getString(R.string.mine_money), Float.valueOf(data.cash)));
    }

    /**
     * 设置用户数据
     *
     * @param data 数据
     */
    private void initNumData(OrderBean data) {
        mRb0.setBadgeNumber(data.unpaid);
        mRb1.setBadgeNumber(data.paid);
        mRb3.setBadgeNumber(data.success);
        mRb4.setBadgeNumber(data.refunded);
    }

    @Override
    public void onResponseData(boolean isSuccess, UserBean data) {
        if (isSuccess) {
            this.data = data;
            initData(data);
        }
    }

    @Override
    public void onGetUserCoin(boolean isSuccess, UserCoinBean data) {
        if (isSuccess) {
            initMoneyData(data);
        }
    }


    @Override
    public void onGetOrder(boolean isSuccess, OrderBean data) {
        if (isSuccess) {
            initNumData(data);
        }

    }

    @Override
    public void onUploadAvatar(boolean isSuccess, UploadBean data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_fragment_mine_sale:
                //我的优惠券
                startActivityEx(MyCouponActivity.class);
                break;
            case R.id.ll_fragment_mine_person:
                //个人资料
                Bundle bundle = new Bundle();
                if (null != data) {
                    bundle.putString(Count.USER_URL, data.largeAvatar);
                    bundle.putString(Count.USER_NAME, data.nickname);
                    bundle.putString(Count.USER_BIRTHDAY, data.birthday);
                    bundle.putString(Count.USER_GENDER, data.gender);
                }
                startActivityEx(UserDataActivity.class, bundle);
                break;
            case R.id.ll_fragment_mine_set:
                //设置
                startActivityEx(SettingActivity.class);
                break;
            case R.id.ll_fragment_mine_order:
                //订单
                startActivityEx(OrderActivity.class);
                break;
            case R.id.rb_fragment_mine_0:
                //待付款
                Bundle bundleCreated = new Bundle();
                bundleCreated.putInt(Count.ORDER_STATUS, 1);
                startActivityEx(OrderActivity.class, bundleCreated);
                break;
            case R.id.rb_fragment_mine_1:
                //待自提
                Bundle bundlePaid = new Bundle();
                bundlePaid.putInt(Count.ORDER_STATUS, 2);
                startActivityEx(OrderActivity.class, bundlePaid);
                break;
            case R.id.rb_fragment_mine_2:
                //待收货
//                Bundle bundlePaid2 = new Bundle();
//                bundlePaid2.putInt(Count.ORDER_STATUS, 3);
//                startActivityEx(OrderActivity.class, bundlePaid2);
                break;
            case R.id.rb_fragment_mine_3:
                //已完成
                Bundle bundleSuccess = new Bundle();
                bundleSuccess.putInt(Count.ORDER_STATUS, 3);
                startActivityEx(OrderActivity.class, bundleSuccess);
                break;
            case R.id.rb_fragment_mine_4:
                //售后
                Bundle bundleRefunding = new Bundle();
                bundleRefunding.putInt(Count.ORDER_STATUS, 4);
                startActivityEx(OrderActivity.class, bundleRefunding);
                break;
            case R.id.ll_fragment_wallet:
                //钱包
                startActivityEx(MyWalletActivity.class);
                break;
            case R.id.ll_fragment_integral:
                //积分
                startActivityEx(IntegralDetailActivity.class);
                break;
            case R.id.ll_fragment_mine_site_layout:
                //地址管理
                startActivityEx(ManageAddressActivity.class);
                break;
        }
    }
}
