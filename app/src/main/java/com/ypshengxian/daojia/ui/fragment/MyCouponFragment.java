package com.ypshengxian.daojia.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshFragment;
import com.ypshengxian.daojia.mvp.contract.IMyCouponContract;
import com.ypshengxian.daojia.mvp.presenter.MyCouponPresenter;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 优惠券
 */
public class MyCouponFragment extends BaseMVPYpFreshFragment<IMyCouponContract.View, MyCouponPresenter> implements IMyCouponContract.Presenter {

	public static final int S_TYPE_ENABLE = 0;//可用
	public static final int S_TYPE_UNENABLE = 1;//不可用

	private int mType = 0;
	private RecyclerView mRv;

	/**
	 * 单例
	 *
	 * @param type 类型0可用优惠券1历史优惠券
	 * @return
	 */
	public static MyCouponFragment newInstence(int type) {
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		MyCouponFragment myCouponFragment = new MyCouponFragment();
		myCouponFragment.setArguments(bundle);
		return myCouponFragment;
	}

	@Override
	protected MyCouponPresenter createPresenter() {
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			Bundle bundle = getArguments();
			mType = bundle.getInt("type");
		}
	}

	@Override
	public int getContentView() {
		return R.layout.fragment_my_coupon;
	}

	@Override
	public void initView() {
		//item ---- R.layout.item_receive_coupons_rl
		mRv = (RecyclerView) findViewById(R.id.rv_fragment_my_coupon);
	}

	@Override
	public void requestData() {

	}
}
