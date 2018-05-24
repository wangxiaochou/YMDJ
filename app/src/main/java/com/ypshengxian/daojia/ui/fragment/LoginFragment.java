package com.ypshengxian.daojia.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.LoginEvent;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.mvp.contract.ILoginContract;
import com.ypshengxian.daojia.mvp.presenter.LoginPresenter;
import com.ypshengxian.daojia.network.bean.LoginBean;
import com.ypshengxian.daojia.network.bean.SmsBean;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.task.ActivityStack;
import com.ypshengxian.daojia.ui.activity.RegisterActivity;
import com.ypshengxian.daojia.utils.RegexUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.ExtendEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author ZSH
 * @create 2018/3/25
 * @Describe 登录
 */
public class LoginFragment extends BaseMVPYpFreshFragment<ILoginContract.View, LoginPresenter> implements ILoginContract.View,
        View.OnClickListener {
    public static final int S_TYPE_ACCOUNT = 0;//账号密码登录
    public static final int S_TYPE_CODE = 1;//验证码登录
    public static final int S_QUIK_LOGIN = 123;//快捷登录

    private int mType = 0;
    private ExtendEditText mTvPhone;
    /**
     * 请输入密码
     */
    private ExtendEditText mTvPw;
    private ExtendEditText mTvCode;
    /**
     * 获取验证码
     */
    private TextView mTvGetCode;
    private LinearLayout mLlCode;
    /**
     * 忘记密码？
     */
    private TextView mTvForgetPw;
    /**
     * 注册
     */
    private TextView mTvRegister;
    /**
     * 登录
     */
    private TextView mTvLogin;

    private LinearLayout mLlWx;

    private IWXAPI mIWXAPI;
    /**
     * 用户账户
     */
    private String mUserPhone;
    /**
     * 密码
     */
    private String mPassword;
    /**
     * 是否要跳转到主页
     */
    private boolean isJumpMain = false;

    private String smsToken;


    public static LoginFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
        }
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_login;
    }


    @Override
    public void initView() {
        mTvPhone = (ExtendEditText) findViewById(R.id.tv_fragment_login_phone);
        mTvPw = (ExtendEditText) findViewById(R.id.tv_fragment_login_pw);
        mTvCode = (ExtendEditText) findViewById(R.id.tv_fragment_login_code);
        mTvGetCode = (TextView) findViewById(R.id.tv_fragment_login_get_code);
        mLlCode = (LinearLayout) findViewById(R.id.ll_fragment_login_code);
        mTvForgetPw = (TextView) findViewById(R.id.tv_fragment_login_forget_pw);
        mTvRegister = (TextView) findViewById(R.id.tv_fragment_login_register);
        mTvLogin = (TextView) findViewById(R.id.tv_fragment_login_login);
        mLlWx = (LinearLayout) findViewById(R.id.ll_fragment_login_wx);
        if (mType == S_TYPE_CODE) {
            mTvPw.setVisibility(View.GONE);
            mLlCode.setVisibility(View.VISIBLE);
        } else {
            mTvPw.setVisibility(View.VISIBLE);
            mLlCode.setVisibility(View.GONE);
        }
        mTvGetCode.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mTvForgetPw.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mLlWx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_fragment_login_get_code:
                //获取验证码
                if (regexStringCode()) {
                    Map<String, String> map = new WeakHashMap<String, String>();
                    map.put("phoneNumber", mUserPhone);
                    map.put("smsType", Count.SMS_LOGIN);
                    YpCache.setMap(map);
                    mPresenter.requestCode(map);
                }
                break;
            case R.id.tv_fragment_login_register:
                //注册
                Bundle registerBundle = new Bundle();
                registerBundle.putString(Count.FORGET_OR_REGISTER_TITLE, Count.REGISTER_TITLE);
                startActivityEx(RegisterActivity.class, registerBundle);
                break;
            case R.id.tv_fragment_login_forget_pw:
                //忘记密码
                Bundle forgetBundle = new Bundle();
                forgetBundle.putString(Count.FORGET_OR_REGISTER_TITLE, Count.FORGET_TITLE);
                startActivityEx(RegisterActivity.class, forgetBundle);
                break;
            case R.id.tv_fragment_login_login:
                if (mType == S_TYPE_CODE) {
                    if (regexStringCode()) {
                        //验证码登录
                        String mCode = mTvCode.getText().toString().trim();
                        if (TextUtils.isEmpty(mCode) || mCode.length() < 6) {
                            ToastUtils.showShortToast("请输入6位数字验证码！");
                            return;
                        }
                        mPresenter.requestData(mUserPhone, mCode, smsToken);
                    }
                } else {
                    //账号密码登录
                    if (regexStringAccount()) {
                        mPresenter.login(mUserPhone, mPassword);
                    }
                }

                break;
            case R.id.ll_fragment_login_wx:
                //微信登录
                mIWXAPI = WXAPIFactory.createWXAPI(getActivity(), Count.WX_APP_ID);
                mIWXAPI.registerApp(Count.WX_APP_ID);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = genNonceStr();
                mIWXAPI.sendReq(req);
                break;
        }
    }

    @Override
    public void responseCode(boolean isSuccess, SmsBean data) {
        if (isSuccess) {
            smsToken=data.smsToken;
            Observable.interval(0, 1, TimeUnit.SECONDS)
                    .compose(getBaseActivity().applySchedulers(ActivityEvent.DESTROY))
                    .take(Count.COUNTDOWN_TIME)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            int remain = Count.COUNTDOWN_TIME - 1 - aLong.intValue();
                            if (remain > 0) {
                                mTvGetCode.setEnabled(false);
                                mTvGetCode.setBackgroundResource(R.drawable.shape_gray_rectangle_soild_bg);
                                mTvGetCode.setTextColor(ResUtils.getColor(R.color.color_gray_login_hint_text));
                                mTvGetCode.setText("获取验证码(" + remain + ")");
                            } else {
                                mTvGetCode.setEnabled(true);
                                mTvGetCode.setBackgroundResource(R.drawable.shape_theme_color_bg);
                                mTvGetCode.setTextColor(ResUtils.getColor(R.color.color_white));
                                mTvGetCode.setText("获取验证码");
                            }
                        }
                    });
        }
    }

    @Override
    public void onLogin(boolean isSuccess, LoginBean data) {
        if (isSuccess) {
            YPPreference.newInstance().setUserToken(data.token);
            YPPreference.newInstance().setLogin(true);
            YPPreference.newInstance().setUserPhone(mUserPhone);
            EventBus.getDefault().post(new LoginEvent(true));
            EventBus.getDefault().post(new OrderEvent());
            ActivityStack.finishActivities();
        }
    }

    @Override
    public void onRequestData(boolean isSuccess, LoginBean data) {
        if (isSuccess) {
            YPPreference.newInstance().setUserToken(data.token);
            YPPreference.newInstance().setLogin(true);
            YPPreference.newInstance().setUserPhone(mUserPhone);
            EventBus.getDefault().post(new LoginEvent(true));
            EventBus.getDefault().post(new OrderEvent());
            ActivityStack.finishActivities();
        }
    }

    /**
     * 正则判断
     */
    private boolean regexStringCode() {
        mUserPhone = mTvPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mUserPhone)) {
            ToastUtils.showShortToast("请输入手机号！");
            return false;
        }
        if (!RegexUtils.isMobileExact(mUserPhone)) {
            ToastUtils.showShortToast("手机号不正确！");
            return false;
        }
        return true;
    }

    /**
     * 正则判断
     */
    private boolean regexStringAccount() {
        if (regexStringCode()) {
            mPassword = mTvPw.getText().toString().trim();
            if (TextUtils.isEmpty(mPassword)) {
                ToastUtils.showShortToast("请输入密码！");
                return false;
            }
            if (mPassword.length() < 6) {
                ToastUtils.showShortToast("密码需要6位及以上！");
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 随机字符串
     *
     * @return 随机数
     */
    private String genNonceStr() {
        Random r = new Random(System.currentTimeMillis());
        return (r.nextInt(10000) + System.currentTimeMillis()) + "";
    }
}
