package com.ypshengxian.daojia.ui.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IGetGoodsOrderinfoContract;
import com.ypshengxian.daojia.mvp.presenter.GetGoodsOrderInfoPresenter;
import com.ypshengxian.daojia.network.bean.LineItemBean;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.RecyclerDivider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 取单订单详情
 */
public class GetGoodsOrderInfoActivity extends BaseMVPYpFreshActicity<IGetGoodsOrderinfoContract.View, GetGoodsOrderInfoPresenter> implements IGetGoodsOrderinfoContract.View, View.OnClickListener {

    /**
     * 头布局
     */
    private View mHeaderView;

    /**
     * 底布局
     */
    private View mFooterView;

    private RecyclerView mRvDetails;

    /**
     * 适配器
     */
    private BaseQuickAdapter<LineItemBean.ItemsBean, BaseViewHolder> mAdapter;
    /**
     * 数据源
     */
    private List<LineItemBean.ItemsBean> mData = new ArrayList<LineItemBean.ItemsBean>();
    private ImageView mIvNoticeClose;
    private RelativeLayout mRlNotice;
    private TextView mTvStoreName;
    private TextView mTvStorePhone;
    private TextView mTvAddress;
    private TextView mTvWaitPick;
    private TextView mTvOrderNum;
    private TextView mTvOrderTime;
    private TextView mTvBag;
    private TextView mTvContentCoupon;
    private TextView mTvSendDate;
    private TextView mTvSendTime;

    private TextView mTvGoodsMoney;
    private TextView mTvCouponMoney;
    private TextView mTvBagMoney;
    private TextView mTvAllMoney;
    private TextView mTvGoInfo;

    /**
     * 订单号
     */
    private String mSn;
    private TextView mTvMinusMoney;
    private TextView mTvActualMoney;
    private LineItemBean lineItemBean;

    @Override
    public void onResponseData(boolean isSuccess, LineItemBean data) {
        if (isSuccess) {
            LogUtils.e(data.createTime);
            mData = data.items;
            lineItemBean = data;
            mAdapter.setNewData(mData);
            initData();
        }
        mData = data.items;
    }

