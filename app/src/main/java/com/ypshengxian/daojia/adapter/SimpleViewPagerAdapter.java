package com.ypshengxian.daojia.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 简单的ViewPager适配器
 *
 * @author mos
 * @date 2017.03.01
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class SimpleViewPagerAdapter extends PagerAdapter {
    /** View容器 */
    private List<View> mViewContainer;

    /**
     * 构造函数
     *
     * @param viewContainer view容器
     */
    public SimpleViewPagerAdapter(List<View> viewContainer) {
        mViewContainer = viewContainer;
    }

    @Override
    public int getCount() {

        return mViewContainer.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewContainer.get(position));

        return mViewContainer.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewContainer.get(position));
    }
}
