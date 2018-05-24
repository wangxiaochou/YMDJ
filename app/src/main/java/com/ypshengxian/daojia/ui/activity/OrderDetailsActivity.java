package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.ItemEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IOrderDetailsContract;
import com.ypshengxian.daojia.mvp.presenter.OrderDetailsPresenter;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.LineItemBean;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.StringUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.RecyclerDivider;
import com.ypshengxian.daojia.view.YpDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 订单详情
 */
public class OrderDetailsActivity extends BaseMVPYpFreshActicity<IOrderDetailsContract.View, OrderDetailsPresenter> implements IOrderDetailsContract.View, View.OnClickListener {


    /** 数据展示 */
    private RecyclerView mRvDetails;
    /** 提交订单 */
    private Button mBtnAdd;
    private Button mBtnPut;
    /** 申请配送 */
    private Button mBtnDelivery;
    /** 申请退款 */
    private Button mBtnRetreat;
    private RelativeLayout mRlGoodsLayout;
    /** 删除订单 */
    private Button mBtnDelete;
    /** 实际取货时间:17-12-12 14:23:12 */
    private TextView mBtnTime;
    /** 取消订单布局 */
    private RelativeLayout mRlDeleteLayout;
    /** 头布局 */
    private View mHeaderView;
    /**
     * 店铺名称
     */
    private TextView mShopName;
    /**
     * 详细地址
     */
    private TextView mShopAddress;
    /**
     * 联系方向
     */
    private TextView mShopPhone;
    /**
     * 购物袋
     */
    private TextView mShopBag;
    /**
     * 购物券
     */
    private TextView mShopPass;
    /**
     * 日期
     */
    private TextView mShopDay;
    /**
     * 时间
     */
    private TextView mShopTime;
    /** 分享 */
    private LinearLayout mLlShare;
    /** 订单编号布局 */
    private RelativeLayout mRlCodeLayout;
    /** 待取货 */
    private TextView mTvGet;
    /** 订单编号 */
    private TextView mTvNumber;
    /** 订单编号时间 */
    private TextView mTvNumberTime;
    /**
     * 底布局
     */
    private View mFooterView;
    /**
     * 商品金额
     */
    private TextView mShopMoney;
    /**
     * 劵
     */
    private TextView mPassMoney;
    /**
     * 袋
     */
    private TextView mBagMoney;
    /**
     * 所有金额
     */
    private TextView mAllMoney;
    /** 订单编号 */
    private String mSn;
    /** 状态码 */
    private String mStatus;
    /** 适配器 */
    private BaseQuickAdapter<LineItemBean.ItemsBean, BaseViewHolder> mAdapter;
    /** 数据源 */
    private List<LineItemBean.ItemsBean> mData = new ArrayList<LineItemBean.ItemsBean>();
    /** 总金额 */
    private String mMoney="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSn = bundle.getString(Count.CODE_SN);
            mStatus = bundle.getString(Count.CODE_STATUS);
        }
        initView();
        LogUtils.e(mSn);
        Map<String, String> map = new WeakHashMap<String, String>();
        map.put("sn", mSn);
        YpCache.setMap(map);
        mPresenter.requestData(map);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_order_details;
    }

    @Override
    protected OrderDetailsPresenter createPresenter() {
        return new OrderDetailsPresenter();
    }

    /**
     * 初始化视图
     */
    private void initView() {

        TitleUtils.setTitleBar(this, "订单详情");
        //头部
        mHeaderView = getLayoutInflater().inflate(R.layout.layout_order_details_top, null, false);
        mShopName = mHeaderView.findViewById(R.id.tv_layout_order_details_top_name);
        mShopAddress = mHeaderView.findViewById(R.id.tv_layout_order_details_top_address);
        mShopPhone = mHeaderView.findViewById(R.id.tv_layout_order_details_top_phone);
        mShopBag = mHeaderView.findViewById(R.id.tv_layout_order_details_top_content_bag);
        mShopPass = mHeaderView.findViewById(R.id.tv_layout_order_details_top_content_coupon);
        mShopDay = mHeaderView.findViewById(R.id.tv_layout_order_details_top_content_date);
        mShopTime = mHeaderView.findViewById(R.id.tv_layout_order_details_top_content_time);
        mLlShare = mHeaderView.findViewById(R.id.ll_layout_order_details_top_share);
        mRlCodeLayout = mHeaderView.findViewById(R.id.rl_layout_order_details_top_code_layout);
        mTvGet = mHeaderView.findViewById(R.id.tv_layout_order_details_top_wait_pick);
        mTvNumber = mHeaderView.findViewById(R.id.tv_layout_order_details_top_order_num);
        mTvNumberTime = mHeaderView.findViewById(R.id.tv_layout_order_details_top_order_time);
        //底部
        mFooterView = getLayoutInflater().inflate(R.layout.layout_order_details_bottom, null, false);
        mShopMoney = mFooterView.findViewById(R.id.tv_layout_order_details_bottom_goods_money);
        mPassMoney = mFooterView.findViewById(R.id.tv_layout_order_details_bottom_coupon);
        mBagMoney = mFooterView.findViewById(R.id.tv_layout_order_details_bottom_bag);
        mAllMoney = mFooterView.findViewById(R.id.tv_layout_order_details_bottom_all_money);

        mRvDetails = (RecyclerView) findViewById(R.id.rv_activity_order_details);
        mBtnAdd = (Button) findViewById(R.id.btn_activity_order_details_add);
        mBtnAdd.setOnClickListener(this);
        mBtnAdd.setVisibility(View.GONE);
        mBtnPut = (Button) findViewById(R.id.btn_activity_order_details_put);
        mBtnPut.setOnClickListener(this);
        mBtnDelivery = (Button) findViewById(R.id.btn_activity_order_details_delivery);
        mBtnDelivery.setOnClickListener(this);
        mBtnRetreat = (Button) findViewById(R.id.btn_activity_order_details_retreat);
        mBtnRetreat.setOnClickListener(this);
        mRlGoodsLayout = (RelativeLayout) findViewById(R.id.rl_activity_order_details_goods_layout);
        mRlGoodsLayout.setOnClickListener(this);
        mBtnDelete = (Button) findViewById(R.id.btn_activity_order_details_delete);
        mBtnDelete.setOnClickListener(this);
        mBtnTime = (TextView) findViewById(R.id.btn_activity_order_details_time);
        mBtnTime.setOnClickListener(this);
        mRlDeleteLayout = (RelativeLayout) findViewById(R.id.rl_activity_order_details_delete_layout);
        mRlDeleteLayout.setOnClickListener(this);
        mLlShare.setVisibility(View.GONE);
        mRlCodeLayout.setVisibility(View.VISIBLE);
        mRvDetails.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new BaseQuickAdapter<LineItemBean.ItemsBean, BaseViewHolder>(R.layout.item_order_details_rv, mData) {
            @Override
            protected void convert(BaseViewHolder helper, LineItemBean.ItemsBean item) {
                helper.setText(R.id.tv_item_order_details_rv_goods_name, item.title);
                ImageView view = (ImageView) helper.getView(R.id.iv_item_order_details_goods_img);
                LoaderFactory.getLoader().loadNet(view, item.img);
                helper.setText(R.id.tv_item_order_details_rv_goods_new,
                        String.format(ResUtils.getString(R.string.cart_now_money),item.price));
                helper.setText(R.id.tv_item_order_details_rv_goods_old,
                        String.format(ResUtils.getString(R.string.cart_text_new_price), item.prePrice,item.unit.replace("g","斤")));
                helper.setText(R.id.tv_item_order_details_rv_goods_num,
                        String.format(ResUtils.getString(R.string.pay_order_num), item.num));

            }
        };

        mRvDetails.setAdapter(mAdapter);
        mRvDetails.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mAdapter.setHeaderView(mHeaderView);
        mAdapter.setFooterView(mFooterView);


        //状态
        switch (mStatus) {
            case "未付款":
                mRlGoodsLayout.setVisibility(View.VISIBLE);
                mBtnPut.setVisibility(View.VISIBLE);
                mBtnPut.setText("付款");
                mBtnDelivery.setVisibility(View.VISIBLE);
                mBtnDelivery.setText("取消订单");
                mBtnRetreat.setVisibility(View.GONE);
                mRlDeleteLayout.setVisibility(View.GONE);
                break;
            case "待自提":
                mRlGoodsLayout.setVisibility(View.VISIBLE);
                mRlDeleteLayout.setVisibility(View.VISIBLE);
                mBtnPut.setVisibility(View.VISIBLE);
                mBtnPut.setText("取货");
                mBtnDelivery.setVisibility(View.VISIBLE);
                mBtnDelivery.setText("申请配送");
                mBtnRetreat.setVisibility(View.VISIBLE);
                mBtnRetreat.setText("申请退款");
                break;
            case "已退款":
                mRlGoodsLayout.setVisibility(View.GONE);
                mRlDeleteLayout.setVisibility(View.GONE);
                mBtnDelete.setVisibility(View.VISIBLE);
                mBtnDelivery.setVisibility(View.GONE);
                break;
            case "已完成":
                mRlGoodsLayout.setVisibility(View.VISIBLE);
                mRlDeleteLayout.setVisibility(View.GONE);
                mBtnPut.setVisibility(View.INVISIBLE);
                mBtnDelivery.setVisibility(View.VISIBLE);
                mBtnDelivery.setText("删除订单");
                mBtnRetreat.setVisibility(View.VISIBLE);
                mBtnRetreat.setText("售后申请");
                break;
            case "退款中":
                mRlGoodsLayout.setVisibility(View.GONE);
                mRlDeleteLayout.setVisibility(View.GONE);
                break;
            case "审核中":
                mRlGoodsLayout.setVisibility(View.GONE);
                mRlDeleteLayout.setVisibility(View.GONE);
                break;
            case "拒绝退款":
                mRlGoodsLayout.setVisibility(View.GONE);
                mRlDeleteLayout.setVisibility(View.GONE);
                break;
            case "退款成功":
                mRlGoodsLayout.setVisibility(View.GONE);
                mRlDeleteLayout.setVisibility(View.GONE);
                break;
            case "已取消":
                mRlGoodsLayout.setVisibility(View.GONE);
                mRlDeleteLayout.setVisibility(View.GONE);
                break;
            default:
                mRlGoodsLayout.setVisibility(View.GONE);
                mRlDeleteLayout.setVisibility(View.GONE);
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_activity_order_details_add:
                break;
            case R.id.btn_activity_order_details_put:
                //状态  取货 收货 付款
                switch (mStatus) {
                    case "未付款":
                        Bundle bundle = new Bundle();
                        bundle.putString(Count.ORDER_SN, mSn);
                        bundle.putString(Count.ORDER_PRICE, StringUtils.replaceChar(R.string.pay_all_money,(Float.valueOf(mMoney))/100f));
                        if (mData.size() > 1) {
                            bundle.putString(Count.ORDER_NAME, mData.get(0).title
                                    + "等" + mData.size() + "件商品");
                        } else {
                            bundle.putString(Count.ORDER_NAME, mData.get(0).title);
                        }
                     startActivityEx(PayWayActivity.class,bundle);
                        break;
                    case "待自提":

                        break;
                    case "已退款":

                        break;
                    case "已完成":

                        break;
                    case "退款中":

                        break;
                    case "审核中":

                        break;
                    case "拒绝退款":

                        break;
                    case "退款成功":

                        break;
                    case "已取消":

                        break;
                    default:

                        break;
                }
                break;
            case R.id.btn_activity_order_details_delivery:
                //状态
                switch (mStatus) {
                    case "未付款":
                        YpDialog dialog=new YpDialog(getBaseActivity());
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
                                map.put("orderSn", mSn);
                                YpCache.setMap(map);
                                mPresenter.closeOrder(map);
                            }
                        });
                        dialog.show();

                        break;
                    case "待自提":

                        break;
                    case "已退款":

                        break;
                    case "已完成":

                        break;
                    case "退款中":

                        break;
                    case "审核中":

                        break;
                    case "拒绝退款":

                        break;
                    case "退款成功":

                        break;
                    case "已取消":

                        break;
                    default:

                        break;
                }
                break;
            case R.id.btn_activity_order_details_retreat:
                //状态  申请退款  申请售后
                switch (mStatus) {
                    case "未付款":

                        break;
                    case "待自提":
                        mPresenter.refundOrder(mSn, "", "", new String[]{});
                        break;
                    case "已退款":

                        break;
                    case "已完成":
                        Bundle bundle = new Bundle();
                        bundle.putString(Count.AFTER_SALE_SN, mSn);
                        bundle.putString(Count.AFTER_SALE_MONEY, StringUtils.replaceChar(R.string.pay_all_money,(Float.valueOf(mMoney))/100f));
                        startActivityEx(ApplyAfterSalesActivity.class, bundle);
                        break;
                    case "退款中":

                        break;
                    case "审核中":

                        break;
                    case "拒绝退款":

                        break;
                    case "退款成功":

                        break;
                    case "已取消":

                        break;
                    default:

                        break;
                }
                break;
            case R.id.rl_activity_order_details_goods_layout:
                //状态
                switch (mStatus) {
                    case "未付款":

                        break;
                    case "待自提":

                        break;
                    case "已退款":

                        break;
                    case "已完成":

                        break;
                    case "退款中":

                        break;
                    case "审核中":

                        break;
                    case "拒绝退款":

                        break;
                    case "退款成功":

                        break;
                    case "已取消":

                        break;
                    default:

                        break;
                }
                break;
            case R.id.btn_activity_order_details_delete:
                //状态
                switch (mStatus) {
                    case "未付款":

                        break;
                    case "待自提":

                        break;
                    case "已退款":

                        break;
                    case "已完成":

                        break;
                    case "退款中":

                        break;
                    case "审核中":

                        break;
                    case "拒绝退款":

                        break;
                    case "退款成功":

                        break;
                    case "已取消":

                        break;
                    default:

                        break;
                }
                break;
            case R.id.btn_activity_order_details_time:
                //状态
                switch (mStatus) {
                    case "未付款":

                        break;
                    case "待自提":

                        break;
                    case "已退款":

                        break;
                    case "已完成":

                        break;
                    case "退款中":

                        break;
                    case "审核中":

                        break;
                    case "拒绝退款":

                        break;
                    case "退款成功":

                        break;
                    case "已取消":

                        break;
                    default:

                        break;
                }
                break;
            case R.id.rl_activity_order_details_delete_layout:
                //状态
                switch (mStatus) {
                    case "未付款":

                        break;
                    case "待自提":

                        break;
                    case "已退款":

                        break;
                    case "已完成":

                        break;
                    case "退款中":

                        break;
                    case "审核中":

                        break;
                    case "拒绝退款":

                        break;
                    case "退款成功":

                        break;
                    case "已取消":

                        break;
                    default:

                        break;
                }
                break;

        }
    }

    @Override
    public void onResponseData(boolean isSuccess, LineItemBean data) {
        if (isSuccess) {
            LogUtils.e(data.createTime);
            mMoney=data.priceAmount;
            mData = data.items;
            mAdapter.setNewData(mData);
            initData(data);
        }
        mData = data.items;

    }


    /**
     * 设置数据
     *
     * @param data 数据
     */
    private void initData(LineItemBean data) {
        LineItemBean.ShopBean shopBean = data.shop;
        mShopName.setText(String.format(ResUtils.getString(R.string.pay_order_name), shopBean.title));
        mShopAddress.setText(shopBean.address);
        mShopPhone.setText(String.format(ResUtils.getString(R.string.pay_order_phone), shopBean.phone));
        mTvGet.setText(mStatus);
        mTvNumber.setText(String.format(ResUtils.getString(R.string.pay_order_sn), data.sn));
        mTvNumberTime.setText(String.format(ResUtils.getString(R.string.pay_order_time), data.createTime));
        LineItemBean.PacketBean packetBean = data.packet;
        if (null != packetBean) {
            mShopBag.setText(packetBean.name);
            mBagMoney.setText(String.format(ResUtils.getString(R.string.pay_bag_money_two), (Float.valueOf(packetBean.price))/100f));
        } else {
            mShopBag.setText("未选择购物袋");
        }

        mShopPass.setText("未选择优惠卷");
        if (TextUtils.isEmpty(data.deliveryDay)) {
            mShopDay.setText("未选择收货日期");
        } else {
            mShopDay.setText(data.deliveryDay);
        }
        if (TextUtils.isEmpty(data.deliveryTime)) {
            mShopTime.setText("未选择收货时间");
        } else {
            mShopTime.setText(data.deliveryTime);
        }

        mShopMoney.setText(String.format(ResUtils.getString(R.string.cart_all_money), (Float.valueOf(data.priceAmount))/100f));
        initAllMoney((Float.valueOf(data.priceAmount))/100f);
    }

    /**
     * 设置总金额
     */
    private void initAllMoney(float money) {
        mAllMoney.setText(String.format(ResUtils.getString(R.string.cart_all_money), money));
    }

    @Override
    public void onCloseOrder(boolean isSuccess, BaseModuleApiResult data) {
        if (isSuccess){
            finish();
        EventBus.getDefault().post(new ItemEvent());
        }
    }

    @Override
    public void onRefundOrder(boolean isSuccess, BaseModuleApiResult data) {
        if (isSuccess){
            YpDialog dialog = new YpDialog(getBaseActivity());
            dialog.setTitle("订单已取消");
            dialog.setMessage("退款金额将在2-5个工作日返回您的账号!");
            dialog.setNegativeButton("取消", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            dialog.setPositiveButton("确定", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            dialog.show();
        }
    }
}
