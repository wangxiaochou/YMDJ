package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jungly.gridpasswordview.GridPasswordView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.ISetPaymentPasswordContract;
import com.ypshengxian.daojia.mvp.presenter.SetPaymentPasswordPresenter;
import com.ypshengxian.daojia.network.bean.SmsBean;
import com.ypshengxian.daojia.preference.YPPreference;
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
 * @Describe 设置支付密码
 */
public class SetPaymentPasswordActivity extends BaseMVPYpFreshActicity<ISetPaymentPasswordContract.View, SetPaymentPasswordPresenter> implements
        ISetPaymentPasswordContract.View, View.OnClickListener {

    private ExtendEditText mTvCode;
    /**
     * 获取验证码
     */
    private TextView mTvGetCode;
    private TextView mTvConfirm;
    private GridPasswordView mPetInput;

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
    private String smsToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_set_payment_password;
    }

    @Override
    protected SetPaymentPasswordPresenter createPresenter() {
        return new SetPaymentPasswordPresenter();
    }

    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.set_payment_password_title_text));
        mUserPhone = YPPreference.newInstance().getUserPhone();

        mTvCode = (ExtendEditText) findViewById(R.id.tv_activity_set_payment_password_code);
        mTvGetCode = (TextView) findViewById(R.id.tv_activity_set_payment_password_get_code);
        mPetInput = (GridPasswordView) findViewById(R.id.pet_activity_set_payment_password_input);
        mTvConfirm = (TextView) findViewById(R.id.tv_activity_set_payment_password_confirm);
        mTvGetCode.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        initEvent();
    }

    private void initEvent() {
        mPetInput.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw != null) {
                    mUserPass = psw;
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    /**
     * 正则判断
     */
    private boolean regexStringCode() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_set_payment_password_get_code:
                //获取验证码
                if (regexStringCode()) {
                    Map<String, String> map = new WeakHashMap<String, String>();
                    map.put("phoneNumber", mUserPhone);
                    map.put("smsType", Count.SMS_FORGET_PAY_PASSWORD);
                    //只要传参的GET请求
                    YpCache.setMap(map);
                    mPresenter.getCode(map);
                }
                break;
            case R.id.tv_activity_set_payment_password_confirm:
                mCode = mTvCode.getText().toString();
                if (TextUtils.isEmpty(mCode) || mCode.length() < 6) {
                    ToastUtils.showShortToast("请输入6位数字验证码！");
                    break;
                }
                if (TextUtils.isEmpty(mUserPass) || mUserPass.length() < 6) {
                    ToastUtils.showShortToast("请输入6位数字支付密码！");
                    break;
                }
                mPresenter.requestData(mUserPhone, mCode, mUserPass,smsToken);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseData(boolean isSuccess, Object data) {
        if (isSuccess) {
            ToastUtils.showShortToast("支付密码设置成功！");
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

}
