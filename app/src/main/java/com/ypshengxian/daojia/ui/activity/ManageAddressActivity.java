package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IManageAddressContract;
import com.ypshengxian.daojia.mvp.presenter.ManageAddressPresenter;
import com.ypshengxian.daojia.utils.TitleUtils;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 管理地址
 */
public class ManageAddressActivity extends BaseMVPYpFreshActicity<IManageAddressContract.View, ManageAddressPresenter> implements IManageAddressContract.View, View.OnClickListener {
    /**
     * 新增收货地址
     */
    private TextView mTvAddAddress;
    private RecyclerView mRv;

    @Override
    public void onResponseData(boolean isSuccess, Object data) {

    }

    @Override
    protected ManageAddressPresenter createPresenter() {
        return new ManageAddressPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_manage_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.manage_address_title_text));

        mTvAddAddress = (TextView) findViewById(R.id.tv_activity_manage_address_add_address);
        mTvAddAddress.setOnClickListener(this);
        mRv = (RecyclerView) findViewById(R.id.rv_activity_manage_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_activity_manage_address_add_address:
                startActivityEx(AddAddressActivity.class);
                break;
        }
    }
}
