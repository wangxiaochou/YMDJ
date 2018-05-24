package com.ypshengxian.daojia.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.ICartContract;
import com.ypshengxian.daojia.mvp.presenter.CartPresenter;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.ui.activity.PayOrderActivity;
import com.ypshengxian.daojia.utils.LogUtils;
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
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class CartFragment extends BaseMVPYpFreshFragment<ICartContract.View, CartPresenter> implements
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
    /** 空数据 */
    private View mEmptyLayout;
    /** 空布局具体 */
    private EmptyView mEvView;
    /** 是否全选中 */
    private boolean isSelected;

    /**
     * 单列
     *
     * @return this
     */
    public static CartFragment newInstance() {

        Bundle args = new Bundle();

        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_cart;
    }

    @Override
    public void loadData(boolean firstLoad) {
        super.loadData(firstLoad);
        mPresenter.requestData();
    }

    @Override
    public void initView() {
        SimpleTitleBarBuilder.attach(getBaseActivity(), getView())
                .setLeftVisible(true)
                .setStatusBarColorAlpha(ResUtils.getColor(R.color.color_theme),0)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setTitleText("购物车")
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .setTitleSize(18)
                .setShadowHeight(0);
        mRlCount = (RecyclerView) findViewById(R.id.rl_fragment_cart_count);
        //mEmptyLayout =LayoutInflater.from(Utils.getContext()).inflate(R.layout.empty_view, (ViewGroup) mRlCount.getParent(), false);
        mEvView=new EmptyView(getBaseActivity());
        mEvView.setMessage(R.string.msg_cart_empty);
        mEvView.setBtnText(R.string.text_go_home, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTvBottomAllMoney = (TextView) findViewById(R.id.tv_fragment_cart_bottom_all_money);
        mBtnBottomGo = (Button) findViewById(R.id.btn_fragment_cart_bottom_go);
        mBtnBottomGo.setOnClickListener(this);
        //全选容器
        mLlCheckLayout = (LinearLayout) findViewById(R.id.ll_fragment_cart_bottom_all_check_layout);

        //全选
        mCbCheck = (CheckBox) findViewById(R.id.cb_fragment_cart_bottom_all_check);
        mCbCheck.setOnClickListener(this);
        ViewUtils.setTouchDelegate(mCbCheck, 50);
        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new BaseQuickAdapter<AddCartBean.GoodsBean, BaseViewHolder>(R.layout.item_fragment_cart, mData) {
            @Override
            protected void convert(BaseViewHolder helper, AddCartBean.GoodsBean item) {
                int position = helper.getLayoutPosition();
                CheckBox box = (CheckBox) helper.getView(R.id.cb_item_fragment_cart_select);
                box.setChecked(mData.get(position).isSelected);
                helper.setText(R.id.tv_item_fragment_cart_name, mData.get(position).goods_name);
                ImageView view = (ImageView) helper.getView(R.id.iv_item_fragment_cart_image);
                LoaderFactory.getLoader().loadNet(view, mData.get(position).goods_img);
                helper.setText(R.id.tv_item_fragment_cart_money,
                        String.format(ResUtils.getString(R.string.cart_now_money), mData.get(position).goods_price));
                helper.setText(R.id.tv_item_fragment_cart_discount,
                        String.format(ResUtils.getString(R.string.cart_text_new_price), mData.get(position).goods_pre_price,mData.get(position).goods_unit.replace("g","斤")));
                MyCountView myCountView = (MyCountView) helper.getView(R.id.mv_item_fragment_cart_num);
                myCountView.setNum(Integer.valueOf(mData.get(position).goods_num));
                myCountView.setMListener(new MyCountView.OnNumChangedListener() {

                    @Override
                    public void onAdd(int num) {

                        //加
                        mData.get(position).goods_num = String.valueOf(num);
                        if (mData.get(position).isSelected) {
                            mAllMoney += Float.valueOf(mData.get(position).goods_price);
                            setMoney();
                        }
                    }

                    @Override
                    public void onMinus(int num) {
                        //减
                        mData.get(position).goods_num = String.valueOf(num);
                        if (mData.get(position).isSelected) {
                            mAllMoney -= Float.valueOf(mData.get(position).goods_price);
                            setMoney();
                        }
                    }
                });
                helper.addOnClickListener(R.id.iv_item_fragment_cart_delete);


                //扩大点击面积
                helper.getView(R.id.rl_item_fragment_cart_select_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!box.isChecked()) {
                            box.setChecked(true);
                        } else {
                            box.setChecked(false);
                        }
                    }
                });

                box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mAllMoney += Float.valueOf(mData.get(position).goods_price) * Float.valueOf(mData.get(position).goods_num);
                            setMoney();
                        } else {
                            mAllMoney -= Float.valueOf(mData.get(position).goods_price) * Float.valueOf(mData.get(position).goods_num);
                            setMoney();
                        }


                        int size = mData.size();
                        for (int i = 0; i < size; i++) {
                            if (position == i) {
                                if (isChecked) {
                                    //当选中的时候设置数据为true,结束本层循环
                                    mData.get(i).isSelected = true;
                                    break;
                                } else {
                                    mData.get(i).isSelected = false;
                                }
                            }
                        }
                        //设置全选的按钮是否显示
                        mCbCheck.setChecked(isAllChecked());

                        for (int i = 0; i < size; i++) {
                            if (mData.get(i).isSelected) {
                                //当数据里面的字段信息含有已经选中信息的时候,设置button可以点击,并结束整个循环
                                setButtonCanClick();
                                return;
                            } else {
                                setButtonCannotClick();
                            }
                        }


                    }
                });


            }
        };
        mRlCount.setAdapter(mAdapter);
        mRlCount.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CartFragment.this.mPosition = position;
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

    /**
     *
     * 全选是否选中
     * @return 是否
     */
    private boolean isAllChecked() {
        for (AddCartBean.GoodsBean bean : mData) {
            if (!bean.isSelected) {
                return false;
            }
        }
        return true;
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
            LogUtils.e(data.total_price);
            mData = data.goods;
            mAdapter.setNewData(mData);

        }
        setEmptyView();
    }

    /**
     * 设置空布局
     */
    private void setEmptyView() {
        int size = mData.size();
        if (0 == size) {
            mAdapter.setEmptyView(mEvView);
            mCbCheck.setChecked(false);
            setButtonCannotClick();
        }else {
            mCbCheck.setChecked(true);
            setButtonCanClick();
        }
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
            ToastUtils.showShortToast("删除商品成功");
        }
        setEmptyView();
    }

    @Override
    public void onAddCart(boolean isSuccess, AddCartBean data) {

    }

    @Override
    public void onUpdateCart(boolean isSuccess, AddCartBean data) {

    }

    @Override
    public void onBatchUpdateCart(boolean isSuccess, AddCartBean data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_fragment_cart_bottom_go:
                //去结算的时候判断是否有选中的商品
                if(!TextUtils.isEmpty(YPPreference.newInstance().getShopId())) {
                    startActivityEx(PayOrderActivity.class);
                }
                break;

            case R.id.cb_fragment_cart_bottom_all_check:
                int size = mData.size();
                if (mCbCheck.isChecked()) {
                    for (int i = 0; i < size; i++) {
                        mData.get(i).isSelected = true;
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < size; i++) {
                        mData.get(i).isSelected = false;
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mAllMoney=0.0f;
    }

    /**
     * 没有选中checkBox的时候删除按钮是不能被点击的
     */
    private void setButtonCannotClick() {
        mBtnBottomGo.setBackgroundResource(R.color.color_background);
        mBtnBottomGo.setEnabled(false);
    }

    /**
     * 选中checkBox的时候删除按钮是可以点击的
     */
    private void setButtonCanClick() {
        mBtnBottomGo.setBackgroundResource(R.color.color_theme);
        mBtnBottomGo.setEnabled(true);
    }


}
