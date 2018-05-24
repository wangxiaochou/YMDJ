package com.ypshengxian.daojia.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IShopDetailsContract;
import com.ypshengxian.daojia.mvp.presenter.ShopDetailsPresenter;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.TopBean;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.EmptyView;
import com.ypshengxian.daojia.view.RecyclerDivider;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 首页点击活动详情界面
 *
 * @author Yan
 * @date 2018-03-21
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
@ActivityOption(reqLogin = true)
public class ShopDetailsActivity extends BaseMVPYpFreshActicity<IShopDetailsContract.View, ShopDetailsPresenter> implements
        IShopDetailsContract.View {

    /** 图片 */
    private ImageView mIvImg;
    /** 提示语 */
    private TextView mTvText;
    /** 内容展示 */
    private RecyclerView mRlCount;
    /** 适配器 */
    private BaseQuickAdapter<TopBean.GoodsBean, BaseViewHolder> mAdapter;
    /** 数据源 */
    private List<TopBean.GoodsBean> mData = new ArrayList<TopBean.GoodsBean>();
    /** 查询的ID */
    private String id;
    /** 标题 */
    private String title;
    /** 开始的位置 */
    private int start = 0;
    /** 加载更多 */
    private SmartRefreshLayout mSrRefresh;
    /** 空布局 */
    private EmptyView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            id = bundle.getString(Count.SHOP_DETAILS_ID);
            title = bundle.getString(Count.TITLE_NAME);
        }
        initView();

        switch (title) {
            case "新人专享":
                Map<String, String> map = new WeakHashMap<String, String>();
                map.put("start", String.valueOf(start));
                YpCache.setMap(map);
                mPresenter.getNewSales(map);
                break;
            case "专题详情":
                mPresenter.requestData(id);
                break;
            default:
                break;
        }
    }

    @Override
    protected ShopDetailsPresenter createPresenter() {
        return new ShopDetailsPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_shop_details;
    }

    private void initView() {
        TitleUtils.setTitleBar(this, title);
        mIvImg = (ImageView) findViewById(R.id.iv_shop_details_img);
        mTvText = (TextView) findViewById(R.id.tv_shop_details_text);
        mSrRefresh = (SmartRefreshLayout) findViewById(R.id.sr_shop_details_refresh);
        mSrRefresh.setEnableRefresh(false);
        mRlCount = (RecyclerView) findViewById(R.id.rl_shop_details_count);
        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mEmptyView = new EmptyView(Utils.getContext());
        mEmptyView.setMessage(R.string.msg_shop_empty);
        mAdapter = new BaseQuickAdapter<TopBean.GoodsBean, BaseViewHolder>(R.layout.item_class, mData) {
            @Override
            protected void convert(BaseViewHolder helper, TopBean.GoodsBean item) {
                helper.setGone(R.id.tv_class_folder, true);
                helper.setText(R.id.tv_class_folder, String.format(ResUtils.getString(R.string.shop_details_folder), (float) ((Float.valueOf(item.price) / Float.valueOf(item.originPrice) * 10f))));
                helper.setText(R.id.tv_class_name, item.title);
                helper.setText(R.id.tv_class_num, String.format(ResUtils.getString(R.string.class_text_num), item.salesSum));
                helper.setText(R.id.tv_item_class_money,
                        String.format(ResUtils.getString(R.string.main_text_new_price), item.price.replace("元", ""), item.unit.replace("g", "斤")));
                TextView textView = helper.getView(R.id.tv_item_class_old_money);
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                textView.setText(String.format(ResUtils.getString(R.string.class_text_old),
                        item.originPrice.replace("元", ""), item.unit.replace("g", "斤")));

                if (!TextUtils.isEmpty(item.thumb)) {
                    LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_class_image), item.thumb);
                } else {
                    if (null != item.picture && item.picture.size() > 0) {
                        LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_class_image), item.picture.get(0));
                    }
                }
                helper.addOnClickListener(R.id.btn_item_class_add);


            }
        };
        mRlCount.setAdapter(mAdapter);
        mRlCount.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putInt(Count.CATEGORY_ID, mData.get(position).id);
                startActivityEx(GoodsDetailsActivity.class, bundle);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (id) {
                    case R.id.btn_item_class_add:
                        Map<String, String> map = new WeakHashMap<String, String>();
                        map.put("goodsId", String.valueOf(mData.get(position).id));
                        map.put("num", "1");
                        YpCache.setMap(map);
                        mPresenter.addCart(map);
                        break;
                    default:
                        break;
                }
            }
        });

        mSrRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                start += 10;
                Map<String, String> map = new WeakHashMap<String, String>();
                map.put("start", String.valueOf(start));
                YpCache.setMap(map);
                mPresenter.getNewSales(map);
            }
        });

    }

    @Override
    public void onResponseData(boolean isSuccess, TopBean data) {
        if (isSuccess) {
            mData = data.goods;
            mAdapter.setNewData(mData);
            initImg(data);
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
    public void onGetNewSales(boolean isSuccess, TopBean data) {
        if (isSuccess) {
            mSrRefresh.finishLoadmore();
            mData.addAll(data.goods);
            mAdapter.setNewData(mData);
            initImg(data);
            if (start >= data.total) {
                mSrRefresh.finishLoadmoreWithNoMoreData();
            }
        }
        setEmptyView();
    }

    @Override
    public void onAddCart(boolean isSuccess, AddCartBean data) {
        if (isSuccess) {
            EventBus.getDefault().post(new OrderEvent());
            ToastUtils.showShortToast("添加购物车成功");
        }
    }

    /**
     * 加载图片
     */
    private void initImg(TopBean data) {
        if (null != data) {
            LoaderFactory.getLoader().loadNet(mIvImg, data.banner);
        }

    }
}
