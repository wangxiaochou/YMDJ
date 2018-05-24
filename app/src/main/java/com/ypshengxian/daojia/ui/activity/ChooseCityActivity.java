package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.adapter.ChooseCityHeaderAdapter;
import com.ypshengxian.daojia.adapter.CityAdapter;
import com.ypshengxian.daojia.base.BaseYpFreshActivity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.ShopIdEvent;
import com.ypshengxian.daojia.network.bean.CityBean;
import com.ypshengxian.daojia.network.bean.ShopBean;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 选择城市
 *
 * @author Yan
 * @date 2018-04-01
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ChooseCityActivity extends BaseYpFreshActivity {

    /** 城市列表 */
    private IndexableLayout mIlCount;
    /** 城市适配器 */
    private CityAdapter mAdapter;
    /** 城市数据源 */
    private ArrayList<CityBean> cityBeans = new ArrayList<CityBean>();
    /** 店铺数据源 */
    private ArrayList<ShopBean> shopBeans = new ArrayList<ShopBean>();
    /** 头部布局 */
    private ChooseCityHeaderAdapter mCityHeaderAdapter;
    /** 热门城市适配器 */
    private BaseQuickAdapter<CityBean, BaseViewHolder> hotAdapter;
    /** 热门城市数据源 ,最大为10 */
    private List<CityBean> hotList = new ArrayList<CityBean>(10);
    /** 头部为1,只能加入一个数据 */
    private List<String> hotName = new ArrayList<String>(1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            cityBeans = bundle.getParcelableArrayList(Count.CITY);
            shopBeans = bundle.getParcelableArrayList(Count.HAS_SHOP_CITY);
        }
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_choose_city;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        TitleUtils.setTitleBar(this, "选择城市");
        mIlCount = (IndexableLayout) findViewById(R.id.il_activity_count);
        mIlCount.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mAdapter = new CityAdapter(Utils.getContext());
        mIlCount.setAdapter(mAdapter);
        mIlCount.setOverlayStyle_Center();
        mAdapter.setDatas(cityBeans);
        mIlCount.setCompareMode(IndexableLayout.MODE_FAST);
        hotName.add("合肥市");
        mCityHeaderAdapter = new ChooseCityHeaderAdapter(Utils.getContext(), "↑", null, hotName) {
            @Override
            protected void convert(BaseViewHolder holder, String data, int position) {
                holder.setText(R.id.item_header_city_dw, data);
                RecyclerView recyclerView = holder.getView(R.id.item_header_city_gridview);
                recyclerView.setLayoutManager(new GridLayoutManager(Utils.getContext(), 3));
                hotAdapter = new BaseQuickAdapter<CityBean, BaseViewHolder>(R.layout.item_gridview_cyb_change_city, hotList) {
                    @Override
                    protected void convert(BaseViewHolder helper, CityBean item) {
                        helper.setText(R.id.item_gridview_cyb_change_city_tv, item.name);
                    }
                };
                recyclerView.setAdapter(hotAdapter);
                hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (!isHasShop(hotList.get(position).id)) {
                            ToastUtils.showShortToast("当前城市暂未开通店铺");
                        } else {
                            //开通的城市
                            chooseShopTwoId(hotList.get(position).id);
                        }
                    }
                });
            }
        };


        mIlCount.addHeaderAdapter(mCityHeaderAdapter);
        mAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CityBean>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CityBean entity) {
                LogUtils.e(entity.name + entity.id);
                if (!isHasShop(entity.id)) {
                    ToastUtils.showShortToast("当前城市暂未开通店铺");
                } else {
                    //开通的城市
                    chooseShopTwoId(entity.id);
                }
            }
        });
        initHotData();


    }

    /**
     * 设置热门城市数据
     */
    private void initHotData() {
        Observable.create(new Observable.OnSubscribe<List<CityBean>>() {
            @Override
            public void call(Subscriber<? super List<CityBean>> subscriber) {
                List<CityBean> beans = new ArrayList<CityBean>();
                for (CityBean bean : cityBeans) {
                    if (TextUtils.equals("0", bean.parentId)) {
                        if (beans.size() > 9) {
                            break;
                        }
                        beans.add(bean);
                    }
                }


                subscriber.onNext(beans);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CityBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CityBean> cityBeans) {
                        hotList = cityBeans;
                        hotAdapter.setNewData(hotList);
                    }
                });

    }

    /**
     * 筛选当前城市下的店铺二级ID 回传给上一个界面
     *
     * @param id 当前选择城市的Id
     */
    private void chooseShopTwoId(String id) {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                ArrayList<ShopBean> shops = new ArrayList<ShopBean>();
                for (ShopBean bean : shopBeans) {
                    if (TextUtils.equals(id, bean.rootCityId)
                            || TextUtils.equals(id, bean.secCityId)
                            || TextUtils.equals(id, bean.trdCityId)) {
                        shops.add(bean);
                    }
                }
                subscriber.onNext(shops.get(0).secCityId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        EventBus.getDefault().post(new ShopIdEvent(s));
                        finish();
                    }
                });
    }

    /**
     * 当前选中城市是否含有店铺
     *
     * @param id 点击城市的ID
     */
    private boolean isHasShop(String id) {
        if (null == shopBeans) {
            return false;
        }
        if (shopBeans.size() <= 0) {
            return false;
        }
        for (ShopBean bean : shopBeans) {
            if (TextUtils.equals(id, bean.rootCityId)
                    || TextUtils.equals(id, bean.secCityId)
                    || TextUtils.equals(id, bean.trdCityId)) {
                return true;
            }
        }

        return false;
    }

}
