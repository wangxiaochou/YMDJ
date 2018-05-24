package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.annotation.ActivityOption;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IGoodsDetailsContract;
import com.ypshengxian.daojia.mvp.presenter.GoodsDetailsPresenter;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.GoodsBean;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.banner.Banner;
import com.ypshengxian.daojia.view.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 商品詳情
 */
@ActivityOption(reqLogin = true)
public class GoodsDetailsActivity extends BaseMVPYpFreshActicity<IGoodsDetailsContract.View, GoodsDetailsPresenter> implements IGoodsDetailsContract.View, View.OnClickListener {
    private Banner mBeBanner;
    private TextView mTvDes;
    private TextView mTvGoodsTitle;
    private TextView mTvGoodsSubtitle;
    private TextView mTvPriceEach;
    /**
     * 元/份
     */
    private TextView mTvEach;
    /**
     * 折合65.20/斤
     */
    private TextView mTvPriceCatty;
    /**
     * 每份：约150g
     */
    private TextView mTvEachWeight;
    /**
     * 购买可获得15积分
     */
    private TextView mTvIntegralDes;
    private WebView mWeb;
    private NestedScrollView mSl;
    /**
     * 消耗积分：7200
     */
    private TextView mTvConsumeIntegral;
    private ImageView mIvReduce;
    /**
     * 0
     */
    private TextView mTvNum;
    private ImageView mIvAdd;
    /**
     * 加入购物车
     */
    private TextView mTvJoinCar;
    private RelativeLayout mRlJoinCar;
    /** 商品数量 */
    private int mGoodsNum = 1;
    /** 商品ID */
    private int mCategory = 0;
    /** 数据源 */
    private GoodsBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mCategory = bundle.getInt(Count.CATEGORY_ID);
        }
        mPresenter.requestData(String.valueOf(mCategory));
    }

    @Override
    public int getContentView() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected GoodsDetailsPresenter createPresenter() {
        return new GoodsDetailsPresenter();
    }


    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.goods_details_title_text));
        mBeBanner = (Banner) findViewById(R.id.be_banner);
        mTvDes = (TextView) findViewById(R.id.tv_activity_goods_details_des);
        mTvGoodsTitle = (TextView) findViewById(R.id.tv_activity_goods_details_goods_title);
        mTvGoodsSubtitle = (TextView) findViewById(R.id.tv_activity_goods_details_goods_subtitle);
        mTvPriceEach = (TextView) findViewById(R.id.tv_activity_goods_details_price_each);
        mTvEach = (TextView) findViewById(R.id.tv_activity_goods_details_each);
        mTvPriceCatty = (TextView) findViewById(R.id.tv_activity_goods_details_price_catty);
        mTvEachWeight = (TextView) findViewById(R.id.tv_activity_goods_details_each_weight);
        mTvIntegralDes = (TextView) findViewById(R.id.tv_activity_goods_details_integral_des);
        mWeb = (WebView) findViewById(R.id.web_activity_goods_details);
        mSl = (NestedScrollView) findViewById(R.id.sl_activity_goods_details);
        mTvConsumeIntegral = (TextView) findViewById(R.id.tv_activity_goods_details_consume_integral);
        mIvReduce = (ImageView) findViewById(R.id.iv_activity_goods_details_reduce);
        mTvNum = (TextView) findViewById(R.id.tv_activity_goods_details_num);
        mIvAdd = (ImageView) findViewById(R.id.iv_activity_goods_details_add);
        mTvJoinCar = (TextView) findViewById(R.id.tv_activity_goods_details_join_car);
        mRlJoinCar = (RelativeLayout) findViewById(R.id.rl_activity_goods_details_join_car);

        //设置透明度
        mTvDes.getBackground().setAlpha(170);

        mIvReduce.setOnClickListener(this);
        mIvAdd.setOnClickListener(this);
        mTvJoinCar.setOnClickListener(this);

        //设置图片加载器
        mBeBanner.setImageLoader(LoaderFactory.getLoader());
        //设置banner样式
        mBeBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBeBanner != null) {
            mBeBanner.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBeBanner != null) {
            mBeBanner.stopAutoPlay();
        }
    }

    @Override
    public void onResponseData(boolean isSuccess, GoodsBean data) {
        if (isSuccess) {
            LogUtils.e(data.toString());
            this.data = data;
            mTvGoodsTitle.setText(data.title);
            mTvDes.setText(data.subtitle);
            mTvPriceEach.setText(String.format(ResUtils.getString(R.string.main_text_new_price), data.prePrice, data.unit.replace("g", "斤")));
            mTvEach.setText("");
            mTvPriceCatty.setText(Html.fromHtml("折合<font color=\"#E9464D\">" + data.price + "</font>/" + "份"));
            mTvEachWeight.setText("每份 :" + data.formatNum + data.unit);
            mTvIntegralDes.setText(Html.fromHtml("购买可获得<font color=\"#fb5f1b\">" + "</font>积分"));
            initBannerView(data);
        }
    }

    @Override
    public void onAddCart(boolean isSuccess, AddCartBean data) {
        if (isSuccess) {
            EventBus.getDefault().post(new OrderEvent());
            ToastUtils.showShortToast("添加购物车成功");
        }
    }

    /**
     * 设置banner图片
     *
     * @param data 数据
     */
    private void initBannerView(GoodsBean data) {
        mBeBanner.setImages(data.picture);
        mBeBanner.start();
        mBeBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_activity_goods_details_reduce:
                //数量-
                if (mGoodsNum <= 1) {
                    ToastUtils.showShortToast("数量不少于1");
                    return;
                }
                mGoodsNum--;
                mTvNum.setText(String.valueOf(mGoodsNum));
                break;
            case R.id.iv_activity_goods_details_add:
                //数量+
                mGoodsNum++;
                mTvNum.setText(String.valueOf(mGoodsNum));
                break;
            case R.id.tv_activity_goods_details_join_car:
                if (mGoodsNum == 0) {
                    ToastUtils.showShortToast("请添加数量");
                    return;
                }
                //加入购物车
                Map<String, String> map = new WeakHashMap<String, String>();
                map.put("goodsId", data.id);
                map.put("num", String.valueOf(mGoodsNum));
                YpCache.setMap(map);
                mPresenter.addCart(map);

                break;
        }
    }
}
