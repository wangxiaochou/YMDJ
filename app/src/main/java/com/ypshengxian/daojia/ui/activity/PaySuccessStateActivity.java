package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshActivity;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 支付成功/失败的状态
 */
public class PaySuccessStateActivity extends BaseYpFreshActivity implements View.OnClickListener {

    private ImageView mIvState;
    private ImageView mIvHint;
    /**
     * 购买成功
     */
    private TextView mTvState;
    /**
     * 您可以在我的订单随时查看订单情况
     */
    private TextView mTvStateDescribe;
    /**
     * 回到首页
     */
    private TextView mTvStateBack;
    /**
     * 查看个人订单
     */
    private TextView mTvStateViewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_pay_success_state;
    }

    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.pay_success_state_title_text));
        mIvState = (ImageView) findViewById(R.id.iv_activity_pay_success_state);
        mTvState = (TextView) findViewById(R.id.tv_activity_pay_success_state);
        mTvStateDescribe = (TextView) findViewById(R.id.tv_activity_pay_success_state_describe);
        mTvStateBack = (TextView) findViewById(R.id.tv_activity_pay_success_state_back);
        mTvStateViewOrder = (TextView) findViewById(R.id.tv_activity_pay_success_state_view_order);
        mIvHint = (ImageView) findViewById(R.id.iv_activity_pay_success_state_hint);

        mTvStateBack.setOnClickListener(this);
        mTvStateViewOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_pay_success_state_back:
                finish();
                break;
            case R.id.tv_activity_pay_success_state_view_order:
                startActivityEx(OrderActivity.class);
                finish();
                break;
            default:
                break;
        }
    }
}
