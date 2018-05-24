package com.ypshengxian.daojia.ui.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.ShopIdEvent;
import com.ypshengxian.daojia.event.ShopNameEvent;
import com.ypshengxian.daojia.mvp.contract.IChooseStoreContract;
import com.ypshengxian.daojia.mvp.presenter.ChooseStorePresenter;
import com.ypshengxian.daojia.network.bean.CityAndShopBean;
import com.ypshengxian.daojia.network.bean.CityBean;
import com.ypshengxian.daojia.network.bean.ShopBean;
import com.ypshengxian.daojia.network.bean.ShopSmallBean;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TencentLocationUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.utils.ViewUtils;
import com.ypshengxian.daojia.view.RecyclerDivider;
import com.ypshengxian.daojia.view.SimpleTitleBarBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 选择门店
 */
public class ChooseStoreActivity extends BaseMVPYpFreshActicity<IChooseStoreContract.View, ChooseStorePresenter> implements
        IChooseStoreContract.View {

    private MapView mMv;
    private RecyclerView mRvArea;
    private RecyclerView mRlStore;

    private TencentMap mTencentMap;
    private UiSettings mUiSettings;
    private Marker marker;
    /** 城市名称 */
    private String mCityName;
    /** 经度 */
    private double mLongitude;
    /** 纬度 */
    private double mLatitude;
    /** 门店数据源 */
    private List<ShopSmallBean> mShopBeans = new ArrayList<ShopSmallBean>();
    /** 门店适配器 */
    private BaseQuickAdapter<ShopSmallBean, BaseViewHolder> mShopAdapter;
    /** 城市列表 */
    private List<CityBean> mCityBeans = new ArrayList<CityBean>();
    /** 城市适配器 */
    private BaseQuickAdapter<CityBean, BaseViewHolder> mCityAdapter;


    private TextView tvCenterCoordinate;
    /** 城市数据源 */
    private ArrayList<CityBean> cityBeans = new ArrayList<CityBean>();
    /** 地址 */
    private String mAddress;

    /** 标题栏 */
    private SimpleTitleBarBuilder simpleTitleBarBuilder;
    /** 城市 */
    private TextView mCitytext;
    /** 地址容器 */
    private LinearLayout mCityLayout;
    /** 门店列表 */
    private ArrayList<ShopBean> shopBeans;
    /** 需要显示的城市数据源 */
    private ArrayList<CityBean> needCity = new ArrayList<CityBean>();
    /** 需要显示的数据源 */
    private ArrayList<ShopBean> needShop = new ArrayList<ShopBean>();
    /** 城市下所有的店铺 */
    private ArrayList<ShopBean> childShops = new ArrayList<ShopBean>();
    /** 当前显示的店铺数据源 */
    private ArrayList<ShopSmallBean> shopSmallBeans = new ArrayList<ShopSmallBean>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        mPresenter.getCity();
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
                        mPresenter.requestData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                    }
                });

        TencentLocationUtils.newInstance().startService(new TencentLocationUtils.ILocationResult() {
            @Override
            public void onReceiveLocation(TencentLocation location) {
                mCityName = location.getCity();
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mAddress = location.getAddress();
                initLocationClient();
                mCitytext.setText(mCityName);

            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_choose_store;
    }

    @Override
    protected ChooseStorePresenter createPresenter() {
        return new ChooseStorePresenter();
    }


    //R.layout.item_choose_store_area_rv
    // R.layout.item_choose_store_store_rv

    /**
     * 初始化视图
     */
    private void initView() {
        simpleTitleBarBuilder = SimpleTitleBarBuilder.attach(this)
                .setTitleText("选择城市")
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .inflateRightView(R.layout.city_city_right)
                .setBackgroundResource(R.color.color_theme)
                .setShadowHeight(0)
                .setLeftClickAsBack();
        mCitytext = simpleTitleBarBuilder.findViewById(R.id.tv_city_right_city);
        mCityLayout = simpleTitleBarBuilder.findViewById(R.id.ll_city_right_layout);
        mCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Count.CITY, cityBeans);
                bundle.putParcelableArrayList(Count.HAS_SHOP_CITY,shopBeans);
                startActivityEx(ChooseCityActivity.class, bundle);
            }
        });


        mMv = (MapView) findViewById(R.id.mv_activity_choose_store);
        mRvArea = (RecyclerView) findViewById(R.id.rv_activity_choose_store_area);
        mRvArea.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mCityAdapter = new BaseQuickAdapter<CityBean, BaseViewHolder>(R.layout.item_choose_store_area_rv, needCity) {
            @Override
            protected void convert(BaseViewHolder helper, CityBean item) {
                if (item.isChecked) {
                    helper.setBackgroundColor(R.id.tv_item_choose_store_area, ResUtils.getColor(R.color.color_white));
                    helper.setTextColor(R.id.tv_item_choose_store_area, ResUtils.getColor(R.color.preference_count));

                } else {
                    helper.setBackgroundColor(R.id.tv_item_choose_store_area, ResUtils.getColor(R.color.color_background));
                    helper.setTextColor(R.id.tv_item_choose_store_area, ResUtils.getColor(R.color.preference_name));
                }
                helper.setText(R.id.tv_item_choose_store_area, item.name);
            }
        };
        mCityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                checkShop(needCity.get(position).id);
                for (int i = 0; i < needCity.size(); i++) {
                    if (position == i) {
                        needCity.get(i).isChecked = true;
                    } else {
                        needCity.get(i).isChecked = false;
                    }
                }
                mCityAdapter.setNewData(needCity);
                if (null != shopSmallBeans && shopSmallBeans.size() > 0) {
                    setMark(shopSmallBeans.get(0));
                }
            }
        });
        mRvArea.setAdapter(mCityAdapter);
        mRlStore = (RecyclerView) findViewById(R.id.rl_activity_choose_store_store);
        mRlStore.setLayoutManager(new LinearLayoutManager(Utils.getContext()));
        mShopAdapter = new BaseQuickAdapter<ShopSmallBean, BaseViewHolder>(R.layout.item_choose_store_store_rv, shopSmallBeans) {
            @Override
            protected void convert(BaseViewHolder helper, ShopSmallBean item) {
                helper.setChecked(R.id.cb_item_choose_store_store_selected, item.isChecked);
                if (item.isChecked) {
                    helper.setTextColor(R.id.tv_item_choose_store_store_title, ResUtils.getColor(R.color.preference_count));
                    helper.setTextColor(R.id.tv_item_choose_store_store_distance, ResUtils.getColor(R.color.preference_count));
                } else {
                    helper.setTextColor(R.id.tv_item_choose_store_store_title, ResUtils.getColor(R.color.preference_name));
                    helper.setTextColor(R.id.tv_item_choose_store_store_distance, ResUtils.getColor(R.color.preference_name));
                }
                helper.setText(R.id.tv_item_choose_store_store_title, item.name);
                helper.setText(R.id.tv_item_choose_store_store_distance,
                        String.format(ResUtils.getString(R.string.choose_distance_text), item.distance));
                helper.addOnClickListener(R.id.cb_item_choose_store_store_selected);
                ViewUtils.setTouchDelegate(helper.getView(R.id.cb_item_choose_store_store_selected),50);

            }
        };
        mRlStore.addItemDecoration(new RecyclerDivider(LinearLayoutManager.VERTICAL));
        mRlStore.setAdapter(mShopAdapter);
        mShopAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.cb_item_choose_store_store_selected:
                        setMark(shopSmallBeans.get(position));
                        YPPreference.newInstance().setShopId(shopSmallBeans.get(position).id);
                        YPPreference.newInstance().setShopName(shopSmallBeans.get(position).name);
                        YPPreference.newInstance().setChooseShopTwo(shopSmallBeans.get(position).secCityId);
                        YPPreference.newInstance().setChooseShopTheree(shopSmallBeans.get(position).trdCityId);
                        EventBus.getDefault().post(new ShopNameEvent(shopSmallBeans.get(position).name));

                        for (int i = 0; i < shopSmallBeans.size(); i++) {
                            if (i == position) {
                                shopSmallBeans.get(i).isChecked = true;
                            } else {
                                shopSmallBeans.get(i).isChecked = false;
                            }
                        }
                        mShopAdapter.setNewData(shopSmallBeans);
                        break;
                    default:
                        break;
                }


            }
        });


    }

    /**
     * 设置图标显示位置
     */
    private void initLocationClient() {
        mTencentMap = mMv.getMap();
        mUiSettings = mTencentMap.getUiSettings();

        mUiSettings.setLogoPosition(0);

        //注册定位监听
//		mMyLocationSource = new MyLocationSource(this);
//		mTencentMap.setLocationSource(mMyLocationSource);
//		mTencentMap.setMyLocationEnabled(true);

    }

    /**
     * 设置地图中心点
     *
     * @param lat
     * @param lon
     */
    private void setCenterPoint(double lat, double lon) {
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(lat, lon), //新的中心点坐标
                        15,  //新的缩放级别
                        0f, //俯仰角 0~45° (垂直地图时为0)
                        0f)); //偏航角 0~360° (正北方为0)
        //移动地图
        mTencentMap.moveCamera(cameraSigma);
    }

    /**
     * 添加marker
     *
     * @param lat
     * @param lon
     */
    private void addMarker(double lat, double lon, String title, String dis) {
        LatLng mLatLng = new LatLng(lat, lon);
        mTencentMap.moveCamera(CameraUpdateFactory.zoomTo(13));
//		mTencentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(NE, new LatLng(39.906901, 116.397972)), 5));
        mTencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(mLatLng, 11, 0, 0)));
        mTencentMap.addCircle(new CircleOptions().center(mLatLng).radius(5));
        marker = mTencentMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_baidumap_geo))
                .position(mLatLng));
        marker.setRotation(0);
        marker.setTitle(title);
        marker.setSnippet(dis);
        marker.showInfoWindow();
    }

    /**
     * 添加marker并自定义信息窗样式
     *
     * @param lat 纬度
     * @param lon 经度
     * @param bean 门店信息
     */
    private void addMarkerCustomInfowindow(double lat, double lon, String title,ShopSmallBean bean) {
        //标注坐标
        LatLng latLng = new LatLng(lat, lon);
        final Marker mMarker = mTencentMap.addMarker(new MarkerOptions().
                        position(latLng).
                        title(title).
                        snippet(title).
                        visible(true)
                /*.visible(true)*/);
        //创建图标
        mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_baidumap_geo));
        mMarker.setTag(bean);
        TencentMap.InfoWindowAdapter infoWindowAdapter = new TencentMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {//返回View为信息窗自定义样式，返回null时为默认信息窗样式
                LinearLayout infoWindow = (LinearLayout) View.inflate(ChooseStoreActivity.this,
                        R.layout.layout_tencentmap_custom_infowindow, null);
                TextView mTitle = infoWindow.findViewById(R.id.tv_layout_tencent_map_cust_infowindow_title);
                TextView mDiscribe = infoWindow.findViewById(R.id.tv_layout_tencent_map_cust_infowindow_content);
                mTitle.setText(marker.getTitle());
                mDiscribe.setText(marker.getSnippet());
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {//返回View为信息窗内容自定义样式，返回null时为默认信息窗样式
                return null;
            }
        };
        mTencentMap.setInfoWindowAdapter(infoWindowAdapter);
        mMarker.showInfoWindow();

        mTencentMap.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ShopSmallBean shopSmallBean=(ShopSmallBean) marker.getTag();
                Bundle bundle=new Bundle();
                bundle.putParcelable(Count.STORE_DETAILS,shopSmallBean);
                startActivityEx(StoreDetailsActivity.class,bundle);
            }

            @Override
            public void onInfoWindowClickLocation(int i, int i1, int i2, int i3) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMv != null) {
            mMv.onStart();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mMv != null) {
            mMv.onDestroy();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMv != null) {
            mMv.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMv != null) {
            mMv.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMv != null) {
            mMv.onStop();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mMv != null) {
            mMv.onRestart();
        }
    }

    @Override
    public void onResponseData(boolean isSuccess, ArrayList<ShopBean> data) {
        if (isSuccess) {
            shopBeans = data;
            Collections.sort(shopBeans, new Comparator<ShopBean>() {
                @Override
                public int compare(ShopBean o1, ShopBean o2) {
                    float[] f1 = new float[1];
                    Location.distanceBetween(mLatitude, mLongitude, Double.valueOf(o1.lat), Double.valueOf(o1.lng), f1);
                    float[] f2 = new float[1];
                    Location.distanceBetween(mLatitude, mLongitude, Double.valueOf(o2.lat), Double.valueOf(o2.lng), f2);
                    return (int) f1[0] - (int) f2[0];
                }
            });

        }

    }

    @Override
    public void onGetCity(boolean isSuccess, ArrayList<CityBean> data) {
        if (isSuccess) {
            cityBeans = data;
            getHasShopList(cityBeans, "11589");
        }
    }


    /**
     * 获得包含选中店铺的城市信息
     *
     * @param twoShopId 当前选择的二级城市ID
     *
     */

    private void getHasShopList(ArrayList<CityBean> cityBeans,String twoShopId){
        getHasShopList(cityBeans,twoShopId,"","");
    }

    /**
     * 获得包含选中店铺的城市信息
     *
     * @param twoShopId 当前选择的二级城市ID
     * @param cityName 当前定位城市
     * @param checkThreeCityId 当前选择城市的三级Id
     * @param cityBeans 城市
     */
    private void getHasShopList(ArrayList<CityBean> cityBeans, String twoShopId, String cityName, String checkThreeCityId) {
        Observable.create(new Observable.OnSubscribe<CityAndShopBean>() {
            @Override
            public void call(Subscriber<? super CityAndShopBean> subscriber) {

                //获得当前选中的店铺的城市列表
                List<CityBean> checkCity = new ArrayList<>();
                for (CityBean bean : cityBeans) {
                    if (bean.parentId.equals(twoShopId)) {
                        checkCity.add(bean);
                    }
                }

                //获得当前城市下所有的3级ID
                childShops = new ArrayList<ShopBean>();
                for (ShopBean bean : shopBeans) {
                    if (twoShopId.equals(bean.secCityId)) {
                        childShops.add(bean);
                    }
                }
                //获得3ID  对应的城市
                Set<CityBean> set = new HashSet<CityBean>();
                for (ShopBean shopBean : childShops) {
                    for (CityBean bean : checkCity) {
                        if (bean.id.equals(shopBean.trdCityId)) {
                            set.add(bean);
                            LogUtils.e(bean.name + "当前城市名称");
                        }
                    }
                }
                needCity.addAll(set);
                needCity.get(0).isChecked = true;

//                if (TextUtils.isEmpty(checkThreeCityId)) {
//                    checkThreeCityId = needCity.get(0).id;
//                }


                //获得所有的选中城市的ID
                needShop = new ArrayList<ShopBean>();
                for (ShopBean shopBean : childShops) {
                    if (shopBean.trdCityId.equals(needCity.get(0).id)) {
                        LogUtils.e(shopBean.name + "当前店铺的名称");
                        needShop.add(shopBean);
                    }
                }
                //获得所有的店铺
                Set<ShopSmallBean> smallSet = new HashSet<ShopSmallBean>();
                for (ShopBean bean : needShop) {
                    float[] f1 = new float[1];
                    Location.distanceBetween(mLatitude, mLongitude, Double.valueOf(bean.lat), Double.valueOf(bean.lng), f1);
                    double f = f1[0];
                    smallSet.add(new ShopSmallBean(bean.name, f, bean.id, bean.secCityId, bean.trdCityId, bean.lat, bean.lng));
                }
                shopSmallBeans.addAll(smallSet);
                Collections.sort(shopSmallBeans, new Comparator<ShopSmallBean>() {
                    @Override
                    public int compare(ShopSmallBean o1, ShopSmallBean o2) {
                        return (int) o1.distance - (int) o2.distance;
                    }
                });
                for (ShopSmallBean bean : shopSmallBeans) {
                    if (TextUtils.equals(YPPreference.newInstance().getShopName(), bean.name)) {
                        bean.isChecked = true;
                    } else {
                        bean.isChecked = false;
                    }
                }

                CityAndShopBean cityAndShopBean = new CityAndShopBean(needCity, shopSmallBeans);
                subscriber.onNext(cityAndShopBean);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CityAndShopBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CityAndShopBean cityAndShopBean) {
                        LogUtils.e(cityAndShopBean.cityBeans.size());
                        mCityAdapter.setNewData(cityAndShopBean.cityBeans);
                        mShopAdapter.setNewData(cityAndShopBean.shopSmallBeans);

                        setMark(cityAndShopBean.shopSmallBeans.get(0));
                    }
                });

    }

    /**
     * 设置mark
     */
    private void setMark(ShopSmallBean bean) {
        float lat = Float.valueOf(bean.lat);
        float lng = Float.valueOf(bean.lng);
        addMarkerCustomInfowindow(lat, lng, bean.name,bean);

        setCenterPoint(lat, lng);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetShopId(ShopIdEvent event){
        if(TextUtils.isEmpty(event.shopTwoId)){
            return;
        }
        for (CityBean bean:needCity){
            bean.isChecked=false;
        }
        mCityAdapter.setNewData(needCity);
        needCity.clear();
        shopSmallBeans.clear();
        getHasShopList(cityBeans,event.shopTwoId);
    }

    /**
     * 选择城市
     *
     * @param cityId 城市的ID
     */
    private void checkShop(String cityId) {

        Observable.create(new Observable.OnSubscribe<ArrayList<ShopSmallBean>>() {
            @Override
            public void call(Subscriber<? super ArrayList<ShopSmallBean>> subscriber) {
                //获得所有的选中城市的ID
                needShop = new ArrayList<ShopBean>();
                for (ShopBean shopBean : childShops) {
                    if (shopBean.trdCityId.equals(cityId)) {
                        LogUtils.e(shopBean.name + "当前店铺的名称");
                        needShop.add(shopBean);
                    }
                }
                //获得所有的店铺
                shopSmallBeans = new ArrayList<ShopSmallBean>();
                for (ShopBean bean : needShop) {
                    float[] f1 = new float[1];
                    Location.distanceBetween(mLatitude, mLongitude, Double.valueOf(bean.lat), Double.valueOf(bean.lng), f1);
                    LogUtils.e("原生计算" + f1[0]);
                    double f = f1[0];
                    LogUtils.e("当前计算" + f);
                    shopSmallBeans.add(new ShopSmallBean(bean.name, f, bean.id, bean.secCityId, bean.trdCityId, bean.lat, bean.lng));
                }
                Collections.sort(shopSmallBeans, new Comparator<ShopSmallBean>() {
                    @Override
                    public int compare(ShopSmallBean o1, ShopSmallBean o2) {
                        return (int) o1.distance - (int) o2.distance;
                    }
                });
                subscriber.onNext(shopSmallBeans);

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<ShopSmallBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<ShopSmallBean> shops) {
                        shopSmallBeans = new ArrayList<ShopSmallBean>();
                        shopSmallBeans.addAll(shops);

                        for (ShopSmallBean bean : shopSmallBeans) {
                            if (TextUtils.equals(YPPreference.newInstance().getShopName(), bean.name)) {
                                bean.isChecked = true;
                            } else {
                                bean.isChecked = false;
                            }
                        }
                        setMark(shopSmallBeans.get(0));
                        mShopAdapter.setNewData(shopSmallBeans);
                    }
                });


    }
}
