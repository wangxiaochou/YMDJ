package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.ISpecialPartyContract;
import com.ypshengxian.daojia.mvp.presenter.SpecialPartyPresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 专题活动
 */
public class SpecialPartyActivity extends BaseMVPYpFreshActicity<ISpecialPartyContract.View, SpecialPartyPresenter> implements ISpecialPartyContract.Presenter {

	private RecyclerView mRv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public int getContentView() {
		return R.layout.activity_special_party;
	}

	@Override
	protected SpecialPartyPresenter createPresenter() {
		return new SpecialPartyPresenter();
	}

	@Override
	public void requestData() {

	}

	private void initView() {
		TitleUtils.setTitleBar(this,this.getString(R.string.special_party_title_text));
		mRv = (RecyclerView) findViewById(R.id.rv_activity_special_party);

//		item     R.layout.item_special_party_rv

	}
}
