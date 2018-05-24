package com.ypshengxian.daojia.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Service基类
 *
 * @author mos
 * @date 2017.01.23
 * @note 1. 项目中所有子类必须继承自此基类
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class BaseService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return onBind(intent, 0);
    }

    /**
     * onBind回调
     *
     * @param intent 参考回调文档说明
     * @param flag 标志(暂未使用)
     * @return 参考回调文档说明
     */
    public abstract IBinder onBind(Intent intent, int flag);
}
