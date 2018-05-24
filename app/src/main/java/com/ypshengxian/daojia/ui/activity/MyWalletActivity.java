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
import com.ypshengxian.daojia.mvp.contract.IMyWalletContract;
import com.ypshengxian.daojia.mvp.presenter.MyWalletPresenter;
import com.ypshengxian.daojia.network.bean.UserCashFlowBean;
import com.ypshengxian.daojia.network.bean.UserCoinBean;
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
 * @Describe 我的钱包
 */
public class MyWalletActivity extends BaseMVPYpFreshActicity<IMyWalletContract.View, MyWalletPresenter> implements IMyWalletContract.View, View.OnClickListener {

    /**
     * 充值
     */
    private TextView mTvRecharge;
    /**
     * 总金额
     */
    private TextView mTvTotalAmount;
    /**
     * 钱包安全
     */
    private TextView mTvSafety;
    /**
     * 总金额
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
    private BaseQuickAdapter<UserCashFlowBean.DataBean, BaseViewHolder> mAdapter;
    /**
     * 数据源
     */
    private List<UserCashFlowBean.DataBean> mData = new ArrayList<UserCashFlowBean.DataBean>();

    @Override
    protected MyWalletPresenter createPresenter() {
        return new MyWalletPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.my_wallet_title_text));
        mTvRecharge = (TextView) findViewById(R.id.tv_activity_wallet_recharge);
        mTvRecharge.setOnClickListener(this);
        mTvTotalAmount = (TextView) findViewById(R.id.tv_activity_wallet_total_amount);
        mTvTotalAmount.setOnClickListener(this);
        mTvSafety = (TextView) findViewById(R.id.tv_activity_wallet_safety);
        mTvSafety.setOnClickListener(this);
        mTvAmount = (TextView) findViewById(R.id.tv_activity_wallet_amount);
        mRvHistory = (RecyclerView) findViewById(R.id.rv_activity_wallet_history);
        mRvHistory.setItemViewCacheSize(500);
        mSrRefresh = (SmartRefreshLayout) findViewById(R.id.sr_activity_wallet_refresh);
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
        mAdapter = new BaseQuickAdapter<UserCashFlowBean.DataBean, BaseViewHolder>(R.layout.item_my_wallet_history, mData) {
            @Override
            protected void convert(BaseViewHolder helper, UserCashFlowBean.DataBean item) {
                helper.setText(R.id.tv_item_my_wallet_history_title, item.title);
                helper.setText(R.id.tv_item_my_wallet_history_date, item.created_time);
                /**
                 * outflow
                 * inflow
                 */
                if (item.type.equals("outflow")) {
                    helper.setText(R.id.tv_item_my_wallet_history_amount,
                            String.format(ResUtils.getString(R.string.my_wallet_item_add), (Float.valueOf(item.amount) / 100f)));
                } else {
                    helper.setText(R.id.tv_item_my_wallet_history_amount,
                            String.format(ResUtils.getString(R.string.my_wallet_item_minus), (Float.valueOf(item.amount) / 100f)));
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
            case R.id.tv_activity_wallet_recharge:
                break;
            case R.id.tv_activity_wallet_total_amount:
                break;
            case R.id.tv_activity_wallet_safety:
                startActivityEx(PurseSafetyActivity.class);
                break;
        }
    }

    @Override
    public void onGetUserCoin(boolean isSuccess, UserCoinBean data) {
        if (isSuccess) {
            mTvAmount.setText(String.format(ResUtils.getString(R.string.mine_money), Float.valueOf(data.cash)));

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
    public void onResponseData(boolean isSuccess, UserCashFlowBean data) {
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
