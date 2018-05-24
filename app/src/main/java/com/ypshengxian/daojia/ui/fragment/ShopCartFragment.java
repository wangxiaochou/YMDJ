package com.ypshengxian.daojia.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.JumpEvent;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.ICartContract;
import com.ypshengxian.daojia.mvp.presenter.CartPresenter;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.ui.activity.PayOrderActivity;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.utils.ViewUtils;
import com.ypshengxian.daojia.view.EmptyView;
import com.ypshengxian.daojia.view.MyCountView;
import com.ypshengxian.daojia.view.RecyclerDivider;
import com.ypshengxian.daojia.view.SimpleTitleBarBuilder;
import com.ypshengxian.daojia.view.YpDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 购物车
 *
 * @author Yan
 * @date 2018-04-09
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ShopCartFragment extends BaseMVPYpFreshFragment<ICartContract.View, CartPresenter> implements
        ICartContract.View, View.OnClickListener {

    /** 数据展示 */
    private RecyclerView mRlCount;
    /** 1231321 */
    private TextView mTvBottomAllMoney;
    /** 去结算 */
    private Button mBtnBottomGo;
    /** 底部box容器 */
    private LinearLayout mLlCheckLayout;
    /** 全选box */
    private CheckBox mCbCheck;
    /** 数据源 */
    private List<AddCartBean.GoodsBean> mData = new ArrayList<AddCartBean.GoodsBean>();
    /** 适配器 */
    private BaseQuickAdapter<AddCartBean.GoodsBean, BaseViewHolder> mAdapter;
    /** 所有的钱 */
    private float mAllMoney;
    /** 点击删除的时候保存位置 */
    private int mPosition = 0;
    /** 空布局具体 */
    private EmptyView mEvView;
    /** 是否全选中 */
    private boolean isSelected;
    /** 底部布局 */
    private LinearLayout mLlBottomLayout;

    /**
     * 单例
     *
     * @return this
     */
    public static ShopCartFragment newInstance() {

        Bundle args = new Bundle();

        ShopCartFragment fragment = new ShopCartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void loadData(boolean firstLoad) {
        super.loadData(firstLoad);
        mPresenter.requestData();
    }

    @Override
    protected CartPresenter createPresenter() {
        return new CartPresenter();
    }

    @Override
    public void onResponseData(boolean isSuccess, AddCartBean data) {
        if (isSuccess) {
            this.mAllMoney = (float) data.total_price;
            setMoney();
            mData = data.goods;
            mAdapter.setNewData(mData);

        }
        mCbCheck.setChecked(isAllChecked());
        setEmptyView();
    }

    /**
     * 设置空布局
     */
    private void setEmptyView() {
        int size = mData.size();
        if (0 == size) {
            mAdapter.setEmptyView(mEvView);
            setButtonCannotClick();
        } else {
            setButtonCanClick();
        }
    }

    /**
     * 全选是否选中
     *
     * @return 是否
     */
    private boolean isAllChecked() {
        for (AddCartBean.GoodsBean bean : mData) {
            if (bean.selected == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAllMoney = 0.0f;
    }

    /**
     * 没有选中checkBox的时候删除按钮是不能被点击的
     */
    private void setButtonCannotClick() {
        mLlBottomLayout.setVisibility(View.GONE);
        mBtnBottomGo.setBackgroundResource(R.color.color_background);
        mBtnBottomGo.setEnabled(false);
    }

    /**
     * 选中checkBox的时候删除按钮是可以点击的
     */
    private void setButtonCanClick() {
        mLlBottomLayout.setVisibility(View.VISIBLE);
        mBtnBottomGo.setBackgroundResource(R.color.color_theme);
        mBtnBottomGo.setEnabled(true);
    }

    /**
     * 设置总的money
     */
    private void setMoney() {
        mTvBottomAllMoney.setText(String.format(ResUtils.getString(R.string.cart_all_money), mAllMoney));
    }

    @Override
    public void onDeletedCart(boolean isSuccess, AddCartBean data) {
        if (isSuccess) {
            this.mAllMoney = (float) data.total_price;
            setMoney();
            mData = data.goods;
            mAdapter.setNewData(mData);
            EventBus.getDefault().post(new OrderEvent());
        }
        setEmptyView();
    }

    @Override
    public void onAddCart(boolean isSuccess, AddCartBean data) {
        if (isSuccess) {
            this.mAllMoney = (float) data.total_price;
            setMoney();
            mData = data.goods;
            mAdapter.setNewData(mData);
            EventBus.getDefault().post(new OrderEvent());
        }
        setEmptyView();
    }

    @Override
    public void onUpdateCart(boolean isSuccess, AddCartBean data) {
        if (isSuccess) {
            this.mAllMoney = (float) data.total_price;
            setMoney();
            mData = data.goods;
            mAdapter.setNewData(mData);
        }
        setEmptyView();
        mCbCheck.setChecked(isAllChecked());
    }

    @Override
    public void onBatchUpdateCart(boolean isSuccess, AddCartBean data) {
        if (isSuccess) {
            this.mAllMoney = (float) data.total_price;
            setMoney();
            mData = data.goods;
            mAdapter.setNewData(mData);
        }
        setEmptyView();
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_cart;
    }

    @Override
    public void initView() {
        SimpleTitleBarBuilder.attach(getBaseActivity(), getView())
                .setLeftVisible(true)
                .setStatusBarColorAlpha(ResUtils.getColor(R.color.color_theme), 0)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setTitleText("购物车")
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .setTitleSize(18)
                .setShadowHeight(0);

        mRlCount = (RecyclerView) findViewById(R.id.rl_fragment_cart_count);

        //空布局
        mEvView = new EmptyView(Utils.getContext());
        mEvView.setMessage(R.string.msg_cart_empty)
                .setBtnText(R.string.text_go_home, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new JumpEvent(Count.JUMP_MAIN));
                    }
                });
        mTvBottomAllMoney = (TextView) findViewById(R.id.tv_fragment_cart_bottom_all_money);
        mBtnBottomGo = (Button) findViewById(R.id.btn_fragment_cart_bottom_go);
        mBtnBottomGo.setOnClickListener(this);

        mLlBottomLayout = (LinearLayout) findViewById(R.id.ll_fragment_cart_bottom_layout);
        //全选容器
        mLlCheckLayout = (LinearLayout) findViewById(R.id.ll_fragment_cart_bottom_all_check_layout);
        mLlCheckLayout.setOnClickListener(this);
        //全选
        mCbCheck = (CheckBox) findViewById(R.id.cb_fragment_cart_bottom_all_check);
        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new BaseQuickAdapter<AddCartBean.GoodsBean, BaseViewHolder>(R.layout.item_fragment_cart, mData) {
            @Override
            protected void convert(BaseViewHolder helper, AddCartBean.GoodsBean item) {
                int position = helper.getLayoutPosition();
                CheckBox box = (CheckBox) helper.getView(R.id.cb_item_fragment_cart_select);
                int selected = mData.get(position).selected;
                switch (selected) {
                    case 0:
                        box.setChecked(false);
                        break;
                    case 1:
                        box.setChecked(true);
                        break;
                    default:
                        box.setChecked(true);
                        break;
                }
                helper.getView(R.id.rl_item_fragment_cart_select_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> map = new WeakHashMap<String, String>();
                        if (box.isChecked()) {
                            map.put("goodsId", mData.get(position).goods_id);
                            map.put("selected", "0");
                            box.setChecked(false);

                        } else {
                            map.put("goodsId", mData.get(position).goods_id);
                            map.put("selected", "1");
                            box.setChecked(true);
                        }
                        YpCache.setMap(map);
                        mPresenter.updateCart(map);
                    }
                });

                helper.setText(R.id.tv_item_fragment_cart_name, mData.get(position).goods_name);
                ImageView view = (ImageView) helper.getView(R.id.iv_item_fragment_cart_image);
                LoaderFactory.getLoader().loadNet(view, mData.get(position).goods_img);
                helper.setText(R.id.tv_item_fragment_cart_money,
                        String.format(ResUtils.getString(R.string.cart_now_money), mData.get(position).goods_price));
                helper.setText(R.id.tv_item_fragment_cart_discount,
                        String.format(ResUtils.getString(R.string.cart_text_new_price), mData.get(position).goods_pre_price, mData.get(position).goods_unit.replace("g", "斤")));
                MyCountView myCountView = (MyCountView) helper.getView(R.id.mv_item_fragment_cart_num);
                myCountView.setNum(Integer.valueOf(mData.get(position).goods_num));
                myCountView.setMListener(new MyCountView.OnNumChangedListener() {

                    @Override
                    public void onAdd(int num) {
                        Map<String, String> addMap = new WeakHashMap<String, String>();
                        addMap.put("goodsId", mData.get(position).goods_id);
                        addMap.put("num", "1");
                        YpCache.setMap(addMap);
                        mPresenter.addCart(addMap);
                    }

                    @Override
                    public void onMinus(int num) {
                        Map<String, String> minuMap = new WeakHashMap<String, String>();
                        minuMap.put("goodsId", mData.get(position).goods_id);
                        minuMap.put("num", "1");
                        YpCache.setMap(minuMap);
                        mPresenter.deletedCart(minuMap);
                    }
                });

                ViewUtils.setTouchDelegate(helper.getView(R.id.iv_item_fragment_cart_delete), 50);
                helper.addOnClickListener(R.id.iv_item_fragment_cart_delete);
            }
        };

        mRlCount.setAdapter(mAdapter);
        mRlCount.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (id) {
                    case R.id.iv_item_fragment_cart_delete:
                        YpDialog dialog = new YpDialog(getBaseActivity());
                        dialog.setTitle("确定要删除该商品么");
                        dialog.setNegativeButton("取消", true, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        dialog.setPositiveButton("确定", true, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String, String> map = new WeakHashMap<String, String>();
                                map.put("goodsId", mData.get(position).goods_id);
                                map.put("num", mData.get(position).goods_num);
                                YpCache.setMap(map);
                                mPresenter.deletedCart(map);
                            }
                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_fragment_cart_bottom_all_check_layout:
                Map<String, String> map = new WeakHashMap<String, String>();
                if (mCbCheck.isChecked()) {
                    map.put("selected", "0");
                    mCbCheck.setChecked(false);
                } else {
                    map.put("selected", "1");
                    mCbCheck.setChecked(true);
                }
                YpCache.setMap(map);
                mPresenter.batchUpdateCart(map);
                break;
            case R.id.btn_fragment_cart_bottom_go:
                //去结算的时候判断是否有选中的商品
                if (isCartChecked()) {
                    if(!TextUtils.isEmpty(YPPreference.newInstance().getShopId())) {
                        startActivityEx(PayOrderActivity.class);
                    }else {
                        ToastUtils.showShortToast("请先选择店铺");
                    }
                } else {
                    ToastUtils.showShortToast("请先勾选商品");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断是否有商品选中
     *
     * @return 是否
     */
    public boolean isCartChecked() {
        for (AddCartBean.GoodsBean bean : mData) {
            if (bean.selected == 1) {
                return true;
            }
        }
        return false;
    }
}
