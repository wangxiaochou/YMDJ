package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.annotation.ActivityOption;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.ItemEvent;
import com.ypshengxian.daojia.event.JumpEvent;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IOrderAllContract;
import com.ypshengxian.daojia.mvp.presenter.OrderAllPresenter;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.MyOrderBean;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.StringUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.EmptyView;
import com.ypshengxian.daojia.view.RecyclerDivider;
import com.ypshengxian.daojia.view.YpDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 订单界面
 *
 * @author Yan
 * @date 2018-03-22
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
@ActivityOption(reqLogin = true)
public class OrderActivity extends BaseMVPYpFreshActicity<IOrderAllContract.View, OrderAllPresenter> implements
        IOrderAllContract.View {
    /**
     * tab布局
     */
    private TabLayout mTlTab;
    /**
     * tab数据
     */
    private List<String> tabs = new ArrayList<String>();
    /**
     * 数据展示
     */
    private RecyclerView mRlCount;
    /**
     * 适配器
     */
    private BaseQuickAdapter<MyOrderBean.DataBean, BaseViewHolder> mAdapter;
    /**
     * 数据源
     */
    private List<MyOrderBean.DataBean> mData = new ArrayList<MyOrderBean.DataBean>();
    /**
     * 状态码
     */
    private String status = null;
    /**
     * 空数据
     */
    private EmptyView mEmptyView;
    /**
     * 删除位置
     */
    private int pos;
    /**
     * 加载更多
     */
    private SmartRefreshLayout mSrRefresh;
    /**
     * 开始的位置
     */
    private int start = 0;
    /**
     * 当Tab点击时候的状态
     */
    private String tabStatus = "";
    /** 当前点击的TAB */
    private int tabPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            int position = bundle.getInt(Count.ORDER_STATUS);
            mTlTab.getTabAt(position).select();
        } else {
            Map<String, String> map = new WeakHashMap<String, String>();
            map.put("start", String.valueOf(start));
            YpCache.setMap(map);
            mPresenter.getMyOrder(map);
        }
    }

    @Override
    protected OrderAllPresenter createPresenter() {
        return new OrderAllPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_order;
    }

    private void initView() {
        TitleUtils.setTitleBar(this, "我的订单");
        mTlTab = (TabLayout) findViewById(R.id.tl_activity_order_tab);
        tabs.add("全部订单");
        tabs.add("待付款");
        tabs.add("待自提");
        tabs.add("已完成");
        tabs.add("售后");
        initTabs(tabs);
        mRlCount = (RecyclerView) findViewById(R.id.rl_activity_order_count);
        mRlCount.setItemViewCacheSize(500);
        mSrRefresh = (SmartRefreshLayout) findViewById(R.id.sr_activity_order_refresh);
        mSrRefresh.setEnableRefresh(false);
        mSrRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                start += 10;
                Map<String, String> map = new WeakHashMap<String, String>();
                map.put("status", tabStatus);
                map.put("start", String.valueOf(start));
                YpCache.setMap(map);
                mPresenter.getMyOrder(map);
            }
        });

        //空布局
        mEmptyView = new EmptyView(Utils.getContext());
        mEmptyView.setMessage(R.string.msg_order_empty)
                .setBtnText(R.string.text_go_home, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        EventBus.getDefault().post(new JumpEvent(Count.JUMP_MAIN));
                    }
                });
        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mRlCount.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL, 24));
        mAdapter = new BaseQuickAdapter<MyOrderBean.DataBean, BaseViewHolder>(R.layout.item_order_all, mData) {
            @Override
            protected void convert(BaseViewHolder helper, MyOrderBean.DataBean item) {
                Button mBtnRefund = helper.getView(R.id.btn_item_order_all_one);
                Button mBtnDelete = helper.getView(R.id.btn_item_order_all_two);
                Button mBtnPay = helper.getView(R.id.btn_item_order_all_three);
//                    created      订单创建未付款  //待付款
//                    paid         订单付款未提货  //待自提
//                    success      订单已提货完成  //已完成
//                    refunding    订单售后中  退款
//                    refunded     订单售后完成  售后
                switch (item.status) {
                    case "created":
                        //待付款
                        status = "未付款";
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.color_yellow_order_details_wait_pick));
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.VISIBLE);
                        mBtnPay.setText("付款");
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mBtnDelete.setText("取消订单");
                        break;
                    case "paid":
                        //待自提
                        status = "待自提";
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.color_yellow_order_details_wait_pick));
                        mBtnRefund.setVisibility(View.VISIBLE);
                        mBtnRefund.setText("申请退款");
                        mBtnPay.setVisibility(View.VISIBLE);
                        mBtnPay.setText("提货");
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mBtnDelete.setText("申请配送");
                        break;
                    case "refunding":
                        switch (item.refund_status) {
                            case "auditing":
                                status = "审核中";
                                break;
                            case "refused ":
                                status = "拒绝退款";
                                break;
                            case "refunded":
                                status = "退款成功";
                                break;
                            default:
                                status = "退款中";
                                break;
                        }
                        //退款中
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.color_yellow_order_details_wait_pick));
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.GONE);
                        mBtnDelete.setVisibility(View.GONE);
                        break;
                    case "refunded":
                        //已退款
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.preference_price));
                        status = "已退款";
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.GONE);
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mBtnDelete.setText("删除订单");
                        break;
                    case "success":
                        //已完成
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.preference_price));
                        status = "已完成";
                        mBtnRefund.setVisibility(View.VISIBLE);
                        mBtnRefund.setText("售后申请");
                        mBtnPay.setVisibility(View.GONE);
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mBtnDelete.setText("删除订单");
                        break;
                    case "closed":
                        helper.setTextColor(R.id.tv_item_order_all_status,
                                ResUtils.getColor(R.color.color_yellow_order_details_wait_pick));
                        status = "已取消";
                        mBtnRefund.setVisibility(View.GONE);
                        mBtnPay.setVisibility(View.GONE);
                        mBtnDelete.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                helper.setText(R.id.tv_item_order_all_goods, String.format(ResUtils.getString(R.string.order_all_good), item.item.size()));
                helper.setText(R.id.tv_item_order_all_number,
                        String.format(ResUtils.getString(R.string.order_all_number), item.sn));
                helper.setText(R.id.tv_item_order_all_status, status);
                helper.setText(R.id.tv_item_order_all_money,
                        String.format(ResUtils.getString(R.string.order_all_money), (Float.valueOf(item.pay_amount) / 100f)));
                helper.addOnClickListener(R.id.btn_item_order_all_one);
                helper.addOnClickListener(R.id.btn_item_order_all_two);
                helper.addOnClickListener(R.id.btn_item_order_all_three);
                RecyclerView recyclerView = helper.getView(R.id.rl_item_order_all_child_count);
                recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getContext(), LinearLayoutManager.HORIZONTAL, false));
                BaseQuickAdapter<MyOrderBean.DataBean.ItemBean, BaseViewHolder> mChildAdapter =
                        new BaseQuickAdapter<MyOrderBean.DataBean.ItemBean, BaseViewHolder>(R.layout.item_order_all_child, item.item) {
                            @Override
                            protected void convert(BaseViewHolder helper, MyOrderBean.DataBean.ItemBean item) {
                                if (!TextUtils.isEmpty(item.thumb)) {
                                    LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_item_order_all_child_img), item.thumb);
                                } else {
                                    if (null != item.picture && item.picture.size() > 0) {
                                        LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_item_order_all_child_img), item.picture.get(0));
                                    }
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
                pos = position;
                Bundle bundle = new Bundle();
                bundle.putString(Count.CODE_SN, mData.get(position).sn);
                String mStatus = null;
                switch (mData.get(position).status) {
                    case "created":
                        mStatus = "未付款";
                        break;
                    case "paid":
                        mStatus = "待自提";
                        break;
                    case "refunding":
                        switch (mData.get(position).refund_status) {
                            case "auditing":
                                mStatus = "审核中";
                                break;
                            case "refused ":
                                mStatus = "拒绝退款";
                                break;
                            case "refunded":
                                mStatus = "退款成功";
                                break;
                            default:
                                mStatus = "退款中";
                                break;
                        }
                        break;
                    case "refunded":
                        mStatus = "已退款";
                        break;
                    case "success":
                        mStatus = "已完成";
                        break;
                    case "closed":
                        mStatus = "已取消";
                        break;
                    default:
                        mStatus = "已完成";
                        break;
                }
                bundle.putString(Count.CODE_STATUS, mStatus);

                startActivityEx(OrderDetailsActivity.class, bundle);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (mData.get(position).status) {
                    case "created":
                        switch (id) {
                            default:
                                break;
                            case R.id.btn_item_order_all_one:
                                break;
                            case R.id.btn_item_order_all_two:
                                YpDialog dialog = new YpDialog(getBaseActivity());
                                dialog.setTitle("您确定要取消订单吗?");
                                dialog.setNegativeButton("取消", true, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                                dialog.setPositiveButton("确定", true, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Map<String, String> map = new WeakHashMap<String, String>();
                                        map.put("orderSn", mData.get(position).sn);
                                        YpCache.setMap(map);
                                        mPresenter.closeOrder(map);
                                    }
                                });
                                dialog.show();
                                break;
                            case R.id.btn_item_order_all_three:
                                Bundle bundle = new Bundle();
                                bundle.putString(Count.ORDER_SN, mData.get(position).sn);
                                bundle.putString(Count.ORDER_PRICE, StringUtils.replaceChar(R.string.pay_all_money, (Float.valueOf(mData.get(position).pay_amount)) / 100f));
                                if (mData.size() > 1) {
                                    bundle.putString(Count.ORDER_NAME, mData.get(0).item.get(0).title
                                            + "等" + mData.size() + "件商品");
                                } else {
                                    bundle.putString(Count.ORDER_NAME, mData.get(0).item.get(0).title);
                                }
                                startActivityEx(PayWayActivity.class, bundle);
                                break;
                        }
                        break;
                    case "paid":
                        switch (id) {
                            default:
                                break;
                            case R.id.btn_item_order_all_one:
                                mPresenter.refundOrder(mData.get(position).sn, "", "", new String[]{});
                                break;

                            case R.id.btn_item_order_all_two:
                                break;
                            case R.id.btn_item_order_all_three:
                                break;
                        }
                        break;
                    case "refunding":
                        switch (id) {
                            default:
                                break;
                            case R.id.btn_item_order_all_one:
                                break;
                            case R.id.btn_item_order_all_two:
                                break;
                            case R.id.btn_item_order_all_three:
                                break;
                        }
                        break;
                    case "refunded":
                        switch (id) {
                            default:
                                break;
                            case R.id.btn_item_order_all_one:
                                break;
                            case R.id.btn_item_order_all_two:
                                break;
                            case R.id.btn_item_order_all_three:
                                break;
                        }
                        break;
                    case "success":
                        switch (id) {
                            default:
                                break;
                            case R.id.btn_item_order_all_one:
                                Bundle bundle = new Bundle();
                                bundle.putString(Count.AFTER_SALE_SN, mData.get(position).sn);
                                bundle.putString(Count.AFTER_SALE_MONEY, StringUtils.replaceChar(R.string.pay_all_money, (Float.valueOf(mData.get(position).pay_amount)) / 100f));
                                startActivityEx(ApplyAfterSalesActivity.class, bundle);
                                break;
                            case R.id.btn_item_order_all_two:
                                break;
                            case R.id.btn_item_order_all_three:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 标签数据初始化
     */
    private void initTabs(final List<String> data) {
        if (null != data) {
            for (String bean : data) {
                mTlTab.addTab(mTlTab.newTab().setText(bean));
            }
            mTlTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tabPosition = tab.getPosition();
                    mData.clear();
                    start = 0;
                    mSrRefresh.resetNoMoreData();
                    Map<String, String> map = new WeakHashMap<String, String>();
                    switch (tabPosition) {
                        case 0:
                            tabStatus = "";
                            break;
                        case 1:
                            tabStatus = "created";
                            break;
                        case 2:
                            tabStatus = "paid";
                            break;
                        case 3:
                            tabStatus = "success";
                            break;
                        case 4:
                            tabStatus = "refunded";
                            break;

                        default:
                            break;
                    }
                    map.put("status", tabStatus);
                    map.put("start", String.valueOf(start));
                    YpCache.setMap(map);
                    mPresenter.getMyOrder(map);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

        }
    }


    @Override
    public void onGetMyOrder(boolean isSuccess, MyOrderBean data) {
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
            mAdapter.setEmptyView(mEmptyView);
        }
    }

    @Override
    public void onCloseOrder(boolean isSuccess, BaseModuleApiResult data) {
        if (isSuccess) {
            mData.remove(pos);
            mAdapter.setNewData(mData);
            setEmptyView();
        }

    }

    @Override
    public void onRefundOrder(boolean isSuccess, BaseModuleApiResult data) {
        if(isSuccess) {
            mData.remove(pos);
            mAdapter.setNewData(mData);
            YpDialog dialog = new YpDialog(getBaseActivity());
            dialog.setTitle("订单已取消");
            dialog.setMessage("退款金额将在2-5个工作日返回您的账号!");
            dialog.setNegativeButton("取消", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            dialog.setPositiveButton("确定", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            dialog.show();
        }
        setEmptyView();
        EventBus.getDefault().post(new OrderEvent());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeItem(ItemEvent event) {
        mData.remove(pos);
        mAdapter.setNewData(mData);
        setEmptyView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
