package com.ypshengxian.daojia.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.mvp.contract.IStoreDetailsContract;
import com.ypshengxian.daojia.mvp.presenter.StoreDetailsPresenter;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.ShopSmallBean;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.YpDialog;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 门店详情
 */
public class StoreDetailsActivity extends BaseMVPYpFreshActicity<IStoreDetailsContract.View, StoreDetailsPresenter> implements IStoreDetailsContract.View, View.OnClickListener {

    private ImageView mIvDetails;
    private TextView mTvName;
    private TextView mTvTime;
    private TextView mTvDistance;
    /**
     * 地图
     */
    private RelativeLayout mRlMap;
    /**
     * 导航
     */
    private TextView mTvNavigation;
    private RelativeLayout mRlNavigation;
    /**
     * 联系
     */
    private TextView mTvContact;
    private RelativeLayout mRlContact;

    private ShopSmallBean shopSmallBean;

    @Override
    public void onResponseData(boolean isSuccess, BaseModuleApiResult data) {

    }

    @Override
    protected StoreDetailsPresenter createPresenter() {
        return new StoreDetailsPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_store_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        TitleUtils.setTitleBar(this, R.string.store_details_title);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            shopSmallBean = bundle.getParcelable(Count.STORE_DETAILS);
        }

        mIvDetails = (ImageView) findViewById(R.id.iv_activity_store_details);
        mTvName = (TextView) findViewById(R.id.tv_activity_store_name);
        mTvTime = (TextView) findViewById(R.id.tv_activity_store_time);
        mTvDistance = (TextView) findViewById(R.id.tv_activity_store_distance);
        mRlMap = (RelativeLayout) findViewById(R.id.rl_activity_store_map);
        mRlMap.setOnClickListener(this);
        mTvNavigation = (TextView) findViewById(R.id.tv_activity_store_navigation);
        mRlNavigation = (RelativeLayout) findViewById(R.id.rl_activity_store_navigation);
        mRlNavigation.setOnClickListener(this);
        mTvContact = (TextView) findViewById(R.id.tv_activity_store_contact);
        mRlContact = (RelativeLayout) findViewById(R.id.rl_activity_store_contact);
        mRlContact.setOnClickListener(this);
        if (null != shopSmallBean) {
            initData();
        }
    }

    private void initData() {
        LoaderFactory.getLoader().loadResource(mIvDetails, R.drawable.img_shop_success);
        mTvName.setText(shopSmallBean.name);
        mTvTime.setText(String.format(ResUtils.getString(R.string.store_details_time), "08：00-19：00"));
        mTvDistance.setText(String.format(ResUtils.getString(R.string.store_details_distance), shopSmallBean.distance / 1000));
        mTvNavigation.setText("重庆市沙坪坝区大学城南路28号附225号天蓝水蓝蓝小区物管办公室旁");
        mTvContact.setText(ResUtils.getString(R.string.setting_call_phone));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.rl_activity_store_map:

                break;
            case R.id.rl_activity_store_navigation:

                break;
            case R.id.rl_activity_store_contact:
                YpDialog callDialog = new YpDialog(getBaseActivity());
                callDialog.setTitle("联系客服", R.color.top_bar_title_color, 14);
                callDialog.setMessage(ResUtils.getString(R.string.setting_call_phone), R.color.color_theme, 20);
                callDialog.setNegativeButton("取消", v12 -> callDialog.dismiss());
                callDialog.setPositiveButton("拨号", true, v1 -> {
                    RxPermissions rxPermissions = new RxPermissions(StoreDetailsActivity.this);
                    rxPermissions.request(Manifest.permission.CALL_PHONE)
                            .subscribe((Boolean granted) -> {
                                if (granted) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + ResUtils.getString(R.string.setting_call_phone));
                                    intent.setData(data);
                                    if (ActivityCompat.checkSelfPermission(StoreDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ToastUtils.showLongToast("请开启拨打电话权限！");
                                        return;
                                    }
                                    StoreDetailsActivity.this.startActivity(intent);
                                } else {
                                    ToastUtils.showLongToast("您拒绝权限了拨打电话权限，请设置");
                                }
                                callDialog.dismiss();
                            });
                });
                callDialog.show();
                break;
        }
    }
}
