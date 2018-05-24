package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.annotation.ActivityOption;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.event.PayOrderEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IPayOrderContract;
import com.ypshengxian.daojia.mvp.presenter.PayOrderPresenter;
import com.ypshengxian.daojia.network.bean.CreateOrderBean;
import com.ypshengxian.daojia.network.bean.PayOrderBean;
import com.ypshengxian.daojia.pop.BagPopWindow;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TimeUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.RecyclerDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
@ActivityOption(reqLogin = true)
public class PayOrderActivity extends BaseMVPYpFreshActicity<IPayOrderContract.View, PayOrderPresenter> implements
        IPayOrderContract.View, View.OnClickListener {

    /**
     * 内容展示
     */
    private RecyclerView mRvDetails;
    /**
     * 提交订单
     */
    private Button mBtnAdd;
    /**
     * 头布局
     */
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
    /**
     * 数据源
     */
    private List<PayOrderBean.OrderInfoBean.GoodsBean> mData = new ArrayList<PayOrderBean.OrderInfoBean.GoodsBean>();
    /**
     * 适配器
     */
    private BaseQuickAdapter<PayOrderBean.OrderInfoBean.GoodsBean, BaseViewHolder> mAdapter;
    /**
     * 弹窗数据
     */
    private List<PayOrderBean.PacketsBean> packetsBeans = new ArrayList<PayOrderBean.PacketsBean>();
    /**
     * 最终成交价
     */
    private float mEndMoney;
    /**
     * 购物袋ID
     */
    private String mBagId;
    /**
     * 劵ID
     */
    private String mPassId = "";
    /**
     * 送货时间
     */
    private String mTime = "07:30-19:30";
    /** 购物袋的钱 */
    private float mBMoney;
    /** 头部地址容器 */
    private LinearLayout mLlAddressLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        Map<String, String> map = new WeakHashMap<String, String>();
        map.put("shopId", YPPreference.newInstance().getShopId());
        YpCache.setMap(map);
        mPresenter.requestData(map);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResponseData(boolean isSuccess, PayOrderBean data) {
        if (isSuccess) {
            mEndMoney = data.orderInfo.total_price;
            packetsBeans = data.packets;
            mData = data.orderInfo.goods;
            mAdapter.setNewData(mData);
            initData(data);
        } else {
            ToastUtils.showShortToast(data.error.message);
        }
    }

    @Override
    public void onCreateOrder(boolean isSuccess, CreateOrderBean data) {
        if (isSuccess) {
            EventBus.getDefault().post(new OrderEvent());
            LogUtils.e(data.sn);
            if (!data.sn.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString(Count.ORDER_SN, data.sn);
                bundle.putString(Count.ORDER_PRICE, String.format(ResUtils.getString(R.string.pay_all_money), mEndMoney + mBMoney));
                if (mData.size() > 1) {
                    bundle.putString(Count.ORDER_NAME, mData.get(0).goods_name
                            + "等" + mData.size() + "件商品");
                } else {
                    bundle.putString(Count.ORDER_NAME, mData.get(0).goods_name);
                }
                startActivityEx(PayWayActivity.class, bundle);
                finish();
            } else {
                ToastUtils.showShortToast("订单创建失败！");
            }
        }
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    private void initData(PayOrderBean data) {
        PayOrderBean.ShopBean shopBean = data.shop;
        mShopName.setText(String.format(ResUtils.getString(R.string.pay_order_name), shopBean.title));
        mShopAddress.setText(shopBean.fullAddress);
        mShopPhone.setText(String.format(ResUtils.getString(R.string.pay_order_phone), shopBean.phone));
        mShopMoney.setText(String.format(ResUtils.getString(R.string.cart_all_money), data.orderInfo.total_price));
        mShopDay.setText(data.deliveryDay);
        initAllMoney(data.orderInfo.total_price);

    }

    /**
     * 设置总金额
     */
    private void initAllMoney(float money) {
        mAllMoney.setText(String.format(ResUtils.getString(R.string.cart_all_money), money));
    }

    @Override
    protected PayOrderPresenter createPresenter() {
        return new PayOrderPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_order_details;
    }

    private void initView() {
        TitleUtils.setTitleBar(this, "确认订单");
        //头部
        mHeaderView = getLayoutInflater().inflate(R.layout.layout_order_details_top, null, false);
        mShopName = mHeaderView.findViewById(R.id.tv_layout_order_details_top_name);
        mShopAddress = mHeaderView.findViewById(R.id.tv_layout_order_details_top_address);
        mShopPhone = mHeaderView.findViewById(R.id.tv_layout_order_details_top_phone);
        mShopBag = mHeaderView.findViewById(R.id.tv_layout_order_details_top_content_bag);
        mShopPass = mHeaderView.findViewById(R.id.tv_layout_order_details_top_content_coupon);
        mShopDay = mHeaderView.findViewById(R.id.tv_layout_order_details_top_content_date);
        mShopTime = mHeaderView.findViewById(R.id.tv_layout_order_details_top_content_time);
        mLlAddressLayout = mHeaderView.findViewById(R.id.ll_layout_order_details_top_address_layout);
        mLlAddressLayout.setOnClickListener(this);
        mShopBag.setOnClickListener(this);
        mShopPass.setOnClickListener(this);
        mShopDay.setOnClickListener(this);
        mShopTime.setOnClickListener(this);
        //底部
        mFooterView = getLayoutInflater().inflate(R.layout.layout_order_details_bottom, null, false);
        mShopMoney = mFooterView.findViewById(R.id.tv_layout_order_details_bottom_goods_money);
        mPassMoney = mFooterView.findViewById(R.id.tv_layout_order_details_bottom_coupon);
        mBagMoney = mFooterView.findViewById(R.id.tv_layout_order_details_bottom_bag);
        mAllMoney = mFooterView.findViewById(R.id.tv_layout_order_details_bottom_all_money);

        mRvDetails = (RecyclerView) findViewById(R.id.rv_activity_order_details);
        mBtnAdd = (Button) findViewById(R.id.btn_activity_order_details_add);
        mBtnAdd.setOnClickListener(this);
        mRvDetails.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mRvDetails.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mAdapter = new BaseQuickAdapter<PayOrderBean.OrderInfoBean.GoodsBean, BaseViewHolder>(R.layout.item_order_details_rv, mData) {
            @Override
            protected void convert(BaseViewHolder helper, PayOrderBean.OrderInfoBean.GoodsBean item) {
                helper.setText(R.id.tv_item_order_details_rv_goods_name, item.goods_name);
                ImageView view = (ImageView) helper.getView(R.id.iv_item_order_details_goods_img);
                LoaderFactory.getLoader().loadNet(view, item.goods_img);
                helper.setText(R.id.tv_item_order_details_rv_goods_new,
                        String.format(ResUtils.getString(R.string.cart_now_money),item.goods_price));
                helper.setText(R.id.tv_item_order_details_rv_goods_old,
                        String.format(ResUtils.getString(R.string.cart_text_new_price), item.goods_pre_price,item.goods_unit.replace("g","斤")));
                helper.setText(R.id.tv_item_order_details_rv_goods_num,
                        String.format(ResUtils.getString(R.string.pay_order_num), item.goods_num));
            }
        };
        mRvDetails.setAdapter(mAdapter);
        mAdapter.addHeaderView(mHeaderView);
        mAdapter.addFooterView(mFooterView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_activity_order_details_add:
                //提交
                mPresenter.createOrder(YPPreference.newInstance().getShopId(), mPassId, mBagId, mTime);
                break;
            case R.id.tv_layout_order_details_top_content_bag:
                //购物袋
                BagPopWindow bagPopWindow = new BagPopWindow(this, packetsBeans);
                bagPopWindow.showAtLocation(mShopName, Gravity.BOTTOM, 0, 0);

                break;
            case R.id.tv_layout_order_details_top_content_coupon:
                //优惠券
                break;
            case R.id.tv_layout_order_details_top_content_date:
                //日期
               // initDatePick();
                break;
            case R.id.tv_layout_order_details_top_content_time:
                //时间
                initTimePick();
                break;
            case R.id.ll_layout_order_details_top_address_layout:
                //跳转地址
               startActivityEx(ChooseStoreActivity.class);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNeed(PayOrderEvent event) {
        mShopBag.setText(event.title);
        mBagId = event.id;
        mBMoney = Float.valueOf(event.money);
        mBagMoney.setText(String.format(ResUtils.getString(R.string.pay_bag_money), event.money));
        initAllMoney(mEndMoney + Float.valueOf(event.money));
    }

    /**
     * 选择日期
     */
    private void initDatePick() {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String mDate = TimeUtils.date2String(date, "yyyy.MM.dd");
                //天
                mShopDay.setText(mDate);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCyclic(true)
                .setSubmitColor(ResUtils.getColor(R.color.color_theme))
                .setCancelColor(ResUtils.getColor(R.color.color_theme))
                .build();
        pvTime.show();
    }

    /**
     * 选择日期
     */
    private void initTimePick() {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long mills = TimeUtils.date2Millis(date) + (1800 * 1000);
                mTime = TimeUtils.date2String(date, "HH:mm") + "-" + TimeUtils.millis2String(mills, "HH:mm");
                //天
                mShopTime.setText(mTime);
            }
        })
                .setType(new boolean[]{false, false, false, true, true, false})
                .isCyclic(true)
                .setTitleText("所选时间延后半小时")
                .setTitleSize(14)
                .setTitleColor(ResUtils.getColor(R.color.color_theme))
                .setSubmitColor(ResUtils.getColor(R.color.color_theme))
                .setCancelColor(ResUtils.getColor(R.color.color_theme))
                .build();
        pvTime.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
