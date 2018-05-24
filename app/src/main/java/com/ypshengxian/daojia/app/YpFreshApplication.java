package com.ypshengxian.daojia.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.networkbench.agent.impl.NBSAppAgent;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.ypshengxian.daojia.BuildConfig;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BaseApplication;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.loader.LoaderFactory;
import com.ypshengxian.daojia.loader.Options;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.ui.activity.LoginActivity;
import com.ypshengxian.daojia.utils.AppUtils;
import com.ypshengxian.daojia.utils.ConvertUtils;
import com.ypshengxian.daojia.utils.LogUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.Utils;
import com.ypshengxian.daojia.view.SimpleTitleBarBuilder;

import java.net.Proxy;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class YpFreshApplication extends BaseApplication {

	static {
		//设置全局的Header构建器
		SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
			@NonNull
			@Override
			public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
				//全局设置主题颜色
				layout.setPrimaryColorsId(R.color.color_theme, R.color.color_white);
				//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
				return new ClassicsHeader(context);
			}
		});
		//设置全局的Footer构建器
		SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
			@NonNull
			@Override
			public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
				//指定为经典Footer，默认是 BallPulseFooter
				return new ClassicsFooter(context).setDrawableSize(20);
			}
		});
	}
	@Override
	public void onCreate() {
		super.onCreate();

		// 日志初始化
		// TODO: lwc 2017/11/30 打包的时候一定要检查是否输出Log
		LogUtils.init(AppUtils.isAppDebug(), false, 'v', "YPDJ");
		// 文件下载器
		FileDownloader.init(this, new DownloadMgrInitialParams.InitCustomMaker()
				.connectionCreator(new FileDownloadUrlConnection
						.Creator(new FileDownloadUrlConnection.Configuration()
						.connectTimeout(10_000)
						.readTimeout(10_000)
						.proxy(Proxy.NO_PROXY)
				)));

		// 初始化标题栏
		initSimpleTitleBarOptions();

		// 图片加载初始化
		initImageLoader();

		// 初始化网络
		initNetwork();


		setCheckLoginInterceptor(new BaseActivity.ICheckLoginInterceptor() {
			@Override
			public boolean checkLoginBeforeAction(String actionAfterLogin, Bundle data) {
				if(!YPPreference.newInstance().getLogin()){
					Intent intent=new Intent();
					intent.setClass(Utils.getContext(),LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Utils.getContext().startActivity(intent);
				}

				return YPPreference.newInstance().getLogin();
			}
		});

		NBSAppAgent.setLicenseKey(BuildConfig.apm+ Count.APM).withLocationServiceEnabled(true).start(Utils.getContext());
	}


	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

	}


	/**
	 * 初始化图片加载器
	 */
	private void initImageLoader() {
		LoaderFactory.getLoader().init(this, new Options(R.drawable.icon_loading, R.drawable.icon_fail));
	}


	/**
	 * 初始化网络
	 */
	private void initNetwork() {

	}

	/**
	 * 初始化标题栏的参数
	 */
	private void initSimpleTitleBarOptions() {
		SimpleTitleBarBuilder.DefaultOptions options = new SimpleTitleBarBuilder.DefaultOptions();

		// 背景
		options.backgroundColor = ResUtils.getColor(R.color.color_theme);
		options.titleBarHeight = getResources().getDimensionPixelSize(R.dimen.top_bar_height);

		// 左边
		options.leftTextColor = ResUtils.getColor(R.color.top_bar_text_color);
		options.leftTextSize = ConvertUtils.px2sp(getResources().getDimensionPixelSize(R.dimen.font_small));
		options.leftBackDrawable = R.drawable.icon_shop_ge_back;

		// 右边
		options.rightTextColor = ResUtils.getColor(R.color.top_bar_text_color);
		options.rightTextSize = ConvertUtils.px2sp(getResources().getDimensionPixelSize(R.dimen.font_small));

		// 标题
		options.titleColor = ResUtils.getColor(R.color.top_bar_title_color);
		options.titleSize = ConvertUtils.px2sp(getResources().getDimensionPixelSize(R.dimen.font_huge));

		// 阴影
		options.shadowHeight = (int) ResUtils.getDimensionPixelSize(R.dimen.top_bar_shadow_height);
		options.shadowColor = ResUtils.getColor(R.color.top_bar_shadow_color);

		// 6.0一下系统不使用沉浸式状态栏
		options.immersive = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		SimpleTitleBarBuilder.initDefaultOptions(options);
	}
}
