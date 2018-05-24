package com.ypshengxian.daojia.ui.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IAddAddressContract;
import com.ypshengxian.daojia.mvp.presenter.AddAddressPresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 添加地址
 */
public class AddAddressActivity extends BaseMVPYpFreshActicity<IAddAddressContract.View, AddAddressPresenter> implements IAddAddressContract.View, View.OnClickListener {

    /**
     * 保存
     */
    private TextView mTvSave;
    private ImageView mIvNoticeClose;
    private RelativeLayout mRlNotice;
    /**
     * 输入姓名
     */
    private EditText mEtName;
    /**
     * 输入手机号
     */
    private EditText mEtPhone;
    /**
     * 重庆市
     */
    private EditText mEtAddress;
    /**
     * 大学城店
     */
    private TextView mTvChooseStore;
    /**
     * 请选择
     */
    private TextView mTvChooseHousing;
    /**
     * 如：1栋18-7号
     */
    private EditText mEtAddressDetail;

    @Override
    public void onResponseData(boolean isSuccess, Object data) {

    }

    @Override
    protected AddAddressPresenter createPresenter() {
        return new AddAddressPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.add_address_title_text));
        mTvSave = (TextView) findViewById(R.id.tv_activity_add_address_save);
        mTvSave.setOnClickListener(this);
        mIvNoticeClose = (ImageView) findViewById(R.id.iv_activity_add_address_notice_close);
        mIvNoticeClose.setOnClickListener(this);
        mRlNotice = (RelativeLayout) findViewById(R.id.rl_activity_add_address_notice);
        mEtName = (EditText) findViewById(R.id.et_activity_add_address_name);
        mEtPhone = (EditText) findViewById(R.id.et_activity_add_address_phone);
        mEtAddress = (EditText) findViewById(R.id.et_activity_add_address_address);
        mTvChooseStore = (TextView) findViewById(R.id.tv_activity_add_address_choose_store);
        mTvChooseStore.setOnClickListener(this);
        mTvChooseHousing = (TextView) findViewById(R.id.tv_activity_add_address_choose_housing);
        mTvChooseHousing.setOnClickListener(this);
        mEtAddressDetail = (EditText) findViewById(R.id.et_activity_add_address_address_detail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_activity_add_address_save:
                break;
            case R.id.iv_activity_add_address_notice_close:
                mRlNotice.setVisibility(View.GONE);
                break;
            case R.id.tv_activity_add_address_choose_store:
                startActivityEx(ChooseStoreActivity.class);
                break;
            case R.id.tv_activity_add_address_choose_housing:
                startActivityEx(ChooseHousingActivity.class);
                break;
        }
    }
}
