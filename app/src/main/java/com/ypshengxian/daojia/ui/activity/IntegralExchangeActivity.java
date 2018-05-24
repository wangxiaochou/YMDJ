package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IIntegralExchangeContract;
import com.ypshengxian.daojia.mvp.presenter.IntegralExchangePresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 账户余额兑换
 */
public class IntegralExchangeActivity extends BaseMVPYpFreshActicity<IIntegralExchangeContract.View, IntegralExchangePresenter> implements IIntegralExchangeContract.View, View.OnClickListener {

    /**
     * 请输入兑换金额
     */
    private EditText mEtExchangeAmount;
    /**
     * 确定
     */
    private TextView mTvConfirm;

    @Override
    public void onResponseData(boolean isSuccess, Object data) {

    }

    @Override
    protected IntegralExchangePresenter createPresenter() {
        return new IntegralExchangePresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_integral_exchange;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.integral_exchange_title_text));
        mEtExchangeAmount = (EditText) findViewById(R.id.et_activity_integral_exchange_amount);
        mTvConfirm = (TextView) findViewById(R.id.tv_activity_integral_confirm);
        mTvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_activity_integral_confirm:

                break;
        }
    }
}
