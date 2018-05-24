package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IMyCouponContract;
import com.ypshengxian.daojia.mvp.presenter.MyCouponPresenter;
import com.ypshengxian.daojia.ui.fragment.MyCouponFragment;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 优惠券
 */
public class MyCouponActivity extends BaseMVPYpFreshActicity<IMyCouponContract.View, MyCouponPresenter> implements IMyCouponContract.Presenter {

	/**
	 * 可用优惠券(2
	 */
	/**
	 * 历史优惠券
	 */
	private ViewPager mVp;
	private TabLayout mTl;


	private String[] mTitles = {"可用优惠券", "历史优惠券"};
	private Fragment[] mFragments = {MyCouponFragment.newInstence(MyCouponFragment.S_TYPE_ENABLE),
			MyCouponFragment.newInstence(MyCouponFragment.S_TYPE_UNENABLE)};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public int getContentView() {
		return R.layout.activity_my_coupon;
	}

	@Override
	protected MyCouponPresenter createPresenter() {
		return new MyCouponPresenter();
	}

	@Override
	public void requestData() {

	}

	private void initView() {
		TitleUtils.setTitleBar(this, this.getString(R.string.my_coupon_title_text));
		mVp = (ViewPager) findViewById(R.id.vp_activity_my_coupon);
		mTl = (TabLayout) findViewById(R.id.tl_activity_my_coupon);

		mVp.setAdapter(new MyPageadapter(getSupportFragmentManager()));
		mTl.addTab(mTl.newTab());
		mTl.addTab(mTl.newTab());
		mTl.setupWithViewPager(mVp);
		for (int i = 0; i < mTitles.length; i++) {
			mTl.getTabAt(i).setText(mTitles[i]);
		}
	}

	View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

	private class MyPageadapter extends FragmentPagerAdapter {

		public MyPageadapter(FragmentManager fm) {
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

}
