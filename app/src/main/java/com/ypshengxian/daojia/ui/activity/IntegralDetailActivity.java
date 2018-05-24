package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IIntegralDetailContract;
import com.ypshengxian.daojia.mvp.presenter.IntegralDetailPresenter;
import com.ypshengxian.daojia.network.bean.UserCoinBean;
import com.ypshengxian.daojia.network.bean.UserPointFlowBean;
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
 * @Describe 我的钱包
 */
public class IntegralDetailActivity extends BaseMVPYpFreshActicity<IIntegralDetailContract.View, IntegralDetailPresenter> implements IIntegralDetailContract.View, View.OnClickListener {


    /**
     * 兑换商品
     */
    private TextView mTvExchange;
    /**
     * 总积分
     */
    private TextView mTvTotalIntegral;
    /**
     * 兑换余额
     */
    private TextView mTvExchangeBalance;
    /**
     * 总积分
     */
    private TextView mTvAmount;
    /**
     * 历史记录
     */
    private RecyclerView mRvHistory;
    /**
     * 下拉刷新
     */
    private SmartRefreshLayout mSrRefresh;
    /**
     * 开始的位置
     */
    private int start = 0;
    /**
     * 适配器
     */
    private BaseQuickAdapter<UserPointFlowBean.DataBean, BaseViewHolder> mAdapter;
    /**
     * 数据源
     */
    private List<UserPointFlowBean.DataBean> mData = new ArrayList<UserPointFlowBean.DataBean>();


    @Override
    protected IntegralDetailPresenter createPresenter() {
        return new IntegralDetailPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_integral_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.integral_detail_title_text));
        mTvExchange = (TextView) findViewById(R.id.tv_activity_integral_exchange);
        mTvTotalIntegral = (TextView) findViewById(R.id.tv_activity_integral_total_integral);
        mTvTotalIntegral.setOnClickListener(this);
        mTvExchangeBalance = (TextView) findViewById(R.id.tv_activity_integral_exchange_balance);
        mTvExchangeBalance.setOnClickListener(this);
        mTvAmount = (TextView) findViewById(R.id.tv_activity_integral_amount);
        mRvHistory = (RecyclerView) findViewById(R.id.rv_activity_integral_history);
        mSrRefresh = (SmartRefreshLayout) findViewById(R.id.sr_activity_integral_refresh);
        mSrRefresh.setEnableRefresh(false);
        mSrRefresh.setOnLoadmoreListener(refreshlayout -> {
            start += 10;
            Map<String, String> map = new WeakHashMap<String, String>();
            map.put("limit", "10");
            map.put("start", String.valueOf(start));
            YpCache.setMap(map);
            mPresenter.requestData(map);
        });

        mRvHistory.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mRvHistory.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL, 0));
        mAdapter = new BaseQuickAdapter<UserPointFlowBean.DataBean, BaseViewHolder>(R.layout.item_integral_history, mData) {
            @Override
            protected void convert(BaseViewHolder helper, UserPointFlowBean.DataBean item) {
                helper.setText(R.id.tv_item_my_wallet_history_title, item.title);
                helper.setText(R.id.tv_item_my_wallet_history_date, item.created_time);
                /**
                 * outflow 支出
                 * inflow  收入
                 */
                if (item.type.equals("outflow")) {
                    helper.setText(R.id.tv_item_my_wallet_history_amount, "- " + item.amount);
                } else {
                    helper.setText(R.id.tv_item_my_wallet_history_amount, "+ " + item.amount);
                }

            }
        };
        mRvHistory.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mPresenter.getUserCoin();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_activity_integral_total_integral:
                break;
            case R.id.tv_activity_integral_exchange_balance:
                startActivityEx(IntegralExchangeActivity.class);
                break;
        }
    }


    @Override
    public void onGetUserCoin(boolean isSuccess, UserCoinBean data) {
        if (isSuccess) {
            mTvAmount.setText(String.valueOf(data.cash));

            //请求历史记录
            mData.clear();
            start = 0;
            mSrRefresh.resetNoMoreData();
            Map<String, String> map = new WeakHashMap<String, String>();
            map.put("limit", "10");
            map.put("start", String.valueOf(start));
            YpCache.setMap(map);
            mPresenter.requestData(map);
        }
    }

    @Override
    public void onResponseData(boolean isSuccess, UserPointFlowBean data) {
        if (isSuccess) {
            mData.addAll(data.data);
            mAdapter.setNewData(mData);
            mSrRefresh.finishLoadmore();
            if (start >= data.total) {
                mSrRefresh.finishLoadmoreWithNoMoreData();
            }
        }
        setEmptyView();
    }

    /**
     * 设置空布局
     */
    private void setEmptyView() {
        int size = mData.size();
        if (0 == size) {
//            mAdapter.setEmptyView(mEmptyView);
        }
    }
}
