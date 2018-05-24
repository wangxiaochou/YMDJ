package com.ypshengxian.daojia.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.ISettingContract;
import com.ypshengxian.daojia.mvp.presenter.SettingPresenter;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.task.ActivityStack;
import com.ypshengxian.daojia.utils.AppUtils;
import com.ypshengxian.daojia.utils.FileUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.TitleUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.YpDialog;

import java.io.File;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 设置
 */
public class SettingActivity extends BaseMVPYpFreshActicity<ISettingContract.View, SettingPresenter> implements ISettingContract.View, View.OnClickListener {

    /**
     * 25.0M
     */
    private TextView mTvCache;
    private ImageView mIvClear;
    private RelativeLayout mRlCache;
    private RelativeLayout mRlModifyPw;
    /**
     * 版本号
     */
    private TextView mTvVersion;
    /**
     * 版本更新
     */
    private TextView mTvUpdate;
    private RelativeLayout mRlUpdate;
    private RelativeLayout mRlAboutUs;
    private RelativeLayout mRlContractUs;
    private RelativeLayout mRlBindWx;
    /**
     * 退出登录
     */
    private TextView mTvExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.addActivity(this);
        initView();
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_setting;
    }

    private void initView() {
        TitleUtils.setTitleBar(this, this.getString(R.string.setting_title_text));
        mTvCache = (TextView) findViewById(R.id.tv_activity_setting_cache);
        mIvClear = (ImageView) findViewById(R.id.iv_activity_setting_clear);
        mRlCache = (RelativeLayout) findViewById(R.id.rl_activity_setting_cache);
        mRlModifyPw = (RelativeLayout) findViewById(R.id.rl_activity_setting_modify_pw);
        mTvUpdate = (TextView) findViewById(R.id.tv_activity_setting_update);
        mRlUpdate = (RelativeLayout) findViewById(R.id.rl_activity_setting_update);
        mRlAboutUs = (RelativeLayout) findViewById(R.id.rl_activity_setting_about_us);
        mRlContractUs = (RelativeLayout) findViewById(R.id.rl_activity_setting_contract_us);
        mRlBindWx = (RelativeLayout) findViewById(R.id.rl_activity_setting_bind_wx);
        mTvExit = (TextView) findViewById(R.id.tv_activity_setting_exit);
        mTvVersion = (TextView) findViewById(R.id.tv_activity_setting_update_version);

        mRlCache.setOnClickListener(this);
        mRlModifyPw.setOnClickListener(this);
        mRlUpdate.setOnClickListener(this);
        mRlAboutUs.setOnClickListener(this);
        mRlContractUs.setOnClickListener(this);
        mRlBindWx.setOnClickListener(this);
        mTvExit.setOnClickListener(this);
        File file = Utils.getContext().getCacheDir();
        String s = FileUtils.getFormatSize(file.length());
        mTvCache.setText(s);
        mTvVersion.setText(AppUtils.getAppVersionName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.rl_activity_setting_cache:
                //清除缓存
                //startActivity(new Intent(SettingActivity.this, ApplyAfterSalesActivity.class));
                break;
            case R.id.rl_activity_setting_modify_pw:
                //修改密码
                Bundle bundle = new Bundle();
                bundle.putString(Count.FORGET_OR_REGISTER_TITLE, Count.FORGET_TITLE);
                startActivityEx(RegisterActivity.class, bundle);
                break;
            case R.id.rl_activity_setting_update:
                //版本更新
//				versionUpdateDialog(SettingActivity.this);
                YpDialog updateDialog = new YpDialog(getBaseActivity());
                updateDialog.setTitle("版本更新",R.color.top_bar_title_color,14);
                updateDialog.setMessage("当前已是最新版本！",R.color.top_bar_title_color,14);
                updateDialog.setPositiveButton("确定", true, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog.dismiss();
                    }
                });
                updateDialog.show();
                break;
            case R.id.rl_activity_setting_about_us:
                //关于我们
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                break;
            case R.id.rl_activity_setting_contract_us:
                //联系客服
//				contactServiceDialog(SettingActivity.this);
                YpDialog callDialog = new YpDialog(getBaseActivity());
                callDialog.setTitle("联系客服",R.color.top_bar_title_color,14);
                callDialog.setMessage(ResUtils.getString(R.string.setting_call_phone),R.color.color_theme, 20);
                callDialog.setNegativeButton("取消", v12 -> callDialog.dismiss());
                callDialog.setPositiveButton("拨号", true, v1 -> {
                    RxPermissions rxPermissions = new RxPermissions(SettingActivity.this);
                    rxPermissions.request(Manifest.permission.CALL_PHONE)
                            .subscribe((Boolean granted) -> {
                                if (granted) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + ResUtils.getString(R.string.setting_call_phone));
                                    intent.setData(data);
                                    if (ActivityCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ToastUtils.showLongToast("请开启拨打电话权限！");
                                        return;
                                    }
                                    SettingActivity.this.startActivity(intent);
                                } else {
                                    ToastUtils.showLongToast("您拒绝权限了拨打电话权限，请设置");
                                }
                                callDialog.dismiss();
                            });
                });
                callDialog.show();
                break;
            case R.id.rl_activity_setting_bind_wx:
                //绑定微信

                break;
            case R.id.tv_activity_setting_exit:
                //退出
                YPPreference.newInstance().setLogin(false);
                YPPreference.newInstance().setUserToken(null);
                YPPreference.newInstance().setUserPhone("");
                startActivityEx(LoginActivity.class);
                ActivityStack.finishActivities();
                break;
        }
    }

    @Override
    public void onResponseData(boolean isSuccess, BaseModuleApiResult data) {
        if (isSuccess) {

        }
    }

    /**
     * 版本更新
     *
     * @param context
     */
    private void versionUpdateDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("版本更新");
        builder.setMessage("当前已是最新版本！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    /**
     * 拨打电话
     *
     * @param context
     */
    private void contactServiceDialog(Context context) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_service_dialog, null);
        mBuilder.setView(view);
        TextView mTvCancel = view.findViewById(R.id.tv_layout_contact_service_dialog_cancel);
        TextView mTvCall = view.findViewById(R.id.tv_layout_contact_service_dialog_call);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
        mTvCall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                RxPermissions rxPermissions = new RxPermissions((Activity) context);
                rxPermissions.request(Manifest.permission.CALL_PHONE)
                        .subscribe((Boolean granted) -> {
                            if (granted) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                Uri data = Uri.parse("tel:" + ResUtils.getString(R.string.setting_call_phone));
                                intent.setData(data);
                                context.startActivity(intent);
                            } else {
                                ToastUtils.showLongToast("您拒绝权限了拨打电话权限，请设置");
                            }
                            mDialog.dismiss();
                        });

            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }
}
