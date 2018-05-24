package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.JumpEvent;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.mvp.contract.IMainContract;
import com.ypshengxian.daojia.mvp.presenter.MainPresenter;
import com.ypshengxian.daojia.network.bean.OrderBean;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.ui.fragment.CategoryFragment;
import com.ypshengxian.daojia.ui.fragment.ExtractFragment;
import com.ypshengxian.daojia.ui.fragment.HomeFragment;
import com.ypshengxian.daojia.ui.fragment.MineFragment;
import com.ypshengxian.daojia.ui.fragment.ShopCartFragment;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.PermissionUtil;
import com.ypshengxian.daojia.view.BadgeRadioButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 主界面
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class MainActivity extends BaseMVPYpFreshActicity<IMainContract.View, MainPresenter> implements
        RadioGroup.OnCheckedChangeListener, IMainContract.View,HomeFragment.OnNavClickListener {
    /** 首页 */
    private HomeFragment mHomeFragment = HomeFragment.newInstance();
    /** 分类 */
    private CategoryFragment mCategoryFragment = CategoryFragment.newInstance();
    /** 提取 */
    private ExtractFragment mExtractFragment = ExtractFragment.newInstance();
    /** 购物车 */
    private ShopCartFragment mCartFragment = ShopCartFragment.newInstance();
    /** 我的 */
    private MineFragment mMineFragment = MineFragment.newInstance();

    /** 容器 */
    private FrameLayout mFlMainContainer;
    /** 首页 */
    private BadgeRadioButton mRbMain0;
    /** 分类 */
    private BadgeRadioButton mRbMain1;
    /** 购物车 */
    private BadgeRadioButton mRbMain2;
    /** 提货 */
    private BadgeRadioButton mRbMain3;
    /** 我的 */
    private BadgeRadioButton mRbMain4;
    /** button的容器 */
    private RadioGroup mRgMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.getInstance().initPermissions(this);
        initView();
        EventBus.getDefault().register(this);
        if(YPPreference.newInstance().getLogin()) {
            Observable.create(new Observable.OnSubscribe<Integer>() {
                @Override
                public void call(Subscriber<? super Integer> subscriber) {
                    subscriber.onCompleted();
                }
            })
                    .delay(500, TimeUnit.MILLISECONDS)
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

        }
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mFlMainContainer = (FrameLayout) findViewById(R.id.fl_main_container);
        mRbMain0 = (BadgeRadioButton) findViewById(R.id.rb_main_0);
        mRbMain1 = (BadgeRadioButton) findViewById(R.id.rb_main_1);
        mRbMain2 = (BadgeRadioButton) findViewById(R.id.rb_main_2);
        mRbMain3 = (BadgeRadioButton) findViewById(R.id.rb_main_3);
        mRbMain4 = (BadgeRadioButton) findViewById(R.id.rb_main_4);
        mRgMain = (RadioGroup) findViewById(R.id.rg_main);
        onCheckedChanged(mRgMain, R.id.rb_main_0);
        mRgMain.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_main_0:
                showFragmentAtIndex(0);
                break;
            case R.id.rb_main_1:
                showFragmentAtIndex(1);
                break;
            case R.id.rb_main_2:
                showFragmentAtIndex(2);
                break;
            case R.id.rb_main_3:
                showFragmentAtIndex(3);
                break;
            case R.id.rb_main_4:
                showFragmentAtIndex(4);
                break;
            default:
                break;
        }


    }

    /**
     * 显示指定位置fragment
     *
     * @param index 位置
     */
    private void showFragmentAtIndex(int index) {
        switch (index) {
            case 0:
                showFragment(R.id.fl_main_container, mHomeFragment);
                hideFragment(mCategoryFragment);
                hideFragment(mMineFragment);
                hideFragment(mExtractFragment);
                hideFragment(mCartFragment);
                mHomeFragment.setNavClickListener(this);
                break;
            case 1:
                showFragment(R.id.fl_main_container, mCategoryFragment);
                hideFragment(mHomeFragment);
                hideFragment(mMineFragment);
                hideFragment(mExtractFragment);
                hideFragment(mCartFragment);

                break;
            case 2:
                if(YPPreference.newInstance().getLogin()) {
                    showFragment(R.id.fl_main_container, mCartFragment);
                    hideFragment(mCategoryFragment);
                    hideFragment(mMineFragment);
                    hideFragment(mExtractFragment);
                    hideFragment(mHomeFragment);
                }else {
                    Bundle cartBundle=new Bundle();
                    cartBundle.putString(Count.JUMP_NAME,Count.JUMP_CART);
                    startActivityEx(LoginActivity.class,cartBundle);
                }
                break;
            case 3:
                if(YPPreference.newInstance().getLogin()) {
                    showFragment(R.id.fl_main_container, mExtractFragment);
                    hideFragment(mCategoryFragment);
                    hideFragment(mMineFragment);
                    hideFragment(mCartFragment);
                    hideFragment(mHomeFragment);
                }else {
                    Bundle extractBundle=new Bundle();
                    extractBundle.putString(Count.JUMP_NAME,Count.JUMP_EXTRACT);
                    startActivityEx(LoginActivity.class,extractBundle);
                }
                break;
            case 4:
                if (YPPreference.newInstance().getLogin()) {
                    showFragment(R.id.fl_main_container, mMineFragment);
                    hideFragment(mCategoryFragment);
                    hideFragment(mCartFragment);
                    hideFragment(mExtractFragment);
                    hideFragment(mHomeFragment);
                }else {
                    Bundle mineBundle=new Bundle();
                    mineBundle.putString(Count.JUMP_NAME,Count.JUMP_MINE);
                    startActivityEx(LoginActivity.class,mineBundle);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 跳转通知
     *
     * @param event 消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jumpToWhere(JumpEvent event){
        String where=event.getTabName();
        LogUtils.e(where);
        switch (where){
            case Count.JUMP_CART:
                mRbMain2.setChecked(true);
                showFragmentAtIndex(2);
                break;
            case Count.JUMP_EXTRACT:
                mRbMain3.setChecked(true);
                showFragmentAtIndex(3);
                break;
            case Count.JUMP_MINE:
                mRbMain4.setChecked(true);
                showFragmentAtIndex(4);
                break;
            case Count.JUMP_MAIN:
               mRbMain0.setChecked(true);
                break;
            default:
                mRbMain0.setChecked(true);
                break;
        }
    }

    /**
     * 刷新底部数据
     *
     * @param event 消息
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public  void  onOrderEvent(OrderEvent event){
        mPresenter.requestData();
    }

    @Override
    protected void setAppExit(int second, String message) {
        super.setAppExit(3, "再按一次退出");
    }

    @Override
    public void onResponseData(boolean isSuccess, OrderBean data) {
          if(isSuccess){
              mRbMain2.setBadgeNumber(data.cart);
              mRbMain3.setBadgeNumber(data.paid);
          }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onNavClick(String link, String title) {
        mRbMain1.setChecked(true);
        //mRbMain1
         //延时500毫秒执行 防止第一次加载不过去
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onCompleted();
            }
        })
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mCategoryFragment.setOnNavClick(link);
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
