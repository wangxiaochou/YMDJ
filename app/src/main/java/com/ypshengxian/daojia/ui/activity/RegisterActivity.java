package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.IRegisterContract;
import com.ypshengxian.daojia.mvp.presenter.RegisterPresenter;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.RegisterBean;
import com.ypshengxian.daojia.network.bean.SmsBean;
import com.ypshengxian.daojia.task.ActivityStack;
import com.ypshengxian.daojia.utils.RegexUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.ExtendEditText;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 注册/找回密码
 */
public class RegisterActivity extends BaseMVPYpFreshActicity<IRegisterContract.View, RegisterPresenter> implements IRegisterContract.View, View.OnClickListener {

    /**
     * 请输入手机号
     */
    private ExtendEditText mTvPhone;
    /**
     * 请输入验证码
     */
    private ExtendEditText mTvCode;
    /**
     * 请输入密码
     */
    private ExtendEditText mTvPw;
    /**
     * 确认密码
     */
    private ExtendEditText mTvConfirmPw;
    /**
     * 确定
     */
    private TextView mTvConfirm;
    /**
     * 获取验证码
     */
    private TextView mTvGetCode;

    /**
     * 用户手机号
     */
    private String mUserPhone = "";
    /**
     * 验证码
     */
    private String mCode = "";
    /**
     * 密码
     */
    private String mUserPass = "";
    /**
     * 验证密码
     */
    private String mUserPassAgin = "";
    /**
     * 界面的Title
     */
    private String mTitle;
    private String smsToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mTitle = bundle.getString(Count.FORGET_OR_REGISTER_TITLE);
        }
        initView();
        ActivityStack.addActivity(this);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    private void initView() {
        TitleUtils.setTitleBar(this, mTitle);
        mTvPhone = (ExtendEditText) findViewById(R.id.tv_activity_register_phone);
        mTvCode = (ExtendEditText) findViewById(R.id.tv_activity_register_code);
        mTvPw = (ExtendEditText) findViewById(R.id.tv_activity_register_pw);
        mTvConfirmPw = (ExtendEditText) findViewById(R.id.tv_activity_register_confirm_pw);
        mTvConfirm = (TextView) findViewById(R.id.tv_activity_register_confirm);
        mTvGetCode = (TextView) findViewById(R.id.tv_activity_register_get_code);
        mTvGetCode.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        switch (mTitle) {
            case Count.FORGET_TITLE:
                mTvConfirm.setText("确认");
                break;
            case Count.REGISTER_TITLE:
                mTvConfirm.setText("注册");
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseData(boolean isSuccess, RegisterBean data) {
        if (isSuccess) {
            finish();
        }
    }

    @Override
    public void onResponseCode(boolean isSuccess, SmsBean data) {
        if (isSuccess) {
            smsToken=data.smsToken;
            Observable.interval(0, 1, TimeUnit.SECONDS)
                    .compose(getBaseActivity().<Long>applySchedulers(ActivityEvent.DESTROY))
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_register_get_code:
                //获取验证码
                if (regexStringCode()) {
                    Map<String, String> map = new WeakHashMap<String, String>();
                    map.put("phoneNumber", mUserPhone);
                    switch (mTitle) {
                        case Count.FORGET_TITLE:
                            map.put("smsType", Count.SMS_FORGET_PASSWORD);
                            break;
                        case Count.REGISTER_TITLE:
                            map.put("smsType", Count.SMS_REGISTRATION);
                            break;
                        default:
                            break;
                    }

                    //只要传参的GET请求
                    YpCache.setMap(map);

                    mPresenter.getCode(map);
                }
                break;
            case R.id.tv_activity_register_confirm:
                if (regexStringRegist() && regexStringCode()) {
                    //确定
                    switch (mTitle) {
                        case Count.FORGET_TITLE:
                            mPresenter.resetBySms(mUserPass, mUserPhone, mCode,smsToken);
                            break;
                        case Count.REGISTER_TITLE:
                            mPresenter.bindPhone(mUserPass, mUserPhone, mUserPhone, mCode, Count.OS_NAME,smsToken);
                            break;
                        default:
                            break;
                    }
                }

                break;
            default:
                break;

        }
    }

    /**
     * 正则判断
     */
    private boolean regexStringCode() {
        mUserPhone = mTvPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mUserPhone)) {
            ToastUtils.showShortToast("手机号不能为空！");
            return false;
        }
        if (!RegexUtils.isMobileExact(mUserPhone)) {
            ToastUtils.showShortToast("请输入正确的手机号！");
            return false;
        }
        return true;
    }

    /**
     * 正则判断
     */
    private boolean regexStringRegist() {
        if (regexStringCode()) {
            mCode = mTvCode.getText().toString().trim();
            mUserPass = mTvPw.getText().toString().trim();
            mUserPassAgin = mTvConfirmPw.getText().toString().trim();
            if (TextUtils.isEmpty(mUserPass)) {
                ToastUtils.showShortToast("请输入6位及以上密码！");
                return false;
            }
            if (mUserPass.length() < 6) {
                ToastUtils.showShortToast("密码需要6位及以上！");
                return false;
            }
            if (TextUtils.isEmpty(mUserPassAgin) || mUserPassAgin.length() < 6) {
                ToastUtils.showShortToast("请输入6位及以上确认密码！");
                return false;
            }
            if (!TextUtils.equals(mUserPass, mUserPassAgin)) {
                ToastUtils.showShortToast("两次输入的密码不一致！");
                return false;
            }

            if (TextUtils.isEmpty(mCode) || mCode.length() < 6) {
                ToastUtils.showShortToast("请输入6位数字验证码！");
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onResponseResetBySms(boolean isSuccess, BaseModuleApiResult data) {
        if (isSuccess) {
            ToastUtils.showShortToast("密码修改成功！");
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
