package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.event.OrderEvent;
import com.ypshengxian.daojia.mvp.contract.IBindingPhoneContract;
import com.ypshengxian.daojia.mvp.presenter.BindingPhonePresenter;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.SmsBean;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.task.ActivityStack;
import com.ypshengxian.daojia.utils.RegexUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.ExtendEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 绑定手机号
 */
public class BindingPhoneActivity extends BaseMVPYpFreshActicity<IBindingPhoneContract.View, BindingPhonePresenter> implements
		IBindingPhoneContract.View, View.OnClickListener {
	/** 手机号码 */
	private ExtendEditText mTvPhone;
	/** 验证码 */
	private ExtendEditText mTvCode;
	/**
	 * 获取验证码
	 */
	private TextView mTvGetCode;
	/**
	 * 完成
	 */
	private TextView mTvConfirm;
	/** 手机号码 */
	private String mPhone;
	/** 验证码 */
	private String mCode;
	private String smsToken;
	private String mUserPhone;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		ActivityStack.addActivity(this);
	}

	@Override
	public int getContentView() {
		return R.layout.activity_binding_phone;
	}

	@Override
	protected BindingPhonePresenter createPresenter() {
		return new BindingPhonePresenter();
	}

	/**
	 * 初始化
	 *
	 */
	private void initView() {
		TitleUtils.setTitleBar(this, this.getString(R.string.binding_phone_title_text));
		mTvPhone = (ExtendEditText) findViewById(R.id.tv_activity_binding_phone_phone);
		mTvCode = (ExtendEditText) findViewById(R.id.tv_activity_binding_phone_code);
		mTvGetCode = (TextView) findViewById(R.id.tv_activity_binding_phone_get_code);
		mTvConfirm = (TextView) findViewById(R.id.tv_activity_binding_phone_confirm);

		mTvGetCode.setOnClickListener(this);
		mTvConfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_activity_binding_phone_get_code:
				//获取验证码
				mPhone = mTvPhone.getText().toString().trim();
				if (TextUtils.isEmpty(mPhone)) {
					ToastUtils.showShortToast("请输入正确的手机号码");
					return;
				}
				if(!RegexUtils.isMobileExact(mPhone)){
					ToastUtils.showShortToast("请输入正确的手机号码");
					return;
				}
				Map<String, String> map = new WeakHashMap<String, String>();
				map.put("phoneNumber", mPhone);
				map.put("smsType", Count.SMS_BIND);
				YpCache.setMap(map);
				mPresenter.sendSms(map);
				break;
			case R.id.tv_activity_binding_phone_confirm:
				//确定
				mPhone = mTvPhone.getText().toString().trim();
				mCode = mTvCode.getText().toString().trim();
				if (TextUtils.isEmpty(mPhone)){
					ToastUtils.showShortToast("请输入正确的手机号码");
					return;
				}
				if(!RegexUtils.isMobileExact(mPhone)){
					ToastUtils.showShortToast("请输入正确的手机号码");
					return;
				}
				if(TextUtils.isEmpty(mCode)){
					ToastUtils.showShortToast("请输入正确的验证码");
					return;
				}
				mUserPhone=mPhone;
				mPresenter.bindPhone(mPhone,mCode,smsToken);
				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	public void onSendSms(boolean isSuccess, SmsBean data) {
		if(isSuccess){
			smsToken= data.smsToken;
			mTvPhone.setEnabled(false);
			Observable.interval(0, 1, TimeUnit.SECONDS)
					.compose(getBaseActivity().applySchedulers(ActivityEvent.DESTROY))
					.take(Count.COUNTDOWN_TIME)
					.subscribe(new Action1<Long>() {
						@Override
						public void call(Long aLong) {
							int remain = Count.COUNTDOWN_TIME - 1 - aLong.intValue();
							if (remain > 0) {
								mTvGetCode.setEnabled(false);
								mTvGetCode.setBackgroundResource(R.drawable.shape_gray_rectangle_soild_bg);
								mTvGetCode.setTextColor(ResUtils.getColor(R.color.color_gray_login_hint_text));
								mTvGetCode.setText("获取验证码(" + remain + ")");
							} else {
								mTvGetCode.setEnabled(true);
								mTvGetCode.setBackgroundResource(R.drawable.shape_theme_color_bg);
								mTvGetCode.setTextColor(ResUtils.getColor(R.color.color_white));
								mTvGetCode.setText("获取验证码");
							}
						}
					});
		}

	}

	@Override
	public void onBindPhone(boolean isSuccess, BaseModuleApiResult data) {
		if (isSuccess) {
			YPPreference.newInstance().setUserPhone(mUserPhone);
			ToastUtils.showShortToast("绑定电话号码成功");
			ActivityStack.finishActivities();
			EventBus.getDefault().post(new OrderEvent());
		}
	}
}
