package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IRechargeSuccessContract;
import com.ypshengxian.daojia.mvp.presenter.RechargeSuccessPresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 充值成功
 */
public class RechargeSuccessActivity extends BaseMVPYpFreshActicity<IRechargeSuccessContract.View, RechargeSuccessPresenter> implements IRechargeSuccessContract.View, View.OnClickListener {


    /**
     * 返回首页
     */
    private TextView mTvStateBack;

    @Override
    public void onResponseData(boolean isSuccess, Object data) {

    }

    @Override
    protected RechargeSuccessPresenter createPresenter() {
        return new RechargeSuccessPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_recharge_success;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setContentView(R.layout.activity_recharge_success);
    }

    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.recharge_success_title_text));
        mTvStateBack = (TextView) findViewById(R.id.tv_activity_recharge_success_state_back);
        mTvStateBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_activity_recharge_success_state_back:
                finish();
                break;
        }
    }
}
