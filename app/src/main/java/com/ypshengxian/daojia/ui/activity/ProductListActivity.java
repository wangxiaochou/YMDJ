package com.ypshengxian.daojia.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.annotation.ActivityOption;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IProductListContract;
import com.ypshengxian.daojia.mvp.presenter.ProductListPresenter;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.AllGoodsBean;
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
 * 商品列表
 *
 * @author Yan
 * @date 2018-03-30
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
@ActivityOption(reqLogin = true)
public class ProductListActivity extends BaseMVPYpFreshActicity<IProductListContract.View, ProductListPresenter> implements
        IProductListContract.View {
    /** 列表展示 */
    private RecyclerView mRlCount;
    /** 刷新 */
    private SmartRefreshLayout mSrRefresh;
    /** 数据源 */
    private List<AllGoodsBean.DataBean> mData = new ArrayList<AllGoodsBean.DataBean>();
    /** 适配器 */
    private BaseQuickAdapter<AllGoodsBean.DataBean, BaseViewHolder> mAdapter;
    /** 空数据 */
    private EmptyView mEmptyView;
    /** 开始 */
    private int start = 0;
    /** 从哪里过来的 */
    private String fromWhere;
    /** 商品名称 */
    private String productId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        productId = bundle.getString(Count.PRODUCT_LIST_GOODS_ID);
        fromWhere = bundle.getString(Count.PRODUCT_LIST_WHERE);
        initView();
        if (!TextUtils.isEmpty(productId) && !TextUtils.isEmpty(fromWhere)) {
            Map<String, String> map = new WeakHashMap<String, String>();
            switch (fromWhere) {
                case Count.NAV:
                    //首页导航
                    map.put("navId", productId);
                    break;
                case Count.SEARCH:
                    //搜索
                    map.put("search", productId);
                    break;
                case Count.FLOOR:
                    map.put("floorId", productId);
                    //楼层
                    break;
                default:
                    break;
            }
            map.put("start", String.valueOf(start));
            YpCache.setMap(map);
            mPresenter.requestData(map);

        }

    }

    @Override
    public void onResponseData(boolean isSuccess, AllGoodsBean data) {
        if (isSuccess) {
            this.mData.addAll(data.data);
            mAdapter.setNewData(mData);
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

    @Override
    protected ProductListPresenter createPresenter() {
        return new ProductListPresenter();
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
    public int getContentView() {
        return R.layout.activity_product_list;
    }

    private void initView() {
        TitleUtils.setTitleBar(this, "商品列表");
        mSrRefresh = (SmartRefreshLayout) findViewById(R.id.sr_activity_product_list_refresh);
        mRlCount = (RecyclerView) findViewById(R.id.rl_activity_product_list_count);

        //空布局
        mEmptyView =new EmptyView(Utils.getContext());
        mEmptyView.setMessage(R.string.msg_shop_empty);

        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new BaseQuickAdapter<AllGoodsBean.DataBean, BaseViewHolder>(R.layout.item_class, mData) {
            @Override
            protected void convert(BaseViewHolder helper, AllGoodsBean.DataBean item) {
                helper.setGone(R.id.tv_class_folder, false);
                helper.setText(R.id.tv_class_name, item.title);
                helper.setText(R.id.tv_class_num, String.format(ResUtils.getString(R.string.class_text_num), item.salesSum));
                helper.setText(R.id.tv_item_class_money,
                        String.format(ResUtils.getString(R.string.main_text_new_price), item.prePrice, item.unit.replace("g", "斤")));
                TextView textView = helper.getView(R.id.tv_item_class_old_money);
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                textView.setText(String.format(ResUtils.getString(R.string.class_text_old), item.originPrice, item.unit.replace("g", "斤")));

                if (!TextUtils.isEmpty(item.thumb)) {
                    LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_class_image), item.thumb);
                } else {
                    if (null != item.picture && item.picture.size() > 0) {
                        LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_class_image), item.picture.get(0));
                    } else {
                        LoaderFactory.getLoader().loadResource(helper.getView(R.id.iv_class_image), R.mipmap.icon_app);
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
                bundle.putInt(Count.CATEGORY_ID, Integer.valueOf(mData.get(position).id));
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
        mSrRefresh.setEnableRefresh(false);
        mSrRefresh.setEnableLoadmore(false);
//        mSrRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                start += 10;
//                Map<String, String> map = new WeakHashMap<String, String>();
//                switch (fromWhere) {
//                    case Count.NAV:
//                        //首页导航
//                        map.put("navId", productId);
//                        break;
//                    case Count.SEARCH:
//                        //搜索
//                        map.put("search", productId);
//                        break;
//                    case Count.FLOOR:
//                        map.put("floorId", productId);
//                        //楼层
//                        break;
//                    default:
//                        break;
//                }
//                map.put("start", String.valueOf(start));
//                YpCache.setMap(map);
//                mPresenter.requestData(map);
//
//            }
//        });

    }
}