    @Override
    protected GetGoodsOrderInfoPresenter createPresenter() {
        return new GetGoodsOrderInfoPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_get_goods_order_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSn = bundle.getString(Count.GET_GOODS_ORDER_SN);
        }
        initView();
        LogUtils.e(mSn);
        Map<String, String> map = new WeakHashMap<String, String>();
        map.put("sn", mSn);
        YpCache.setMap(map);
        mPresenter.requestData(map);
    }

    public void initView() {
        TitleUtils.setTitleBar(this, R.string.get_goods_order_info);

        mRvDetails = (RecyclerView) findViewById(R.id.rv_activity_get_goods_order_info);

        //头部
        mHeaderView = getLayoutInflater().inflate(R.layout.layout_get_goods_order_info_top, null, false);
        mIvNoticeClose = (ImageView) mHeaderView.findViewById(R.id.iv_activity_get_goods_order_info_notice_close);
        mIvNoticeClose.setOnClickListener(this);
        mRlNotice = (RelativeLayout) mHeaderView.findViewById(R.id.rl_activity_get_goods_order_info_notice);
        mTvStoreName = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_store_name);
        mTvStorePhone = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_store_phone);
        mTvAddress = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_address);
        mTvWaitPick = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_wait_pick);
        mTvOrderNum = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_order_num);
        mTvOrderTime = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_order_time);
        mTvBag = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_content_bag);
        mTvContentCoupon = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_content_coupon);
        mTvSendDate = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_content_date);
        mTvSendTime = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_content_time);

        //底部
        mFooterView = getLayoutInflater().inflate(R.layout.layout_get_goods_order_info_bottom, null, false);
        mTvGoodsMoney = (TextView) mFooterView.findViewById(R.id.tv_activity_get_goods_order_info_goods_money);
        mTvCouponMoney = (TextView) mFooterView.findViewById(R.id.tv_activity_get_goods_order_info_coupon_money);
        mTvBagMoney = (TextView) mFooterView.findViewById(R.id.tv_activity_get_goods_order_info_bag_money);
        mTvAllMoney = (TextView) mFooterView.findViewById(R.id.tv_activity_get_goods_order_info_all_money);
        mTvMinusMoney = (TextView) mFooterView.findViewById(R.id.tv_activity_get_goods_order_info_minus_money);
        mTvActualMoney = (TextView) mFooterView.findViewById(R.id.tv_activity_get_goods_order_info_actual_money);
        mTvGoInfo = (TextView) mFooterView.findViewById(R.id.tv_activity_get_goods_order_go_info);
        mTvGoInfo.setOnClickListener(this);

        mRvDetails.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new BaseQuickAdapter<LineItemBean.ItemsBean, BaseViewHolder>(R.layout.item_order_details_rv, mData) {
            @Override
            protected void convert(BaseViewHolder helper, LineItemBean.ItemsBean item) {
                helper.setText(R.id.tv_item_order_details_rv_goods_name, item.title);
                ImageView view = (ImageView) helper.getView(R.id.iv_item_order_details_goods_img);
                LoaderFactory.getLoader().loadNet(view, item.img);
                helper.setText(R.id.tv_item_order_details_rv_goods_new,
                        String.format(ResUtils.getString(R.string.cart_text_new_price), item.price, item.unit));
                helper.setText(R.id.tv_item_order_details_rv_goods_old,
                        String.format(ResUtils.getString(R.string.cart_text_old), item.prePrice));
                helper.setText(R.id.tv_item_order_details_rv_goods_num,
                        String.format(ResUtils.getString(R.string.pay_order_num), item.num));

            }
        };

        mRvDetails.setAdapter(mAdapter);
        mRvDetails.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mAdapter.setHeaderView(mHeaderView);
        mAdapter.setFooterView(mFooterView);
    }


    /**
     * 设置数据
     */
    private void initData() {
        LineItemBean.ShopBean shopBean = lineItemBean.shop;
        mTvStoreName.setText(shopBean.title);
        mTvAddress.setText(shopBean.address);
        mTvStorePhone.setText(shopBean.phone);
        mTvWaitPick.setText("待提货");
        mTvOrderNum.setText(lineItemBean.sn);
        mTvOrderTime.setText(lineItemBean.createTime);
        LineItemBean.PacketBean packetBean = lineItemBean.packet;
        if (null != packetBean) {
            mTvBag.setText(packetBean.name);
            mTvBagMoney.setText(packetBean.price);
        } else {
            mTvBag.setText("未选择购物袋");
            mTvBagMoney.setText(String.format(ResUtils.getString(R.string.price_add), ResUtils.getString(R.string.price_zero)));
        }

        mTvContentCoupon.setText("未选择优惠卷");
        if (TextUtils.isEmpty(lineItemBean.deliveryDay)) {
            mTvSendDate.setText("未选择收货日期");
        } else {
            mTvSendDate.setText(lineItemBean.deliveryDay);
        }
        if (TextUtils.isEmpty(lineItemBean.deliveryTime)) {
            mTvSendTime.setText("未选择收货时间");
        } else {
            mTvSendTime.setText(lineItemBean.deliveryTime);
        }

        mTvGoodsMoney.setText(String.format(ResUtils.getString(R.string.price_float), Float.valueOf(lineItemBean.priceAmount)));
        mTvAllMoney.setText(String.format(ResUtils.getString(R.string.price_float), Float.valueOf(lineItemBean.priceAmount)));
        mTvCouponMoney.setText(String.format(ResUtils.getString(R.string.price_minus), ResUtils.getString(R.string.price_zero)));
        mTvMinusMoney.setText(String.format(ResUtils.getString(R.string.price_minus), ResUtils.getString(R.string.price_zero)));
        mTvActualMoney.setText(String.format(ResUtils.getString(R.string.price_float), Float.valueOf(lineItemBean.priceAmount)));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_activity_get_goods_order_info_notice_close:
                mRlNotice.setVisibility(View.GONE);
                break;
            case R.id.tv_activity_get_goods_order_go_info:
                Bundle bundle = new Bundle();
                bundle.putString(Count.GOODS_PRICE, String.format(ResUtils.getString(R.string.price_float), Float.valueOf(lineItemBean.priceAmount)));
                startActivityEx(GetGoodsOrderDetailActivity.class,bundle);
                break;
        }
    }
}
