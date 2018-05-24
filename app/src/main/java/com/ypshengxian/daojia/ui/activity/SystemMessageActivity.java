package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.ISystemMessageContract;
import com.ypshengxian.daojia.mvp.presenter.SystemMessagePresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 系统消息
 */
public class SystemMessageActivity extends BaseMVPYpFreshActicity<ISystemMessageContract.View, SystemMessagePresenter> implements ISystemMessageContract.Presenter {

	private RecyclerView mRv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public int getContentView() {
		return R.layout.activity_system_message;
	}

	@Override
	protected SystemMessagePresenter createPresenter() {
		return new SystemMessagePresenter();
	}

	@Override
	public void requestData() {

	}

	private void initView() {
		TitleUtils.setTitleBar(this,this.getString(R.string.system_message_title_text));
		mRv = (RecyclerView) findViewById(R.id.rv_activity_system_message);

	}
}
