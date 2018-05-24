package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IReceiveCouponContract;
import com.ypshengxian.daojia.mvp.presenter.ReceiveCouponPresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 领券中心
 */
public class ReceiveCouponsActivity extends BaseMVPYpFreshActicity<IReceiveCouponContract.View, ReceiveCouponPresenter> implements IReceiveCouponContract.Presenter {

	private RecyclerView mRl;
	private SwipeRefreshLayout mSrlRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public int getContentView() {
		return R.layout.activity_receive_coupons;
	}

	@Override
	protected ReceiveCouponPresenter createPresenter() {
		return new ReceiveCouponPresenter();
	}

	@Override
	public void requestData() {

	}

	private void initView() {
		TitleUtils.setTitleBar(this, this.getString(R.string.receive_coupons_title_text));
		mRl = (RecyclerView) findViewById(R.id.rl_activity_receive_coupons);
		mSrlRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_activity_receive_coupons_refresh);

		//item ---- R.layout.item_receive_coupons_rl
	}
}
