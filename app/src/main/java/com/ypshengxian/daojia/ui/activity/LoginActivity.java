package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.JumpEvent;
import com.ypshengxian.daojia.event.LoginEvent;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.mvp.contract.ILoginActivityContract;
import com.ypshengxian.daojia.mvp.presenter.LoginActivityPresenter;
import com.ypshengxian.daojia.network.bean.LoginBean;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.task.ActivityStack;
import com.ypshengxian.daojia.ui.fragment.LoginFragment;
import com.ypshengxian.daojia.utils.MessageEvent;
import com.ypshengxian.daojia.utils.TitleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 登录
 */
public class LoginActivity extends BaseMVPYpFreshActicity<ILoginActivityContract.View, LoginActivityPresenter> implements
        ILoginActivityContract.View {

    public static final int S_QUIK_LOGIN = 123;
    //快捷登录

    private TextView mTvSimpleTitleBarLeftText;
    private FrameLayout mFralaySimpleTitleBarLeft;
    private TextView mTvSimpleTitleBarRightText;
    private FrameLayout mFralaySimpleTitleBarRight;
    /**  */
    private TextView mTvSimpleTitleBarTitle;
    private FrameLayout mFralaySimpleTitleBarCenter;
    private View mVSimpleTitleBarShadow;
    private RelativeLayout mRellaySimpleTitleBarBackground;
    private TabLayout mTl;
    private ViewPager mVp;
    private String jumpName;

    private String[] mTitle = {"账号密码登录", "手机验证码登录"};
    private Fragment[] mFragments = {LoginFragment.newInstance(LoginFragment.S_TYPE_ACCOUNT), LoginFragment.newInstance(LoginFragment.S_TYPE_CODE)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(null!=bundle){
            jumpName=bundle.getString(Count.JUMP_NAME);
        }
        EventBus.getDefault().register(this);
        initView();
        ActivityStack.addActivity(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }


    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.login_string_login_text));
        mTvSimpleTitleBarLeftText = (TextView) findViewById(R.id.tv_simple_title_bar_left_text);
        mFralaySimpleTitleBarLeft = (FrameLayout) findViewById(R.id.fralay_simple_title_bar_left);
        mTvSimpleTitleBarRightText = (TextView) findViewById(R.id.tv_simple_title_bar_right_text);
        mFralaySimpleTitleBarRight = (FrameLayout) findViewById(R.id.fralay_simple_title_bar_right);
        mTvSimpleTitleBarTitle = (TextView) findViewById(R.id.tv_simple_title_bar_title);
        mFralaySimpleTitleBarCenter = (FrameLayout) findViewById(R.id.fralay_simple_title_bar_center);
        mVSimpleTitleBarShadow = (View) findViewById(R.id.v_simple_title_bar_shadow);
        mRellaySimpleTitleBarBackground = (RelativeLayout) findViewById(R.id.rellay_simple_title_bar_background);
        mTl = (TabLayout) findViewById(R.id.tl_activity_login);
        mVp = (ViewPager) findViewById(R.id.vp_activity_login);

        mVp.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mTl.addTab(mTl.newTab());
        mTl.addTab(mTl.newTab());
        mTl.setupWithViewPager(mVp);
        for (int i = 0; i < mTitle.length; i++) {
            mTl.getTabAt(i).setText(mTitle[i]);
        }
    }

    /**
     * 微信登录消息
     *
     * @param event 消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCodeData(MessageEvent event) {
            mPresenter.wxLogin(event.getMessage(),"weixinmob");

    }

    /**
     * 登录消息
     *
     * @param event 消息
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onLogin(LoginEvent event) {
        if(event.isLogin()){
            EventBus.getDefault().post(new JumpEvent(jumpName));
        }
    }

    @Override
    public void onWxLogin(boolean isSuccess, LoginBean data) {
		if(isSuccess){
            YPPreference.newInstance().setUserToken(data.token);
            YPPreference.newInstance().setLogin(true);
            if (!TextUtils.isEmpty(data.user.verifiedMobile)) {
                EventBus.getDefault().post(new JumpEvent(jumpName));
                ActivityStack.finishActivities();
            }else {
                startActivityEx(BindingPhoneActivity.class);
            }

            EventBus.getDefault().post(new OrderEvent());
        }
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(TextUtils.isEmpty(jumpName)) {
            EventBus.getDefault().post(new JumpEvent(Count.JUMP_MAIN));
        }
        if(!YPPreference.newInstance().getLogin()){
            EventBus.getDefault().post(new JumpEvent(Count.JUMP_MAIN));
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected LoginActivityPresenter createPresenter() {
        return new LoginActivityPresenter();
    }

}
