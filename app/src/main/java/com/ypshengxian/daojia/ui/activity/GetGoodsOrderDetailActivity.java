package com.ypshengxian.daojia.ui.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.IGetGoodsOrderDetailContract;
import com.ypshengxian.daojia.mvp.presenter.GetGoodsOrderDetailPresenter;
import com.ypshengxian.daojia.network.bean.LineItemBean;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.RecyclerDivider;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 取单订单详情
 */
public class GetGoodsOrderDetailActivity extends BaseMVPYpFreshActicity<IGetGoodsOrderDetailContract.View, GetGoodsOrderDetailPresenter> implements IGetGoodsOrderDetailContract.View {

    /**
     * 头布局
     */
    private View mHeaderView;

    private RecyclerView mRvDetails;

    /**
     * 适配器
     */
    private BaseQuickAdapter<LineItemBean.ItemsBean, BaseViewHolder> mAdapter;
    /**
     * 数据源
     */
    private List<LineItemBean.ItemsBean> mData = new ArrayList<LineItemBean.ItemsBean>();
    private TextView mTvGoodsMoney;
    private TextView mTvCouponMoney;
    private TextView mTvBagMoney;
    private TextView mTvAllMoney;
    private TextView mTvGoInfo;
    private TextView mTvMinusMoney;
    private TextView mTvActualMoney;

    private String goodsPrice;

    @Override
    public void onResponseData(boolean isSuccess, Object data) {

    }

    @Override
    protected GetGoodsOrderDetailPresenter createPresenter() {
        return new GetGoodsOrderDetailPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_get_goods_order_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            goodsPrice = bundle.getString(Count.GOODS_PRICE);
        }
        initView();
    }

    public void initView() {
        TitleUtils.setTitleBar(this, R.string.get_goods_order_info);
        mRvDetails = (RecyclerView) findViewById(R.id.rv_activity_get_goods_order_detail);

        //头部
        mHeaderView = getLayoutInflater().inflate(R.layout.layout_get_goods_order_info_bottom, null, false);
        mTvGoodsMoney = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_goods_money);
        mTvCouponMoney = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_coupon_money);
        mTvBagMoney = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_bag_money);
        mTvAllMoney = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_all_money);
        mTvMinusMoney = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_minus_money);
        mTvActualMoney = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_info_actual_money);
        mTvGoInfo = (TextView) mHeaderView.findViewById(R.id.tv_activity_get_goods_order_go_info);
        mTvGoInfo.setVisibility(View.GONE);

        mRvDetails.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new BaseQuickAdapter<LineItemBean.ItemsBean, BaseViewHolder>(R.layout.item_get_goods_order_detail, mData) {
            @Override
            protected void convert(BaseViewHolder helper, LineItemBean.ItemsBean item) {


            }
        };

        mRvDetails.setAdapter(mAdapter);
        mRvDetails.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mAdapter.setHeaderView(mHeaderView);

        setAmount();
    }

    private void setAmount() {
        mTvBagMoney.setText(String.format(ResUtils.getString(R.string.price_add), ResUtils.getString(R.string.price_zero)));
        mTvGoodsMoney.setText(goodsPrice);
        mTvAllMoney.setText(goodsPrice);
        mTvCouponMoney.setText(String.format(ResUtils.getString(R.string.price_minus), ResUtils.getString(R.string.price_zero)));
        mTvMinusMoney.setText(String.format(ResUtils.getString(R.string.price_minus), ResUtils.getString(R.string.price_zero)));
        mTvActualMoney.setText(goodsPrice);
    }
}
