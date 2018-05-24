package com.ypshengxian.daojia.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.view.IosPopupDialog;

import java.lang.ref.WeakReference;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * 下载工具类
 *
 * @author Yan
 * @date 2017-11-30
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BubbleDownUtils {
    /** 静态引用 */
    private static BubbleDownUtils sInstance;

    /**
     * 私有化构造
     */
    private BubbleDownUtils() {
    }

    /**
     * 单例
     *
     * @return this
     */
    public static BubbleDownUtils newInstance() {
        if (null == sInstance) {
            synchronized (BubbleDownUtils.class) {
                if (null == sInstance) {
                    sInstance = new BubbleDownUtils();
                }
            }
        }

        return sInstance;
    }

    /**
     * 是否跟新APP
     *
     * @param newVersion 新的版本号
     *
     * @return 是|否
     */
    private boolean isCanUpdateApp(String newVersion) {
        String nowVersion = AppUtils.getAppVersionName();
        return !TextUtils.equals(nowVersion, newVersion);
    }

    /**
     * 更新APP
     */
    public void updateApp(BaseActivity activity, String newVersion, boolean isConstraint, String message) {
        if (isCanUpdateApp(newVersion)) {
            showDownLoadDialog(activity,isConstraint,message);
        }
    }

    /**
     * 显示更新dialog
     *
     * @param activity 页面上下文
     * @param isConstraint 是否强制
     */
    public void showDownLoadDialog(final BaseActivity activity, final boolean isConstraint,String message) {
        final WeakReference<BaseActivity> activityWeak = new WeakReference<BaseActivity>(activity);
        final IosPopupDialog dialog = new IosPopupDialog(activity);
        dialog.setCanceledOnTouchOutside(!isConstraint);
        dialog.setCancelable(!isConstraint);
        dialog.setTextColor(R.id.tv_title, ResUtils.getColor(R.color.color_black));
        dialog.setTextColor(R.id.tv_message, ResUtils.getColor(R.color.color_black));
        dialog.setTitle("新版本更新说明")
                .setMessage(message)
                .setPositiveButton("确定", true, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       downLoadApk(activityWeak,isConstraint);
                    }
                });
        if (!isConstraint) {
            dialog.setNegativeButton("稍后再说", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();

    }


    /**
     * 下载文件
     */
    private void downLoadApk(WeakReference<BaseActivity> activityWeak, boolean isConstraint) {
        final BaseActivity activity = activityWeak.get();
        final IosPopupDialog downDialog = new IosPopupDialog(activity);
        downDialog.setCanceledOnTouchOutside(false);
        downDialog.setCancelable(false);
        downDialog.setCenterView(R.layout.item_update_progress_dialog);
        final MaterialProgressBar bar = (MaterialProgressBar) downDialog.findViewById(R.id.mpb_update_app_progress);
        final TextView progress = (TextView) downDialog.findViewById(R.id.tv_update_app_progress);
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl()
                .create(Count.WORK_URL+"paopaopay.apk")
                .setPath(FileDownloadUtils.getDefaultSaveRootPath() + "/" + ResUtils.getString(R.string.app_name))
                .setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int percent = (int) ((float) soFarBytes / totalBytes * 100);
                        bar.setProgress(percent);
                        progress.setText(String.format(ResUtils.getString(R.string.update_progress_text),percent,"%"));
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        downDialog.cancel();
                        AppUtils.installApp(activity, task.getPath());

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });
        baseDownloadTask.start();

        downDialog.show();

    }


}
