package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IPurseSafetyContract;
import com.ypshengxian.daojia.mvp.presenter.PurseSafetyPresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 钱包安全
 */
public class PurseSafetyActivity extends BaseMVPYpFreshActicity<IPurseSafetyContract.View, PurseSafetyPresenter> implements IPurseSafetyContract.Presenter {

	private RelativeLayout mRlPurseSafety;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public int getContentView() {
		return R.layout.activity_purse_safety;
	}

	@Override
	protected PurseSafetyPresenter createPresenter() {
		return new PurseSafetyPresenter();
	}

	@Override
	public void requestData() {

	}

	private void initView() {
		TitleUtils.setTitleBar(this,this.getString(R.string.purse_safety_title_text));
		mRlPurseSafety = (RelativeLayout) findViewById(R.id.rl_activity_purse_safety);

		mRlPurseSafety.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityEx(SetPaymentPasswordActivity.class);
			}
		});

	}
}
