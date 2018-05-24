package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IChoiceCouponContract;
import com.ypshengxian.daojia.mvp.presenter.ChoiceCouponPresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 选择优惠券
 */
public class ChoiceCouponActivity extends BaseMVPYpFreshActicity<IChoiceCouponContract.View, ChoiceCouponPresenter> implements IChoiceCouponContract.Presenter, View.OnClickListener {

	private RecyclerView mRv;
	/**
	 * 确定
	 */
	private TextView mTvConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public int getContentView() {
		return R.layout.activity_chioce_coupon;
	}

	@Override
	protected ChoiceCouponPresenter createPresenter() {
		return new ChoiceCouponPresenter();
	}

	@Override
	public void requestData(String userName, String passWord) {

	}

	private void initView() {
		TitleUtils.setTitleBar(this,this.getString(R.string.chioce_coupon_title_text));
		mRv = (RecyclerView) findViewById(R.id.rv_activity_chioce_coupon);
		mRv.setOnClickListener(this);
		mTvConfirm = (TextView) findViewById(R.id.tv_activity_chioce_coupon_confirm);
		//item ---- R.layout.item_receive_coupons_rl
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			default:
				break;
			case R.id.rv_activity_chioce_coupon:
				break;
		}
	}
}
