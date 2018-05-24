package com.ypshengxian.daojia.ui.fragment;

import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.map.geolocation.TencentLocation;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.adapter.BaseDelegateAdapter;
import com.ypshengxian.daojia.adapter.DataInflater;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.event.ShopNameEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IHomeContract;
import com.ypshengxian.daojia.mvp.presenter.HomePresenter;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.HomeBean;
import com.ypshengxian.daojia.network.bean.ShopBean;
import com.ypshengxian.daojia.network.bean.ShopSmallBean;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.ui.activity.ArticleWebActivity;
import com.ypshengxian.daojia.ui.activity.ChooseStoreActivity;
import com.ypshengxian.daojia.ui.activity.EventActivity;
import com.ypshengxian.daojia.ui.activity.GoodsDetailsActivity;
import com.ypshengxian.daojia.ui.activity.LimitActivity;
import com.ypshengxian.daojia.ui.activity.ProductListActivity;
import com.ypshengxian.daojia.ui.activity.SearchActivity;
import com.ypshengxian.daojia.ui.activity.ShopDetailsActivity;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TencentLocationUtils;
import com.ypshengxian.daojia.utils.TimeUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.SimpleTitleBarBuilder;
import com.ypshengxian.daojia.view.YpDialog;
import com.ypshengxian.daojia.view.banner.Banner;
import com.ypshengxian.daojia.view.banner.BannerConfig;
import com.ypshengxian.daojia.view.banner.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 首页
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class HomeFragment extends BaseMVPYpFreshFragment<IHomeContract.View, HomePresenter> implements
        IHomeContract.View, View.OnClickListener {
    /** 内容 */
    private RecyclerView mRlCount;
    /** manger */
    protected VirtualLayoutManager mVirtualLayoutManager;
    /** 适配器 */
    protected DelegateAdapter mDelegateAdapter;
    /** banner */
    private BaseDelegateAdapter<HomeBean.BannerBean> bannerBeanVBaseAdapter;
    /** banner数据源 */
    private List<HomeBean.BannerBean> bannerBeans = new ArrayList<HomeBean.BannerBean>();
    /** 导航 */
    private BaseDelegateAdapter<HomeBean.NavBean> navBeanVBaseAdapter;
    /** 导航数据源 */
    private List<HomeBean.NavBean> navBeans = new ArrayList<HomeBean.NavBean>();
    /** 单图 */
    private BaseDelegateAdapter<String> imgAdapter;
    /** 新人专享 */
    private BaseDelegateAdapter<HomeBean.NewSalesBean.GoodsBean> newbieAdapter;
    /** 新人专享数据源 */
    private List<HomeBean.NewSalesBean.GoodsBean> newSalesBeans = new ArrayList<HomeBean.NewSalesBean.GoodsBean>();
    /** 限时 */
    private BaseDelegateAdapter<HomeBean.FlashSalesBean> limitAdapter;
    /** 限时数据源 */
    private List<HomeBean.FlashSalesBean> flashSalesBeans = new ArrayList<HomeBean.FlashSalesBean>();
    /** 活动 */
    private BaseDelegateAdapter<HomeBean.TopicBean> eventAdapter;
    /** 活动 */
    private BaseQuickAdapter<HomeBean.TopicBean, BaseViewHolder> smallEventAdapter;
    /** 活动数据 */
    private List<HomeBean.TopicBean> topicBeans = new ArrayList<HomeBean.TopicBean>();

    /** 具体商品的title */
    private BaseDelegateAdapter<HomeBean.FloorBean> floorTitleAdapter;
    /** 底部商品数据源 */
    private List<HomeBean.FloorBean> floorBeans = new ArrayList<HomeBean.FloorBean>();
    /** 标题栏 */
    private SimpleTitleBarBuilder mTitleBarBuilder;
    /** 定位数据 */
    private TextView mTitleCenter;
    /** 轮播 */
    private Banner mBanner;
    /** 刷新布局 */
    private SmartRefreshLayout mSrRefresh;
    /** 导航栏点击事件 */
    private OnNavClickListener navClickListener;
    /** 悬浮的button */
    private FloatingActionButton mFabGoTop;
    /** 滑动的距离Y轴 */
    private int mScrollY = 0;
    private boolean isRefresh = false;


    /**
     * 单列
     *
     * @return this
     */
    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_home;
    }


    /**
     * 设置导航栏点击事件的监听
     *
     * @param navClickListener 监听者
     */
    public void setNavClickListener(OnNavClickListener navClickListener) {
        this.navClickListener = navClickListener;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mTitleBarBuilder = SimpleTitleBarBuilder.attach(getBaseActivity(), getView())
                .setStatusBarColorAlpha(ResUtils.getColor(R.color.color_theme), 0)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .inflateCenterView(R.layout.layout_home_title)
                .setRightDrawable(ResUtils.getDrawable(R.drawable.icon_search))
                .setRightVisible(true)
                .setShadowHeight(0);

        //定位点击事件
        mTitleCenter = (TextView) mTitleBarBuilder.findViewById(R.id.tv_layout_home_title);
        mTitleCenter.setOnClickListener(this);


        //搜索点击事件
        mTitleBarBuilder.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityEx(SearchActivity.class);
            }
        });

        mSrRefresh = (SmartRefreshLayout) findViewById(R.id.sr_fragment_refresh);
        mSrRefresh.setEnableLoadmore(false);
        mSrRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isRefresh = true;
                mPresenter.requestData();
            }
        });
        mRlCount = (RecyclerView) findViewById(R.id.rl_fragment_count);

        mFabGoTop = (FloatingActionButton) findViewById(R.id.fab_fragment_home_go_top);
        mFabGoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVirtualLayoutManager.smoothScrollToPosition(mRlCount,null,0);
                mScrollY = 0;
            }
        });
        initRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }

    /**
     * 记载数据
     *
     * @param data 数据
     */
    private void initData(HomeBean data) {
        final ArrayList<String> images = new ArrayList<String>();
        int size = 0;
        if (null != bannerBeans) {
            size = bannerBeans.size();
        }
        if (size > 0) {
            for (HomeBean.BannerBean imageUrl : bannerBeans) {
                images.add(imageUrl.img);
            }
        }
        //banner
        bannerBeanVBaseAdapter = new BaseDelegateAdapter<HomeBean.BannerBean>(bannerBeans, R.layout.item_main_home_template_banner, new SingleLayoutHelper(), 1) {
            @Override
            public void convert(BaseViewHolder holder, HomeBean.BannerBean data, int position) {
                mBanner = (Banner) holder.getView(R.id.banner_template_banner);

                // 设置图片加载器
                mBanner.setImageLoader(LoaderFactory.getLoader());

                // 设置Banner样式
                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                mBanner.setIndicatorGravity(BannerConfig.CENTER);


                // 点击监听
                mBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void onBannerClick(int position) {
                        int type = bannerBeans.get(position).type;
                        LogUtils.e(type+"daad");
                        String link = bannerBeans.get(position).link;
                        switch (type) {
                            case 0:
                                //列表
                                Bundle bundle = new Bundle();
                                bundle.putString(Count.PRODUCT_LIST_GOODS_ID,link);
                                bundle.putString(Count.PRODUCT_LIST_WHERE, Count.BANNER);
                                startActivityEx(ProductListActivity.class, bundle);
                                break;
                            case 1:
                                //分类
                                if(null!=navClickListener){
                                    LogUtils.e("点击banner");
                                    navClickListener.onNavClick(link,"");
                                }
                                break;
                            case 2:
                                startActivityEx(ArticleWebActivity.class);
                            default:
                                break;
                        }
                    }

                });

                // 设置图片集合
                mBanner.setImages(images);


                // 开始下载
                mBanner.start();
            }
        };
        //导航
        navBeanVBaseAdapter = new BaseDelegateAdapter<HomeBean.NavBean>(navBeans, R.layout.item_main_home_navigation, new GridLayoutHelper(4), 2) {
            @Override
            public void convert(BaseViewHolder helper, HomeBean.NavBean navBean, int position) {
                helper.setText(R.id.tv_item_main_home_navigation, navBean.title);
                ImageView view = (ImageView) helper.getView(R.id.iv_item_main_home_navigation);
                LoaderFactory.getLoader().loadNet(view, navBean.img);
                helper.getView(R.id.ll_item_main_home_nav_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int type = navBean.type;
                        switch (type) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString(Count.PRODUCT_LIST_GOODS_ID, navBean.link);
                                bundle.putString(Count.PRODUCT_LIST_WHERE, Count.NAV);
                                startActivityEx(ProductListActivity.class, bundle);
                                break;
                            case 1:
                                if (null != navClickListener) {
                                    navClickListener.onNavClick(navBean.link, navBean.title);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
            }


        };


        //单图
        SingleLayoutHelper oneHelper = new SingleLayoutHelper();
        oneHelper.setMargin(0, 10, 0, 10);
        imgAdapter = new BaseDelegateAdapter<String>("", R.layout.item_one_img, oneHelper, 3) {
            @Override
            public void convert(BaseViewHolder holder, String data, int position) {
                holder.getView(R.id.iv_item_one_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }


        };

        //新人专享
        newbieAdapter = new BaseDelegateAdapter<HomeBean.NewSalesBean.GoodsBean>(newSalesBeans, R.layout.item_main_small_goods, new GridLayoutHelper(3), 4) {
            @Override
            public void convert(BaseViewHolder helper, HomeBean.NewSalesBean.GoodsBean newSalesBean, int position) {
                ImageView view = helper.getView(R.id.iv_item_main_small_goods_img);
                if (!TextUtils.isEmpty(newSalesBean.thumb)) {
                    LoaderFactory.getLoader().loadNet(view, newSalesBean.thumb);
                } else {
                    if (null != newSalesBean.picture && newSalesBean.picture.size() > 0) {
                        LoaderFactory.getLoader().loadNet(view, newSalesBean.picture.get(0));
                    }
                }
                helper.setGone(R.id.tv_item_main_small_goods_limit, false);
                helper.setGone(R.id.rl_item_main_small_goods_limit_layout, false);
                helper.setText(R.id.tv_item_main_small_goods_now,
                        String.format(ResUtils.getString(R.string.main_text_new_price), newSalesBean.price, newSalesBean.unit));
                TextView textView = helper.getView(R.id.tv_item_main_small_goods_old);
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                textView.setText(newSalesBean.originPrice);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Count.CATEGORY_ID, Integer.valueOf(newSalesBean.id));
                        startActivityEx(GoodsDetailsActivity.class, bundle);
                    }
                });
            }


        };

        //限时
        limitAdapter = new BaseDelegateAdapter<HomeBean.FlashSalesBean>(flashSalesBeans, R.layout.item_main_small_goods, new GridLayoutHelper(3), 5) {
            @Override
            public void convert(BaseViewHolder helper, HomeBean.FlashSalesBean flashSalesBean, int position) {
                LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_item_main_small_goods_img), flashSalesBean.img);

                helper.setText(R.id.tv_item_main_small_goods_now,
                        String.format(ResUtils.getString(R.string.main_text_new_price), flashSalesBean.price, flashSalesBean.unit));
                TextView textView = helper.getView(R.id.tv_item_main_small_goods_old);
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                textView.setText(flashSalesBean.origin_price);
                TextView time = helper.getView(R.id.tv_item_main_small_goods_limit);
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .compose(getBaseActivity().applySchedulers(ActivityEvent.DESTROY))
                        .take(flashSalesBeans.get(position).expiry)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                int remain = flashSalesBeans.get(position).expiry - 1 - aLong.intValue();
                                if (remain > 0) {
                                    time.setText(TimeUtils.millis2String(remain * 1000, "HH:mm:ss"));
                                } else {
                                    time.setText("00:00:00");
                                }
                            }
                        });
                helper.getView(R.id.cv_item_main_small_goods_img_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Count.CATEGORY_ID, 1);
                        startActivityEx(GoodsDetailsActivity.class, bundle);
                    }
                });
            }


        };


        //活动
        eventAdapter = new BaseDelegateAdapter<HomeBean.TopicBean>(topicBeans, R.layout.item_main_home_template_module_entry, new SingleLayoutHelper(), 6) {
            @Override
            public void convert(BaseViewHolder helper, HomeBean.TopicBean data, int position) {
                RecyclerView recyclerView = (RecyclerView) helper.getView(R.id.vp_main_home_template_module_entry_viewpager);
                recyclerView.setFocusable(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getContext(),
                        LinearLayoutManager.HORIZONTAL, false));
                smallEventAdapter = new BaseQuickAdapter<HomeBean.TopicBean, BaseViewHolder>(R.layout.item_main_small_event, topicBeans) {

                    @Override
                    protected void convert(BaseViewHolder helper, HomeBean.TopicBean item) {
                        if (!TextUtils.isEmpty(item.img)) {
                            LoaderFactory.getLoader().loadNet((ImageView) helper.getView(R.id.iv_item_main_small_event_img), item.img);
                        }
                    }
                };
                recyclerView.setAdapter(smallEventAdapter);
                smallEventAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Count.SHOP_DETAILS_ID, String.valueOf(topicBeans.get(position).id));
                        bundle.putString(Count.TITLE_NAME, "专题详情");
                        startActivityEx(ShopDetailsActivity.class, bundle);
                    }
                });
            }


        };

        //商品数据
        LinearLayoutHelper goodsHelper = new LinearLayoutHelper();
        goodsHelper.setMarginTop(14);

        floorTitleAdapter = new BaseDelegateAdapter<HomeBean.FloorBean>(floorBeans, R.layout.item_main_title_and_img, goodsHelper, 7) {
            @Override
            public void convert(BaseViewHolder helper, HomeBean.FloorBean floorBean, int position) {
                helper.getView(R.id.rl_item_title_and_img_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转商品列表
                        Bundle bundle = new Bundle();
                        bundle.putString(Count.PRODUCT_LIST_GOODS_ID, String.valueOf(floorBean.link));
                        bundle.putString(Count.PRODUCT_LIST_WHERE, Count.FLOOR);
                        startActivityEx(ProductListActivity.class, bundle);
                    }
                });
                helper.setText(R.id.tv_item_title_and_img_title, floorBean.title);
                LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_item_title_and_img_img), floorBean.img);
                RecyclerView recyclerView = (RecyclerView) helper.getView(R.id.rl_item_title_and_img_count);
                recyclerView.setFocusable(false);
                recyclerView.setLayoutManager(new GridLayoutManager(Utils.getContext(), 2));
                BaseQuickAdapter good = new BaseQuickAdapter<HomeBean.FloorBean.ItemsBean, BaseViewHolder>(R.layout.item_main_goods, floorBean.items) {

                    @Override
                    protected void convert(BaseViewHolder helper, HomeBean.FloorBean.ItemsBean item) {
                        helper.setText(R.id.tv_item_main_goods_title, item.subtitle);
                        if (!TextUtils.isEmpty(item.thumb)) {
                            LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_item_main_goods_img), item.thumb);
                        } else {
                            if (null != item.picture && item.picture.size() > 0) {
                                LoaderFactory.getLoader().loadNet(helper.getView(R.id.iv_item_main_goods_img), item.picture.get(0));
                            }
                        }

                        helper.setText(R.id.tv_item_main_goods_now,
                                String.format(ResUtils.getString(R.string.main_text_new_price), item.prePrice, item.unit.replace("g", "斤")));
                        TextView textView = helper.getView(R.id.tv_item_main_goods_old);
                        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        textView.setText(String.format(ResUtils.getString(R.string.main_text_new_price), item.originPrice, item.unit.replace("g", "斤")));
                        String need = item.attribute;
                        if (!TextUtils.isEmpty(need)) {
                            if (need.length() > 2) {
                                if (need.length() > 4) {
                                    need = new StringBuilder(need.substring(0, 4)).insert(2, "\n").toString();
                                } else {
                                    need = new StringBuilder(need).insert(2, "\n").toString();
                                }
                            }
                        } else {
                            need = "";
                            helper.setVisible(R.id.rl__item_main_goods_title_top_layout, false);
                        }
                        helper.setText(R.id.tv_item_main_goods_title_top, need);
                        helper.setText(R.id.tv_item_main_goods_name, item.title);
                        helper.addOnClickListener(R.id.btn_item_main_goods_add);


                    }
                };
                good.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Count.CATEGORY_ID, floorBean.items.get(position).id);
                        startActivityEx(GoodsDetailsActivity.class, bundle);
                    }
                });
                good.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        int id = view.getId();
                        switch (id) {
                            case R.id.btn_item_main_goods_add:
                                Map<String, String> map = new WeakHashMap<String, String>();
                                //String.valueOf(allGoodsBeans.get(position).id)
                                map.put("goodsId", String.valueOf(floorBean.items.get(position).id));
                                map.put("num", "1");
                                YpCache.setMap(map);
                                mPresenter.addCart(map);
                                break;
                            default:
                                break;
                        }
                    }
                });
                recyclerView.setAdapter(good);
            }


        };

        //加入总的adapter
        mDelegateAdapter.addAdapter(bannerBeanVBaseAdapter);
        mDelegateAdapter.addAdapter(navBeanVBaseAdapter);
        mDelegateAdapter.addAdapter(imgAdapter);
        if (null != flashSalesBeans && flashSalesBeans.size() > 0) {
            DataInflater.inflateImgItem(mDelegateAdapter, "限时特惠", new DataInflater.OnTitleClickListener() {
                @Override
                public void onTitleClick(View view, int position, String title) {
                    if (TextUtils.equals("限时特惠", title)) {
                        startActivityEx(LimitActivity.class);
                    }
                }
            });
            mDelegateAdapter.addAdapter(limitAdapter);
        }

        //新人专享
        if (null != newSalesBeans && newSalesBeans.size() > 0) {
            DataInflater.inflateTitleItem(mDelegateAdapter, data.new_sales.name, new DataInflater.OnTitleClickListener() {
                @Override
                public void onTitleClick(View view, int position, String title) {
                    if (TextUtils.equals(data.new_sales.name, title)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Count.TITLE_NAME, data.new_sales.name);
                        startActivityEx(ShopDetailsActivity.class, bundle);
                    }
                }
            });
            mDelegateAdapter.addAdapter(newbieAdapter);
        }

        if (null != topicBeans && topicBeans.size() > 0) {
            DataInflater.inflateTitleItem(mDelegateAdapter, "专题活动", new DataInflater.OnTitleClickListener() {
                @Override
                public void onTitleClick(View view, int position, String title) {
                    if (TextUtils.equals("专题活动", title)) {
                        startActivityEx(EventActivity.class);
                    }
                }
            });
            mDelegateAdapter.addAdapter(eventAdapter);
        }
        mDelegateAdapter.addAdapter(floorTitleAdapter);

    }

    /**
     * 初始化多布局
     */
    public void initRecyclerView() {
        //设置LayoutManager
        mVirtualLayoutManager = new VirtualLayoutManager(getBaseActivity());
        mRlCount.setLayoutManager(mVirtualLayoutManager);
        //总adapter
        mDelegateAdapter = new DelegateAdapter(mVirtualLayoutManager, true);
        mRlCount.setItemAnimator(new DefaultItemAnimator());
        mRlCount.setAdapter(mDelegateAdapter);
//        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
//        pool.setMaxRecycledViews(5, 3);
//        mRlCount.setRecycledViewPool(pool);
//        mRlCount.setItemViewCacheSize(2000);
        mRlCount.clearOnScrollListeners();
        mRlCount.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY = mVirtualLayoutManager.getOffsetToStart();
                if (mScrollY > Count.MAX_SCROLL) {
                    mFabGoTop.setVisibility(View.VISIBLE);
                } else {
                    mFabGoTop.setVisibility(View.GONE);
                }
            }


        });


    }

    @Override
    public void loadData(boolean firstLoad) {
        super.loadData(firstLoad);
        if (firstLoad) {
            mPresenter.requestData();
            Observable.create(new Observable.OnSubscribe<Integer>() {
                @Override
                public void call(Subscriber<? super Integer> subscriber) {
                    subscriber.onCompleted();
                }
            })
                    .delay(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            mPresenter.getShop();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Integer integer) {
                        }
                    });
        }
    }

    @Override
    public void onResponseData(boolean isSuccess, HomeBean data) {
        if (isSuccess) {
            this.bannerBeans = data.banner;
            this.navBeans = data.nav;
            this.floorBeans = data.floor;
            this.newSalesBeans = data.new_sales.goods;
            this.flashSalesBeans = data.flash_sales;
            this.topicBeans = data.topic;
            if (!isRefresh) {
                initData(data);
            } else {
                bannerBeanVBaseAdapter.setData(bannerBeans);
                navBeanVBaseAdapter.setData(navBeans);
                floorTitleAdapter.setData(floorBeans);
                newbieAdapter.setData(newSalesBeans);
                limitAdapter.setData(flashSalesBeans);
                eventAdapter.setData(topicBeans);
                isRefresh = false;
            }
            mSrRefresh.finishRefresh();

        }
    }

    @Override
    public void onAddCart(boolean isSuccess, AddCartBean data) {
        if (isSuccess) {
            EventBus.getDefault().post(new OrderEvent());
            ToastUtils.showShortToast("添加购物车成功");
        }
    }

    @Override
    public void onGetShop(boolean isSuccess, List<ShopBean> data) {
        if (isSuccess) {
            TencentLocationUtils.newInstance().startService(new TencentLocationUtils.ILocationResult() {
                @Override
                public void onReceiveLocation(TencentLocation location) {
                    List<ShopSmallBean> shopSmallBeans = new ArrayList<ShopSmallBean>();
                    List<Double> doubles = new ArrayList<Double>();
                    for (ShopBean bean : data) {
                        float[] f1 = new float[1];
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double.valueOf(bean.lat), Double.valueOf(bean.lng), f1);
                        double f = f1[0];
                        doubles.add(f);
                        shopSmallBeans.add(new ShopSmallBean(bean.name, f, bean.id,
                                bean.secCityId, bean.trdCityId, bean.lat, bean.lng));
                    }
                    Collections.sort(shopSmallBeans, new Comparator<ShopSmallBean>() {
                        @Override
                        public int compare(ShopSmallBean o1, ShopSmallBean o2) {
                            return (int) o1.distance - (int) o2.distance;
                        }
                    });
                    if (!YPPreference.newInstance().getIsFirstLocation()) {
                        YpDialog dialog = new YpDialog(getBaseActivity());
                        dialog.setTitle(R.string.home_location_success);
                        dialog.setMessage(String.format(ResUtils.getString(R.string.home_location_msg), shopSmallBeans.get(0).name));
                        dialog.setNegativeButton("重新选择", true, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityEx(ChooseStoreActivity.class);
                            }
                        });
                        dialog.setPositiveButton("确定", true, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        dialog.show();
                        YPPreference.newInstance().setIsFirstLocation(true);
                    }

                    //默认店铺名字
                    if (TextUtils.isEmpty(YPPreference.newInstance().getShopName())) {
                        mTitleCenter.setText(shopSmallBeans.get(0).name);
                        YPPreference.newInstance().setShopName(shopSmallBeans.get(0).name);
                    } else {
                        mTitleCenter.setText(YPPreference.newInstance().getShopName());
                    }
                    //保存默认的二级ID
                    if (TextUtils.isEmpty(YPPreference.newInstance().getChooseShopTwo())) {
                        YPPreference.newInstance().setChooseShopTwo(shopSmallBeans.get(0).secCityId);
                    }
                    //保存默认的三级ID
                    if (TextUtils.isEmpty(YPPreference.newInstance().getChooseShopThree())) {
                        YPPreference.newInstance().setChooseShopTwo(shopSmallBeans.get(0).trdCityId);
                    }
                    //保存默认城市
                    if (TextUtils.isEmpty(YPPreference.newInstance().getCityName())) {
                        YPPreference.newInstance().setCityName(location.getCity());
                    }
                    //保存店铺id
                    if (TextUtils.isEmpty(YPPreference.newInstance().getShopId())) {
                        YPPreference.newInstance().setShopId(shopSmallBeans.get(0).id);
                    }
                }
            });
        }
    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_layout_home_title:
                startActivityEx(ChooseStoreActivity.class);
                break;
            default:
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ShopNameEvent event) {
        mTitleCenter.setText(event.name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 导航点击事件
     */
    public interface OnNavClickListener {
        /**
         * 点击
         *
         * @param link ID
         * @param title 名字
         */
        void onNavClick(String link, String title);
    }
}
