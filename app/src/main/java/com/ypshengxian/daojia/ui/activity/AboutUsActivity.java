package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshActivity;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 关于我们
 */
public class AboutUsActivity extends BaseYpFreshActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		TitleUtils.setTitleBar(this, this.getString(R.string.about_us_title_text));
	}

	@Override
	public int getContentView() {
		return R.layout.activity_about_us;
	}
}
