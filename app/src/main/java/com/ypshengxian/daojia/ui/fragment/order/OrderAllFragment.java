package com.ypshengxian.daojia.ui.fragment.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IOrderAllContract;
import com.ypshengxian.daojia.mvp.presenter.OrderAllPresenter;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.MyOrderBean;
import com.ypshengxian.daojia.ui.activity.OrderDetailsActivity;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.RecyclerDivider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 全部订单
 *
 * @author Yan
 * @date 2018-03-22
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class OrderAllFragment extends BaseMVPYpFreshFragment<IOrderAllContract.View, OrderAllPresenter> implements
        IOrderAllContract.View {
    /** 数据展示 */
    private RecyclerView mRlCount;
    /** 适配器 */
    private BaseQuickAdapter<MyOrderBean.DataBean,BaseViewHolder> mAdapter;
    /** 数据源 */
    private List<MyOrderBean.DataBean>  mData=new ArrayList<MyOrderBean.DataBean>();
    /** 状态码 */
    private String status=null;

    public static OrderAllFragment newInstance() {

        Bundle args = new Bundle();

        OrderAllFragment fragment = new OrderAllFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_order_all;
    }

    @Override
    public void initView() {
        mRlCount = (RecyclerView)findViewById(R.id.rl_fragment_order_all_count);
        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mRlCount.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL,24));
        mAdapter=new BaseQuickAdapter<MyOrderBean.DataBean, BaseViewHolder>(R.layout.item_order_all,mData) {
            @Override
            protected void convert(BaseViewHolder helper, MyOrderBean.DataBean item) {
                Button mBtnRefund=helper.getView(R.id.btn_item_order_all_one);
                Button mBtnDelete=helper.getView(R.id.btn_item_order_all_two);
                Button mBtnPay=helper.getView(R.id.btn_item_order_all_three);
                switch (item.status){
                 default:break;
                    case "created":
                        //待付款
                        status="待付款";
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.color_yellow_order_details_wait_pick));
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.VISIBLE);
                        mBtnPay.setText("付款");
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mBtnDelete.setText("删除订单");
                        break;
                    case "paid":
                        //待自提
                        status="待自提";
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.color_yellow_order_details_wait_pick));
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.VISIBLE);
                        mBtnPay.setText("提货");
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mBtnDelete.setText("申请配送");
                        break;
                    case "refunding":
                        //退款中
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.color_yellow_order_details_wait_pick));
                        status="退款中";
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.GONE);
                        mBtnDelete.setVisibility(View.GONE);
                        break;
                    case "refunded":
                        //已退款
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.preference_price));
                        status="已退款";
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.GONE);
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mBtnDelete.setText("删除订单");
                        break;
                    case "closed":
                        //待收货
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.color_yellow_order_details_wait_pick));
                        status="待收货";
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.GONE);
                        mBtnPay.setText("收货");
                        mBtnDelete.setVisibility(View.GONE);
                        break;
                    case "finished":
                        //已完成
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.preference_price));
                        status="已完成";
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.GONE);
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mBtnDelete.setText("删除订单");
                        break;
                }
                helper.setText(R.id.tv_item_order_all_goods,String.format(ResUtils.getString(R.string.order_all_good),item.item.size()));
               helper.setText(R.id.tv_item_order_all_number,
                       String.format(ResUtils.getString(R.string.order_all_number),item.sn));
                helper.setText(R.id.tv_item_order_all_status,status);
                helper.setText(R.id.tv_item_order_all_money,
                        String.format(ResUtils.getString(R.string.order_all_money),item.pay_amount));
                RecyclerView recyclerView=helper.getView(R.id.rl_item_order_all_child_count);
                recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getContext(),LinearLayoutManager.HORIZONTAL,false));
                BaseQuickAdapter<MyOrderBean.DataBean.ItemBean,BaseViewHolder> mChildAdapter=
                        new BaseQuickAdapter<MyOrderBean.DataBean.ItemBean, BaseViewHolder>(R.layout.item_order_all_child,item.item) {
                            @Override
                            protected void convert(BaseViewHolder helper, MyOrderBean.DataBean.ItemBean item) {
                                if(null!=item.picture&&item.picture.size()>0) {
                                    LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_item_order_all_child_img), item.picture.get(0));
                                }
                            }
                        };
                recyclerView.setAdapter(mChildAdapter);
            }
        };
        mRlCount.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               Bundle bundle=new Bundle();
               bundle.putString(Count.CODE_SN,mData.get(position).sn);
               bundle.putString(Count.CODE_STATUS,status);
               startActivityEx(OrderDetailsActivity.class,bundle);
            }
        });

    }

    @Override
    public void loadData(boolean firstLoad) {
        super.loadData(firstLoad);
        if (firstLoad) {
            Map<String, String> map = new WeakHashMap<String, String>();
            YpCache.setMap(map);
            mPresenter.getMyOrder(map);
        }
    }

    @Override
    protected OrderAllPresenter createPresenter() {
        return new OrderAllPresenter();
    }

    @Override
    public void onGetMyOrder(boolean isSuccess, MyOrderBean data) {
        if (isSuccess) {
            LogUtils.e(data.data.get(0).pay_amount);
            mData=data.data;
            mAdapter.setNewData(mData);
         }
    }

    @Override
    public void onCloseOrder(boolean isSuccess, BaseModuleApiResult data) {

    }

    @Override
    public void onRefundOrder(boolean isSuccess, BaseModuleApiResult data) {

    }
}
