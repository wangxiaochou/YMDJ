package com.ypshengxian.daojia.ui.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.ICategoryContract;
import com.ypshengxian.daojia.mvp.presenter.CategoryPresenter;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.AllGoodsBean;
import com.ypshengxian.daojia.network.bean.CateGoryBean;
import com.ypshengxian.daojia.ui.activity.GoodsDetailsActivity;
import com.ypshengxian.daojia.ui.activity.SearchActivity;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.EmptyView;
import com.ypshengxian.daojia.view.ExtendEditText;
import com.ypshengxian.daojia.view.RecyclerDivider;
import com.ypshengxian.daojia.view.SimpleTitleBarBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-27
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class CategoryFragment extends BaseMVPYpFreshFragment<ICategoryContract.View, CategoryPresenter> implements
        ICategoryContract.View, View.OnClickListener {
    /** 左边的列表 */
    private RecyclerView mRlLeft;
    /** 顶部 */
    private RecyclerView mRlTop;
    /** 内容 */
    private RecyclerView mRlCount;
    /** 综合排序 */
    private TextView mTvAll;
    /** 销量排序 */
    private TextView mTvAmount;
    /** 价格排序 */
    private LinearLayout mLlMoneyLayout;
    /** 价格排序 */
    private TextView mTvMoney;
    /** 顶部导航栏 */
    private SimpleTitleBarBuilder simpleTitleBarBuilder;
    /** 搜索 */
    private ExtendEditText mEdSearch;
    /** 左边数据源 */
    private List<CateGoryBean> datas = new ArrayList<CateGoryBean>();
    /** 左边适配器 */
    private BaseQuickAdapter<CateGoryBean, BaseViewHolder> leftAdapter;
    /** top数据源 */
    private List<CateGoryBean.ChildrenBean> topData = new ArrayList<CateGoryBean.ChildrenBean>();
    /** top适配器 */
    private BaseQuickAdapter<CateGoryBean.ChildrenBean, BaseViewHolder> topAdapter;
    /** 内容适配器 */
    private BaseQuickAdapter<AllGoodsBean.DataBean, BaseViewHolder> countAdapter;
    /** 内容数据 */
    private List<AllGoodsBean.DataBean> allGoodsBeans = new ArrayList<AllGoodsBean.DataBean>();

    /** 分类ID */
    private String category;
    /** 请求数据 */
    private Map<String, String> map = new WeakHashMap<String, String>();
    /** 空布局 */
    private EmptyView mEmptyView;
    /** 上拉加载更多 */
    private SmartRefreshLayout mSrRefresh;
    /** 开始条数 */
    private int start = 0;
    /** 升序降序,默认升序 */
    private String mSortStyle=Count.SORT_litre;
    /** 排序的字符串,默认综合 */
    private String mCurrentSortText=Count.SORT_SYNTHESIZE;
    /** 是否单次点击综合 */
    private boolean isSynthesizeFrist;
    /** 是否单次点击价格 */
    private boolean isPriceFrist;
    /** 是否单次点击销量 */
    private boolean isSalesSumFrist;


    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_category;
    }

    @Override
    public void loadData(boolean firstLoad) {
        super.loadData(firstLoad);
        if (firstLoad) {
            mPresenter.requestData();
        }
    }

    @Override
    public void initView() {
        //顶部
        simpleTitleBarBuilder = SimpleTitleBarBuilder.attach(getBaseActivity(), getView())
                .inflateCenterView(R.layout.layout_class_title)
                .setStatusBarColorAlpha(ResUtils.getColor(R.color.color_theme), 0)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setShadowHeight(0);
        mEdSearch = simpleTitleBarBuilder.findViewById(R.id.eet_layout_class_title_input);
        mEdSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityEx(SearchActivity.class);
            }
        });

        //排序
        mTvAll = (TextView) findViewById(R.id.tv_fragment_category_random_sort);
        mTvAmount = (TextView) findViewById(R.id.tv_fragment_category_sale_sort);
        mLlMoneyLayout = (LinearLayout) findViewById(R.id.ll_fragment_category_price_sort);
        mTvMoney = (TextView) findViewById(R.id.tv_fragment_category_money);
        mTvAll.setOnClickListener(this);
        mTvAmount.setOnClickListener(this);
        mLlMoneyLayout.setOnClickListener(this);

        //左边
        mRlLeft = (RecyclerView) findViewById(R.id.rl_fragment_category_left);
        mRlLeft.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        leftAdapter = new BaseQuickAdapter<CateGoryBean, BaseViewHolder>(R.layout.item_class_fragment_category_rv, datas) {

            @Override
            protected void convert(BaseViewHolder helper, CateGoryBean item) {
                if (item.isSelect) {
                    helper.setTextColor(R.id.tv_item_class_fragment_category_name, ResUtils.getColor(R.color.preference_name));
                    helper.setBackgroundColor(R.id.ll_item_class_fragment_category_layout, ResUtils.getColor(R.color.color_white));
                    helper.setVisible(R.id.tv_item_class_fragment_category_color, true);
                } else {
                    helper.setTextColor(R.id.tv_item_class_fragment_category_name, ResUtils.getColor(R.color.preference_price));
                    helper.setBackgroundColor(R.id.ll_item_class_fragment_category_layout, ResUtils.getColor(R.color.color_background));
                    helper.setVisible(R.id.tv_item_class_fragment_category_color, false);
                }
                helper.setText(R.id.tv_item_class_fragment_category_name, item.name);
            }
        };
        mRlLeft.setAdapter(leftAdapter);
        mRlLeft.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));


        //顶部
        mRlTop = (RecyclerView) findViewById(R.id.rl_fragment_category_top);
        mRlTop.setLayoutManager(new GridLayoutManager(Utils.getContext(), 3));
        topAdapter = new BaseQuickAdapter<CateGoryBean.ChildrenBean, BaseViewHolder>(R.layout.item_top_category, topData) {
            @Override
            protected void convert(BaseViewHolder helper, CateGoryBean.ChildrenBean item) {
                if (item.isSelect) {
                    helper.setBackgroundRes(R.id.tv_item_top_category_name, R.drawable.shape_category_top_select);
                    helper.setTextColor(R.id.tv_item_top_category_name, ResUtils.getColor(R.color.color_white));
                } else {
                    helper.setBackgroundRes(R.id.tv_item_top_category_name, R.drawable.shape_category_top_selected);
                    helper.setTextColor(R.id.tv_item_top_category_name, ResUtils.getColor(R.color.preference_price));
                }
                helper.setText(R.id.tv_item_top_category_name, item.name);
            }
        };
        mRlTop.setAdapter(topAdapter);

        //内容
        mSrRefresh = (SmartRefreshLayout) findViewById(R.id.sr_fragment_category_refresh);
        mSrRefresh.setEnableRefresh(false);
        mRlCount = (RecyclerView) findViewById(R.id.rl_fragment_category_count);
        mRlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mEmptyView = new EmptyView(Utils.getContext());
        mEmptyView.setMessage(R.string.msg_category_empty);
        countAdapter = new BaseQuickAdapter<AllGoodsBean.DataBean, BaseViewHolder>(R.layout.item_class, allGoodsBeans) {
            @Override
            protected void convert(BaseViewHolder helper, AllGoodsBean.DataBean item) {
                helper.setGone(R.id.tv_class_folder, false);
                helper.setText(R.id.tv_class_name, item.title);
                helper.setText(R.id.tv_class_num, String.format(ResUtils.getString(R.string.class_text_num), item.salesSum));
                helper.setText(R.id.tv_item_class_money,
                        String.format(ResUtils.getString(R.string.main_text_new_price), item.price, item.unit.replace("g", "斤")));
                TextView textView = helper.getView(R.id.tv_item_class_old_money);
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                textView.setText(String.format(ResUtils.getString(R.string.main_text_new_price), item.originPrice, item.unit.replace("g", "斤")));

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
        mRlCount.setAdapter(countAdapter);
        mRlCount.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));

        initListener();


    }

    /**
     * 设置点击事件
     */
    private void initListener() {
        leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                allGoodsBeans.clear();
                start = 0;
                mSrRefresh.resetNoMoreData();
                map = new WeakHashMap<String, String>();
                topData = datas.get(position).children;
                category = datas.get(position).id;
                //设置第一个为默认
                LogUtils.e(topData.size() + "当前大小1");
                if (topData.size() > 0) {
                    for (int i = 0; i < topData.size(); i++) {
                        topData.get(i).isSelect = false;
                    }
                    topData.get(0).isSelect = true;
                }
                topAdapter.setNewData(topData);

                for (int i = 0; i < datas.size(); i++) {
                    if (position == i) {
                        datas.get(i).isSelect = true;
                    } else {
                        datas.get(i).isSelect = false;
                    }
                }
                leftAdapter.setNewData(datas);
                map.put("categoryId", category);
                map.put("start", String.valueOf(start));
                map.put("order",mCurrentSortText);
                map.put("sort",mSortStyle);
                YpCache.setMap(map);
                mPresenter.getGoods(map);
            }
        });

        topAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                allGoodsBeans.clear();
                start = 0;
                mSrRefresh.resetNoMoreData();
                map = new WeakHashMap<String, String>();
                category = topData.get(position).id;
                for (int i = 0; i < topData.size(); i++) {
                    if (position == i) {
                        topData.get(i).isSelect = true;
                    } else {
                        topData.get(i).isSelect = false;
                    }
                }
                topAdapter.setNewData(topData);
                map.put("categoryId", String.valueOf(category));
                map.put("start", String.valueOf(start));
                map.put("order",mCurrentSortText);
                map.put("sort",mSortStyle);
                YpCache.setMap(map);
                mPresenter.getGoods(map);
            }
        });

        countAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(Count.CATEGORY_ID, Integer.valueOf(allGoodsBeans.get(position).id));
                startActivityEx(GoodsDetailsActivity.class, bundle);
            }
        });
        countAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (id) {
                    case R.id.btn_item_class_add:
                        Map<String, String> map = new WeakHashMap<String, String>();
                        //String.valueOf(allGoodsBeans.get(position).id)
                        map.put("goodsId", String.valueOf(allGoodsBeans.get(position).id));
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
                map = new WeakHashMap<String, String>();
                map.put("categoryId", String.valueOf(category));
                map.put("start", String.valueOf(start));
                map.put("order",mCurrentSortText);
                map.put("sort",mSortStyle);
                YpCache.setMap(map);
                mPresenter.getGoods(map);
            }
        });
    }


    @Override
    protected CategoryPresenter createPresenter() {
        return new CategoryPresenter();
    }


    @Override
    public void onResponseData(boolean isSuccess, List<CateGoryBean> data) {
        map = new WeakHashMap<String, String>();
        if (isSuccess) {
            datas.addAll(data);
            datas.get(0).isSelect = true;
            topData = datas.get(0).children;
            category = datas.get(0).id;
            for (int i = 0; i < topData.size(); i++) {
                if (i == 0) {
                    topData.get(i).isSelect = true;
                }
            }
            leftAdapter.setNewData(datas);
            topAdapter.setNewData(topData);
            map.put("categoryId", String.valueOf(category));
            map.put("start", String.valueOf(start));
            map.put("order",mCurrentSortText);
            map.put("sort",mSortStyle);
            YpCache.setMap(map);
            mPresenter.getGoods(map);
        }

    }

    /**
     * 设置空布局
     */
    private void setEmptyView() {
        int size = allGoodsBeans.size();
        if (0 == size) {
            countAdapter.setEmptyView(mEmptyView);

        }
    }

    @Override
    public void onGetGoods(boolean isSuccess, AllGoodsBean data) {
        if (isSuccess) {
            allGoodsBeans.addAll(data.data);
            countAdapter.setNewData(allGoodsBeans);
            mSrRefresh.finishLoadmore();
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
            ToastUtils.showShortToast("添加成功");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            default:
                break;
            case R.id.tv_fragment_category_random_sort:
                mTvAll.setTextColor(ResUtils.getColor(R.color.color_theme));
                mTvAmount.setTextColor(ResUtils.getColor(R.color.top_bar_title_color));
                mTvMoney.setTextColor(ResUtils.getColor(R.color.top_bar_title_color));
                mCurrentSortText=Count.SORT_SYNTHESIZE;
                if(!isSynthesizeFrist){
                    mSortStyle=Count.SORT_drop;
                }else {
                    mSortStyle=Count.SORT_litre;
                }
                isSynthesizeFrist=!isSynthesizeFrist;
                break;
            case R.id.tv_fragment_category_sale_sort:
                mTvAmount.setTextColor(ResUtils.getColor(R.color.color_theme));
                mTvAll.setTextColor(ResUtils.getColor(R.color.top_bar_title_color));
                mTvMoney.setTextColor(ResUtils.getColor(R.color.top_bar_title_color));
                mCurrentSortText=Count.SORT_SALES_VOLUME;
                if(!isSalesSumFrist){
                    mSortStyle=Count.SORT_drop;
                }else {
                    mSortStyle=Count.SORT_litre;
                }

                isSalesSumFrist=!isSalesSumFrist;
                break;
            case R.id.ll_fragment_category_price_sort:
                mTvMoney.setTextColor(ResUtils.getColor(R.color.color_theme));
                mTvAmount.setTextColor(ResUtils.getColor(R.color.top_bar_title_color));
                mTvAll.setTextColor(ResUtils.getColor(R.color.top_bar_title_color));
                mCurrentSortText=Count.SORT_PRICE;
                if(!isPriceFrist){
                    mSortStyle=Count.SORT_drop;
                }else {
                    mSortStyle=Count.SORT_litre;
                }

                isPriceFrist=!isPriceFrist;
                break;
        }
        allGoodsBeans.clear();
        map = new WeakHashMap<String, String>();
        map.put("categoryId", String.valueOf(category));
        map.put("start", String.valueOf(start));
        map.put("order",mCurrentSortText);
        map.put("sort",mSortStyle);
        YpCache.setMap(map);
        mPresenter.getGoods(map);
    }

    /**
     * 当导航被点击,或者轮播图规则触发
     */
    public void setOnNavClick(String link) {
        CateGoryBean chooseBean = null;
        int needPosition = 0;
        if (null != datas) {
            int size = datas.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    if (link.equals(datas.get(i).id)) {
                        needPosition = i;
                        chooseBean = datas.get(i);
                        break;
                    }
                }
            }
        } else {
            return;
        }


        if (null == chooseBean) {
            return;
        }
        //重新设置数据源
        for (int i = 0; i < datas.size(); i++) {
            if (needPosition == i) {
                datas.get(i).isSelect = true;
            } else {
                datas.get(i).isSelect = false;
            }
        }
        leftAdapter.setNewData(datas);

        topData = chooseBean.children;
        category = datas.get(needPosition).id;
        for (int i = 0; i < topData.size(); i++) {
            if (i == 0) {
                topData.get(i).isSelect = true;
            } else {
                topData.get(i).isSelect = false;
            }
        }
        topAdapter.setNewData(topData);
        allGoodsBeans.clear();
        start = 0;
        mSrRefresh.resetNoMoreData();
        map = new WeakHashMap<String, String>();
        map.put("categoryId", String.valueOf(category));
        map.put("start", String.valueOf(start));
        map.put("order",mCurrentSortText);
        map.put("sort",mSortStyle);
        YpCache.setMap(map);
        mPresenter.getGoods(map);

    }

    /**
     * 添加全部到子分类列表
     *
     * @return 子分类
     */
    private CateGoryBean.ChildrenBean addChildAllTitle(int position) {
        CateGoryBean.ChildrenBean bean = new CateGoryBean.ChildrenBean();
        bean.isShow = "1";
        bean.id = datas.get(position).id;
        bean.name = "全部";
        bean.isSelect = true;
        return bean;
    }


}
